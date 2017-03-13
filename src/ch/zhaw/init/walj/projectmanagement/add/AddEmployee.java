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
import ch.zhaw.init.walj.projectmanagement.util.Mail;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;

/**
 * Projectmanagement tool, create employee page
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/newEmployee")
public class AddEmployee extends HttpServlet {
	
	// connection to database
	private DBConnection con;
	
	/*
	 * method to handle get requests
	 * Form to create a new employee
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// prepare success/error message
		String message = "";
		if (request.getAttribute("error") != null){
			
			message = "<div class=\"row\">"
					   + "<div class=\"callout alert\">"
					   + "<h5>Something went wrong</h5>"
					   + "<p>The employee could not be created.</p>"
					   + "</div></div>";
			
		} else if (request.getAttribute("success") != null){
			
			Employee user = (Employee) request.getAttribute("success");
			message = "<div class=\"row\">"
					+ "<div class=\"callout success\">"
					+ "<h5>Employee successfully created</h5>"
					+ "<p>The new employee was successfully created with the following data:</p>"
					+ "<p>Name: " + user.getName()+ "</p>"
					+ "<p>Kuerzel: " + user.getKuerzel() + "</p>"
					+ "<p>Mail: " + user.getMail() + "</p>"
					+ "<p>Wage: " + user.getWage() + "</p>"
					+ "<p>Password: " + user.getPassword() + "</p>"
					+ "</div></div>";	
		}
		
		// Print HTML
		out.println(HTMLHeader.getInstance().printHeader("New Employee", "../", "New Employee", "")
				  // HTML section with form
				  + "<section>"
				  + message
				  + "<form method=\"post\" action=\"newEmployee\" data-abide novalidate>"
				  // error message (if something's wrong with the form)
				  + "<div class=\"row\">"
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				  + "</div>"
				  + "</div>"
				  // field for firstname
				  + "<div class=\"row\">"
				  + "<label class=\"small-12 large-6 columns\">First Name "
				  + "<input type=\"text\" name=\"firstname\" required>"
				  + "</label>"
				  // field for lastname
				  + "<label class=\"small-12 large-6 columns\">Last Name "
				  + "<input type=\"text\" name=\"lastname\" required>"
				  + "</label>"
				  // field for kuerzel
				  + "<label class=\"small-12 large-6 columns\">Kuerzel "
				  + "<input type=\"text\" name=\"kuerzel\" required>"
				  + "</label>"
				  // field for e-mail address
				  + "<label class=\"small-12 large-6 columns\">Mail "
				  + "<input type=\"email\" name=\"mail\" required>"
				  + "</label>"
				  // field for wage per hour
				  + "<label class=\"small-12 large-6 columns end\">Wage per Hour "
				  + "<input type=\"number\" name=\"wage\" required>"
				  + "</label>"
				  + "</div>"
				  + "<div class=\"row\">"
				  // submit button
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Create Employee\">"
				  + "</div>"
				  + "</form>"
				  + "</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</div>"
				  + "</body>"
				  + "</html>");
		
	}	
	
	/*
	 * method to handle post requests
	 * creates new employee in database
	 * sends welcome mail with password to new employee
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// get user id from session
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get parameters
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String kuerzel = request.getParameter("kuerzel");
		String mailAddress = request.getParameter("mail");
		int wage = Integer.parseInt(request.getParameter("wage"));
		
		try {
			// create new employee
			Employee user = con.newEmployee(id, firstname, lastname, kuerzel, mailAddress, wage);
			
			// send welcome mail
			Mail mail = new Mail(user, this.getServletContext().getRealPath("/"));
			mail.sendWelcomeMail(request.getRequestURL());
			
			// call get method with success message
			request.setAttribute("success", user);
            doGet(request, response);  
			
		} catch (SQLException e) {
			// call get method with error message
			request.setAttribute("error", "");
            doGet(request, response);  
		}
	}
}