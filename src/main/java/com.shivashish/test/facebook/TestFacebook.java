package com.shivashish.test.facebook;

import com.shivashish.helper.InitializeDriver;
import com.shivashish.pages.facebook.FacebookHomePage;
import com.shivashish.pages.facebook.FacebookLoginPage;
import com.shivashish.test.ConfigureConstant;
import com.shivashish.utils.commonutils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class TestFacebook {


    Logger logger = LoggerFactory.getLogger(TestFacebook.class);
    WebDriver driver;
    InitializeDriver initializeDriver;
    SoftAssert softAssert;
    FacebookLoginPage facebookLoginPage;
    FacebookHomePage facebookHomePage;
    boolean failedInConfigReading = false;
    private String userName;
    private String passWord;

    @BeforeClass
    public void beforeClass() {
        try {
            logger.info("Inside Before Class");
            initializeDriver = new InitializeDriver(ConfigureConstant.getConstantFieldsValue("browser"));
            driver = initializeDriver.getDriver();
            facebookLoginPage = new FacebookLoginPage(driver);

            userName = ConfigReader.getValueFromConfigFile("src/main/resources/config", "facebook.cfg", "login detail", "username");
            passWord = ConfigReader.getValueFromConfigFile("src/main/resources/config", "facebook.cfg", "login detail", "password");

        } catch (Exception e) {
            logger.error("Some Exception at Before Class Level : [{}]", e.getMessage());
            failedInConfigReading = true;
        }

    }


    @BeforeMethod
    public void beforeMethod() {
        logger.info("Inside Before Method");
        if (!failedInConfigReading) {
            if (driver == null) {
                logger.error("Failed in Initializing Web Driver so skipping the test");
                throw new SkipException("Failed in Initializing Web Driver so skipping the test");
            }

            softAssert = new SoftAssert();
        } else {
            throw new SkipException("Skipping all the tests");
        }
    }

    @Test
    public void TestLoginInFacebook() {

        facebookLoginPage.launchLoginPage();
        facebookLoginPage.enterUserName(userName);
        facebookLoginPage.enterPassword(passWord);

        facebookHomePage = facebookLoginPage.clickOnLoginButton();
        if (facebookHomePage.getHomeButton() != null) {
            logger.info("login Successful");
        } else {
            softAssert.assertTrue(false, "Login Failed : Not Being able to Find Home Button After Clicking on Login Button");
        }

    }

    @Test(dependsOnMethods = "TestLoginInFacebook")
    public void TestWishBirthdayOnFacebook() {

        if (facebookHomePage.clickOnBirthDayRemider()) {
            if (facebookHomePage.enterBirthMessage("Happy b'day :-)")) {

                if (facebookHomePage.clickOnBirthdayPostButtons()) {
                    logger.info("Test Passed :");
                } else {
                    softAssert.assertTrue(false, "Failed in Clicking on Post Button After Putting Birthday Wish");
                }

            } else {
                softAssert.assertTrue(false, "Failed in Putting Birthday Messaged in Input Box");
            }


        }


    }


    @AfterMethod

    public void afterMethod() {
        logger.info("Inside After Method");
        softAssert.assertAll();
    }


    @AfterClass

    public void afterClass() {

        logger.info("Inside After Class");
        driver.quit();

    }

}
