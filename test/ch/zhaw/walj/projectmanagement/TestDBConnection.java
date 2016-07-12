package ch.zhaw.walj.projectmanagement;

import java.sql.SQLException;

public class TestDBConnection {
	
	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "projectmanagement";
		String userName = "Janine";
		String password = "test123";
		
		DBConnection con = new DBConnection(url, dbName, userName, password);
		
		con.newEmployee(1, "Test", "Test", "Test", "Test", "78");
	}
	
}
