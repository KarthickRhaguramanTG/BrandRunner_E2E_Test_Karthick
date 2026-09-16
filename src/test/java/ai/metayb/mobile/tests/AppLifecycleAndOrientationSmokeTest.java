package ai.metayb.mobile.tests;

import ai.metayb.mobile.base.MobileBaseTest;
import ai.metayb.mobile.screens.LoginScreen;
import ai.metayb.mobile.utils.AppLifecycleUtils;
import ai.metayb.mobile.utils.DeviceUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * EXAMPLE smoke suite validating background/foreground and orientation
 * change - the two device-level behaviors that don't fit naturally into a
 * screen object.
 */
@Epic("Mobile Automation")
@Feature("App Lifecycle and Orientation")
public class AppLifecycleAndOrientationSmokeTest extends MobileBaseTest {

    @Test(groups = "mobile-smoke", description = "App state is retained after background/foreground")
    @Description("Sends the app to the background briefly, lets Appium bring it back, and confirms the login screen is still displayed (state retained).")
    public void appRetainsStateAfterBackgroundForeground() {
        LoginScreen loginScreen = new LoginScreen(getDriver());
        Assert.assertTrue(loginScreen.isDisplayed(), "Precondition: login screen should be visible before backgrounding");

        AppLifecycleUtils.runInBackground(getDriver(), Duration.ofSeconds(3));

        Assert.assertTrue(loginScreen.isDisplayed(), "Login screen should still be visible after returning to foreground");
    }

    @Test(groups = "mobile-smoke", description = "App remains usable after an orientation change")
    @Description("Rotates the device to landscape then back to portrait and confirms the login screen remains visible/usable throughout.")
    public void appRemainsUsableAfterOrientationChange() {
        LoginScreen loginScreen = new LoginScreen(getDriver());

        DeviceUtils.setOrientation(getDriver(), ScreenOrientation.LANDSCAPE);
        Assert.assertTrue(loginScreen.isDisplayed(), "Login screen should remain visible in landscape");

        DeviceUtils.setOrientation(getDriver(), ScreenOrientation.PORTRAIT);
        Assert.assertTrue(loginScreen.isDisplayed(), "Login screen should remain visible back in portrait");
    }
}
