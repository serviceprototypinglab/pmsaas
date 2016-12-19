package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.NumberFormatter;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * project management tool, overview page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview")
public class Overview extends HttpServlet {
		
	/*
	 * method to handle get requests
	 * gets all own, shared and archived projects from the database 
	 * and prints a list of them
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// set response content type
		response.setContentType("text/html;charset=UTF8");
				
		// get user id and name from session
		int id = (int) request.getSession(false).getAttribute("ID");
		String name = (String) request.getSession(false).getAttribute("user");
				
		// connection to the database
		DBConnection con = new DBConnection();
		
		// get print writer
		PrintWriter out = response.getWriter();
		
		// prepare script for header
		String script = "<script>function Redirect(ID) {var url = \"Overview/Project?id=\" + ID; window.location=url;}</script>";
		
		// print html
		out.println(HTMLHeader.getInstance().printHeader("Projects", "../", "Projects", script, "")
				  // HTML section with list of all projects
				  + "<section>" 
				  + "<div class=\"row\">"
				  + "<h2 class=\"blue\">Welcome " + name + "</h2>"
				  + "</div>" 
				  + "<div class=\"row\">"
				  + "<h3>Your Projects</h3>"
				  + "</div>"
				  + "<div class=\"row\">"); 
		
		try {
			// get all projects where the current user is supervisor
			ArrayList<Project> projects = con.getProjects(id, false);
			if (projects != null){
				out.println("<ul class=\"accordion\" data-accordion data-multi-expand=\"true\" data-allow-all-closed=\"true\">");
				// write project information
				for (Project project : projects) {
					
					// get all employees
					ArrayList <Employee> employee = project.getEmployees();
					
					// make a string with all employee names
					String employees = "";
					if (!employee.isEmpty()){
						employees = employee.get(0).getName();
						for (int i = 1; i < employee.size(); i++){
							employees += "<br> " + employee.get(i).getName();
						}
					} else {
						employees = "no employees assigned";
					}
					
					// get number of days left until the end of the project
					Date date = new Date();
					String daysUntilEnd = "";
					int days = DateFormatter.getInstance().getDaysBetween(date, project.getEnd());
					if (days == 0){
						daysUntilEnd = "Project finished";
					} else {
						daysUntilEnd = "Project ends in " + days + " days";
					}
					
					// print list item
					out.println("<li class=\"accordion-item\" data-accordion-item><a href=\"#\" class=\"accordion-title\">"
							  + "<span class=\"bigtext small-3 columns down\">" + project.getShortname() + "</span>"
							  + "<span class=\"middletext small-7 columns down\">" + project.getName() + "</span>"
							  + "<button class=\"button small-2 columns end down smalltext\" onclick=\"Redirect(" + project.getID() + ");\">Select</button>"
							  + "</a>"
							  + "<div class=\"accordion-content\" data-tab-content>"
							  // Write Duration
							  +"<p>"
							  + "<span class=\"small-3 columns\">Projectduration</span>"
							  + "<span class=\"small-4 columns\">" + project.getDuration() + "</span>"
					  		  + "<span class=\"small-5 end columns align-right\">" + daysUntilEnd + "</span>"
			  		  		  + "</p>"
			  		  		  // Write Budget
			  		  		  + "<p>"
			  		  		  + "<span class=\"small-3 columns\">Budget</span>"
			  		  		  + "<span class=\"small-4 columns\">" + project.getCurrency() 
							  + " " + NumberFormatter.getInstance().formatDouble(project.getBudget()) + "</span>"
			  		  		  + "<span class=\"small-5 end columns align-right\">" + project.getCurrency()
			  		  		  + " " + NumberFormatter.getInstance().formatDouble(project.getRemainingBudget()) + " left</span>"
			  		  		  + "</p>"
							  // Write Workpackages
							  + "<p>"
							  + "<span class=\"small-3 columns\">Workpackages</span>"
							  + "<span class=\"small-9 columns\">" + project.nbrOfWorkpackages() + "</span>"
							  + "</p>"
							  // Write Tasks
							  + "<p>"
							  + "<span class=\"small-3 columns\">Tasks</span>"
							  + "<span class=\"small-9 columns\">" + project.nbrOfTasks() + "</span>"
							  + "</p>"
							  // Write Employees
							  + "<p>"
							  + "<span class=\"small-3 columns\">Employees</span>"
							  + "<span class=\"small-9 end columns\">" + employees 
							  + "</span>"
							  + "</p>"
							  // Write Partners
							  + "<p><span class=\"small-3 columns\">Partner</span>"
							  + "<span class=\"small-9 end columns\">" + project.getPartners() + "</span>"
							  + "</p>"
							  + "</div>"
							  + "</li>");
					
					
				}
				out.println("</ul>");
			} else {
				// print callout if there are no own projects yet
				out.println("<div class=\"callout secondary\">"
						  + "<h4>No projects found</h4>"
						  + "<a href=\"/Projektverwaltung/Projects/newProject\">Click here to create a new project</a>"
						  + "</div>");				
			}
		
			out.println("</div>"
					  + "<hr>");
			
			// get all projects that are shared with the user
			ArrayList<Project> sharedProjects = con.getSharedProjects(id);
			
			if (!sharedProjects.isEmpty()){
				// print title
				out.println("<div class=\"row\">"
			     	  + "<h3>Other Projects</h3>"
				      + "</div>"
			     	  + "<div class=\"row\">"
				      + "<ul class=\"accordion\" data-accordion data-multi-expand=\"true\" data-allow-all-closed=\"true\">");
				for (Project project : sharedProjects){
					// get all employees
					ArrayList <Employee> employee = project.getEmployees();
					
					Employee supervisor = con.getEmployee(project.getLeader());
					
					String employees = "";
					
					// string of all employee names
					if (!employee.isEmpty()){
						employees = employee.get(0).getName();
						for (int i = 1; i < employee.size(); i++){
							employees += "<br> " + employee.get(i).getName();
						}
					} else {
						employees = "no employees assigned";
					}
					
					// get number of days left to end of project
					Date date = new Date();
					String daysUntilEnd = "";
					int days = DateFormatter.getInstance().getDaysBetween(date, project.getEnd());
					if (days == 0){
						daysUntilEnd = "Project finished";
					} else {
						daysUntilEnd = "Project ends in " + days + " days";
					}
								
					// print list item
					out.println("<li class=\"accordion-item\" data-accordion-item><a href=\"#\" class=\"accordion-title\">"
							  + "<span class=\"bigtext small-3 columns down\">" + project.getShortname() + "</span>"
							  + "<span class=\"middletext small-6 columns end down\">" + project.getName() + "</span>"
							  + "</a>"
							  + "<div class=\"accordion-content\" data-tab-content>"
							  // Write ProjectLeader
							  + "<p><span class=\"small-3 columns\">Project Leader</span>"
							  + "<span class=\"small-9 end columns\">" + supervisor.getName() + "</span>"
							  + "</p>"
							  // Write Duration
							  + "<p>"
							  + "<span class=\"small-3 columns\">Projectduration</span>"
							  + "<span class=\"small-4 columns\">" + project.getDuration() + "</span>"
					  		  + "<span class=\"small-5 end columns align-right\">" + daysUntilEnd + "</span>"
			  		  		  + "</p>"
			  		  		  // Write Budget
			  		  		  + "<p>"
			  		  		  + "<span class=\"small-3 columns\">Budget</span>"
			  		  		  + "<span class=\"small-4 columns\">" + project.getCurrency() 
							  + " " + NumberFormatter.getInstance().formatDouble(project.getBudget()) + "</span>"
			  		  		  + "<span class=\"small-5 end columns align-right\">" + project.getCurrency()
			  		  		  + " " + NumberFormatter.getInstance().formatDouble(project.getRemainingBudget()) + " left</span>"
			  		  		  + "</p>"
							  // Write Workpackages
							  + "<p>"
							  + "<span class=\"small-3 columns\">Workpackages</span>"
							  + "<span class=\"small-9 columns\">" + project.nbrOfWorkpackages() + "</span>"
							  + "</p>"
							  // Write Tasks
							  + "<p>"
							  + "<span class=\"small-3 columns\">Tasks</span>"
							  + "<span class=\"small-9 columns\">" + project.nbrOfTasks() + "</span>"
							  + "</p>"
							  // Write Employees
							  + "<p>"
							  + "<span class=\"small-3 columns\">Employees</span>"
							  + "<span class=\"small-9 end columns\">" + employees 
							  + "</span>"
							  + "</p>"
							  // Write Partners
							  + "<p><span class=\"small-3 columns\">Partner</span>"
							  + "<span class=\"small-9 end columns\">" + project.getPartners() + "</span>"
							  + "</p>"
							  + "</div>"
							  + "</li>");
				}					
			}				
		

			out.println("</div>"
					  + "<hr>");
			
			// get archived projects
			projects = con.getProjects(id, true);
			if (projects != null){
				// print title
				out.println("<div class=\"row\">"
				     	  + "<h3>Archived Projects</h3>"
					      + "</div>"
				     	  + "<div class=\"row\">"
					      + "<ul class=\"accordion\" data-accordion data-multi-expand=\"true\" data-allow-all-closed=\"true\">");
				// write project information
				for (Project project : projects) {
					
					// get all employees
					ArrayList <Employee> employee = project.getEmployees();
					
					String employees = "";
					
					// string of all employee names
					if (!employee.isEmpty()){
						employees = employee.get(0).getName();
						for (int i = 1; i < employee.size(); i++){
							employees += "<br> " + employee.get(i).getName();
						}
					} else {
						employees = "no employees assigned";
					}
																		
					// print list item
					out.println("<li class=\"accordion-item\" data-accordion-item>"
							  + "<a href=\"#\" class=\"accordion-title\">"
							  + "<span class=\"bigtext small-3 columns down\">" + project.getShortname() + "</span>"
							  + "<span class=\"middletext small-6 columns end down\">" + project.getName() + "</span>"
							  + "</a>"
							  + "<div class=\"accordion-content\" data-tab-content>"
							  // Write Duration
							  +"<p>"
							  + "<span class=\"small-3 columns\">Projectduration</span>"
							  + "<span class=\"small-9 end columns\">" + project.getDuration() + "</span>"
			  		  		  + "</p>"
			  		  		  // Write Budget
			  		  		  + "<p>"
			  		  		  + "<span class=\"small-3 columns\">Budget</span>"
			  		  		  + "<span class=\"small-9 columns\">" + project.getCurrency() 
							  + " " + NumberFormatter.getInstance().formatDouble(project.getBudget()) + "</span>"
			  		  		  + "</p>"
							  // Write Workpackages
							  + "<p>"
							  + "<span class=\"small-3 columns\">Workpackages</span>"
							  + "<span class=\"small-9 columns\">" + project.nbrOfWorkpackages() + "</span>"
							  + "</p>"
							  // Write Tasks
							  + "<p>"
							  + "<span class=\"small-3 columns\">Tasks</span>"
							  + "<span class=\"small-9 columns\">" + project.nbrOfTasks() + "</span>"
							  + "</p>"
							  // Write Employees
							  + "<p>"
							  + "<span class=\"small-3 columns\">Employees</span>"
							  + "</span><span class=\"small-9 end columns\">" + employees
							  + "</span></p>"
							  // Write Partners
							  + "<p><span class=\"small-3 columns\">Partner</span>"
							  + "<span class=\"small-9 end columns\">" + project.getPartners() + "</span>"
							  + "</p>"
							  + "</div>"
							  + "</li>");
				}
				out.println("</ul>");
			}
			
		} catch (SQLException e) {
		}
		out.println("</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
