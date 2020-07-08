package com.crm.qa.automation.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.Constants;
import com.crm.qa.automation.utils.EventHandler;
import com.crm.qa.automation.utils.Reporting;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class TestBase {
	
	public static WebDriver driver;
	public Properties prop;
	public Properties ObjectRep;
	public static WebDriverWait wait;
	public String TestRunID;
	public static ExtentReports report;
	public static ExtentTest test;
	public HashMap<String, String> TestParams = new HashMap<String, String>();
	public String Expected;
	public String Actual;
	public LogStatus status;
	public String MethodName;
	public Logger log = Logger.getLogger(TestBase.class);
	public static Reporting Reporting;
	
	
	public TestBase() {

		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream("C:\\Users\\aparn\\eclipse-workspace\\FreeCRMTest\\src\\main\\java\\com\\crm\\qa\\automation\\config\\config.properties");
			prop.load(ip);
			
			ObjectRep = new Properties();
			FileInputStream objrep = new FileInputStream("C:\\Users\\aparn\\eclipse-workspace\\FreeCRMTest\\src\\main\\java\\com\\crm\\qa\\automation\\objectrepository\\objectproperties");
			ObjectRep.load(objrep);
			
		}
		
		
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initialization() {
		
		String browserName = prop.getProperty("browser");
		Boolean bDriverInitialized = false;
		
		if (browserName.toLowerCase().equals("chrome")){
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\aparn\\eclipse-workspace\\FreeCRMTest\\src\\main\\java\\browserdrivers\\chromedriver.exe");
			driver = new ChromeDriver();
			bDriverInitialized = true;
		}
		else if (browserName.toLowerCase().equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", "C:\\Users\\aparn\\eclipse-workspace\\FreeCRMTest\\src\\main\\java\\browserdrivers\\geckodriver.exe");
			driver = new FirefoxDriver();
			bDriverInitialized = true;
		}
		else {
			System.out.println("Please specify the property 'browser' in config.properties file. ");
		}
		
		if (bDriverInitialized) {
			
			EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
			EventHandler eventHandler = new EventHandler();
			eventDriver.register(eventHandler);
			driver = eventDriver;
			
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().pageLoadTimeout(Constants.PAGELOAD_TIMEOUT, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(Constants.IMPLICITWAIT_TIMEOUT, TimeUnit.SECONDS);		//not recommended since it makes the selenium wait for each and every element to wait before throwing error
			
			//explicit wait declaration. you can use this for particular elements for which you want to wait
			wait = new WebDriverWait(driver, Constants.EXPLICITWAIT_TIMEOUT);
			
			driver.navigate().to(prop.getProperty("url"));
		}		
	}
	
	public Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	@AfterSuite
	public void AfterSuite() {
		report.close();
		Reporting.WriteIntoLogFile("Closed HTML Report", LogStatus.INFO);
	}
	
	@BeforeSuite
	public void BeforeSuite() {
		TestRunID = prop.getProperty("AppName") + "_" + LocalDateTime.now().toString().replace(":", "_").replace(".", "_");
		String path = System.getProperty("user.dir") + File.separator + "ExecutionReports" + File.separator  + "ExecutionReport_" + TestRunID + ".html";
		report = new ExtentReports(path);
		Reporting = new Reporting();
		Reporting.WriteIntoLogFile("Created HTML Report in the path: " + path, LogStatus.INFO);
	}
	
	@BeforeMethod
	public void beforeEachMethod(Method method) {
		
		try {
			Expected = "";
			Actual = "";
			status = LogStatus.FAIL;
			
			MethodName = method.getName();		
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}

		Reporting.WriteIntoLogFile("Starting method: " + MethodName, LogStatus.INFO);
		
	}
	
	@AfterMethod
	public void AfterEachMethod() throws Exception {
		
		try {
			if (status == LogStatus.FAIL || status == LogStatus.ERROR) {
				CommonMethods CommonMethods = new CommonMethods();
				Actual += ". Screenshot: " + test.addScreenCapture(CommonMethods.takeScreenshot());
			
			}
			Reporting.WriteIntoReport(Expected, Actual, status);	
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}

		Reporting.WriteIntoLogFile("Completed Method: "+ MethodName, LogStatus.INFO);
	}
	
	@BeforeTest
	public void BeforeTest() {
		
		//initialize and launch Browser
		initialization();
	}
	
	@AfterTest
	public void tearDown() {
		//driver.close();
		try {
			driver.manage().deleteAllCookies();
			driver.quit();
			
			CommonMethods CommonMethods = new CommonMethods();
			test.setEndedTime(CommonMethods.getTime(System.currentTimeMillis()));
			report.endTest(test);
			report.flush();
			TestParams.clear();
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}
	}
	
	@Test(enabled=false)
	public void verifyBrokenLinks() {
		
		Expected = "No Broken links should be found on the page";
		try {
			CommonMethods CommonMethods = new CommonMethods();
			ArrayList<String> ListOfBrokenLinkText = CommonMethods.getAllBrokenLinks();
			
			if (ListOfBrokenLinkText.isEmpty()) {
				Actual = "No broken links were found in the page";
				status = LogStatus.PASS;
			} else {
				Actual = "List of broken links on the page - " + ListOfBrokenLinkText.toString();
			}
		} catch(Exception e) {
			Actual = e.toString();
			status = LogStatus.ERROR;
		}
	}
}
