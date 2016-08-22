package ch.zhaw.walj.projectmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Effort {
	
	private ArrayList<Task> tasks = new ArrayList<Task>();
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
	
	
	public Effort (ArrayList<Task> tasks){
		this.tasks = tasks;
	}
	
	public double getPlannedEffort (double month){
		double effort = 0;
		int y = 0;
		for (Task task : tasks){
			if ((task.getStartMonth() == month) || (task.getEndMonth() == month)){
				effort += task.getPMsPerMonth();
			} else {
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
	
	public double getBookedEffort (double month) throws SQLException{
		double effort = 0.0;
		int y = 0;
		int projectMonth = 0;
		double hours = 0.0;
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			st2 = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Task task : tasks){
    		res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID() );
    		while (res.next()){
    			res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + res.getInt("AssignmentID"));
    			while (res2.next()){
    				projectMonth = res2.getInt("Month");
    				if (month == projectMonth){
    					effort += res2.getDouble("Hours");
    				} 		
    			}
    		}
    	}		
		
		effort = effort / 168;
		
		conn.close();
		return effort;
	}
	
	
	public float getEffortPerEmployee(int employeeID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		float effort = 0;
		
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(url + dbName, userName, password);
		st = conn.createStatement();
		st2 = conn.createStatement();	
		
		
		for (Task task : tasks){
			res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID() + " and EmployeeIDFS = " + employeeID);
			while (res.next()){
				res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + res.getInt("AssignmentID"));
    			while (res2.next()){
    				effort += res2.getFloat("Hours");
    			}
			}
		}
		conn.close();
		return effort;
	}
	
}
