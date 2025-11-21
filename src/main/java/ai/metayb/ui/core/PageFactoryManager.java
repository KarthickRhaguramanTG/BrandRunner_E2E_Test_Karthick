package ai.metayb.ui.core;

import ai.metayb.ui.pages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageFactoryManager {

    WebDriver driver;

    public PageFactoryManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        LoginPage loginPage = new LoginPage();
        PageFactory.initElements(driver, loginPage);
        return loginPage;
    }
}
