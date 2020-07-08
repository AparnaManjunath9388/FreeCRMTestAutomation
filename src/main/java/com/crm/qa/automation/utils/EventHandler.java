package com.crm.qa.automation.utils;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.crm.qa.automation.base.TestBase;
import com.relevantcodes.extentreports.LogStatus;


public class EventHandler extends TestBase implements WebDriverEventListener {
	
	
	@Override
	public void afterAlertAccept(WebDriver arg0) {
		Reporting.WriteIntoLogFile("Alert acception completed. ", LogStatus.INFO);
		
	}

	@Override
	public void afterAlertDismiss(WebDriver arg0) {
		Reporting.WriteIntoLogFile("Alert dismission completed. ", LogStatus.INFO);
		
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		Reporting.WriteIntoLogFile("Intended to set value: " + arg2[0] + ", value that has been set: " + arg0.getAttribute("value"), LogStatus.INFO);
		
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Clicked on element " + arg0.toString(), LogStatus.INFO);
		
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		Reporting.WriteIntoLogFile("Found the element using " + arg0.toString(), LogStatus.INFO);
		
	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> arg0, X arg1) {
		// TODO Auto-generated method stub
		Reporting.WriteIntoLogFile("Screenshot taken", LogStatus.INFO);
		
	}

	@Override
	public void afterGetText(WebElement arg0, WebDriver arg1, String arg2) {
		Reporting.WriteIntoLogFile("Fetched value '" + arg2 + "' from element " + arg0.toString(), LogStatus.INFO);
		
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {
		Reporting.WriteIntoLogFile("Navigated back to url " + arg0.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {
		Reporting.WriteIntoLogFile("Navigated forward to url " + arg0.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver arg0) {
		Reporting.WriteIntoLogFile("After Navigate Refresh url " + arg0.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Intended to navigate to url: " + arg0 + ", navigated to: " + arg1.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Completed the script '" + arg0 + "'", LogStatus.INFO);
		
	}

	@Override
	public void afterSwitchToWindow(String arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Intended to switch to window '" + arg0 + "', navigated to window '" + arg1.getWindowHandle().toString(), LogStatus.INFO);
		
	}

	@Override
	public void beforeAlertAccept(WebDriver arg0) {
		Reporting.WriteIntoLogFile("Trying to accept alert. ", LogStatus.INFO);
		
	}

	@Override
	public void beforeAlertDismiss(WebDriver arg0) {
		Reporting.WriteIntoLogFile("Trying to dismiss alert. ", LogStatus.INFO);
		
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		Reporting.WriteIntoLogFile("Before value: " + arg0.getAttribute("value") + ", expected to be set to: " + arg2[0], LogStatus.INFO);
		
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Trying to click on element " + arg0.toString(), LogStatus.INFO);
		
	}

	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		Reporting.WriteIntoLogFile("Trying to find the element using " + arg0.toString(), LogStatus.INFO);
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> arg0) {
		Reporting.WriteIntoLogFile("Trying to take screenshot. ", LogStatus.INFO);
		
	}

	@Override
	public void beforeGetText(WebElement arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Trying to get text value from element " + arg0.toString(), LogStatus.INFO);
		
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		Reporting.WriteIntoLogFile("url before navigating back: " + arg0.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		Reporting.WriteIntoLogFile("url before navigating forward: " + arg0.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver arg0) {
		Reporting.WriteIntoLogFile("url before navigate refresh: " + arg0.getCurrentUrl(), LogStatus.INFO);
		
	}

	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Trying to navigate to url: " + arg0, LogStatus.INFO);
		
	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Trying to start the script " + arg0, LogStatus.INFO);
		
	}

	@Override
	public void beforeSwitchToWindow(String arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Trying to switch to window " + arg0, LogStatus.INFO);
		
	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		Reporting.WriteIntoLogFile("Exception occured: " + arg0.getMessage(), LogStatus.INFO);
		
		try {
			CommonMethods commonMethods = new CommonMethods();
			Reporting.WriteIntoLogFile("Exception occured: " + arg0.toString() + ". Screenshot path: " + commonMethods.takeScreenshot(), LogStatus.INFO);
		} catch(IOException e) {
			Reporting.WriteIntoLogFile("Exception while taking screenshot: " + e.getMessage(), LogStatus.INFO);
		} catch (Exception e) {
			Reporting.WriteIntoLogFile("Exception while taking screenshot: " + e.getMessage(), LogStatus.ERROR);
		}
	}

}
