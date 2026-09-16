package ai.metayb.mobile.driver;

import io.appium.java_client.AppiumDriver;

/**
 * Holds the mobile driver per-thread so tests never touch a shared static
 * driver (parallel-safe by construction), separate from BaseDriver's own
 * ThreadLocal used for Web automation.
 */
public class MobileDriverManager {

    private static final ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    private MobileDriverManager() {
    }

    public static void setDriver(AppiumDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static AppiumDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void removeDriver() {
        driverThreadLocal.remove();
    }
}
