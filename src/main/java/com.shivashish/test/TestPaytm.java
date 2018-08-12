package com.shivashish.test;

import com.shivashish.helper.InitializeDriver;
import com.shivashish.pages.Paytm;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class TestPaytm {


    Logger logger = LoggerFactory.getLogger(TestPaytm.class);
    WebDriver driver;
    InitializeDriver initializeDriver;
    Paytm paytm;
    SoftAssert softAssert ;



    @BeforeClass
    public void beforeClass() {
        logger.info("Inside Before Class");
        initializeDriver = new InitializeDriver();
        driver = initializeDriver.getDriver();
        paytm = new Paytm(driver);
    }


    @BeforeMethod
    public void beforeMethod() {
        logger.info("Inside Before Method");
        softAssert = new SoftAssert() ;
    }

    @Test
    public void validateGoldPriceTabInPaytm() throws InterruptedException {
        driver.get(paytm.getDigitalGoldPageUrl());
        WebElement element = paytm.getGoldPriceElement();
        if(element!=null) {
            String goldprice = element.getText().split("₹")[1];
            logger.info("goldPrice is : [{}] per gram", goldprice);
        }
        else
        {
            softAssert.assertTrue(false, "Not being able to get live digital " +
                    "gold price from gold tab in Paytm");
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
