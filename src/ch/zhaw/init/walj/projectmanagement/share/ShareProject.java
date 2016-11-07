package ch.zhaw.init.walj.projectmanagement.share;

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
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, Page to assign employees (choose task)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Share")
public class ShareProject extends HttpServlet{
	
	// connection to database
	private DBConnection con = new DBConnection();
		
	
	@Override
	// method to handle post-requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		// variable declaration, get parameters
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (project.getLeader() == id){			

			ArrayList<Employee> employees = project.getEmployees();
			ArrayList<Employee> assignedEmployees = con.getSharedEmployees(project.getID());
			
			PrintWriter out = response.getWriter();
			
			// print HTML
			out.println(HTMLHeader.getInstance().getHeader("Share project", "../", "Share project", "", "<a href=\"Overview/Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"));
			// print HTML section with form
			out.println("<section>"
					  + "<div class=\"row\">"
					  + "<h3>Choose employees you want to share the project with:</h3>" 
					  + "<form method=\"post\" action=\"Share\" data-abide novalidate>"
				
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose at least one employee.</p></div>"
	
					  // project and employee
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
					  
					  // select employees
					  + "<label class=\"small-12 medium-6 end columns\">Employee "
					  + "<span class=\"grey\">multiple options possible</span> <select name=\"employees\" size=\"5\" multiple required>");
					
			// only tasks, the employee is not assigned to yet
			for (Employee e : employees){
				int i = 0;
				for (Employee emp : assignedEmployees){
					if (emp.getID() == e.getID()){
						i++;
					}
				}
				if (i == 0){
					out.println("<option value =\"" + e.getID() + "\">" + e.getName() + "</option>");
				}
			}
					
			out.println("</select></label></div>");
		
			// print return and submit buttons
			out.println("<div class=\"row\">"
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Share project\">"
					  + "</div>");
			
			out.println("</section>"
					  + "</div>"
					  + "<script src=\"../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
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
		
			String[] employeeIDs = request.getParameterValues("employees");
			ArrayList<Employee> employees = new ArrayList<Employee>();
			
			for (int i = 0; i < employeeIDs.length; i++){
				employees.add(con.getEmployee(Integer.parseInt(employeeIDs[i])));
			}
			
			String message = "";
	
			try {
				
				
				
				// create new assignment
				for (Employee e : employees) {
					con.newShare(project.getID(), e.getID());
				}
				// write success message
				message = "<div class=\"row\">" 
						+ "<div class=\"callout success\">" 
						+ "<h5>" 
						+ project.getName()
						+ " has been shared with the following employees:</h5>";
				for (Employee e : employees) {
					message += "<p>" + e.getName() + "</p>";
				}
				message += "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "#effort\">Click here to go to the project overview</a>"
						 + "</div>"
						 + "</div>";
	
			} catch (SQLException e) {
				// error message
				message = "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Something went wrong</h5>"
					    + "<p>The employee could not be assigned</p>" + "</div></div>";
			}
	
		
			PrintWriter out = response.getWriter();
	
			// print HTML
			out.println(HTMLHeader.getInstance().getHeader("Share Project", "../", "Share Project", "", "<a href=\"Overview/Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  + "<section>" 
					  + message
					  + "</section>"
					  + "</div>"
					  + "<script src=\"../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		} else {
	        String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);			
		}
	}
}
