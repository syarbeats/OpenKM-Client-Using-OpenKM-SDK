package com.poc.openkm.document_api;

import java.io.FileInputStream;
import java.io.InputStream;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.OKMWebservicesFactory;
import com.openkm.sdk4j.bean.Document;
import com.openkm.sdk4j.bean.Folder;
import com.openkm.sdk4j.exception.DatabaseException;
import com.openkm.sdk4j.exception.PathNotFoundException;
import com.openkm.sdk4j.exception.RepositoryException;
import com.openkm.sdk4j.exception.UnknowException;
import com.openkm.sdk4j.exception.WebserviceException;

public class App {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		//new App().proceed();
		Options options = new Options();
    	
    	Option username = new Option("u", "username", true, "username");
        username.setRequired(true);
        options.addOption(username);

        Option password = new Option("p", "password", true, "password");
        password.setRequired(true);
        options.addOption(password);

        Option docPath = new Option("d", "docPath", true, "file path");
        docPath.setRequired(true);
        options.addOption(docPath);

        Option content = new Option("c", "content", true, "content file");
        content.setRequired(true);
        options.addOption(content);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

        String docFilePath = cmd.getOptionValue("docPath");
        String contentDocFile = cmd.getOptionValue("content");
        String usernameCmd = cmd.getOptionValue("username");
        String passwordCmd = cmd.getOptionValue("password");
		
        String host = "http://localhost:8080/OpenKM";
        
		new App().insertFileToDMS(host, usernameCmd, passwordCmd, docFilePath, contentDocFile);
	}

	public void proceed() {
		System.out.println("-------------Accessing OpenKM DMS Via Java SDK!--------------");
		String url = "http://localhost:8080/OpenKM";
        String user = "okmAdmin";
        String pass = "admin";
        OKMWebservices okm = OKMWebservicesFactory.newInstance(url, user, pass);
 
        try {
			for (Folder fld : okm.getFolderChildren("/okm:root")) {
			    System.out.println("Folder -> " + fld.getPath());
			}
		} catch (PathNotFoundException | RepositoryException | DatabaseException | UnknowException
				| WebserviceException e) {
			e.printStackTrace();
		}
	}
	
	public void insertFileToDMS(String host, String username, String password, String path, String content)
	{
		/*String host = "http://localhost:8080/OpenKM";
        String username =  "okmAdmin";
        String password = "admin";*/
        OKMWebservices ws = OKMWebservicesFactory.newInstance(host, username, password);
        
        try {
        	System.out.println("-----------Insert File To DMS--------------");
            //InputStream is = new FileInputStream("E:\\TEST\\pic1.jpg");
        	InputStream is = new FileInputStream(content);
            Document doc = new Document();
            //doc.setPath("/okm:root/horreechoy.jpg");
            doc.setPath(path);
            ws.createDocument(doc, is);
            IOUtils.closeQuietly(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
