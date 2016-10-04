package ch.zhaw.init.walj.projectmanagement.delete;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;

/**
 * Projectmanagement tool, Page to delete workpackages
 * 
 * @author Janine Walther, ZHAW
 * 
 */

@SuppressWarnings("serial")
@WebServlet("/Projects/deleteWorkpackage")
public class DeleteWorkpackage extends HttpServlet {

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
		int workpackageID = Integer.parseInt(request.getParameter("workpackageID"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		String message = "";

		// delete project from database
		try {
			con.deleteWorkpackage(workpackageID);
			// success message
			message = "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The workpackage has sucessfully been deleted.</h2>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#workpackages\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
					+ "</div>"
					+ "</div>";
		} catch (SQLException e) {
			// error message
			message = "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The workpackage could not be deleted</h2>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#workpackages\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
					+ "</div>"
					+ "</div>";			
		}
		
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
				  + "<h1>Delete Workpackage</h1>"
				  + "</div>"
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
