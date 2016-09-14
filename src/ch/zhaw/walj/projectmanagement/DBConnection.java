package ch.zhaw.walj.projectmanagement;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * creates a connection to the database
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class DBConnection {

	private String driver = "com.mysql.jdbc.Driver";
	private String url = "";
	private String dbName = "";
	private String userName = "";
	private String password = "";
	private Connection conn;
	private Statement st;
	private ResultSet res;
	private String query;
	private int idToUse;

	private String characters = "QWERTZUIOPASDFGHJKLYXCVBNMmnbvcxylkjhgfdsapoiuztrewq1234567890";
	private int pwLength = 8;

	private DateHelper dateFormatter = new DateHelper();

	/**
	 * Constructor to the DBConnection class
	 * creates a connection to a database
	 * 
	 * @param url
	 *            URL to the database
	 * @param dbName
	 *            name of the database
	 * @param userName
	 * 			  username for login
	 * @param password
	 * 			  password for login
	 */
	public DBConnection(String url, String dbName, String userName, String password) {
		this.url = url;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;

		// get connection to database
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
	 * @param pID
	 *            id of the project
	 * 
	 * @return project with data from the database
	 * @throws SQLException
	 */
	public Project getProject(int pID) throws SQLException {

		// variable declaration
		Statement stProject = conn.createStatement();
		Statement stWP = conn.createStatement();
		Statement stTask = conn.createStatement();
		Statement stEmployee = conn.createStatement();
		Statement stWage = conn.createStatement();
		Statement stExpenses = conn.createStatement();

		ResultSet resProject;
		ResultSet resWP;
		ResultSet resTask;
		ResultSet resEmployee;
		ResultSet resWage;
		ResultSet resExpenses;

		// variables for data from project table
		Project project;
		String pName;
		String pShortname;
		String pStart;
		String pEnd;
		String pCurrency;
		double pBudget;
		String pPartner;

		// variables for data from workpackage table
		int wID;
		int wProjectID;
		String wName;
		String wStart;
		String wEnd;

		// variables for data from task table
		int tID;
		int tWorkpackageID;
		String tName;
		String tStart;
		String tEnd;
		int tPMs;
		double tBudget;

		// variables for data from empoloyee table
		int eID;
		String eFirstname;
		String eLastname;
		String eKuerzel;
		String eMail;
		double eWage;
		int eSupervisor;

		// variables for data from expense table
		Expense expense;
		int exID;
		int exProjextID;
		int exEmployeeID;
		double exCosts;
		String exType;
		String exDescription;
		String exDate;

		// get the project information from the database
		resProject = stProject.executeQuery("SELECT * FROM  Projects where ProjectIDFS=" + pID + "");

		resProject.next();
		pName = resProject.getString("ProjectName");
		pShortname = resProject.getString("ProjectShortname");
		pStart = dateFormatter.getFormattedDate(resProject.getString("ProjectStart"));
		pEnd = dateFormatter.getFormattedDate(resProject.getString("ProjectEnd"));
		pCurrency = resProject.getString("Currency");
		pBudget = resProject.getDouble("TotalBudget");
		pPartner = resProject.getString("Partner");

		project = new Project(pID, pName, pShortname, pStart, pEnd, pCurrency, pBudget, pPartner);

		// get all expenses
		resExpenses = stExpenses.executeQuery("Select * from Expenses where ProjectIDFS=" + pID + "");

		while (resExpenses.next()) {

			exID = resExpenses.getInt("ExpenseID");
			exProjextID = resExpenses.getInt("ProjectIDFS");
			exEmployeeID = resExpenses.getInt("EmployeeIDFS");
			exCosts = resExpenses.getDouble("Costs");
			exType = resExpenses.getString("Type");
			exDescription = resExpenses.getString("Description");
			exDate = resExpenses.getString("Date");

			expense = new Expense(exID, exProjextID, exEmployeeID, exCosts, exType, exDescription, exDate);
			project.addExpense(expense);
		}

		// get all workpackages
		resWP = stWP.executeQuery("SELECT * FROM  Workpackages where ProjectIDFS=" + pID + "");
		while (resWP.next()) {
			wID = resWP.getInt("WorkpackageID");
			wProjectID = resWP.getInt("ProjectIDFS");
			wName = resWP.getString("WPName");
			wStart = dateFormatter.getFormattedDate(resWP.getString("WPStart"));
			wEnd = dateFormatter.getFormattedDate(resWP.getString("WPEnd"));

			Workpackage wp = new Workpackage(wID, wProjectID, wName, wStart, wEnd);

			// get all tasks
			resTask = stTask.executeQuery("SELECT * FROM  Tasks where WorkpackageIDFS=" + wID + "");
			while (resTask.next()) {
				tID = resTask.getInt("TaskID");
				tWorkpackageID = resTask.getInt("WorkpackageIDFS");
				tName = resTask.getString("TaskName");
				tStart = dateFormatter.getFormattedDate(resTask.getString("TaskStart"));
				tEnd = dateFormatter.getFormattedDate(resTask.getString("TaskEnd"));
				tPMs = resTask.getInt("PMs");
				tBudget = resTask.getDouble("Budget");

				ProjectTask task = new ProjectTask(tID, tWorkpackageID, tName, tStart, pStart, tEnd, tPMs, tBudget);

				// get all employees that are assigned to at least one of the tasks
				resEmployee = stEmployee.executeQuery("SELECT Employees.* "
						+ "FROM Employees INNER JOIN Assignments ON Employees.EmployeeID = Assignments.EmployeeIDFS "
						+ "WHERE Assignments.TaskIDFS =" + tID + "");

				while (resEmployee.next()) {

					eID = resEmployee.getInt("EmployeeID");
					eFirstname = resEmployee.getString("Firstname");
					eLastname = resEmployee.getString("Lastname");
					eKuerzel = resEmployee.getString("Kuerzel");
					eMail = resEmployee.getString("Mail");
					eSupervisor = resEmployee.getInt("Supervisor");

					// get the wage of the employee
					resWage = stWage.executeQuery(
							"SELECT WagePerHour FROM  Wage where EmployeeIDFS=" + eID + " order by ValidFrom desc");
					resWage.next();
					eWage = resWage.getDouble("WagePerHour");

					Employee employee = new Employee(eID, eFirstname, eLastname, eKuerzel, eMail, eWage, eSupervisor);
					
					// add employee to the task
					task.addEmployee(employee);
				}

				// add the task and his employees to the workpackage
				wp.addTask(task);
				wp.addEmployees();
			}

			// add the workpackages with his tasks and the employees to the project
			project.addWorkpackage(wp);
			project.addEmployees();
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
	 * @return all employees, working at the current project
	 */
	public ResultSet getEmployees(int id) {
		try {
			res = st.executeQuery("SELECT `Employees`.`Firstname` , `Employees`.`Lastname` "
					+ "FROM `Employees` INNER JOIN `Assignments` ON `Employees`.`EmployeeID` = `Assignments`.`EmployeeIDFS` "
					+ "WHERE `Assignments`.`TaskIDFS` =" + id + "");
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

		// generate random password
		SecureRandom random = new SecureRandom();
		StringBuilder pass = new StringBuilder(pwLength);
		for (int i = 0; i < pwLength; i++) {
			pass.append(characters.charAt(random.nextInt(characters.length())));
		}

		String password = pass.toString();

		// create new employee
		query = "INSERT INTO Employees (" + "Firstname, Lastname, Kuerzel, Password, Mail, Supervisor" + ") VALUES ('"
				+ firstname + "', '" + lastname + "', '" + kuerzel + "', '" + password + "', '" + mail + "', "
				+ employeeID + ");";

		st.executeUpdate(query);

		// get the ID of the employee
		res = st.executeQuery("Select EmployeeID from Employees order by EmployeeID desc");

		res.next();

		// create new date from the current time
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(cal.getTime());

		// create new wage
		query = "INSERT INTO Wage (EmployeeIDFS, WagePerHour, ValidFrom) VALUES (" + res.getInt("EmployeeID") + ", "
				+ wage + ", '" + date + "');";

		st.executeUpdate(query);
	}

	/**
	 * Get all the employees with the given supervisor and the supervisor
	 * itself.
	 * 
	 * @param supervisor
	 *            ID of the supervisor
	 * 
	 * @return ArrayList with all employees
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Employee> getAllEmployees(int supervisor) throws SQLException {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		Employee employee;

		try {

			// get the employees
			res = st.executeQuery(
					"SELECT * from Employees WHERE Supervisor =" + supervisor + " or EmployeeID = " + supervisor);

			while (res.next()) {

				// create new employees and add them to the ArrayList
				employee = new Employee(res.getInt("EmployeeID"), res.getString("Firstname"), res.getString("Lastname"),
						res.getString("Kuerzel"), res.getString("Mail"), 0, supervisor);
				employees.add(employee);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employees;
	}

	/**
	 * create a new expense in the database
	 * 
	 * @param projectID
	 *            ID of the project
	 * 
	 * @param employeeID
	 *            ID of the employee
	 * 
	 * @param costs
	 *            costs of the expense
	 * 
	 * @param type
	 *            type of the expense (Travel, etc.)
	 * 
	 * @param description
	 *            description
	 * 
	 * @param date
	 *            date of the expense
	 * 
	 * @throws SQLException
	 */
	public void newExpense(int projectID, int employeeID, double costs, String type, String description, String date)
			throws SQLException {

		// create new expense
		query = "INSERT INTO Expenses (" + "ProjectIDFS, EmployeeIDFS, Costs, Type, Description, Date" + ") VALUES ("
				+ projectID + ", " + employeeID + ", " + costs + ", '" + type + "', '" + description + "', '" + date
				+ "');";

		st.executeUpdate(query);
	}

	/**
	 * get all tasks the given employee is assigned to
	 * 
	 * @param employee
	 *            ID of the employee
	 * @return ArrayList with all task IDs
	 * @throws SQLException
	 */
	public ArrayList<Integer> getAssignments(int employee) throws SQLException {
		ArrayList<Integer> tasks = new ArrayList<Integer>();

		// get the task IDs
		res = st.executeQuery("SELECT TaskIDFS from Assignments WHERE EmployeeIDFS =" + employee);

		while (res.next()) {
			// add the IDs to the ArrayList
			tasks.add(res.getInt("TaskIDFS"));
		}

		return tasks;
	}

	/**
	 * get the ID of the assignment between the given employee and task
	 * 
	 * @param employee
	 *            ID of the employee
	 * @param task
	 *            ID of the task
	 * @return Assignment ID
	 * @throws SQLException
	 */
	public int getAssignment(int employee, int task) throws SQLException {

		// get the ID of the assignment
		res = st.executeQuery(
				"SELECT AssignmentID from Assignments WHERE EmployeeIDFS =" + employee + " and TaskIDFS = " + task);
		res.next();

		return res.getInt("AssignmentID");
	}

	/**
	 * create a new assignment for the given task and employee
	 * 
	 * @param taskID
	 *            ID of the task
	 * @param employeeID
	 *            ID of the employee
	 * @throws SQLException
	 */
	public void newAssignment(int taskID, int employeeID) throws SQLException {

		// create new assignment
		query = "INSERT INTO Assignments (TaskIDFS, EmployeeIDFS) VALUES (" + taskID + ", " + employeeID + ");";

		st.executeUpdate(query);
	}

	/**
	 * create a new booking in the database
	 * 
	 * @param assignment
	 *            ID of the assignment
	 * @param month
	 *            number of the month
	 * @param hours
	 *            amount of hours
	 * @throws SQLException
	 */
	public void newBooking(int assignment, int month, double hours) throws SQLException {

		// create new booking
		query = "INSERT INTO Bookings (AssignmentIDFS, Month, Hours) VALUES (" + assignment + ", " + month + ", "
				+ hours + ");";

		st.executeUpdate(query);
	}
}
