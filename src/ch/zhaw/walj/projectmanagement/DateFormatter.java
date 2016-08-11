package ch.zhaw.walj.projectmanagement;

public class DateFormatter {
	
	public String getFormattedString(String unformatted){
		String[] helper = unformatted.split("-");
		String formatted = helper[2] + "." + helper[1] + "." + helper[0];
		return formatted;
	}
	
	
}
