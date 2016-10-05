package ch.zhaw.init.walj.projectmanagement.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;


// TODO /** kommentare
@SuppressWarnings("serial")
@WebServlet("/Projects/EditProject")
public class EditProject extends HttpServlet {
	
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
		String name = request.getParameter("name");
		String shortname = request.getParameter("shortname");
		double budget = Double.parseDouble(request.getParameter("budget"));
		String currency = request.getParameter("currency");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String partners = request.getParameter("partner");
		

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(start);
			start = format2.format(date);
			date = format.parse(end);
			end = format2.format(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
				
		String message = "";
		
		try {
			con.updateProject(id, name, shortname, budget, currency, start, end, partners);
			
			message = "<div class=\"callout success\">"
					+ "<h5>Project successfully updated</h5>"
					+ "<p>The new project has succsessfully been updated with the following data:</p>"
					+ "<p>Name: " + name + "</p>"
					+ "<p>Shortname: " + shortname + "</p>"
					+ "<p>Budget: " + budget + "</p>"
					+ "<p>Currency: " + currency + ""
					+ "<p>Duration: " + start + " - " + end + "</p>"
					+ "<p>Partners: " + partners + "</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + id + "\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + id + "\">Click here to go to the project overview</a>"
					+ "</div>";
		} catch (SQLException e) {
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
				    + "<h5>Project could not be updated</h5>"
				    + "<p>An error occured and the project could not be updated.</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + id + "\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + id + "\">Click here to go to the project overview</a>"
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
				  + "<div class=\"small-8 columns\">"
				  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
				  // title
				  + "<h1>" + name + "</h1>"
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
