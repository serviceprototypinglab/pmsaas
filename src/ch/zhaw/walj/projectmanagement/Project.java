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
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Workpackage> wps = new ArrayList<Workpackage>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private ArrayList<Expense> expenses = new ArrayList<Expense>();
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
	
	public float getBudget(){
		return budget;
	}
	
	public String getBudgetFormatted(){
		String formattedBudget = String.format("%.2f", budget);
		return currency + " " + formattedBudget;
	}
	
	
	public String getPartners(){
		return partner;
	}	
	
	public void addEmployees(){
		for (Workpackage wp : wps){
			for (int i = 0; i < wp.nbrOfEmployees(); i++){
				Employee employee = wp.getEmployee(i);
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
	}
	
	public ArrayList<Employee> getEmployees(){
		return employees;
	}
	
	public Employee getSpecificEmployee(int id){
		for(Employee employee : employees){
			if (employee.getID() == id){
				return employee;
			}
		}
		return null;
	}
	
	public int nbrOfEmployees(){
		return employees.size();
	}
	
	public int nbrOfWorkpackages(){
		return wps.size();
	}
	

	public int nbrOfTasks(){
		int nbrOfTasks = 0;
		for (Workpackage wp : wps){
			nbrOfTasks += wp.nbrOfTasks();
		}
		return nbrOfTasks;
	}
	
	public ArrayList<Task> getTasks(){
		for (Workpackage wp : wps){
			tasks.addAll(wp.getTasks());
		}
		return tasks;		
	}

	public String getCurrency() {
		return currency;
	}
	
	
	public void addExpense(Expense expense){
		expenses.add(expense);
	}
	
	public ArrayList<Expense> getExpenses(){
		return expenses;
	}
	
	public float getTotalExpenses(){
		float total = 0;
		for (Expense expense : expenses){
			total += expense.getCosts();
		}
		return total;
	}
}
