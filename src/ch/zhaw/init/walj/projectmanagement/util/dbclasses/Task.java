/**
 *	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 *	All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

import java.util.ArrayList;
import java.util.Date;

import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * implementation of the database class Tasks
 * @author Janine Walther, ZHAW
 *
 */
public class Task {

	// variable initialization
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
	private ArrayList<Weight> weights = new ArrayList<Weight>();
	
	/**
	 * constructor of the task class
	 * @param id ID of the task
	 * @param workpackageID ID of the workpackage the task belongs to
	 * @param name name of the task
	 * @param start start date of the task
	 * @param projectStart start date of the project
	 * @param end end date of the task
	 * @param pms planned PMs of the task
	 * @param budget budget of the task
	 * @param weights list of weights for each month of the task
	 */
	public Task(int id, int workpackageID, String name, String start, String projectStart, String end, int pms, double budget, ArrayList<Weight> weights){
		this.id = id;
		this.workpackageID = workpackageID;
		this.name = name;
		this.start = start;
		this.projectStart = projectStart;
		this.end = end;
		this.pms = pms;
		this.budget = budget;
		this.weights = weights;
	}
	
	/**
	 * adds an employee to the task 
	 * @param employee the employee who should be added
	 */
	public void addEmployee(Employee employee){
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
	
	/**
	 * @return ID of the task
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return ID of the workpackage the task belongs to
	 */
	public int getWorkpackageID(){
		return workpackageID;
	}
	
	/**
	 * @return the name of the task
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return the start date of the task as string
	 */
	public String getStart(){
		return start;
	}

	/**
	 * @return the start date of the task as date
	 */
	public Date getStartAsDate(){
		return DateFormatter.getInstance().stringToDate(start, "dd.MM.yyyy");
	}
	
	/**
	 * @return the end date of the task as string
	 */
	public String getEnd(){
		return end;
	}

	/**
	 * @return the end date of the task as date
	 */
	public Date getEndAsDate(){
		return DateFormatter.getInstance().stringToDate(end, "dd.MM.yyyy");
	}
	
	/**
	 * @return start and end date as string like "01.01.2017 - 31.01.2017"
	 */
	public String getDuration(){
		String duration = start + " - " + end;
		return duration;
	}
	
	/**
	 * @return amount of planned PMs
	 */
	public int getPMs(){
		return pms;
	}
	
	/**
	 * @return total amount of budget
	 */
	public double getBudget(){
		return budget;
	}
	
	/**
	 * @return list of all employees
	 */
	public ArrayList<Employee> getEmployees(){
		return employees;
	}
	
	/**
	 * @return amount of employees assigned to this task
	 */
	public int nbrOfEmployees(){
		return employees.size();
	}
	
	/**
	 * calculates the number of the start month in the project
	 * @return number of the month
	 */
	public int getStartMonth(){
		startMonth = DateFormatter.getInstance().getMonthsBetween(projectStart, start);
		return startMonth;
	}	
	
	/**
	 * calculates the number of the end month in the project
	 * @return number of the month
	 */
	public int getEndMonth(){
		endMonth = DateFormatter.getInstance().getMonthsBetween(projectStart, end);
		return endMonth;
	}	
	
	/**
	 * calculates the amount of months in this task
	 * @return the number of tasks
	 */
	public int getNumberOfMonths(){
		int numberOfMonths = 0;
		numberOfMonths = DateFormatter.getInstance().getMonthsBetween(start, end);
		return numberOfMonths;
	}
		
	/**
	 * calculates the PMs per month with including the weight of each month
	 * @return average PMs per month
	 */
	public double getPMsPerMonth(){
		
		double weight = 0;
		double pmsPerMonth = 0;
		
		for (Weight w : weights){
			weight += w.getWeight();
		}
		
		if (weight == 0) {
			pmsPerMonth = (double)pms / (double)this.getNumberOfMonths();
		} else {		
			pmsPerMonth = pms / weight;
		}				
		return pmsPerMonth;
	}

	/**
	 * @return list of all weights
	 */
	public ArrayList<Weight> getWeight() {
		return weights;
	}

	/**
	 * weight of a specific month
	 * @param month number of the month
	 * @return weight of the month
	 */
	public Weight getWeight(double month) {
		for (Weight w : weights){
			if (w.getMonth() == month){
				return w;
			}
		}
		return null;
	}
}
