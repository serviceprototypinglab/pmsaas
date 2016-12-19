package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

/**
 * implementation of the database class Bookings
 * @author Janine Walther, ZHAW
 *
 */
public class Booking {

	private int bookingID;
	private int assignmentID;
	private int month;
	private double hours;
	private int taskID;
	private int employeeID;
	
	/**
	 * constructor of Booking
	 * @param bookingID ID of the booking
	 * @param assignmentID ID of the assignment
	 * @param month number of the month of the booking
	 * @param hours amount of hours booked
	 * @param taskID ID of the task
	 * @param employeeID ID of the employeee
	 */
	public Booking(int bookingID, int assignmentID, int month, double hours, int taskID, int employeeID){
		this.bookingID = bookingID;
		this.assignmentID = assignmentID;
		this.month = month;
		this.hours = hours;
		this.taskID = taskID;
		this.employeeID = employeeID;
	}
	
	/**
	 * @return ID of the booking
	 */
	public int getID(){
		return bookingID;
	}
	
	/**
	 * @return ID of the assignment
	 */
	public int getAssignmentID(){
		return assignmentID;
	}
	
	/**
	 * @return number of the month of the booking
	 */
	public int getMonth(){
		return month;
	}
	
	/**
	 * @return amount of hours booked
	 */
	public double getHours(){
		return hours;
	}
	
	/**
	 * @return ID of the task
	 */
	public int getTaskID(){
		return taskID;
	}
	
	/**
	 * @return ID of the employee
	 */
	public int getEmployeeID(){
		return employeeID;
	}
	
}
