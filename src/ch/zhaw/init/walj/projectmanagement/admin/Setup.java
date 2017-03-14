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

package ch.zhaw.init.walj.projectmanagement.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
 * Projectmanagement tool, setup page
 * (set Admin mail and password)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/setup")
public class Setup extends HttpServlet {
	
	/*
	 * method to handle get requests
	 * Form to set admin mail and password
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	DBConnection con = null;    	
    	try {
    		// connection to database
    		con = new DBConnection(this.getServletContext().getRealPath("/"));            
    	} catch (NullPointerException e){
    		
    	}
    	
    	if (!con.noConnection){
            String setupURI = request.getContextPath() + "/login";
    		response.sendRedirect(setupURI);
    		return;
    	}    	
    	
    	// prepare response
    	response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
	    			
		// set error message
		String message = "";
    	if (request.getAttribute("error") != null){
    		message = (String) request.getAttribute("error");
    	}
		
    	// print HTML
    	out.println(HTMLHeader.getInstance().printHeader("Setup", "", "Setup", "", "", true, false)
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<section>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns\">"
				  + "<h1 class=\"blue add\">Project Management SaaS</h1>"
				  + "</div>"
				  + "<div class=\"small-12 columns\">"
				  + "<h2 class=\"blue\">Initial setup</h2>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns\">"
				  + message
				  + "<form method=\"post\" action=\"setup\" data-abide novalidate>"
				  // error message (if something's wrong with the form)
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
				  + "</div>"
				  + "<div class=\"small-12 columns\">"
				  + "<h3 class=\"no-margin\">MySQL Database</h3>"
				  + "</div>"
				  + "<label class=\"small-12 columns\">URL "
				  + "<input type=\"text\" name=\"dbURL\" required>"
				  + "</label>"
				  + "<label class=\"small-12 columns\">Database name "
				  + "<input type=\"text\" name=\"dbName\" required>"
				  + "</label>"
				  + "<label class=\"small-6 columns\">User "
				  + "<input type=\"text\" name=\"dbUser\" required>"
				  + "</label>"
				  + "<label class=\"small-6 columns\">Password "
				  + "<input type=\"password\" name=\"dbPassword\" required>"
				  + "</label>"
				  + "<hr>"					  
				  + "<div class=\"small-12 columns\">"
				  + "<h3 class=\"no-margin\">Mail Server</h3>"
				  + "</div>"
				  + "<label class=\"small-12 columns\">Sender's e-mail address (e.g. noreply@pmsaas.ch) "
				  + "<input type=\"email\" name=\"mailFrom\" required>"
				  + "</label>"
				  + "<label class=\"small-12 columns\">Host "
				  + "<input type=\"text\" name=\"host\" required>"
				  + "</label>"
				  + "<hr>"					  
				  + "<div class=\"small-12 columns\">"
				  + "<h3 class=\"no-margin\">Admin Credentials</h3>"
				  + "</div>"
				  // field for e-mail address
				  + "<label class=\"small-12 columns\">Admin Mail "
				  + "<input type=\"email\" name=\"mail\" required>"
				  + "</label>"
				  // field for password
				  + "<label class=\"small-12 large-6 end columns\">New Password "
				  + "<input type=\"password\" name=\"password\" id=\"password\" required>"
				  + "<span class=\"form-error\">"
		          + "Password is required!"
		          + "</span>"
				  + "</label>"
				  // field to re-enter password
				  + "<label class=\"small-12 large-6 end columns\">Re-enter Password "
				  + "<input type=\"password\" data-equalto=\"password\" required>"
				  + "<span class=\"form-error\">"
		          + "Passwords don't match!"
		          + "</span>"
				  + "</label>"
				  // submit button
				  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"submit\">"
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

    /*
     *  method to handle post requests
     *  creates new employee "Admin"
     *  creates session
     *  sends mail to admin
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	// prepare response
    	response.setContentType("text/html;charset=UTF8");
        PrintWriter out = response.getWriter();	 

    	// get parameters
        String dbURL = request.getParameter("dbURL");
        String dbName = request.getParameter("dbName");
        String dbUser = request.getParameter("dbUser");
        String dbPassword = request.getParameter("dbPassword");
        
        String mailFrom = request.getParameter("mailFrom");
        String host = request.getParameter("host");
        
    	String mailAddress = request.getParameter("mail");
        String password = request.getParameter("password");
        
        // write database config file
        FileWriter fw = new FileWriter(this.getServletContext().getRealPath("/") + ".config");
        BufferedWriter bw = new BufferedWriter(fw);
        
        bw.write("URL=" + dbURL);
        bw.newLine();
        bw.write("Database=" + dbName);
        bw.newLine();
        bw.write("Username=" + dbUser);
        bw.newLine();
        bw.write("Password=" + dbPassword);

        bw.close();
        
        // write mail config file
        fw = new FileWriter(this.getServletContext().getRealPath("/") + ".mailconfig");
        bw = new BufferedWriter(fw);
        
        bw.write("mailFrom=" + mailFrom);
        bw.newLine();
        bw.write("host=" + host);

        bw.close();
        
        // read SQL statements
        FileReader fr = new FileReader(this.getServletContext().getRealPath("/") + "SQL/projectmanagement.sql");
	    BufferedReader br = new BufferedReader(fr);
	    
	    try {
	    	// set driver
	    	String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver).newInstance();
	    	
			// get connection and create statement
			Connection connection = DriverManager.getConnection(dbURL + "?user="+ dbUser + "&password=" + dbPassword);
			Statement st = connection.createStatement();
			
			// create database
		    String sql = "CREATE DATABASE IF NOT EXISTS `" + dbName + "` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;";
		    st.executeUpdate(sql);
		    
		    // select database
		    sql =  "USE `" + dbName + "`;"; 
		    st.executeUpdate(sql);
		    
		    // read all SQL statements and execute them
		    while ((sql = br.readLine()) != null){
		    	st.executeUpdate(sql);
		    }
		    br.close();
	 
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	    
	    
    	DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
        
        try {
        	// create new employee "Admin" in database
        	Employee e = con.newEmployee(0, "Admin", " ", "admin", mailAddress, password, 0);
        	
        	// create session
            request.getSession().setAttribute("user", e.getFirstName());
            request.getSession().setAttribute("ID", e.getID());
            request.getSession().setAttribute("kuerzel", e.getKuerzel());
            request.getSession().setMaxInactiveInterval(60*60);
            
            // send mail to admin
            Mail mail = new Mail(e, this.getServletContext().getRealPath("/"));
            mail.sendInitialSetupMail();
            
	    	// print HTML
            out.println(HTMLHeader.getInstance().printHeader("Setup - Project Management Saas", "") 
					  + "<body>"
					  + "<div id=\"wrapper\">" 
					  + "<section>" 
					  + "<div class=\"row\">" 
					  + "<div class=\"small-4 columns\">"
					  + "<img src=\"img/logo.png\" class=\"add\">"
					  + "</div>"
					  + "</div>"
					  + "<div class=\"row\">" 
					  + "<div class=\"small-12 columns\">"
					  + "<h1 class=\"blue add\">Project Management SaaS</h1>"
					  + "</div>"
					  + "<div class=\"small-12 columns\">"
					  + "<h2 class=\"blue\">Initial setup</h2>"
					  + "</div>"
					  + "</div>"
					  + "<div class=\"row\">" 
  					  + "<div class=\"small-12 columns\">"
  					  + "<div class=\"row\">" 
  					  + "<div class=\"callout success\">" 
  					  + "<h5>Initial setup successful</h5>"
  					  + "<p><a href=\"admin/properties\">Go to properties</a></p>"
  					  + "</div>"
  					  + "</div>"
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
        } catch (NullPointerException | SQLException ex){
        	// set error message and call get method
        	String message = "<div class=\"row\">" 
  						   + "<div class=\"small-6 small-offset-3 end columns\">"
  						   + "<div class=\"row\">" 
  						   + "<div class=\"callout alert\">" 
  						   + "<h5>Something went wrong.</h5>"
  						   + "</div>"
  						   + "</div>"
  						   + "</div>"
  						   + "</div>";
        	request.setAttribute("error", message);
            doGet(request, response);        	
        }
    }
}
