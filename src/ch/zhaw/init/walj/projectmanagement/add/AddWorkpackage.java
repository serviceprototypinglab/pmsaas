package ch.zhaw.init.walj.projectmanagement.add;

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
 * project management tool, page to add workpackages
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addWorkpackage")
public class AddWorkpackage extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con = new DBConnection();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
		int pID = Integer.parseInt(request.getParameter("projectID"));
		
		// get project
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
			
		// check if user is project leader
		if (project.getLeader() == id){
		
			// write HTML
			out.println(HTMLHeader.getInstance().printHeader("Add Workpackages", "../../", "Add Workpackages", "", "<a href=\"Project?id=" + pID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  // HTML section with form
					  + "<section>"
					  + message 
					  + "<form method=\"post\" action=\"addWorkpackage\" data-abide novalidate>"
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + pID + "\">"
					  + "<div class=\"row\">"
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
					  + "</div>"
					  + "</div>"
					  // fields for the Workpackages
					  + "<div class=\"row\">"
					  // labels
					  + "<p class=\"small-4 columns\">Name</p>"
					  + "<p class=\"small-4 columns\">Start<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
					  + "<p class=\"small-4 columns\">End<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
					  + "<div id=\"workpackage\">"
					  // field for name
					  + "<div class=\"small-4 columns\">"
					  + "<input type=\"text\" name=\"wpName\" required>"
					  + "</div>"
					  // field for start
					  + "<div class=\"small-4 columns\">"
					  + "<input type=\"text\" name=\"wpStart\" required>"
					  + "</div>"
					  // field for end
					  + "<div class=\"small-4 columns\">"
					  + "<input type=\"text\" name=\"wpEnd\" required>"
					  + "</div></div>"
					  // submit button
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Add Workpackage\">"
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");

		// get user and project ID
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
		
		if (project.getLeader() == id){
		
			// get the parameters
			String wpName[] = request.getParameterValues("wpName");
			String wpStart[] = request.getParameterValues("wpStart");
			String wpEnd[] = request.getParameterValues("wpEnd");
			
			try {	
				// create the new workpackages in the DB
				for (int i = 0; i < wpName.length; ++i) {
					con.newWorkpackage(pID, wpName[i], wpStart[i], wpEnd[i]);
				}
										
				String message = "<div class=\"callout success small-12 columns\">"
						  + "<h5>Workpackage successfully created</h5>"
						  + "<p>The new workpackage has succsessfully been created.</p>"
						  + "<a href=\"/Projektverwaltung/Projects/Overview/addTask?projectID=" + pID + "\">Click here to add tasks</a>"
						  + "</div>";
				
				// send error message and call get method
				request.setAttribute("msg", message);
		        doGet(request, response); 	

			} catch (SQLException e) {

				String message = "<div class=\"callout alert small-12 columns\">"
						  + "<h5>Workpackage could not be created</h5>"
						  + "<p>An error occured and the workpackage could not be created.</p>"
						  + "</div>";
				
				// send error message and call get method
				request.setAttribute("msg", message);
		        doGet(request, response); 
			}
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}
	}
}
