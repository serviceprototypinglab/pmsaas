package ch.zhaw.init.walj.projectmanagement.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Workpackage {
	
	private int id;
	private int projectID;
	private String name;
	private String start;
	private String end;
	private ArrayList<ProjectTask> tasks = new ArrayList<ProjectTask>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	
	public Workpackage(int id, int projectID, String name, String start, String end){
		this.id = id;
		this.projectID = projectID;
		this.name = name;
		this.start = start;
		this.end = end;		
	}
	
	public void addTask(ProjectTask task){
		tasks.add(task);		
	}
	
	public int nbrOfTasks(){
		return tasks.size();
	}	
	
	public ArrayList<ProjectTask> getTasks(){
		return tasks;		
	}

	public ArrayList<Employee> addEmployees(){
		for(ProjectTask task : tasks){
		int nbrOfEmployees = task.nbrOfEmployees();
			for (int i = 0; i < nbrOfEmployees; i++){
				Employee employee = task.getEmployee(i);
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
	
	public Date getStartAsDate() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		Date date = format.parse(start);
		return date;
	}
	
	public String getEnd(){
		return end;
	}
	
	public Date getEndAsDate() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		Date date = format.parse(end);
		return date;
	}
	
	public String getDuration(){
		String duration = start + " - " + end;
		return duration;
	}

	public Employee getEmployee(int i) {
		return employees.get(i);
	}
	
	
	
}
