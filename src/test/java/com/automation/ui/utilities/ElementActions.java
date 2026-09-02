package com.automation.ui.utilities;

import com.automation.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementActions {
    private final WebDriver driver;
    private final int defaultTimeoutSeconds;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeoutSeconds = ConfigReader.getSettings().ui.timeoutSeconds;
    }

    public WebElement waitUntilVisible(By locator) {
        return waitUntilVisible(locator, defaultTimeoutSeconds);
    }

    public WebElement waitUntilVisible(By locator, int timeoutSeconds) {
        return createWait(timeoutSeconds)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitUntilClickable(By locator) {
        return waitUntilClickable(locator, defaultTimeoutSeconds);
    }

    public WebElement waitUntilClickable(By locator, int timeoutSeconds) {
        return createWait(timeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void enterText(By locator, String text) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void click(By locator) {
        waitUntilClickable(locator).click();
    }

    public void waitUntilUrlContains(String expectedPath) {
        createWait(defaultTimeoutSeconds).until(ExpectedConditions.urlContains(expectedPath));
    }

    public WebElement waitUntilVisible(WebElement parent, By locator) {
        return waitUntilVisible(parent, locator, defaultTimeoutSeconds);
    }

    private WebElement waitUntilVisible(WebElement parent, By locator, int timeoutSeconds) {
        return createWait(timeoutSeconds)
                .until(driver -> {
                    WebElement element = parent.findElement(locator);
                    return element.isDisplayed() ? element : null;
                });
    }

    private WebDriverWait createWait(int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(timeoutSeconds)
        );

        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);

        return wait;
    }
}
