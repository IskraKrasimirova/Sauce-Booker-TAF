package com.automation.ui.steps;

import com.automation.config.ConfigReader;
import com.automation.ui.driver.DriverFactory;
import com.automation.ui.pages.LoginPage;
import com.automation.ui.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginSteps {

    private final WebDriver driver;
    private final LoginPage loginPage;

    public LoginSteps() {
        this.driver = DriverFactory.getDriver();
        this.loginPage = new LoginPage(driver);
    }

    @Given("I am on the Login page")
    public void iAmOnTheLoginPage() {
        Assert.assertTrue(loginPage.isAtLoginPage(), "Login page is not loaded.");
    }

    @Given("I am logged in")
    public void iAmLoggedIn() {
        Assert.assertTrue(loginPage.isAtLoginPage(), "Login page is not loaded.");
        loginPage.loginWith(ConfigReader.getSettings().ui.username, ConfigReader.getSettings().ui.password);
    }

    @When("I log in with valid credentials")
    public void iLogInWithValidCredentials() {
        loginPage.loginWith(ConfigReader.getSettings().ui.username, ConfigReader.getSettings().ui.password);
    }

    @When("I log in with {string} and {string}")
    public void iLogInWithUsernameAndPassword(String username, String password) {
        loginPage.loginWith(username, password);
    }

    @Then("I should see the error message {string}")
    public void iShouldSeeTheErrorMessage(String expectedErrorMessage) {
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage,
                "Unexpected login error message.");
    }

    @Then("I should be redirected to the Products page")
    public void iShouldBeRedirectedToTheProductsPage() {
        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isAtProductsPage(), "Products page is not loaded.");
    }
}
