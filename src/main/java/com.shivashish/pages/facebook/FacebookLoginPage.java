package com.shivashish.pages.facebook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookLoginPage {

    Logger logger = LoggerFactory.getLogger(FacebookLoginPage.class);
    WebDriver driver;
    private String loginUrl = "https://www.facebook.com/";
    By userNameField = By.xpath("//input[@id='email']");
    By passwordField = By.xpath("//input[@id='pass']");
    By loginButton = By.id("loginbutton");

    FacebookHomePage facebookHomePage;

    public FacebookLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void launchLoginPage()
    {
        driver.get(loginUrl);
    }


    public void enterUserName(String userName) {
        driver.findElement(userNameField).sendKeys(userName);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }


    public FacebookHomePage clickOnLoginButton() {
        driver.findElement(loginButton).click();
        facebookHomePage = new FacebookHomePage(driver);
        return facebookHomePage ;
    }


}
