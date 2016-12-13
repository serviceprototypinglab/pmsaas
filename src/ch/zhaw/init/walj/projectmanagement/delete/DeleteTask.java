package ch.zhaw.init.walj.projectmanagement.delete;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, Page to delete tasks
 * 
 * @author Janine Walther, ZHAW
 * 
 */

@SuppressWarnings("serial")
@WebServlet("/Projects/deleteTask")
public class DeleteTask extends HttpServlet {

	// connection to database
	private DBConnection con = new DBConnection();

	/*
	 *	method to handle get requests
	 *	deletes the task and shows success/error message to the user   
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get project given from get-parameter projectID
		int taskID = Integer.parseInt(request.getParameter("taskID"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int userID =  (int) request.getSession(false).getAttribute("ID");
		
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
		if (project.getLeader() == userID){
					
			String message = "";
			try {
				// delete project from database
				con.deleteTask(taskID);
				// success message
				message = "<div class=\"row\">"
						+ "<div class=\"small-12 columns align-center\">" 
					    + "<h2>The task has sucessfully been deleted.</h2>"
						+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#tasks\">Click here to go back to the edit page</a>"
						+ "<br>"
						+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
						+ "</div>"
						+ "</div>";
			} catch (SQLException e) {
				// error message
				message = "<div class=\"row\">"
						+ "<div class=\"small-12 columns align-center\">" 
					    + "<h2>The task could not be deleted</h2>"
						+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#tasks\">Click here to go back to the edit page</a>"
						+ "<br>"
						+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
						+ "</div>"
						+ "</div>";			
			}

			// Print HTML
			out.println(HTMLHeader.getInstance().printHeader("Delete Task", "../", "Delete Task", "")
					  + "<section>"
					  + message
					  + "</section>"
					  + HTMLFooter.getInstance().printFooter(false)
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
