package ch.zhaw.init.walj.projectmanagement.util;

import java.sql.SQLException;
import java.util.ArrayList;

import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Assignment;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Booking;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Weight;

/**
 * Class to calculate the effort per month and employee
 *  
 * @author Janine Walther, ZHAW
 *
 */
public class Effort {
	
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	DBConnection con;
	
	/**
	 * constructor of the Effort class
	 * @param tasks ArrayList with all tasks
	 */
	public Effort (ArrayList<Task> tasks, String path){
		this.tasks = tasks;
		con = new DBConnection(path);
	}
	
	
	public ArrayList<Booking> getBookings () throws SQLException {
			
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		
		for (Task task : tasks){
			ArrayList<Assignment> assignments = con.getAssignments(task.getID());
			for (Assignment a : assignments){
				bookings.addAll(con.getBookings(a));
			}
    	}	
		
		return bookings;
	}
	
	/**
	 * get all bookings for a specific employee
	 * @param employeeID ID of the employee
	 * @return list of all bookings of the employee
	 * @throws SQLException
	 */
	public ArrayList<Booking> getBookings (int employeeID) throws SQLException {
				
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		for (Task task : tasks){
			Assignment assignment = con.getAssignment(employeeID, task.getID());
			if (assignment != null) {
				bookings.addAll(con.getBookings(assignment));
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
		for (Task task : tasks){
			Weight w = task.getWeight(month);
			double weight;
			
			if (w == null) {
				weight = 1;
			} else {
				weight = w.getWeight();
			}
			
			// compare the given month to the start and end month from the task
			if ((task.getStartMonth() == month) || (task.getEndMonth() == month)){
				effort += (task.getPMsPerMonth() * weight);
			} else {
				// compare the given months to the other months
				for (int i = 2; i < task.getNumberOfMonths(); i++){
					y = task.getStartMonth() + (task.getNumberOfMonths() - i);
					if (y == month){
						effort += task.getPMsPerMonth() * weight;
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
		
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		for (Task task : tasks){
			ArrayList<Assignment> assignments = con.getAssignments(task.getID());
			for (Assignment a : assignments){
				bookings = con.getBookings(a);
				for (Booking b : bookings){
					if (month == b.getMonth()){
						effort += b.getHours();
					}
				}
			}
    	}			
		
		// calculate PMs
		effort = effort / 168;
		
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
	
	public float getEffortPerEmployee(int employee){
		float effort = 0;
		ArrayList<Booking> bookings = new ArrayList<Booking>();
				
		for (Task task : tasks){
			// get assignments to the task
			Assignment a;
			try {
				a = con.getAssignment(employee, task.getID());
				// get bookings to the assignment
				bookings = con.getBookings(a);
				for (Booking b : bookings){
						effort += b.getHours(); 		
	    		}
			} catch (SQLException | NullPointerException e) {
			
			}
			
    	}	
		
		return effort;
	}


	/**
	 * get the booked effort for a specific month and employee
	 * @param month number of the month
	 * @param employee ID of the employee
	 * @return amount of hours booked by the employee in a specific month
	 */
	public double getBookedEffortPerMonth(double month, int employee){
		double effort = 0.0;
		
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		for (Task task : tasks){
			try {
				// get assignments to the task
				Assignment a = con.getAssignment(employee, task.getID());
				// get bookings to the assignment
				bookings = con.getBookings(a);
				for (Booking b : bookings){
					if (b.getMonth() == month){
						effort += b.getHours();
					} 		
	    		}
			} catch (SQLException | NullPointerException e) {
			}
    	}		
		
		return effort;
	}
	
}
