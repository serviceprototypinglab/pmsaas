package ch.zhaw.walj.projectmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
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
	
	private String				driver		= "com.mysql.jdbc.Driver";
	private String				url			= "";
	private String				dbName		= "";
	private String				userName	= "";
	private String				password	= "";
	private Connection			conn;
	private Statement			st;
	private ResultSet			res;
	private String				query;
	private int					idToUse;
	
	private Random				rndm;
	private String				characters	= "QWERTZUIOPASDFGHJKLYXCVBNMmnbvcxylkjhgfdsapoiuztrewq1234567890";
	private int					length;
	
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
	 * creates a new Project object and returns it to the user
	 * 
	 * @param id
	 *            id of the project
	 * 
	 * @return project with data from the database
	 * @throws SQLException 
	 */
	public Project getProject(int pID) throws SQLException {

		Statement stProject = conn.createStatement();
		Statement stWP = conn.createStatement();
		Statement stTask = conn.createStatement();
		Statement stEmployee = conn.createStatement();
		Statement stWage = conn.createStatement();
		

		ResultSet resProject;
		ResultSet resWP;
		ResultSet resTask;
		ResultSet resEmployee;
		ResultSet resWage;
				
		Project project;
		String pName;
		String pShortname;
		String pStart;
		String pEnd;
		String pCurrency;
		float pBudget;
		String pPartner;
		
		int wID;
		int wProjectID;
		String wName;
		String wStart;
		String wEnd;
		
		int tID;
		int tWorkpackageID;
		String tName;
		String tStart;
		String tEnd;
		int tPMs;
		float tBudget;
		
		int eID;
		String eFirstname;
		String eLastname;
		String eKuerzel;
		String eMail;
		float eWage;
		int eSupervisor;
		
		resProject = stProject.executeQuery("SELECT * FROM  Projects where ProjectIDFS=" + pID + "");
		
		resProject.next();
		pName = resProject.getString("ProjectName");
		pShortname = resProject.getString("ProjectShortname");
		pStart = resProject.getString("ProjectStart");
		pEnd = resProject.getString("ProjectEnd");
		pCurrency = resProject.getString("Currency");
		pBudget = resProject.getFloat("TotalBudget");
		pPartner = resProject.getString("Partner");
		
		project = new Project(pID, pName, pShortname, pStart, pEnd, pCurrency, pBudget, pPartner);
		
		resWP = stWP.executeQuery("SELECT * FROM  Workpackages where ProjectIDFS=" + pID + "");
		while (resWP.next()){
			wID = resWP.getInt("WorkpackageID");
			wProjectID = resWP.getInt("ProjectIDFS");
			wName = resWP.getString("WPName");
			wStart = resWP.getString("WPStart");
			wEnd = resWP.getString("WPEnd");
			
			Workpackage wp = new Workpackage(wID, wProjectID, wName, wStart, wEnd);
			
			resTask = stTask.executeQuery("SELECT * FROM  Tasks where WorkpackageIDFS=" + wID + "");
			while (resTask.next()){
				tID = resTask.getInt("TaskID");
				tWorkpackageID = resTask.getInt("WorkpackageIDFS");
				tName = resTask.getString("TaskName");
				tStart = resTask.getString("TaskStart");
				tEnd = resTask.getString("TaskEnd");
				tPMs = resTask.getInt("PMs");
				tBudget = resTask.getFloat("Budget");
				
				Task task = new Task(tID, tWorkpackageID, tName, tStart, tEnd, tPMs, tBudget);
				
				resEmployee = stEmployee.executeQuery("SELECT Employees.* " + 
						"FROM Employees INNER JOIN Assignments ON Employees.EmployeeID = Assignments.EmployeeIDFS " + 
						"WHERE Assignments.TaskIDFS =" + tID + "");
				
				while(resEmployee.next()){

					eID = resEmployee.getInt("EmployeeID");
					eFirstname = resEmployee.getString("Firstname");
					eLastname = resEmployee.getString("Lastname");
					eKuerzel = resEmployee.getString("Kuerzel");
					eMail = resEmployee.getString("Mail");
					eSupervisor = resEmployee.getInt("Supervisor");
					
					resWage = stWage.executeQuery("SELECT WagePerHour FROM  Wage where EmployeeIDFS=" + eID + " order by ValidFrom desc");
					resWage.next();
					eWage = resWage.getFloat("WagePerHour");
					
					Employee employee = new Employee(eID, eFirstname, eLastname, eKuerzel, eMail, eWage, eSupervisor);
					
					task.addEmployee(employee);
				}
				
				wp.addTask(task);
			}
			
			project.addWorkpackage(wp);
		}
		
		return project;
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
	 *            id of the current workpackage
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
	 * returns all employees, working at the current project
	 * 
	 * @param id
	 *            id of the current task
	 * 
	 * @return  all employees, working at the current project
	 */
	public ResultSet getEmployees(int id) {
		try {
			res = st.executeQuery("SELECT `Employees`.`Firstname` , `Employees`.`Lastname` " + 
					"FROM `Employees` INNER JOIN `Assignments` ON `Employees`.`EmployeeID` = `Assignments`.`EmployeeIDFS` " + 
					"WHERE `Assignments`.`TaskIDFS` =" + id + "");
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
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
		
		char[] text = new char[12];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rndm.nextInt(characters.length()));
		}
		
		String password = new String(text);
		
		query = "INSERT INTO Employees (" + "Firstname, Lastname, Kuerzel, Password, Mail, Supervisor" + ") VALUES ('"
				+ firstname + "', '" + lastname + "', '" + kuerzel + "', '" + password + "', '" + mail + "', "
				+ employeeID + ");";
		
		st.executeUpdate(query);
	}
}
