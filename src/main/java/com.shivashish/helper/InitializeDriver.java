package com.shivashish.helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class InitializeDriver {

	WebDriver driver;


	public InitializeDriver(String browserName) {

		setGekoDriverBasedOnOS(browserName);
		if (browserName.toLowerCase().contains("chrome")) {
			driver = new ChromeDriver();
		} else if (browserName.toLowerCase().contains("firefox")) {
			driver = new FirefoxDriver();
		}


	}


	// in case browser name is not provided then default browser will be taken as firefox
	public InitializeDriver() {

		setGekoDriverBasedOnOS("firefox");
		driver = new FirefoxDriver();
	}

	public void setGekoDriverBasedOnOS(String browserName) {

		String os = System.getProperty("os.name").toLowerCase();
		String driverName = "geckodriver"; //default value
		String propertyName = "webdriver.gecko.driver";//default value
		String commonPath = "/src/main/resources/GekoDriver/";

		if (browserName.contains("chrome")) {
			driverName = "chromedriver";
			propertyName = "webdriver.chrome.driver";
		}


		if (os.contains("mac")) {
			System.setProperty(propertyName, System.getProperty("user.dir") + commonPath + browserName + "/Mac/" + driverName);
		}
		if (os.contains("linux")) {
			System.setProperty(propertyName, System.getProperty("user.dir") + commonPath + browserName + "/Linux/" + driverName);
		}
		if (os.contains("windows")) {
			System.setProperty(propertyName, System.getProperty("user.dir") + commonPath + browserName + "/Windows/" + driverName + ".exe");
		}


	}

	public WebDriver getDriver() {
		return driver;
	}

	public void closeCurrentTab() {
		driver.close();

	}

	public void quitCurrentTab() {
		driver.quit();

	}


}
