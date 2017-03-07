package ch.zhaw.init.walj.projectmanagement.util.password;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * encrypts a password
 * @author Janine Walther, ZHAW
 *
 */
public final class PasswordService {

	private static PasswordService instance;

	/**
	 * encrypts a password
	 * @param plaintext the password that should be encrypted
	 * @return the encrypted password
	 */
	public synchronized String encrypt(String plaintext){
	   
		// encrypt password
		MessageDigest md = null;
	    try{
	    	md = MessageDigest.getInstance("SHA");
	    } catch(NoSuchAlgorithmException e){
	    
	    }
	    
	    try{
	    	md.update(plaintext.getBytes("UTF-8"));
	    } catch(UnsupportedEncodingException e){
	      
	    }
	
	    byte raw[] = md.digest();
	    String hash = (new BASE64Encoder()).encode(raw);
	    
	    return hash;
	}
  
	/**
	 * @return an instance of PasswordService
	 */
	public static synchronized PasswordService getInstance(){
  
	    if(instance == null){
	       instance = new PasswordService(); 
	    } 
	    return instance;
	}
}