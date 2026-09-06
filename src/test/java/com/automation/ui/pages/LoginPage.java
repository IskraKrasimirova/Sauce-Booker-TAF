package com.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By loginLogo = By.className("login_logo");
    private final By usernameInput = By.xpath("//input[@data-test='username']");
    private final By passwordInput = By.xpath("//input[@data-test='password']");
    private final By loginButton = By.xpath("//input[@data-test='login-button']");
    private final By errorMessage = By.xpath("//h3[@data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginWith(String username, String password) {
        elementActions.enterText(usernameInput, username);
        elementActions.enterText(passwordInput, password);
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        return elementActions.waitUntilVisible(errorMessage).getText();
    }

    public boolean isAtLoginPage() {
        return elementActions.waitUntilVisible(loginLogo).isDisplayed();
    }
}
