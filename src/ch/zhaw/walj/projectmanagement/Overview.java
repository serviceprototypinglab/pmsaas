package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.walj.projectmanagement.chart.PieChart;

/**
 * Servlet implementation class Overview
 */

// TODO übersicht richtige werte (anstatt XX days until...)
@SuppressWarnings("serial")
@WebServlet("/Overview")
public class Overview extends HttpServlet {
	
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "projectmanagement";
	private String userName	= "Janine";
	private String password	= "test123";

	private ResultSet resProjects;

	private DateHelper dateHelper = new DateHelper();
	private PieChart piechart;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		DBConnection con = new DBConnection(url, dbName, userName, password);
		
		// TODO employee id
		// get all projects where the current user is supervisor
		resProjects = con.getProjects(1);
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>" 
				  + "<html>"
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">"
				  + "<title>Projects</title>" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome/css/font-awesome.min.css\" />" 
				  // function to redirect after click on select button
				  + "<script>function Redirect(ID) {var url = \"Overview/Project?id=\" + ID; window.location=url;}</script>"
				  + "</head>" 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-8 medium-6 columns\">"
				  // title
				  + "<h1>Projects</h1>"
				  + "</div>"
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a> "
				  + "<a href=\"newProject\" class=\"button\">New Project</a> " 
				  + "<a href=\"newEmployee\" class=\"button\">New Employee</a> "
				  + "<a href=\"help\" class=\"button\">Help</a> " 
				  + "<a href=\"logout\" class=\"button\">Logout</a> " 
				  + "</div>" 
				  + "</div>"
				  + "</div>" 
				  + "</header>" 
				  // HTML section with list of all projects
				  + "<section>" 
				  + "<div class=\"row\">" 
				  + "<ul class=\"accordion\" data-accordion>");
		
		try {
			// write project information
			while (resProjects.next()) {
				
				// new project with data from the database
				Project project = con.getProject(resProjects.getInt("ProjectIDFS"));
				// get all employees
				ArrayList <Employee> employee = project.getEmployees();
				
				String employees = "";
				
				// string of all employee names
				if (!employee.isEmpty()){
					employees = employee.get(0).getName();
					for (int i = 1; i < employee.size(); i++){
						employees += ", " + employee.get(i).getName();
					}
				} else {
					employees = "no employees assigned";
				}
				
				// get number of days left to end of project
				Date date = new Date();
				String daysUntilEnd = "";
				int days = dateHelper.getDaysBetween(date, project.getEnd());
				if (days == 0){
					daysUntilEnd = "Project finished";
				} else {
					daysUntilEnd = "Project ends in " + days + " days";
				}
		
				piechart = new PieChart(project);
				
				
				// print list item
				out.println("<li class=\"accordion-item\" data-accordion-item><a href=\"#\" class=\"accordion-title\">"
						  + "<span class=\"bigtext small-3 columns down\">" + project.getShortname() + "</span>"
						  + "<span class=\"middletext small-6 columns down\">" + project.getName() + "</span>"
						  + "<button class=\"button small-2 columns down smalltext\" onclick=\"Redirect(" + project.getID() + ");\">Select</button>"
						  + "<span class=\"success badge errorbadge\">0</span>"
						  + "</a>"
						  + "<div class=\"accordion-content\" data-tab-content>"
						  // Write Duration
						  +"<p>"
						  + "<span class=\"small-3 columns\">Projectduration</span>"
						  + "<span class=\"small-4 columns\">" 
						  + dateHelper.getFormattedDate(project.getStart()) + " - " + dateHelper.getFormattedDate(project.getEnd()) + "</span>"
				  		  + "<span class=\"small-4 end columns align-right\">" + daysUntilEnd + "</span>"
		  		  		  + "</p>"
		  		  		  // Write Budget
		  		  		  + "<p>"
		  		  		  + "<span class=\"small-3 columns\">Budget</span>"
		  		  		  + "<span class=\"small-4 columns\">" + project.getBudgetFormatted() + "</span>"
		  		  		  + "<span class=\"small-4 end columns align-right\">" + project.getCurrency()
		  		  		  + " " + piechart.getRemainingBudgetAsString() + " left</span>"
		  		  		  + "</p>"
						  // Write Workpackages
						  + "<p>"
						  + "<span class=\"small-3 columns\">Workpackages</span>"
						  + "<span class=\"small-4 columns\">" + project.nbrOfWorkpackages() + "</span>"
						  + "<span class=\"small-4 end columns align-right\">"
						  + "-"
						  + "</span>"
						  + "</p>"
						  // Write Tasks
						  + "<p>"
						  + "<span class=\"small-3 columns\">Tasks</span>"
						  + "<span class=\"small-4 columns\">" + project.nbrOfTasks() + "</span>"
						  + "<span class=\"small-4 end columns align-right\">" 
						  + "-"
						  + "</span>"
						  + "</p>"
						  // Write Employees
						  + "<p>"
						  + "<span class=\"small-3 columns\">Employees</span>"
						  + "<span class=\"small-4 columns\">" + project.nbrOfEmployees() 
						  + "</span><span class=\"small-4 end columns align-right\">" + employees
						  + "</span></p>"
						  // Write Partners
						  + "<p><span class=\"small-3 columns\">Partner</span>"
						  + "<span class=\"small-4 end columns\">" + project.getPartners() + "</span>"
						  + "</p>"
						  + "</div>"
						  + "</li>");
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		out.println("</ul>"
				  + "</div>"
				  + "</section>"
				  + "</div>"
				  + "<script src=\"js/vendor/jquery.js\"></script>"
				  + "<script src=\"js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
