package ch.zhaw.walj.projectmanagement;

import java.util.ArrayList;

public class Workpackage {
	
	private int id;
	private int projectID;
	private String name;
	private String start;
	private String end;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	
	public Workpackage(int id, int projectID, String name, String start, String end){
		this.id = id;
		this.projectID = projectID;
		this.name = name;
		this.start = start;
		this.end = end;		
	}
	
	public void addTask(Task task){
		tasks.add(task);		
	}
	
	public int nbrOfTasks(){
		return tasks.size();
	}	

	public ArrayList<Employee> getEmployees(){
		for(Task task : tasks){
			for (int i = 0; i < task.nbrOfEmployees(); i++){
				Employee employee = task.getEmployee(i);
				if (!employees.contains(employee)){
					employees.add(employee);
				}
			}
		}
		return employees;
	}
	
	public int nbrOfEmployees(){
		return employees.size();
	}
	
	public int getID(){
		return id;
	}
	
	public int getProjectID(){
		return projectID;
	}
	
	public String getName(){
		return name;
	}
	

	public String getStart(){
		return start;
	}
	
	public String getEnd(){
		return end;
	}
	
	public String getDuration(){
		String duration = start + " - " + end;
		return duration;
	}

	public Employee getEmployee(int i) {
		return employees.get(i);
	}
	
	
	
}
