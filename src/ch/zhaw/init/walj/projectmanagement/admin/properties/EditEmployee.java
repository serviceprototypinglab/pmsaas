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

package ch.zhaw.init.walj.projectmanagement.admin.properties;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * project management tool, edit employees (as admin)
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/admin/editEmployee")
public class EditEmployee extends HttpServlet {
	
	// Database connection
	private DBConnection con;
	
	/*
	 * method to handle get requests
	 * form for editing user information
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		// get employee
		Employee employee = null;
		try {
			employee = con.getEmployee(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// get print writer
		PrintWriter out = response.getWriter();
		
		// print html
		assert employee != null;
		out.println(HTMLHeader.getInstance().printHeader("Edit " + employee.getName(), "../", "Edit " + employee.getName(), "", "<a href=\"properties\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to properties</a>", true)
				  + "<section>"					  
				  // form to edit user information
				  + "<div class=\"row\" id=\"user\">"
				  + "<div class=\"small-2 columns\">"
				  + "<i class=\"fa fa-user blue big-icon\"></i>"
				  + "</div>"
				  + "<div class=\"small-10 columns\">"
				  + "<form method=\"post\" action=\"editEmployee?id=" + id + "\" data-abide novalidate>"
				  + "<input type=\"hidden\" name=\"id\" value=\"" + employee.getID() + "\">"
				  + "<label class=\"small-10 end columns\">First Name "
				  + "<input type=\"text\" value=\"" + employee.getFirstName() + "\" name=\"firstname\" required>"
				  + "</label>"
				  + "<label class=\"small-10 end columns\">Last Name "
				  + "<input type=\"text\" value=\"" + employee.getLastName() + "\" name=\"lastname\" required>"
				  + "</label>"
				  + "<label class=\"small-10 end columns\">Kuerzel "
				  + "<input type=\"text\" value=\"" + employee.getKuerzel() + "\" name=\"kuerzel\" required>"
				  + "</label>"
				  + "<label class=\"small-10 end columns\">E-Mail "
				  + "<input type=\"text\" value=\"" + employee.getMail() + "\" name=\"mail\" required>"
				  + "</label>"
				  + "<label class=\"small-10 end columns\">Wage per hour "
				  + "<input type=\"number\" value=\"" + employee.getWage() + "\" name=\"wage\" required>"
				  + "</label>"
				  + "<div class=\"small-3 small-offset-7 columns end\">" 			  
				  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
				  + "</div>"
				  + "</form>"
				  + "</div>"
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
	 * updates user information
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// variable declaration, get parameters
		int userID = Integer.parseInt(request.getParameter("id"));
		
		Employee employee = null;
		try {
			employee = con.getEmployee(userID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		String message;
		String title;
		
		// get parameters
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String kuerzel = request.getParameter("kuerzel");
		String mail = request.getParameter("mail");
		double wage = Double.parseDouble(request.getParameter("wage"));
			
		// set title
		title = "Update " + firstname + " " + lastname;
		try {
			// update user
			con.updateUser(userID, firstname, lastname, kuerzel, mail);
			assert employee != null;
			if (employee.getWage() != wage){
				con.newWage(userID, wage, DateFormatter.getInstance().formatDateForDB(new Date()));
			}
			
			// prepare success message
			message = "<div class=\"callout success\">"
					+ "<h5>Project successfully updated</h5>"
					+ "<p>The user has succsessfully been updated with the following data:</p>"
					+ "<p>First Name: " + firstname + "</p>"
					+ "<p>Last Name: " + lastname + "</p>"
					+ "<p>Kuerzel: " + kuerzel + "</p>"
					+ "<p>E-Mail: " + mail + "</p>"
					+ "<p>Wage per hour: " + wage + "</p>"
					+ "<a href=\"/Projects/employee\">Click here to go back to the user page</a>"
					+ "</div>";
			
		} catch (SQLException e) {
			e.printStackTrace();
			// prepare error message
			message = "<div class=\"callout alert\">"
				    + "<h5>User could not be updated</h5>"
				    + "<p>An error occured and the user could not be updated.</p>"
					+ "<a href=\"/Projects/employee\">Click here to go back to the user page</a>"
					+ "</div>";
		}	
	
		// print HTML 
		out.println(HTMLHeader.getInstance().printHeader(title, "../", title, "", "", true)
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
