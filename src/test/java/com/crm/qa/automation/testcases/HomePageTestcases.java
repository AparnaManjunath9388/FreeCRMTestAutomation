package com.crm.qa.automation.testcases;

import org.testng.annotations.Test;

import java.util.ArrayList;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.crm.qa.automation.base.TestBase;
import com.crm.qa.automation.pages.LoginPage;
import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.DataProviderMethods;
import com.relevantcodes.extentreports.LogStatus;
import com.crm.qa.automation.pages.ContactPage;
import com.crm.qa.automation.pages.HomePage;

public class HomePageTestcases extends TestBase {
	
	public HomePage HomePage;
	
	public HomePageTestcases() {
		super();
	}
	
	@BeforeClass
	@Parameters({"ExcelDataRow"})
	public void initialSetUp(@Optional("")String ExcelDataRow) throws Exception {
		
		Expected = "Prerequisite: Login to Application. ";
		status = LogStatus.INFO;
		try {
			
			//initialize test object
			test = report.startTest("Home Page Testcases");
			test.setDescription("Covers all the scenarios of " + prop.getProperty("AppName") + " Home Page");
			
			//set start time and description
			CommonMethods CommonMethods = new CommonMethods();
			test.setStartedTime(CommonMethods.getTime(System.currentTimeMillis()));	
			
			//initialize Home page object
			HomePage = new HomePage();
			
			//read all the data required by this test
			DataProviderMethods DataProvider = new DataProviderMethods(prop.getProperty("dataproviderpath"));
			TestParams = DataProvider.getParams("HomePageTestcases", ExcelDataRow);
			
			//Login to application which is a Pre-requisite. Report the result
			LoginPage LoginPage = new LoginPage();
			HomePage = LoginPage.login(TestParams.get("Username"), TestParams.get("Password"));
			Actual = "Login to application completed. ";
			
			CommonMethods.switchToFrame("mainpanel", "name");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Actual = "Exception occured. Check logs for more details: " + e.toString();
			status = LogStatus.ERROR;
		}
		Reporting.WriteIntoReport(Expected, Actual, status);
		
	}
	
	@Test(priority=1)
	public void verifyPageTitle() throws Exception {
		
		try {
			String PageTitle = HomePage.getPageTitle();
			String ExpectedTitle = TestParams.get("HomePageTitle");
			Expected = "Verify Home Page title. Expected: " + ExpectedTitle;
			
			if (PageTitle.equals(ExpectedTitle)) {
				Actual = "Home Page title as expected. ";
				status = LogStatus.PASS;
			} else {
				Actual = "Actual title: " + PageTitle + ", which is not as expected. ";
			}
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}
	}
	
	@Test(priority=2)
	public void verifyLoggedInUser() throws Exception {	
		
		try {
			String LoggedInUser = TestParams.get("LoggedInUser");
			Expected = "Logged in user '" + LoggedInUser + "' should be displayed. ";
			
			if (HomePage.verifyLoggedInUser(LoggedInUser)) {
				Actual = "Expected logged in user is displayed on the page. ";
				status = LogStatus.PASS;
			} else {
				Actual = "Logged in user is not displayed on the page. ";
			}
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}
	}
	
	@Test(enabled=false)
	public void createContactquickly() {
		try {
			
			Expected = "Contact(s) should get created and should be found when searched in Contacts Page";
			String CompanyName = TestParams.get("QuickCreate_CompanyName");
			String FirstName = TestParams.get("QuickCreate_FirstName");
			String LastName = TestParams.get("QuickCreate_LastName");
			
			HomePage.quickContactCreate(CompanyName, FirstName, LastName);
			
			if (HomePage.searchInContacts(FirstName, LastName)) {
				Actual = "Contacts have been created and searched successfully";
				status = LogStatus.PASS;
			} else {
				Actual = "Created contact(s) not found in the Contacts Page";
			}
			
		} catch(Exception e) {
			Actual = e.toString();
			status = LogStatus.ERROR;
		}
		
	}
	
	
	@Test(priority=4)
	public void addBoxAndVerify() {
		
		Expected = "Add box to Home Page: ";
		try {
			String boxToCreate = TestParams.get("BoxToAdd");
			String Location = TestParams.get("Location");
			Expected += "'" + boxToCreate + "'";
			
			if (HomePage.addBoxAndVerify(boxToCreate, Location)) {
				Actual = "'" + boxToCreate + "' added successfully";
				status = LogStatus.PASS;
			} else {
				Actual = "Box isn't added";
			}
		} catch(Exception e) {
			Actual = e.getMessage();
			status = LogStatus.ERROR;
		}
	}
	
	
	@Test(priority=5)
	public void searchInHome() throws Exception {
		
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}
		
		try {
			String SearchText = TestParams.get("SearchText");
			String TargetLocation = TestParams.get("TargetLocation");
			
			Expected = "Search for '" + SearchText + "' in '" + TargetLocation + "'";
			status = LogStatus.INFO;
			HomePage.search(SearchText, TargetLocation);	
			Actual = "Completed search without any exceptions. ";
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}
	}
	

	@AfterClass
	public void afterClass() throws Exception {
		try {
			CommonMethods CommonMethods = new CommonMethods();
			CommonMethods.switchToFrame("", "");
		} catch(Exception e) {
			Reporting.WriteIntoLogFile("Exception occured: " + e.getMessage(), LogStatus.ERROR);
		}
	}

}
