package com.shivashish.pages.facebook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FacebookHomePage {


    Logger logger = LoggerFactory.getLogger(FacebookHomePage.class);
    WebDriver driver;
    By homeButton = By.xpath("//a[text()='Home']");
    By birthDayReminder = By.xpath("//div[@class = 'fbReminders']");
    By birthdayInputTexts = By.xpath("//button[text()='Post']/../../div[1][@class='uiMentionsInput']");
    By birthdayPostButtons = By.xpath("//button[text()='Post'][not(@disabled)]");
    By likeButton = By.xpath("//a[text()='Like'][not(@data-testid='fb-ufi-unlikelink')]");

    public FacebookHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getHomeButton() {
        return driver.findElement(homeButton);
    }


    public boolean clickOnBirthDayReminder() {
        if (driver.findElement(birthDayReminder) != null) {
            driver.findElement(birthDayReminder).click();
            return true;
        } else {
            logger.info("There is not any Facebook Reminder for Today");
            return false;
        }
    }

    public boolean clickOnBirthdayPostButtons() {
        List<WebElement> elements = this.getBirthdayWishesPostsButton();
        if (elements != null && !elements.isEmpty()) {
            for (WebElement element : elements) {
                element.click();
            }
            return true;

        } else
            return false;

    }

    public boolean enterBirthMessage(String message) {
        List<WebElement> elements = this.getAllBirthDayInputTextElements();
        if (elements != null && !elements.isEmpty()) {
            for (WebElement element : elements) {
                element.sendKeys(message);
            }
            return true;
        } else
            return false;

    }

    public int likeAllPost(WebDriver driver) {
        int i = 0;
        try {

            List<WebElement> elements = this.getAllLikeButtons();

            logger.info("Total Element Found [{}]",elements.size());

            if (elements != null && !elements.isEmpty()) {
                for (WebElement element : elements) {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(element).click().perform();
                    i++;
                }

            }
        }
        catch (Exception e)
        {
            logger.error("Exception : [{}]",e.getLocalizedMessage());
        }

        return i;
    }


    private List<WebElement> getAllBirthDayInputTextElements() {
        List<WebElement> elements = driver.findElements(birthdayInputTexts);
        return elements;
    }

    private List<WebElement> getBirthdayWishesPostsButton() {
        List<WebElement> elements = driver.findElements(birthdayPostButtons);
        return elements;
    }

    private List<WebElement> getAllLikeButtons() {
        List<WebElement> elements = driver.findElements(likeButton);
        return elements;
    }


}
