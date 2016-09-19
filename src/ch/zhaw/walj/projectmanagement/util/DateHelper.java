package ch.zhaw.walj.projectmanagement.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateHelper has some useful methods for date strings 
 *
 * @author Janine Walther, ZHAW
 * 
 */
public class DateHelper {
	
	private String date;
	
	private int day;
	private int month;
	private int year;
	
	public DateHelper(){
		
	}
	/**
	 * Constructor of the DateHelper class
	 * 
	 * @param date 
	 * 			date as a string
	 */
	public DateHelper(String date){
		this.date = date;
		String[] helper = date.split("-");
		day = Integer.parseInt(helper[2]);
		month = Integer.parseInt(helper[1]);
		year = Integer.parseInt(helper[0]);
	}
	
	
	public String getFormattedDate(){
		return getFormattedDate(date);
	}
	
	/**
	 * 
	 * @param unformatted
	 * 				date like 'YYYY-MM-DD' as a string
	 * @return
	 * 				date like 'DD.MM.YYYY' as a string
	 */
	public String getFormattedDate(String unformatted){
		String[] helper = unformatted.split("-");
		if (helper.length == 3) {
			String formatted = helper[2] + "." + helper[1] + "." + helper[0];
			return formatted;
		}
		return unformatted;
	}
	
	
	public int getMonthsBetween(String end){
		return getMonthsBetween(date, end);
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
		
		boolean f = false;
		int monthsBetween = 0;
		int startMonth;
		int endMonth;
		int startYear;
		int endYear;
		
		String[] startDate = start.split("\\.");
		String[] endDate = end.split("\\.");
		

		startMonth = Integer.parseInt(startDate[1]);
		endMonth = Integer.parseInt(endDate[1]);

		startYear = Integer.parseInt(startDate[2]);
		endYear = Integer.parseInt(endDate[2]);
		
		while (!f) {
			if (startMonth == endMonth){
				if (startYear == endYear){
					monthsBetween++;
					f = true;
				} else {
					do {
						startYear++;
						monthsBetween += 12;
					} while (startYear != endYear);
					f= true;
				}
			} else {
				monthsBetween++;
				do {
					if (startMonth == 12){
						startYear++;
						startMonth = 0;
					}
					startMonth++;
					monthsBetween++;
				} while ((startMonth != endMonth) || (startYear != endYear));
				f = true;
			}
			
		}
		return monthsBetween;
	}
	
	
	public int getNumberOfMonth(String start){				
		return getMonthsBetween(start, date);
	}
	
	public int getDaysBetween(Date startDate, String end){
		int days = 0;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		try {		
			Calendar cStart = Calendar.getInstance();
			Calendar cEnd = Calendar.getInstance();
						
			String start = format.format(startDate);	
			Date endDate = format.parse(end);
			startDate = format.parse(start);
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
}
