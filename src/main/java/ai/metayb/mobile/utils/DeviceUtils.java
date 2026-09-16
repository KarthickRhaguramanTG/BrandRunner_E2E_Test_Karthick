package ai.metayb.mobile.utils;

import ai.metayb.mobile.core.MobileFrameworkException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsRotation;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;

/**
 * Device info, orientation, and lock/unlock. Real-device/simulator caveats
 * (GPS, camera, biometrics, battery, sensors) are NOT emulated here - those
 * genuinely differ between emulator/simulator/physical/cloud and are
 * documented rather than faked.
 */
public class DeviceUtils {

    private DeviceUtils() {
    }

    public static Dimension getScreenSize(AppiumDriver driver) {
        return driver.manage().window().getSize();
    }

    public static ScreenOrientation getOrientation(AppiumDriver driver) {
        if (driver instanceof SupportsRotation rotatable) {
            return rotatable.getOrientation();
        }
        throw new MobileFrameworkException("Driver does not support orientation control");
    }

    public static void setOrientation(AppiumDriver driver, ScreenOrientation orientation) {
        if (driver instanceof SupportsRotation rotatable) {
            rotatable.rotate(orientation);
        } else {
            throw new MobileFrameworkException("Driver does not support orientation control");
        }
    }

    public static void lockDevice(AppiumDriver driver) {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.lockDevice();
        } else if (driver instanceof IOSDriver iosDriver) {
            iosDriver.lockDevice();
        } else {
            throw new MobileFrameworkException("Driver does not support device lock");
        }
    }

    public static void unlockDevice(AppiumDriver driver) {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.unlockDevice();
        } else if (driver instanceof IOSDriver iosDriver) {
            iosDriver.unlockDevice();
        } else {
            throw new MobileFrameworkException("Driver does not support device unlock");
        }
    }

    public static String getPlatformName(AppiumDriver driver) {
        return capabilityOrUnknown(driver, "platformName");
    }

    public static String getPlatformVersion(AppiumDriver driver) {
        return capabilityOrUnknown(driver, "platformVersion");
    }

    public static String getDeviceName(AppiumDriver driver) {
        return capabilityOrUnknown(driver, "deviceName");
    }

    private static String capabilityOrUnknown(AppiumDriver driver, String capabilityName) {
        Object value = driver.getCapabilities().getCapability(capabilityName);
        return value != null ? value.toString() : "unknown";
    }
}
