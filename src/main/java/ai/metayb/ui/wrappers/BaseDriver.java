package ai.metayb.ui.wrappers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
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

    public static RemoteWebDriver driver;
    protected Logger logger;

    public BaseDriver() {
        this.logger = LogManager.getLogger(this.getClass());
    }

    @Override
    public boolean startApp(String browser, String url) {
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.setAcceptInsecureCerts(true); // for HTTPS issues
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "ie":
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                case "edge":
                    EdgeOptions options = new EdgeOptions();
                    driver = new EdgeDriver(options);
                    break;
                default:
                    System.err.println("This browser " + browser + " is not supported");
                    return false;
            }
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().window().maximize();
            reportStep("The browser " + browser + " launched successfully", "PASS");
            return true;
        } catch (Exception e) {
            reportStep("The browser " + browser + " could not be launched", "FAIL");
            return false;
        }
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public void WebDriverWait(By ele) {
        try {
            Thread.sleep(5000); // Wait for 10 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
            reportStep("Waited for " + 10 + " seconds for the element to be present", "PASS");
        } catch (Exception e) {
            reportStep("Could not wait for the element to be present", "FAIL");
        }
    }

    @Override
    public WebElement locateElement(String locator, String locValue) {
        try {
            switch (locator.toLowerCase()) {
                case "id":
                    return driver.findElement(By.id(locValue));
                case "name":
                    return driver.findElement(By.name(locValue));
                case "class":
                    return driver.findElement(By.className(locValue));
                case "xpath":
                    return driver.findElement(By.xpath(locValue));
                case "css":
                    return driver.findElement(By.cssSelector(locValue));
                case "linktext":
                    return driver.findElement(By.linkText(locValue));
                case "partiallinktext":
                    return driver.findElement(By.partialLinkText(locValue));
                case "tagname":
                    return driver.findElement(By.tagName(locValue));
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
        String actualTitle = driver.getTitle();
        if (actualTitle.equals(expectedTitle)) {
            reportStep("The title " + expectedTitle + " is verified", "PASS");
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyPartialTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        if (actualTitle.contains(expectedTitle)) {
            reportStep("The title " + expectedTitle + " is verified", "PASS");
            return true;
        }
        return false;
    }

    @Override
    public void closeActiveBrowser() {
        try {
            driver.close();
            reportStep("The active browser is closed", "PASS");
        } catch (Exception e) {
            reportStep("The active browser could not be closed", "FAIL");
        } finally {
            driver = null;
        }
    }

    @Override
    public void closeAllBrowsers() {
        try {
            driver.quit();
            reportStep("All browsers are closed", "PASS");
        } catch (Exception e) {
            reportStep("All browsers could not be closed", "FAIL");
        }
    }

    @Override
    public void quitBrowser() {
        try {
            driver.quit();
            reportStep("The browser is quit", "PASS");
        } catch (Exception e) {
            reportStep("The browser could not be quit", "FAIL");
        }
    }

    @Override
    public void switchToWindow(int index) {
        try {
            Set<String> windowHandles = driver.getWindowHandles();
            List<String> allWindows = new ArrayList<>(windowHandles);
            driver.switchTo().window(allWindows.get(index));
            reportStep("The window of index " + index + " switched", "PASS");
        } catch (Exception e) {
            reportStep("The window of index " + index + " could not be switched", "FAIL");
        }
    }

    @Override
    public void switchToFrame(WebElement ele) {
        try {
            driver.switchTo().frame(ele);
            reportStep("Switched to the frame", "PASS");
        } catch (Exception e) {
            reportStep("Could not switch to the frame", "FAIL");
        }
    }

    @Override
    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            reportStep("Alert accepted", "PASS");
        } catch (Exception e) {
            reportStep("Alert could not be accepted", "FAIL");
        }
    }

    @Override
    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            reportStep("Alert dismissed", "PASS");
        } catch (Exception e) {
            reportStep("Alert could not be dismissed", "FAIL");
        }
    }

    @Override
    public String getAlertText() {
        try {
            String text = driver.switchTo().alert().getText();
            reportStep("Alert text: " + text, "INFO");
            return text;
        } catch (Exception e) {
            reportStep("Could not get alert text", "FAIL");
            return "";
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot(String stepDesc) {
        if(driver != null) {
            try {
                File src = driver.getScreenshotAs(OutputType.FILE);
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
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

    //    @Step("{stepDesc}")
    public void reportStep(String stepDesc, String status) {
        logger.info("[Test] - {}", stepDesc);
        if (status.equalsIgnoreCase("PASS")) {
            Allure.step(stepDesc, Status.PASSED);
        } else if (status.equalsIgnoreCase("FAIL")) {
            attachScreenshot(stepDesc);
            Allure.step(stepDesc, Status.FAILED);
        } else {
            Allure.step(stepDesc);
        }
    }

    @Override
    @Step("Type '{1}' into element")
    public void type(WebElement ele, String data) {
        try {
            ele.clear();
            ele.sendKeys(data);
            reportStep("The data " + data + " is entered", "PASS");
        } catch (Exception e) {
            reportStep("The data " + data + " could not be entered", "FAIL");
        }
    }

    @Override
    public void typeAndEnter(WebElement ele, String data) {
        try {
            ele.clear();
            ele.sendKeys(data, Keys.ENTER);
            reportStep("The data " + data + " is entered and submitted", "PASS");
        } catch (Exception e) {
            reportStep("The data " + data + " could not be entered and submitted", "FAIL");
        }
    }

    @Override
    public void typeDownAndEnter(WebElement ele, String data) {
        try {
            ele.clear();
            ele.sendKeys(data);
            ele.sendKeys(Keys.ARROW_DOWN);
            ele.sendKeys(Keys.ENTER);
            reportStep("Typed '" + data + "', navigated down and selected", "PASS");
        } catch (Exception e) {
            reportStep("Failed to type '" + data + "' and select using Down + Enter", "FAIL");
        }
    }

    @Override
    @Step("Click on element")
    public void click(WebElement ele) {
        try {
            ele.click();
            reportStep("The element is clicked", "PASS");
        } catch (Exception e) {
            reportStep("The element could not be clicked", "FAIL");
        }
    }

    @Override
    public String getText(WebElement ele) {
        try {
            String text = ele.getText();
            reportStep("The text is: " + text, "INFO");
            return text;
        } catch (Exception e) {
            reportStep("Could not get the text", "FAIL");
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
        if (actualValue.equals(value)) {
            reportStep("The attribute " + attribute + " with value " + value + " is verified", "PASS");
        } else {
            reportStep("The attribute " + attribute + " with value " + value + " is not verified", "FAIL");
        }
    }

    @Override
    public void verifyPartialAttribute(WebElement ele, String attribute, String value) {
        String actualValue = ele.getDomAttribute(attribute);
        if (actualValue.contains(value)) {
            reportStep("The attribute " + attribute + " with value " + value + " is verified", "PASS");
        } else {
            reportStep("The attribute " + attribute + " with value " + value + " is not verified", "FAIL");
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
            ele.click();
            ele.sendKeys(value);
            reportStep("The dropdown is selected with value: " + value, "PASS");
        } catch (Exception e) {
            reportStep("The dropdown could not be selected with value: " + value, "FAIL");
        }
    }

    @Override
    public void selectDropDownUsingValue(WebElement ele, String value) {
        try {
            ele.click();
            ele.sendKeys(value);
            reportStep("The dropdown is selected with value: " + value, "PASS");
        } catch (Exception e) {
            reportStep("The dropdown could not be selected with value: " + value, "FAIL");
        }
    }

    @Override
    public void selectDropDownUsingIndex(WebElement ele, int index) {
        try {
            ele.click();
            ele.sendKeys(String.valueOf(index));
            reportStep("The dropdown is selected with index: " + index, "PASS");
        } catch (Exception e) {
            reportStep("The dropdown could not be selected with index: " + index, "FAIL");
        }
    }

    @Override
    public String getSelectedValue(WebElement ele) {
        try {
            String selectedValue = ele.getAttribute("value");
            reportStep("The selected value is: " + selectedValue, "INFO");
            return selectedValue;
        } catch (Exception e) {
            reportStep("Could not get the selected value", "FAIL");
            return "";
        }
    }

    @Override
    public String getSelectedVisibleText(WebElement ele) {
        try {
            String selectedText = ele.getText();
            reportStep("The selected visible text is: " + selectedText, "INFO");
            return selectedText;
        } catch (Exception e) {
            reportStep("Could not get the selected visible text", "FAIL");
            return "";
        }
    }

    @Override
    public int getSelectedIndex(WebElement ele) {
        try {
            int selectedIndex = Integer.parseInt(ele.getAttribute("selectedIndex"));
            reportStep("The selected index is: " + selectedIndex, "INFO");
            return selectedIndex;
        } catch (Exception e) {
            reportStep("Could not get the selected index", "FAIL");
            return 0;
        }
    }

    @Override
    public int getAllOptions(WebElement ele) {
        try {
            List<WebElement> options = ele.findElements(By.tagName("option"));
            int allOptions = options.size();
            reportStep("The total number of options is: " + allOptions, "INFO");
            return allOptions;
        } catch (Exception e) {
            reportStep("Could not get the total number of options", "FAIL");
            return 0;
        }
    }

    public void assertPopupMessage(String expectedMessage) {
        try {
            WebElement toast = driver.findElement(By.xpath("//div[text()='" + expectedMessage + "']"));
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
