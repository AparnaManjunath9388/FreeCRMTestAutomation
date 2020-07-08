package com.crm.qa.automation.utils;
import org.openqa.selenium.By;

public class LocationParser {
	
	private String Key;
	private String Value;
	
	//private static WebDriver driver = TestBase.driver;
	
	public By getBy(String ObjDetails) throws Exception {
		
		String prop = ObjDetails;
		
		Key = prop.split(":")[0];
		Value = prop.split(":")[1];
		
		switch(Key.toLowerCase()) {
		 
		case "name":
			return By.name(Value);
			
		case "id":
			return By.id(Value);
			
		case "linktext":
			return By.linkText(Value);
			
		case "partiallinktext":
			return By.partialLinkText(Value);
	
		case "tagname":
			return By.tagName(Value);
			
		case "cssselector":
			return By.cssSelector(Value);
		
		case "classname":
			return By.cssSelector(Value);
			
		case "xpath":
			return By.xpath(Value);
		default:
			throw new Exception("Exception from LocationParser.getBy: Unknown locator type '" + Key + "'");

		}
	}
}
