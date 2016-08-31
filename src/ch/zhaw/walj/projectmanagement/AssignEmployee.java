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
@WebServlet("/Overview/assignEmployee")
public class AssignEmployee extends HttpServlet{

	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	private DBConnection con = new DBConnection(url, dbName, userName, password);
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		int projectID = Integer.parseInt(request.getParameter("projectID"));
			

		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			//TODO Get ID from logged-in user
			employees = con.getAllEmployees(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		String message = "";
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
				+ "<html>\n" 
					+ "<head>\n" 
						+ "<meta charset=\"UTF-8\">\n"
						+ "<title>Assign Employees</title>\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />\n"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />\n" 
					+ "</head>\n" 
					+ "<body>\n"
						+ "<div id=\"wrapper\">\n" 
							+ "<header>\n" 
								+ "<div class=\"row\">\n" 
									+ "<div class=\"small-8 medium-6 columns\">"
									+ "<h1>Assign Employees</h1><a href=\"Project?id=" + projectID + "\" class=\"back\">"
											+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>\n"
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
							+ message
							+ "<section>\n");
		out.println("<div class=\"row\">"
				+ "<form method=\"post\" action=\"assignEmployee/chooseTask\" data-abide novalidate>"
			
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose an employee.</p></div>"
				
				+ "<h3>Choose an employee you want to assign to a task.</h3></br>"
				+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				
				+ "<h5 class=\"small-12 medium-2 columns\">Employee</h5>"
				+ "<div class=\"small-12 medium-6 end columns\">"
				+ "<select name=\"employee\" required>"
				+ "<option></option>");
	
		for (Employee employee : employees){
			out.println("<option value =\"" + employee.getID() + "\">" + employee.getName() + "</option>");
		}
				
		out.println("</select></div>"
						+ "</div>"
						+"<div class=\"row\">"
						+ "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Choose Task  <i class=\"fa fa-chevron-right\"></i></button>"
						+ "</div>");
		
		out.println("</section></div><script src=\"../js/vendor/jquery.js\"></script><script src=\"../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
				
		
	}
	
	
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employeeID"));
		String[] task = request.getParameterValues("tasks");
		ArrayList<Integer> tasks = new ArrayList<Integer>();
		ArrayList<String> taskNames = new ArrayList<String>();
		
		String message = "";
		String employeeName = "";
				
		
		for(String s : task){
			String[] helper = s.split(";");
			tasks.add(Integer.parseInt(helper[0]));
			taskNames.add(helper[1]);
		}
		
		for(int i : tasks){
			try {
				con.newAssignment(i, employeeID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		Project project = null;
		try {
			project = con.getProject(projectID);
			System.out.println(project.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Employee employee = project.getSpecificEmployee(employeeID);
				
		employeeName = employee.getName();
		
				message = "<div class=\"row\">"
						+ "<div class=\"callout success\">"
						+ "<h5>" + employeeName + " has been added to the following tasks:</h5>";
				
				for (String s : taskNames){
					message += "<p>" + s + "</p>";
				}
				
				message += "</div></div>";
		
		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			//TODO Get ID from logged-in user
			employees = con.getAllEmployees(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		if (employees.isEmpty()){
			message = "<div class=\"row\">"
					+ "<div class=\"callout alert\">"
					+ "<h5>There are no employees you could add</h5>"
					+ "<p>Seems like you should create some employees first.</p>"
					+ "<a href=\"#\">Click here to add employees</a>"
					+ "</div></div>";
		}
		
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
				+ "<html>\n" 
					+ "<head>\n" 
						+ "<meta charset=\"UTF-8\">\n"
						+ "<title>Assign Employees</title>\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />\n"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />\n" 
					+ "</head>\n" 
					+ "<body>\n"
						+ "<div id=\"wrapper\">\n" 
							+ "<header>\n" 
								+ "<div class=\"row\">\n" 
									+ "<div class=\"small-8 medium-6 columns\">"
									+ "<h1>Assign Employees</h1><a href=\"Project?id=" + projectID + "\" class=\"back\">"
											+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>\n"
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
							+ message
							+ "<section>\n");
		out.println("<div class=\"row\">"
				+ "<form method=\"post\" action=\"assignEmployee/chooseTask\" data-abide novalidate>"
			
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose an employee.</p></div>"
				
				+ "<h3>Choose an employee you want to assign to a task.</h3></br>"
				+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				
				+ "<h5 class=\"small-12 medium-2 columns\">Employee</h5>"
				+ "<div class=\"small-12 medium-6 end columns\">"
				+ "<select name=\"employee\" required>"
				+ "<option></option>");
	
		for (Employee e : employees){
			out.println("<option value =\"" + e.getID() + "\">" + e.getName() + "</option>");
		}
				
		out.println("</select></div>"
						+ "</div>"
						+"<div class=\"row\">"
						+ "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Choose Task  <i class=\"fa fa-chevron-right\"></i></button>"
						+ "</div>");
		
		out.println("</section></div><script src=\"../js/vendor/jquery.js\"></script><script src=\"../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
				
		
	}
}
