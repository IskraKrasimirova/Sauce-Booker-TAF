package com.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationBar extends BasePage{

    private final By menuButton = By.id("react-burger-menu-btn");
    private final By cartLink = By.xpath("//a[@data-test='shopping-cart-link']");
    private final By appLogo = By.className("app_logo");

    public NavigationBar(WebDriver driver) {
        super(driver);
    }

    public void openMenu(){
        elementActions.click(menuButton);
    }

    public void openCart(){
        elementActions.click(cartLink);
    }

    public boolean isDisplayed(){
        return elementActions.waitUntilVisible(appLogo).isDisplayed()
                && elementActions.waitUntilVisible(menuButton).isDisplayed()
                && elementActions.waitUntilVisible(cartLink).isDisplayed();
    }
}
