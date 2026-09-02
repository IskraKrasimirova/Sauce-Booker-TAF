package com.automation.ui.hooks;

import com.automation.config.ConfigReader;
import com.automation.ui.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class UiHooks {

    @Before("@ui")
    public void setUp(){
        DriverFactory.createDriver();
        DriverFactory.getDriver().get(ConfigReader.getSettings().ui.baseUrl);
    }

    @After("@ui")
    public void tearDown(){
        DriverFactory.closeDriver();
    }
}
