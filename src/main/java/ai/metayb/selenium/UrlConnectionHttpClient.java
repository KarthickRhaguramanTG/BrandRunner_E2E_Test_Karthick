package ai.metayb.selenium;

import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpClientName;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.WebSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * A minimal {@link HttpClient} backed by the classic, blocking
 * {@link HttpURLConnection} API instead of {@code java.net.http.HttpClient}.
 *
 * Selenium's only bundled factory since 4.11 ("jdk-http-client") builds a
 * {@code java.net.http.HttpClient}. On some machines that fails at construction:
 * its internal Selector/Pipe wakeup mechanism tries to open a Unix Domain Socket
 * loopback connection (a JDK NIO implementation detail on Windows, confirmed
 * present across every locally available JDK 17/21/24 build - Temurin, Oracle and
 * Microsoft - so it is not specific to one JDK version or vendor), and that
 * connect() call fails with "SocketException: Invalid argument: connect" -
 * apparently blocked by this machine's network/security stack, independent of
 * Selenium, WebDriverManager or the browser (reproduced identically for both
 * Chrome and Edge). HttpURLConnection is old, purely java.io-based blocking I/O
 * and never touches java.nio Selector/Pipe, so it sidesteps the problem entirely.
 *
 * Handles the synchronous request/response exchange used for the W3C WebDriver
 * wire protocol (GET/POST/DELETE) directly over plain HTTP. WebSocket support
 * (openSocket) is delegated to {@link SocketWebSocket} - ChromiumDriver
 * (Chrome/Edge) eagerly opens a DevTools/CDP WebSocket in its constructor
 * whenever the browser reports one, regardless of whether this framework ever
 * calls {@code getDevTools()}, so a real connection has to succeed there too.
 */
public class UrlConnectionHttpClient implements HttpClient {

    private final URI baseUri;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    private UrlConnectionHttpClient(ClientConfig config) {
        this.baseUri = config.baseUri();
        this.connectTimeoutMillis = (int) config.connectionTimeout().toMillis();
        this.readTimeoutMillis = (int) config.readTimeout().toMillis();
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws UncheckedIOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) resolve(request).openConnection();
            connection.setRequestMethod(request.getMethod().toString());
            connection.setConnectTimeout(connectTimeoutMillis);
            connection.setReadTimeout(readTimeoutMillis);
            connection.setInstanceFollowRedirects(true);
            request.forEachHeader(connection::addRequestProperty);

            byte[] body = readAllBytes(request.getContent().get());
            if (body.length > 0) {
                connection.setDoOutput(true);
                try (OutputStream out = connection.getOutputStream()) {
                    out.write(body);
                }
            }

            int status = connection.getResponseCode();
            HttpResponse response = new HttpResponse().setStatus(status);
            connection.getHeaderFields().forEach((name, values) -> {
                if (name != null) {
                    values.forEach(value -> response.addHeader(name, value));
                }
            });

            InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
            response.setContent(Contents.bytes(stream == null ? new byte[0] : readAllBytes(stream)));
            return response;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private URL resolve(HttpRequest request) throws IOException {
        try {
            String uri = request.getUri();
            URI resolved = uri.startsWith("http") ? new URI(uri) : baseUri.resolve(uri);
            StringBuilder withQuery = new StringBuilder(resolved.toString());
            boolean first = !resolved.toString().contains("?");
            for (String name : request.getQueryParameterNames()) {
                for (String value : request.getQueryParameters(name)) {
                    withQuery.append(first ? '?' : '&')
                            .append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                    first = false;
                }
            }
            return new URI(withQuery.toString()).toURL();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        try (in) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        }
    }

    @Override
    public WebSocket openSocket(HttpRequest request, WebSocket.Listener listener) {
        try {
            return new SocketWebSocket(request, listener);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> CompletableFuture<java.net.http.HttpResponse<T>> sendAsyncNative(
            java.net.http.HttpRequest request, java.net.http.HttpResponse.BodyHandler<T> bodyHandler) {
        throw new UnsupportedOperationException(
                "UrlConnectionHttpClient deliberately avoids java.net.http.HttpClient - see class Javadoc");
    }

    @Override
    public <T> java.net.http.HttpResponse<T> sendNative(
            java.net.http.HttpRequest request, java.net.http.HttpResponse.BodyHandler<T> bodyHandler) {
        throw new UnsupportedOperationException(
                "UrlConnectionHttpClient deliberately avoids java.net.http.HttpClient - see class Javadoc");
    }

    @HttpClientName("urlconnection")
    public static class Factory implements HttpClient.Factory {
        @Override
        public HttpClient createClient(ClientConfig config) {
            return new UrlConnectionHttpClient(config);
        }
    }
}
