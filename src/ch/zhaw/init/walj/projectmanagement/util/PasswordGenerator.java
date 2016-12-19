package ch.zhaw.init.walj.projectmanagement.util;

import java.security.SecureRandom;

/**
 * Class to generate random passwords
 * @author Janine Walther, ZHAW
 *
 */
public class PasswordGenerator {
	
	private static PasswordGenerator instance;

	// possible characters and length of password
	private String characters = "QWERTZUIOPASDFGHJKLYXCVBNMmnbvcxylkjhgfdsapoiuztrewq1234567890";
	private int pwLength = 8;
	
	/**
	 * generates a new, random password
	 * @return a random password
	 */
	public String getNewPassword(){
		// generate random password
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(pwLength);
		for (int i = 0; i < pwLength; i++) {
			password.append(characters.charAt(random.nextInt(characters.length())));
		}
	
		return password.toString();
	}
	
	/**
	 * @return an instance of Password Generator
	 */
	public static synchronized PasswordGenerator getInstance(){
	  
	    if(instance == null){
	       instance = new PasswordGenerator(); 
	    } 
	    return instance;
	}
	
}
