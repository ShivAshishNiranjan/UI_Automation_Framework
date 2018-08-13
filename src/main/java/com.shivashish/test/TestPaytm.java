package com.shivashish.test;

import com.shivashish.helper.InitializeDriver;
import com.shivashish.pages.Paytm;
import com.shivashish.utils.commonutils.DateUtils;
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
	String dumpFilePath ;
	String dumpFileName = "paytmDigitalGoldPriceTracker.txt";
	DateUtils dateUtils;


	@BeforeClass
	public void beforeClass() {
		logger.info("Inside Before Class");
		initializeDriver = new InitializeDriver();
		driver = initializeDriver.getDriver();
		paytm = new Paytm(driver);

		dateUtils = new DateUtils();

		dumpFilePath = System.getProperty("user.dir")+"/output";
	}


	@BeforeMethod
	public void beforeMethod() {
		logger.info("Inside Before Method");
		if (driver == null) {
			logger.error("Failed in Initializing Web Driver so skipping the test");
			throw new SkipException("Failed in Initializing Web Driver so skipping the test");
		}
		softAssert = new SoftAssert();
	}

	void dumpPriceInTextFile(String price)
	{
		try {

			String getCurrentTimeStamp = DateUtils.getCurrentDateIndd_MM_YYYY_HH_mm_ss();
			Double goldPrice = Double.parseDouble(price.substring(0, price.length() - 2));

			FileWriter writer = new FileWriter(dumpFilePath + "/" + dumpFileName, true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.newLine();
			bufferedWriter.write(getCurrentTimeStamp+"\t" + " -----> " +goldPrice.toString());
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (Exception e) {
			logger.error("Exception while dumping data in txt File {}", e.getStackTrace());
		}


	}

	@Test
	public void validateGoldPriceTabInPaytm() throws InterruptedException {
		driver.get(paytm.getDigitalGoldPageUrl());
		WebElement element = paytm.getGoldPriceElement();
		if (element != null) {
			String goldprice = element.getText().split("₹")[1];
			logger.info("goldPrice is : [{}] per gram", goldprice);
			dumpPriceInTextFile(goldprice);

		} else {
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
