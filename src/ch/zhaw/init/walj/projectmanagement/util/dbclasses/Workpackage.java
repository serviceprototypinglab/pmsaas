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
import java.util.Date;

import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * implementation of the database class Workpackages
 * @author Janine Walther, ZHAW
 *
 */
public class Workpackage {
	
	// variable initialization
	private final int id;
	private final String name;
	private final String start;
	private final String end;
	private final ArrayList<Task> tasks = new ArrayList<>();
	private final ArrayList<Employee> employees = new ArrayList<>();
	
	/**
	 * constructor of Workpackage
	 * @param id ID of the workpackage 
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
        return DateFormatter.getInstance().stringToDate(start, "dd.MM.yyyy");
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
		return DateFormatter.getInstance().stringToDate(end, "dd.MM.yyyy");
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
	 */
	public void addEmployees(){
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
