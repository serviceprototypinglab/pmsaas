package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class to calculate the effort per month and employee
 *  
 * @author Janine Walther, ZHAW
 *
 */
public class Effort {
	
	private ArrayList<ProjectTask> tasks = new ArrayList<ProjectTask>();
	

	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "projectmanagement";
	private String userName	= "Janine";
	private String password	= "test123";
	private Connection conn;
	private Statement st;
	private ResultSet res;
	private Statement st2;
	private ResultSet res2;
	
	
	
	/**
	 * constructor of the Effort class
	 * @param tasks ArrayList with all tasks
	 */
	public Effort (ArrayList<ProjectTask> tasks){
		this.tasks = tasks;
	}
	
	
	public ArrayList<Booking> getBookings () throws SQLException {
		
		int bookingID;
		int assignmentID;
		int month;
		double hours;
		int taskID;
		int employeeID;
		
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			st2 = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (ProjectTask task : tasks){
			// get assignments to the task
    		res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID());
    		while (res.next()){
    			assignmentID = res.getInt("AssignmentID");
    			taskID = task.getID();
    			employeeID = res.getInt("EmployeeIDFS"); 
    			// get bookings to the assignment
    			res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + assignmentID + " order by Month");
    			while (res2.next()){
    				bookingID = res2.getInt("BookingID");
    				month = res2.getInt("Month");
    				hours = res2.getDouble("Hours");
    				Booking booking = new Booking(bookingID, assignmentID, month, hours, taskID, employeeID);
    				bookings.add(booking);
    			}
    		}
    	}	
		
		return bookings;
	}
	
public ArrayList<Booking> getBookings (int employeeID) throws SQLException {
		
		int bookingID;
		int assignmentID;
		int month;
		double hours;
		int taskID;
		
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			st2 = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (ProjectTask task : tasks){
			// get assignments to the task
    		res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID() + " and EmployeeIDFS = " + employeeID);
    		while (res.next()){
    			assignmentID = res.getInt("AssignmentID");
    			taskID = task.getID();
    			// get bookings to the assignment
    			res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + assignmentID);
    			while (res2.next()){
    				bookingID = res2.getInt("BookingID");
    				month = res2.getInt("Month");
    				hours = res2.getDouble("Hours");
    				Booking booking = new Booking(bookingID, assignmentID, month, hours, taskID, employeeID);
    				bookings.add(booking);
    			}
    		}
    	}	
		
		return bookings;
	}
	
	/**
	 * get the planned effort for a specific month from all tasks
	 *
	 * @param month number of a month
	 * @return planned effort as PMs
	 */
	public double getPlannedEffort (double month){
		double effort = 0;
		int y = 0;
		for (ProjectTask task : tasks){
			// compare the given month to the start and end month from the task
			if ((task.getStartMonth() == month) || (task.getEndMonth() == month)){
				effort += task.getPMsPerMonth();
			} else {
				// compare the given months to the other months
				for (int i = 2; i < task.getNumberOfMonths(); i++){
					y = task.getStartMonth() + (task.getNumberOfMonths() - i);
					if (y == month){
						effort += task.getPMsPerMonth();
					}
				}
			}
		}		
		return effort;
	}
	
	/**
	 * get the booked effort for a specific month from all tasks
	 * @param month
	 * @return booked effort as PMs
	 * @throws SQLException
	 */
	public double getBookedEffort (double month) throws SQLException{
		double effort = 0.0;
		int projectMonth = 0;
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			st2 = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (ProjectTask task : tasks){
			// get assignments to the task
    		res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID());
    		while (res.next()){
    			// get bookings to the assignment
    			res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + res.getInt("AssignmentID"));
    			while (res2.next()){
    				projectMonth = res2.getInt("Month");
    				// compare month of the booking to the given month
    				if (month == projectMonth){
    					effort += res2.getDouble("Hours");
    				} 		
    			}
    		}
    	}		
		
		// calculate PMs
		effort = effort / 168;
		
		conn.close();
		return effort;
	}
	
	/**
	 * get the effort on the project for a specific employee (in hours)
	 * 
	 * @param employeeID
	 * 				ID of the employee
	 * @return
	 * 		effort in hours
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	
	public float getEffortPerEmployee(int employeeID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		float effort = 0;
		
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(url + dbName, userName, password);
		st = conn.createStatement();
		st2 = conn.createStatement();	
		
		for (ProjectTask task : tasks){
			// get the assignmentID for the assignment with the given task and employee
			res = st.executeQuery("select AssignmentID from Assignments where TaskIDFS = " + task.getID() + " and EmployeeIDFS = " + employeeID);
			while (res.next()){
				// get the hours from the bookings table
				res2 = st2.executeQuery("select Hours from Bookings where AssignmentIDFS = " + res.getInt("AssignmentID"));
    			while (res2.next()){
    				effort += res2.getFloat("Hours");
    			}
			}
		}
		
		conn.close();
		return effort;
	}


	public double getBookedEffort(double month, int employee) throws SQLException {
		double effort = 0.0;
		int projectMonth = 0;
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			st2 = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (ProjectTask task : tasks){
			// get assignments to the task
    		res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID() + " and EmployeeIDFS = " + employee);
    		while (res.next()){
    			// get bookings to the assignment
    			res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + res.getInt("AssignmentID"));
    			while (res2.next()){
    				projectMonth = res2.getInt("Month");
    				// compare month of the booking to the given month
    				if (projectMonth == month){
    					effort += res2.getDouble("Hours");
    				} 		
    			}
    		}
    	}		
		
		
		conn.close();
		return effort;
	}
	
}
