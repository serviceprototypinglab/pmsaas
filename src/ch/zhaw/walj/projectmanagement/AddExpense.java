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

@SuppressWarnings("serial")
@WebServlet("/Overview/addExpense")
public class AddExpense extends HttpServlet{
	
	private int projectIDFS;
	private int employeeIDFS;
	private double costs;
	private String type;
	private String description;
	private String date;
	
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	private DBConnection con = new DBConnection(url, dbName, userName, password);
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		}
				

		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees = project.getEmployees();
			
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
				+ "<html>\n" 
					+ "<head>\n" 
						+ "<meta charset=\"UTF-8\">\n"
						+ "<title>Add Expenses</title>\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />\n"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />\n" 
					+ "</head>\n" 
					+ "<body>\n"
						+ "<div id=\"wrapper\">\n" 
							+ "<header>\n" 
								+ "<div class=\"row\">\n" 
									+ "<div class=\"small-8 medium-6 columns\"><h1>Add Expenses</h1><a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>\n"
									+ "<div class=\"small-12 medium-6 columns\">\n" 
										+ "<div class=\"float-right menu\">\n"
											+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
											+ "<a href=\"newProject\" class=\"button\">New Project</a>\n" 
											+ "<a href=\"newEmployee\" class=\"button\">New Employee</a>\n"
											+ "<a href=\"help\" class=\"button\">Help</a>\n" 
											+ "<a href=\"logout\" class=\"button\">Logout</a>\n" 
										+ "</div>\n" 
									+ "</div>\n"
								+ "</div>\n" 
							+ "</header>\n"
							+ "<section>\n");
		
		out.println("<div class=\"row\">"
					+ "<form method=\"post\" action=\"addExpense\" data-abide novalidate>"
				
					+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p></div>"
					
					+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
					
					+ "<label class=\"small-12 medium-6 end columns\">Employee <select name=\"employee\" required>"
					+ "<option></option>");
		
		for (Employee employee : employees){
			out.println("<option value =\"" + employee.getID() + "\">" + employee.getName() + "</option>");
		}
				
		out.println("</select></label>"
				
						+ "<label class=\"small-12 medium-6 columns\">Costs"
							+ "<div class=\"input-group\">"
							+ "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
							+ "<input class=\"input-group-field\" type=\"number\" name=\"costs\" required></div>"
							+ "</label>"
							
						+ "<label class=\"small-12 medium-6 columns\">Type <select name=\"type\" required>"
							+ "<option></option>"
							+ "<option>Travel</option>"
							+ "<option>Others</option>"
						+ "</select></label>"
							
						+ "<label class=\"small-12 medium-6 columns\">Description"
							+ "<input type=\"text\" name=\"description\" required>"
						+ "</label>"
							
						+ "<label class=\"small-12 medium-6 end columns\">Date <span class =\"grey\"> (YYYY-MM-DD)</span>"
							+ "<input type=\"date\" name=\"date\" required>"
						+ "</label>"
							
						+ "</div>");
		
		out.println("<div class=\"row\">"
				+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Add Expense\">"
				+ "</div>");
		
		out.println("</section></div><script src=\"../js/vendor/jquery.js\"></script><script src=\"../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
				
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employee"));
		double costs = Double.parseDouble(request.getParameter("costs"));
		String type = request.getParameter("type");
		String description = request.getParameter("description");
		String date = request.getParameter("date");
		
		try {
			con.newExpense(projectID, employeeID, costs, type, description, date);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees = project.getEmployees();
				
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
		+ "<html>\n" 
			+ "<head>\n" 
				+ "<meta charset=\"UTF-8\">\n"
				+ "<title>Add Expenses</title>\n" 
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />\n" 
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />\n" 
			+ "</head>\n" 
			+ "<body>\n"
				+ "<div id=\"wrapper\">\n" 
					+ "<header>\n" 
						+ "<div class=\"row\">\n" 
							+ "<div class=\"small-8 medium-6 columns\"><h1>Add Expenses</h1>"
							+ "<a href=\"Project?id=" + projectID + "\" class=\"back\">"
									+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project"
							+ "</a></div>\n"
							+ "<div class=\"small-12 medium-6 columns\">\n" 
								+ "<div class=\"float-right menu\">\n"
									+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
									+ "<a href=\"newProject\" class=\"button\">New Project</a>\n" 
									+ "<a href=\"newEmployee\" class=\"button\">New Employee</a>\n"
									+ "<a href=\"help\" class=\"button\">Help</a>\n" 
									+ "<a href=\"logout\" class=\"button\">Logout</a>\n" 
								+ "</div>\n" 
							+ "</div>\n"
						+ "</div>\n" 
					+ "</header>\n"
					+ "<section>\n"	
					+ "<div class=\"row\">"
					+ "<div class=\"callout success\">"
					+ "<h5>Expense successfully added</h5>"
					+ "<p>The new expense was successfully added with the following data:</p>");
					
		for (Employee employee : employees){
			if (employee.getID() == employeeID) {
				out.println("<p>Employee: " + employee.getName()+ "</p>");
			}
		}		
		out.println("<p>Costs: " + project.getCurrency() + " " + costs + "</p>"
					+ "<p>Type: " + type + "</p>"
					+ "<p>Description: " + description + "</p>"
					+ "<p>Date: " + date + "</p>"
					+ "</div></div>");	
		
		out.println("<div class=\"row\">"
			+ "<form method=\"post\" action=\"addExpense\" data-abide novalidate>"
		
			+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
			+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p></div>"
			
			+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
			
			+ "<label class=\"small-12 medium-6 end columns\">Employee <select name=\"employee\" required>"
			+ "<option></option>");
		
		for (Employee employee : employees){
		out.println("<option value =\"" + employee.getID() + "\">" + employee.getName() + "</option>");
		}
		
		out.println("</select></label>"
		
				+ "<label class=\"small-12 medium-6 columns\">Costs"
					+ "<div class=\"input-group\">"
					+ "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
					+ "<input class=\"input-group-field\" type=\"number\" name=\"costs\" required></div>"
					+ "</label>"
					
				+ "<label class=\"small-12 medium-6 columns\">Type <select name=\"type\" required>"
					+ "<option></option>"
					+ "<option>Travel</option>"
					+ "<option>Others</option>"
				+ "</select></label>"
					
				+ "<label class=\"small-12 medium-6 columns\">Description"
					+ "<input type=\"text\" name=\"description\" required>"
				+ "</label>"
					
				+ "<label class=\"small-12 medium-6 end columns\">Date <span class =\"grey\"> (YYYY-MM-DD)</span>"
					+ "<input type=\"date\" name=\"date\" required>"
				+ "</label>"
					
				+ "</div>");
		
		out.println("<div class=\"row\">"
		+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Add Expense\">"
		+ "</div>");
		
		out.println("</section></div><script src=\"../js/vendor/jquery.js\"></script><script src=\"../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");

	}
}
