package ch.zhaw.walj.projectmanagement;

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
	private DateHelper dateHelper = new DateHelper();
	

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
	 * get the effort on the project for a specific employee
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
	
}
