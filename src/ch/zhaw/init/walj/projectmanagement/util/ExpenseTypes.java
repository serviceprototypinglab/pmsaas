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

package ch.zhaw.init.walj.projectmanagement.util;

/**
 * 	possible types for expenses
 * 	@author Janine Walther, ZHAW
 */	
public enum ExpenseTypes {
	
	TRAVEL {
		@Override
		public String toString() {
			return "Travel";
		}
	},
	
	OVERNIGHT_STAY {
		@Override
		public String toString() {
			return "Overnight Stay";
		}		
	},
	
	MEALS {
		@Override
		public String toString() {
			return "Meals";
		}		
	},
	
	OFFICE_SUPPLIES {
		@Override
		public String toString() {
			return "Office Supplies";
		}
	}, 
	
	EVENTS {
		@Override
		public String toString() {
			return "Events";
		}
	}
}
