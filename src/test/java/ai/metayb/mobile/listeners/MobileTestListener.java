package ai.metayb.mobile.listeners;

import ai.metayb.mobile.driver.MobileDriverManager;
import ai.metayb.mobile.utils.ScreenshotUtils;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Mobile counterpart to testUtils.ConvertBrokenToFailedListener: attaches a
 * screenshot + page source to Allure on failure. Sourced from
 * MobileDriverManager rather than BaseDriver - the Web listener reads a
 * completely separate ThreadLocal and would see nothing during a mobile test.
 */
public class MobileTestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        AppiumDriver driver = MobileDriverManager.getDriver();
        String testName = result.getMethod().getMethodName();
        ScreenshotUtils.attachScreenshot(driver, "Screenshot - " + testName);
        ScreenshotUtils.attachPageSource(driver, "Page source - " + testName);
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
