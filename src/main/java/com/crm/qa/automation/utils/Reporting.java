package com.crm.qa.automation.utils;

import com.crm.qa.automation.base.TestBase;
import com.relevantcodes.extentreports.LogStatus;

public class Reporting extends TestBase{
	
	public void WriteIntoReport(String Expected, String Actual, LogStatus status) {
		
		//write into html report
		test.log(status, Expected, Actual);	
		
		WriteIntoLogFile(Actual, status);

	}
	
	public void WriteIntoLogFile(String Message, LogStatus status) {
		
		//write into log file
		switch (status) {
		case FAIL:
		case ERROR:
			log.error(Message);
			break;
			
		case FATAL:
			log.fatal(Message);
			break;
			
		default:
			log.info(Message);
		}
	}

}
