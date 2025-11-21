package testUtils;

import ai.metayb.config.ConfigManager;
import ai.metayb.ui.wrappers.BaseDriver;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

@Listeners({AllureTestNg.class})
public class BaseTest extends BaseDriver {

    private static final Properties prop = new Properties();
    public static String baseUrl, browser;
    protected Logger logger;
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public BaseTest() {
        this.logger = LogManager.getLogger(this.getClass());
    }

    @BeforeSuite
    public void onStart() {
        logger.info("[BeforeSuite] - Setting up WebDriver");
        AllureEnvironment.createEnvironmentFile();
        baseUrl = ConfigManager.getBaseUrl();
        browser = ConfigManager.getBrowser();
    }

    @BeforeClass
    public void setUp() {
        logger.info("[BeforeClass] - Navigating to the base URL");
        System.out.println("Base URL: " + baseUrl);
        startApp(browser, baseUrl);
    }

    @AfterMethod
    public void logTestResults(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        attachScreenshot("final_state_" + testName);
        if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info(GREEN + "[Test] '{}' PASSED" + RESET, testName);
        } else if (result.getStatus() == ITestResult.FAILURE) {
            logger.error(RED + "[Test] '{}' FAILED" + RESET, testName, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn(YELLOW + "[Test] '{}' SKIPPED" + RESET, testName);
        }
    }

    @AfterClass
    public void tearDown() {
        logger.info("[AfterClass] - Quitting WebDriver");
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}

