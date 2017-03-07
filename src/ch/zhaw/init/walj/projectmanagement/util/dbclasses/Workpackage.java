package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

import java.util.ArrayList;
import java.util.Date;

import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * implementation of the database class Workpackages
 * @author Janine Walther, ZHAW
 *
 */
public class Workpackage {
	
	// variable initialization
	private int id;
	private String name;
	private String start;
	private String end;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	
	/**
	 * constructor of Workpackage
	 * @param id ID of the workpackage 
	 * @param projectID ID of the project the workpackage belongs to
	 * @param name name of the workpackage
	 * @param start start date of the workpackage
	 * @param end end date of the workpackage
	 */
	public Workpackage(int id, String name, String start, String end){
		this.id = id;
		this.name = name;
		this.start = start;
		this.end = end;		
	}
	
	/**
	 * @return the ID of the workpackage
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return the name of the workpackage
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return the start date of the workpackage as a string
	 */
	public String getStart(){
		return start;
	}

	/**
	 * @return the start date of the workpackage as a date
	 */
	public Date getStartAsDate(){
		Date date = DateFormatter.getInstance().stringToDate(start, "dd.MM.yyyy");
		return date;
	}

	/**
	 * @return the end date of the workpackage as a string
	 */
	public String getEnd(){
		return end;
	}

	/**
	 * @return the end date of the workpackage as a date
	 */
	public Date getEndAsDate(){
		Date date = DateFormatter.getInstance().stringToDate(end, "dd.MM.yyyy");
		return date;
	}

	/**
	 * @return start and end date as string like "01.01.2017 - 31.01.2017"
	 */
	public String getDuration(){
		String duration = start + " - " + end;
		return duration;
	}

	/**
	 * adds a task to the workpackage
	 * @param task the task that should be added
	 */
	public void addTask(Task task){
		tasks.add(task);		
	}

	/**
	 * @return anmount of tasks in this workpackage
	 */
	public int nbrOfTasks(){
		return tasks.size();
	}

	/**
	 * @return a list of all tasks
	 */
	public ArrayList<Task> getTasks(){
		return tasks;		
	}

	/**
	 * adds the employee to the workpackage
	 * @return a list of all employees
	 */
	public ArrayList<Employee> addEmployees(){
		for(Task task : tasks){
			int nbrOfEmployees = task.nbrOfEmployees();
			for (int i = 0; i < nbrOfEmployees; i++){
				Employee employee = task.getEmployees().get(i);
				int flag = 0;
				for (Employee e : employees){
					if (e.getKuerzel().equals(employee.getKuerzel())){
						flag++;
					}
				}
				if (flag == 0){
					employees.add(employee);
				}
			}
		}
		return employees;
	}

	/**
	 * @return amount of employees
	 */
	public int nbrOfEmployees(){
		return employees.size();
	}

	/**
	 * @return list of all employees
	 */
	public ArrayList<Employee> getEmployees() {
		return employees;
	}
}
