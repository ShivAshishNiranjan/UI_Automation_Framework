package com.shivashish.utils.commonutils;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class SeleniumCommonFunctions {


    public static void waitForGiveTimeIntervalInSeconds(WebDriver driver, int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }


}
