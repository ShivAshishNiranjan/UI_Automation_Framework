package com.shivashish.helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class InitializeDriver {

    WebDriver driver;

    // @Todo
    InitializeDriver(String browserName) {


    }


    public InitializeDriver() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/src/main/resources/GekoDriver/Mac/geckodriver");

        }
        if (os.contains("firefox")) {
            System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/src/main/resources/GekoDriver/Linux/geckodriver");

        }

        driver = new FirefoxDriver();
    }


    public WebDriver getDriver() {
        return driver;
    }

    public void closeCurrentTab() {
        driver.close();

    }

    public void quitCurrentTab() {
        driver.quit();

    }


}
