package ai.metayb.mobile.utils;

import ai.metayb.mobile.core.MobileFrameworkException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.time.Duration;

/**
 * App lifecycle actions (launch/terminate/activate/background/reset). Test
 * code calls these against a plain AppiumDriver and stays platform-agnostic;
 * the platform-specific call is resolved here, once.
 */
public class AppLifecycleUtils {

    private AppLifecycleUtils() {
    }

    public static void activateApp(AppiumDriver driver, String appId) {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.activateApp(appId);
        } else if (driver instanceof IOSDriver iosDriver) {
            iosDriver.activateApp(appId);
        } else {
            throw unsupported(driver);
        }
    }

    public static void terminateApp(AppiumDriver driver, String appId) {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.terminateApp(appId);
        } else if (driver instanceof IOSDriver iosDriver) {
            iosDriver.terminateApp(appId);
        } else {
            throw unsupported(driver);
        }
    }

    public static boolean isAppInstalled(AppiumDriver driver, String appId) {
        if (driver instanceof AndroidDriver androidDriver) {
            return androidDriver.isAppInstalled(appId);
        } else if (driver instanceof IOSDriver iosDriver) {
            return iosDriver.isAppInstalled(appId);
        }
        throw unsupported(driver);
    }

    /** Sends the app to the background for the given duration, then Appium restores it to the foreground. */
    public static void runInBackground(AppiumDriver driver, Duration duration) {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.runAppInBackground(duration);
        } else if (driver instanceof IOSDriver iosDriver) {
            iosDriver.runAppInBackground(duration);
        } else {
            throw unsupported(driver);
        }
    }

    /** Terminates then relaunches the app - a simple, reliable reset that works the same on both platforms. */
    public static void resetApp(AppiumDriver driver, String appId) {
        terminateApp(driver, appId);
        activateApp(driver, appId);
    }

    private static MobileFrameworkException unsupported(AppiumDriver driver) {
        return new MobileFrameworkException(
                "App lifecycle actions are not supported for driver type: " + driver.getClass().getSimpleName());
    }
}
