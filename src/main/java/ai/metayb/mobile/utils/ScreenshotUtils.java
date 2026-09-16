package ai.metayb.mobile.utils;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;

/**
 * Screenshot + page-source capture and Allure attachment for mobile tests.
 * Mirrors the Web side's BaseDriver.attachScreenshot pattern; reimplemented
 * here rather than shared, since BaseDriver is Selenium-web-specific
 * (ChromeOptions, RemoteWebDriver field, etc.) and out of scope to modify.
 */
public class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static byte[] capture(AppiumDriver driver) {
        return driver.getScreenshotAs(OutputType.BYTES);
    }

    public static void attachScreenshot(AppiumDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            Allure.addAttachment(name, new ByteArrayInputStream(capture(driver)));
        } catch (Exception ignored) {
            // Screenshot capture failing must never mask the real test failure.
        }
    }

    public static void attachPageSource(AppiumDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            String pageSource = driver.getPageSource();
            if (pageSource != null) {
                Allure.addAttachment(name, "text/xml", pageSource, ".xml");
            }
        } catch (Exception ignored) {
        }
    }
}
