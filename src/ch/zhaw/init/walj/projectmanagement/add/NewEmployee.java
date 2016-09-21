package ch.zhaw.init.walj.projectmanagement.add;

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
 * Projectmanagement tool, Page to assign employees (choose task)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/newEmployee")
public class NewEmployee extends HttpServlet {
	
	// variable declaration
	private String firstname;
	private String lastname;
	private String kuerzel;
	private String mail;
	private String wage;
	
	// database access information
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	// connection to database
	private DBConnection con = new DBConnection(url, dbName, userName, password);
	
	
	@Override
	/*
	 * Form to create a new employee
	 * handles get requests on /newEmployee
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
		
		// Print HTML
		out.println("<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>New Employee</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  + "</head>"
				  // HTML body
				  + "<body>"
				  + "<div id=\"wrapper\">"
				  + "<header>"
				  + "<div class=\"row\">"
				  // title
				  + "<div class=\"small-8 medium-6 columns\">"
				  + "<h1>New Employee</h1>"
				  + "</div>"
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
				  // HTML section with form
				  + "<section>"
				  + "<form method=\"post\" action=\"newEmployee\" data-abide novalidate>"
				  + "<div class=\"row\">"
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				  + "</div></div>"
				  // field for firstname
				  + "<div class=\"row\">"
				  + "<label class=\"small-12 large-6 columns\">First Name "
				  + "<input type=\"text\" name=\"firstname\" required>"
				  + "</label>"
				  // field for lastname
				  + "<label class=\"small-12 large-6 columns\">Last Name "
				  + "<input type=\"text\" name=\"lastname\" required>"
				  + "</label>"
				  // field for kuerzel
				  + "<label class=\"small-12 large-6 columns\">Kuerzel "
				  + "<input type=\"text\" name=\"kuerzel\" required>"
				  + "</label>"
				  // field for e-mail address
				  + "<label class=\"small-12 large-6 columns\">Mail "
				  + "<input type=\"email\" name=\"mail\" required>"
				  + "</label>"
				  // field for wage per hour
				  + "<label class=\"small-12 large-6 columns end\">Wage per Hour "
				  + "<input type=\"number\" name=\"wage\" required>"
				  + "</label>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // submit button
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Employee\">"
				  + "</div>"
				  + "</form>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</div>"
				  + "</body>"
				  + "</html>");
		
	}	
		
	
	@Override
	/*
	 * Form to create a new employee, after the first employee was created
	 * handles post requests on /newEmployee
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		// get parameters
		firstname = request.getParameter("firstname");
		lastname = request.getParameter("lastname");
		kuerzel = request.getParameter("kuerzel");
		mail = request.getParameter("mail");
		wage = request.getParameter("wage");
		
		// error message
		String message = "<div class=\"row\">"
					   + "<div class=\"callout alert\">"
					   + "<h5>Something went wrong</h5>"
					   + "<p>The employee could not be created.</p>"
					   + "</div></div>";
		
		
		// create new employee
		try {
			con.newEmployee(1, firstname, lastname, kuerzel, mail, wage);
			// success message
			message = "<div class=\"row\">"
					+ "<div class=\"callout success\">"
					+ "<h5>Employee successfully created</h5>"
					+ "<p>The new employee was successfully created with the following data:</p>"
					+ "<p>Name: " + firstname + " " + lastname + "</p>"
					+ "<p>Kuerzel: " + kuerzel + "</p>"
					+ "<p>Mail: " + mail + "</p>"
					+ "<p>Wage: " + wage + "</p>"
					+ "<a href=\"#\">If you want to assign this employee to a task, klick here</a>" // TODO insert correct link
					+ "</div></div>";			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
		
		// print HTML
		out.println("<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>New Employee</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  + "</head>"
				  + "<body>"
				  + "<div id=\"wrapper\">"
				  + "<header>"
				  + "<div class=\"row\">"
				  // title
				  + "<div class=\"small-8 medium-6 columns\">"
				  + "<h1>New Employee</h1>"
				  + "</div>"
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">"
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\">All Projects</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\">New Project</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\">New Employee</a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\">Help</a> "
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\">Logout</a> "
				  + "</div></div></div>"
				  + "</header>"
				  // HTML section with message and form
				  + "<section>"
				  + message
				  + "<form method=\"post\" action=\"newEmployee\" data-abide novalidate>"
				  + "<div class=\"row\">"
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				  + "</div></div>"
				  + "<div class=\"row\">"
				  // field for firstname
				  + "<label class=\"small-12 large-6 columns\">First Name "
				  + "<input type=\"text\" name=\"firstname\" required>"
				  + "</label>"
				  // field for lastname
				  + "<label class=\"small-12 large-6 columns\">Last Name "
				  + "<input type=\"text\" name=\"lastname\" required>"
				  + "</label>"
				  // field for kuerzel
				  + "<label class=\"small-12 large-6 columns\">Kuerzel "
				  + "<input type=\"text\" name=\"kuerzel\" required>"
				  + "</label>"
				  // field for e-mail address
				  + "<label class=\"small-12 large-6 columns\">Mail "
				  + "<input type=\"email\" name=\"mail\" required>"
				  + "</label>"
				  // field for the employees wage per hour
				  + "<label class=\"small-12 large-6 columns end\">Wage per Hour "
				  + "<input type=\"number\" name=\"wage\" required>"
				  + "</label>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // submit button
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Employee\">"
				  + "</div>"
				  + "</form>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</div>"
				  + "</body></html>");
	}
}
