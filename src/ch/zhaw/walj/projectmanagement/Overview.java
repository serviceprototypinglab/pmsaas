package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.walj.chart.PieChart;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Overview")
public class Overview extends HttpServlet {
	
	private String		url			= "jdbc:mysql://localhost:3306/";
	private String		dbName		= "projectmanagement";
	private String		userName	= "Janine";
	private String		password	= "test123";

	private ResultSet	resProjects;
	private ResultSet	resWorkpackages;
	private ResultSet	resTasks;
	private ResultSet	resEmployees;
	

	private DateFormatter dateFormatter = new DateFormatter();
	
	private int	i = 0;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		DBConnection con = new DBConnection(url, dbName, userName, password);
		DBConnection con2 = new DBConnection(url, dbName, userName, password);
		DBConnection con3 = new DBConnection(url, dbName, userName, password);
		DBConnection con4 = new DBConnection(url, dbName, userName, password);
		
		
		resProjects = con.getProjects(1);
		
		
		PrintWriter out = response.getWriter();
		
		// write HTML (head, header, nav)
		out.println("<!DOCTYPE html>\n" 
						+ "\t<html>\n" 
							+ "\t\t<head>\n" 
								+ "\t\t\t<meta charset=\"UTF-8\">\n"
								+ "\t\t\t<title>Projects</title>\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />\n"
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome/css/font-awesome.min.css\" />\n" 
								+ "\t\t\t<script>function Redirect(ID) {var url = \"Overview/Project?id=\" + ID; window.location=url;}</script>\n"
							+ "\t\t</head>\n" 
							+ "\t\t<body>\n"
								+ "\t\t\t<div id=\"wrapper\">\n" 
									+ "\t\t\t\t<header>\n" 
										+ "\t\t\t\t\t<div class=\"row\">\n" 
											+ "\t\t\t\t\t\t<div class=\"small-8 medium-6 columns\"><h1>Projects</h1></div>\n"
											+ "\t\t\t\t\t\t<div class=\"small-12 medium-6 columns\">\n" 
												+ "\t\t\t\t\t\t\t<div class=\"float-right\">\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"newProject.shtml\" class=\"button\">New Project</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"newEmployee.shtml\" class=\"button\">New Employee</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"help.shtml\" class=\"button\">Help</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"logout.shtml\" class=\"button\">Logout</a>\n" 
												+ "\t\t\t\t\t\t\t</div>\n" 
											+ "\t\t\t\t\t\t</div>\n"
										+ "\t\t\t\t\t</div>\n" 
									+ "\t\t\t\t</header>\n" 
								+ "\t\t\t<section>\n" 
								+ "\t\t\t<div class=\"row\">\n" 
									+ "\t\t\t\t<ul class=\"accordion\" data-accordion>");
		
		try {
			// write Projectinformation
			while (resProjects.next()) {
				
				ArrayList<String> employees = new ArrayList<String>();
				String allEmployees = null;

				int projectID = resProjects.getInt("ProjectIDFS");
				String projectShortname = resProjects.getString("ProjectShortname");
				String projectName = resProjects.getString("ProjectName");
				String projectStart = resProjects.getString("ProjectStart");
				String projectEnd = resProjects.getString("ProjectEnd");
				String projectCurrency = resProjects.getString("Currency");
				float totalBudget = resProjects.getFloat("TotalBudget");
				String partner = resProjects.getString("Partner");
				
				resWorkpackages = con2.getWorkpackages(resProjects.getInt("ProjectIDFS"));
				int nbrOfWP = 0;
				int nbrOfTasks = 0;
				for (nbrOfWP = 0; resWorkpackages.next(); nbrOfWP++){
					resTasks = con3.getTasks(resWorkpackages.getInt("WorkpackageID"));
					for (nbrOfTasks = 0; resTasks.next(); nbrOfTasks++){			
						
						
						resEmployees = con4.getEmployees(resTasks.getInt("TaskID"));
						while (resEmployees.next()){
							String firstname = resEmployees.getString("Firstname");
							String lastname = resEmployees.getString("Lastname");
							String employee =  firstname + " " + lastname; 
							if (!employees.contains(employee)){
								employees.add(employee);
							}
							allEmployees = employees.get(0);
							for (i = 1; i < employees.size(); i++){
								allEmployees += ", " + employees.get(i);
							}
						}
					};
				};

				PieChart pieChart = new PieChart(con.getProject(projectID));
				try {
					pieChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				
				out.println("<li class=\"accordion-item\" data-accordion-item><a href=\"#\" class=\"accordion-title\">");
				out.println("<span class=\"bigtext small-3 columns down\">" + projectShortname + "</span>");
				out.println("<span class=\"middletext small-6 columns down\">" + projectName + "</span>");
				out.println("<button class=\"button small-2 columns down smalltext\" onclick=\"Redirect(" + projectID + ");\">Select</button>");
				out.println("<span class=\"success badge errorbadge\">0</span>");
				out.println("</a>");
				out.println("<div class=\"accordion-content\" data-tab-content>");
				// Write Duration
				out.println(
						"<p><span class=\"small-3 columns\">Projectduration</span><span class=\"small-4 columns\">" + dateFormatter.getFormattedString(projectStart)
								+ " - " + dateFormatter.getFormattedString(projectEnd) + "</span><span class=\"small-4 end columns align-right\">Project ends in X Days</span></p>");
				
				// Write Budget
				out.println("<p><span class=\"small-3 columns\">Budget</span><span class=\"small-4 columns\">" + projectCurrency + " "
						+ totalBudget + "</span><span class=\"small-4 end columns align-right\">" + projectCurrency
						+ " XXXXX left</span></p>");

				// Write Workpackages
				out.println("<p><span class=\"small-3 columns\">Workpackages</span><span class=\"small-4 columns\">" + nbrOfWP 
						+ "</span><span class=\"small-4 end columns align-right\">"
						+ "Workpackage 1 ends in 5 days</span></p>");

				// Write Tasks
				out.println("<p><span class=\"small-3 columns\">Tasks</span><span class=\"small-4 columns\">" + nbrOfTasks 
						+ "</span><span class=\"small-4 end columns align-right\">" 
						+ "Task 2 ends in 5 days</span></p>");
				
				// Write Employees
				out.println("<p><span class=\"small-3 columns\">Employees</span><span class=\"small-4 columns\">" + employees.size() 
						+ "</span><span class=\"small-4 end columns align-right\">" + allEmployees
						+ "</span></p>");
				
				// Write Partners
				out.println("<p><span class=\"small-3 columns\">Partner</span><span class=\"small-4 end columns\">" + partner + 
						"</span></p>");
				
				out.println("</div>");
				out.println("</li>");
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		out.println(
				"</ul></div></section></div><script src=\"js/vendor/jquery.js\"></script><script src=\"js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
	}
	
}
