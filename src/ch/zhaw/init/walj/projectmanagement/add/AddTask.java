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
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Workpackage;

/**
 * project management tool, page to add tasks
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addTask")
public class AddTask extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con = new DBConnection();
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		int id = (int) request.getSession(false).getAttribute("ID");
		
		int pID = Integer.parseInt(request.getParameter("projectID"));
			
		Project project = null;
		try {
			project = con.getProject(pID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// get error/success message from msg attribute
		String message = "";
		if (request.getAttribute("msg") != null){
			message = (String) request.getAttribute("msg");
		}

		if (project.getLeader() == id){
			ArrayList<Workpackage> workpackages = project.getWorkpackages();
								
			out.println(HTMLHeader.getInstance().getHeader("Add Tasks", "../../", "Add Tasks", "", "<a href=\"Project?id=" + pID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  // HTML section with form
					  + "<section>"
					  + message
					  + "<div class=\"row\">"
					  + "<h2 class=\"small-12\"></h2>"// TODO Titel überlegen
					  + "</div>"
					  + "<form method=\"post\" action=\"addTask\" data-abide novalidate>"
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + pID + "\">"
					  + "<div class=\"row\">"
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
					  + "</div>"
					  + "</div>"
					  // fields for the tasks
					  + "<div class=\"row\">"
					  // labels
					  + "<p class=\"small-2 columns\">Name</p>"
					  + "<p class=\"small-2 columns\">Start<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
					  + "<p class=\"small-2 columns\">End<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
					  + "<p class=\"small-2 columns\">PMs</p>"
					  + "<p class=\"small-2 columns\">Budget</p>"
					  + "<p class=\"small-2 columns\">Workpackage</p>"
					  + "<div id=\"task\">"
					  + "<div class=\"small-2 columns\">"
					  // field for name
					  + "<input type=\"text\" name=\"taskName\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for start date
					  + "<input type=\"text\" name=\"taskStart\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for end date
					  + "<input type=\"text\" name=\"taskEnd\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for amount of PMs
					  + "<input type=\"number\" name=\"taskPM\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for the budget per task
					  + "<input type=\"number\" name=\"taskBudget\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field to assign the task to a workpackage
					  + "<select name=\"taskWP\" required>"
					  + "<option></option>");
			
			// option for every task
			for (Workpackage w : workpackages){
				out.println("<option value =\"" + w.getID() + "\">" 
								+ w.getName() + " (" + w.getStart() + " - " + w.getEnd() 
							+ ")</option>");
			}				
			
			out.println("</select>"
					  + "</div>"
					  // submit button
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Add Task\">"
					  + "</form>"
					  +	"</div>"
					  + "</section>"
					  // required JavaScript
					  + "<script src=\"../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body></html>");
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get the parameters
		int pID = Integer.parseInt(request.getParameter("projectID"));
		String taskName = request.getParameter("taskName");
		String taskStart = request.getParameter("taskStart");
		String taskEnd = request.getParameter("taskEnd");
		String taskPM = request.getParameter("taskPM");
		String taskBudget = request.getParameter("taskBudget");
		int taskWP = Integer.parseInt(request.getParameter("taskWP"));
		
		// get project
		Project project = null;
		try {
			project = con.getProject(pID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}

		if (project.getLeader() == id){
			try {	
				// create the new workpackages in the DB
				con.newTask(taskWP, taskName, taskStart, taskEnd, taskPM, taskBudget);
										
				String message = "<div class=\"callout success small-12 columns\">"
						  	   + "<h5>Task successfully created</h5>"
						  	   + "<p>The new task has succsessfully been created.</p>"
						  	   + "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + pID + "\">Click here to go back to the project overview</a>"
						  	   + "</div>";

				// send success message and call get method
				request.setAttribute("msg", message);
		        doGet(request, response);  
				
			} catch (SQLException e) {
				String message = "<div class=\"callout alert small-12 columns\">"
						       + "<h5>Task could not be created</h5>"
						       + "<p>An error occured and the task could not be created.</p>"
						       + "</div>";

				// send success message and call get method
				request.setAttribute("msg", message);
		        doGet(request, response);  
			}
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}
	}
}
