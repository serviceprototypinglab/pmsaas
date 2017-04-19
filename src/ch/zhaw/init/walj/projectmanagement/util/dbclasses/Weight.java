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
 * implementation of the database class Weights
 * @author Janine Walther, ZHAW
 *
 */
public class Weight {

	private final int id;
	private final int taskIDFS;
	private final int month;
	private final double weight;
	
	/**
	 * constructor of weight
	 * @param id ID of the weight
	 * @param taskIDFS ID of the task
	 * @param month number of the month
	 * @param weight the weight of the month
	 */
	public Weight (int id, int taskIDFS, int month, double weight){
		this.id = id;
		this.taskIDFS = taskIDFS;
		this.month = month;
		this.weight = weight;
	}

	/**
	 * @return number of the month
	 */
	public int getMonth(){
		return month;
	}

	/**
	 * @return weight of the month
	 */
	public double getWeight(){
		return weight;
	}
}
