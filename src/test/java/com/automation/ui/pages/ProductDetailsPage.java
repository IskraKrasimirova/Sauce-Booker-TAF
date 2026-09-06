package com.automation.ui.pages;

import com.automation.ui.models.Product;
import com.automation.ui.utilities.PageUrls;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {
    private final NavigationBar navigationBar;
    private final By backToProductsButton = By.xpath("//button[@data-test='back-to-products']");
    private final By productName = By.xpath("//div[@data-test='inventory-item-name']");
    private final By productPrice = By.xpath("//div[@data-test='inventory-item-price']");
    private final By productDescription = By.xpath("//div[@data-test='inventory-item-desc']");
    private final By productImage = By.className("inventory_details_img");
    private final By addToCartButton = By.xpath("//button[@data-test='add-to-cart']");
    private final By removeButton = By.xpath("//button[@data-test='remove']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    public Product getProductDetails() {
        String name = elementActions.waitUntilVisible(productName).getText();
        String price = elementActions.waitUntilVisible(productPrice).getText();
        String description = elementActions.waitUntilVisible(productDescription).getText();

        return new Product(name, price, description);
    }

    public void addProductToCart() {
        driver.findElement(addToCartButton).click();
    }

    public boolean isProductAddedToCart() {
        return elementActions.waitUntilVisible(removeButton).isDisplayed();
    }

    public void goBackToProductsPage() {
        driver.findElement(backToProductsButton).click();
    }

    public boolean isAtProductDetailsPage() {
        elementActions.waitUntilUrlContains(PageUrls.PRODUCT_DETAILS);

        return navigationBar.isDisplayed()
                && elementActions.waitUntilVisible(productName).isDisplayed()
                && elementActions.waitUntilVisible(productImage).isDisplayed();
    }
}
