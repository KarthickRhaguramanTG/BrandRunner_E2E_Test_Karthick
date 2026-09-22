package ai.metayb.ui.components;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class LoginPageElements {

    // Verified live against the real Brand Runners login page (maggie.qas.brandrunners.ai):
    // the email/password inputs have no id attribute at all, only name="email"/name="password" -
    // the previous id="email-login"/id="password-login" locators matched a different,
    // legacy application (this project's UI suite predates the Brand Runners rebrand).
    @FindBy(name = "email")
    private WebElement txtEmailAddress;

    By EmailAddressLocator = By.name("email");

    @FindBy(name = "password")
    private WebElement txtPassword;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement btnLogin;

    // The real post-login landing page is the ANP dashboard, titled "Dashboard" (a plain
    // <p>, not a heading tag - this is a Material-UI app). "Hello User!" does not appear
    // anywhere on it - verified live, same legacy-app mismatch as the locators above.
    @FindBy(xpath = "//p[text()='Dashboard']")
    private WebElement dashboardTitle;

    By DashboardTitleLocator = By.xpath("//p[text()='Dashboard']");

}
