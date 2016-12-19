package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

/**
 * implementation of the database class Assignments
 * @author Janine Walther, ZHAW
 */
public class Assignment {
	
	private int id;
	private int taskID;
	private int employeeID;
	
	/**
	 * constructor of Assignment, sets ID, taskID and employeeID
	 * @param id ID of the assignment
	 * @param taskID ID of the task
	 * @param employeeID ID of the employee
	 */
	public Assignment(int id, int taskID, int employeeID){
		this.id = id;
		this.taskID = taskID;
		this.employeeID = employeeID;
	}
	
	/**
	 * @return the Assignment ID
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return the task ID
	 */
	public int getTaskID(){
		return taskID;
	}

	/**
	 * @return the employee ID
	 */
	public int getEmployeeID() {
		return employeeID;
	}
}
