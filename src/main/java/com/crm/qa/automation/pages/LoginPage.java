package com.crm.qa.automation.pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.crm.qa.automation.base.TestBase;
import com.crm.qa.automation.utils.CommonMethods;
import com.crm.qa.automation.utils.LocationParser;

public class LoginPage extends TestBase {
	
	private CommonMethods CommonMethods;
	private LocationParser LocationParser;
	
	public LoginPage() {
		CommonMethods = new CommonMethods();
		LocationParser = new LocationParser();
	}
	//private static WebDriver driver = TestBase.driver;
	
	/**************************************************************************
	 * getPageTitle()
	 * verifyLoginPageLogo()
	 * login(String sArgUserName, String sArgPwd)
	 */

	
	/************************************************************
	 * getPageTitle
	 * @return String page title
	 */
	public String getPageTitle() {
		String title = driver.getTitle();
		return title;
	}

	
	/***********************************************************
	 * verifyLoginPageLogo
	 * @return boolean value indicating if the Logo image is appearing at the login screen
	 * @throws Exception 
	 */
	public boolean verifyLoginPageLogo() throws Exception {
		
		boolean flag = false;
		
		try {
			flag = driver.findElement(LocationParser.getBy(ObjectRep.getProperty("login.logoImage"))).isDisplayed();
		}
		
		catch (Exception e) {
			throw e;
		}
		return flag;
	}
	
	
	/***************************************************************
	 * login
	 * @param sArgUserName username
	 * @param sArgPwd password
	 * @return Homepage instance which is the landing page from Login page
	 * @throws Exception 
	 * @throws InterruptedException 
	 */
	public HomePage login(String sArgUserName, String sArgPwd) throws Exception {
		
		try {
			driver.findElement(LocationParser.getBy(ObjectRep.getProperty("login.username"))).sendKeys(sArgUserName);
			driver.findElement(LocationParser.getBy(ObjectRep.getProperty("login.password"))).sendKeys(sArgPwd);
		
			wait.until(ExpectedConditions.invisibilityOf(driver.findElement(LocationParser.getBy(ObjectRep.getProperty("login.preloader")))));
			driver.findElement(LocationParser.getBy(ObjectRep.getProperty("login.loginBtn"))).click();
		}
		catch (Exception e){
			throw e;
		}
		return new HomePage();
	}
	
	/****************************************************************
	 * getLinksNotPresent
	 * @param String List Of LinksToVerify
	 * @return String list of links not displayed in the screen
	 */
	public String getLinksNotPresent(String ListOfLinks) {
		String[] arrayOfLinks = ListOfLinks.split(",");
		return CommonMethods.find_Which_Of_These_Links_Not_Displayed(arrayOfLinks);
	}
	
	/****************************************************************
	 * verifyPageCenterText
	 * @param String value to assert
	 * @return boolean value indicating whether the text is matching or not
	 * @throws Exception 
	 */
	public boolean verifyPageCenterText(String TextToMatch) throws Exception {
		try {
			String elementText = CommonMethods.getElementText("login.defaultPageCenterText");	
			return CommonMethods.CompareText(elementText, TextToMatch, "equals");
		} catch(Exception e) {
			throw e;
		}
	}
	
	public List<String> scrollDownAndVerifyLinksPresence(String ListOfLinks) throws Exception {
		try {
			
			CommonMethods.scrollVerticalBarBy(0, "up");
			String[] arrayOfLinks = ListOfLinks.split(",");
			LinkedList<String> LinksList = new LinkedList<String>();
			for (String link : arrayOfLinks ) {
				LinksList.add(link);
			}
			
			LinkedList<String> ListOfLinksNotDisplayed = LinksList;
			long ScrollLen = CommonMethods.getVerticalScrollBarLength();
			Long ScrollPixelUnitsToMove = (long) ScrollLen/5;
			Long CurrentScrollPos = (long) 0;
			while (CurrentScrollPos < ScrollLen) {
				CommonMethods.getListOfLinksNotDisplayed(ListOfLinksNotDisplayed);
				if (!ListOfLinksNotDisplayed.isEmpty()) {
					CommonMethods.scrollVerticalBarBy(ScrollPixelUnitsToMove, "down");
					CurrentScrollPos += ScrollPixelUnitsToMove;
				} else {
					break;
				}
			}

			return ListOfLinksNotDisplayed;
		} catch(Exception e) {
			throw e;
		}
	}
	
}
