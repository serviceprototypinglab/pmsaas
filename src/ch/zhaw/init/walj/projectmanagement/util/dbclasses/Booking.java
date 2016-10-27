package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

public class Booking {

	private int bookingID;
	private int assignmentID;
	private int month;
	private double hours;
	private int taskID;
	private int employeeID;
	
	public Booking(int bookingID, int assignmentID, int month, double hours, int taskID, int employeeID){
		this.bookingID = bookingID;
		this.assignmentID = assignmentID;
		this.month = month;
		this.hours = hours;
		this.taskID = taskID;
		this.employeeID = employeeID;
	}
	
	public int getID(){
		return bookingID;
	}
	
	public int getAssignmentID(){
		return assignmentID;
	}
	
	public int getMonth(){
		return month;
	}
	
	public double getHours(){
		return hours;
	}
	
	public int getTaskID(){
		return taskID;
	}
	
	public int getEmployeeID(){
		return employeeID;
	}
	
}
