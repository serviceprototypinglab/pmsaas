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
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

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

	/*
	 * method to handle get requests
	 * Form to assign employees to tasks,
	 * choose an employee
	 * calls assignEmployee/chooseTask afterwards
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get projectID given from parameter
		int projectID = Integer.parseInt(request.getParameter("projectID"));

		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = "/Projektverwaltung/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == id){
			
			// prepare success/error message
			String message = "";
			if (request.getAttribute("msg") != null){
				message = (String) request.getAttribute("msg");
			}
			
			// get all employees
			ArrayList<Employee> employees = new ArrayList<Employee>();
			try {
				employees.addAll(con.getAllEmployees());
			} catch (SQLException e) {
			}
	
			// print HTML header
			out.println(HTMLHeader.getInstance().printHeader("Assign Employees", "../../", "Assign Employees", "", "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"));
			
			// print HTML section with form
			out.println("<section>"
					  + message
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
				out.println("<option value =\"" + employee.getID() + "\">" + employee.getFullName() + "</option>");
			}
	
			// submit button
			out.println("</select></div>" 
						+ "</div>" 
						+ "<div class=\"row\">"
						+ "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Choose Task  <i class=\"fa fa-chevron-right\"></i></button>"
						+ "</div>"
						+ "</section>"
						+ HTMLFooter.getInstance().printFooter(false)
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

	/*
	 * method to handle post requests
	 * adds new assignment to database 
	 * calls get method with 
	 * success/error message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
		int projectID = Integer.parseInt(request.getParameter("projectID"));

		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = "/Projektverwaltung/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == id){
		
			// get parameters
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
	
			try {
				// create new assignment
				for (int i : taskIDs) {
					con.newAssignment(i, employeeID);
				}
	
				// load project again with the new assignment
				project = con.getProject(projectID);
				
				// get the assigned employee
				Employee employee = project.getSpecificEmployee(employeeID);
	
				// set success message
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
				request.setAttribute("msg", message);
	            
			} catch (SQLException e) {
				// set error message
				message = "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Something went wrong</h5>"
					    + "<p>The employee could not be assigned</p>" + "</div></div>";
				request.setAttribute("msg", message);
			}
			// call get method
            doGet(request, response); 
	
		} else {
	        String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);			
		}
	}
}
