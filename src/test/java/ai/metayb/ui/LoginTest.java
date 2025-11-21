package ai.metayb.ui;

import ai.metayb.ui.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import testUtils.BaseTest;

@Epic("IFC End to end automation")
@Feature("Login Scenario")
public class LoginTest extends BaseTest {

    @Test(description = "Validate User Login")
    @Story("User Login Details")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a user can log in adn see the dashboard in IFC application.")
    public void userLoginTest() {
        logger.info("[Test] - Starting userLoginTest");
        logger.info("Page Title: {}", driver.getTitle());
        logger.info("Current URL : {}", driver.getCurrentUrl());

        LoginPage login = new LoginPage();

        reportStep("Entering user credentials", "INFO");
        login.enterUserCredentials();

        reportStep("User successfully logged in and dashboard displayed", "PASS");
        login.assertLoginSuccess();
    }
}
