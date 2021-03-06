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

package ch.zhaw.init.walj.projectmanagement;

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
import ch.zhaw.init.walj.projectmanagement.util.password.PasswordGenerator;
import ch.zhaw.init.walj.projectmanagement.util.password.PasswordService;

/**
 * project management tool, reset password page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/resetPassword")
public class ResetPassword extends HttpServlet {
		
	/*
	 * method to handle get requests
	 * form to reset password
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// prepare response
		PrintWriter out = response.getWriter();
		response.setContentType("text/html;charset=UTF8");
    	    	
    	// print HTML
    	out.println(HTMLHeader.getInstance().printHeader("Reset password - Project Management Saas", "") 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<section>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-4 small-offset-4 end columns\">"
				  + "<img src=\"img/logo.png\">"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<h1 class=\"align-center\">Reset your password</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-12 columns text-center\">"
				  // password reset form
				  + "<form method=\"post\" action=\"resetPassword\" data-abide novalidate>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Fill in your e-mail address.</p></div>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
  				  +	"<div class=\"input-group\">"
  				  +	"<span class=\"input-group-label\"><i class=\"fa fa-envelope\"></i></span>"
				  + "<input type=\"email\" class=\"input-group-field\" name=\"mail\" required>" 
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns text-center\">"
				  + "<input type=\"submit\" class=\"expanded button\" value=\"Reset Password\">" 
				  + "</div>"
				  + "</div>"
				  + "</form>"
				  + "</div>"
				  + "</div>"
				  + "</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  // required JavaScript
				  + "<script src=\"js/vendor/jquery.js\"></script>"
				  + "<script src=\"js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");  	
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	// connection to database
    	DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
    	
    	// get mail address
    	String user = request.getParameter("mail");
        
    	// find user 
        Employee employee = con.findUser(user);
             
        // try to reset password
        try {
        	// create new password
	        String newPassword = PasswordGenerator.getInstance().getNewPassword();
	        
	        // set new password for employee
	        employee.setNewPassword(newPassword);
	        
	        // update password in database
	        con.updatePassword(employee.getID(), PasswordService.getInstance().encrypt(employee.getPassword()));
	        
	        // send mail with new password
	        Mail mail = new Mail(employee, this.getServletContext().getRealPath("/"));
			mail.sendNewPassword();
			

		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
        
        // redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
