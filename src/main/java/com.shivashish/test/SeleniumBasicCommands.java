package com.shivashish.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//comment the above line and uncomment below line to use Chrome
//import org.openqa.selenium.chrome.ChromeDriver;
public class SeleniumBasicCommands  {


	public static void main(String[] args) {


		Logger logger = LoggerFactory.getLogger(SeleniumBasicCommands.class);
		// declaration and instantiation of objects/variables
		//System.setProperty("webdriver.gecko.driver","/home/shivashish/Desktop/MyGitRepo/UI_Automation_Framework/src/main/resources/GekoDriver/geckodriver");
		String os = System.getProperty("os.name").toLowerCase();


		if(os.contains("mac"))
		{
			System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"/src/resources/GekoDriver/Mac/geckodriver");

		}
		if(os.contains("firefox"))
		{
			System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"/src/resources/GekoDriver/Linux/geckodriver");

		}

		WebDriver driver = new FirefoxDriver();


		String baseUrl = "http://demo.guru99.com/test/newtours/";
		String expectedTitle = "Welcome: Mercury Tours";
		String actualTitle = "";

		// launch Fire fox and direct it to the Base URL
		driver.get(baseUrl);

		// get the actual value of the title
		actualTitle = driver.getTitle();

		/*
		 * compare the actual title of the page with the expected one and print
		 * the result as "Passed" or "Failed"
		 */
		if (actualTitle.contentEquals(expectedTitle)){
			System.out.println("Test Passed!");
		} else {
			System.out.println("Test Failed");
		}

		//close Firefox
		driver.close();

	}

}