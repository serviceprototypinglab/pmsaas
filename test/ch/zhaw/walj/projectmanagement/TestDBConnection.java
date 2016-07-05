package ch.zhaw.walj.projectmanagement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDBConnection {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "projectmanagement";
		String userName = "Janine"; 
		String password = "test123";
		
		DBConnection con;
		ResultSet res;
		
		con = new DBConnection(url, dbName, userName, password);
		res = con.getProjects(1);
		
		try {
			while (res.next()){
				System.out.println(res.getString("ProjectName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		
		}

	}

}