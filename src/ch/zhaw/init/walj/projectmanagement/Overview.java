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

import ch.zhaw.init.walj.projectmanagement.chart.PieChart;
import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.NumberFormatter;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview")
public class Overview extends HttpServlet {
	
	private PieChart piechart;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		ArrayList<Project> projects = new ArrayList<Project>();
		
		int id = (int) request.getSession(false).getAttribute("ID");
		String name = (String) request.getSession(false).getAttribute("user");
				
		DBConnection con = new DBConnection();
		
				
		PrintWriter out = response.getWriter();
		
		String script = "<script>function Redirect(ID) {var url = \"Overview/Project?id=\" + ID; window.location=url;}</script>";
		
		out.println(HTMLHeader.getInstance().getHeader("Projects", "../", "Projects", script, "")
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
			projects = con.getProjects(id, false);
			if (projects != null){
				out.println("<ul class=\"accordion\" data-accordion data-multi-expand=\"true\" data-allow-all-closed=\"true\">");
				// write project information
				for (Project project : projects) {
					
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
					int days = DateFormatter.getInstance().getDaysBetween(date, project.getEnd());
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
							  + "<button class=\"button small-2 columns end down smalltext\" onclick=\"Redirect(" + project.getID() + ");\">Select</button>"
							// TODO  + "<span class=\"success badge errorbadge\">0</span>"
							  + "</a>"
							  + "<div class=\"accordion-content\" data-tab-content>"
							  // Write Duration
							  +"<p>"
							  + "<span class=\"small-3 columns\">Projectduration</span>"
							  + "<span class=\"small-4 columns\">" 
							  + DateFormatter.getInstance().formatDate(project.getStart()) + " - " + DateFormatter.getInstance().formatDate(project.getEnd()) + "</span>"
					  		  + "<span class=\"small-4 end columns align-right\">" + daysUntilEnd + "</span>"
			  		  		  + "</p>"
			  		  		  // Write Budget
			  		  		  + "<p>"
			  		  		  + "<span class=\"small-3 columns\">Budget</span>"
			  		  		  + "<span class=\"small-4 columns\">" + project.getCurrency() 
							  + " " + NumberFormatter.getInstance().formatDouble(project.getBudget()) + "</span>"
			  		  		  + "<span class=\"small-4 end columns align-right\">" + project.getCurrency()
			  		  		  + " " + NumberFormatter.getInstance().formatDouble(piechart.getRemainingBudget()) + " left</span>"
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
				out.println("</ul>");
			} else {
				out.println("<div class=\"callout warning\">"
						  + "<h4>No projects found</h4>"
						  + "<a href=\"/Projektverwaltung/Projects/newProject\">Click here to create a new project</a>"
						  + "</div>");				
			}
		
			out.println("</div>");
			
			
			ArrayList<Project> sharedProjects = con.getSharedProjects(id);
			
			if (sharedProjects != null){
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
							employees += ", " + employee.get(i).getName();
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
			
					piechart = new PieChart(project);
					
					
					// print list item
					out.println("<li class=\"accordion-item\" data-accordion-item><a href=\"#\" class=\"accordion-title\">"
							  + "<span class=\"bigtext small-3 columns down\">" + project.getShortname() + "</span>"
							  + "<span class=\"middletext small-6 columns end down\">" + project.getName() + "</span>"
							 // TODO + "<span class=\"success badge errorbadge\">0</span>"
							  + "</a>"
							  + "<div class=\"accordion-content\" data-tab-content>"
							  // Write ProjectLeader
							  + "<p><span class=\"small-3 columns\">Project Leader</span>"
							  + "<span class=\"small-9 end columns\">" + supervisor.getName() + "</span>"
							  + "</p>"
							  // Write Duration
							  + "<p>"
							  + "<span class=\"small-3 columns\">Projectduration</span>"
							  + "<span class=\"small-4 columns\">" 
							  + DateFormatter.getInstance().formatDate(project.getStart()) + " - " + DateFormatter.getInstance().formatDate(project.getEnd()) + "</span>"
					  		  + "<span class=\"small-4 end columns align-right\">" + daysUntilEnd + "</span>"
			  		  		  + "</p>"
			  		  		  // Write Budget
			  		  		  + "<p>"
			  		  		  + "<span class=\"small-3 columns\">Budget</span>"
			  		  		  + "<span class=\"small-4 columns\">" + project.getCurrency() 
							  + " " + NumberFormatter.getInstance().formatDouble(project.getBudget()) + "</span>"
			  		  		  + "<span class=\"small-4 end columns align-right\">" + project.getCurrency()
			  		  		  + " " + NumberFormatter.getInstance().formatDouble(piechart.getRemainingBudget()) + " left</span>"
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
							  + "<span class=\"small-4 columns\">" + project.nbrOfEmployees() 
							  + "</span><span class=\"small-4 end columns align-right\">" + employees
							  + "</span></p>"
							  // Write Partners
							  + "<p><span class=\"small-3 columns\">Partner</span>"
							  + "<span class=\"small-9 end columns\">" + project.getPartners() + "</span>"
							  + "</p>"
							  + "</div>"
							  + "</li>");
				}					
			}				
		

			out.println("</div>");
			
			
			projects = con.getProjects(id, true);
			if (projects != null){
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
							employees += ", " + employee.get(i).getName();
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
							  + "<span class=\"small-9 end columns\">" 
							  + DateFormatter.getInstance().formatDate(project.getStart()) + " - " + DateFormatter.getInstance().formatDate(project.getEnd()) + "</span>"
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
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
