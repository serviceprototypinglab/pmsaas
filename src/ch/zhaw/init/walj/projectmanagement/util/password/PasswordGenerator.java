/*
 	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 	All Rights Reserved.

   Licensed under the Apache License, Version 2.0 (the "License"); you may
   not use this file except in compliance with the License. You may obtain
   a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
   License for the specific language governing permissions and limitations
   under the License.
 */

package ch.zhaw.init.walj.projectmanagement.util.password;

import java.security.SecureRandom;

/**
 * Class to generate random passwords
 * @author Janine Walther, ZHAW
 *
 */
public class PasswordGenerator {
	
	private static PasswordGenerator instance;

    /**
	 * generates a new, random password
	 * @return a random password
	 */
	public String getNewPassword(){
		// generate random password
		SecureRandom random = new SecureRandom();
        int pwLength = 8;
        StringBuilder password = new StringBuilder(pwLength);
		for (int i = 0; i < pwLength; i++) {
            String characters = "QWERTZUIOPASDFGHJKLYXCVBNMmnbvcxylkjhgfdsapoiuztrewq1234567890";
            password.append(characters.charAt(random.nextInt(characters.length())));
		}
	
		return password.toString();
	}
	
	/**
	 * @return an instance of Password Generator
	 */
	public static PasswordGenerator getInstance(){
	  
	    if(instance == null){
	       instance = new PasswordGenerator(); 
	    } 
	    return instance;
	}
	
}
