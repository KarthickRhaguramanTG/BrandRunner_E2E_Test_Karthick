package ai.metayb.ui.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InputReader {

    private static final Properties prop = new Properties();

    static {
        try (InputStream input = InputReader.class
                .getClassLoader()
                .getResourceAsStream("config/input.properties")) {

            if (input == null) {
                throw new RuntimeException("File not found: config/input.properties");
            }

            prop.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load input.properties", e);
        }
    }

    public final String UserPassword = prop.getProperty("UserPassword");
    public final String UserEmail = prop.getProperty("UserEmail");
}
