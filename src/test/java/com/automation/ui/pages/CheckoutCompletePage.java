package com.automation.ui.pages;

import com.automation.ui.utilities.PageUrls;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {
    private final NavigationBar navigationBar;
    private final By checkoutHeader = By.xpath("//span[@data-test='title']");
    private final By completeHeader = By.xpath("//h2[@data-test='complete-header']");
    private final By completeText = By.xpath("//div[@data-test='complete-text']");
    private final By backToProductsButton = By.xpath("//button[@data-test='back-to-products']");
    private final By generatePdfButton = By.xpath("//button[@data-test='generate-pdf-order']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    public boolean isAtCheckoutCompletePage() {
        elementActions.waitUntilUrlContains(PageUrls.CHECKOUT_COMPLETE);

        return navigationBar.isDisplayed()
                && elementActions.waitUntilVisible(checkoutHeader).getText().equals("Checkout: Complete!")
                && elementActions.waitUntilVisible(completeHeader).getText().equals("Thank you for your order!");
    }
}
