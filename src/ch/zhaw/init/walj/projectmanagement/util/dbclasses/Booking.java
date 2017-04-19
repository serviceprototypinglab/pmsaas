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
 * implementation of the database class Bookings
 * @author Janine Walther, ZHAW
 *
 */
public class Booking {

	private final int bookingID;
	private final int month;
	private final double hours;
	private final int taskID;
	private final int employeeID;
	
	/**
	 * constructor of Booking
	 * @param bookingID ID of the booking
	 * @param assignmentID ID of the assignment
	 * @param month number of the month of the booking
	 * @param hours amount of hours booked
	 * @param taskID ID of the task
	 * @param employeeID ID of the employeee
	 */
	public Booking(int bookingID, int assignmentID, int month, double hours, int taskID, int employeeID){
		this.bookingID = bookingID;
		this.month = month;
		this.hours = hours;
		this.taskID = taskID;
		this.employeeID = employeeID;
	}
	
	/**
	 * @return ID of the booking
	 */
	public int getID(){
		return bookingID;
	}

    /**
	 * @return number of the month of the booking
	 */
	public int getMonth(){
		return month;
	}
	
	/**
	 * @return amount of hours booked
	 */
	public double getHours(){
		return hours;
	}
	
	/**
	 * @return ID of the task
	 */
	public int getTaskID(){
		return taskID;
	}
	
	/**
	 * @return ID of the employee
	 */
	public int getEmployeeID(){
		return employeeID;
	}
	
}
