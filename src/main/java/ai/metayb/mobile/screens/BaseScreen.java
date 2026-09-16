package ai.metayb.mobile.screens;

import ai.metayb.mobile.waits.MobileWaits;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Common screen behavior (wait-then-find) shared by every screen, so
 * individual screens contain only their own locators and actions - never
 * raw Appium calls or duplicated wait logic.
 */
public abstract class BaseScreen {

    protected final AppiumDriver driver;

    protected BaseScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    protected WebElement visible(By locator) {
        return MobileWaits.waitForVisible(driver, locator);
    }

    protected WebElement clickable(By locator) {
        return MobileWaits.waitForClickable(driver, locator);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
