package ch.zhaw.walj.projectmanagement;

public class Expense {
		
	private int id;
	private int projectID;
	private int employeeID;
	private float costs;
	private String type;
	private String description;
	private String date;
	
	public Expense (int id, int projectID, int employeeID, float costs, String type, String description, String date){
		this.id = id;
		this.projectID = projectID;
		this.employeeID = employeeID;
		this.costs = costs;
		this.type = type;
		this.description = description;
		this.date = date;
	}

	public int getID(){
		return id;
	}
	
	public int getProjectID(){
		return projectID;
	}
	
	public int getEmployeeID(){
		return employeeID;
	}
	
	public float getCosts() {
		return costs;
	}
	
	public String getType(){
		return type;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getDate(){
		return date;
	}
}
