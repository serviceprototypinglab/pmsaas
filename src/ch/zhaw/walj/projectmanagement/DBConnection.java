package ch.zhaw.walj.projectmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	String driver = "com.mysql.jdbc.Driver";
	String url = "";
	String dbName = "";
	String userName = ""; 
	String password = "";
	Connection conn;
	Statement st;
	PreparedStatement pstmt;
	ResultSet res;
	String query;
	
	public DBConnection(String url, String dbName, String userName, String password){
		this.url = url;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(this.url+this.dbName,this.userName,this.password);
			st = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	public ResultSet getProjects (int id){
		 try {
			res = st.executeQuery("SELECT * FROM  Projects where ProjectLeader=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	public ResultSet getWorkpackages (int id){
		 try {
			res = st.executeQuery("SELECT * FROM  Workpackages where ProjectIDFS=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	public ResultSet getTasks (int id){
		 try {
			res = st.executeQuery("SELECT * FROM Tasks where WorkpackageIDFS=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	public int createProject(String pName, String pShortname, String pBudget, String pCurrency, String pStart, String pEnd, String pPartners) throws SQLException{
				
		conn = DriverManager.getConnection(this.url+this.dbName,this.userName,this.password);
		st = conn.createStatement();
		
		
		query = "INSERT INTO Projects (" +
				"ProjectShortname, ProjectName, ProjectLeader, TotalBudget, Currency, ProjectStart, ProjectEnd, Partner" +
				") VALUES ('" +
				pShortname + "', '" +
				pName +	"', " +
				"1" + ", " + 
				pBudget + ", '" +
				pCurrency +	"', '" +
				pStart + "', '" +
				pEnd + "', '" +
				pPartners +	"');";
				
		int nothingToDoHere = st.executeUpdate(query);
		
		query = "SELECT `ProjectIDFS` FROM `projectmanagement`.`Projects` ORDER BY `ProjectIDFS` DESC LIMIT 1";
		
		res = st.executeQuery(query);
		return res.getInt("ProjectIDFS");
	}
	
	public void closeConnection () {
		try {
			res.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
