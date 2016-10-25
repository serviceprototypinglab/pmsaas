package ch.zhaw.init.walj.projectmanagement.util;

public class NumberFormatter {

	private static NumberFormatter instance;
	
	
	public static synchronized NumberFormatter getInstance(){
	  
	    if(instance == null){
	       instance = new NumberFormatter(); 
	    } 
	    return instance;
	}
	
	
	public String formatDouble(double number){
		return String.format("%.2f", number);
	}
	
	
	
	public String formatHours(double number){
		return String.format("%.0f", number);
	}
}
