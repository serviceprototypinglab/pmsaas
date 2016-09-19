package ch.zhaw.walj.projectmanagement.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProjectTask {

	private int id;
	private int workpackageID;
	private String name;
	private String start;
	private String projectStart;
	private String end;
	private int pms;
	private double budget;
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private int startMonth;
	private int endMonth;
	private DateHelper dateHelper = new DateHelper();

	
	public ProjectTask(int id, int workpackageID, String name, String start, String projectStart, String end, int pms, double budget){
		this.id = id;
		this.workpackageID = workpackageID;
		this.name = name;
		this.start = start;
		this.projectStart = projectStart;
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
	
	public int getPMs(){
		return pms;
	}
	
	public double getBudget(){
		return budget;
	}
	
	public int nbrOfEmployees(){
		return employees.size();
	}
	
	public Employee getEmployee(int i){
		return employees.get(i);
	}	
	
	public int getStartMonth(){
		startMonth = dateHelper.getMonthsBetween(projectStart, start);
		return startMonth;
	}	
	
	public int getEndMonth(){
		endMonth = dateHelper.getMonthsBetween(projectStart, end);
		return endMonth;
	}	
	
	public int getNumberOfMonths(){
		int numberOfMonths = 0;
		numberOfMonths = dateHelper.getMonthsBetween(start, end);
		return numberOfMonths;
	}
		
	public double getPMsPerMonth(){
		double pmsPerMonth = pms / this.getNumberOfMonths();
		return pmsPerMonth;
	}
}
