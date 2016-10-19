package ch.zhaw.init.walj.projectmanagement.util;

import java.security.SecureRandom;

public class PasswordGenerator {
	
	private static PasswordGenerator instance;

	private String characters = "QWERTZUIOPASDFGHJKLYXCVBNMmnbvcxylkjhgfdsapoiuztrewq1234567890";
	private int pwLength = 8;
	
	public String getNewPassword(){
		// generate random password
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(pwLength);
		for (int i = 0; i < pwLength; i++) {
			password.append(characters.charAt(random.nextInt(characters.length())));
		}
	
		return password.toString();
	}
	
	public static synchronized PasswordGenerator getInstance(){
	  
	    if(instance == null){
	       instance = new PasswordGenerator(); 
	    } 
	    return instance;
	}
	
}
