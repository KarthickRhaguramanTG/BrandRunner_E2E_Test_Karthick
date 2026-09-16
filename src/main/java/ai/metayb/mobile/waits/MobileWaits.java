package ai.metayb.mobile.waits;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Explicit-wait helpers only. No Thread.sleep, no implicit wait - the same
 * anti-pattern that was fixed on the Web side's BaseDriver.waitForElement is
 * deliberately not repeated here.
 */
public class MobileWaits {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    private MobileWaits() {
    }

    public static WebElement waitForVisible(AppiumDriver driver, By locator) {
        return waitForVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForVisible(AppiumDriver driver, By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(AppiumDriver driver, By locator) {
        return waitForClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForClickable(AppiumDriver driver, By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForPresent(AppiumDriver driver, By locator) {
        return waitForPresent(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForPresent(AppiumDriver driver, By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForInvisible(AppiumDriver driver, By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
