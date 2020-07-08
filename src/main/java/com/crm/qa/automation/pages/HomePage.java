package com.crm.qa.automation.pages;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.crm.qa.automation.base.TestBase;
import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.LocationParser;

public class HomePage extends TestBase {

	private CommonMethods CommonMethods;
	private LocationParser LocationParser;
	/************************************************************
	 * getPageTitle
	 * @return String page title
	 * @throws Exception 
	 */
	
	public HomePage() {
		CommonMethods = new CommonMethods();
		LocationParser = new LocationParser();
	}
	
	public String getPageTitle() throws Exception {
		
		String title = driver.getTitle();
		return title;
	}
	
	
	/**************************************************************
	 * 
	 * @param ExpectedUserName
	 * @return boolean value indicating if the Username is being displayed or not
	 * @throws Exception 
	 */
	public boolean verifyLoggedInUser(String ExpectedUserName) throws Exception{
		
		boolean flag = false;
		String property = ObjectRep.getProperty("home.loggedInUser").replace("username", ExpectedUserName);
		
		try {
			flag = driver.findElement(LocationParser.getBy(property)).isDisplayed();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return flag;
		
	}
	
	/**********************************************************************************
	 * search
	 * @param SearchString
	 * @param WhereToSearch
	 * @throws Exception
	 */
	public void search(String SearchString, String WhereToSearch) throws Exception {
		
		CommonMethods.setText("home.searchBox", SearchString);
		CommonMethods.selectFromDropdown("home.selectTarget", WhereToSearch);
		CommonMethods.clickOnWebElement("home.searchBtn");
	}
	
	
	/*************************************************************************************
	 * quickContactCreate
	 * @param Company
	 * @param FName
	 * @param LName
	 * @throws Exception 
	 */
	//@SuppressWarnings("deprecation")
	public void quickContactCreate(String Company, String Fname, String LName) throws Exception {
		
		try {
			
			//clickOnLeftMenu("Quick Create");
			CommonMethods.clickOnWebElement("home.quickCreate");
			
			wait.until(ExpectedConditions.presenceOfElementLocated(LocationParser.getBy(ObjectRep.getProperty("home.quickCreatebox"))));
			//if (CommonMethods.elementExists("home.quickCreatebox")) {
			String[] companies = Company.split(",");
			String[] Fnames = Fname.split(",");
			String[] LNames = LName.split(",");
			
			if (companies.length == Fnames.length && Fnames.length == LNames.length) {
				for (int i=0;i<companies.length;i++) {
					CommonMethods.setText("home.quickCreateCompany", companies[i]);
					Thread.sleep(1000);
					CommonMethods.setText("home.quickCreateFirstName", Fnames[i]);
					Thread.sleep(1000);
					CommonMethods.setText("home.quickCreateLastName", LNames[i]);
					Thread.sleep(1000);
					if (i == 0)
						CommonMethods.clickOnWebElement("home.quickCreateBtn");
					else
						CommonMethods.clickOnWebElement("home.quickCreateSaveBtn");
					Thread.sleep(1000);
				}
			}
				
				CommonMethods.clickOnWebElement("home.quickCreateCloseBtn");
			//}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*************************************************************************************
	 * searchInContacts
	 * @param FirstNames
	 * @param LLsatNames
	 * @return boolean value indicating if the contacts were found
	 */
	public boolean searchInContacts(String FNames, String LNames) throws Exception {
	
		try {
			ContactPage ContactsPage = new ContactPage();
			boolean isPresent = true;
			
			wait.until(ExpectedConditions.invisibilityOfElementLocated(LocationParser.getBy(ObjectRep.getProperty("home.quickCreateOverlay"))));
			CommonMethods.clickOnWebElement("home.contactsLink");
			
			if (CommonMethods.elementExists("contacts.advancedSearchBtn")) {
				CommonMethods.switchToFrame("default", "");
				String[] FNameList = FNames.split(",");
				String[] LNameList = LNames.split(",");
				
				for (int i=0;i<FNameList.length;i++) {
					String Name = FNameList[i] + " " + LNameList[i];
					if (ContactsPage.searchContact(Name) > 0) {
						isPresent = false;
					}
				}
			}
			CommonMethods.clickOnWebElement("home.homeLink");
			return isPresent;
		} catch(Exception e) {
			throw e;
		}
	}
	
	/********************************************************************************************
	 * clickOnLeftMenu
	 * @param MenuItem
	 * @return boolean value indicating if the Menu was clicked on not
	 */
	public boolean clickOnLeftMenu(String MenuItem) throws Exception {
		
		boolean ElementFound = false;
		try {
			String MenuItemsProp = ObjectRep.getProperty("home.leftMenuItems");
			List<WebElement> AllElements = driver.findElements(LocationParser.getBy(MenuItemsProp));
			
			for (int i=1;i<=AllElements.size();i++) {
				String MenuXPath = MenuItemsProp + "[" + i + "]/a";
				WebElement e = driver.findElement(LocationParser.getBy("xpath:" + MenuXPath));
				if (CommonMethods.CompareText(e.getAttribute("value"), MenuItem, "contains")) {
					e.click();
					ElementFound = true;
					break;		
				}
			}
			return ElementFound;
		} catch(Exception e) {
			throw new Exception("Exception from CommonMethods.clickOnElementBasedOnText: " + e.getMessage());
		}
	}
	
	
	/**
	 * @throws Exception *********************************************************************************************
	 * 
	 */
	public boolean addBoxAndVerify(String BoxToAdd, String Location) throws Exception {
		
		try {
			boolean addSuccessful = false;
			wait.until(ExpectedConditions.elementToBeClickable(LocationParser.getBy(ObjectRep.getProperty("home.addBoxLink"))));
			if (CommonMethods.clickOnWebElement("home.addBoxLink")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(LocationParser.getBy(ObjectRep.getProperty("home.addBox"))));
				
				CommonMethods.selectFromDropdown("home.BoxToSelect", BoxToAdd);
				CommonMethods.selectFromDropdown("home.BoxLocation", Location);
				CommonMethods.clickOnWebElement("home.addBtn");
				
				wait.until(ExpectedConditions.elementToBeClickable(LocationParser.getBy(ObjectRep.getProperty("home.addBoxLink"))));
				//String boxToVerify = getBoxNameToVerify(BoxToAdd);
				String boxProperty = ObjectRep.getProperty(getBoxNameToVerify(BoxToAdd));
				String Pos = Location.split(" ")[0].trim();
				
				boxProperty = boxProperty.replace("position", "homeColumn" + Pos);
				By by = LocationParser.getBy(boxProperty);
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
				if (driver.findElement(by).isDisplayed())
					addSuccessful = true;
			}
			return addSuccessful;
		} catch(Exception e) {
			CommonMethods.switchToFrame("default", "");
			throw new Exception("Exception from method HomePage.addBoxAndVerify: " + e.getMessage());
		}
	}
	
	
	/**
	 * @throws Exception *****************************************************************************************
	 * 
	 */
	public String getBoxNameToVerify(String OptionSelected) throws Exception {
		
		switch(OptionSelected) {
		
		case "Quick Create Form":
			return "home.quickCreateBoxOnHome";
			
		case "This Week Overview":
			return "home.thisWeek";
			
		case "Call List (non scheduled)":
			return "home.callListBox";
			
		case "Email Campaigns":
			return "home.emailCampaignsBox";
			
		case "CRMPRO News":
			return "home.crmproNews";
			
		case "Tag List":
			return "home.tagListBox";
			
		case "Scheduled Calls":
			return "home.scheduledCalls";
		
		default:
			throw new Exception("Unable to figure out option.");
		}
	}
}
