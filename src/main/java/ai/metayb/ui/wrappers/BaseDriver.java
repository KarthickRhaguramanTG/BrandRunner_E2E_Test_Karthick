package ai.metayb.ui.wrappers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class BaseDriver implements Browser, Element, Select, TargetLocator {

    static {
        // Selenium's default "jdk-http-client" factory fails to construct on this
        // machine (java.net.http.HttpClient's internal Selector/Pipe loopback
        // wakeup uses a Unix Domain Socket that this machine's network/security
        // stack rejects - reproduced for both Chrome and Edge, across every local
        // JDK 17/21/24 build). ai.metayb.selenium.UrlConnectionHttpClient avoids
        // that entirely via plain java.net.HttpURLConnection. See its Javadoc.
        System.setProperty("webdriver.http.factory", "urlconnection");
    }

    private static final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();
    protected Logger logger;

    public BaseDriver() {
        this.logger = LogManager.getLogger(this.getClass());
    }

    @Override
    public boolean startApp(String browser, String url) {
        try {
            RemoteWebDriver newDriver;
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.setAcceptInsecureCerts(true); // for HTTPS issues
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    // Isolated profile per run so this doesn't collide with an already-running
                    // Chrome instance on the same machine (shared default profile/singleton lock).
                    chromeOptions.addArguments("--user-data-dir=" +
                            java.nio.file.Files.createTempDirectory("chrome-profile-").toAbsolutePath());
                    newDriver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    newDriver = new FirefoxDriver();
                    break;
                case "ie":
                    WebDriverManager.iedriver().setup();
                    newDriver = new InternetExplorerDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions options = new EdgeOptions();
                    newDriver = new EdgeDriver(options);
                    break;
                default:
                    System.err.println("This browser " + browser + " is not supported");
                    return false;
            }
            driverThreadLocal.set(newDriver);
            newDriver.get(url);
            newDriver.manage().window().maximize();
            reportStep("The browser " + browser + " launched successfully", "PASS");
            return true;
        } catch (Exception e) {
            reportStep("The browser " + browser + " could not be launched: " + e.getMessage(), "FAIL");
            return false;
        }
    }

    public static RemoteWebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public void waitForElement(By ele) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
            reportStep("Waited for the element to be present", "PASS");
        } catch (Exception e) {
            reportStep("Could not wait for the element to be present: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public WebElement locateElement(String locator, String locValue) {
        try {
            switch (locator.toLowerCase()) {
                case "id":
                    return getDriver().findElement(By.id(locValue));
                case "name":
                    return getDriver().findElement(By.name(locValue));
                case "class":
                    return getDriver().findElement(By.className(locValue));
                case "xpath":
                    return getDriver().findElement(By.xpath(locValue));
                case "css":
                    return getDriver().findElement(By.cssSelector(locValue));
                case "linktext":
                    return getDriver().findElement(By.linkText(locValue));
                case "partiallinktext":
                    return getDriver().findElement(By.partialLinkText(locValue));
                case "tagname":
                    return getDriver().findElement(By.tagName(locValue));
                default:
                    reportStep("The locator " + locator + " is not supported", "FAIL");
                    return null;
            }
        } catch (NoSuchElementException e) {
            reportStep("The element with locator: " + locator + ", value: " + locValue + " was not found", "FAIL");
            return null;
        }
    }

    @Override
    public boolean verifyExactTitle(String expectedTitle) {
        String actualTitle = getDriver().getTitle();
        if (actualTitle.equals(expectedTitle)) {
            reportStep("The title " + expectedTitle + " is verified", "PASS");
            return true;
        }
        reportStep("The title " + expectedTitle + " is not verified. Actual: " + actualTitle, "FAIL");
        return false;
    }

    @Override
    public boolean verifyPartialTitle(String expectedTitle) {
        String actualTitle = getDriver().getTitle();
        if (actualTitle.contains(expectedTitle)) {
            reportStep("The title " + expectedTitle + " is verified", "PASS");
            return true;
        }
        reportStep("The title " + expectedTitle + " is not verified. Actual: " + actualTitle, "FAIL");
        return false;
    }

    @Override
    public void closeActiveBrowser() {
        try {
            getDriver().close();
            reportStep("The active browser is closed", "PASS");
        } catch (Exception e) {
            reportStep("The active browser could not be closed: " + e.getMessage(), "FAIL");
        } finally {
            driverThreadLocal.remove();
        }
    }

    @Override
    public void closeAllBrowsers() {
        try {
            getDriver().quit();
            reportStep("All browsers are closed", "PASS");
        } catch (Exception e) {
            reportStep("All browsers could not be closed: " + e.getMessage(), "FAIL");
        } finally {
            driverThreadLocal.remove();
        }
    }

    @Override
    public void quitBrowser() {
        try {
            getDriver().quit();
            reportStep("The browser is quit", "PASS");
        } catch (Exception e) {
            reportStep("The browser could not be quit: " + e.getMessage(), "FAIL");
        } finally {
            driverThreadLocal.remove();
        }
    }

    @Override
    public void switchToWindow(int index) {
        try {
            Set<String> windowHandles = getDriver().getWindowHandles();
            List<String> allWindows = new ArrayList<>(windowHandles);
            getDriver().switchTo().window(allWindows.get(index));
            reportStep("The window of index " + index + " switched", "PASS");
        } catch (Exception e) {
            reportStep("The window of index " + index + " could not be switched: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void switchToFrame(WebElement ele) {
        try {
            getDriver().switchTo().frame(ele);
            reportStep("Switched to the frame", "PASS");
        } catch (Exception e) {
            reportStep("Could not switch to the frame: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void acceptAlert() {
        try {
            getDriver().switchTo().alert().accept();
            reportStep("Alert accepted", "PASS");
        } catch (Exception e) {
            reportStep("Alert could not be accepted: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void dismissAlert() {
        try {
            getDriver().switchTo().alert().dismiss();
            reportStep("Alert dismissed", "PASS");
        } catch (Exception e) {
            reportStep("Alert could not be dismissed: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public String getAlertText() {
        try {
            String text = getDriver().switchTo().alert().getText();
            reportStep("Alert text: " + text, "INFO");
            return text;
        } catch (Exception e) {
            reportStep("Could not get alert text: " + e.getMessage(), "FAIL");
            return "";
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot(String stepDesc) {
        if(getDriver() != null) {
            try {
                File src = getDriver().getScreenshotAs(OutputType.FILE);
                byte[] screenshot = getDriver().getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(stepDesc, new ByteArrayInputStream(screenshot));
                return FileUtils.readFileToByteArray(src);
            } catch (IOException e) {
                return new byte[0];
            }
        } else {
            System.out.println("Driver is null. Skipping screenshot.");
            return new byte[0];
        }
    }

    public void reportStep(String stepDesc, String status) {
        logger.info("[Test] - {}", stepDesc);
        if (status.equalsIgnoreCase("PASS")) {
            Allure.step(stepDesc, Status.PASSED);
        } else if (status.equalsIgnoreCase("FAIL")) {
            attachScreenshot(stepDesc);
            Allure.step(stepDesc, Status.FAILED);
            throw new AssertionError(stepDesc);
        } else {
            Allure.step(stepDesc);
        }
    }

    @Override
    public void type(WebElement ele, String data) {
        try {
            ele.clear();
            ele.sendKeys(data);
            reportStep("The data " + data + " is entered", "PASS");
        } catch (Exception e) {
            reportStep("The data " + data + " could not be entered: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void typeSecret(WebElement ele, String data) {
        try {
            ele.clear();
            ele.sendKeys(data);
            reportStep("A masked value is entered", "PASS");
        } catch (Exception e) {
            reportStep("A masked value could not be entered: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void typeWithoutClear(WebElement ele, String data) {
        try {
            ele.sendKeys(data);
            reportStep("The data " + data + " is entered", "PASS");
        } catch (Exception e) {
            reportStep("The data " + data + " could not be entered: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void typeAndEnter(WebElement ele, String data) {
        try {
            ele.clear();
            ele.sendKeys(data, Keys.ENTER);
            reportStep("The data " + data + " is entered and submitted", "PASS");
        } catch (Exception e) {
            reportStep("The data " + data + " could not be entered and submitted: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public void click(WebElement ele) {
        try {
            ele.click();
            reportStep("The element is clicked", "PASS");
        } catch (Exception e) {
            reportStep("The element could not be clicked: " + e.getMessage(), "FAIL");
        }
    }

    @Override
    public String getText(WebElement ele) {
        try {
            String text = ele.getText();
            reportStep("The text is: " + text, "INFO");
            return text;
        } catch (Exception e) {
            reportStep("Could not get the text: " + e.getMessage(), "FAIL");
            return "";
        }
    }

    @Override
    public void verifyExactText(WebElement ele, String expectedText) {
        String actualText = ele.getText();
        if (actualText.equals(expectedText)) {
            reportStep("The text " + expectedText + " is verified", "PASS");
        } else {
            reportStep("The text " + expectedText + " is not verified", "FAIL");
        }
    }

    @Override
    public void verifyPartialText(WebElement ele, String expectedText) {
        String actualText = ele.getText();
        if (actualText.contains(expectedText)) {
            reportStep("The text " + expectedText + " is verified", "PASS");
        } else {
            reportStep("The text " + expectedText + " is not verified", "FAIL");
        }
    }

    @Override
    public void verifyExactAttribute(WebElement ele, String attribute, String value) {
        String actualValue = ele.getDomAttribute(attribute);
        if (value.equals(actualValue)) {
            reportStep("The attribute " + attribute + " with value " + value + " is verified", "PASS");
        } else {
            reportStep("The attribute " + attribute + " with value " + value + " is not verified. Actual: " + actualValue, "FAIL");
        }
    }

    @Override
    public void verifyPartialAttribute(WebElement ele, String attribute, String value) {
        String actualValue = ele.getDomAttribute(attribute);
        if (actualValue != null && actualValue.contains(value)) {
            reportStep("The attribute " + attribute + " with value " + value + " is verified", "PASS");
        } else {
            reportStep("The attribute " + attribute + " with value " + value + " is not verified. Actual: " + actualValue, "FAIL");
        }
    }

    @Override
    public void verifySelected(WebElement ele) {
        if (ele.isSelected()) {
            reportStep("The element is selected", "PASS");
        } else {
            reportStep("The element is not selected", "FAIL");
        }
    }

    @Override
    public void verifyDisplayed(WebElement ele) {
        if (ele.isDisplayed()) {
            reportStep("The element is displayed", "PASS");
        } else {
            reportStep("The element is not displayed", "FAIL");
        }
    }

    @Override
    public void selectDropDownUsingVisibleText(WebElement ele, String value) {
        try {
            new org.openqa.selenium.support.ui.Select(ele).selectByVisibleText(value);
            reportStep("The dropdown is selected with visible text: " + value, "PASS");
        } catch (Exception e) {
            reportStep("The dropdown could not be selected with visible text: " + value + " (" + e.getMessage() + ")", "FAIL");
        }
    }

    @Override
    public void selectDropDownUsingValue(WebElement ele, String value) {
        try {
            new org.openqa.selenium.support.ui.Select(ele).selectByValue(value);
            reportStep("The dropdown is selected with value: " + value, "PASS");
        } catch (Exception e) {
            reportStep("The dropdown could not be selected with value: " + value + " (" + e.getMessage() + ")", "FAIL");
        }
    }

    @Override
    public void selectDropDownUsingIndex(WebElement ele, int index) {
        try {
            new org.openqa.selenium.support.ui.Select(ele).selectByIndex(index);
            reportStep("The dropdown is selected with index: " + index, "PASS");
        } catch (Exception e) {
            reportStep("The dropdown could not be selected with index: " + index + " (" + e.getMessage() + ")", "FAIL");
        }
    }

    @Override
    public String getSelectedValue(WebElement ele) {
        try {
            String selectedValue = new org.openqa.selenium.support.ui.Select(ele).getFirstSelectedOption().getAttribute("value");
            reportStep("The selected value is: " + selectedValue, "INFO");
            return selectedValue;
        } catch (Exception e) {
            reportStep("Could not get the selected value: " + e.getMessage(), "FAIL");
            return "";
        }
    }

    @Override
    public String getSelectedVisibleText(WebElement ele) {
        try {
            String selectedText = new org.openqa.selenium.support.ui.Select(ele).getFirstSelectedOption().getText();
            reportStep("The selected visible text is: " + selectedText, "INFO");
            return selectedText;
        } catch (Exception e) {
            reportStep("Could not get the selected visible text: " + e.getMessage(), "FAIL");
            return "";
        }
    }

    @Override
    public int getSelectedIndex(WebElement ele) {
        try {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(ele);
            int selectedIndex = select.getOptions().indexOf(select.getFirstSelectedOption());
            reportStep("The selected index is: " + selectedIndex, "INFO");
            return selectedIndex;
        } catch (Exception e) {
            reportStep("Could not get the selected index: " + e.getMessage(), "FAIL");
            return 0;
        }
    }

    @Override
    public int getAllOptions(WebElement ele) {
        try {
            int allOptions = new org.openqa.selenium.support.ui.Select(ele).getOptions().size();
            reportStep("The total number of options is: " + allOptions, "INFO");
            return allOptions;
        } catch (Exception e) {
            reportStep("Could not get the total number of options: " + e.getMessage(), "FAIL");
            return 0;
        }
    }

    public void assertPopupMessage(String expectedMessage) {
        try {
            WebElement toast = getDriver().findElement(By.xpath("//div[text()='" + expectedMessage + "']"));
            if (toast.isDisplayed()) {
                reportStep("Popup message is displayed: " + expectedMessage, "PASS");
            } else {
                reportStep("Popup message is not displayed: " + expectedMessage, "FAIL");
            }
        } catch (NoSuchElementException e) {
            reportStep("Popup message not found: " + expectedMessage, "FAIL");
        }
    }

    public void validateDisplayText(String actualText, String expectedText, String stepDesc) {
        if (actualText.equals(expectedText)) {
            reportStep(stepDesc + ": Text matched -> " + actualText, "PASS");
        } else {
            reportStep(stepDesc + ": Expected '" + expectedText + "' but found '" + actualText + "'", "FAIL");
        }
    }
}
