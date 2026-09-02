package com.automation.ui.pages;

import com.automation.ui.models.Product;
import com.automation.ui.utilities.PageUrls;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {
    private final NavigationBar navigationBar;
    private final By productsHeader = By.xpath("//span[@data-test='title']");
    private final By product = By.xpath("//div[@data-test='inventory-item']");
    private final By productName = By.xpath(".//div[@data-test='inventory-item-name']");
    private final By productPrice = By.xpath(".//div[@data-test='inventory-item-price']");
    private final By productDescription = By.xpath(".//div[@data-test='inventory-item-desc']");
    private final By addToCartButton = By.xpath(".//button[contains(@data-test,'add-to-cart')]");
    private final By removeButton = By.xpath(".//button[contains(@data-test,'remove')]");

    public ProductsPage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public List<WebElement> getProducts() {
        elementActions.waitUntilVisible(product);
        return driver.findElements(product);
    }

    public Product getProduct(int index) {
        List<WebElement> products = getProducts();
        WebElement selectedProduct = products.get(index);

        String name = selectedProduct.findElement(productName).getText();
        String price = selectedProduct.findElement(productPrice).getText();
        String description = selectedProduct.findElement(productDescription).getText();

        return new Product(name, price, description);
    }

    public Product addProductToCart(int index) {
        Product selectedProduct = getProduct(index);

        WebElement productElement = getProducts().get(index);
        productElement.findElement(addToCartButton).click();

        return selectedProduct;
    }

    public boolean isProductAddedToCart(int index) {
        List<WebElement> products = getProducts();
        WebElement selectedProduct = products.get(index);

        return elementActions.waitUntilVisible(selectedProduct, removeButton).isDisplayed();
    }

    public boolean isProductNotAddedToCart(int index) {
        List<WebElement> products = getProducts();
        WebElement selectedProduct = products.get(index);

        return elementActions.waitUntilVisible(selectedProduct, addToCartButton).isDisplayed();
    }

    public void openProductDetails(int index) {
        List<WebElement> products = getProducts();
        WebElement selectedProduct = products.get(index);

        selectedProduct.findElement(productName).click();
    }

    public boolean isAtProductsPage() {
        elementActions.waitUntilUrlContains(PageUrls.PRODUCTS);

        return navigationBar.isDisplayed()
                && elementActions.waitUntilVisible(productsHeader).getText().equals("Products")
                && !getProducts().isEmpty();
    }
}
