package ai.metayb.ui.wrappers;

import org.openqa.selenium.WebElement;

public interface Element {

    /**
     * This method will enter the value in the given text field 
     * @param ele   - The Webelement (text field) in which the data to be entered
     * @param data  - The data to be sent to the webelement
     * @author Samuel Manoj
     * @throws org.openqa.selenium.ElementNotInteractableException if the element is not interactable (e.g., not visible or disabled)
     */
    public void type(WebElement ele, String data) ;

    /**
     * This method will enter the value in the given text field, exactly like {@link #type},
     * but without logging the value itself to the console/Allure report - use this for
     * passwords, tokens, or any other field whose value must never appear in a log or report.
     * @param ele   - The Webelement (text field) in which the data to be entered
     * @param data  - The sensitive data to be sent to the webelement
     * @author Samuel Manoj
     * @throws org.openqa.selenium.ElementNotInteractableException if the element is not interactable (e.g., not visible or disabled)
     */
    public void typeSecret(WebElement ele, String data);

    /**
     * This method will enter the value in the given text field without clearing
     * @param ele   - The Webelement (text field) in which the data to be entered
     * @param data  - The data to be sent to the webelement
     * @author Samuel Manoj
     * @throws org.openqa.selenium.ElementNotInteractableException if the element is not interactable (e.g., not visible or disabled)
     */
    public void typeWithoutClear(WebElement ele, String data) ;

    /**
     * This method will enter the value in the given text field and Submit
     * @param ele   - The Webelement (text field) in which the data to be entered
     * @param data  - The data to be sent to the webelement
     * @author Samuel Manoj
     * @throws  org.openqa.selenium.ElementNotInteractableException if the element is not interactable (e.g., not visible or disabled)
     */
    public void typeAndEnter(WebElement ele, String data) ;

    /**
     * This method will click the element and take snap
     * @param ele   - The Webelement (button/link/element) to be clicked
     * @author Samuel Manoj
     */
    public void click(WebElement ele);

    /**
     * This method will get the text of the element
     * @param ele   - The Webelement (button/link/element) in which text to be retrieved
     * @author Samuel Manoj
     */
    public String getText(WebElement ele);

    /**
     * This method will verify exact given text with actual text on the given element
     * @param ele   - The Webelement in which the text to be need to be verified
     * @param expectedText  - The expected text to be verified
     * @author Samuel Manoj
     */
    public void verifyExactText(WebElement ele, String expectedText);

    /**
     * This method will verify given text contains actual text on the given element
     * @param ele   - The Webelement in which the text to be need to be verified
     * @param expectedText  - The expected text to be verified
     * @author Samuel Manoj
     */
    public void verifyPartialText(WebElement ele, String expectedText);

    /**
     * This method will verify exact given attribute's value with actual value on the given element
     * @param ele   - The Webelement in which the attribute value to be need to be verified
     * @param attribute  - The attribute to be checked (like value, href etc)
     * @param value  - The value of the attribute
     * @author Samuel Manoj
     */
    public void verifyExactAttribute(WebElement ele, String attribute, String value);

    /**
     * This method will verify partial given attribute's value with actual value on the given element
     * @param ele   - The Webelement in which the attribute value to be need to be verified
     * @param attribute  - The attribute to be checked (like value, href etc)
     * @param value  - The value of the attribute
     * @author Samuel Manoj
     */
    public void verifyPartialAttribute(WebElement ele, String attribute, String value);

    /**
     * This method will verify if the element (Radio button, Checkbox)  is selected
     * @param ele   - The Webelement (Radio button, Checkbox) to be verified
     * @author Samuel Manoj
     */
    public void verifySelected(WebElement ele);

    /**
     * This method will verify if the element is visible in the DOM
     * @param ele   - The Webelement to be checked
     * @author Samuel Manoj
     */
    public void verifyDisplayed(WebElement ele);

}
