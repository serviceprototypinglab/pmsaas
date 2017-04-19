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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.ExpenseTypes;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, Page to add expenses
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/addExpense")
public class AddExpense extends HttpServlet {

	// connection to database
	private DBConnection con;

	/*
	 * method to handle get requests
	 * Form to add new expenses
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get user ID
		int id = (int) request.getSession(false).getAttribute("ID");

		// get project given from get-parameter projectID
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
		
		// set error/success message
    	String message = "";    	
    	if (request.getAttribute("msg") != null){
    		message = (String) request.getAttribute("msg");
    	}
		
    	// check if user is project leader
		if (project.getLeader() == id){
			
			// get the employees assigned to the project and add them to the ArrayList
			ArrayList<Employee> employees;
			employees = project.getEmployees();
	
			// Print HTML head and header
			out.println(HTMLHeader.getInstance().printHeader("Add Expenses", "../../", "Add Expenses", "", "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false));
	
			// print HTML section with form
			out.println("<section>"
					  + message
					  + "<div class=\"row\">" 
					  + "<form method=\"post\" action=\"addExpense\" data-abide novalidate>"
	
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p></div>"
					  
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
	
					  // select employee
					  + "<label class=\"small-12 medium-6 end columns\">Employee <select name=\"employee\" required>"
					  + "<option></option>");
	
			// option for every employee
			for (Employee employee : employees) {
				out.println("<option value =\"" + employee.getID() + "\">" + employee.getFullName() + "</option>");
			}
	
			// Print fields for costs, type of expense, description and date
			out.println("</select></label>"
					  // costs
					  + "<label class=\"small-12 medium-6 columns\">Costs" 
					  + "<div class=\"input-group\">"
					  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
					  + "<input class=\"input-group-field\" type=\"number\" name=\"costs\" required></div>" 
					  + "</label>"
	
					  // type
					  + "<label class=\"small-12 medium-6 columns\">Type "
					  +	"<select name=\"type\" required>"
					  + "<option></option>");

			for (ExpenseTypes type : ExpenseTypes.values()){
				out.println("<option>" + type + "</option>");
			}

			out.println("</select>"
					  + "</label>"
	
					  // description
					  + "<label class=\"small-12 medium-6 columns\">Description"
					  + "<input type=\"text\" name=\"description\" required>" 
					  + "</label>"
	
					  // date
					  + "<label class=\"small-12 medium-6 end columns\">Date <span class =\"grey\"> (dd.mm.yyyy)</span>"
					  + "<input type=\"text\" name=\"date\" required>" 
					  + "</label>"
					  + "</div>");

			// print HTML submit button
			out.println("<div class=\"row\">"
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Add Expense\">"
					  + "</div>");
	
			// required JavaScript
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
	 * creates new expense in database
	 * calls get method with 
	 * success/error message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// get user ID
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// variable declaration, get parameters
		int projectID = Integer.parseInt(request.getParameter("projectID"));
	
		// get parameters
		int employeeID = Integer.parseInt(request.getParameter("employee"));
		double costs = Double.parseDouble(request.getParameter("costs"));
		String type = request.getParameter("type");
		String description = request.getParameter("description");
		String date = request.getParameter("date");
	
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
			
			// create new expense in the database with the given parameters
			try {
				// create new expense
				con.newExpense(projectID, employeeID, costs, type, description, date);
	
				// get employee
				Employee employee = project.getEmployee(employeeID);
				
				// create success message
				String message = "<div class=\"row\">" 
						+ "<div class=\"callout success\">" 
						+ "<h5>Expense successfully added</h5>"
						+ "<p>The new expense was successfully added with the following data:</p>"
						+ "<p>Employee: " + employee.getName() + "</p>"
						+ "<p>Costs: " + project.getCurrency() + " " + costs + "</p>" 
						+ "<p>Type: " + type + "</p>"
						+ "<p>Description: " + description + "</p>" 
						+ "<p>Date: " + date + "</p>" + "</div></div>";
				
				// set success message as request attribute
	            request.setAttribute("msg", message);
	
			} catch (SQLException e) {
				// create error message
				String message = "<div class=\"row\">" 
						   + "<div class=\"callout alert\">" 
						   + "<h5>Something went wrong</h5>"
						   + "<p>The expense could not be added</p>" 
						   + "</div></div>";
				// set error message as request attribute
	            request.setAttribute("msg", message);
			}
			
			// call get method with message
            doGet(request, response);
            
		} else {
	        String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);			
		}
	}
}
