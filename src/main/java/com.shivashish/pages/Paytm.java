package com.shivashish.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Paytm {

    Logger logger = LoggerFactory.getLogger(Paytm.class);
    WebDriver driver;
    String digitalGoldPageUrl = "https://paytm.com/digitalgold";
    By getGoldBuyPrice = By.xpath("//div[text()='LIVE BUY PRICE']//..//div[3]");
    By getGoldSellPrice = By.xpath("//div[text()='LIVE SELL PRICE']//..//div[3]");
    By sellTab = By.xpath("//a[text()='Sell']");


    public Paytm(WebDriver driver) {
        this.driver = driver;
    }

    public String getDigitalGoldPageUrl() {
        return digitalGoldPageUrl;
    }

    public WebElement getGoldPriceBuyElement() {
        return driver.findElement(getGoldBuyPrice);
    }

    public WebElement getGoldPriceSellElement() {
        return driver.findElement(getGoldSellPrice);
    }

    public boolean clickOnSellTab() {
        WebElement element = driver.findElement(sellTab);
        if (element != null) {
            element.click();
            return true;
        } else {
            logger.error("Not Being able to Find Sell tab on given Page");
            return false;
        }


    }


}
