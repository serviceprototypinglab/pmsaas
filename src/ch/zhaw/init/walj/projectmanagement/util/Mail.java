package ch.zhaw.init.walj.projectmanagement.util;

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
	
	private static final String mailFrom = "noreply@projectmanagementsaas.ch";
	private static final String host = "smtp.zhaw.ch";
	private Properties properties = System.getProperties();
	private Session session;
	private MimeMessage message;
	private Employee user;

	/**
	 * constructor of the Mail class
	 * sets user, host, sender e-mail address and properties
	 * @param user
	 */
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
	
	/**
	 * Sends a welcome mail to the user.
	 * Contains name, kuerzel, mail and password 
	 * of the new registered user.		 
	 */		
	public void sendWelcomeMail(){
		try {
			// set subject of the mail
			message.setSubject("Welcome to Project Management SaaS");
			
			// TODO better content
			// set content of the mail
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
		
			// TODO better content
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
		
			// TODO better content
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