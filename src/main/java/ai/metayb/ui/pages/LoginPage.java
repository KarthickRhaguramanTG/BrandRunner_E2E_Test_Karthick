package ai.metayb.ui.pages;

import ai.metayb.ui.components.LoginPageElements;
import ai.metayb.ui.core.DataReader;
import ai.metayb.ui.core.InputReader;
import ai.metayb.ui.wrappers.BaseDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BaseDriver {

    private final LoginPageElements elements;
    private final DataReader data;
    private final InputReader input;

    public LoginPage() {
        super();
        elements = new LoginPageElements();
        data = new DataReader();
        input = new InputReader();
        PageFactory.initElements(getDriver(), elements);
    }

    @Step("Enter email id and password")
    public void enterUserCredentials() {
        WebDriverWait(elements.getEmailAddressLocator());
        type(elements.getTxtEmailAddress(), data.emailAddress);
        type(elements.getTxtPassword(), data.password);
        click(elements.getBtnLogin());
    }

    public void assertLoginSuccess() {
        String actualText = getText(elements.getHelloUserMessage());
        validateDisplayText(actualText, "Hello User!", "Login is successful");
    }
}