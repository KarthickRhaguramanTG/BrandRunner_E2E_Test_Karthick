package ai.metayb.mobile.utils;

import ai.metayb.mobile.config.ExecutionTarget;
import ai.metayb.mobile.config.MobileConfigManager;
import ai.metayb.mobile.config.MobilePlatform;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;

/**
 * Attaches mobile session context (platform, device, OS version, execution
 * target) as Allure parameters on the current test, so every mobile test in
 * the report carries that information without each test doing it by hand.
 */
public class AllureReportUtils {

    private AllureReportUtils() {
    }

    public static void attachDeviceContext(AppiumDriver driver) {
        MobilePlatform platform = MobileConfigManager.getPlatform();
        ExecutionTarget executionTarget = MobileConfigManager.getExecutionTarget();

        Allure.parameter("Platform", platform.name());
        Allure.parameter("Execution Target", executionTarget.name());
        Allure.parameter("Device", DeviceUtils.getDeviceName(driver));
        Allure.parameter("Platform Version", DeviceUtils.getPlatformVersion(driver));
    }
}
