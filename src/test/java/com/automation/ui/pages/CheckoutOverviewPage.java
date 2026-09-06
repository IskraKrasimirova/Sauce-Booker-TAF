package com.automation.ui.pages;

import com.automation.ui.utilities.PageUrls;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckoutOverviewPage extends BasePage {
    private final NavigationBar navigationBar;
    private final By checkoutHeader = By.xpath("//span[@data-test='title']");
    private final By cartList = By.xpath("//div[@data-test='cart-list']");
    private final By cartItem = By.xpath("//div[@data-test='inventory-item']");
    private final By itemName = By.xpath(".//div[@data-test='inventory-item-name']");
    private final By itemPrice = By.xpath(".//div[@data-test='inventory-item-price']");
    private final By itemDescription = By.xpath(".//div[@data-test='inventory-item-desc']");
    private final By finishButton = By.xpath("//button[@data-test='finish']");
    private final By cancelButton = By.xpath("//button[@data-test='cancel']");
    private final By itemTotal = By.xpath("//div[@data-test='subtotal-label']");
    private final By tax = By.xpath("//div[@data-test='tax-label']");
    private final By totalPrice = By.xpath("//div[@data-test='total-label']");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    public void finishOrder() {
        driver.findElement(finishButton).click();
    }

    public List<WebElement> getItems() {
        return driver.findElements(cartItem);
    }

    public WebElement getItemByName(String productName) {
        return getItems().stream()
                .filter(item -> item.findElement(itemName).getText().equals(productName))
                .findFirst()
                .orElse(null);
    }

    public String getItemPrice(WebElement item) {
        return item.findElement(itemPrice).getText();
    }

    public String getItemDescription(WebElement item) {
        return item.findElement(itemDescription).getText();
    }

    public String getItemTotal() {
        return elementActions.waitUntilVisible(itemTotal).getText();
    }

    public String getTax() {
        return elementActions.waitUntilVisible(tax).getText();
    }

    public String getTotalPrice() {
        return elementActions.waitUntilVisible(totalPrice).getText();
    }

    public boolean isAtCheckoutOverviewPage() {
        elementActions.waitUntilUrlContains(PageUrls.CHECKOUT_OVERVIEW);

        return navigationBar.isDisplayed()
                && elementActions.waitUntilVisible(checkoutHeader).getText().equals("Checkout: Overview")
                && elementActions.waitUntilVisible(cartList).isDisplayed();
    }
}
