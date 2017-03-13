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

package ch.zhaw.init.walj.projectmanagement.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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
 * Projectmanagement tool, Page to edit tasks
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/EditTask")
public class EditTask extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con;
		
	/*
	 * 	method to handle post requests
	 * 	makes changes in database
	 * 	returns error/success message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get parameters and user ID
		int taskID = Integer.parseInt(request.getParameter("id"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		String name = request.getParameter("name");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		int pm = Integer.parseInt(request.getParameter("pm"));
		double budget = Double.parseDouble(request.getParameter("budget"));
		int wp = Integer.parseInt(request.getParameter("workpackage"));
		int userID = (int) request.getSession(false).getAttribute("ID");
		
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
		if (project.getLeader() == userID) {
			
			String message = "";
			try {
				// update task
				con.updateTask(taskID, name, DateFormatter.getInstance().formatDateForDB(start), DateFormatter.getInstance().formatDateForDB(end), pm, budget, wp);
				
				// success message
				message = "<div class=\"callout success\">"
						+ "<h5>Task successfully updated</h5>"
						+ "<p>The task has succsessfully been updated with the following data:</p>"
						+ "<p>Name: " + name + "</p>"
						+ "<p>Duration: " + start + " - " + end + "</p>"
						+ "<p>PMs: " + pm + ""
						+ "<p>Budget: " + budget + "</p>"
						+ "<p>Workpackage: " + wp + "</p>"
						+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#tasks\">Click here to go back to the edit page</a>"
						+ "<br>"
						+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
						+ "</div>";
			} catch (SQLException e) {
				
				// error message
				message = "<div class=\"callout alert\">"
					    + "<h5>Task could not be updated</h5>"
					    + "<p>An error occured and the task could not be updated.</p>"
						+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + taskID + "#tasks\">Click here to go back to the edit page</a>"
						+ "<br>"
						+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + taskID + "\">Click here to go to the project overview</a>"
						+ "</div>";
			}
					
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Edit " + name, "../", "Edit " + name, "")
					  + "<section>"
					  + "<div class=\"row\">"
					  + message
					  + "</div>"
					  + "</section>"
					  + HTMLFooter.getInstance().printFooter(false)
					  // required JavaScript
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
