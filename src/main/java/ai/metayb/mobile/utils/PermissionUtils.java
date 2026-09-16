package ai.metayb.mobile.utils;

import ai.metayb.mobile.core.MobileFrameworkException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;
import java.util.Map;

/**
 * Runtime permission handling.
 *
 * Android: implemented via Appium/UiAutomator2's "mobile: changePermissions"
 * extension - grant/revoke work reliably with no external tooling.
 *
 * iOS: Appium can reset a permission so its system dialog reappears
 * ("mobile: resetPermission"), but *granting* a permission programmatically
 * is not reliably exposed through Appium/XCUITest - it typically needs
 * simulator-level tooling (`xcrun simctl privacy`, macOS-only) or a
 * pre-authorized capability set at session start. This class documents that
 * limitation rather than pretending full support.
 */
public class PermissionUtils {

    private PermissionUtils() {
    }

    public static void grantAndroidPermission(AppiumDriver driver, String appPackage, String permission) {
        requireAndroid(driver).executeScript("mobile: changePermissions", Map.of(
                "permissions", List.of(permission),
                "appPackage", appPackage,
                "action", "grant"
        ));
    }

    public static void revokeAndroidPermission(AppiumDriver driver, String appPackage, String permission) {
        requireAndroid(driver).executeScript("mobile: changePermissions", Map.of(
                "permissions", List.of(permission),
                "appPackage", appPackage,
                "action", "revoke"
        ));
    }

    /** Resets a permission on iOS so its system dialog reappears on next use. Does NOT grant it - see class docs. */
    public static void resetIosPermission(AppiumDriver driver, String bundleId, String service) {
        requireIos(driver).executeScript("mobile: resetPermission", Map.of(
                "bundleId", bundleId,
                "service", service
        ));
    }

    private static AndroidDriver requireAndroid(AppiumDriver driver) {
        if (driver instanceof AndroidDriver androidDriver) {
            return androidDriver;
        }
        throw new MobileFrameworkException("This permission operation is Android-only");
    }

    private static IOSDriver requireIos(AppiumDriver driver) {
        if (driver instanceof IOSDriver iosDriver) {
            return iosDriver;
        }
        throw new MobileFrameworkException("This permission operation is iOS-only");
    }
}
