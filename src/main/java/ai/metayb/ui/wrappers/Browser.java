package ai.metayb.ui.wrappers;

import org.openqa.selenium.WebElement;

public interface Browser {

    /**
     * This method will launch and load the browser with maximized and timeout set
     * @param browser  - The name of the browser (chrome, firefox, safari, ie, edge)
     * @param url - The url of the application to be launched
     * @author Samuel Manoj
     * @return boolean - True if all loaded else false
     * @throws RuntimeException if browser driver setup fails
     */
    public boolean startApp(String browser, String url) ;

    /**
     * This method will locate the element using any given locator
     * @param locator  - The locator by which the element to be found
     * @param locValue - The locator value by which the element to be found
     * @author Samuel Manoj
     * @return WebElement - the first matching element
     * @throws org.openqa.selenium.NoSuchElementException if element not found
     */
    public WebElement locateElement(String locator, String locValue) ;


    /**
     * This method will verify browser actual title with expected
     * @param expectedTitle - The expected title of the browser
     * @author Samuel Manoj
     */
    public boolean verifyExactTitle(String expectedTitle);

    /**
     * This method will verify browser actual title with expected text using contains
     * @param expectedTitle - The expected title of the browser
     * @author Samuel Manoj
     */
    public boolean verifyPartialTitle(String expectedTitle);


    /**
     * This method will close the active browser
     * @author Samuel Manoj
     */
    public void closeActiveBrowser();

    /**
     * This method will close all the browsers
     * @author Samuel Manoj
     */
    public void closeAllBrowsers();

    /**
     * This method will quit the browser
     * @author Samuel Manoj
     */
    public void quitBrowser();

}
