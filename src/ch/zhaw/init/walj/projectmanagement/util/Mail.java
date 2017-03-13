/**
 *	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 *	All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package ch.zhaw.init.walj.projectmanagement.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;

/**
 * can send different kind of e-mails 
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class Mail {
	
	private static String mailFrom;
	private static String host;
	private Properties properties = System.getProperties();
	private Session session;
	private MimeMessage message;
	private Employee user;
	private String helper[];

	/**
	 * constructor of the Mail class
	 * sets user, host, sender e-mail address and properties
	 * @param user
	 */
	public Mail (Employee user, String path){
		
		// reads the address host from the .mailconfig file 
		try {
			FileReader fr = new FileReader(path + ".mailconfig");
		    BufferedReader br = new BufferedReader(fr);
			
		    mailFrom = br.readLine();
		    helper = mailFrom.split("=");
		    mailFrom = helper[1];
		    
		    host = br.readLine();
		    helper = host.split("=");
		    host = helper[1];

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
		this.user = user;
		properties.setProperty("mail.smtp.host", host);
		session = Session.getDefaultInstance(properties);
		message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mailFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.user.getMail()));
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a welcome mail to the user.
	 * Contains name, kuerzel, mail and password 
	 * of the new registered user.	
	 * 	
	 * @param url the request URL
	 */
	public void sendWelcomeMail(StringBuffer url){
		
		String path = url.toString();
		path = path.split("/Projektverwaltung")[0];
		
		try {
			// set subject of the mail
			message.setSubject("Welcome to Project Management SaaS");
			
			// set content of the mail
			String content = "<p>Hello " + user.getFirstName() + "</p>"
					   	   + "<br>"
						   + "<p>Someone created a new account with the following information:</p>"
						   + "<p>Name: " + user.getName() + "</p>"			
						   + "<p>Kuerzel: " + user.getKuerzel() + "</p>"			
						   + "<p>Mail: " + user.getMail() + "</p>"
						   + "<p>Password: " + user.getPassword() + "</p>"
						   + "<p style=\"font-style:italic\">Please change your password at your first login.</p>"
						   + "<br>"
						   + "<p>Click <a href=\"" + path + "/Projektverwaltung/login\">here</a> to sign in </p>"
						   + "<p></p>"
						   + "<p style=\"font-style:italic\">This e-mail was generated automatically, please do not respond to it.</p>";			
			message.setContent(content, "text/html");
			
			// send mail
			Transport.send(message);		
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @throws MessagingException
	 */
	public void sendNewPasswordMail(){
		try {
			// set subject of the mail
			message.setSubject("Password reset");
		
			// set content of the mail
			String content = "<p>Hello " + user.getFirstName() + "</p>\n"
						   + "<p>Here is your new Password:</p>"
						   + "<p>Password: " + user.getPassword() + "</p>"
						   + "<br>"
						   + "<p style=\"font-style:italic\">This e-mail was generated automatically, please do not respond to it.</p>";			
			message.setContent(content, "text/html");
			
			// send mail
			Transport.send(message);

		} catch (MessagingException e) {

		}
	}
	
	/**
	 * send initial mail to admin
	 * @throws MessagingException
	 */
	public void sendInitialSetupMail(){
		try {
			// set subject of the mail
			message.setSubject("Project Management SaaS setup");
		
			// set content of the mail
			String content = "<p>Hello " + user.getFirstName() + "</p>\n"
						   + "<p>Thanks for initialising the Project Management SaaS.</p>"
						   + "<br>"
						   + "<p style=\"font-style:italic\">This e-mail was generated automatically, please do not respond to it.</p>";			
			message.setContent(content, "text/html");

			// send mail
			Transport.send(message);
			
		} catch (MessagingException e) {
			
		}
	}

}