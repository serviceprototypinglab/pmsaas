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
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;

/**
 * Projectmanagement tool, Page to assign employees (choose task)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/assignEmployee/chooseTask")
public class ChooseTask extends HttpServlet{

	// database access information
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	// connection to database
	private DBConnection con = new DBConnection(url, dbName, userName, password);
		
	
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
			
			int employeeID = Integer.parseInt(request.getParameter("employee"));
								
			ArrayList<ProjectTask> tasks = project.getTasks();
			ArrayList<Integer> assignedTasks = null;
			// get assignments
			try {
				assignedTasks = con.getAssignments(employeeID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
					
			
			PrintWriter out = response.getWriter();
			
			// print HTML
			out.println(HTMLHeader.getInstance().getHeader("Assign Employees)", "../../../", "Assign Employees", "<a href=\"../Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"));
			// print HTML section with form
			out.println("<section>"
					  + "<div class=\"row\">"
					  + "<h3>Choose Task(s)</h3>" 
					  + "<form method=\"post\" action=\"../assignEmployee\" data-abide novalidate>"
				
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose at least one task.</p></div>"
	
					  // project and employee ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
					  + "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">"
					  
					  // select tasks
					  + "<label class=\"small-12 medium-6 end columns\">Task "
					  + "<span class=\"grey\">multiple options possible</span> <select name=\"tasks\" size=\"5\" multiple required>");
					
			// only tasks, the employee is not assigned to yet
			for (ProjectTask task : tasks){
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
		
			// print return and submit buttons
			out.println("<div class=\"row\">"
						
					  + "<a href=\"../assignEmployee?projectID=" + projectID +"&employee=" + employeeID + "\" class=\"small-3 columns large button back-button\"><i class=\"fa fa-chevron-left\"></i>  Choose Employee</a>"
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Assign\">"
					  + "</div>");
			
			out.println("</section>"
					  + "</div>"
					  + "<script src=\"../../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}		
	}
}
