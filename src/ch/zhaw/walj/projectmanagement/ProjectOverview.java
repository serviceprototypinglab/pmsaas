package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.walj.chart.LineChart;
import ch.zhaw.walj.chart.PieChart;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Overview/Project")
public class ProjectOverview extends HttpServlet {
	
	private String		url			= "jdbc:mysql://localhost:3306/";
	private String		dbName		= "projectmanagement";
	private String		userName	= "Janine";
	private String		password	= "test123";

	private Project project;
	private ArrayList<Expense> expenses = new ArrayList<Expense>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private Effort effort;
	private PieChart pieChart;
	private LineChart lineChart;
	
	private DateHelper dateFormatter = new DateHelper();
	
	private int	i = 0;
	
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
		try {
			pieChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
			lineChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		
		// write HTML (head, header, nav)
		out.println("<!DOCTYPE html>\n" 
						+ "\t<html>\n" 
							+ "\t\t<head>\n" 
								+ "\t\t\t<meta charset=\"UTF-8\">\n"
								+ "\t\t\t<title>Projects</title>\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />\n"
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />\n" 
							+ "\t\t</head>\n" 
							+ "\t\t<body>\n"
								+ "\t\t\t<div id=\"wrapper\">\n" 
									+ "\t\t\t\t<header>\n" 
										+ "\t\t\t\t\t<div class=\"row\">\n" 
											+ "\t\t\t\t\t\t<div class=\"small-8 medium-6 columns\"><h1>" + project.getName() + "</h1></div>\n"
											+ "\t\t\t\t\t\t<div class=\"small-12 medium-6 columns\">\n" 
												+ "\t\t\t\t\t\t\t<div class=\"float-right\">\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"../newProject.shtml\" class=\"button\">New Project</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"../newEmployee.shtml\" class=\"button\">New Employee</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"help.shtml\" class=\"button\">Help</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"logout.shtml\" class=\"button\">Logout</a>\n" 
												+ "\t\t\t\t\t\t\t</div>\n" 
											+ "\t\t\t\t\t\t</div>\n"
										+ "\t\t\t\t\t</div>\n" 
									+ "\t\t\t\t</header>\n" 
								+ "\t\t\t<section>\n"
								+ "\t\t\t<div class=\"row\">\n"
								+ "\t\t\t<div class=\"panel small-12 columns\">\n");

		out.println("<div class=\"row round\"><div class=\"small-4 medium-2 columns bold\">Projectduration</div>" 
						+ "<div class=\"small-8 medium-4 columns\">" + project.getDuration() + "</div>"
						+ "<div class=\"small-4 medium-2 columns bold\">Total Budget</div>"
						+ "<div class=\"small-8 medium-4 columns\">" + project.getBudgetFormatted() + "</div>");

		out.println("<div class=\"small-4 medium-2 columns bold\">Workpackages</div>" 
						+ "<div class=\"small-8 medium-4 columns\">" + project.nbrOfWorkpackages() + "</div>"
						+ "<div class=\"small-4 medium-2 columns bold\">Tasks</div>"
						+ "<div class=\"small-8 medium-4 columns\">" + project.nbrOfTasks() + "</div>");

		out.println("<div class=\"small-4 medium-2 columns bold\">Employees</div>" 
						+ "<div class=\"small-8 medium-4 columns\">" + project.nbrOfEmployees() + "</div>"
						+ "<div class=\"small-4 medium-2 columns bold\">Partner</div>"
						+ "<div class=\"small-8 medium-4 columns\">" + project.getPartners() + "</div></div>");
		
		try {
			out.println("</div><div class=\"panel small-12 medium-6 columns\">"
							+ "<div class=\"row round\"><div class=\"small-12 columns\"><h2>Budget</h2></div>"
							+ "<div class=\"small-12 medium-8 columns\"><img src=\"../Charts/BudgetProject" + project.getID() + ".jpg\"></div>" 
							+ "<div class=\"small-12 medium-4 columns verticalAlignMiddle\"></br><span class=\"legend-1  smallbadge\"></span> Remaining: </br>" + project.getCurrency() + " " + pieChart.getRemainingBudget()
							+ "0</br><span class=\"legend-2 smallbadge\"></span> Spent: </br>" + project.getCurrency() + " " + pieChart.getUsedBudget() + "0</div>"
							+ "<div class=\"small-8 columns\"><h3>Expenses</h3></div>"
							+ "<div class=\"small-4 columns\"><a class=\"button\"><i class=\"fa fa-plus\"></i> add expenses</a></div>"
							+ "<div class=\"small-4 columns\"><span class=\"bold\">Employee</span></div>"
							+ "<div class=\"small-2 columns\"><span class=\"bold\">Type</span></div>"
							+ "<div class=\"small-3 columns\"><span class=\"bold\">Date</span></div>"
							+ "<div class=\"small-3 columns\"><span class=\"bold\">Costs</span></div>");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for(Expense ex : expenses){
			Employee employee = project.getSpecificEmployee(ex.getEmployeeID());
			out.println("<div class=\"small-4 columns\">" + employee.getName() + "</div>"
							+ "<div class=\"small-2 columns\" title=\"" + ex.getDescription() + "\">" + ex.getType() + "</div>"
							+ "<div class=\"small-3 columns\">" + dateFormatter.getFormattedDate(ex.getDate()) + "</div>"
							+ "<div class=\"small-3 columns\">" + project.getCurrency() + " " + ex.getCosts() + "</div>");
		}
		out.println("<span class=\"bold\"><div class=\"small-3 small-offset-6 columns\"></br>Total</div>"
						+ "<div class=\"small-3 columns\"></br>" + project.getCurrency() + " " 
						+ project.getTotalExpenses() + "</div></span>");
		out.println("</div></div><div class=\"panel small-12 medium-6 columns\"><div class=\"row round\">"
						+ "<div class=\"small-6 columns\"><h2>Effort</h2></div><div class=\"small-6 columns align-right padding-top-10\">"
						+ "<a class=\"button\">Add Employee</a> <a class=\"button\">Book Hours</a></div>");
		out.println("<div class=\"small-12 no-padding columns\"><img src=\"../Charts/EffortProject" + project.getID() + ".jpg\"></div>");
		
		out.println("<div class=\"small-6 columns\"><span class=\"bold\">Employee</span></div>"
						+ "<div class=\"small-4 end columns\"><span class=\"bold\">Total Effort</span></div>");
		

		float totalEffort = 0;
		for(Employee employee : employees){
			try {
				float effortEmployee = effort.getEffortPerEmployee(employee.getID());
				totalEffort += effortEmployee;
				out.println("<div class=\"small-6 columns\">" + employee.getName() + "</div>");
				out.println("<div class=\"small-4 columns\">" + effortEmployee + "</div>");
				out.println("<div class=\"small-2 columns\"><a class=\"button\">Details</a></div>");
				
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		out.println("<div class=\"small-6 columns\"><span class=\"bold\">Total</span></div>");
		out.println("<div class=\"small-4 columns\"><span class=\"bold\">" + totalEffort + "</span></div>");
		out.println("<div class=\"small-2 columns\"><a class=\"button\">Details</a></div>");
		
		out.println(
				"</div></div></section></div><script src=\"../js/vendor/jquery.js\"></script><script src=\"../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
	}
	
}
