package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

public class Assignment {
	
	private int id;
	private int taskID;
	private int employeeID;
	
	public Assignment(int id, int taskID, int employeeID){
		this.id = id;
		this.taskID = taskID;
		this.employeeID = employeeID;
	}
	
	public int getID(){
		return id;
	}
	
	public int getTaskID(){
		return taskID;
	}

	public int getEmployeeID() {
		return employeeID;
	}
}
