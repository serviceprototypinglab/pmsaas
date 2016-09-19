package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.walj.projectmanagement.util.DBConnection;
import ch.zhaw.walj.projectmanagement.util.Employee;
import ch.zhaw.walj.projectmanagement.util.Project;

/**
 * Projectmanagement tool, Page to add expenses
 * 
 * @author Janine Walther, ZHAW
 * 
 */

@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addExpense")
public class AddExpense extends HttpServlet {

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

		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
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
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />"
				  + "</head>" 
				  // HTML body
				  + "<body>" 
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">"
				  // title
				  + "<div class=\"small-12 medium-6 columns\"><h1>Add Expenses</h1>"
				  + "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>"
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\">All Projects</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\">New Project</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\">New Employee</a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\">Help</a> " 
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\">Logout</a> "
				  + "</div></div></div></header><section>");

		// print HTML section with form
		out.println("<div class=\"row\">" 
				  + "<form method=\"post\" action=\"addExpense\" data-abide novalidate>"

				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p></div>"
				  
				  // project ID
				  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"

				  // select employee
				  + "<label class=\"small-12 medium-6 end columns\">Employee <select name=\"employee\" required>"
				  + "<option></option>");

		// option for every employee
		for (Employee employee : employees) {
			out.println("<option value =\"" + employee.getID() + "\">" + employee.getName() + "</option>");
		}

		// Print fields for costs, type of expense, description and date
		out.println("</select></label>"
				  // costs
				  + "<label class=\"small-12 medium-6 columns\">Costs" 
				  + "<div class=\"input-group\">"
				  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
				  + "<input class=\"input-group-field\" type=\"number\" name=\"costs\" required></div>" 
				  + "</label>"

				  // type
				  + "<label class=\"small-12 medium-6 columns\">Type <select name=\"type\" required>"
				  + "<option></option>" 
				  + "<option>Travel</option>" 
				  + "<option>Others</option>" 
				  + "</select></label>"

				  // description
				  + "<label class=\"small-12 medium-6 columns\">Description"
				  + "<input type=\"text\" name=\"description\" required>" 
				  + "</label>"

				  // date
				  + "<label class=\"small-12 medium-6 end columns\">Date <span class =\"grey\"> (dd.mm.yyyy)</span>"
				  + "<input type=\"date\" name=\"date\" required>" 
				  + "</label>"

				  + "</div>");
		// print HTML submit button
		out.println("<div class=\"row\">"
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Add Expense\">"
				  + "</div>");

		out.println("</section>"
				  + "</div>"
				  + "<script src=\"../../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");

	}

	@Override
	// method to handle post-requests
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		// variable declaration, get parameters
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employee"));
		double costs = Double.parseDouble(request.getParameter("costs"));
		String type = request.getParameter("type");
		String description = request.getParameter("description");
		String date = request.getParameter("date");

		String message = "<div class=\"row\">" 
					   + "<div class=\"callout alert\">" 
					   + "<h5>Something went wrong</h5>"
					   + "<p>The expense could not be added</p>" 
					   + "</div></div>";

		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees = project.getEmployees();

		// create new expense in the database with the given parameters
		try {
			// create new expense
			con.newExpense(projectID, employeeID, costs, type, description, date);

			// create success message
			message = "<div class=\"row\">" 
					+ "<div class=\"callout success\">" 
					+ "<h5>Expense successfully added</h5>"
					+ "<p>The new expense was successfully added with the following data:</p>";

			for (Employee employee : employees) {
				if (employee.getID() == employeeID) {
					message += "<p>Employee: " + employee.getName() + "</p>";
				}
			}
			message += "<p>Costs: " + project.getCurrency() + " " + costs + "</p>" 
					 + "<p>Type: " + type + "</p>"
					 + "<p>Description: " + description + "</p>" 
					 + "<p>Date: " + date + "</p>" + "</div></div>";

		} catch (SQLException e) {
			e.printStackTrace();
		}

		PrintWriter out = response.getWriter();

		// print HTML head, header, success / error message
		out.println("<!DOCTYPE html>" 
				  + "<html>"
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">" 
				  + "<title>Add Expenses</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />"
				  + "</head>" 
				  // HTML body
				  + "<body>" 
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">"
				  // title
				  + "<div class=\"small-12 medium-6 columns\"><h1>Add Expenses</h1>" 
				  + "<a href=\"Project?id=" + projectID	+ "\" class=\"back\">" 
				  + "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project"
				  + "</a></div>" 
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\">All Projects</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\">New Project</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\">New Employee</a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\">Help</a> " 
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\">Logout</a> "
				  + "</div></div></div></header><section>" 
				  + message);

		// print HTML expense form
		out.println("<div class=\"row\">" 
				  + "<form method=\"post\" action=\"addExpense\" data-abide novalidate>"
				  
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p></div>"

				  // project ID
				  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				  
				  // select employee
				  + "<label class=\"small-12 medium-6 end columns\">Employee <select name=\"employee\" required>"
				  + "<option></option>");

		// option for every employee
		for (Employee employee : employees) {
			out.println("<option value =\"" + employee.getID() + "\">" + employee.getName() + "</option>");
		}

		out.println("</select></label>"
				  // costs
				  + "<label class=\"small-12 medium-6 columns\">Costs" 
				  + "<div class=\"input-group\">"
				  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
				  + "<input class=\"input-group-field\" type=\"number\" name=\"costs\" required></div>" 
				  + "</label>"

				  // type
				  + "<label class=\"small-12 medium-6 columns\">Type <select name=\"type\" required>"
				  + "<option></option>" 
				  + "<option>Travel</option>" 
				  + "<option>Others</option>" 
				  + "</select></label>"
				  
				  // description
				  + "<label class=\"small-12 medium-6 columns\">Description"
				  + "<input type=\"text\" name=\"description\" required>" 
				  + "</label>"

				  // date
				  + "<label class=\"small-12 medium-6 end columns\">Date <span class =\"grey\"> (dd.mm.yyyy)</span>"
				  + "<input type=\"date\" name=\"date\" required>" 
				  + "</label>"

				  + "</div>");

		// print HTML submit button
		out.println("<div class=\"row\">"
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Add Expense\">"
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
