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

package ch.zhaw.init.walj.projectmanagement.user.share;

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
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, Page share projects
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Share")
public class ShareProject extends HttpServlet{
	
	// connection to database
	private DBConnection con;
		
	/*
	 * 	method to handle get requests
	 * 	form to share project
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user and project ID
		int userID = (int) request.getSession(false).getAttribute("ID");
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		// get project
		Project project;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == userID){			

			// get all employees
			ArrayList<Employee> employees = new ArrayList<>();
			try {
				employees = con.getAllEmployees();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ArrayList<Employee> sharedEmployees = con.getSharedEmployees(project.getID());
			
			
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Share project", "../", "Share project", "", "<a href=\"Overview/Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false)
					  + "<section>"
					  + "<div class=\"row\">"
					  + "<h3>Choose employees you want to share the project with:</h3>" 
					  + "<form method=\"post\" action=\"Share\" data-abide novalidate>"
				
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose at least one employee.</p></div>"
	
					  // project and employee
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
					  
					  // select employees
					  + "<label class=\"small-12 medium-6 end columns\">Employee "
					  + "<span class=\"grey\">multiple options possible</span> <select name=\"employees\" size=\"10\" multiple required>");
					
			// option for every employee the task is not already shared with
			for (Employee employee : employees){
				int i = 0;
				for (Employee sharedEmployee : sharedEmployees){
					if (sharedEmployee.getID() == employee.getID()){
						i++;
					}
				}
				if (i == 0){
					out.println("<option value =\"" + employee.getID() + "\">" + employee.getFullName() + "</option>");
				}
			}
					
			out.println("</select>"
					  + "</label>"
					  + "</div>"
					  // print return and submit buttons
					  + "<div class=\"row\">"
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Share project\">"
					  + "</div>"
					  + "</section>"
					  + HTMLFooter.getInstance().printFooter(false)
					  + "</div>"
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
	
	/*
	 * 	method to handle post requests
	 * 	shares the project with the given employees
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
		int projectID = Integer.parseInt(request.getParameter("projectID"));

		// get project
		Project project;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == id){
		
			// get parameters
			String[] employeeIDs = request.getParameterValues("employees");
			ArrayList<Employee> employees = new ArrayList<>();
			
			// get employees
			for (String employeeID : employeeIDs) {
				employees.add(project.getEmployee(Integer.parseInt(employeeID)));
			}
	

			String message;
			try {

				// create new share
				for (Employee e : employees) {
					con.newShare(project.getID(), e.getID());
				}
				// success message
				message = "<div class=\"row\">" 
						+ "<div class=\"callout success\">" 
						+ "<h5>" 
						+ project.getName()
						+ " has been shared with the following employees:</h5>";
				for (Employee e : employees) {
					message += "<p>" + e.getName() + "</p>";
				}
				message += "<a href=\"/Projects/Overview/Project?id=" + projectID + "#effort\">Click here to go to the project overview</a>"
						 + "</div>"
						 + "</div>";
	
			} catch (SQLException e) {
				// error message
				message = "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Something went wrong</h5>"
					    + "<p>The project could not be shared</p>" + "</div></div>";
			}
	
		
	
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Share Project", "../", "Share Project", "", "<a href=\"Overview/Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false)
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
		} else {
	        String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);			
		}
	}
}
