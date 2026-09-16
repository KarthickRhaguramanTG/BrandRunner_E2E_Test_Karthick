package ai.metayb.mobile.screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * EXAMPLE screen object. Its locators come entirely from
 * config/mobile/locators.properties (the "login.*" keys) - this class works
 * unmodified once those values are pointed at a real app's real elements.
 */
public class LoginScreen extends BaseScreen {

    private final By usernameField = ScreenLocators.resolve("login.username.field");
    private final By passwordField = ScreenLocators.resolve("login.password.field");
    private final By loginButton = ScreenLocators.resolve("login.button");
    private final By errorMessage = ScreenLocators.resolve("login.error.message");

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(usernameField);
    }

    public LoginScreen enterUsername(String username) {
        visible(usernameField).sendKeys(username);
        return this;
    }

    public LoginScreen enterPassword(String password) {
        visible(passwordField).sendKeys(password);
        return this;
    }

    /** Taps login and returns the screen it navigates to on success. */
    public HomeScreen tapLogin() {
        clickable(loginButton).click();
        return new HomeScreen(driver);
    }

    /** Taps login without navigating away - for the invalid-input case, where the app is expected to stay put. */
    public void tapLoginExpectingError() {
        clickable(loginButton).click();
    }

    public String getErrorMessage() {
        return visible(errorMessage).getText();
    }
}
