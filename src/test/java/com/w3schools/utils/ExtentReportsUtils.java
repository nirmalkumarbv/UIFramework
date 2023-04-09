package com.w3schools.utils;

import java.io.File;
import java.io.IOException;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import com.aventstack.extentreports.*;

public class ExtentReportsUtils {

	public static ExtentSparkReporter spark;
	public static ExtentReports extent;

	// helps to generate logs in the test report
	public static ExtentTest test;

	@BeforeSuite
	public void startTest() {
		try {
			spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/WebAutomationReport.html");
			extent = new ExtentReports();

			extent.attachReporter(spark);

			spark.config().setDocumentTitle("Test Automation Report");
			spark.config().setReportName("Web Automation Report");
			spark.config().setTheme(Theme.STANDARD);
			spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'"); // Thursday, April 05,
																							// 2023,10:15 AM(IST)

			System.out.println("Report");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@BeforeMethod
	public void simpleMethod() {
		System.out.println("Before Method");
	}

	@AfterMethod(alwaysRun = true)
	public void getResult(ITestResult result) {

		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, result.getTestName());
			System.out.println("Pass");
		} else if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, result.getThrowable());
			System.out.println("Fail");

		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, result.getTestName());
			System.out.println("Skip");

		}

	}

	public void setTCDesc(String testCaseName) {
		try {
			test = extent.createTest(testCaseName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void reportStep(String desc, String status) {

		/*
		 * 
		 * long number = (long) Math.floor(Math.random() * 900000000L)+10000000L; try {
		 * 
		 * File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		 * 
		 * FileUtils.copyFile(src , new File("reports/images/"+number+".jpg")); } catch
		 * (WebDriverException | IOException e) { e.printStackTrace(); }
		 */

		try {
			// Write if it is successful or failure or information
			if (status.toUpperCase().equals("PASS")) {
				// test.log(status,
				// desc+test.addScreenCapture("./../reports/images/"+number+".jpg"));
				test.log(Status.PASS, desc);
			} else if (status.toUpperCase().equals("FAIL")) {
				// test.log(LogStatus.FAIL,
				// desc+test.addScreenCapture("./../reports/images/"+number+".jpg"));
				test.log(Status.FAIL, desc);
			} else if (status.toUpperCase().equals("INFO")) {
//			test.log(status, desc);
				test.log(Status.SKIP, desc);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@AfterSuite
	public void endTest() {

		extent.flush();
	}

}
