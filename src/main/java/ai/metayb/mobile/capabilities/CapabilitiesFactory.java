package ai.metayb.mobile.capabilities;

import ai.metayb.mobile.config.ExecutionTarget;
import ai.metayb.mobile.config.MobileConfigManager;
import ai.metayb.mobile.config.MobilePlatform;
import ai.metayb.mobile.core.MobileFrameworkException;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

import java.io.File;
import java.time.Duration;
import java.util.Map;

/**
 * Builds Appium capabilities from {@link MobileConfigManager}, per platform
 * and execution target - supporting all five targets: Android Emulator,
 * Android Physical Device, iOS Simulator, iOS Physical Device, and
 * BrowserStack (for either platform). Test code never builds capabilities
 * directly, and nothing here is hardcoded - every value is read through
 * MobileConfigManager, which resolves system property -> env var ->
 * properties file for every key.
 *
 * Local device identity (deviceName/platformVersion from android.properties
 * or ios.properties) and BrowserStack device identity (browserstack.device/
 * browserstack.osVersion) are deliberately kept on separate code paths - a
 * BrowserStack session must never also carry the local emulator/simulator's
 * deviceName/platformVersion, which would send Appium two contradictory
 * device descriptions in the same capabilities set.
 */
public class CapabilitiesFactory {

    private CapabilitiesFactory() {
    }

    public static Capabilities build(MobilePlatform platform, ExecutionTarget executionTarget) {
        switch (platform) {
            case ANDROID:
                return buildAndroid(executionTarget);
            case IOS:
                return buildIos(executionTarget);
            default:
                throw new MobileFrameworkException("Unsupported platform: " + platform);
        }
    }

    private static UiAutomator2Options buildAndroid(ExecutionTarget executionTarget) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName(MobileConfigManager.get("automationName", "UiAutomator2"));
        applyResetAndTimeoutSettings(options);
        applyAndroidAppIdentity(options);

        switch (executionTarget) {
            case LOCAL_EMULATOR:
                applyLocalDeviceIdentity(options);
                applyLocalAppPath(options);
                break;
            case PHYSICAL_DEVICE:
                applyLocalDeviceIdentity(options);
                options.setUdid(MobileConfigManager.getRequired("udid"));
                applyLocalAppPath(options);
                break;
            case BROWSERSTACK:
                applyBrowserStack(options);
                break;
            default:
                throw new MobileFrameworkException(
                        "Execution target " + executionTarget + " is not supported for Android");
        }
        return options;
    }

    private static XCUITestOptions buildIos(ExecutionTarget executionTarget) {
        XCUITestOptions options = new XCUITestOptions();
        options.setAutomationName(MobileConfigManager.get("automationName", "XCUITest"));
        applyResetAndTimeoutSettings(options);
        applyIosAppIdentity(options);

        switch (executionTarget) {
            case LOCAL_SIMULATOR:
                applyLocalDeviceIdentity(options);
                applyLocalAppPath(options);
                break;
            case PHYSICAL_DEVICE:
                applyLocalDeviceIdentity(options);
                options.setUdid(MobileConfigManager.getRequired("udid"));
                applyLocalAppPath(options);
                break;
            case BROWSERSTACK:
                applyBrowserStack(options);
                break;
            default:
                throw new MobileFrameworkException(
                        "Execution target " + executionTarget + " is not supported for iOS");
        }
        return options;
    }

    /** deviceName/platformVersion for a LOCALLY addressed device (emulator, simulator, or a USB/network physical device). */
    private static void applyLocalDeviceIdentity(MutableCapabilities options) {
        options.setCapability("appium:deviceName", MobileConfigManager.getRequired("deviceName"));
        options.setCapability("appium:platformVersion", MobileConfigManager.getRequired("platformVersion"));
    }

    private static void applyAndroidAppIdentity(MutableCapabilities options) {
        String appPackage = MobileConfigManager.get("appPackage");
        if (appPackage != null && !appPackage.isBlank()) {
            options.setCapability("appium:appPackage", appPackage);
        }
        String appActivity = MobileConfigManager.get("appActivity");
        if (appActivity != null && !appActivity.isBlank()) {
            options.setCapability("appium:appActivity", appActivity);
        }
    }

    private static void applyIosAppIdentity(MutableCapabilities options) {
        String bundleId = MobileConfigManager.get("bundleId");
        if (bundleId != null && !bundleId.isBlank()) {
            options.setCapability("appium:bundleId", bundleId);
        }
    }

    private static void applyResetAndTimeoutSettings(MutableCapabilities options) {
        options.setCapability("appium:noReset", Boolean.parseBoolean(MobileConfigManager.get("noReset", "false")));
        options.setCapability("appium:fullReset", Boolean.parseBoolean(MobileConfigManager.get("fullReset", "false")));
        options.setCapability("appium:newCommandTimeout",
                Duration.ofSeconds(Long.parseLong(MobileConfigManager.get("newCommandTimeoutSeconds", "120"))));
    }

    /** A local file path for the app under test - only meaningful when Appium and the device/emulator share a filesystem. Not used for BrowserStack. */
    private static void applyLocalAppPath(MutableCapabilities options) {
        String appPath = MobileConfigManager.get("appPath");
        if (appPath != null && !appPath.isBlank()) {
            options.setCapability("appium:app", new File(appPath).getAbsolutePath());
        }
    }

    /**
     * BrowserStack device identity + credentials + app, entirely separate
     * from the local deviceName/platformVersion/appPath keys above.
     * Credentials come exclusively from environment variables (see
     * MobileConfigManager.getBrowserStackUsername/AccessKey) - never from a
     * properties file.
     */
    private static void applyBrowserStack(MutableCapabilities options) {
        options.setCapability("bstack:options", Map.of(
                "userName", MobileConfigManager.getBrowserStackUsername(),
                "accessKey", MobileConfigManager.getBrowserStackAccessKey(),
                "deviceName", MobileConfigManager.getRequired("browserstack.device"),
                "osVersion", MobileConfigManager.getRequired("browserstack.osVersion"),
                "projectName", MobileConfigManager.get("browserstack.project", "Mobile Automation"),
                "buildName", MobileConfigManager.get("browserstack.build", "local-build")
        ));
        String app = MobileConfigManager.get("browserstack.app");
        if (app != null && !app.isBlank()) {
            options.setCapability("appium:app", app);
        }
    }
}
