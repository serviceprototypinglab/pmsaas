/**
 *	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 *	All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

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
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;

/**
 * project management tool, page to add projects
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/newProject")
public class AddProject extends HttpServlet {
			
	@Override
	/*
	 * method to handle get requests
	 * form to create a new project
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get error/success message from msg attribute
		String message = "";
		if (request.getAttribute("msg") != null){
			message = (String) request.getAttribute("msg");
		}
		
		// JavaScript functions to add fields for new Workpackages or new Tasks
		String script = "<script type=\"text/javascript\">"
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
					  + "</script>";		
		
		// print HTML
		out.println(HTMLHeader.getInstance().printHeader("New Project", "../", "New Project", script, "", false)
				  + "<section>"
				  + message
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
				  + "<p class=\"small-4 large-2 columns\">Name*</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pName\" required>"
				  + "</div></div>"
				  // field for the short name of the project
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Shortname*</p>"
				  + "<div class=\"small-6 large-5 end columns\">"
				  + "<input type=\"text\" name=\"pShortname\">"
				  + "</div></div>"
				  // fields for total budget and to select the currency (EUR or CHF)
				  + "<div class=\"row\">"
				  + "<p class=\"small-4 large-2 columns\">Total Budget*</p>"
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
				  + "<p class=\"small-4 large-2 columns\">Duration*<span class =\"grey\"></br>(dd.mm.yyyy)</span></p>"
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
				  + "<p class=\"small-4 columns\">Name*</p>"
				  + "<p class=\"small-4 columns\">Start*<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<p class=\"small-4 columns\">End*<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
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
				  + "<p class=\"small-2 columns\">Name*</p>"
				  + "<p class=\"small-2 columns\">Start*<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<p class=\"small-2 columns\">End*<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
				  + "<p class=\"small-2 columns\">PMs*</p>"
				  + "<p class=\"small-2 columns\">Budget*</p>"
				  + "<p class=\"small-2 columns\">Workpackage*</p>"
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
				  + HTMLFooter.getInstance().printFooter(false)
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body></html>");
	}
	
	@Override
	/*
	 * method to handle post requests
	 * creates a new project in the database
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get user ID
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get project parameters
		String pName = request.getParameter("pName");
		String pShortname = request.getParameter("pShortname");
		String pBudget = request.getParameter("pBudget");
		String pCurrency = request.getParameter("pCurrency");
		String pStart = request.getParameter("pStart");
		String pEnd = request.getParameter("pEnd");
		String pPartners = request.getParameter("pPartners");
		if (pPartners == null){
			pPartners = "-";
		}

		// get workpackage and task parameters
		String wpName[] = request.getParameterValues("wpName");
		String wpStart[] = request.getParameterValues("wpStart");
		String wpEnd[] = request.getParameterValues("wpEnd");
		String taskName[] = request.getParameterValues("taskName");
		String taskStart[] = request.getParameterValues("taskStart");
		String taskEnd[] = request.getParameterValues("taskEnd");
		String taskPM[] = request.getParameterValues("taskPM");
		String taskBudget[] = request.getParameterValues("taskBudget");
		String taskWP[] = request.getParameterValues("taskWP");
		
		try {
			// create a new DB connection
			DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
			
			// create a new project in the DB
			int pID = con.newProject(pName, pShortname, id,  pBudget, pCurrency, pStart, pEnd, pPartners);
			
			// create the new workpackages in the DB
			for (int i = 0; i < wpName.length; ++i) {
				con.newWorkpackage(pID, wpName[i], wpStart[i], wpEnd[i]);
			}
			
			// create the new tasks in the DB
			for (int i = 0; i < taskName.length; i++) {
				con.newTask(pID, taskWP[i], taskName[i], taskStart[i], taskEnd[i], taskPM[i], taskBudget[i]);
			}
	
			// create success message
			String message = "<div class=\"callout success\">"
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
			
			// send success message and call get method
			request.setAttribute("msg", message);
	        doGet(request, response);  
			
		} catch (SQLException e) {
			// create error message 
			String message = "<div class=\"callout alert\">"
					       + "<h5>Project could not be created</h5>"
					       + "<p>An error occured and the project could not be created.</p>"
					       + "</div>";
			
			// send error message and call get method
			request.setAttribute("msg", message);
	        doGet(request, response);  
		}
		
	}
}
