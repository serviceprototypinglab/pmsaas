package ch.zhaw.init.walj.projectmanagement.add;

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
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Project;

/**
 * Projectmanagement tool, Page to assign employees
 * 
 * @author Janine Walther, ZHAW
 *
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/assignEmployee")
public class AssignEmployee extends HttpServlet {

	// Database connection
	private DBConnection con = new DBConnection();

	@Override
	// method to handle get-requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get projectID given from parameter
		int projectID = Integer.parseInt(request.getParameter("projectID"));

		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (project.getLeader() == id){
			ArrayList<Employee> employees = new ArrayList<Employee>();
			try {
				employees = con.getAllEmployees(id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			PrintWriter out = response.getWriter();
	
			// print HTML
			out.println(HTMLHeader.getInstance().getHeader("Assign Employees", "../../", "Assign Employees", "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"));
			
			// print HTML section with form
			out.println("<section>"
					+ "<div class=\"row\">"
					  + "<form method=\"get\" action=\"assignEmployee/chooseTask\" data-abide novalidate>"
	
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose an employee.</p></div>"
	
					  + "<h3>Choose an employee you want to assign to a task.</h3></br>"
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
	
					  // select employee
					  + "<h5 class=\"small-12 medium-2 columns\">Employee</h5>"
					  + "<div class=\"small-12 medium-6 end columns\">" 
					  + "<select name=\"employee\" required>"
					  + "<option></option>");
	
			// Option for every Employee
			for (Employee employee : employees) {
				out.println("<option value =\"" + employee.getID() + "\">" + employee.getName() + "</option>");
			}
	
			// submit button
			out.println("</select></div>" 
						+ "</div>" 
						+ "<div class=\"row\">"
						+ "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Choose Task  <i class=\"fa fa-chevron-right\"></i></button>"
						+ "</div>");
	
			out.println("</section>"
					  + "</div>"
					  + "<script src=\"../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		} else {
	        String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);			
		}

	}

	@Override
	// method to handle post-requests
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		int id = (int) request.getSession(false).getAttribute("ID");

		// variable declaration and get the parameters
		int projectID = Integer.parseInt(request.getParameter("projectID"));

		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (project.getLeader() == id){
		
			int employeeID = Integer.parseInt(request.getParameter("employeeID"));
			String[] task = request.getParameterValues("tasks");
			ArrayList<Integer> taskIDs = new ArrayList<Integer>();
			ArrayList<String> taskNames = new ArrayList<String>();
			
			String message = "";
	
			// Split the task string in name and ID
			for (String s : task) {
				String[] helper = s.split(";");
				taskIDs.add(Integer.parseInt(helper[0]));
				taskNames.add(helper[1]);
			}
	
			Employee employee = null;
	
			try {
				// create new assignment
				for (int i : taskIDs) {
					con.newAssignment(i, employeeID);
				}
	
				// get the assigned employee
				employee = project.getSpecificEmployee(employeeID);
	
				// write success message
				message = "<div class=\"row\">" 
						+ "<div class=\"callout success\">" 
						+ "<h5>" 
						+ employee.getName()
						+ " has been added to the following tasks:</h5>";
				for (String s : taskNames) {
					message += "<p>" + s + "</p>";
				}
				message += "</div>"
						 + "</div>";
	
			} catch (SQLException e) {
				// error message
				message = "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Something went wrong</h5>"
					    + "<p>The employee could not be assigned</p>" + "</div></div>";
			}
	
			// get all employees
			ArrayList<Employee> employees = new ArrayList<Employee>();
			try {
				employees = con.getAllEmployees(id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			PrintWriter out = response.getWriter();
	
			// print HTML
			out.println("<!DOCTYPE html>" 
					  + "<html>" 
					  // HTML head
					  + "<head>" 
					  + "<meta charset=\"UTF-8\">"
					  + "<title>Assign Employees</title>"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />"
					  + "</head>" 
					  // HTML body
					  + "<body>" 
					  + "<div id=\"wrapper\">" 
					  + "<header>" 
					  + "<div class=\"row\">"
					  + "<div class=\"small-8 columns\">"
					  + "<img src=\"../../img/logo_small.png\" class=\"small-img left\">"
					  // title
					  + "<h1>Assign Employees</h1><a href=\"Project?id=" + projectID + "\" class=\"back\">"
					  + "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>"
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
					  + message);
			
			// print HTML section with form
			out.println("<div class=\"row\">"
					  + "<form method=\"post\" action=\"assignEmployee/chooseTask\" data-abide novalidate>"
					  
	   				  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose an employee.</p></div>"
	
					  // project ID
					  + "<h3>Choose an employee you want to assign to a task.</h3></br>"
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
	
					  // select employee
					  + "<h5 class=\"small-12 medium-2 columns\">Employee</h5>"
					  + "<div class=\"small-12 medium-6 end columns\">" + "<select name=\"employee\" required>"
					  + "<option></option>");
	
			// print option for every employee
			for (Employee e : employees) {
				out.println("<option value =\"" + e.getID() + "\">" + e.getName() + "</option>");
			}
	
			// submit button
			out.println("</select></div>" 
					  + "</div>" + "<div class=\"row\">"
					  + "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Choose Task  <i class=\"fa fa-chevron-right\"></i></button>"
					  + "</div>"
					  + "</section>"
					  + "</div>"
					  + "<script src=\"../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		} else {
	        String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);			
		}
	}
}
