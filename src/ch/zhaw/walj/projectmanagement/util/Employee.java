package ch.zhaw.walj.projectmanagement.util;

/**
 * class to create employees, usually filled with data from the employee table
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class Employee {
	
	private int id;
	private String firstname;
	private String lastname;
	private String kuerzel;
	private String mail;
	private double wage;
	private int supervisor;
	
	/**
	 * constructor for the employee class
	 * 
	 * @param id
	 * 			ID of the employee
	 * @param firstname
	 * 			firstname of the employee
	 * @param lastname
	 * 			lastname of the employee
	 * @param kuerzel
	 * 			kuerzel of the employee
	 * @param mail
	 * 			mail of the employee
	 * @param wage
	 * 			the employee's wage per hour
	 * @param supervisor
	 * 			supervisor of the employee
	 */
	public Employee(int id, String firstname, String lastname, String kuerzel, String mail, double wage, int supervisor){
		
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.kuerzel = kuerzel;
		this.mail = mail;
		this.wage = wage;
		this.supervisor = supervisor;
		
	}
	
	/**
	 * @return ID of the employee
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return the firstname and lastname of the employee together as a string
	 */
	public String getName(){
		String name = firstname + " " + lastname;
		return name;
	}
	
	/**
	 * @return kuerzel of the employee
	 */
	public String getKuerzel(){
		return kuerzel;
	}
	
	/**
	 * @return the mail address of the employee
	 */
	public String getMail(){
		return mail;
	}
	
	/**
	 * @return the employees wage per hour
	 */
	public double getWage(){
		return wage;
	}
	
	/**
	 * @return the ID of the supervisor
	 */
	public int getSupervisor(){
		return supervisor;
	}
}
