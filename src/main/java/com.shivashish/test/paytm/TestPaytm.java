package com.shivashish.test.paytm;

import com.shivashish.helper.InitializeDriver;
import com.shivashish.pages.paytm.Paytm;
import com.shivashish.test.ConfigureConstant;
import com.shivashish.utils.commonutils.ConfigReader;
import com.shivashish.utils.commonutils.DateUtils;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class TestPaytm {


    Logger logger = LoggerFactory.getLogger(TestPaytm.class);
    WebDriver driver;
    InitializeDriver initializeDriver;
    Paytm paytm;
    SoftAssert softAssert;
    String dumpFilePath;
    String dumpFileName = "paytmDigitalGoldPriceTracker.txt";
    DateUtils dateUtils;

    Double totalMoneySpent;
    Double totalGoldInGram;
    boolean failedInConfigReading = false;


    @BeforeClass
    public void beforeClass(){
        try {
            logger.info("Inside Before Class");
            initializeDriver = new InitializeDriver(ConfigureConstant.getConstantFieldsValue("browser"));
            driver = initializeDriver.getDriver();
            paytm = new Paytm(driver);

            dateUtils = new DateUtils();
            dumpFilePath = System.getProperty("user.dir") + "/output";


            totalMoneySpent = Double.parseDouble(ConfigReader.getValueFromConfigFile("src/main/resources/config", "paytmdigitalgold.cfg", "buying detail", "totalmoneyspent"));
            totalGoldInGram = Double.parseDouble(ConfigReader.getValueFromConfigFile("src/main/resources/config", "paytmdigitalgold.cfg", "buying detail", "totalgoldingram"));


            logger.debug("totalMoneySpent [{}]", totalMoneySpent);
            logger.debug("totalGoldInGram [{}]", totalGoldInGram);
        } catch (Exception e) {
            logger.error("Some Exception at Before Class Level : [{}]", e.getMessage());
            failedInConfigReading = true;
        }


    }


    @BeforeMethod
    public void beforeMethod() {
        if (!failedInConfigReading) {
            logger.info("Inside Before Method");
            if (driver == null) {
                logger.error("Failed in Initializing Web Driver so skipping the test");
                throw new SkipException("Failed in Initializing Web Driver so skipping the test");
            }
            softAssert = new SoftAssert();
        } else {
            throw new SkipException("Skipping all the tests");
        }
    }

    void dumpPriceInTextFile(String price) {
        try {

            String getCurrentTimeStamp = DateUtils.getCurrentDateIndd_MM_YYYY_HH_mm_ss();
            Double goldPrice = Double.parseDouble(price.substring(0, price.length() - 2));

            FileWriter writer = new FileWriter(dumpFilePath + "/" + dumpFileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.newLine();
            bufferedWriter.write(getCurrentTimeStamp + "\t" + " -----> " + goldPrice.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            logger.error("Exception while dumping data in txt File {}", e.getStackTrace());
        }


    }

    @Test
    public void validateGoldPriceTabBuyingRateInPaytm() {
        driver.get(paytm.getDigitalGoldPageUrl());
        WebElement element = paytm.getGoldPriceBuyElement();
        if (element != null) {
            String goldPrice = element.getText().split("₹")[1];
            logger.info("Buy goldPrice is : [{}] per gram", goldPrice);
            dumpPriceInTextFile(goldPrice);

        } else {
            softAssert.assertTrue(false, "Not being able to get live digital " +
                    "gold buy price from gold tab in FacebookHomePage");
        }

    }

    @Test(dependsOnMethods = "validateGoldPriceTabBuyingRateInPaytm")
    public void validateGoldPriceTabSellingRateInPaytm() {

        driver.get(paytm.getDigitalGoldPageUrl());
        if (paytm.clickOnSellTab()) {
            WebElement element = paytm.getGoldPriceSellElement();
            if (element != null) {
                String goldPrice = element.getText().split("₹")[1];
                logger.info("Sell goldPrice is : [{}] per gram", goldPrice);

                Double goldSellPrice = Double.parseDouble(goldPrice.substring(0, goldPrice.length() - 2));

                if (goldSellPrice * totalGoldInGram > totalMoneySpent) {
                    logger.info("Total Profit is :[{}]", goldSellPrice * totalGoldInGram - totalMoneySpent);
                } else {
                    logger.info("Total Loss is :[{}]", totalMoneySpent - goldSellPrice * totalGoldInGram);

                }

            } else {
                softAssert.assertTrue(false, "Not being able to get live digital " +
                        "gold sell price from gold tab in FacebookHomePage");
            }
        } else {
            softAssert.assertTrue(false, "");
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
