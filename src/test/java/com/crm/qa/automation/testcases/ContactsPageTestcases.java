package com.crm.qa.automation.testcases;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.crm.qa.automation.base.TestBase;
import com.crm.qa.automation.pages.ContactPage;
import com.crm.qa.automation.pages.HomePage;
import com.crm.qa.automation.pages.LoginPage;
import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.DataProviderMethods;
import com.relevantcodes.extentreports.LogStatus;

public class ContactsPageTestcases extends TestBase {
	
	public ContactPage ContactsPage;
	
	public ContactsPageTestcases() {
		super();
	}
	
	@BeforeClass
	@Parameters({"ExcelDataRow"})
	public void initialSetUp(@Optional("")String ExcelDataRow) throws Exception {
		
		Expected = "Prerequisite: Login to Application. ";
		status = LogStatus.INFO;
		try {
			
			//initialize test object
			test = report.startTest("Contacts Page Testcases");
			test.setDescription("Covers all the scenarios of " + prop.getProperty("AppName") + " Contacts Page");
			
			//set start time and description
			CommonMethods CommonMethods = new CommonMethods();
			test.setStartedTime(CommonMethods.getTime(System.currentTimeMillis()));	
			
			//initialize Contacts page object
			ContactsPage = new ContactPage();
			
			//read all the data required by this test
			DataProviderMethods DataProvider = new DataProviderMethods(prop.getProperty("dataproviderpath"));
			TestParams.putAll(DataProvider.getParams("ContactsPageTestcases", ExcelDataRow));
			
			//Login to application which is a Pre-requisite. Report the result
			LoginPage LoginPage = new LoginPage();
			LoginPage.login(TestParams.get("Username"), TestParams.get("Password"));
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
	public void addNewContact() {
		Expected = "New contact is added successfully";
		
		try {
			String ContactDetail = TestParams.get("NewContactDetails");
			
			if (ContactsPage.addNewContact(ContactDetail) > 0) {
				Actual = "New contact added successfully";
				status = LogStatus.PASS;
			} else {
				Actual = "Failed to add new Contact";
			}	
		} catch (Exception e) {
			Actual = e.toString() + e.getMessage();
			status = LogStatus.ERROR;
		}
	}
	
	@Test(priority=2)
	public void editContact() {
		Expected = "Search for contact, right click and edit: ";
		
		try {
			String ContactName = TestParams.get("ContactNameToEdit");
			String ContactDetailsToEdit = TestParams.get("ContactDetailsToEdit");
			
			Expected += ContactName;
			
			if (ContactsPage.editContact(ContactsPage.searchContact(ContactName), ContactName, ContactDetailsToEdit)) {
				Actual = "Contact edited successfully";
				status = LogStatus.PASS;
			} else {
				Actual = "Failed to edit contact";
			}
				
		} catch (Exception e) {
			Actual = e.toString() + e.getMessage();
			status = LogStatus.ERROR;
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
