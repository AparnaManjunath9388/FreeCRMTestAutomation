package com.crm.qa.automation.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.crm.qa.automation.base.TestBase;

public class DataProviderMethods extends TestBase {
	
	private Fillo fillo;
	private Connection connection;
	
	public DataProviderMethods(String DataFilePath) {
	   try {
		   		fillo = new Fillo();
		   		connection = fillo.getConnection(DataFilePath);

			} catch (Exception e){
				System.out.println("Exception while creating fillo connection with Data sheet : " + e.toString());
			}

	}
	
	public HashMap<String, String> getParams(String SheetName, String RowId) {
		
		HashMap<String, String> DataMap = new HashMap<String, String>();
		String Query = "";
		
		if (RowId.contains(",")) {
			String[] rows = RowId.split(",");
			String RowIds = "";
			for (String row : rows) {
				RowIds+="'" + row + "',";
			}
			RowIds = RowIds.substring(1, RowIds.length() - 1);
			Query = "Select * from " + SheetName + " where TestID in (" + RowId + ")";
			
		} else {
			Query = "Select * from " + SheetName + " where TestID='" + RowId + "'";
		}

		try {
			Recordset recordset = connection.executeQuery(Query);
			
			String colData, key, value;
			ArrayList<String> arr;
			while (recordset.next()) {
				
				arr = recordset.getFieldNames();
				
				for(int i=1;i<arr.size();i++) {
					colData = recordset.getField(arr.get(i));
					if (colData.isEmpty()) {
						break;
					} else {
						String[] arrData = colData.split(";");
						key = arrData[0];
						value = "";
						if (arrData.length == 1) {
							value = "";
						} else {
							value = arrData[1];
						}
						DataMap.put(key, value);
					}
					
				}
			}
			
			recordset.close();
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while reading data from Excel: " + e.toString());
		}
		
		connection.close();
		return DataMap;
	}
	
	
}
