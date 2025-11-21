package ai.metayb.ui.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ai.metayb.ui.wrappers.BaseDriver.driver;


public class CommonUtils {

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
        // Implementation to get the current date
    }

    public static String getUniqueNumber() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void assertPopupMessage(String expectedMessage, String actualMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[text()='PFI created successfully.']")
        ));
        System.out.println("Toast is visible: " + toast.isDisplayed());
        if (!expectedMessage.equals(actualMessage)) {
            throw new AssertionError("Expected message: " + expectedMessage + ", but got: " + actualMessage);
        }
    }

}
