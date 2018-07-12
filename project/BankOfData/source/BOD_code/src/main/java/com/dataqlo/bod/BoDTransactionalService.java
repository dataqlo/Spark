package com.dataqlo.bod;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

import javax.ws.rs.GET;

import javax.ws.rs.Path;

import javax.ws.rs.Produces;

import javax.ws.rs.core.Response;

import javax.xml.parsers.ParserConfigurationException;


import org.xml.sax.SAXException;
 
@Path("/Transaction")
public class BoDTransactionalService {
 
	static ArrayList<String> recordsCSV = new ArrayList<String>();
	static ArrayList<String> recordsXML = new ArrayList<String>();
	static ArrayList<String> recordsJSON = new ArrayList<String>();
	private static  boolean readOnce = true;
	private static int recordsCounter,recordsCounterCSV,recordsCounterXML,recordsCounterJSON = 1;
	
	@GET
	@Path("/data")
	@Produces({"application/xml", "application/json", "application/text"})
	public Response getMsg() throws IOException, ParserConfigurationException, SAXException {
		if(readOnce){
			ReadCSV();
			ReadXMLFromEachLine();
			ReadJsonFromEachLine();
			readOnce=false;
		}
		String output="";
		if( recordsCounter%3 == 1) {
			 output = recordsXML.get(recordsCounterXML);
			recordsCounterXML++;
		}else if ( recordsCounter%3 == 2) {
			output = recordsCSV.get(recordsCounterCSV);
			recordsCounterCSV++;
		}else {
			output = recordsJSON.get(recordsCounterJSON);
			recordsCounterJSON++;
		}
		
		
		recordsCounter++;
		return Response.status(200).entity(output.toString()).build();
 
	}
	
	private void  ReadCSV() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File sourceFileCSV = new File(classLoader.getResource("AmEx_CC.csv").getFile().toString());
		System.out.print(sourceFileCSV);
		BufferedReader sourceFileBR = new BufferedReader(new FileReader(sourceFileCSV));
		String line;
		while ((line = sourceFileBR.readLine()) != null) {
			recordsCSV.add(line);
		}
		sourceFileBR.close();
		 
	}
 
	
	private void ReadXMLFromEachLine() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fXmlFile = new File(classLoader.getResource("Mastercard_CC.xml").getFile().toString());
		BufferedReader sourceFileBR = new BufferedReader(new FileReader(fXmlFile));
		String line;
		while ((line = sourceFileBR.readLine()) != null) {
			recordsXML.add(line);
		}
		sourceFileBR.close();
		
	}

	private void ReadJsonFromEachLine() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fXmlFile = new File(classLoader.getResource("Visa_CC.json").getFile().toString());	
		BufferedReader sourceFileBR = new BufferedReader(new FileReader(fXmlFile));
		String line;
		while ((line = sourceFileBR.readLine()) != null) {
			recordsJSON.add(line);
		}
		sourceFileBR.close();
	}
}