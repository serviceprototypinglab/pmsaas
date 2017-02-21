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
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task;

/**
 * Projectmanagement tool, Page to assign employees (choose task)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/assignEmployee/chooseTask")
public class ChooseTask extends HttpServlet{
	
	// connection to database
	private DBConnection con;

	/*
	 * method to handle get requests
	 * Form to assign employees to tasks, 
	 * choose tasks
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		int id = (int) request.getSession(false).getAttribute("ID");
		
		// variable declaration, get parameters
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == id){
			
			// get parameters
			int employeeID = Integer.parseInt(request.getParameter("employee"));
			ArrayList<Task> tasks = project.getTasks();
			ArrayList<Integer> assignedTasks = null;

			assignedTasks = con.getAssignedTasks(employeeID);
			
			// print HTML header
			out.println(HTMLHeader.getInstance().printHeader("Assign Employees", "../../../", "Assign Employees", "", "<a href=\"../Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false));
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
					  + "<span class=\"grey\">multiple options possible</span> <select name=\"tasks\" size=\"10\" multiple required>");
					
			// print all tasks, where the employee is not assigned to
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
					
			out.println("</select>"
					  + "</label>"
					  + "</div>");
		
			// print return and submit buttons
			out.println("<div class=\"row\">"
						
					  + "<a href=\"../assignEmployee?projectID=" + projectID +"&employee=" + employeeID + "\" class=\"small-3 columns large button back-button\"><i class=\"fa fa-chevron-left\"></i>  Choose Employee</a>"
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Assign\">"
					  + "</div>");
			
			// print required javascript
			out.println("</section>"
					  + HTMLFooter.getInstance().printFooter(false)
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
