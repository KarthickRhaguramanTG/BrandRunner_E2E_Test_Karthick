package ai.metayb.ui.wrappers;

import org.openqa.selenium.WebElement;

public interface Select {

    /**
     * This method will select the drop down visible text
     * @param ele   - The Webelement (dropdown) to be selected
     * @param value The value to be selected (visibletext) from the dropdown 
     * @author Samuel Manoj
     */
    public void selectDropDownUsingVisibleText(WebElement ele, String value) ;

    /**
     * This method will select the drop down using value
     * @param ele   - The Webelement (dropdown) to be selected
     * @param value The value attribute to be selected from the dropdown 
     * @author Samuel Manoj
     */
    public void selectDropDownUsingValue(WebElement ele, String value) ;

    /**
     * This method will select the drop down using index
     * @param ele   - The Webelement (dropdown) to be selected
     * @param index The index to be selected from the dropdown 
     * @author Samuel Manoj
     */
    public void selectDropDownUsingIndex(WebElement ele, int index) ;
    /**
     * This method will get the selected value from the dropdown
     * @param ele   - The Webelement (dropdown) to be selected
     * @author Samuel Manoj
     */
    public String getSelectedValue(WebElement ele) ;

    /**
     * This method will get the selected visible text from the dropdown
     * @param ele   - The Webelement (dropdown) to be selected
     * @author Samuel Manoj
     */
    public String getSelectedVisibleText(WebElement ele) ;

    /**
     * This method will get the selected index from the dropdown
     * @param ele   - The Webelement (dropdown) to be selected
     * @author Samuel Manoj
     */
    public int getSelectedIndex(WebElement ele) ;

    /**
     * This method will get the all options from the dropdown
     * @param ele   - The Webelement (dropdown) to be selected
     * @author Samuel Manoj
     */
    public int getAllOptions(WebElement ele) ;

}
