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

/**
 * implementation of the database class Assignments
 * @author Janine Walther, ZHAW
 */
public class Assignment {
	
	private int id;
	private int taskID;
	private int employeeID;
	
	/**
	 * constructor of Assignment, sets ID, taskID and employeeID
	 * @param id ID of the assignment
	 * @param taskID ID of the task
	 * @param employeeID ID of the employee
	 */
	public Assignment(int id, int taskID, int employeeID){
		this.id = id;
		this.taskID = taskID;
		this.employeeID = employeeID;
	}
	
	/**
	 * @return the Assignment ID
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return the task ID
	 */
	public int getTaskID(){
		return taskID;
	}

	/**
	 * @return the employee ID
	 */
	public int getEmployeeID() {
		return employeeID;
	}
}
