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
	public static synchronized NumberFormatter getInstance(){
	  
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
