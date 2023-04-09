package com.w3schools.tests;

import org.junit.Assert;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.w3schools.pages.LoginPage;
import com.w3schools.utils.ExtentReportsUtils;
import com.w3schools.utils.Sewrappers;

public class LoginTest extends LoginPage{
	
	ExtentReportsUtils exUtils= new ExtentReportsUtils();
	
	@Test
	public void loginTest() throws InterruptedException
	{
		startBrowser();
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		
		exUtils.setTCDesc("TestCase 1");		
		login.setUserName("bv.nirmal@gmail.com");
		login.setPassword("Testing@123");
		login.login();
		Thread.sleep(7000);
		
		
		/*
		if("google" == "google")
		{
			ExtentReportsUtils.reportStep("TEST Case 1", "PASS");
		}
		*/
		
	}
	
	

}
