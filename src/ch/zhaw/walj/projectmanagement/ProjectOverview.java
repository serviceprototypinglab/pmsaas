package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.walj.projectmanagement.chart.GanttChart;
import ch.zhaw.walj.projectmanagement.chart.LineChart;
import ch.zhaw.walj.projectmanagement.chart.PieChart;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Overview/Project")
public class ProjectOverview extends HttpServlet {
	
	// database access information
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "projectmanagement";
	private String userName	= "Janine";
	private String password	= "test123";

	// variable declaration
	private Project project;
	private ArrayList<Expense> expenses = new ArrayList<Expense>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private Effort effort;
	private PieChart pieChart;
	private LineChart lineChart;
	private GanttChart ganttChart;
	
	private DateHelper dateFormatter = new DateHelper();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		int projectID = Integer.parseInt(request.getParameter("id"));
		
		DBConnection con = new DBConnection(url, dbName, userName, password);
		
		try {
			project = con.getProject(projectID);
			expenses = project.getExpenses();
			employees = project.getEmployees();
			effort = new Effort(project.getTasks());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		
		pieChart = new PieChart(project);
		lineChart = new LineChart(project);
		ganttChart = new GanttChart(project);
		try {
			pieChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
			lineChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
			ganttChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			out.println("<!DOCTYPE html>" 
					  + "<html>" 
					  // HTML head
					  + "<head>" 
					  + "<meta charset=\"UTF-8\">"
					  + "<title>Projects</title>" 
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />" 
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />" 
					  + "</head>" 
					  + "<body>"
					  + "<div id=\"wrapper\">" 
					  + "<header>" 
					  + "<div class=\"row\">" 
					  + "<div class=\"small-8 medium-6 columns\">"
					  // title
					  + "<h1>" + project.getName() + "</h1>"
					  + "</div>"
					  // menu
					  + "<div class=\"small-12 medium-6 columns\">" 
					  + "<div class=\"float-right menu\">"
					  + "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a> "
					  + "<a href=\"../newProject\" class=\"button\">New Project</a> " 
					  + "<a href=\"../newEmployee\" class=\"button\">New Employee</a> "
					  + "<a href=\"help\" class=\"button\">Help</a> " 
					  + "<a href=\"logout\" class=\"button\">Logout</a> " 
					  + "</div>" 
					  + "</div>"
					  + "</div>" 
					  + "</header>" 
					  // HTML section with panels
					  + "<section>"
					  + "<div class=\"row\">"
					  // panel for project overview
					  + "<div class=\"panel small-12 columns\">"
					  // project duration
					  + "<div class=\"row round\">"
					  + "<div class=\"small-4 medium-2 columns bold\">Projectduration</div>" 
					  + "<div class=\"small-8 medium-4 columns\">" + project.getDuration() + "</div>"
					  // total budget
					  + "<div class=\"small-4 medium-2 columns bold\">Total Budget</div>"
					  + "<div class=\"small-8 medium-4 columns\">" + project.getBudgetFormatted() + "</div>"
					  // number of workpackages
					  + "<div class=\"small-4 medium-2 columns bold\">Workpackages</div>" 
					  + "<div class=\"small-8 medium-4 columns\">" + project.nbrOfWorkpackages() + "</div>"
					  // number of tasks
				  	  + "<div class=\"small-4 medium-2 columns bold\">Tasks</div>"
					  + "<div class=\"small-8 medium-4 columns\">" + project.nbrOfTasks() + "</div>"
					  // number of employees
					  + "<div class=\"small-4 medium-2 columns bold\">Employees</div>" 
					  + "<div class=\"small-8 medium-4 columns\">" + project.nbrOfEmployees() + "</div>"
					  // number of partners
					  + "<div class=\"small-4 medium-2 columns bold\">Partner</div>"
					  + "<div class=\"small-8 medium-4 columns\">" + project.getPartners() + "</div>"
					  + "</div>"
					  + "</div>"
					  // budget panel
					  + "<div class=\"panel small-12 medium-6 columns\">"
					  + "<div class=\"row round\">"
					  + "<div class=\"small-12 columns\">"
					  + "<h2>Budget</h2>"
					  + "</div>"
					  + "<div class=\"small-12 medium-8 columns\">"
					  // load pie chart
					  + "<img src=\"../Charts/BudgetProject" + project.getID() + ".jpg\">"
					  + "</div>"
					  // pie chart legend
					  + "<div class=\"small-12 medium-4 columns\">"
					  + "</br>"
					  + "<span class=\"legend-1  smallbadge\"></span> "
					  // remaining budget
					  + "Remaining: </br>" + project.getCurrency() + " " + pieChart.getRemainingBudgetAsString()
					  + "</br>"
					  + "<span class=\"legend-2 smallbadge\"></span> "
					  // used budget
					  + "Spent: </br>" + project.getCurrency() + " " + pieChart.getUsedBudgetAsString() 
					  + "</div>"
					  + "<div class=\"small-8 columns\">"
					  // Expenses
					  + "<h3>Expenses</h3>"
					  + "</div>"
					  + "<div class=\"small-4 columns\">"
					  // add expense button
					  + "<a class=\"button\" href=\"addExpense?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Add Expenses</a>"
					  + "</div>"
					  + "<div class=\"small-4 columns\">"
					  + "<span class=\"bold\">Employee</span>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  + "<span class=\"bold\">Type</span>"
					  + "</div>"
					  + "<div class=\"small-3 columns\">"
					  + "<span class=\"bold\">Date</span>"
					  + "</div>"
					  + "<div class=\"small-3 columns\">"
					  + "<span class=\"bold\">Costs</span>"
					  + "</div>");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		// print expenses
		for(Expense ex : expenses){
			Employee employee = project.getSpecificEmployee(ex.getEmployeeID());
			try {
				out.println("<div class=\"small-4 columns\">" + employee.getName() + "</div>"
						  + "<div class=\"small-2 columns\" title=\"" + ex.getDescription() + "\">" + ex.getType() + "</div>"
						  + "<div class=\"small-3 columns\">" + dateFormatter.getFormattedDate(ex.getDate()) + "</div>"
						  + "<div class=\"small-3 columns\">" + project.getCurrency() + " " + ex.getCostsAsString() + "</div>");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		out.println("<span class=\"bold\">"
				  + "<div class=\"small-3 small-offset-6 columns\">"
				  // Total expenses
				  + "</br>Total</div>"
				  + "<div class=\"small-3 columns\">"
				  + "</br>" + project.getCurrency() + " " + project.getTotalExpensesAsString() 
				  + "</div>"
				  + "</span>"
				  + "</div>"
				  + "</div>"
				  // effort panel
				  + "<div class=\"panel small-12 medium-6 columns\">"
				  + "<div class=\"row round\">"
				  + "<div class=\"small-4 columns\">"
				  + "<h2>Effort</h2>"
				  + "</div>"
				  + "<div class=\"small-8 columns align-right padding-top-10\">"
				  // button to assign employees
				  + "<a class=\"button\" href=\"assignEmployee?projectID=" + project.getID() + "\">"
				  + "<i class=\"fa fa-plus\"></i> Assign Employees</a> "
				  // button to book hours
				  + "<a class=\"button\" href=\"bookHours?projectID=" + project.getID() + "\">"
				  + "<i class=\"fa fa-clock-o\"></i> Book Hours</a></div>"
				  + "<div class=\"small-12 no-padding columns\">"
				  // load line chart
				  + "<img src=\"../Charts/EffortProject" + project.getID() + ".jpg\">"
				  + "</div>"
				  // Effort per employee
				  + "<div class=\"small-6 columns\">"
				  + "<span class=\"bold\">Employee</span>"
				  + "</div>"
				  + "<div class=\"small-4 end columns\">"
				  + "<span class=\"bold\">Total Effort</span>"
				  + "</div>");

		// effort for every employee and total effort
		double totalEffort = 0;		
		for(Employee employee : employees){
			try {
				double effortEmployee = effort.getEffortPerEmployee(employee.getID());
				totalEffort += effortEmployee;
				out.println("<div class=\"small-6 columns\">" + employee.getName() + "</div>"
						   +"<div class=\"small-3 columns\">" + effortEmployee + "h</div>"
						   + "<div class=\"small-3 columns\">"
						   + "<a class=\"button float-right\">"
						   + "<i class=\"fa fa-info\"></i> Details</a>"
						   + "</div>");
				
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		out.println("<div class=\"small-6 columns\">"
				  + "<span class=\"bold\">Total</span>"
				  + "</div>"
				  + "<div class=\"small-3 columns\">"
				  + "<span class=\"bold\">" + totalEffort + "h</span>"
				  + "</div>"
				  +"<div class=\"small-3 columns\">"
				  + "<a class=\"button float-right\">"
				  + "<i class=\"fa fa-info\"></i> Details</a>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  // Panel for workpackages and tasks (Gantt chart)
				  + "<div class=\"panel small-12 columns\">"
				  + "<div class=\"row round\">"
				  + "<div class=\"small-7 columns\">"
				  + "<h2>Workpackages & Tasks</h2>"
				  + "</div>"
				  + "<div class=\"small-12 no-padding columns\">"
				  // load gantt chart
				  + "<img src=\"../Charts/GanttProject" + project.getID() + ".jpg\">"
				  + "</div>"
				  + "<div class=\"small-12 columns align-right\">"
				  + "<a class=\"button \">"
				  + "<i class=\"fa fa-plus\"></i> Add Workpackage</a> "
				  + "<a class=\"button\"> "
				  + "<i class=\"fa fa-plus\"></i> Add Task</a>"				  
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</section>"
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
