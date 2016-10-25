package ch.zhaw.init.walj.projectmanagement.add;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;

// TODO /** kommentare
@SuppressWarnings("serial")
@WebServlet("/Projects/newProject")
public class NewProject extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con = new DBConnection();
	
	// Variables for POST parameters
	private int pID;
	private String pName;
	private String pShortname;
	private String pBudget;
	private String pCurrency;
	private String pStart;
	private String pEnd;
	private String pPartners;
	private String wpName[];
	private String wpStart[];
	private String wpEnd[];
	private String taskName[];
	private String taskStart[];
	private String taskEnd[];
	private String taskPM[];
	private String taskBudget[];
	private String taskWP[];
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
				
		out.println("<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>New Project</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\">"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  // JavaScript functions to add fields for new Workpackages or new Tasks
				  + "<script type=\"text/javascript\">"
				  // add fields for Workpackages
				  + "function addWP(divName) {"
				  + "var newdiv = document.createElement('div'); "
				  + "newdiv.innerHTML = '<div class=\"small-4 columns\">"
				  					  + "<input type=\"text\" name=\"wpName\">"
				  					  + "</div>"
				  					  + "<div class=\"small-4 columns\">"
				  					  + "<input type=\"text\" name=\"wpStart\">"
				  					  + "</div>	"
				  					  + "<div class=\"small-4 columns\">"
				  					  + "<input type=\"text\" name=\"wpEnd\">"
				  					  + "</div>'; "
				  + "document.getElementById(divName).appendChild(newdiv);}"
				  // add fields for Tasks
				  + "function addTask(divName) {"
				  + "var newdiv = document.createElement('div'); "
				  + "newdiv.innerHTML = '<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskName\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskStart\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskEnd\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"number\" name=\"taskPM\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskBudget\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskWP\">"
				  					  + "</div>';"
				  + "document.getElementById(divName).appendChild(newdiv);}"
				  + "</script>"
				  + "</head>"
				  + "<body>"
				  + "<div id=\"wrapper\"><header>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-8 medium-8 columns\">"
				  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
				  // title
				  + "<h1>New Project</h1>"
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
				  + "<h2 class=\"small-12\">Project</h2>"
				  + "</div>"
				  + "<form method=\"post\" action=\"newProject\" data-abide novalidate>"
				  + "<div class=\"row\">"
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				  + "</div></div>"
				  // field for project name
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Name</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pName\" required>"
				  + "</div></div>"
				  // field for the short name of the project
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Shortname</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pShortname\">"
				  + "</div></div>"
				  // fields for total budget and to select the currency (EUR or CHF)
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
				  // fields for duration of the project
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Duration<span class =\"grey\"></br>(dd.mm.yyyy)</span></p>"
				  + "<div class=\"small-4 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pStart\" required>"
				  + "</div>"
				  + "<div class=\"small-4 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pEnd\" required>"
				  + "</div></div>"
				  // fields for the partners
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Partners</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pPartners\">"
				  + "</div></div>"
				  // fields for the Workpackages
				  + "<div class=\"row\">"
				  + "<h2 class=\"small-12\">Workpackages</h2>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // labels
				  + "<p class=\"small-4 columns\">Name</p>"
				  + "<p class=\"small-4 columns\">Start<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<p class=\"small-4 columns\">End<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<div id=\"workpackage\">"
				  + "<div class=\"small-4 columns\">"
				  // field for name
				  + "<input type=\"text\" name=\"wpName\" required>"
				  + "</div>"
				  + "<div class=\"small-4 columns\">"
				  // field for start
				  + "<input type=\"text\" name=\"wpStart\" required>"
				  + "</div>"
				  + "<div class=\"small-4 columns\">"
				  // field for end
				  + "<input type=\"text\" name=\"wpEnd\" required>"
				  + "</div></div>"
				  // button to add more fields
				  + "<i id=\"addWP\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addWP('workpackage')\"></i>"
				  + "</div>"
				  // fields for the tasks
				  + "<div class=\"row\">"
				  + "<h2 class=\"small-12\">Tasks</h2>"
				  + "</div>"
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
				  + "<input type=\"text\" name=\"taskWP\" required>"
				  + "</div></div>"
				  // button to add more fields
				  + "<i id=\"addTask\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addTask('task')\"></i>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // submit button
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Project\">"
				  + "</div>"
				  + "</form>"
				  +	"</div>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body></html>");
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		int id = (int) request.getSession(false).getAttribute("ID");
		
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
		

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sDate = format.parse(pStart);
			Date eDate = format.parse(pEnd);
			pStart = format2.format(sDate);
			pEnd = format2.format(eDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
				
		String message = "";
		
		try {
			
			// create a new project in the DB
			pID = con.newProject(pName, pShortname, id,  pBudget, pCurrency, pStart, pEnd, pPartners);
			
			// create the new workpackages in the DB
			for (int i = 0; i < wpName.length; ++i) {

				Date sDate = format.parse(wpStart[i]);
				Date eDate = format.parse(wpEnd[i]);
				String wpStart = format2.format(sDate);
				String wpEnd = format2.format(eDate);
				
				con.newWorkpackage(pID, wpName[i], wpStart, wpEnd);
			}
			
			// create the new tasks in the DB
			for (int i = 0; i < taskName.length; i++) {

				Date sDate = format.parse(taskStart[i]);
				Date eDate = format.parse(taskEnd[i]);
				String taskStart = format2.format(sDate);
				String taskEnd = format2.format(eDate);
				
				con.newTask(pID, taskWP[i], taskName[i], taskStart, taskEnd, taskPM[i], taskBudget[i]);
			}
						
			message = "<div class=\"callout success\">"
					  + "<h5>Project successfully created</h5>"
					  + "<p>The new project has succsessfully been created with the following data:</p>"
					  + "<p>Name: " + pName + "</p>"
					  + "<p>Shortname: " + pShortname + "</p>"
					  + "<p>Budget: " + pBudget + "</p>"
					  + "<p>Currency: " + pCurrency + ""
					  + "<p>Duration: " + pStart + " - " + pEnd + "</p>"
					  + "<p>Partners: " + pPartners + "</p>"
					  + "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + pID + "\">Click here to go to the project overview</a>"
					  + "</div>";
			
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException | ParseException e) {
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
					  + "<h5>Project could not be created</h5>"
					  + "<p>An error occured and the project could not be created.</p>"
					  + "</div>";
		}
				
		out.println("<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>New Project</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\">"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  // JavaScript functions to add fields for new Workpackages or new Tasks
				  + "<script type=\"text/javascript\">"
				  // add fields for Workpackages
				  + "function addWP(divName) {"
				  + "var newdiv = document.createElement('div'); "
				  + "newdiv.innerHTML = '<div class=\"small-4 columns\">"
				  					  + "<input type=\"text\" name=\"wpName\">"
				  					  + "</div>"
				  					  + "<div class=\"small-4 columns\">"
				  					  + "<input type=\"text\" name=\"wpStart\">"
				  					  + "</div>	"
				  					  + "<div class=\"small-4 columns\">"
				  					  + "<input type=\"text\" name=\"wpEnd\">"
				  					  + "</div>'; "
				  + "document.getElementById(divName).appendChild(newdiv);}"
				  // add fields for Tasks
				  + "function addTask(divName) {"
				  + "var newdiv = document.createElement('div'); "
				  + "newdiv.innerHTML = '<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskName\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskStart\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskEnd\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"number\" name=\"taskPM\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskBudget\">"
				  					  + "</div>"
				  					  + "<div class=\"small-2 columns\">"
				  					  + "<input type=\"text\" name=\"taskWP\">"
				  					  + "</div>';"
				  + "document.getElementById(divName).appendChild(newdiv);}"
				  + "</script>"
				  + "</head>"
				  + "<body>"
				  + "<div id=\"wrapper\"><header>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-8 medium-8 columns\">"
				  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
				  // title
				  + "<h1>New Project</h1>"
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
				  + "</div>"
				  + "<div class=\"row\">"
				  + "<h2 class=\"small-12\">Project</h2>"
				  + "</div>"
				  + "<form method=\"post\" action=\"newProject\" data-abide novalidate>"
				  + "<div class=\"row\">"
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				  + "</div></div>"
				  // field for project name
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Name</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pName\" required>"
				  + "</div></div>"
				  // field for the short name of the project
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Shortname</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pShortname\">"
				  + "</div></div>"
				  // fields for total budget and to select the currency (EUR or CHF)
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
				  // fields for duration of the project
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Duration<span class =\"grey\"></br>(dd.mm.yyyy)</span></p>"
				  + "<div class=\"small-4 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pStart\" required>"
				  + "</div>"
				  + "<div class=\"small-4 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pEnd\" required>"
				  + "</div></div>"
				  // fields for the partners
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Partners</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pPartners\">"
				  + "</div></div>"
				  // fields for the Workpackages
				  + "<div class=\"row\">"
				  + "<h2 class=\"small-12\">Workpackages</h2>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // labels
				  + "<p class=\"small-4 columns\">Name</p>"
				  + "<p class=\"small-4 columns\">Start<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<p class=\"small-4 columns\">End<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<div id=\"workpackage\">"
				  + "<div class=\"small-4 columns\">"
				  // field for name
				  + "<input type=\"text\" name=\"wpName\" required>"
				  + "</div>"
				  + "<div class=\"small-4 columns\">"
				  // field for start
				  + "<input type=\"text\" name=\"wpStart\" required>"
				  + "</div>"
				  + "<div class=\"small-4 columns\">"
				  // field for end
				  + "<input type=\"text\" name=\"wpEnd\" required>"
				  + "</div></div>"
				  // button to add more fields
				  + "<i id=\"addWP\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addWP('workpackage')\"></i>"
				  + "</div>"
				  // fields for the tasks
				  + "<div class=\"row\">"
				  + "<h2 class=\"small-12\">Tasks</h2>"
				  + "</div>"
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
				  + "<input type=\"text\" name=\"taskWP\" required>"
				  + "</div></div>"
				  // button to add more fields
				  + "<i id=\"addTask\" class=\"fa fa-plus small-1 columns button add float-left\" aria-hidden=\"true\" onclick=\"addTask('task')\"></i>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // submit button
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Project\">"
				  + "</div>"
				  + "</form>"
				  +	"</div>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
}
