package ai.metayb.ui.components;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class LoginPageElements {

    @FindBy(id = "email-login")
    private WebElement txtEmailAddress;

    By EmailAddressLocator = By.id("email-login");

    @FindBy(id = "password-login")
    private WebElement txtPassword;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement btnLogin;

    @FindBy(xpath = "//h5[text()='Hello User!']")
    private WebElement helloUserMessage;

}
