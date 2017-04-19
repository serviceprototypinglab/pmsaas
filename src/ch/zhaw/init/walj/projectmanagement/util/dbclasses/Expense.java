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

/**
 * class to create expenses 
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class Expense {
		
	private final int id;
	private final int projectID;
	private final int employeeID;
	private final double costs;
	private final String type;
	private final String description;
	private final String date;
	
	/**
	 * constructor of the Expense class
	 * @param id
	 * 			ID of the expense
	 * @param projectID
	 * 			ID of the project the expense belongs to
	 * @param employeeID
	 * 			ID of the employee the expense belongs to
	 * @param costs
	 * 			costs of the expense
	 * @param type
	 * 			type of the expense
	 * @param description
	 * 			description of the expense
	 * @param date	
	 * 			date of the expense
	 */
	public Expense (int id, int projectID, int employeeID, double costs, String type, String description, String date){
		this.id = id;
		this.projectID = projectID;
		this.employeeID = employeeID;
		this.costs = costs;
		this.type = type;
		this.description = description;
		this.date = date;
	}

	/**
	 * @return the ID of the expense
	 */
	public int getID(){
		return id;
	}

	/**
	 * @return the ID of the employee the expense belongs to
	 */
	public int getEmployeeID(){
		return employeeID;
	}

	/**
	 * @return the costs of the expense
	 */
	public double getCosts() {
		return costs;
	}
	
	/**
	 * @return the type of the expense
	 */
	public String getType(){
		return type;
	}

	/**
	 * @return the description of the expense
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * @return the date of the expense
	 */
	public String getDate(){
		return date;
	}
}
