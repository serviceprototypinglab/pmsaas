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
import ch.zhaw.init.walj.projectmanagement.util.Project;

// TODO datum normale eingabe
// TODO /** kommentare
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addWorkpackage")
public class AddWorkpackage extends HttpServlet {
	
	// Variables for the database connection
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	// create a new DB connection
	private DBConnection con = new DBConnection(url, dbName, userName, password);
	
	// Variables for POST parameters
	private int pID;
	private String wpName[];
	private String wpStart[];
	private String wpEnd[];
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		pID = Integer.parseInt(request.getParameter("projectID"));
		
		Project project = null;
		try {
			project = con.getProject(pID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (project.getLeader() == id){
		
			PrintWriter out = response.getWriter();
					
			out.println("<!DOCTYPE html>"
					  + "<html>"
					  // HTML head
					  + "<head>"
					  + "<meta charset=\"UTF-8\">"
					  + "<title>Add Workpackage</title>"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\">"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />"
					  + "</head>"
					  + "<body>"
					  + "<div id=\"wrapper\"><header>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-8 columns\">"
					  + "<img src=\"../../img/logo_small.png\" class=\"small-img left\">"
					  // title
					  + "<h1>Add Workpackage</h1><a href=\"Project?id=" + pID + "\" class=\"back\">"
					  + "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"
					  + "</div>"
					  // menu
					  + "<div class=\"small-12 medium-4 columns\">"
					  + "<div class=\"float-right menu\">"
					  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\" title=\"All Projects\"><i class=\"fa fa-list fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\" title=\"New Project\"><i class=\"fa fa-file fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/employee\" class=\"button\" title=\"My Profile\"><i class=\"fa fa-user fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\" title=\"Help\"><i class=\"fa fa-book fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw\"></i></a> "
					  + "</div>"
					  + "</div>"
					  + "</div>"
					  + "</header>"
					  // HTML section with form
					  + "<section>"
					  + "<div class=\"row\">"
					  + "<h2 class=\"small-12\"></h2>"// TODO Titel überlegen
					  + "</div>"
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		pID = Integer.parseInt(request.getParameter("projectID"));
		
		Project project = null;
		try {
			project = con.getProject(pID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (project.getLeader() == id){
		
			// get the parameters
			wpName = request.getParameterValues("wpName");
			wpStart = request.getParameterValues("wpStart");
			wpEnd = request.getParameterValues("wpEnd");
					
			final PrintWriter out = response.getWriter();
					
			String message = "";
			
			try {	
				// create the new workpackages in the DB
				for (int i = 0; i < wpName.length; ++i) {
					con.newWorkpackage(pID, wpName[i], wpStart[i], wpEnd[i]);
				}
										
				message = "<div class=\"callout success small-12 columns\">"
						  + "<h5>Workpackage successfully created</h5>"
						  + "<p>The new workpackage has succsessfully been created.</p>"
						  + "<a href=\"/Projektverwaltung/Projects/Overview/addTask?projectID=" + pID + "\">Click here to add tasks</a>"
						  + "</div>";
				
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
				message = "<div class=\"callout alert small-12 columns\">"
						  + "<h5>Workpackage could not be created</h5>"
						  + "<p>An error occured and the workpackage could not be created.</p>"
						  + "</div>";
			}
					
			out.println("<!DOCTYPE html>"
					  + "<html>"
					  // HTML head
					  + "<head>"
					  + "<meta charset=\"UTF-8\">"
					  + "<title>New Project</title>"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\">"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />"
					  + "</head>"
					  + "<body>"
					  + "<div id=\"wrapper\"><header>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-8 columns\">"
					  + "<img src=\"../../img/logo_small.png\" class=\"small-img left\">"
					  // title
					  + "<h1>Add Workpackage</h1><a href=\"Project?id=" + pID + "\" class=\"back\">"
					  + "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"
					  + "</div>"
					  // menu
					  + "<div class=\"small-12 medium-4 columns\">"
					  + "<div class=\"float-right menu\">"
					  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\" title=\"All Projects\"><i class=\"fa fa-list fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\" title=\"New Project\"><i class=\"fa fa-file fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/employee\" class=\"button\" title=\"My Profile\"><i class=\"fa fa-user fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\" title=\"Help\"><i class=\"fa fa-book fa-fw\"></i></a> "
					  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw\"></i></a> "
					  + "</div>"
					  + "</div>"
					  + "</header>"
					  // HTML section with form
					  + "<section>"
					  + "<div class=\"row\">"
					  + message
					  + "<h2 class=\"small-12 columns\">Add Workpackage</h2>"// TODO Titel überlegen
					  + "</div>"
					  + "<form method=\"post\" action=\"addWorkpackage\" data-abide novalidate>"
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + pID + "\">"
					  + "<div class=\"row\">"
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
					  + "</div></div>"
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
					  +	"</div>"
					  + "</form>"
					  +	"</div>"
					  + "</section>"
					  // required JavaScript
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
}
