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
 * class to create employees, usually filled with data from the employee table
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class Employee {
	
	private int id;
	private String firstname;
	private String lastname;
	private String kuerzel;
	private String mail;
	private String password;
	private double wage;
	private int supervisor;
	
	/**
	 * constructor for the employee class
	 * 
	 * @param id
	 * 			ID of the employee
	 * @param firstname
	 * 			firstname of the employee
	 * @param lastname
	 * 			lastname of the employee
	 * @param kuerzel
	 * 			kuerzel of the employee
	 * @param mail
	 * 			mail of the employee
	 * @param ePassword 
	 * 			password of the employee
	 * @param wage
	 * 			the employee's wage per hour
	 * @param supervisor
	 * 			supervisor of the employee
	 */
	public Employee(int id, String firstname, String lastname, String kuerzel, String mail, String password, int wage, int supervisor){
		
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.kuerzel = kuerzel;
		this.mail = mail;
		this.password = password;
		this.wage = wage;
		this.supervisor = supervisor;
		
	}
	
	/**
	 * @return ID of the employee
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * @return the firstname of the employee as a string
	 */
	public String getFirstName(){
		return firstname;
	}
	
	
	/**
	 * @return the lastname of the employee as a string
	 */
	public String getLastName(){
		return lastname;
	}
	
	/**
	 * @return the firstname and lastname of the employee in form of "firstname lastname"
	 */
	public String getName(){
		String name = firstname + " " + lastname;
		return name;
	}
	
	/**
	 * @return the firstname and lastname of the employee in form of "lastname, firstname"
	 */
	public String getFullName(){
		String name = lastname + ", " + firstname;
		return name;
	}
	
	/**
	 * @return kuerzel of the employee
	 */
	public String getKuerzel(){
		return kuerzel;
	}
	
	/**
	 * @return the mail address of the employee
	 */
	public String getMail(){
		return mail;
	}
	
	
	/**
	 * @return the password of the employee
	 */
	public String getPassword(){
		return password;
	}
	
	
	/**
	 * set a new password for the employee
	 * @param password
	 */
	public void setNewPassword(String password){
		this.password = password;
	}
	
	
	/**
	 * @return the employees wage per hour
	 */
	public double getWage(){
		return wage;
	}
	
	public void setWage(double wage){
		this.wage = wage;
	}
	
	
	/**
	 * @return the ID of the supervisor
	 */
	public int getSupervisor(){
		return supervisor;
	}
}
