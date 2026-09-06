package com.automation.ui.pages;

import com.automation.ui.models.CheckoutInfo;
import com.automation.ui.utilities.PageUrls;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInfoPage extends BasePage {
    private final NavigationBar navigationBar;
    private final By checkoutHeader = By.xpath("//span[@data-test='title']");
    private final By firstNameInput = By.xpath("//input[@data-test='firstName']");
    private final By lastNameInput = By.xpath("//input[@data-test='lastName']");
    private final By zipInput = By.xpath("//input[@data-test='postalCode']");
    private final By continueButton = By.xpath("//input[@data-test='continue']");
    private final By cancelButton = By.xpath("//button[@data-test='cancel']");
    private final By errorMessage = By.xpath("//h3[@data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    public void enterCheckoutInformation(CheckoutInfo checkoutInfo) {
        elementActions.enterText(firstNameInput, checkoutInfo.getFirstName());
        elementActions.enterText(lastNameInput, checkoutInfo.getLastName());
        elementActions.enterText(zipInput, checkoutInfo.getPostalCode());
    }

    public void continueToCheckoutOverview() {
        driver.findElement(continueButton).click();
    }

    public String getErrorMessage() {
        return elementActions.waitUntilVisible(errorMessage).getText();
    }

    public boolean isAtCheckoutInfoPage() {
        elementActions.waitUntilUrlContains(PageUrls.CHECKOUT_INFORMATION);

        return navigationBar.isDisplayed()
                && elementActions.waitUntilVisible(checkoutHeader).getText().equals("Checkout: Your Information")
                && elementActions.waitUntilVisible(firstNameInput).isDisplayed();
    }
}
