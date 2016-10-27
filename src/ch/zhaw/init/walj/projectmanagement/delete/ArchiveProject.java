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
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, Page to archive projects
 * 
 * @author Janine Walther, ZHAW
 * 
 */

@SuppressWarnings("serial")
@WebServlet("/Projects/archiveProject")
public class ArchiveProject extends HttpServlet {

	// connection to database
	private DBConnection con = new DBConnection();

	@Override
	// method to handle get-requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		// get project given from get-parameter projectID
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		String message = "";

		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
		}

		// delete project from database
		try {
			con.archiveProject(projectID);
			// success message
			message = "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The Project " + project.getName() + " has sucessfully been archived.</h2>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview\">"
					+ "<i class=\"fa fa-chevron-left fa-4x\" aria-hidden=\"true\"></i></br>"
					+ "Click here to go back to overview</a>"
					+ "</div>"
					+ "</div>";
		} catch (SQLException e) {
			// error message
			message =  "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The Project " + project.getName() + " could not be archived</h2>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + project.getID() + "\">"
					+ "<i class=\"fa fa-chevron-left fa-4x\" aria-hidden=\"true\"></i></br>"
					+ "Click here to go back to project</a>"
					+ "</div>"
					+ "</div>";			
		}
		
		PrintWriter out = response.getWriter();

		// Print HTML head and header
		out.println(HTMLHeader.getInstance().getHeader("Archive Project", "../", "Archive Project", "")
				  + "<section>"
				  + message
				  + "</section>"
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
}
