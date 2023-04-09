package com.w3schools.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.w3schools.utils.Sewrappers;

public class LoginPage extends Sewrappers {
	
	/*
	
	WebDriver driver;
	
	   public LoginPage(WebDriver driver){ 
           this.driver=driver; 
  }
  */
	
	
	@FindBy(id="modalusername")
	WebElement username;
	
	@FindBy(xpath="//input[@id='current-password']")
	WebElement password;
	
	@FindBy(xpath="//span[text()='Log in']")
	WebElement login;
	
	public void setUserName(String user)
	{
		
		//username.sendKeys(user);
		typeText(username, user);
	}
	
	public void setPassword(String pass)
	{
		typeText(password,pass);
//		password.sendKeys(pass);
		
	}
	
	public void login()
	{
		login.click();
	}
	
	
	

}
