package com.automation.ui.steps;

import com.automation.ui.context.ContextConstants;
import com.automation.ui.context.ScenarioContext;
import com.automation.ui.driver.DriverFactory;
import com.automation.ui.models.Product;
import com.automation.ui.pages.CartPage;
import com.automation.ui.pages.ProductDetailsPage;
import com.automation.ui.pages.ProductsPage;
import com.automation.ui.utilities.ProductSelectionHelper;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class ProductsSteps {
    private final WebDriver driver;
    private final ProductsPage productsPage;
    private final ProductDetailsPage productDetailsPage;
    private final CartPage cartPage;
    private final ScenarioContext scenarioContext;

    public ProductsSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.driver = DriverFactory.getDriver();
        this.productsPage = new ProductsPage(driver);
        this.productDetailsPage = new ProductDetailsPage(driver);
        this.cartPage = new CartPage(driver);
    }

    @And("I am on the Products page")
    public void iAmOnTheProductsPage() {
        Assert.assertTrue(productsPage.isAtProductsPage(), "Product page is not loaded.");
    }

    @When("I add a product to the cart")
    public void iAddAProductToTheCart() {
        int productsCount = productsPage.getProducts().size();
        int index = ProductSelectionHelper.getRandomProductIndex(productsCount);
        Product selectedProduct = productsPage.addProductToCart(index);

        scenarioContext.set(ContextConstants.SELECTED_PRODUCT, selectedProduct);
        scenarioContext.set(ContextConstants.SELECTED_PRODUCT_INDEX, index);
    }

    @When("I open the details of a product")
    public void iOpenTheDetailsOfAProduct() {
        int productsCount = productsPage.getProducts().size();
        int index = ProductSelectionHelper.getRandomProductIndex(productsCount);
        Product selectedProduct = productsPage.getProduct(index);

        scenarioContext.set(ContextConstants.SELECTED_PRODUCT, selectedProduct);
        scenarioContext.set(ContextConstants.SELECTED_PRODUCT_INDEX, index);

        productsPage.openProductDetails(index);

        Assert.assertTrue(productDetailsPage.isAtProductDetailsPage(), "Product details page is not loaded.");
    }

    @And("I add the product to the cart from the product details page")
    public void iAddTheProductToTheCartFromTheProductDetailsPage() {
        productDetailsPage.addProductToCart();
    }

    @Then("the selected product should be marked as added")
    public void theSelectedProductShouldBeMarkedAsAdded() {
        int selectedProductIndex = scenarioContext.get(ContextConstants.SELECTED_PRODUCT_INDEX, Integer.class);

        Assert.assertTrue(productsPage.isProductAddedToCart(selectedProductIndex), "Product is not added.");
    }


    @Then("the product should be marked as added on the product details page")
    public void theProductShouldBeMarkedAsAddedOnTheProductDetailsPage() {
        Assert.assertTrue(productDetailsPage.isProductAddedToCart(), "Product is not added.");
    }

    @And("the product details should match the selected product")
    public void theProductDetailsShouldMatchTheSelectedProduct() {
        Product expectedProduct = scenarioContext.get(ContextConstants.SELECTED_PRODUCT, Product.class);
        Product actualProduct = productDetailsPage.getProductDetails();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actualProduct.getName(), expectedProduct.getName(), "Product name is not matched.");
        softAssert.assertEquals(actualProduct.getPrice(), expectedProduct.getPrice(), "Product price is not matched.");
        softAssert.assertEquals(actualProduct.getDescription(), expectedProduct.getDescription(), "Product description is not matched.");
        softAssert.assertAll();
    }

    @And("the product should be marked as added on the Products page")
    public void theProductShouldBeMarkedAsAddedOnTheProductsPage() {
        productDetailsPage.goBackToProductsPage();
        Assert.assertTrue(productsPage.isAtProductsPage(), "Product page is not loaded.");

        int index = scenarioContext.get(ContextConstants.SELECTED_PRODUCT_INDEX, Integer.class);
        Assert.assertTrue(productsPage.isProductAddedToCart(index), "Product is not marked as added.");
    }

    @And("I open the cart")
    public void iOpenTheCart() {
        productsPage.getNavigationBar().openCart();
        Assert.assertTrue(cartPage.isAtCartPage(), "Cart page is not loaded.");
    }

    @And("the product should be marked as not added on the Products page")
    public void theProductShouldBeMarkedAsNotAddedOnTheProductsPage() {
        cartPage.goToProductsPage();
        Assert.assertTrue(productsPage.isAtProductsPage(), "Product page is not loaded.");

        int index = scenarioContext.get(ContextConstants.SELECTED_PRODUCT_INDEX, Integer.class);
        Assert.assertTrue(productsPage.isProductNotAddedToCart(index), "Product is still marked as added.");
    }
}
