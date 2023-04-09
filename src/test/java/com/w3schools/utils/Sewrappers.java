package com.w3schools.utils;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Sewrappers {
	
	public static WebDriver driver= null;

	public void startBrowser()
	{
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get("https://profile.w3schools.com/");
	}
	
	
	public void typeText(WebElement element,String msg)
	{
		
		element.sendKeys(msg);
		
	}
	
	public void clickAction(WebElement ele)
	{
		ele.click();
	}
	
	//click
	
	//javascript
	
	
	
	//select
	
	//
	

}
