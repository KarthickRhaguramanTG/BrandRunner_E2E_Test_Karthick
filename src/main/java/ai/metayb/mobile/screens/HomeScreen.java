package ai.metayb.mobile.screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * EXAMPLE screen object, reached after a successful login. Locators come
 * from config/mobile/locators.properties ("home.*" keys).
 */
public class HomeScreen extends BaseScreen {

    private final By welcomeText = ScreenLocators.resolve("home.welcome.text");

    public HomeScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(welcomeText);
    }

    public String getWelcomeText() {
        return visible(welcomeText).getText();
    }
}
