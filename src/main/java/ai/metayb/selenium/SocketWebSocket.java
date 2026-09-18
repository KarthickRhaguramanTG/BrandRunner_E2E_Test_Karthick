package ai.metayb.selenium;

import org.openqa.selenium.remote.http.BinaryMessage;
import org.openqa.selenium.remote.http.CloseMessage;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.Message;
import org.openqa.selenium.remote.http.TextMessage;
import org.openqa.selenium.remote.http.WebSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * A minimal RFC 6455 WebSocket client built on plain {@link Socket} I/O.
 *
 * Selenium's own WebSocket support goes through {@code java.net.http.HttpClient},
 * which cannot even be constructed on this machine (see
 * {@link UrlConnectionHttpClient}'s Javadoc). This class exists solely so that
 * {@code ChromiumDriver}'s constructor - which eagerly opens a DevTools/CDP
 * WebSocket connection whenever the driver reports one, regardless of whether
 * this framework ever calls {@code getDevTools()} - has something that actually
 * connects. Nothing in this framework sends CDP commands, so only enough of the
 * protocol is implemented to complete the handshake and keep the connection
 * alive: text/binary/close frames in both directions, and responding to pings.
 */
class SocketWebSocket implements WebSocket {

    private final Socket socket;
    private final OutputStream out;
    private final Object writeLock = new Object();
    private volatile boolean closed;

    SocketWebSocket(HttpRequest request, WebSocket.Listener listener) throws IOException {
        URI uri = URI.create(request.getUri());
        String host = uri.getHost();
        int port = uri.getPort() == -1 ? ("wss".equals(uri.getScheme()) ? 443 : 80) : uri.getPort();

        this.socket = new Socket(host, port);
        this.out = socket.getOutputStream();
        performHandshake(uri, host, port);
        startReaderThread(listener);
    }

    private void performHandshake(URI uri, String host, int port) throws IOException {
        byte[] keyBytes = new byte[16];
        new SecureRandom().nextBytes(keyBytes);
        String key = Base64.getEncoder().encodeToString(keyBytes);

        String path = uri.getRawPath() == null || uri.getRawPath().isEmpty() ? "/" : uri.getRawPath();
        if (uri.getRawQuery() != null) {
            path = path + "?" + uri.getRawQuery();
        }

        String request = "GET " + path + " HTTP/1.1\r\n"
                + "Host: " + host + ":" + port + "\r\n"
                + "Upgrade: websocket\r\n"
                + "Connection: Upgrade\r\n"
                + "Sec-WebSocket-Key: " + key + "\r\n"
                + "Sec-WebSocket-Version: 13\r\n"
                + "\r\n";
        out.write(request.getBytes(StandardCharsets.US_ASCII));
        out.flush();

        InputStream in = socket.getInputStream();
        String statusLine = readLine(in);
        if (statusLine == null || !statusLine.contains("101")) {
            throw new IOException("WebSocket handshake failed, status line: " + statusLine);
        }
        String acceptHeader = null;
        String line;
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("sec-websocket-accept:")) {
                acceptHeader = line.substring(line.indexOf(':') + 1).trim();
            }
        }
        String expected = expectedAccept(key);
        if (acceptHeader == null || !acceptHeader.equals(expected)) {
            throw new IOException("WebSocket handshake failed: unexpected Sec-WebSocket-Accept");
        }
    }

    private static String expectedAccept(String key) throws IOException {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.US_ASCII));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }

    private static String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        int b;
        int prev = -1;
        while ((b = in.read()) != -1) {
            if (prev == '\r' && b == '\n') {
                byte[] bytes = line.toByteArray();
                return new String(bytes, 0, bytes.length - 1, StandardCharsets.US_ASCII);
            }
            line.write(b);
            prev = b;
        }
        return line.size() == 0 ? null : line.toString(StandardCharsets.US_ASCII);
    }

    private void startReaderThread(WebSocket.Listener listener) {
        Thread reader = new Thread(() -> readFrames(listener), "socket-websocket-reader");
        reader.setDaemon(true);
        reader.start();
    }

    private void readFrames(WebSocket.Listener listener) {
        try {
            InputStream in = socket.getInputStream();
            ByteArrayOutputStream fragmented = new ByteArrayOutputStream();
            int fragmentedOpcode = -1;

            while (!closed) {
                int first = in.read();
                if (first == -1) {
                    break;
                }
                int second = readRequired(in);
                boolean fin = (first & 0x80) != 0;
                int opcode = first & 0x0F;
                boolean masked = (second & 0x80) != 0;
                long length = second & 0x7F;
                if (length == 126) {
                    length = (readRequired(in) << 8) | readRequired(in);
                } else if (length == 127) {
                    length = 0;
                    for (int i = 0; i < 8; i++) {
                        length = (length << 8) | readRequired(in);
                    }
                }
                byte[] maskKey = null;
                if (masked) {
                    maskKey = new byte[4];
                    for (int i = 0; i < 4; i++) {
                        maskKey[i] = (byte) readRequired(in);
                    }
                }
                byte[] payload = new byte[(int) length];
                int offset = 0;
                while (offset < payload.length) {
                    int read = in.read(payload, offset, payload.length - offset);
                    if (read == -1) {
                        throw new IOException("WebSocket stream closed mid-frame");
                    }
                    offset += read;
                }
                if (maskKey != null) {
                    for (int i = 0; i < payload.length; i++) {
                        payload[i] ^= maskKey[i % 4];
                    }
                }

                switch (opcode) {
                    case 0x9: // ping
                        sendFrame(0xA, payload);
                        break;
                    case 0xA: // pong
                        break;
                    case 0x8: // close
                        closed = true;
                        listener.onClose(1000, "");
                        socket.close();
                        return;
                    case 0x0: // continuation
                        fragmented.write(payload);
                        if (fin) {
                            dispatch(listener, fragmentedOpcode, fragmented.toByteArray());
                            fragmented.reset();
                            fragmentedOpcode = -1;
                        }
                        break;
                    default: // text or binary
                        if (fin) {
                            dispatch(listener, opcode, payload);
                        } else {
                            fragmentedOpcode = opcode;
                            fragmented.reset();
                            fragmented.write(payload);
                        }
                }
            }
        } catch (IOException e) {
            if (!closed) {
                listener.onError(e);
            }
        }
    }

    private static void dispatch(WebSocket.Listener listener, int opcode, byte[] payload) {
        if (opcode == 0x1) {
            listener.onText(new String(payload, StandardCharsets.UTF_8));
        } else {
            listener.onBinary(payload);
        }
    }

    private static int readRequired(InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new IOException("WebSocket stream closed mid-frame");
        }
        return b;
    }

    @Override
    public WebSocket send(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                sendFrame(0x1, textMessage.text().getBytes(StandardCharsets.UTF_8));
            } else if (message instanceof BinaryMessage binaryMessage) {
                sendFrame(0x2, binaryMessage.data());
            } else if (message instanceof CloseMessage closeMessage) {
                ByteArrayOutputStream payload = new ByteArrayOutputStream();
                payload.write((closeMessage.code() >> 8) & 0xFF);
                payload.write(closeMessage.code() & 0xFF);
                if (closeMessage.reason() != null) {
                    payload.writeBytes(closeMessage.reason().getBytes(StandardCharsets.UTF_8));
                }
                sendFrame(0x8, payload.toByteArray());
            } else {
                throw new IllegalArgumentException("Unsupported message type: " + message.getClass());
            }
        } catch (IOException e) {
            throw new org.openqa.selenium.WebDriverException(e);
        }
        return this;
    }

    private void sendFrame(int opcode, byte[] payload) throws IOException {
        synchronized (writeLock) {
            out.write(0x80 | opcode); // FIN + opcode
            int maskedLengthByte = 0x80; // client frames are always masked
            if (payload.length < 126) {
                out.write(maskedLengthByte | payload.length);
            } else if (payload.length <= 0xFFFF) {
                out.write(maskedLengthByte | 126);
                out.write((payload.length >> 8) & 0xFF);
                out.write(payload.length & 0xFF);
            } else {
                out.write(maskedLengthByte | 127);
                for (int shift = 56; shift >= 0; shift -= 8) {
                    out.write((int) ((long) payload.length >> shift) & 0xFF);
                }
            }
            byte[] maskKey = new byte[4];
            new SecureRandom().nextBytes(maskKey);
            out.write(maskKey);
            byte[] masked = new byte[payload.length];
            for (int i = 0; i < payload.length; i++) {
                masked[i] = (byte) (payload[i] ^ maskKey[i % 4]);
            }
            out.write(masked);
            out.flush();
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            sendFrame(0x8, new byte[]{0x03, (byte) 0xE8}); // 1000, normal closure
        } catch (IOException ignored) {
            // best-effort close frame; fall through to closing the socket regardless
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
                // nothing more to do
            }
        }
    }
}
