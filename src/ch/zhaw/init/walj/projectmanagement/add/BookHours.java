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
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * Projectmanagement tool, Page to book hours
 * 
 * @author Janine Walther, ZHAW
 *
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/bookHours")
public class BookHours extends HttpServlet {
	
	// Database connection
	private DBConnection con;

	/*
	 * method to handle get requests
	 * Form to book hours,
	 * choose employee
	 * calls bookHours/chooseTask afterwards
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user ID
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get projectID given from parameter
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
		
		// check if user is project leader
		if (project.getLeader() == id){
		
			// set error/success message
	    	String message = "";    	
	    	if (request.getAttribute("msg") != null){
	    		message = (String) request.getAttribute("msg");
	    	}
			
			// get all employees
			ArrayList<Employee> employees = new ArrayList<Employee>();
			try {
				employees = con.getAllEmployees();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
	
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Book Hours", "../../", "Book Hours", "", "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false)
					  + "<section>"
					  + "<div class=\"row\">" 
					  + message
					  + "<h3>Choose Employee</h3>"
					  + "<form method=\"get\" action=\"bookHours/chooseTask\" data-abide novalidate>"
	
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Please choose an employee.</p></div>"
	
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
	
					  // select employee
					  + "<h5 class=\"small-12 medium-2 columns\">Employee</h5>"
					  + "<div class=\"small-12 medium-6 end columns\">" 
					  + "<select name=\"employee\" required>"
					  + "<option></option>");
	
			// option for every employee
			for (Employee employee : employees) {
				out.println("<option value =\"" + employee.getID() + "\">" + employee.getFullName() + "</option>");
			}
	
			// print submit button
			out.println("</select></div>" 
					  + "</div>" 
					  + "<div class=\"row\">"
					  + "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Choose Task  <i class=\"fa fa-chevron-right\"></i></button>"
					  + "</div>");
	
			// print required JavaScript
			out.println("</section>"
					  + HTMLFooter.getInstance().printFooter(false)
					  + "</div>"
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
	 * adds new bookings to database 
	 * calls get method with 
	 * success/error message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
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
		
		// check if user is project leader
		if (project.getLeader() == id){
			// get parameters
			String[] assignment = request.getParameterValues("assignmentID");
			String[] hour = request.getParameterValues("hours");
			String[] month = request.getParameterValues("months");
			ArrayList<Integer> months = new ArrayList<Integer>();
			ArrayList<Double> hours = new ArrayList<Double>();
			ArrayList<Integer> assignments = new ArrayList<Integer>();
	
			// parse the hour strings to double, add them to arraylist
			for (String s : hour) {
				hours.add(Double.parseDouble(s));
			}
	
			// get number of the month in the project, add month to arraylist
			for (String s : month) {
				int monthNbr = DateFormatter.getInstance().getMonthsBetween(project.getStart(), s);
				months.add(monthNbr);
			}
	
			// parse assignment IDs to integer, add them to arraylist
			for (String s : assignment) {
				assignments.add(Integer.parseInt(s));
			}
	
			String message = "";
			try {
				// create new bookings in DB
				for (int i = 0; i < hour.length; i++) {
						con.newBooking(assignments.get(i), months.get(i), hours.get(i));
				}
				// set success message
				message = "<div class=\"row\">" 
						+ "<div class=\"callout success\">"
						+ "<h5>Hours successfully booked</h5>" 
						+ "<p>The hours were successfully booked to the project "
						+ project.getName() 
						+ ".</p>" 
						+ "</div></div>";
				request.setAttribute("msg", message);
	            
			} catch (SQLException e) {
				// set error message
				message = "<div class=\"row\">" 
				        + "<div class=\"callout alert\">" 
				        + "<h5>Something went wrong</h5>"
				        + "<p>The hours could not be booked.</p>" 
				        + "</div>"
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
