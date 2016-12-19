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
