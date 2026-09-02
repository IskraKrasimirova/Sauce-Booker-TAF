package com.automation.ui.steps;

import com.automation.ui.context.ContextConstants;
import com.automation.ui.context.ScenarioContext;
import com.automation.ui.driver.DriverFactory;
import com.automation.ui.models.Product;
import com.automation.ui.pages.CartPage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class CartSteps {
    private final ScenarioContext scenarioContext;
    private final WebDriver driver;
    private final CartPage cartPage;

    public CartSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.driver = DriverFactory.getDriver();
        this.cartPage = new CartPage(driver);
    }

    @And("the cart should contain the selected product")
    public void theCartShouldContainTheSelectedProduct() {
        Product selectedProduct = scenarioContext.get(ContextConstants.SELECTED_PRODUCT, Product.class);
        String selectedProductName = selectedProduct.getName();
        String selectedProductPrice = selectedProduct.getPrice();
        String selectedProductDescription = selectedProduct.getDescription();

        WebElement cartItem = cartPage.getItemByName(selectedProductName);

        Assert.assertNotNull(cartItem, "Selected product was not found in the cart.");

        String itemPrice = cartPage.getItemPrice(cartItem);
        String itemDescription = cartPage.getItemDescription(cartItem);

        Assert.assertEquals(itemPrice, selectedProductPrice, "Price of the product was not correct.");
        Assert.assertEquals(itemDescription, selectedProductDescription, "Description of the product was not correct.");
    }

    @And("the product is removed from the cart")
    public void theProductIsRemovedFromTheCart() {
        Product selectedProduct  = scenarioContext.get(ContextConstants.SELECTED_PRODUCT, Product.class);
        String selectedProductName = selectedProduct.getName();
        cartPage.removeItem(selectedProductName);

        Assert.assertTrue(cartPage.isProductRemoved(selectedProductName), "Product was not removed from the cart.");
    }
}
