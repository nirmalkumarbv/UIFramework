package com.w3schools.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;






public class BaseText_bkp {

	//	public static WebDriver driver;
	protected String browser = null;
	//Base class configuration file
	//	protected static Properties prop = new Properties();
	//packages page configuration file 
	protected static Properties packagesProperties = new Properties();

	File baseConfigFile = new File("configuration/config.properties");
	public WebDriverWait wait = null;
	WebElement highlightElement;

	// public static ExtentTest extentTest;
	FluentWait<WebDriver> waiter;


	//Packages page input
	public static RemoteWebDriver driver;
	public static String sUrl,swissUrl,sHubUrl,sHubPort,
	 fromMail, toMail1, localEnvUrl,browserName,
	sesHub, sesPort, sesUsername,sesPassword, toMail, ccMail, remoteHub, remotePort, swissUsername,swissPassword;


	protected Properties prop;
	public static boolean browserVal=false;

	public BaseText_bkp() {
		prop = new Properties();
		try {

			prop.load(new FileInputStream(new File(System.getProperty("user.dir")+"/configuration/config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			browserName=prop.getProperty("browser");
			swissUrl= prop.getProperty("swissUrl");
			swissUsername=prop.getProperty("swissUser");
			swissPassword=prop.getProperty("swissPass");
			toMail1 = prop.getProperty("toMail1");
			ccMail = prop.getProperty("ccMail");


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void startApp(String browser, boolean bRemote, String env) {
		try {
			//Define desired capabilities
			DesiredCapabilities cap=new DesiredCapabilities();

			// this is for grid run
			if(bRemote)
			{

				try {

					/*
					 * Parallel run for Zalenium. Commenting the below code because we have migrated to Kubernetes
					 *
					if (browser.equalsIgnoreCase("chrome"))
					{
						System.out.println("Hub: "+sHubUrl);
						System.out.println("Port: "+sHubPort);
						driver = new RemoteWebDriver(new URL("http://"+sHubUrl+":"+sHubPort+"/wd/hub"), cap);

					}

					else
					{
					 */
					/*
					 * hubService is the mvn command line argument
					 * mvn -f pom-parallel.xml clean install -DconfigEnv=$URL -DforkCount=$SERVERS -DhubService=$SERVICE_URL -DreportUrl=$REPORT_URL -Dcucumber.options="--tags @$SCOPE"
					 */

					sHubUrl=System.getProperty("hubService");
					sHubPort="4444";
					System.out.println("Hub Url: "+sHubUrl+":"+sHubPort+"/wd/hub");
					driver = new RemoteWebDriver(new URL(sHubUrl+":"+sHubPort+"/wd/hub"), cap);
					//					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{ // this is for local run

				try
				{

					if(browser.equalsIgnoreCase("chrome")){
						try
						{
							ChromeOptions chromeOptions = new ChromeOptions();
							chromeOptions.addArguments("--disable-notifications");
							System.setProperty("webdriver.chrome.silentOutput", "true");
							driver= new ChromeDriver(chromeOptions);
							System.out.println("Launched chrome");
							browserVal=true;
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}

					}
					else if(browser.equalsIgnoreCase("chromeLocal")){
						System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
						ChromeOptions chromeOptions = new ChromeOptions();
						chromeOptions.addArguments("headless");
						chromeOptions.addArguments("window-size=1200x600");
						chromeOptions.addArguments("--disable-notifications");
						System.setProperty("webdriver.chrome.silentOutput", "true");
						driver= new ChromeDriver(chromeOptions);
					}
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
			localEnvUrl=env;
			driver.get(env);

		} catch (WebDriverException e) {
			System.out.println("The browser: "+browser+" could not be launched");
			browserVal=false;
			e.printStackTrace();
		}
	}


	public void startApp(String browser, String env) {
		//				System.out.println(browser);
		startApp(browser, false, env);
	}

	
	
	public WebDriver getDriver() {
		return driver;
	}




	public void typeText(WebElement element, String msg) {
		try
		{
			waitForMe(element);
			element.clear();
			Thread.sleep(2000);
			for (int i = 0; i < msg.length(); i++) {
				element.sendKeys(Character.toString(msg.charAt(i)));
			}
		}
		catch(Exception ex)
		{
			System.out.println("Failed to type the Webelement "+element);
			ex.printStackTrace();
		}

	}

	public void forceClick(WebElement ele) {
		Actions actions = new Actions(driver);
		actions.moveToElement(ele).click().build().perform();
	}

	public WebElement waitForMe(WebElement element)
	{
		try
		{
			wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element))); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return element;

	}


	public void waitForMe(WebElement first, WebElement second)
	{
		try
		{
			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(first));
			wait.until(ExpectedConditions.visibilityOf(first));

			wait.until(ExpectedConditions.elementToBeClickable(second));
			wait.until(ExpectedConditions.visibilityOf(second));
		}
		catch(Exception ex)
		{

		}

	}

	public void clickAction(WebElement element) throws InterruptedException {

		try {
			Thread.sleep(3000);
			pollingWait(element,30);
			element.click();
		}
		catch(ElementNotInteractableException ex)
		{
			Thread.sleep(3000);
			clickJavaScriptElement(element);
		}
		catch (Exception ex) {
			System.out.println("Failed to click the Webelement "+element);
			ex.printStackTrace();
		}
	}

	public void clickActionFailure(WebElement element) throws InterruptedException {

		try {
			Thread.sleep(3000);
			element.click();

		}
		catch(ElementNotInteractableException ex)
		{
			Thread.sleep(3000);
			element.click();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}




	public void clickAction(WebElement element, String msg) throws InterruptedException {

		try {
			//			waitForMe(element);
			pollingWait(element, 30);
			element.click();

		}
		catch(ElementNotInteractableException ex)
		{
			Thread.sleep(2000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView()", element);
			js.executeScript("arguments[0].click()", element);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public boolean verifyClickAction(WebElement element) throws InterruptedException {
		boolean res= true;
		try {
			//			waitForMe(element);
			pollingWait(element, 30);
			element.click();

		}
		catch(ElementNotInteractableException ex)
		{
			res = false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}











	public String getText(WebElement element) {

		String eleText="";
		try
		{
			//			waitForMe(element);
			pollingWait(element, 20);
			//			Thread.sleep(2000);
			eleText=element.getText();
		}
		catch(Exception ex)
		{
		}
		return eleText;
	}


	
	public boolean compareTwoWebElements(WebElement e1, WebElement e2)
	{
		boolean retVal = false;
		try
		{
			String firstElement=e1.getText().toLowerCase();
			String secondElement=e2.getText().toLowerCase();
			if(firstElement.contentEquals(secondElement))
			{
				retVal=true;
			}
		}
		catch(Exception ex)
		{
			retVal=false;
		}
		return retVal;
	}



	public String switchWindows() {
		String currentWindowHandle="";
		try {
			// Store the current window handle
			currentWindowHandle = driver.getWindowHandle();
			// Switch to new window opened

			Set<String> allWindows= driver.getWindowHandles();
			for (String winHandle : allWindows) {
				if (!winHandle.equals(currentWindowHandle) ) {
					driver.switchTo().window(winHandle);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return currentWindowHandle;
	}



	public String switchWindowspackages(String pwindow) {
		// Store the current window handle
		String winHandleBefore="";
		try {
			winHandleBefore = driver.getWindowHandle();

			// Perform the click operation that opens new window

			// Switch to new window opened
			for (String winHandle : driver.getWindowHandles()) {
				if (!winHandle.contentEquals(winHandleBefore) && !winHandle.contentEquals(pwindow)) {
					driver.switchTo().window(winHandle);

				}
			}
			return winHandleBefore;

		}

		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return winHandleBefore;
	}

	public void closeSwittchParentWindow(String window) {

		try
		{
			// Perform the actions on new window
			// Close the new window, if that window no more required
			driver.close();
			// Switch back to original browser (first window)
			driver.switchTo().window(window);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();

		}
	}

	// Creating a method getScreenshot and passing two parameters
	// driver and screenshotName
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {

		String destination="";
		File finalDestination =null;
		try {
			// below line is just to append the date format with the screenshot name to
			// avoid duplicate names
			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			// after execution, you could see a folder "FailedTestsScreenshots" under src
			// folder

			screenshotName=screenshotName.replaceAll("\\s+", "");
			//			destination = System.getProperty("user.dir")+"/reports/images/" + screenshotName + dateName+ ".jpg";
			destination = "./reports/images/" + screenshotName + dateName
					+ ".jpg";
			finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
			//			 Reports.reportStep("Captured the Failed TC screenshot successfully", "PASS");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();		}
		// Returns the captured file path
		return destination;

	}

	public String getCurrentUrl() {

		String currentURL="";
		try
		{
			//("Capturing current URL");
			currentURL= driver.getCurrentUrl();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return currentURL;

	}



	public boolean elementExist(WebElement ele) {
		try {
			pollingWait(ele,25);
			ele.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void elementNotExist(WebElement ele) {

		try {
			if (ele.getTagName() != null) {

			} else
				Assert.fail();
		} catch (Exception e) {
			System.out.println("User Not able to remove the hotel as expexted");
		}



	}


	public Boolean elementDisplayed(WebElement ele) {
		try {
			//			waitUntil(ele, 2);
			pollingWait(ele,30);
			// waitForMe(ele);
			if(ele.isDisplayed()) {
				return true;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}



	public void navigateToUrl(String url)
	{
		try
		{
			driver.get(url);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			}
	}



	public static void hoverWebelement(WebElement HovertoWebElement)
	{
		try
		{
			Actions builder = new Actions(driver);
			builder.moveToElement(HovertoWebElement).perform();
			Thread.sleep(1000);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
	public  void scrolltoElement(WebElement ScrolltoThisElement) {
		try {

			waitForMe(ScrolltoThisElement,50);
			Actions act = new Actions(driver);

			act.moveToElement(ScrolltoThisElement).build().perform();


		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void scroll()
	{
		try
		{
			//			WebDriver driver = new FirefoxDriver();
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("scroll(0, 300);");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}


	public void scrollToReserveSlot()
	{
		try
		{
			//			WebDriver driver = new FirefoxDriver();
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("scroll(0, 1000);");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}


	public void scrollToEnd()
	{
		try
		{
			//			WebDriver driver = new FirefoxDriver();
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("scroll(0, 4100);");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}


	public void scrollToView(WebElement ScrolltoThisView)
	{
		try
		{
			//			WebDriver driver = new FirefoxDriver();
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].scrollIntoView();",ScrolltoThisView );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}


	public boolean verifyExistence(WebElement webElement, String message, boolean showMessage) throws Exception {

		boolean returnValue = false;

		try {

			highlightElement = null;

			if (webElement != null && webElement.isDisplayed()) {

				returnValue = true;

				if (webElement.getText() != null &&

						webElement.getText().length() > 0 &&

						!webElement.getText().equals("*")) {// use message if value = *



				} else {


				}

			} else {

				if (showMessage) {

					// fail("Verification Failed: Element Not Found On UI :" + message);

					// ScreenShot_Helper.capturefullPageScreenShot(driver, "i-" + message);

				} else {

					// reportLog("Verification Failed: Element Not Found On UI :"+message);

				}
			}
		} finally {

		}
		return returnValue;
	}




	public String getAttribute(WebElement ele, String attribute) {
		String bReturn = "";
		try {
			waitForMe(ele);
			bReturn=  ele.getAttribute(attribute);

		} catch (WebDriverException e) {
		}
		return bReturn;
	}

	// method to select values in drop down based on index
	public void selectDropDownUsingIndex(WebElement element, int indexer) {
		try
		{
			waitForMe(element);
			Select sel = new Select(element);
			sel.selectByIndex(indexer);
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();
		}

	}

	// method to select values in drop down based on Visible Text
	public void selectDropDownUsingVisibleText(WebElement element, String visibleText) {
		try
		{
			waitForMe(element);
			Select sel = new Select(element);
			sel.selectByVisibleText(visibleText);
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();
			}

	}


	// method to select values in drop down based on Value
	public void selectDropDownUsingValue(WebElement element, String value) {
		try
		{
			waitForMe(element);
			Select sel = new Select(element);
			sel.selectByValue(value);
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();
		}

	}


	public String getTitle() {
		String bReturn = "";
		try {
			bReturn =  driver.getTitle();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
		return bReturn;
	}


	public boolean verifyTitle(String title) throws InterruptedException {
		boolean bReturn =false;
		try {
			Thread.sleep(3000);
			if(getTitle().equals(title)) {
				bReturn= true;
			}
		} catch (WebDriverException ex) {
			ex.printStackTrace();
			}
		return bReturn;
	}

	public boolean verifyExactText(WebElement ele, String expectedText) {
		boolean bReturn =false;
		try {
			//			waitForMe(ele);
			pollingWait(ele, 20);
			if(getText(ele).equals(expectedText)) {
				bReturn=true;
			}
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();
		}
		return bReturn;

	}


	public boolean verifyPartialText(WebElement ele, String expectedText) {
		boolean bReturn =false;

		try {
			waitForMe(ele);
			if(getText(ele).contains(expectedText)) {
				bReturn=true;
			}else {
			}
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		
		}
		return bReturn;
	}

	public boolean verifyExactAttribute(WebElement ele, String attribute, String value) {
		boolean bReturn =false;

		try {
			waitForMe(ele);
			if(getAttribute(ele, attribute).equals(value)) {
				bReturn=true;
		} 
		}catch (WebDriverException ex) {
			ex.printStackTrace();
		
		}

		return bReturn;

	}

	public boolean verifyPartialAttribute(WebElement ele, String attribute, String value) {
		boolean bReturn =false;

		try {
			waitForMe(ele);
			if(getAttribute(ele, attribute).contains(value)) {
				bReturn=true;
			}else {
			}
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
		return bReturn;
	}


	public boolean verifySelected(WebElement ele) {
		boolean bReturn =false;

		try {
			waitForMe(ele);
			if(ele.isSelected()) {
				bReturn=true;
			} else {
			}
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
		return bReturn;
	}

	public boolean verifyEnabled(WebElement ele) {
		boolean retVal=false;
		try {
			//			waitForMe(ele);
			if(ele.isEnabled()) {
				retVal=true;

			} else {
				retVal=false;
			}
		} catch (WebDriverException e) {
			retVal=false;


		}
		return retVal;
	}

	public void switchWindowByIndex(int index)
	{
		try
		{
			Set<String> allWindowHandles=driver.getWindowHandles();
			List<String> allWindows=new ArrayList<String>();
			allWindows.addAll(allWindowHandles);
			driver.switchTo().window(allWindows.get(index));
		}
		catch (NoSuchWindowException e) {
			e.printStackTrace();
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
	}

	public void switchToFrameByWebElement(WebElement ele)
	{
		try
		{
			Thread.sleep(4000);
			driver.switchTo().frame(ele);
		}
		catch (NoSuchFrameException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToFrameByName(String frameName_ID)
	{
		try
		{
			driver.switchTo().frame(frameName_ID);
		}
		catch (NoSuchFrameException e) {
			e.printStackTrace();
			} catch (WebDriverException e) {
				e.printStackTrace();
		}
	}

	public void switchToFrameByIndex(int index)
	{
		try
		{
			driver.switchTo().frame(index);
		}
	
		catch (WebDriverException e) {
			e.printStackTrace();
		}
	}

	public void accept() {
		String text = "";
		try {
			Alert alert = driver.switchTo().alert();
			text = alert.getText();
			alert.accept();
		} catch (NoAlertPresentException ex) {
		} catch (WebDriverException e) {
			e.printStackTrace();
			}
	}

	public void dismissAlert() {
		String text = "";
		try {
			Alert alert = driver.switchTo().alert();
			text = alert.getText();
			alert.dismiss();
		} catch (NoAlertPresentException e) {
		} catch (WebDriverException e) {
			e.printStackTrace();
			}

	}

	public String getAlertText() {
		String text = "";
		try {
			Alert alert = driver.switchTo().alert();
			text = alert.getText();
		} catch (NoAlertPresentException e) {
		} catch (WebDriverException e) {
			e.printStackTrace();
			}
		return text;
	}

	public void closeBrowser() {
		try {
			driver.close();
		} catch (Exception e) {

			e.printStackTrace();
			}
	}

	public void closeAllBrowsers() {
		try {
			driver.quit();
			driver.manage().deleteAllCookies();
		} catch (Exception e) {
			e.printStackTrace();
			}
	}

	public void doubleClick(WebDriver driver, WebElement ele)
	{
		try
		{
			waitForMe(ele);
			Actions doubleClk = new Actions(driver);
			doubleClk.moveToElement(ele).doubleClick().build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			}
	}

	public void rightClick(WebDriver driver, WebElement ele)
	{
		try
		{
			waitForMe(ele);
			Actions rightClk = new Actions(driver);
			rightClk.moveToElement(ele).contextClick().build().perform();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void dragDropUsingClickAndHold(WebDriver driver, WebElement dragElement,WebElement dropElement)
	{
		try
		{
			waitForMe(dragElement, dropElement);
			Actions dragDrop = new Actions(driver);
			dragDrop.clickAndHold(dragElement).moveToElement(dropElement).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			}
	}

	public void moveToSubMenu(WebDriver driver, WebElement menu, WebElement subMenu)
	{
		try
		{
			waitForMe(menu, subMenu);
			Actions navigateSubMenu = new Actions(driver);
			navigateSubMenu.moveToElement(menu).moveToElement(subMenu).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
	}

	public void dragDrop(WebDriver driver, WebElement dragElement,WebElement dropElement)
	{
		try
		{
			waitForMe(dragElement, dropElement);
			Actions dragDrop = new Actions(driver);
			dragDrop.dragAndDrop(dragElement,dropElement).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void moveTo(WebDriver driver, WebElement moveElement)
	{
		try
		{
			waitForMe(moveElement);
			Actions moveTo = new Actions(driver);
			moveTo.moveToElement(moveElement).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public void moveToUrl(WebDriver driver)
	{
		try
		{
			Thread.sleep(2000);
			Dimension initial_size = driver.manage().window().getSize();
			int ab =initial_size.getHeight();
			System.out.println("Window size"+initial_size);
			System.out.println(initial_size.getHeight());
			//			moveToUrl.keyDown(Keys.CONTROL)
			//			   .click(ele)
			//			   .moveByOffset( 0, 20 )
			//			   .keyUp(Keys.CONTROL).build().perform();
			Actions moveToUrl = new Actions(driver);
			moveToUrl.moveByOffset(0, -750).click().build().perform();
			// Reports.reportStep("Dragged the Mouse pointer to the Url","PASS");
		}
		catch(Exception ex)
		{
			// Reports.reportStep("Unable to drag the Mouse pointer to the Url","FAIL");
		}
	}




	public boolean uploadFileUsingSendKeys(WebElement ele, String path)
	{
		boolean retVal=false;
		try
		{
			waitForMe(ele);



		}
		catch(Exception ex)
		{
			retVal=false;
		}
		return retVal;
	}

	

	public void validate_String_Contains(String actual, String expected) {
		//		Reporter.addStepLog("Validating " + actual + " contains " + expected + "");
		System.out.println("Validating " + actual + " contains " + expected + "");
		Assert.assertTrue(actual.contains(expected));
	}


	public void clickJavaScriptElement(WebElement element) throws InterruptedException {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click()", element);
			//            System.out.println("Successfully clicked the Javascript Webelement");
		} catch (ElementNotInteractableException ex) {
			Thread.sleep(3000);
			System.out.println("Unable to click for the first time and trying one more time");
			element.click();
		} catch (Exception ex) {
			System.out.println("Failed to click the Javascript Webelement");
		}
	}

	public void clickDisbledJavaScriptElement(WebElement element) throws InterruptedException {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].removeAttribute(\"disabled\");", element);

			//            System.out.println("Successfully clicked the Javascript Webelement");
		} catch (ElementNotInteractableException ex) {
			Thread.sleep(3000);
			System.out.println("Unable to click for the first time and trying one more time");
			element.click();
		} catch (Exception ex) {
			System.out.println("Failed to click the Javascript Webelement");
		}
	}

	public void pollingWait(WebElement element,int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		wait.until(ExpectedConditions.visibilityOf(element));


	}

	public boolean pollingWaitForCostingFlow(String element,int timeout) {
		boolean retVal=false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout))
					.pollingEvery(Duration.ofSeconds(5));
			wait.until(ExpectedConditions.urlContains(element));
			retVal=true;
		} catch (NoSuchElementException e) {
			//("Unable to locate Element : " + element);
		}
		return retVal;
	}	
	public boolean verifyStringNotEquals(String actual, String expected) {
		boolean retVal=false;

		try {
			//(actual + " string should not contain " + expected);
			Assert.assertFalse(actual.contains(expected));
			retVal=true;
		}
		catch(Exception ex)
		{
			//("Problem in comparing the strings "+actual+"--->"+expected);

		}
		return retVal;
	}

	public void actionsClick(WebDriver driver, WebElement ele)
	{
		try
		{
			waitForMe(ele);
			Actions singleClick = new Actions(driver);
			singleClick.moveToElement(ele).click().build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			}
	}


	public void scrollUp()
	{
		try
		{
			//			WebDriver driver = new FirefoxDriver();
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.scrollBy(0,-750)"); 
			//("Successfully scrolled up the page");
		}
		catch(Exception ex)
		{

			ex.printStackTrace();
			}
	}


	public void scrollByValue(int value)
	{
		try
		{
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.scrollBy(0,"+value+")"); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void scrollDown()
	{
		try
		{
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,3000)");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void actionTypeText( WebElement ele,String text) {
		try {
			waitForMe(ele);
			Actions typeText = new Actions(driver);
			typeText.sendKeys(ele,text).build().perform();


		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public boolean scrollTo(int val) {
		boolean flag = false;
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("scroll(0, " + val + ");");
			flag = true;
		} catch (Exception ex) {
			System.out.println("Failed to scroll");
		}
		return flag;
	}

	public WebElement waitForMe(WebElement element,int timeOut)
	{
		try
		{
			wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
			wait.until(ExpectedConditions.visibilityOf(element));
		}
		catch(Exception ex)
		{
			System.out.println("Unable to locate the Webelement: "+element);

		}
		return element;
	}

	public void pageRefresh() {
		driver.navigate().refresh();
	}

	public Boolean verifyIsDisabled(WebElement element) {
		Boolean disabled = false;

		try
		{
			disabled = (Boolean) driver.executeScript("return arguments[0].hasAttribute(\"disabled\");", element);
			System.out.println(element+" is disabled with value "+disabled);
			return disabled;
		}
		catch(Exception ex)
		{
			System.out.println("Unable to locate the disabled attribute in the webelement: "+element);

		}
		return disabled;


	}

	public void openInNewWindow(WebElement element)
	{
		try
		{
			System.out.println(element);
			String keyString =   Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
			element.sendKeys(keyString);


		}
		catch(Exception ex)
		{
			System.out.println("Error opening in New tab" +ex.getMessage());
		}
	}

	public void openInNewTab(WebElement element)
	{
		try
		{
			System.out.println(element);
			String packageLink=element.getAttribute("href");
			System.out.println(packageLink);
			((JavascriptExecutor)driver).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			driver.get(packageLink);


		}
		catch(Exception ex)
		{
			System.out.println("Error opening in New tab" +ex.getMessage());
		}
	}
	
	public void fullPageScreenshot(String screenshotName)
	{
		try
		{
			String path= System.getProperty("user.dir"+"/screenshots/");
			//Shutterbug.shootPage(driver, Capture.FULL, true).save(path+screenshotName+".png");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}




}
