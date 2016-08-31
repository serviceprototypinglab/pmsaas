package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/newProject")
public class NewProject extends HttpServlet {
	
	// Variables for the database connection
	String					url			= "jdbc:mysql://localhost:3306/";
	String					dbName		= "projectmanagement";
	String					userName	= "Janine";
	String					password	= "test123";
	
	// create a new DB connection
	private DBConnection	con			= new DBConnection(url, dbName, userName, password);
	
	// Variables for POST parameters
	private int				pID;
	private String			pName;
	private String			pShortname;
	private String			pBudget;
	private String			pCurrency;
	private String			pStart;
	private String			pEnd;
	private String			pPartners;
	private String			wpName[];
	private String			wpStart[];
	private String			wpEnd[];
	private String			taskName[];
	private String			taskStart[];
	private String			taskEnd[];
	private String			taskPM[];
	private String			taskBudget[];
	private String			taskWP[];
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">"
				
				+ "<title>New Project</title>"
				
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome/css/font-awesome.min.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />"
				
				+ "<script type=\"text/javascript\">function addWP(divName) {"
				+ "var newdiv = document.createElement('div'); "
				+ "newdiv.innerHTML = '<div class=\"small-4 columns\"><input type=\"text\" name=\"wpName\"></div><div class=\"small-4 columns\"><input type=\"date\" name=\"wpStart\"></div>	<div class=\"small-4 columns\"><input type=\"date\" name=\"wpEnd\"></div>'; document.getElementById(divName).appendChild(newdiv);}"
				
				+ "function addTask(divName) {var newdiv = document.createElement('div'); newdiv.innerHTML = '<div class=\"small-2 columns\"><input type=\"text\" name=\"taskName\"></div><div class=\"small-2 columns\"><input type=\"date\" name=\"taskStart\"></div><div class=\"small-2 columns\"><input type=\"date\" name=\"taskEnd\"></div><div class=\"small-2 columns\"><input type=\"number\" name=\"taskPM\"></div><div class=\"small-2 columns\"><input type=\"text\" name=\"taskBudget\"></div><div class=\"small-2 columns\"><input type=\"text\" name=\"taskWP\"></div>';document.getElementById(divName).appendChild(newdiv);}"
				+ "</script>"
				+ "</head><body>"
				
				+ "<div id=\"wrapper\"><header>"
				
				+ "<div class=\"row\">"
				+ "<div class=\"small-8 medium-6 columns\">"
				+ "<h1>New Project</h1>"
				+ "</div>"
				
				+ "<div class=\"small-12 medium-6 columns\">"
				+ "<div class=\"float-right menu\">"
				
				+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a> "
				+ "<a href=\"newProject\" class=\"button\">New Project</a> "
				+ "<a href=\"newEmployee\" class=\"button\">New Employee</a> "
				+ "<a href=\"help\" class=\"button\">Help</a> "
				+ "<a href=\"logout\" class=\"button\">Logout</a> "
				
				+ "</div></div></div></header>"
				
				+ "<section>"
				
				+ "<div class=\"row\">"
				+ "<h2 class=\"small-12\">Project</h2>"
				+ "</div>"
				
				+ "<form method=\"post\" action=\"newProject\" data-abide novalidate>"
				
				+ "<div class=\"row\">"
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Name</p>"
				+ "<div class=\"small-6 large-5 end columns\">"
				+ "<input type=\"text\" name=\"pName\" required>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Shortname</p>"
				+ "<div class=\"small-6 large-5 end columns\">"
				+ "<input type=\"text\" name=\"pShortname\">"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Total Budget</p>"
				+ "<div class=\"small-4 large-5 columns\">"
				+ "<input type=\"number\" name=\"pBudget\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 large-2 end columns\">"
				+ "<select name=\"pCurrency\" required>"
				+ "<option></option>"
				+ "<option value=\"CHF\">CHF</option>"
				+ "<option value=\"EUR\">EUR</option>"
				+ "</select></div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Duration<span class =\"grey\"></br>(YYYY-MM-DD)</span></p>"
				+ "<div class=\"small-4 large-5 end columns\">"
				+ "<input type=\"date\" name=\"pStart\" required>"
				+ "</div>"
				+ "<div class=\"small-4 large-5 end columns\">"
				+ "<input type=\"date\" name=\"pEnd\" required>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Partners</p>"
				+ "<div class=\"small-6 large-5 end columns\">"
				+ "<input type=\"text\" name=\"pPartners\">"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<h2 class=\"small-12\">Workpackages</h2>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 columns\">Name</p>"
				+ "<p class=\"small-4 columns\">Start<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				+ "<p class=\"small-4 columns\">End<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				
				+ "<div id=\"workpackage\">"
				
				+ "<div class=\"small-4 columns\">"
				+ "<input type=\"text\" name=\"wpName\" required>"
				+ "</div>"
				
				+ "<div class=\"small-4 columns\">"
				+ "<input type=\"date\" name=\"wpStart\" required>"
				+ "</div>"
				
				+ "<div class=\"small-4 columns\">"
				+ "<input type=\"date\" name=\"wpEnd\" required>"
				+ "</div></div>"
				
				+ "<i id=\"addWP\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addWP('workpackage')\"></i>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<h2 class=\"small-12\">Tasks</h2>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-2 columns\">Name</p>"
				+ "<p class=\"small-2 columns\">Start<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				+ "<p class=\"small-2 columns\">End<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				+ "<p class=\"small-2 columns\">PMs</p>"
				+ "<p class=\"small-2 columns\">Budget</p>"
				+ "<p class=\"small-2 columns\">Workpackage</p>"
				
				+ "<div id=\"task\">"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"text\" name=\"taskName\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"date\" name=\"taskStart\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"date\" name=\"taskEnd\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"number\" name=\"taskPM\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"number\" name=\"taskBudget\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"text\" name=\"taskWP\" required>"
				+ "</div></div>"
				
				+ "<i id=\"addTask\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addTask('task')\"></i>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Project\">"
				+ "</div>"
				
				+ "</form>"
				+ "</div>"
				+ "</section>"
				+ "<script src=\"js/vendor/jquery.js\"></script>"
				+ "<script src=\"js/vendor/foundation.min.js\"></script>"
				+ "<script>$(document).foundation();</script>"
				+ "</body></html>");
		
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		// get the parameters
		pName = request.getParameter("pName");
		pShortname = request.getParameter("pShortname");
		pBudget = request.getParameter("pBudget");
		pCurrency = request.getParameter("pCurrency");
		pStart = request.getParameter("pStart");
		pEnd = request.getParameter("pEnd");
		pPartners = request.getParameter("pPartners");
		wpName = request.getParameterValues("wpName");
		wpStart = request.getParameterValues("wpStart");
		wpEnd = request.getParameterValues("wpEnd");
		taskName = request.getParameterValues("taskName");
		taskStart = request.getParameterValues("taskStart");
		taskEnd = request.getParameterValues("taskEnd");
		taskPM = request.getParameterValues("taskPM");
		taskBudget = request.getParameterValues("taskBudget");
		taskWP = request.getParameterValues("taskWP");
		
		final PrintWriter out = response.getWriter();
				
		
		// create a new project in the DB
		try {
			pID = con.newProject(pName, pShortname, pBudget, pCurrency, pStart, pEnd, pPartners);
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// create the new workpackages in the DB
		for (int i = 0; i < wpName.length; ++i) {
			
			try {
				con.newWorkpackage(pID, wpName[i], wpStart[i], wpEnd[i]);
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// create the new tasks in the DB
		for (int i = 0; i < taskName.length; i++) {
			
			try {
				con.newTask(pID, taskWP[i], taskName[i], taskStart[i], taskEnd[i], taskPM[i], taskBudget[i]);
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		out.println("<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">"
				
				+ "<title>New Project</title>"
				
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome/css/font-awesome.min.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />"
				
				+ "<script type=\"text/javascript\">function addWP(divName) {"
				+ "var newdiv = document.createElement('div'); "
				+ "newdiv.innerHTML = '<div class=\"small-4 columns\"><input type=\"text\" name=\"wpName\"></div><div class=\"small-4 columns\"><input type=\"date\" name=\"wpStart\"></div>	<div class=\"small-4 columns\"><input type=\"date\" name=\"wpEnd\"></div>'; document.getElementById(divName).appendChild(newdiv);}"
				
				+ "function addTask(divName) {var newdiv = document.createElement('div'); newdiv.innerHTML = '<div class=\"small-2 columns\"><input type=\"text\" name=\"taskName\"></div><div class=\"small-2 columns\"><input type=\"date\" name=\"taskStart\"></div><div class=\"small-2 columns\"><input type=\"date\" name=\"taskEnd\"></div><div class=\"small-2 columns\"><input type=\"number\" name=\"taskPM\"></div><div class=\"small-2 columns\"><input type=\"text\" name=\"taskBudget\"></div><div class=\"small-2 columns\"><input type=\"text\" name=\"taskWP\"></div>';document.getElementById(divName).appendChild(newdiv);}"
				+ "</script>"
				+ "</head><body>"
				
				+ "<div id=\"wrapper\"><header>"
				
				+ "<div class=\"row\">"
				+ "<div class=\"small-8 medium-6 columns\">"
				+ "<h1>New Project</h1>"
				+ "</div>"
				
				+ "<div class=\"small-12 medium-6 columns\">"
				+ "<div class=\"float-right menu\">"
				
				+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a> "
				+ "<a href=\"newProject\" class=\"button\">New Project</a> "
				+ "<a href=\"newEmployee\" class=\"button\">New Employee</a> "
				+ "<a href=\"help\" class=\"button\">Help</a> "
				+ "<a href=\"logout\" class=\"button\">Logout</a> "
				
				+ "</div></div></div></header>"
				
				+ "<section>"

				+ "<div class=\"row\">"
				+ "<div class=\"callout success\">"
				+ "<h5>Project successfully created</h5>"
				+ "<p>The new project has succsessfully been created with the following data:</p>"
				+ "<p>Name: " + pName + "</p>"
				+ "<p>Shortname: " + pShortname + "</p>"
				+ "<p>Budget: " + pBudget + "</p>"
				+ "<p>Currency: " + pCurrency + ""
				+ "<p>Duration: " + pStart + " - " + pEnd + "</p>"
				+ "<p>Partners: " + pPartners + "</p>"
				+ "<a href=\"Overview/Project?id=" + pID + "\">Klick here to go to the project overview</a>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<h2 class=\"small-12\">Project</h2>"
				+ "</div>"
				
				+ "<form method=\"post\" action=\"newProject\" data-abide novalidate>"
				
				+ "<div class=\"row\">"
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				+ "</div></div>"
				

				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Name</p>"
				+ "<div class=\"small-6 large-5 end columns\">"
				+ "<input type=\"text\" name=\"pName\" required>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Shortname</p>"
				+ "<div class=\"small-6 large-5 end columns\">"
				+ "<input type=\"text\" name=\"pShortname\">"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Total Budget</p>"
				+ "<div class=\"small-4 large-5 columns\">"
				+ "<input type=\"number\" name=\"pBudget\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 large-2 end columns\">"
				+ "<select name=\"pCurrency\" required>"
				+ "<option></option>"
				+ "<option value=\"CHF\">CHF</option>"
				+ "<option value=\"EUR\">EUR</option>"
				+ "</select></div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Duration<span class =\"grey\"></br>(YYYY-MM-DD)</span></p>"
				+ "<div class=\"small-4 large-5 end columns\">"
				+ "<input type=\"date\" name=\"pStart\" required>"
				+ "</div>"
				+ "<div class=\"small-4 large-5 end columns\">"
				+ "<input type=\"date\" name=\"pEnd\" required>"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 large-2 columns\">Partners</p>"
				+ "<div class=\"small-6 large-5 end columns\">"
				+ "<input type=\"text\" name=\"pPartners\">"
				+ "</div></div>"
				
				+ "<div class=\"row\">"
				+ "<h2 class=\"small-12\">Workpackages</h2>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-4 columns\">Name</p>"
				+ "<p class=\"small-4 columns\">Start<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				+ "<p class=\"small-4 columns\">End<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				
				+ "<div id=\"workpackage\">"
				
				+ "<div class=\"small-4 columns\">"
				+ "<input type=\"text\" name=\"wpName\" required>"
				+ "</div>"
				
				+ "<div class=\"small-4 columns\">"
				+ "<input type=\"date\" name=\"wpStart\" required>"
				+ "</div>"
				
				+ "<div class=\"small-4 columns\">"
				+ "<input type=\"date\" name=\"wpEnd\" required>"
				+ "</div></div>"
				
				+ "<i id=\"addWP\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addWP('workpackage')\"></i>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<h2 class=\"small-12\">Tasks</h2>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<p class=\"small-2 columns\">Name</p>"
				+ "<p class=\"small-2 columns\">Start<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				+ "<p class=\"small-2 columns\">End<span class =\"grey\"> (YYYY-MM-DD)</span></p>"
				+ "<p class=\"small-2 columns\">PMs</p>"
				+ "<p class=\"small-2 columns\">Budget</p>"
				+ "<p class=\"small-2 columns\">Workpackage</p>"
				
				+ "<div id=\"task\">"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"text\" name=\"taskName\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"date\" name=\"taskStart\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"date\" name=\"taskEnd\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"number\" name=\"taskPM\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"number\" name=\"taskBudget\" required>"
				+ "</div>"
				
				+ "<div class=\"small-2 columns\">"
				+ "<input type=\"text\" name=\"taskWP\" required>"
				+ "</div></div>"
				
				+ "<i id=\"addTask\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addTask('task')\"></i>"
				+ "</div>"
				
				+ "<div class=\"row\">"
				+ "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Project\">"
				+ "</div>"
				
				+ "</form>"
				+ "</div>"
				+ "</section>"
				+ "<script src=\"js/vendor/jquery.js\"></script>"
				+ "<script src=\"js/vendor/foundation.min.js\"></script>"
				+ "<script>$(document).foundation();</script>"
				+ "</body></html>");
		
	}
}
