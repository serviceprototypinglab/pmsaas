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

package ch.zhaw.init.walj.projectmanagement.util.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Projectmanagement tool, Class to format dates
 * 
 * @author Janine Walther, ZHAW
 * 
 */
public class DateFormatter {

	private static DateFormatter instance;
		
	/**
	 * @return instance of a DateFormatter
	 */
	public static synchronized DateFormatter getInstance(){
	  
	    if(instance == null){
	       instance = new DateFormatter(); 
	    } 
	    return instance;
	}
	
	/**
	 * 
	 * @param unformattedDate
	 * 				date 'YYYY-MM-DD' as a string
	 * @return
	 * 				date 'DD.MM.YYYY' as a string
	 */
	public String formatDate(String unformattedDate){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
		
		try {
			Date date = format.parse(unformattedDate);			
			String formattedDate = format2.format(date);
			return formattedDate;
		} catch (ParseException e) {
			return unformattedDate;
		}
	}
	
	/**
	 * 
	 * @param unformattedDate
	 * 				date object
	 * @return
	 * 				date 'DD.MM.YYYY' as a string
	 */
	public String formatDate(Date unformattedDate){
		
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		String formattedDate = format.format(unformattedDate);
		return formattedDate;
	}
	
	/**
	 * 
	 * @param unformattedDate
	 * 				date 'DD.MM.YYYY' as a string
	 * @return
	 * 				date 'YYYY-MM-DD' as a string
	 */
	public String formatDateForDB(String unformattedDate){
		
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Date date = format.parse(unformattedDate);			
			String formattedDate = format2.format(date);
						
			return formattedDate;
		} catch (ParseException e) {
			
			return unformattedDate;
		}
	}
	
	/**
	 * 
	 * @param unformattedDate
	 * 				date object
	 * @return
	 * 				date 'YYYY-MM-DD' as a string
	 */
	public String formatDateForDB(Date unformattedDate){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format.format(unformattedDate);
		return formattedDate;
	}
	
	
	/**
	 * 
	 * returns the number of months between two dates
	 * 
	 * @param start
	 * 			the start-date
	 * @param end
	 * 			the end-date
	 * @return
	 * 			number of months between start-date and end-date
	 */
	public int getMonthsBetween(String start, String end){
		int monthsBetween = 0;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMMM yyyy");
		
		Calendar cStart = Calendar.getInstance();
		Calendar cEnd = Calendar.getInstance();
		
		try {
			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			
			// make sure there is only the date without time
			cStart.setTime(format2.parse(format2.format(startDate)));
			cEnd.setTime(format2.parse(format2.format(endDate)));
	
			if (cStart.after(cEnd)){
				return 0;
			}
			while (!(cStart.getTime().equals(cEnd.getTime()))) {
				cStart.add(Calendar.MONTH, 1);
				monthsBetween++;				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return monthsBetween + 1;
	}
	
	
	/**
	 * calculates the number of days between two dates
	 * 
	 * @param startDate
	 * 			the start-date
	 * @param end
	 * 			the end-date
	 * @return
	 * 			number of days between start-date and end-date
	 */
	public int getDaysBetween(Date startDate, String end){
		int days = 0;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		try {		
			Calendar cStart = Calendar.getInstance();
			Calendar cEnd = Calendar.getInstance();
			
			Date endDate = format.parse(end);
			// make sure only date without time is saved in startDate
			startDate = format.parse(format.format(startDate)); 
			cStart.setTime(startDate);
			cEnd.setTime(endDate);

			if (cStart.after(cEnd)){
				return 0;
			}
			while (!(cStart.getTime().equals(cEnd.getTime()))) {
				cStart.add(Calendar.DAY_OF_MONTH, 1);
				days++;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return days;
	}
	
	/**
	 * Writes a defined amount of months beginning with the start date in an array. 
	 * Every month as '01.09.2016' and 'September 2016'
	 * 
	 * @param start date of the first month
	 * @param nbrOfMonths amount of needed months
	 * @return String array with all months
	 */
	public String[][] getMonths(Date start, int nbrOfMonths){
		String dates[][] = new String[2][nbrOfMonths];
		
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat formatMonthYear = new SimpleDateFormat("MMMM yyyy");
			
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		
		for (int y = 0; y < nbrOfMonths; y++){
			dates[0][y] = format.format(c.getTime());
			dates[1][y] = formatMonthYear.format(c.getTime());
			c.add(Calendar.MONTH, 1);
		}		
		
		return dates;
	}
	
	/**
	 * get names of months (like 'September 2016')
	 * 
	 * @param start date of the first month
	 * @param nbrOfMonths amount of needed months
	 * @return String array with all months
	 */
	public String[] getMonthStrings(Date start, int nbrOfMonths) {
		String months[][] = getMonths(start, nbrOfMonths);
		String month[] = new String[nbrOfMonths];
		
		for (int i = 0; i < nbrOfMonths; i++){
			month[i] = months[1][i];
		}
		
		return month;
	}

	/**
	 * formats a string 
	 * @param dateString
	 * @param formatString
	 * @return
	 */
	public Date stringToDate(String dateString, String formatString){
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * Checks if firstDate is before secondDate. 
	 * @param firstDate
	 * @param secondDate
	 * @param formatString
	 * @return true if firstDate is before secondDate.
	 */
	public boolean checkDate(String firstDate, String secondDate, String formatString){
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		
		try {
			Date date1 = format.parse(firstDate);
			Date date2 = format.parse(secondDate);
			
			return date1.before(date2);
		} catch (ParseException e) {
			return false;
		}
	}
	
}
