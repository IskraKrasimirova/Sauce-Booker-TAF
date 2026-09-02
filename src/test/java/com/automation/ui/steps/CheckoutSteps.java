package com.automation.ui.steps;

import com.automation.ui.context.ContextConstants;
import com.automation.ui.context.ScenarioContext;
import com.automation.ui.driver.DriverFactory;
import com.automation.ui.factories.CheckoutInfoFactory;
import com.automation.ui.models.CheckoutInfo;
import com.automation.ui.models.Product;
import com.automation.ui.pages.*;
import com.automation.ui.utilities.PriceUtils;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutSteps {
    private final ScenarioContext scenarioContext;
    private final WebDriver driver;
    private final ProductsPage productsPage;
    private final ProductDetailsPage productDetailsPage;
    private final CartPage cartPage;
    private final CheckoutInfoPage checkoutInfoPage;
    private final CheckoutOverviewPage checkoutOverviewPage;
    private final CheckoutCompletePage checkoutCompletePage;

    public CheckoutSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.driver = DriverFactory.getDriver();
        this.productsPage = new ProductsPage(driver);
        this.productDetailsPage = new ProductDetailsPage(driver);
        this.cartPage = new CartPage(driver);
        this.checkoutInfoPage = new CheckoutInfoPage(driver);
        this.checkoutOverviewPage = new CheckoutOverviewPage(driver);
        this.checkoutCompletePage = new CheckoutCompletePage(driver);
    }

    @And("I enter valid checkout information")
    public void iEnterValidCheckoutInformation() {
        CheckoutInfo checkoutInfo = CheckoutInfoFactory.createValidCheckoutInfo();
        checkoutInfoPage.enterCheckoutInformation(checkoutInfo);
    }

    @And("I continue to the checkout overview")
    public void iContinueToTheCheckoutOverview() {
        checkoutInfoPage.continueToCheckoutOverview();

        Assert.assertTrue(checkoutOverviewPage.isAtCheckoutOverviewPage(), "Checkout Overview Page is not loaded");
    }

    @Then("the checkout overview should contain the selected product")
    public void theCheckoutOverviewShouldContainTheSelectedProduct() {
        Product selectedProduct = scenarioContext.get(ContextConstants.SELECTED_PRODUCT, Product.class);
        String selectedProductName = selectedProduct.getName();
        String selectedProductPrice = selectedProduct.getPrice();
        String selectedProductDescription = selectedProduct.getDescription();

        WebElement overviewItem = checkoutOverviewPage.getItemByName(selectedProductName);

        Assert.assertNotNull(overviewItem, "Selected product was not found on the Checkout Overview page.");

        String itemPrice = checkoutOverviewPage.getItemPrice(overviewItem);
        String itemDescription = checkoutOverviewPage.getItemDescription(overviewItem);

        Assert.assertEquals(itemPrice, selectedProductPrice, "Product price was not correct on the Checkout Overview page.");
        Assert.assertEquals(itemDescription, selectedProductDescription, "Product description was not correct on the Checkout Overview page.");
    }

    @And("the order total should be correct")
    public void theOrderTotalShouldBeCorrect() {
        BigDecimal expectedItemTotal = BigDecimal.ZERO;
        List<Product> remainingProducts = scenarioContext.getList(ContextConstants.SELECTED_PRODUCTS);

        if (remainingProducts != null) {
            for (Product product : remainingProducts) {
                BigDecimal productPrice = PriceUtils.parsePrice(product.getPrice());

                expectedItemTotal = expectedItemTotal.add(productPrice);
            }
        } else {
            Product selectedProduct = scenarioContext.get(ContextConstants.SELECTED_PRODUCT, Product.class);
            expectedItemTotal = PriceUtils.parsePrice(selectedProduct.getPrice());
        }

        BigDecimal itemTotal = PriceUtils.parsePrice(checkoutOverviewPage.getItemTotal());
        Assert.assertEquals(itemTotal, expectedItemTotal, "Item total is not correct.");

        BigDecimal tax = PriceUtils.parsePrice(checkoutOverviewPage.getTax());
        BigDecimal total = PriceUtils.parsePrice(checkoutOverviewPage.getTotalPrice());
        BigDecimal expectedTotal = expectedItemTotal.add(tax);

        Assert.assertEquals(total, expectedTotal, "Order total is not correct.");
    }

    @And("I finish the order successfully")
    public void iFinishTheOrderSuccessfully() {
        checkoutOverviewPage.finishOrder();
        Assert.assertTrue(checkoutCompletePage.isAtCheckoutCompletePage(),
                "Checkout Complete Page is not loaded");
    }

    @And("I enter checkout information with missing {string}")
    public void iEnterCheckoutInformationWithMissing(String field) {
        CheckoutInfo checkoutInfo = CheckoutInfoFactory.createWithMissingField(field);
        checkoutInfoPage.enterCheckoutInformation(checkoutInfo);
    }

    @And("I submit the invalid checkout information")
    public void iSubmitTheInvalidCheckoutInformation() {
        checkoutInfoPage.continueToCheckoutOverview();
        Assert.assertTrue(checkoutInfoPage.isAtCheckoutInfoPage(),
                "Checkout Information Page is not displayed after submitting invalid data.");
    }

    @Then("I should see the checkout error message {string}")
    public void iShouldSeeTheCheckoutErrorMessage(String expectedErrorMessage) {
        Assert.assertEquals(checkoutInfoPage.getErrorMessage(), expectedErrorMessage,
                "Checkout Error message is not correct.");
    }

    @Then("the checkout overview should contain the remaining products")
    public void theCheckoutOverviewShouldContainTheRemainingProducts() {
        List<Product> remainingProducts = scenarioContext.getList(ContextConstants.SELECTED_PRODUCTS);
        List<WebElement> checkoutOverviewItems = checkoutOverviewPage.getItems();

        Assert.assertEquals(checkoutOverviewItems.size(), remainingProducts.size(),
                "Unexpected number of products on the Checkout Overview page.");

        for (Product product : remainingProducts) {
            String productName = product.getName();
            String productPrice = product.getPrice();
            String productDescription = product.getDescription();
            WebElement overviewItem = checkoutOverviewPage.getItemByName(productName);

            Assert.assertNotNull(overviewItem,
                    "Product was not found on the Checkout Overview page: " + productName);

            String itemPrice = checkoutOverviewPage.getItemPrice(overviewItem);
            String itemDescription = checkoutOverviewPage.getItemDescription(overviewItem);

            Assert.assertEquals(itemPrice, productPrice,
                    "Incorrect price for product: " + productName);
            Assert.assertEquals(itemDescription, productDescription,
                    "Incorrect description for product: " + productName);
        }
    }
}
