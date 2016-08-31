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
@WebServlet("/Overview/assignEmployee/chooseTask")
public class ChooseTask extends HttpServlet{

	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	private DBConnection con = new DBConnection(url, dbName, userName, password);
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employee"));
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		
		ArrayList<Task> tasks = project.getTasks();
		ArrayList<Integer> assignedTasks = null;
		try {
			assignedTasks = con.getAssignments(employeeID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String message = "";
		
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
				+ "<html>\n" 
					+ "<head>\n" 
						+ "<meta charset=\"UTF-8\">\n"
						+ "<title>Assign Employees</title>\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />\n"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />\n" 
					+ "</head>\n" 
					+ "<body>\n"
						+ "<div id=\"wrapper\">\n" 
							+ "<header>\n" 
								+ "<div class=\"row\">\n" 
									+ "<div class=\"small-8 medium-6 columns\">"
									+ "<h1>Assign Employees</h1><a href=\"../Project?id=" + projectID + "\" class=\"back\">"
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
				+ "<form method=\"post\" action=\"../assignEmployee\" data-abide novalidate>"
			
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose at least one task.</p></div>"

				+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				+ "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">"
				+ "<label class=\"small-12 medium-6 end columns\">Task "
				+ "<span class=\"grey\">multiple options possible</span> <select name=\"tasks\" size=\"5\" multiple required>");
				
				for (Task task : tasks){
					int i = 0;
					for (int assignedTask : assignedTasks){
						if (task.getID() == assignedTask){
							i++;
						}
					}
					if (i == 0){
						out.println("<option value =\"" + task.getID() + ";" + task.getName() + "\">" + task.getName() + "</option>");
					}
				}
						
				out.println("</select></label></div>");
	
		out.println("<div class=\"row\">"
				+ "<a href=\"../assignEmployee?projectID=" + projectID +"&employee=" + employeeID + "\" class=\"small-3 columns large button back-button\"><i class=\"fa fa-chevron-left\"></i>  Choose Employee</a>"
				+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Assign\">"
				+ "</div>");
		
		out.println("</section></div><script src=\"../../js/vendor/jquery.js\"></script><script src=\"../../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
			
		
	}
}
