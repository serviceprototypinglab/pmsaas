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
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Workpackage;
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * project management tool, page to add tasks
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addTask")
public class AddTask extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con;
		
	/*
	 * method to handle get requests
	 * Form to create new tasks
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get user ID
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get project ID
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
			 
			//get workpackages
			ArrayList<Workpackage> workpackages = project.getWorkpackages();
								
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Add Tasks", "../../", "Add Tasks", "", "<a href=\"Project?id=" + pID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false)
					  + "<section>"
					  + message
					  + "<div class=\"row\">"
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
					  + HTMLFooter.getInstance().printFooter(false)
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
	
	/*
	 * method to handle post requests
	 * adds new task to database 
	 * calls get method with 
	 * success/error message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// get user ID
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
		
		// check if user is project leader
		if (project.getLeader() == id){
			// get the parameters
			String taskName = request.getParameter("taskName");
			String taskStart = request.getParameter("taskStart");
			String taskEnd = request.getParameter("taskEnd");
			String taskPM = request.getParameter("taskPM");
			String taskBudget = request.getParameter("taskBudget");
			int taskWP = Integer.parseInt(request.getParameter("taskWP"));
			
	
			Workpackage wp = project.getWorkpackage(taskWP);
			
			boolean dateOK = DateFormatter.getInstance().checkDate(wp.getStart(), taskStart, "dd.MM.yyyy") &&
					DateFormatter.getInstance().checkDate(taskEnd, wp.getEnd(), "dd.MM.yyyy") &&
					DateFormatter.getInstance().checkDate(taskStart, taskEnd, "dd.MM.yyyy");
		
			if (dateOK){
				try {	
					// create the new workpackages in the DB
					con.newTask(taskWP, taskName, taskStart, taskEnd, taskPM, taskBudget);
	
					// set success message as request attribute
					String message = "<div class=\"callout success small-12 columns\">"
							  	   + "<h5>Task successfully created</h5>"
							  	   + "<p>The new task has succsessfully been created.</p>"
							  	   + "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + pID + "\">Click here to go back to the project overview</a>"
							  	   + "</div>";
					request.setAttribute("msg", message);
					
				} catch (SQLException e) {
	
					// set error message as request attribute
					String message = "<div class=\"callout alert small-12 columns\">"
							       + "<h5>Task could not be created</h5>"
							       + "<p>An error occured and the task could not be created.</p>"
							       + "</div>";
					request.setAttribute("msg", message);
				}
			} else {
				// set error message 
				String message = "<div class=\"callout alert small-12 columns\">"
						  	   + "<h5>Workpackage could not be created</h5>"
						  	   + "<p>Date not possible, make sure the start date is not before workpackage start and the end date is not after workpackage end</p>"
						  	   + "</div>";
				request.setAttribute("msg", message);
			}

			// call get method
	        doGet(request, response);  
	        
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}
	}
}
