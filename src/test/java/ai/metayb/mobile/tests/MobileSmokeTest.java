package ai.metayb.mobile.tests;

import ai.metayb.mobile.base.MobileBaseTest;
import ai.metayb.mobile.config.MobileConfigManager;
import ai.metayb.mobile.screens.HomeScreen;
import ai.metayb.mobile.screens.LoginScreen;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * EXAMPLE smoke suite validating the framework end to end: app launch,
 * element visibility, text input, button tap/navigation, and invalid input.
 * Point config/mobile/locators.properties at a real app to make these pass
 * against real UI instead of the placeholder locators.
 */
@Epic("Mobile Automation")
@Feature("Login Screen")
public class MobileSmokeTest extends MobileBaseTest {

    @Test(groups = "mobile-smoke", description = "App launches and the login screen is visible")
    @Description("App launch + element visibility: the driver starts a session and the initial (login) screen is displayed.")
    public void appLaunchesAndShowsLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(getDriver());
        Assert.assertTrue(loginScreen.isDisplayed(), "Login screen should be visible after app launch");
    }

    @Test(groups = "mobile-smoke", description = "Valid credentials navigate to the home screen")
    @Description("Text input + button tap/navigation: entering valid credentials and tapping login navigates to the home screen.")
    public void validLoginNavigatesToHomeScreen() {
        LoginScreen loginScreen = new LoginScreen(getDriver());
        HomeScreen homeScreen = loginScreen
                .enterUsername(MobileConfigManager.get("test.validUsername", "demoUser"))
                .enterPassword(MobileConfigManager.get("test.validPassword", "demoPass123"))
                .tapLogin();

        Assert.assertTrue(homeScreen.isDisplayed(), "Home screen should be visible after a valid login");
    }

    @Test(groups = "mobile-smoke", description = "Invalid credentials show a validation error")
    @Description("Negative input handling: invalid credentials keep the user on the login screen with a validation error shown.")
    public void invalidLoginShowsValidationError() {
        LoginScreen loginScreen = new LoginScreen(getDriver());
        loginScreen.enterUsername("").enterPassword("").tapLoginExpectingError();

        Assert.assertFalse(loginScreen.getErrorMessage().isBlank(),
                "A validation error message should be shown for invalid input");
    }
}
