package ch.zhaw.init.walj.projectmanagement.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {

	private static DateFormatter instance;
		
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
		String[] helper = unformattedDate.split("-");
		if (helper.length == 3) {
			String formattedDate = helper[2] + "." + helper[1] + "." + helper[0];
			return formattedDate;
		}
		return unformattedDate;
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
		return monthsBetween;
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
			startDate = format.parse(format.format(startDate)); // make sure only date without time is saved in startDate
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
	
}
