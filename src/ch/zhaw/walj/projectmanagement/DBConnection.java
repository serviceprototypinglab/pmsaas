package ch.zhaw.walj.projectmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class DBConnection {
	
	String				driver		= "com.mysql.jdbc.Driver";
	String				url			= "";
	String				dbName		= "";
	String				userName	= "";
	String				password	= "";
	Connection			conn;
	Statement			st;
	PreparedStatement	pstmt;
	ResultSet			res;
	String				query;
	int					idToUse;
	
	private Random		rng;
	private String		characters	= "QWERTZUIOPASDFGHJKLYXCVBNMmnbvcxylkjhgfdsapoiuztrewq1234567890";
	private int			length		= 12;
	
	public DBConnection(String url, String dbName, String userName, String password) {
		this.url = url;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
			st = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ResultSet getProjects(int id) {
		try {
			res = st.executeQuery("SELECT * FROM  Projects where ProjectLeader=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public ResultSet getWorkpackages(int id) {
		try {
			res = st.executeQuery("SELECT * FROM  Workpackages where ProjectIDFS=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public ResultSet getTasks(int id) {
		try {
			res = st.executeQuery("SELECT * FROM Tasks where WorkpackageIDFS=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public int newProject(String pName, String pShortname, String pBudget, String pCurrency, String pStart, String pEnd,
			String pPartners)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		query = "INSERT INTO Projects ("
				+ "ProjectShortname, ProjectName, ProjectLeader, TotalBudget, Currency, ProjectStart, ProjectEnd, Partner"
				+ ") VALUES ('" + pShortname + "', '" + pName + "', " + "1" + ", " + pBudget + ", '" + pCurrency
				+ "', '" + pStart + "', '" + pEnd + "', '" + pPartners + "');";
		
		st.executeUpdate(query);
		
		query = "SELECT `ProjectIDFS` FROM `projectmanagement`.`Projects` ORDER BY `ProjectIDFS` DESC LIMIT 1";
		
		res = st.executeQuery(query);
		while (res.next()) {
			idToUse = res.getInt("ProjectIDFS");
		}
		return idToUse;
	}
	
	public void newWorkpackage(int projectIDFS, String wpName, String wpStart, String wpEnd)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		query = "INSERT INTO Workpackages (" + "ProjectIDFS, WPName, WPStart, WPEnd" + ") VALUES (" + projectIDFS
				+ ", '" + wpName + "', '" + wpStart + "', '" + wpEnd + "');";
		
		st.executeUpdate(query);
		
	}
	
	public void newTask(int projectID, String wpName, String taskName, String taskStart, String taskEnd, String taskPM,
			String taskBudget)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		query = "SELECT `WorkpackageID` FROM `projectmanagement`.`Workpackages` WHERE WPName = '" + wpName
				+ "' AND ProjectIDFS = " + projectID + ";";
		
		res = st.executeQuery(query);
		res.next();
		idToUse = res.getInt("WorkpackageID");
		
		query = "INSERT INTO Tasks (" + "WorkpackageIDFS, TaskName, TaskStart, TaskEnd, PMs, Budget" + ") VALUES ("
				+ idToUse + ", '" + taskName + "', '" + taskStart + "', '" + taskEnd + "', " + taskPM + ", "
				+ taskBudget + ");";
		
		st.executeUpdate(query);
		
	}
	
	public void newEmployee(int employeeID, String firstname, String lastname, String kuerzel, String mail, String wage)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		char[] text = new char[length];
		String password = "";
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
			password = password + text[i];
		}
		
		query = "INSERT INTO Employees (" + "Firstname, Lastname, Kuerzel, Password, Mail, Supervisor" + ") VALUES ('"
				+ firstname + "', '" + lastname + "', '" + kuerzel + "', '" + password + "', '" + mail + "', "
				+ employeeID + ");";
		
		st.executeUpdate(query);
	}
	
	public void closeConnection() {
		try {
			res.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
