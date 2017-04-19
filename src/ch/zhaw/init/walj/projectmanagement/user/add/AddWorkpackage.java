/*
 	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 	All Rights Reserved.

   Licensed under the Apache License, Version 2.0 (the "License"); you may
   not use this file except in compliance with the License. You may obtain
   a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
   License for the specific language governing permissions and limitations
   under the License.
 */

package ch.zhaw.init.walj.projectmanagement.user.add;

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
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;


/**
 * project management tool, page to add workpackages
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addWorkpackage")
public class AddWorkpackage extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con;
	
	/*
	 * method to handle get requests
	 * Form to create new workpackages
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
		int pID = Integer.parseInt(request.getParameter("projectID"));
		
		// get project
		Project project;
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
		
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Add Workpackages", "../../", "Add Workpackages", "", "<a href=\"Project?id=" + pID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false)
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
					  + HTMLFooter.getInstance().printFooter(false)
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

	/*
	 * method to handle post requests
	 * adds new workpackage to database 
	 * calls get method with 
	 * success/error message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
		int pID = Integer.parseInt(request.getParameter("projectID"));
		
		// get project
		Project project;
		try {
			project = con.getProject(pID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == id){
		
			// get parameters
			String wpName = request.getParameter("wpName");
			String wpStart = request.getParameter("wpStart");
			String wpEnd = request.getParameter("wpEnd");
			
			
			boolean dateOK = DateFormatter.getInstance().checkDate(project.getStart(), wpStart, "dd.MM.yyyy") &&
							DateFormatter.getInstance().checkDate(wpEnd, project.getEnd(), "dd.MM.yyyy") &&
							DateFormatter.getInstance().checkDate(wpStart, wpEnd, "dd.MM.yyyy");
			
			if (dateOK){
				try {	
					// create new workpackage in the DB
					con.newWorkpackage(pID, wpName, wpStart, wpEnd);
	
					// set success message	
					String message = "<div class=\"callout success small-12 columns\">"
							       + "<h5>Workpackage successfully created</h5>"
							       + "<p>The new workpackage has succsessfully been created.</p>"
							       + "<a href=\"/Projects/Overview/addTask?projectID=" + pID + "\">Click here to add tasks</a>"
							       + "</div>";
					request.setAttribute("msg", message);
	
				} catch (SQLException e) {
	
					// set error message 
					String message = "<div class=\"callout alert small-12 columns\">"
							  	   + "<h5>Workpackage could not be created</h5>"
							  	   + "<p>An error occured and the workpackage could not be created.</p>"
							  	   + "</div>";
					request.setAttribute("msg", message);
				}
			} else {
				// set error message 
				String message = "<div class=\"callout alert small-12 columns\">"
						  	   + "<h5>Workpackage could not be created</h5>"
						  	   + "<p>Date not possible, make sure the start date is not before project start and the end date is not after project end</p>"
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
