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

package ch.zhaw.init.walj.projectmanagement.util.format;

/**
 * Class to format numbers
 * @author Janine Walther, ZHAW
 *
 */
public class NumberFormatter {

	private static NumberFormatter instance;
	
	/**
	 * @return instance of the NumberFormatter class
	 */
	public static NumberFormatter getInstance(){
	  
	    if(instance == null){
	       instance = new NumberFormatter(); 
	    } 
	    return instance;
	}
	
	/**
	 * formats a number for displaying money amounts
	 * @param number a number which should be formatted
	 * @return the formatted number as a string
	 */
	public String formatDouble(double number){
		return String.format("%.2f", number);
	}
	
	/**
	 * formats hours to be displayed without floating point
	 * @param number a number which should be formatted
	 * @return the formatted number as a string
	 */
	public String formatHours(double number){
		return String.format("%.0f", number);
	}
}
