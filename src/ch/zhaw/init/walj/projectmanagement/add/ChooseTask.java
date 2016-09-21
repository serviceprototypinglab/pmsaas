package ch.zhaw.init.walj.projectmanagement.add;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;

/**
 * Projectmanagement tool, Page to assign employees (choose task)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/assignEmployee/chooseTask")
public class ChooseTask extends HttpServlet{

	// database access information
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	// connection to database
	private DBConnection con = new DBConnection(url, dbName, userName, password);
		
	
	@Override
	// method to handle post-requests
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		// variable declaration, get parameters from request
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employee"));
		
		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		ArrayList<ProjectTask> tasks = project.getTasks();
		ArrayList<Integer> assignedTasks = null;
		// get assignments
		try {
			assignedTasks = con.getAssignments(employeeID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		
		PrintWriter out = response.getWriter();
		
		// print HTML
		out.println("<!DOCTYPE html>" 
				  + "<html>" 
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">"
				  + "<title>Assign Employees</title>" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />" 
				  + "</head>" 
				  // HTML body
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-8 medium-6 columns\">"
				  // title
				  + "<h1>Assign Employees</h1><a href=\"../Project?id=" + projectID + "\" class=\"back\">"
				  + "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>"
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\">All Projects</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\">New Project</a> " 
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\">New Employee</a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\">Help</a> " 
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\">Logout</a> " 
				  + "</div>" 
				  + "</div>"
				  + "</div>" 
				  + "</header>"
				  + "<section>");
		// print HTML section with form
		out.println("<div class=\"row\">"
				  + "<form method=\"post\" action=\"../assignEmployee\" data-abide novalidate>"
			
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose at least one task.</p></div>"

				  // project and employee ID
				  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				  + "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">"
				  
				  // select tasks
				  + "<label class=\"small-12 medium-6 end columns\">Task "
				  + "<span class=\"grey\">multiple options possible</span> <select name=\"tasks\" size=\"5\" multiple required>");
				
		// only tasks, the employee is not assigned to yet
		for (ProjectTask task : tasks){
			int i = 0;
			for (int assignedTask : assignedTasks){
				if (task.getID() == assignedTask){
					i++;
				}
			}
			if (i == 0){
				out.println("<option value =\"" + task.getID() + ";" + task.getName() + "\">" + task.getName() + "</option>");
			}
		}
				
		out.println("</select></label></div>");
	
		// print return and submit buttons
		out.println("<div class=\"row\">"
					
				  + "<a href=\"../assignEmployee?projectID=" + projectID +"&employee=" + employeeID + "\" class=\"small-3 columns large button back-button\"><i class=\"fa fa-chevron-left\"></i>  Choose Employee</a>"
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Assign\">"
				  + "</div>");
		
		out.println("</section>"
				  + "</div>"
				  + "<script src=\"../../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
			
		
	}
}
