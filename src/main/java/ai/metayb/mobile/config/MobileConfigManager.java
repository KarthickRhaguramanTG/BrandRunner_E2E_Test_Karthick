package ai.metayb.mobile.config;

import ai.metayb.mobile.core.MobileFrameworkException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolves mobile configuration from (in priority order) a system property,
 * an environment variable, then the merged mobile/{platform}/browserstack
 * properties files loaded from the classpath.
 *
 * BrowserStack credentials are the one exception: they are read exclusively
 * from environment variables and are never sourced from a properties file.
 */
public class MobileConfigManager {

    private static final Properties mergedProps = new Properties();
    private static final MobilePlatform platform;
    private static final ExecutionTarget executionTarget;

    static {
        loadInto(mergedProps, "config/mobile/mobile.properties");
        loadInto(mergedProps, "config/mobile/locators.properties");

        platform = MobilePlatform.fromString(resolve("platform", mergedProps.getProperty("platform")));
        executionTarget = ExecutionTarget.fromString(resolve("executionTarget", mergedProps.getProperty("executionTarget")));

        String platformFile = platform == MobilePlatform.ANDROID
                ? "config/mobile/android.properties"
                : "config/mobile/ios.properties";
        loadInto(mergedProps, platformFile);

        if (executionTarget == ExecutionTarget.BROWSERSTACK) {
            loadInto(mergedProps, "config/mobile/browserstack.properties");
        }
    }

    private MobileConfigManager() {
    }

    private static void loadInto(Properties target, String classpathResource) {
        try (InputStream input = MobileConfigManager.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (input == null) {
                throw new MobileFrameworkException("Mobile config file not found on classpath: " + classpathResource);
            }
            target.load(input);
        } catch (IOException e) {
            throw new MobileFrameworkException("Failed to load mobile config file: " + classpathResource, e);
        }
    }

    private static String resolve(String key, String fileDefault) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        String env = System.getenv(toEnvName(key));
        if (env != null && !env.isBlank()) {
            return env;
        }
        return fileDefault;
    }

    private static String toEnvName(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

    public static MobilePlatform getPlatform() {
        return platform;
    }

    public static ExecutionTarget getExecutionTarget() {
        return executionTarget;
    }

    public static String get(String key) {
        return resolve(key, mergedProps.getProperty(key));
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new MobileFrameworkException("Required mobile configuration property is missing: " + key);
        }
        return value;
    }

    public static String getAppiumServerUrl() {
        return get("appiumServerUrl", "http://127.0.0.1:4723");
    }

    public static String getBrowserStackUsername() {
        String value = System.getenv("BROWSERSTACK_USERNAME");
        if (value == null || value.isBlank()) {
            throw new MobileFrameworkException("Environment variable BROWSERSTACK_USERNAME is not set");
        }
        return value;
    }

    public static String getBrowserStackAccessKey() {
        String value = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (value == null || value.isBlank()) {
            throw new MobileFrameworkException("Environment variable BROWSERSTACK_ACCESS_KEY is not set");
        }
        return value;
    }
}
