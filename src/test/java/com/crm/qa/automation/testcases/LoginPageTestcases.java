package com.crm.qa.automation.testcases;

import com.crm.qa.automation.base.TestBase;
import com.crm.qa.automation.pages.HomePage;
import com.crm.qa.automation.pages.LoginPage;
import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.DataProviderMethods;
import com.crm.qa.automation.utils.Reporting;
import com.relevantcodes.extentreports.LogStatus;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.*;

public class LoginPageTestcases extends TestBase {

	private LoginPage LoginPage;
	
	public LoginPageTestcases() {
		super();
	}
	

	@Parameters({"ExcelDataRow"})
	@BeforeClass
	public void initialSetUp(@Optional("") String ExcelDataRow) {
		
		try {

			//initialize test object
			test = report.startTest("Login Page Testcases");
			test.setDescription("Covers all the scenarios of " + prop.getProperty("AppName") + " Login Page");
			
			//set start time and description
			CommonMethods CommonMethods = new CommonMethods();
			test.setStartedTime(CommonMethods.getTime(System.currentTimeMillis()));		
			
			//initialize LoginPage
			LoginPage = new LoginPage();
			
			//Read all the data required for this Test
			DataProviderMethods DataProvider = new DataProviderMethods(prop.getProperty("dataproviderpath"));
			TestParams = DataProvider.getParams("LoginPageTestcases", ExcelDataRow);
			
		} catch (Exception e) {
			Reporting.WriteIntoReport("Exception occured: ", e.toString(), LogStatus.ERROR);
		}
		
	}
	
	@Test(priority=1)	
	public void verifyPageTitle() {
		
		try {
			String PageTitle = LoginPage.getPageTitle();
			String ExpectedTitle = TestParams.get("LoginPageTitle");
			Expected = "Login Page title matches expected title: " + ExpectedTitle;
			
			if (PageTitle.equals(ExpectedTitle)) {
				Actual = "Page title as expected. ";
				status = LogStatus.PASS;
			} else {
				Actual = "Actual Page title: " + PageTitle;
			}			
		} catch (Exception e) {
			Actual = "Exception occured. Check logs for more details: " + e.toString();
			status = LogStatus.ERROR;
		}
	}
	
	@Test(priority=2)
	public void verifyLogoImage() throws Exception {
		Expected = "Logo Image is present in the Login page";
		
		boolean LogoVisible = false;
		try {
			LogoVisible = LoginPage.verifyLoginPageLogo();
			
			if (LogoVisible) {
				Actual = "Logo image visible in Login Page. ";
				status = LogStatus.PASS;
			} else {
				Actual = "Failed to locate Logo image in Login page. ";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Actual = "Exception occured. Check logs for more details: " + e.toString();
			status = LogStatus.ERROR;
		}
	}
	
	@Test(priority=3)
	public void verifyLinksPresence() {
		String ListOfLinks = TestParams.get("ListOfLinksToVerify");
		Expected = "Follwoing links should be present in the page: " + ListOfLinks;
		
		try {
			String ListOfLinksNotPresent = LoginPage.getLinksNotPresent(ListOfLinks);
			
			if (ListOfLinksNotPresent.isEmpty()) {
				Actual = "All of these links are displayed on the page: " + ListOfLinks;
				status = LogStatus.PASS;
			} else {
				Actual = "These links are not displayed on the page: " + ListOfLinksNotPresent;
				status = LogStatus.FAIL;
			}
		} catch (Exception e) {
			Actual = "Exception occured: " + e.toString();
			status = LogStatus.ERROR;			
		}
	}
	
	@Test(priority=4)
	public void verifyPageCenterText() {
		
		String PageCenterText = TestParams.get("PageCenterText");
		Expected = "Page Center text should match this: " + PageCenterText;
		
		try {
			if (LoginPage.verifyPageCenterText(PageCenterText)) {
				Actual = "Page center text is as expected";
				status = LogStatus.PASS;
			} else {
				Actual = "Page center text is not as expected";
			}
		} catch (Exception e) {
			Actual = "Exception occured: " + e.toString();
			status = LogStatus.ERROR;
		}
	}
	
	@Test(priority=5)
	public void verifyLinksDisplayed() {
		try {
			String LinksToVerify = TestParams.get("LinksExpectedToDisplay");
			Expected = "Verify links display by scrolling down till bottom of the page: " + LinksToVerify;
			
			List<String> LinksNotDisplayed = LoginPage.scrollDownAndVerifyLinksPresence(LinksToVerify);
			
			if (LinksNotDisplayed.isEmpty()) {
				Actual = "All the links are correctly displayed on the page";
				status = LogStatus.PASS;
			} else {
				Actual = "Links not displayed: " + LinksNotDisplayed.toString();
				status = LogStatus.FAIL;;
			}
		} catch (Exception e) {
			Actual = "Exception occured: " + e.toString();
			status = LogStatus.ERROR;
		}
			
	}
	
	@Test(priority=6)
	public void loginUnsuccessful() throws Exception {
		
		try {	
			String Username = TestParams.get("Negative_Username");
			String Password = TestParams.get("Negative_Password");
			String expectedNewPageTitle = TestParams.get("NewPageTitle");
			String LoginPageTitle = TestParams.get("LoginPageTitle");
			Expected = "Login to application with the username " + Username + " and verify that login is unsuccessful";
		
	
			LoginPage.login(Username, Password);
			String ActualTitle = LoginPage.getPageTitle();
			if ((!ActualTitle.equals(expectedNewPageTitle)) && ActualTitle.equals(LoginPageTitle)) {
				Actual = "Login unsuccessful as expected. ";
				status = LogStatus.PASS;
			} else {
				Actual = "Expected page title after unsuccessful login: " + LoginPageTitle + ", Actual title: " + ActualTitle;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Actual = e.toString();
			status = LogStatus.ERROR;
		}
	}
	
	
	@Test(priority=7)
	public void loginToApplication() throws Exception {
	
		try {	
			String Username = TestParams.get("Username");
			String Password = TestParams.get("Password");
			String expectedNewPageTitle = TestParams.get("NewPageTitle");
			Expected = "Login to application with the username " + Username;
		
	
			HomePage HomePage = LoginPage.login(Username, Password);
			String ActualTitle = HomePage.getPageTitle();
			if (ActualTitle.equals(expectedNewPageTitle)) {
				Actual = "Login successful. Page title as expected";
				status = LogStatus.PASS;
			} else {
				Actual = "Expected new page title: " + expectedNewPageTitle + ", Actual title: " + ActualTitle;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Actual = e.toString();
			status = LogStatus.ERROR;
		}
	}

}
