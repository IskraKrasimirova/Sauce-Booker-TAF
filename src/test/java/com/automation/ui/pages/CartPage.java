package com.automation.ui.pages;

import com.automation.ui.utilities.PageUrls;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {
    private final NavigationBar navigationBar;
    private final By cartHeader = By.xpath("//span[@data-test='title']");
    private final By cartList = By.xpath("//div[@data-test='cart-list']");
    private final By checkoutButton = By.xpath("//button[@data-test='checkout']");
    private final By continueShoppingButton = By.xpath("//button[@data-test='continue-shopping']");
    private final By cartItem = By.xpath("//div[@data-test='inventory-item']");
    private final By itemName = By.xpath(".//div[@data-test='inventory-item-name']");
    private final By itemPrice = By.xpath(".//div[@data-test='inventory-item-price']");
    private final By itemDescription = By.xpath(".//div[@data-test='inventory-item-desc']");
    private final By removeButton = By.xpath(".//button[contains(@data-test,'remove')]");

    public CartPage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public void goToProductsPage() {
        driver.findElement(continueShoppingButton).click();
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

    public void removeItem(String productName) {
        WebElement selectedItem = getItemByName(productName);

        if (selectedItem == null) {
            throw new IllegalStateException("Product was not found in the cart: " + productName);
        }

        selectedItem.findElement(removeButton).click();
    }

    public boolean isProductRemoved(String productName) {
        return getItemByName(productName) == null;
    }

    public boolean isAtCartPage() {
        elementActions.waitUntilUrlContains(PageUrls.CART);

        return navigationBar.isDisplayed()
                && elementActions.waitUntilVisible(cartHeader).getText().equals("Your Cart")
                && elementActions.waitUntilVisible(cartList).isDisplayed()
                && elementActions.waitUntilVisible(checkoutButton).isDisplayed()
                && elementActions.waitUntilVisible(continueShoppingButton).isDisplayed();
    }
}
