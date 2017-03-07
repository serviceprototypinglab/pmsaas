package ch.zhaw.init.walj.projectmanagement.admin.properties;

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
 * Projectmanagement tool, Page to archive projects
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/admin/restoreProject")
public class RestoreProject extends HttpServlet {

	// connection to database
	private DBConnection con;

	/*
	 *	method to handle get requests
	 *	archives the project and shows success/error message to the user   
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user and project ID
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

		String message = "";
		try {
			// set archive flag for project in database
			con.restoreProject(projectID);
			// success message
			message = "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The Project " + project.getName() + " has sucessfully been restored.</h2>"
					+ "<a href=\"/Projektverwaltung/admin/properties\">"
					+ "<i class=\"fa fa-chevron-left fa-4x\" aria-hidden=\"true\"></i></br>"
					+ "Click here to go back to the properties page</a>"
					+ "</div>"
					+ "</div>";
		} catch (SQLException e) {
			// error message
			message =  "<div class=\"row\">"
					+ "<div class=\"small-12 columns align-center\">" 
				    + "<h2>The Project " + project.getName() + " could not be restored</h2>"
					+ "<a href=\"/Projektverwaltung/admin/properties\">"
					+ "<i class=\"fa fa-chevron-left fa-4x\" aria-hidden=\"true\"></i></br>"
					+ "Click here to go back to the properties page</a>"
					+ "</div>"
					+ "</div>";			
		}

		// Print HTML
		out.println(HTMLHeader.getInstance().printHeader("Archive Project", "../", "Archive Project", "", "", true)
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
		
	}
}
