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

package ch.zhaw.init.walj.projectmanagement.admin.properties;

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
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.password.PasswordService;

/**
 * project management tool, profile page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/admin/deleteEmployee")
public class DeleteEmployee extends HttpServlet {
	
	// Database connection
	private DBConnection con;
	
	/*
	 * method to handle get requests
	 * form for editing user information and change password
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		// get print writer
		PrintWriter out = response.getWriter();
		
		// get user id from session
		int id = Integer.parseInt(request.getParameter("id"));
						
		// get employee
		Employee employee = null;
		boolean assignments = false;
		boolean projects = false;
		boolean expenses = false;
		try {
			employee = con.getEmployee(id);
			if (!con.getAssignedTasks(id).isEmpty()) {
				assignments = true;
			}
			
			if (con.getProjects(id, true) != null){
				projects = true;
			}

			if (con.getProjects(id, false) != null){
				projects = true;
			}
			
			if (!con.getExpenses(id).isEmpty()){
				expenses = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// print html
		out.println(HTMLHeader.getInstance().printHeader("Delete " + employee.getName(), "../", "Delete " + employee.getName(), "", "", true)
				  + "<section>"
				  + "<div class=\"row\">");
		
		String message = "";
		if (!(assignments || projects || expenses)) {
			try {
				con.deleteEmployee(id);
				message = "<div class=\"callout success\">"
						+ "<h5>Employee successfully deleted</h5>"
						+ "<p>The user " + employee.getName() + " has succsessfully been deleted</p>"
						+ "<a href=\"/Projektverwaltung/admin/properties\">Click here to go back to the properties page</a>"
						+ "</div>";
			} catch (SQLException e) {
				message = "<div class=\"callout alert\">"
					    + "<h5>User could not be deleted</h5>"
					    + "<p>An error occured and the user could not be deleted.</p>"
						+ "<a href=\"/Projektverwaltung/admin/properties\">Click here to go back to the properties page</a>"
						+ "</div>";
			}
		} else {
			message = "<div class=\"callout alert\">"
				    + "<h5>User could not be deleted</h5>"
				    + "<p>The user could not be deleted because of the following reason(s):</p>";
			
			if (assignments){
				message += "<p>- The user has at least one assignment to a task.</p>";
			}
			if (projects){
				message += "<p>- The user is leader of at least one project.</p>";
			}
			if (expenses){
				message += "<p>- The user has at least one expense in a project.</p>";
			}
			
			message += "<a href=\"/Projektverwaltung/admin/properties\">Click here to go back to the properties page</a>"
					+ "</div>";
		}
		
		out.println(message
				  + "</div>"
				  + "</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}

	/*
	 * method to handle post requests
	 * updates user information or password
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// variable declaration, get parameters
		int userID = Integer.parseInt(request.getParameter("id"));
		String password = request.getParameter("password");
				
		String message = "";
		String title = "";
		
		// check if the user edited his user information or his password
		if (password == null){
			// get parameters
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String kuerzel = request.getParameter("kuerzel");
			String mail = request.getParameter("mail");
				
			// set title
			title = "Update User";
			try {
				// update user
				con.updateUser(userID, firstname, lastname, kuerzel, mail);
				
				// prepare success message
				message = "<div class=\"callout success\">"
						+ "<h5>Project successfully updated</h5>"
						+ "<p>The user has succsessfully been updated with the following data:</p>"
						+ "<p>First Name: " + firstname + "</p>"
						+ "<p>Last Name: " + lastname + "</p>"
						+ "<p>Kuerzel: " + kuerzel + "</p>"
						+ "<p>E-Mail: " + mail + "</p>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
				
			} catch (SQLException e) {
				// prepare error message
				message = "<div class=\"callout alert\">"
					    + "<h5>User could not be updated</h5>"
					    + "<p>An error occured and the user could not be updated.</p>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
			}	
		} else {
			// encrypt password
			password = PasswordService.getInstance().encrypt(password);
			
			// set title
			title = "Change Password";
			
			try {
				// update password
				con.updatePassword(userID, password);
				
				// prepare success message
				message = "<div class=\"callout success\">"
						+ "<h5>Password successfully updated</h5>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
			} catch (SQLException e) {
				// prepare error message
				message = "<div class=\"callout alert\">"
					    + "<h5>Password could not be updated</h5>"
					    + "<p>An error occured and the password could not be updated.</p>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
			}
		}		
		
		// print HTML 
		out.println(HTMLHeader.getInstance().printHeader(title, "../", title, "")
				  + "<section>"
				  + "<div class=\"row\">"
				  + message
				  + "</div>"
				  +  "</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
		
	}
}
