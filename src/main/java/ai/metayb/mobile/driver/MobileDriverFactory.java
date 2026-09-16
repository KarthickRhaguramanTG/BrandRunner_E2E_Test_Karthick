package ai.metayb.mobile.driver;

import ai.metayb.mobile.capabilities.CapabilitiesFactory;
import ai.metayb.mobile.config.ExecutionTarget;
import ai.metayb.mobile.config.MobileConfigManager;
import ai.metayb.mobile.config.MobilePlatform;
import ai.metayb.mobile.core.MobileFrameworkException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Creates the right AppiumDriver subtype from configuration alone. Test code
 * never needs to know whether it's talking to an emulator, a physical
 * device, or BrowserStack.
 *
 * This does NOT start an Appium server - it assumes one is already reachable
 * at the configured URL (mobile.properties' appiumServerUrl, default
 * http://127.0.0.1:4723). Starting/managing the Appium server process is
 * intentionally out of scope here (same design choice as the Web side's
 * BaseDriver, which assumes a browser binary is available rather than
 * managing one) - run `appium` yourself before executing mobile tests.
 *
 * Any failure below is wrapped with full diagnostic context and rethrown -
 * never swallowed, never returns null.
 */
public class MobileDriverFactory {

    private MobileDriverFactory() {
    }

    public static AppiumDriver createDriver() {
        MobilePlatform platform = MobileConfigManager.getPlatform();
        ExecutionTarget executionTarget = MobileConfigManager.getExecutionTarget();
        Capabilities capabilities = CapabilitiesFactory.build(platform, executionTarget);
        URL serverUrl = resolveServerUrl(executionTarget);

        AppiumDriver driver;
        try {
            switch (platform) {
                case ANDROID:
                    driver = new AndroidDriver(serverUrl, capabilities);
                    break;
                case IOS:
                    driver = new IOSDriver(serverUrl, capabilities);
                    break;
                default:
                    throw new MobileFrameworkException("Unsupported platform: " + platform);
            }
        } catch (MobileFrameworkException e) {
            throw e;
        } catch (Exception e) {
            throw new MobileFrameworkException(
                    diagnosticMessage(platform, executionTarget, capabilities, serverUrl, e), e);
        }

        if (driver == null) {
            // Structurally unreachable (the switch above always returns or throws), but
            // guarded explicitly per the "never let a null driver reach a test" requirement.
            throw new MobileFrameworkException(
                    diagnosticMessage(platform, executionTarget, capabilities, serverUrl, null)
                            + "\n  (createDriver() produced a null driver with no exception - this should never happen)");
        }
        return driver;
    }

    private static String diagnosticMessage(MobilePlatform platform, ExecutionTarget executionTarget,
                                             Capabilities capabilities, URL serverUrl, Exception cause) {
        StringBuilder message = new StringBuilder("Driver creation failed:\n");
        message.append("  platform = ").append(platform).append('\n');
        message.append("  executionTarget = ").append(executionTarget).append('\n');
        message.append("  appiumServerUrl = ").append(serverUrl).append('\n');
        message.append("  automationName = ").append(capabilities.getCapability("appium:automationName")).append('\n');
        message.append("  deviceName = ").append(capabilities.getCapability("appium:deviceName")).append('\n');
        message.append("  udid = ").append(capabilities.getCapability("appium:udid")).append('\n');
        message.append("  app = ").append(capabilities.getCapability("appium:app")).append('\n');
        if (cause != null) {
            message.append("  originalException = ").append(cause.getClass().getName())
                    .append(": ").append(cause.getMessage());
        }
        return message.toString();
    }

    private static URL resolveServerUrl(ExecutionTarget executionTarget) {
        String url = executionTarget == ExecutionTarget.BROWSERSTACK
                ? MobileConfigManager.get("browserstack.hubUrl", "https://hub-cloud.browserstack.com/wd/hub")
                : MobileConfigManager.getAppiumServerUrl();
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new MobileFrameworkException("Invalid Appium server URL: " + url, e);
        }
    }
}
