package ch.zhaw.walj.projectmanagement;

import java.util.ArrayList;

public class Task {

	private int id;
	private int workpackageID;
	private String name;
	private String start;
	private String end;
	private int pms;
	private float budget;
	private ArrayList<Employee> employees = new ArrayList<Employee>();

	
	public Task(int id, int workpackageID, String name, String start, String end, int pms, float budget){
		this.id = id;
		this.workpackageID = workpackageID;
		this.name = name;
		this.start = start;
		this.end = end;
		this.pms = pms;
		this.budget = budget;
	}
	
	public void addEmployee(Employee employee){
		String nameEmployee;
		String nameNewEmployee;
		int flag = 0;
		for (Employee e : employees){
			nameEmployee = e.getKuerzel();
			nameNewEmployee = employee.getKuerzel();
			if (nameEmployee.equals(nameNewEmployee)){
				flag++;
			}
		}
		if (flag == 0){
			employees.add(employee);
		}
	}
	
	public int getID(){
		return id;
	}
	
	public int getWorkpackageID(){
		return workpackageID;
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
	
	public int getPMs(){
		return pms;
	}
	
	public float getBudget(){
		return budget;
	}
	
	public int nbrOfEmployees(){
		return employees.size();
	}
	
	public Employee getEmployee(int i){
		return employees.get(i);
	}
	
}
