package com.shivashish.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Paytm {

    WebDriver driver;
    String digitalGoldPageUrl = "https://paytm.com/digitalgold";
    By getGoldPrice = By.xpath("//div[text()='LIVE BUY PRICE']//..//div[3]");


    public Paytm(WebDriver driver) {
        this.driver = driver;
    }

    public String getDigitalGoldPageUrl() {
        return digitalGoldPageUrl;
    }

    public WebElement getGoldPriceElement()
    {
        return  driver.findElement(getGoldPrice);
    }


}
