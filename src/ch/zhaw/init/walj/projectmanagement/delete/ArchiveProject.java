package ch.zhaw.init.walj.projectmanagement.delete;

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
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.Project;

/**
 * Projectmanagement tool, Page to archive projects
 * 
 * @author Janine Walther, ZHAW
 * 
 */

@SuppressWarnings("serial")
@WebServlet("/Projects/archiveProject")
public class ArchiveProject extends HttpServlet {

	// Database access information
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName = "Janine";
	String password = "test123";

	// connection to database
	private DBConnection con = new DBConnection(url, dbName, userName, password);

	@Override
	// method to handle get-requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		// get project given from get-parameter projectID
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		String message = "";

		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
		}

		// delete project from database
		try {
			con.archiveProject(projectID);
			// success message
			message = "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The Project " + project.getName() + " has sucessfully been archived.</h2>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview\">"
					+ "<i class=\"fa fa-chevron-left fa-4x\" aria-hidden=\"true\"></i></br>"
					+ "Click here to go back to overview</a>"
					+ "</div>"
					+ "</div>";
		} catch (SQLException e) {
			// error message
			message =  "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The Project " + project.getName() + " could not be archived</h2>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + project.getID() + "\">"
					+ "<i class=\"fa fa-chevron-left fa-4x\" aria-hidden=\"true\"></i></br>"
					+ "Click here to go back to project</a>"
					+ "</div>"
					+ "</div>";			
		}
		
		// get the employees assigned to the project and add them to the ArrayList
		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees = project.getEmployees();

		PrintWriter out = response.getWriter();

		// Print HTML head and header
		out.println("<!DOCTYPE html>" 
				  + "<html>" 
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">" 
				  + "<title>Add Expenses</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />"
				  + "</head>" 
				  // HTML body
				  + "<body>" 
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">"
				  // title
				  + "<div class=\"small-12 medium-8 columns\">"
				  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
				  + "<h1>Archive Project</h1>"
				  + "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>"
				  // menu
				  + "<div class=\"small-12 medium-4 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\" title=\"All Projects\"><i class=\"fa fa-list fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\" title=\"New Project\"><i class=\"fa fa-file fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/employee\" class=\"button\" title=\"My Profile\"><i class=\"fa fa-user fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\" title=\"Help\"><i class=\"fa fa-book fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw\"></i></a> "
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</header>"
				  + "<section>"
				  + message
				  + "</section>"
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
}
