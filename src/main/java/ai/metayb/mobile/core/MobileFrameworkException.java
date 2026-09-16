package ai.metayb.mobile.core;

/**
 * Unchecked exception for mobile framework setup/configuration failures
 * (missing config, bad capabilities, driver creation errors).
 */
public class MobileFrameworkException extends RuntimeException {

    public MobileFrameworkException(String message) {
        super(message);
    }

    public MobileFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
