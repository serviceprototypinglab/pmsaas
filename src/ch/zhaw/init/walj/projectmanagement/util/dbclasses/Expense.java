package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

/**
 * class to create expenses 
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class Expense {
		
	private int id;
	private int projectID;
	private int employeeID;
	private double costs;
	private String type;
	private String description;
	private String date;
	
	/**
	 * constructor of the Expense class
	 * @param id
	 * 			ID of the expense
	 * @param projectID
	 * 			ID of the project the expense belongs to
	 * @param employeeID
	 * 			ID of the employee the expense belongs to
	 * @param costs
	 * 			costs of the expense
	 * @param type
	 * 			type of the expense
	 * @param description
	 * 			description of the expense
	 * @param date	
	 * 			date of the expense
	 */
	public Expense (int id, int projectID, int employeeID, double costs, String type, String description, String date){
		this.id = id;
		this.projectID = projectID;
		this.employeeID = employeeID;
		this.costs = costs;
		this.type = type;
		this.description = description;
		this.date = date;
	}

	/**
	 * @return the ID of the expense
	 */
	public int getID(){
		return id;
	}

	/**
	 * @return the ID of the Project the expense belongs to
	 */
	public int getProjectID(){
		return projectID;
	}

	/**
	 * @return the ID of the employee the expense belongs to
	 */
	public int getEmployeeID(){
		return employeeID;
	}

	/**
	 * @return the costs of the expense
	 */
	public double getCosts() {
		return costs;
	}
	
	/**
	 * @return the type of the expense
	 */
	public String getType(){
		return type;
	}

	/**
	 * @return the description of the expense
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * @return the date of the expense
	 */
	public String getDate(){
		return date;
	}
}
