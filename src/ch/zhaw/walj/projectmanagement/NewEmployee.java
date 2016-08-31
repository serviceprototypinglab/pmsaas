package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/newEmployee")
public class NewEmployee extends HttpServlet {
	
	private String			firstname;
	private String			lastname;
	private String			kuerzel;
	private String			mail;
	private String			wage;
	
	String					url			= "jdbc:mysql://localhost:3306/";
	String					dbName		= "projectmanagement";
	String					userName	= "Janine";
	String					password	= "test123";
	
	private DBConnection	con			= new DBConnection(url, dbName, userName, password);
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>"
				+ "<html><head>"
				
				+ "<meta charset=\"UTF-8\">"
				
				+ "<title>New Employee</title>"
				
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />"
				
				+ "</head><body>"
				+ "<div id=\"wrapper\">"
				+ "<header>"
				
				+ "<div class=\"row\">"
				+ "<div class=\"small-8 medium-6 columns\">"
				+ "<h1>New Employee</h1>"
				+ "</div>"
				
				+ "<div class=\"small-12 medium-6 columns\">"
				+ "<div class=\"float-right menu\">"
				
				+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a> "
				+ "<a href=\"newProject\" class=\"button\">New Project</a> "
				+ "<a href=\"newEmployee\" class=\"button\">New Employee</a> "
				+ "<a href=\"help\" class=\"button\">Help</a> "
				+ "<a href=\"logout\" class=\"button\">Logout</a> "
				
				+ "</div></div></div></header>"
				
				+ "<section>"
				
				+ "<form method=\"post\" action=\"newEmployee\" data-abide novalidate>"
				
				+ "<div class=\"row\">"
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<label class=\"small-12 large-6 columns\">First Name "
				+ "<input type=\"text\" name=\"firstname\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns\">Last Name "
				+ "<input type=\"text\" name=\"lastname\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns\">Kürzel "
				+ "<input type=\"text\" name=\"kuerzel\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns\">Mail "
				+ "<input type=\"email\" name=\"mail\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns end\">Wage per Hour "
				+ "<input type=\"number\" name=\"wage\" required>"
				+ "</label>"
				
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Employee\">"
				+ "</div>"
				
				+ "</form>"
				+ "</section>"
				
				+ "<script src=\"js/vendor/jquery.js\"></script>"
				+ "<script src=\"js/vendor/foundation.min.js\"></script>"
				+ "<script>$(document).foundation();</script>"
				
				+ "</div>"
				+ "</body></html>");
		
	}	
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		firstname = request.getParameter("firstname");
		lastname = request.getParameter("lastname");
		kuerzel = request.getParameter("kuerzel");
		mail = request.getParameter("mail");
		wage = request.getParameter("wage");
		
		String message = "<div class=\"row\">"
						+ "<div class=\"callout alert\">"
						+ "<h5>Something went wrong</h5>"
						+ "<p>The employee could not be created.</p>"
						+ "</div></div>";
		
		
		try {
			con.newEmployee(1, firstname, lastname, kuerzel, mail, wage);
			message = "<div class=\"row\">"
					+ "<div class=\"callout success\">"
					+ "<h5>Employee successfully created</h5>"
					+ "<p>The new employee was successfully created with the following data:</p>"
					+ "<p>Name: " + firstname + " " + lastname + "</p>"
					+ "<p>Kuerzel: " + kuerzel + "</p>"
					+ "<p>Mail: " + mail + "</p>"
					+ "<p>Wage: " + wage + "</p>"
					+ "<a href=\"#\">If you want to assign this employee to a task, klick here</a>"
					+ "</div></div>";
			
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>"
				+ "<html><head>"
				
				+ "<meta charset=\"UTF-8\">"
				
				+ "<title>New Employee</title>"
				
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />"
				
				+ "</head><body>"
				+ "<div id=\"wrapper\">"
				+ "<header>"
				
				+ "<div class=\"row\">"
				+ "<div class=\"small-8 medium-6 columns\">"
				+ "<h1>New Employee</h1>"
				+ "</div>"
				
				+ "<div class=\"small-12 medium-6 columns\">"
				+ "<div class=\"float-right menu\">"
				
				+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a> "
				+ "<a href=\"newProject\" class=\"button\">New Project</a> "
				+ "<a href=\"newEmployee\" class=\"button\">New Employee</a> "
				+ "<a href=\"help\" class=\"button\">Help</a> "
				+ "<a href=\"logout\" class=\"button\">Logout</a> "
				
				+ "</div></div></div></header>"
				
				+ "<section>"
				+ message
								
				+ "<form method=\"post\" action=\"newEmployee\" data-abide novalidate>"
				
				+ "<div class=\"row\">"
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<label class=\"small-12 large-6 columns\">First Name "
				+ "<input type=\"text\" name=\"firstname\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns\">Last Name "
				+ "<input type=\"text\" name=\"lastname\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns\">Kürzel "
				+ "<input type=\"text\" name=\"kuerzel\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns\">Mail "
				+ "<input type=\"email\" name=\"mail\" required>"
				+ "</label>"
				
				+ "<label class=\"small-12 large-6 columns end\">Wage per Hour "
				+ "<input type=\"number\" name=\"wage\" required>"
				+ "</label>"
				
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Employee\">"
				+ "</div>"
				
				+ "</form>"
				+ "</section>"
				
				+ "<script src=\"js/vendor/jquery.js\"></script>"
				+ "<script src=\"js/vendor/foundation.min.js\"></script>"
				+ "<script>$(document).foundation();</script>"
				
				+ "</div>"
				+ "</body></html>");
		
	}
}
