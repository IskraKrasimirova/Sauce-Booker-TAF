package com.automation.ui.driver;

import com.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static WebDriver driver;

    private DriverFactory() {

    }

    public static WebDriver createDriver() {
        if (driver == null) {
            String browser = ConfigReader.getSettings().ui.browser;

            logger.info("Starting {} browser", browser);

            if (!browser.equalsIgnoreCase("chrome")) {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.password_manager_leak_detection", false);

            options.setExperimentalOption("prefs", prefs);

            if (ConfigReader.getSettings().ui.headless) {
                options.addArguments("--headless=new");
            }

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }

        return driver;
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver has not been created yet");
        }

        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            logger.info("Closing browser");
            driver.quit();
            driver = null;
        }
    }
}
