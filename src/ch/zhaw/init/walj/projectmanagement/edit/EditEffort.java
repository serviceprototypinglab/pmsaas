package ch.zhaw.init.walj.projectmanagement.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;

@SuppressWarnings("serial")
@WebServlet("/Projects/EditEffort")
public class EditEffort extends HttpServlet {
	
	// Variables for the database connection
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	// create a new DB connection
	private DBConnection con = new DBConnection(url, dbName, userName, password);
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		// get the parameters
		int id = Integer.parseInt(request.getParameter("id"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int taskID = Integer.parseInt(request.getParameter("taskID"));
		String month = request.getParameter("month");
		String hours = request.getParameter("hours");
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		ProjectTask t = project.getTask(taskID);
		
		
		final PrintWriter out = response.getWriter();
				
		String message = "";
		
		try {
			con.updateEffort(id, month, hours);
			
			message = "<div class=\"callout success\">"
					+ "<h5>Project successfully updated</h5>"
					+ "<p>The new project has succsessfully been updated with the following data:</p>"
					+ "<p>Task: " + t.getName() + "</p>"
					+ "<p>Month: " + month + "</p>"
					+ "<p>Hours: " + hours + "</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#effort\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "#effort\">Click here to go to the project overview</a>"
					+ "</div>";
		} catch (SQLException e) {
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
				    + "<h5>Project could not be updated</h5>"
				    + "<p>An error occured and the project could not be updated.</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#effort\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "#effort\">Click here to go to the project overview</a>"
					+ "</div>";
		}
						
		out.println("<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>New Project</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\">"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  + "</head>"
				  + "<body>"
				  + "<div id=\"wrapper\"><header>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-8 medium-6 columns\">"
				  // title
				  + "<h1>" + project.getName() + "</h1>"
				  + "</div>"
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">"
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\" title=\"All Projects\"><i class=\"fa fa-list fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\" title=\"New Project\"><i class=\"fa fa-file fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/employee\" class=\"button\" title=\"My Profile\"><i class=\"fa fa-user fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\" title=\"Help\"><i class=\"fa fa-book fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw\"></i></a> "
				  + "</div>"
				  + "</div>"
				  + "</header>"
				  // HTML section with form
				  + "<section>"
				  + "<div class=\"row\">"
				  + message
				  + "</div>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
}
