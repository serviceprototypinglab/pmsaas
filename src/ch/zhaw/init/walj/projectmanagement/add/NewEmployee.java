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
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Mail;

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
	
	// connection to database
	private DBConnection con = new DBConnection();
	
	
	@Override
	/*
	 * Form to create a new employee
	 * handles get requests on /newEmployee
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
		
		// Print HTML
		out.println(HTMLHeader.getInstance().getHeader("New Employee", "../", "New Employee", "")
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
		
		int id = (int) request.getSession(false).getAttribute("ID");
		
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
			Employee user = con.newEmployee(id, firstname, lastname, kuerzel, mail, wage);
			
			Mail mail = new Mail(user);
			mail.sendWelcomeMail(""); // TODO correct path
			
			// success message
			message = "<div class=\"row\">"
					+ "<div class=\"callout success\">"
					+ "<h5>Employee successfully created</h5>"
					+ "<p>The new employee was successfully created with the following data:</p>"
					+ "<p>Name: " + user.getName()+ "</p>"
					+ "<p>Kuerzel: " + user.getKuerzel() + "</p>"
					+ "<p>Mail: " + user.getMail() + "</p>"
					+ "<p>Wage: " + wage + "</p>"
					+ "<p>Password: " + user.getPassword() + "</p>"
					+ "</div></div>";			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
		
		// print HTML
		out.println(HTMLHeader.getInstance().getHeader("New Employee", "../", "New Employee")
				
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
