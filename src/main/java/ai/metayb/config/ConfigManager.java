package ai.metayb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties props = new Properties();

    static {
        loadConfig("config.properties");
    }

    private static void loadConfig(String fileName) {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/" + fileName)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration: " + fileName, e);
        }
    }

    // API Configurations
    public static String getApiBaseUrl() {
        return props.getProperty("api.base.url");
    }

    public static String getApiTenant() {
        return props.getProperty("api.tenant");
    }


    // UI Configurations
    public static String getBrowser() {
        return props.getProperty("ui.browser");
    }

    public static String getBaseUrl() {
        return props.getProperty("ui.url");
    }

    // Performance Configurations
    public static int getPerformanceThreads() {
        return Integer.parseInt(props.getProperty("performance.threads", "50"));
    }
}