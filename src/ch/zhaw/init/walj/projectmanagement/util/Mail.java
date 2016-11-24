package ch.zhaw.init.walj.projectmanagement.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;

public class Mail {
	
	String mailFrom = "noreply@projectmanagementsaas.ch";
	String host = "smtp.zhaw.ch";
	Properties properties = System.getProperties();
	Session session;
	MimeMessage message;
	Employee user;

	public Mail (Employee user){
		this.user = user;
		properties.setProperty("mail.smtp.host", host);
		session = Session.getDefaultInstance(properties);
		message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mailFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getMail()));
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	
	public void sendWelcomeMail(){
		
		try {
			message.setSubject("Welcome to Project Management SaaS");
			
			// TODO better content
			String content = "<p>Hello " + user.getFirstName() + "</p>\n"
						   + "<p>Someone created a new account with the following information:</p>"
						   + "<p>Name: " + user.getName() + "</p>"			
						   + "<p>Kuerzel: " + user.getKuerzel() + "</p>"			
						   + "<p>Mail: " + user.getMail() + "</p>"
						   + "<p>Password: " + user.getPassword() + "</p>"
						   + "<br>"
						   + "<p>Click <a href=\"" + "" + "\">here</a> to sign in </p>" // TODO correct path to tool
						   + "<p></p>"
						   + "<p style=\"font-style:italic\">This e-mail was generated automatically, please do not respond to it.</p>";			
			
			message.setContent(content, "text/html");
			
			Transport.send(message);
			System.out.println("Sent welcome message to " + user.getMail());
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendResetPasswordMail() throws MessagingException{
		message.setSubject("Password reset");
		
		// TODO better content
		String content = "<p>Hello " + user.getFirstName() + "</p>\n"
					   + "<p>Here is your new Password:</p>"
					   + "<p>Password: " + user.getPassword() + "</p>"
					   + "<br>"
					   + "<p style=\"font-style:italic\">This e-mail was generated automatically, please do not respond to it.</p>";			
		
		message.setContent(content, "text/html");
		
		Transport.send(message);
		System.out.println("Sent password reset message to " + user.getMail());
	}
	
	public void sendInitialSetupMail() throws MessagingException{
		message.setSubject("Password reset");
		
		// TODO better content
		String content = "<p>Hello " + user.getFirstName() + "</p>\n"
					   + "<p>Thanks for initialising the Project Management SaaS.</p>"
					   + "<br>"
					   + "<p style=\"font-style:italic\">This e-mail was generated automatically, please do not respond to it.</p>";			
		
		message.setContent(content, "text/html");
		
		Transport.send(message);
		System.out.println("Sent password reset message to " + user.getMail());	
	}

}