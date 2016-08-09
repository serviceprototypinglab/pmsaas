package ch.zhaw.walj.projectmanagement;

import java.util.ArrayList;

public class Project {
	
	private String shortname;
	private String name;
	private int id;
	private String start;
	private String end;
	private String currency;
	private float budget;
	private ArrayList<Workpackage> wps = new ArrayList<Workpackage>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private String partner;
	
	public Project(int id, String name, String shortname, String start, String end, String currency, float budget, String partner){
		this.id = id;
		this.name = name; 
		this.shortname = shortname;
		this.start = start;
		this.end = end;
		this.currency = currency;
		this.budget = budget;
		this.partner = partner;
		
	}
	
	public void addWorkpackage(Workpackage wp){
		wps.add(wp);
	}

	public String getShortname(){
		return shortname;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
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
	
	public String getBudget(){
		String formattedBudget = String.format("%.2f", budget);
		return currency + " " + formattedBudget;
	}
	
	public String getPartners(){
		return partner;
	}	
	
	public ArrayList<Employee> getEmployees(){
		for (Workpackage wp : wps){
			for (int i = 0; i < wp.nbrOfEmployees(); i++){
				Employee employee = wp.getEmployee(i);
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
}
