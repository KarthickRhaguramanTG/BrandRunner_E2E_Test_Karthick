package ai.metayb.mobile.base;

import ai.metayb.mobile.config.MobileConfigManager;
import ai.metayb.mobile.core.MobileFrameworkException;
import ai.metayb.mobile.driver.MobileDriverFactory;
import ai.metayb.mobile.driver.MobileDriverManager;
import ai.metayb.mobile.listeners.MobileTestListener;
import ai.metayb.mobile.utils.AllureReportUtils;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

/**
 * Mobile equivalent of testUtils.BaseTest: driver lifecycle, per-test result
 * logging, and cleanup that always runs. Uses its own ThreadLocal driver
 * (MobileDriverManager) - entirely independent of BaseDriver's Web driver
 * ThreadLocal. Screenshot/page-source capture on failure is handled by
 * MobileTestListener, not duplicated here.
 */
@Listeners({AllureTestNg.class, MobileTestListener.class})
public class MobileBaseTest {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public void setUpMobileDriver() {
        logger.info("[BeforeClass] Platform={}, ExecutionTarget={}",
                MobileConfigManager.getPlatform(), MobileConfigManager.getExecutionTarget());
        AppiumDriver driver = MobileDriverFactory.createDriver();
        if (driver == null) {
            // MobileDriverFactory.createDriver() should never return null - it throws
            // MobileFrameworkException with full diagnostic context on any failure. This is a
            // hard stop so a null driver can never silently reach a @Test method.
            throw new MobileFrameworkException("MobileDriverFactory.createDriver() returned null driver");
        }
        MobileDriverManager.setDriver(driver);
        AllureReportUtils.attachDeviceContext(driver);
        logger.info("[BeforeClass] Mobile driver session started: {}", driver.getSessionId());
    }

    protected AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void logTestResult(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("[Test] '{}' FAILED", testName, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("[Test] '{}' PASSED", testName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("[Test] '{}' SKIPPED", testName);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownMobileDriver() {
        AppiumDriver driver = getDriver();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
            logger.info("[AfterClass] Mobile driver session ended");
        } catch (Exception e) {
            logger.warn("[AfterClass] Error while quitting mobile driver: {}", e.getMessage());
        } finally {
            MobileDriverManager.removeDriver();
        }
    }
}
