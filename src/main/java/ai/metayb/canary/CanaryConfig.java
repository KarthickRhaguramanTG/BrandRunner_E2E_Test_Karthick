package ai.metayb.canary;

import ai.metayb.config.ConfigManager;
import ai.metayb.ui.core.DataReader;

import java.util.function.Supplier;

/**
 * Canary-specific configuration, layered strictly on top of the existing
 * mechanisms - it never changes what ConfigManager/DataReader do for the
 * TestNG suite, it only adds a way for CloudWatch's own runtime configuration
 * to take priority when it's present.
 *
 * Resolution order:
 * <pre>
 * API_BASE_URL / API_TENANT env vars  -->  ConfigManager (config.properties)
 *
 * credentials:
 *   CANARY_SECRET_ID set?  -->  AWS Secrets Manager (username optionally
 *                               overridden by CANARY_USERNAME env var)
 *   else                   -->  CANARY_USERNAME / CANARY_PASSWORD env vars
 *   else                   -->  DataReader (credentials.properties) - local
 *                               dev fallback only, exactly what BaseApiTest
 *                               already uses today.
 * </pre>
 *
 * This means a real CloudWatch deployment (which sets CANARY_SECRET_ID) never
 * touches the committed credentials.properties at all, while a local run with
 * no environment configured behaves exactly like the existing test suite.
 */
public final class CanaryConfig {

    private final String baseUri;
    private final String tenant;
    private final String username;
    private final String password;

    private CanaryConfig(String baseUri, String tenant, String username, String password) {
        this.baseUri = baseUri;
        this.tenant = tenant;
        this.username = username;
        this.password = password;
    }

    public static CanaryConfig load() {
        String baseUri = firstNonBlank(() -> System.getenv("API_BASE_URL"), ConfigManager::getApiBaseUrl);
        String tenant = firstNonBlank(() -> System.getenv("API_TENANT"), ConfigManager::getApiTenant);
        if (baseUri == null || baseUri.isBlank()) {
            throw new IllegalStateException("No API base URL configured - set API_BASE_URL or api.base.url");
        }
        if (tenant == null || tenant.isBlank()) {
            throw new IllegalStateException("No API tenant configured - set API_TENANT or api.tenant");
        }

        // firstNonBlank's suppliers are evaluated lazily, one at a time, stopping at the
        // first non-blank result - critical here, since localUsername()/localPassword()
        // read credentials.properties, which the deployed Canary artifact deliberately
        // does NOT include (see pom.xml's canary-jar exclusions). A real deployment sets
        // CANARY_SECRET_ID or CANARY_USERNAME/CANARY_PASSWORD, so that fallback supplier
        // must never actually be invoked there - only its being *unreachable* code for a
        // properly-configured environment matters, not whether the class it touches
        // happens to exist on the classpath.
        String secretId = System.getenv("CANARY_SECRET_ID");
        String username;
        String password;
        if (secretId != null && !secretId.isBlank()) {
            SecretsManagerCredentialResolver.Credentials creds = SecretsManagerCredentialResolver.resolve(secretId);
            username = firstNonBlank(() -> System.getenv("CANARY_USERNAME"), creds::username, CanaryConfig::localUsername);
            password = firstNonBlank(creds::password, CanaryConfig::localPassword);
        } else {
            username = firstNonBlank(() -> System.getenv("CANARY_USERNAME"), CanaryConfig::localUsername);
            password = firstNonBlank(() -> System.getenv("CANARY_PASSWORD"), CanaryConfig::localPassword);
        }

        if (username == null || username.isBlank()) {
            throw new IllegalStateException(
                    "No Canary username configured - set CANARY_USERNAME, a 'username' field in the "
                            + "CANARY_SECRET_ID secret, or apiEmail in credentials.properties for local runs");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "No Canary password configured - set CANARY_PASSWORD, CANARY_SECRET_ID, "
                            + "or apiPassword in credentials.properties for local runs");
        }

        return new CanaryConfig(baseUri, tenant, username, password);
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getTenant() {
        return tenant;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private static String localUsername() {
        return localCredentials().apiEmail;
    }

    private static String localPassword() {
        return localCredentials().apiPassword;
    }

    /**
     * The deployed Canary artifact deliberately excludes config/credentials.properties
     * (see pom.xml's canary-jar exclusions), so DataReader's static initializer throws
     * there by design - reaching this method at all already means every other
     * credential source was blank/unset. Wrapped so that failure surfaces as one clear,
     * actionable line in CloudWatch Logs instead of a bare ExceptionInInitializerError.
     */
    private static DataReader localCredentials() {
        try {
            return new DataReader();
        } catch (Throwable t) {
            Throwable root = t.getCause() != null ? t.getCause() : t;
            throw new IllegalStateException(
                    "No CANARY_USERNAME/CANARY_PASSWORD (or CANARY_SECRET_ID) configured, and no local "
                            + "credentials.properties fallback is available in this environment: " + root.getMessage(), t);
        }
    }

    @SafeVarargs
    private static String firstNonBlank(Supplier<String>... suppliers) {
        for (Supplier<String> supplier : suppliers) {
            String value = supplier.get();
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
