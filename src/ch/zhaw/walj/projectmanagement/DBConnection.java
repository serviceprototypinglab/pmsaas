package ch.zhaw.walj.projectmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * 
 * creates a connection to the database
 * 
 * 
 * @author Janine Walther, ZHAW
 *
 */
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
	
	/**
	 * 
	 * @param url
	 *            url to the db
	 * @param dbName
	 *            name of the db
	 * @param userName
	 * @param password
	 */
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
	
	/**
	 * returns all projects from the current user as project leader
	 * 
	 * @param id
	 *            id of the current user
	 * 
	 * @return all projects with current user as project leader
	 */
	public ResultSet getProjects(int id) {
		try {
			res = st.executeQuery("SELECT * FROM  Projects where ProjectLeader=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * returns all workpackages from the current project
	 * 
	 * @param id
	 *            id of the current project
	 * 
	 * @return all workpackages from the current project
	 */
	public ResultSet getWorkpackages(int id) {
		try {
			res = st.executeQuery("SELECT * FROM  Workpackages where ProjectIDFS=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * returns all tasks from the current workpackage
	 * 
	 * @param id
	 *            id of the current work
	 * 
	 * @return all tasks from the current workpackage
	 */
	public ResultSet getTasks(int id) {
		try {
			res = st.executeQuery("SELECT * FROM Tasks where WorkpackageIDFS=" + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * creates a new project in the database
	 * 
	 * 
	 * @param pName
	 *            name of the project
	 * @param pShortname
	 *            shortname for the project
	 * @param pBudget
	 *            Total Budget
	 * @param pCurrency
	 *            Project Budget in EUR or CHF
	 * @param pStart
	 *            start date of the project
	 * @param pEnd
	 *            end date of the project
	 * @param pPartners
	 *            project partners (name of the companies)
	 * 
	 * @return the new ID of the project in the database
	 * 
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public int newProject(String pName, String pShortname, String pBudget, String pCurrency, String pStart, String pEnd,
			String pPartners)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		// create the new project
		query = "INSERT INTO Projects ("
				+ "ProjectShortname, ProjectName, ProjectLeader, TotalBudget, Currency, ProjectStart, ProjectEnd, Partner"
				+ ") VALUES ('" + pShortname + "', '" + pName + "', " + "1" + ", " + pBudget + ", '" + pCurrency
				+ "', '" + pStart + "', '" + pEnd + "', '" + pPartners + "');";
		
		st.executeUpdate(query);
		
		// get the ID of the new project and return it afterwards
		query = "SELECT `ProjectIDFS` FROM `projectmanagement`.`Projects` ORDER BY `ProjectIDFS` DESC LIMIT 1";
		
		res = st.executeQuery(query);
		while (res.next()) {
			idToUse = res.getInt("ProjectIDFS");
		}
		return idToUse;
	}
	
	/**
	 * creates a new workpackage in the database
	 * 
	 * @param projectIDFS
	 *            ID of the project the workpackage belongs to
	 * @param wpName
	 *            name of the workpackage
	 * @param wpStart
	 *            start date of the workpackage
	 * @param wpEnd
	 *            end date of the workpackage
	 * 
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void newWorkpackage(int projectIDFS, String wpName, String wpStart, String wpEnd)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		// create the new workpackage
		query = "INSERT INTO Workpackages (" + "ProjectIDFS, WPName, WPStart, WPEnd" + ") VALUES (" + projectIDFS
				+ ", '" + wpName + "', '" + wpStart + "', '" + wpEnd + "');";
		
		st.executeUpdate(query);
		
	}
	
	/**
	 * creates a new task in the database
	 * 
	 * @param projectID
	 *            ID of the project the task belongs to
	 * @param wpName
	 *            name of the workpackage the task belongs to
	 * @param taskName
	 *            name of the task
	 * @param taskStart
	 *            start date of the task
	 * @param taskEnd
	 *            end date of the task
	 * @param taskPM
	 *            total PMs planned for the task
	 * @param taskBudget
	 *            total budget planned for the task
	 * 
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void newTask(int projectID, String wpName, String taskName, String taskStart, String taskEnd, String taskPM,
			String taskBudget)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(this.url + this.dbName, this.userName, this.password);
		st = conn.createStatement();
		
		// get the id from the workpackage (needed to create the new task)
		query = "SELECT `WorkpackageID` FROM `projectmanagement`.`Workpackages` WHERE WPName = '" + wpName
				+ "' AND ProjectIDFS = " + projectID + ";";
		
		res = st.executeQuery(query);
		res.next();
		idToUse = res.getInt("WorkpackageID");
		
		// create new task
		query = "INSERT INTO Tasks (" + "WorkpackageIDFS, TaskName, TaskStart, TaskEnd, PMs, Budget" + ") VALUES ("
				+ idToUse + ", '" + taskName + "', '" + taskStart + "', '" + taskEnd + "', " + taskPM + ", "
				+ taskBudget + ");";
		
		st.executeUpdate(query);
		
	}
	
	/**
	 * create a new employee in the database
	 * 
	 * @param employeeID
	 *            ID of the current user and the supervisor of the new user
	 * @param firstname
	 *            firstname of the new user
	 * @param lastname
	 *            lastname of the new user
	 * @param kuerzel
	 *            company intern kuerzel of the new user
	 * @param mail
	 *            mail of the new user
	 * @param wage
	 *            wage per hour of the new user
	 * 
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
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
