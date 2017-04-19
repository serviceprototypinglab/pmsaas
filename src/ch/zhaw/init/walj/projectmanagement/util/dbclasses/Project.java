/*
 	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 	All Rights Reserved.

   Licensed under the Apache License, Version 2.0 (the "License"); you may
   not use this file except in compliance with the License. You may obtain
   a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
   License for the specific language governing permissions and limitations
   under the License.
 */

package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

import java.util.ArrayList;

import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * implementation of the database class Projects
 * @author Janine Walther, ZHAW
 *
 */
public class Project {
	
	// set EUR-CHF currency rate
	// TODO dynamic
	@SuppressWarnings("FieldCanBeLocal")
	private final double EUR = 0.9246;
	
	private final int id;
	private final String shortname;
	private final String name;
	private final int leader;
	private final String start;
	private final String end;
	private final String currency;
	private final double budget;
	private ArrayList<Task> tasks = new ArrayList<>();
	private ArrayList<Workpackage> workpackages = new ArrayList<>();
	private ArrayList<Employee> employees = new ArrayList<>();
	private ArrayList<Expense> expenses = new ArrayList<>();
	private final String partner;
	
	/**
	 * constructor of Project
	 * @param id ID of the project
	 * @param name full name of the project
	 * @param shortname short name of the project
	 * @param leader ID of the project leader
	 * @param start start date of the project
	 * @param end end date of the project
 	 * @param currency currency of the project (EUR or CHF)
	 * @param budget total budget of the project
	 * @param partner all partners of the project
	 */
	public Project(int id, String name, String shortname,int leader, String start, String end, String currency, double budget, String partner){

		this.id = id;
		this.name = name; 
		this.shortname = shortname;
		this.leader = leader;
		this.start = start;
		this.end = end;
		this.currency = currency;
		this.budget = budget;
		this.partner = partner;
		
	}
	
	/**
	 * adds a workpackage to the project
	 * @param wp the workpackage who should be added
	 */
	public void addWorkpackage(Workpackage wp){
		workpackages.add(wp);
		tasks.addAll(wp.getTasks());
	}

	/**
	 * @return the ID of the project
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return the short name of the project
	 */
	public String getShortname(){
		return shortname;
	}
	
	/**
	 * @return the full name of the project
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return the ID of the project leader
	 */
	public int getLeader(){
		return leader;
	}
	
	/**
	 * @return the start date of the project
	 */
	public String getStart(){
		return start;
	}

	/**
	 * @return the end date of the project
	 */
	public String getEnd(){
		return end;
	}
	
	/**
	 * @return the start and end date of the project
	 */
	public String getDuration(){
		return start + " - " + end;
	}
	
	/**
	 * @return the amount of months in the project
	 */
	public int getNumberOfMonths(){
		
		return DateFormatter.getInstance().getMonthsBetween(start, end);
	}

	/**
	 * @return the total budget
	 */
	public double getBudget(){
		return budget;
	}	
	
	/**
	 * @return all partners as a string
	 */
	public String getPartners(){
		return partner;
	}	
	
	/**
	 * 	adds all employees to the project
	 */
	public void addEmployees(){
		for (Workpackage wp : workpackages){
			for (int i = 0; i < wp.nbrOfEmployees(); i++){
				Employee employee = wp.getEmployees().get(i);
				int flag = 0;
				for (Employee e : employees){
					if (e.getKuerzel().equals(employee.getKuerzel())){
						flag++;
					}
				}
				if (flag == 0){
					if (currency.equals("EUR")) {
						// calculate wage in EUR
						employee.setWage(employee.getWage() * EUR);
					}
					employees.add(employee);
				}
			}
		}
	}
	
	/**
	 * @return list of all employees
	 */
	public ArrayList<Employee> getEmployees(){
		return employees;
	}
	
	/**
	 * @param id ID of the requested employee
	 * @return the employee or null
	 */
	public Employee getEmployee(int id){
		for(Employee employee : employees){
			if (employee.getID() == id){
				return employee;
			}
		}
		return null;
	}

	/**
	 * @return amount of employees
	 */
	public int nbrOfEmployees(){
		return employees.size();
	}
	
	/**
	 * @return currency as a string
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return list of workpackages
	 */
	public ArrayList<Workpackage> getWorkpackages() {
		return workpackages;
	}

	/**
	 * @return amount of workpackages
	 */
	public int nbrOfWorkpackages(){
		return workpackages.size();
	}

	/**
	 * @return list of all tasks
	 */
	public ArrayList<Task> getTasks(){
		return tasks;		
	}
	
	/**
	 * @param id ID of the requested task
	 * @return the task or null 
	 */
	public Task getTask(int id){
		for (Task t : tasks){
			if (t.getID() == id){
				return t;
			}
		}
		return null;
	}

	/**
	 * @return amount of tasks
	 */
	public int nbrOfTasks(){
		int nbrOfTasks = 0;
		for (Workpackage wp : workpackages){
			nbrOfTasks += wp.nbrOfTasks();
		}
		return nbrOfTasks;
	}

	/**
	 * adds a expense to the project
	 * @param expense the expense to add
	 */
	public void addExpense(Expense expense){
		expenses.add(expense);
	}

	/**
	 * @return a list of all expenses
	 */
	public ArrayList<Expense> getExpenses(){
		return expenses;
	}

	/**
	 * @return total amount of expenses
	 */
	public double getTotalExpenses(){
		double total = 0;
		for (Expense expense : expenses){
			total += expense.getCosts();
		}
		return total;
	}

	public Workpackage getWorkpackage(int taskWP) {
		for (Workpackage wp : workpackages){
			if (wp.getID() == taskWP){
				return wp;
			}
		}
		return null;
	}
}
