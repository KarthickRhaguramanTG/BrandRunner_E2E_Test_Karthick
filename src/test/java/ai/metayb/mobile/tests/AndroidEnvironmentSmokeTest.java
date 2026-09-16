package ai.metayb.mobile.tests;

import ai.metayb.mobile.base.MobileBaseTest;
import ai.metayb.mobile.gestures.MobileGestures;
import ai.metayb.mobile.utils.ScreenshotUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Environment validation smoke test - proves the Android automation stack
 * (Appium + UiAutomator2 + a real emulator/device) actually works end to
 * end. Uses Android's own built-in Settings app rather than a project
 * app, since none is configured yet (android.properties has appPath/
 * appPackage/appActivity commented out - see README). Deliberately
 * separate from MobileSmokeTest, which depends on the example Login/Home
 * screens and their placeholder locators.
 */
@Epic("Mobile Automation")
@Feature("Android Environment Validation")
public class AndroidEnvironmentSmokeTest extends MobileBaseTest {

    @Test(groups = "mobile-smoke", description = "Android driver session is created against a real device/emulator and can interact with a real app")
    @Description("Validates driver creation, device detection, app launch, element location, one interaction, and screenshot capture, all against a real Android emulator.")
    public void androidEnvironmentIsFunctional() {
        Assert.assertNotNull(getDriver(), "Driver should be created");
        Assert.assertNotNull(getDriver().getSessionId(), "Driver should have an active session");

        List<WebElement> textViews = getDriver().findElements(By.className("android.widget.TextView"));
        Assert.assertFalse(textViews.isEmpty(), "At least one TextView should be present on screen");

        MobileGestures.scrollDown(getDriver());

        ScreenshotUtils.attachScreenshot(getDriver(), "Android environment validation screenshot");
    }
}
