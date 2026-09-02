package com.automation.ui.pages;

import com.automation.ui.utilities.ElementActions;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final ElementActions elementActions;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }
}
