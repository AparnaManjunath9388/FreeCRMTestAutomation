package com.crm.qa.automation.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.crm.qa.automation.base.TestBase;
import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.LocationParser;

public class ContactPage extends TestBase {
	
	private CommonMethods CommonMethods;
	private LocationParser LocationParser;
	
	public ContactPage() {
		CommonMethods = new CommonMethods();
		LocationParser = new LocationParser();
	}
	
	public int searchContact(String Name) throws Exception {
		
		int row = -1;
		
		try {
			String FirstLetter = Character.toString(Name.toCharArray()[0]);
			boolean FirstAlphabetSelect = selectFirstAlphabet(FirstLetter);
			
			List<WebElement> PageNumbers = driver.findElements(LocationParser.getBy(ObjectRep.getProperty("contacts.pageIndex")));
			
			if (FirstAlphabetSelect) {
				row = findContactRow(Name, PageNumbers);
			}
			return row;
		} catch(Exception e) {
			throw e;
		}
	}
		
	public boolean selectFirstAlphabet(String firstLetter) throws Exception {

		try {
			boolean AlphabetClicked = false;
			List<WebElement> allAlbhabets = driver.findElements(LocationParser.getBy(ObjectRep.getProperty("contacts.startLetterElements")));
			
			for (WebElement e : allAlbhabets) {
				if (CommonMethods.CompareText(firstLetter, e.getText(), "equalsignorecase")) {
					e.click();
					
					WebElement SelectedAlphabet = driver.findElement(LocationParser.getBy(ObjectRep.getProperty("contacts.selectedFirstLetter")));
					String verification = "-" + firstLetter.toUpperCase() +"- Contacts";
					if (SelectedAlphabet.getText().contains(verification))
						AlphabetClicked = true;
					break;
				}
			}
			return AlphabetClicked;
		} catch(Exception e) {
			throw new Exception("Exception from method ContactPage.selectFirstAllphabet: " + e.getMessage());
		}
	}
	
	
	public int addNewContact(String ContactDetails) throws Exception {
		try {
			String[] ContactDetailsArr = ContactDetails.split(",");
			HashMap<String, String> ContactDetailMap = new HashMap<String, String>();
			
			for (String info : ContactDetailsArr) {
				String[] arrInfo = info.split("/");
				ContactDetailMap.put(arrInfo[0], arrInfo[1]);
			}
			
			CommonMethods.hoverMouseAndClick("home.contactsLink", "contacts.newContactLink");
			
			wait.until(ExpectedConditions.presenceOfElementLocated(LocationParser.getBy(ObjectRep.getProperty("contacts.newContactTitle"))));
			String FName = "", LName = "";

			boolean notSet = false;
			
			for (Entry<String, String> e: ContactDetailMap.entrySet()) {
				String key = e.getKey();
				String value = e.getValue();
				String objName = "contacts." + key;
				
				//CommonMethods.FigureOutInputTypeAndSetValue(objName, ContactDetailMap.get(key));
				
				if (key.toLowerCase().contains("title") || key.toLowerCase().contains("suffix")) {
					if (!CommonMethods.selectFromDropdown(objName, value))
						notSet = true;
				} else if (key.toLowerCase().contains("receiveemail")) {
					if (!CommonMethods.clickOnWebElement(objName))
						notSet = true;
				} else if(key.toLowerCase().contains("imagefile")) {
					/*if (!CommonMethods.uploadFile(objName, value))
						notSet = true; */
				} else if(key.toLowerCase().contains("birthday")) {
					if (!CommonMethods.setDate(objName, "contacts.calendarBox", value))
						notSet = true;
				} else if(key.toLowerCase().contains("companylookup")) {
					if (!CommonMethods.clickOnWebElement(objName) || !CommonMethods.searchInLookUpWindow("Company lookup", value))
						notSet = true;
					//CommonMethods.switchToFrame("mainpanel", "name");
				} else {
					if (key.toLowerCase().contains("firstname"))
						FName = value;
					else if (key.toLowerCase().contains("surname"))
						LName = value;
					
					if (!CommonMethods.setText(objName, value))
						notSet = true;
				}
			}
			
			if(!notSet) {
				CommonMethods.clickOnWebElement("contacts.newContactSave");
				CommonMethods.clickOnWebElement("home.contactsLink");
				return searchContact(FName + " " + LName);
			} else {
				return -1;
			}	
		} catch(Exception e) {
			throw new Exception("Exception from method ContactPage.addNewContact: " + e.getMessage());
		}	
	}
	
	@SuppressWarnings("finally")
	public boolean editContact(int ContactRowToEdit, String ContactNameToEdit, String ContactDetails) throws Exception {
		
		boolean result = false;
		try {
			String[] ContactDetailsArr = ContactDetails.split(",");
			HashMap<String, String> ContactDetailMap = new HashMap<String, String>();
			
			for (String info : ContactDetailsArr) {
				String[] arrInfo = info.split("/");
				ContactDetailMap.put(arrInfo[0], arrInfo[1]);
			}
			
			boolean notSet = false;
			
			List<WebElement> PageNumbers = driver.findElements(LocationParser.getBy(ObjectRep.getProperty("contacts.pageIndex")));
			String editBtnProp = ObjectRep.getProperty("contacts.editContact");
			editBtnProp = editBtnProp.replace("rownum", String.valueOf(ContactRowToEdit));
			
			//WebElement contact = findContactRow(ContactNameToEdit, PageNumbers);
			
			WebElement editBtn = driver.findElement(By.xpath(editBtnProp.split(":")[1]));
			CommonMethods.scrollElementIntoView(editBtn);
			wait.until(ExpectedConditions.elementToBeClickable(editBtn));
				
			editBtn.click();
			
			wait.until(ExpectedConditions.presenceOfElementLocated(LocationParser.getBy(ObjectRep.getProperty("contacts.newContactTitle"))));
			
			for (Entry<String, String> e: ContactDetailMap.entrySet()) {
				String key = e.getKey();
				String value = e.getValue();
				String objName = "contacts." + key;
				
				//CommonMethods.FigureOutInputTypeAndSetValue(objName, ContactDetailMap.get(key));
				
				if (key.toLowerCase().contains("title") || key.toLowerCase().contains("suffix")) {
					if (!CommonMethods.selectFromDropdown(objName, value))
						notSet = true;
				} else if (key.toLowerCase().contains("receiveemail")) {
					if (!CommonMethods.clickOnWebElement(objName))
						notSet = true;
				} else if(key.toLowerCase().contains("imagefile")) {
					/*if (!CommonMethods.uploadFile(objName, value))
						notSet = true; */
				} else if(key.toLowerCase().contains("birthday")) {
					if (!CommonMethods.setDate(objName, "contacts.calendarBox", value))
						notSet = true;
				} else if(key.toLowerCase().contains("companylookup")) {
					if (!CommonMethods.clickOnWebElement(objName) || !CommonMethods.searchInLookUpWindow("Company lookup", value))
						notSet = true;
					//CommonMethods.switchToFrame("mainpanel", "name");
				} else {
					if (!CommonMethods.setText(objName, value))
						notSet = true;
				}
			}
			
			if (!notSet) {
				CommonMethods.clickOnWebElement("contacts.newContactSave");
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			throw new Exception("Exception from method ContactPage.rightClickAndEditContact: " + e.getMessage());
		}
		
		finally {
			return result;
		}
	}
	
	
	public int findContactRow(String ContactName, List<WebElement> PageNumbers) throws Exception {
		
		int PageCounter = -1;
		int row = -1;
		try {
			while (row == -1) {
				 List<WebElement> rows = driver.findElements(LocationParser.getBy(ObjectRep.getProperty("contacts.allContactRows")));
				 for (int i=1;i<=rows.size();i++) {
					 String contactColXPath = ObjectRep.getProperty("contacts.contactColumnInTable").replace("rownum", Integer.toString(i));
					 List<WebElement> contacts = driver.findElements(LocationParser.getBy(contactColXPath));
					 if (contacts.size() > 0)
						 if (contacts.get(0).getText().equalsIgnoreCase(ContactName)) {
							 row = i;
							 break;
						 }
				 }
				 if ((row == -1) && PageNumbers.size() > 0) {
					PageCounter++;
					if (PageCounter < PageNumbers.size()) {
						PageNumbers.get(PageCounter).click();
					} else {
						break;
					}
				 } else {
					 break;
				 }
			}
			return row;
		} catch (Exception e) {
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			throw new Exception("Exception from method " + methodName + ": " + e.getMessage());
		}
	}
}
