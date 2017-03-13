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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.ExpenseTypes;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Booking;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Expense;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Workpackage;
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * Projectmanagement tool, Page edit project
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Edit")
public class Edit extends HttpServlet {
	
	/*
	 * 	method to handle get requests
	 * 	shows forms to edit project, workpackages, tasks, effort and expenses
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get user and project ID
		int userID = (int) request.getSession(false).getAttribute("ID");
		int projectID = Integer.parseInt(request.getParameter("projectID"));

		// Database connection
		DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
		
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
			
			// print html
			out.println(HTMLHeader.getInstance().printHeader("Edit " + project.getShortname(), "../", "Edit " + project.getShortname(), "", "<a href=\"Overview/Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>", false)
					  + "<section>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">"
					  + "<h2 class=\"no-margin\">Edit Project</h2>"
					  + "</div>"
					  + "<ul class=\"menu\">"
					  + "<li><a href=\"#project\">Project</a></li>"
					  + "<li><a href=\"#workpackages\">Workpackages</a></li>"
					  + "<li><a href=\"#tasks\">Tasks</a></li>"
					  + "<li><a href=\"#expenses\">Expenses</a></li>"
					  + "<li><a href=\"#effort\">Effort</a></li>"
					  + "</ul>"
					  + "</div>" 
					  + "<div class=\"row\">"
					  + "<hr>"); 
			
			// set currency field (CHF or EUR selected)
			String currency = "";
			if (project.getCurrency().equals("CHF")){
				currency = "<select name=\"currency\" required>"
						 + "<option value=\"CHF\" selected>CHF</option>"
						 + "<option value=\"EUR\">EUR</option>"
						 + "</select>";
			} else {
				currency = "<select name=\"currency\" required>"
						 + "<option value=\"CHF\">CHF</option>"
						 + "<option value=\"EUR\" selected>EUR</option>"
						 + "</select>";
			}
				
			// form to edit project
			out.println("<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin blue\" id=\"project\">Project</h3>"
					  + "<br>"
					  + "</div>"
					  + "<form method=\"post\" action=\"EditProject\" data-abide novalidate>"
					  + "<input type=\"hidden\" name=\"id\" value=\"" + project.getID() + "\">"
					  + "<p class=\"small-4 columns\">Project Name</p>"
					  + "<div class=\"small-8 end columns\">" 
					  + "<input type=\"text\" name=\"name\" value=\"" + project.getName() + "\">"
					  + "</div>"
					  + "<p class=\"small-4 columns\">Project Shortname</p>"
					  + "<div class=\"small-8 end columns\">" 
					  + "<input type=\"text\" name=\"shortname\" value=\"" + project.getShortname() + "\">"
					  + "</div>"
					  + "<p class=\"small-4 columns\">Project Start</p>"
					  + "<div class=\"small-8 end columns\">" 
					  + "<input type=\"text\" name=\"start\" value=\"" + project.getStart() + "\">"
					  + "</div>"
					  + "<p class=\"small-4 columns\">Project End</p>"
					  + "<div class=\"small-8 end columns\">" 
					  + "<input type=\"text\" name=\"end\" value=\"" + project.getEnd() + "\">"
					  + "</div>"
					  + "<p class=\"small-4 columns\">Currency</p>"
					  + "<div class=\"small-8 columns end\">" 
					  + currency
					  + "</div>"
					  + "<p class=\"small-4 columns\">Budget</p>"
					  + "<div class=\"small-8 end columns\">" 
					  + "<input type=\"number\" name=\"budget\" value=\"" + project.getBudget() + "\">"
					  + "</div>"
					  + "<p class=\"small-4 columns\">Partners</p>"
					  + "<div class=\"small-8 columns end\">" 
					  + "<input type=\"text\" name=\"partner\" value=\"" + project.getPartners() + "\">"
					  + "</div>"	
					  + "<div class=\"small-2 small-offset-10 columns end\">" 			  
					  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
					  + "</div>"
					  + "</form>"
					  + "<hr>"
					  + "</div>");
			
			// form to edit workpackages
			out.println("<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin blue\" id=\"workpackages\">Workpackages</h3>"
					  + "<br>"
					  + "</div>"
					  + "<table>"
					  + "<thead>"
					  + "<tr>"
					  + "<th class=\"th-300\">Name</th>"
					  + "<th class=\"th-200\">Start</th>"
					  + "<th class=\"th-200\">End</th>"
					  + "<th class=\"th-150\"></th>"
					  + "<th class=\"th-150\"></th>"
					  + "</tr>"
					  + "</thead>"
					  + "<tbody>");
			
			// form for every workpackage 
			for (Workpackage w : project.getWorkpackages()){
				out.println("<form method=\"post\" action=\"EditWorkpackage\" data-abide novalidate>"
						  + "<tr>"
						  + "<td>"
						  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
						  + "<input type=\"hidden\" name=\"id\" value=\"" + w.getID() + "\">"
						  + "<input type=\"text\" name=\"name\" value=\"" + w.getName() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"text\" name=\"start\" value=\"" + w.getStart() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"text\" name=\"end\" value=\"" + w.getEnd() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<a class=\"expanded alert button\" data-open=\"deleteWorkpackage" + w.getID() + "\">Delete</a>"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
						  + "</td>"
						  + "<tr>"
						  + "</form>"
						  + "<div class=\"reveal\" id=\"deleteWorkpackage" + w.getID() + "\" data-reveal>"
						  + "<h1 class=\"align-left\">Are you sure?</h1>"
						  + "<p class=\"lead\">If you delete this workpackage, all tasks and bookings that belong to the workpackage will be deleted too.</p>"
						  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteWorkpackage?projectID=" + project.getID() + "&workpackageID=" + w.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Workpackage</a>"
						  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
						  + "<span aria-hidden=\"true\">&times;</span>"
						  + "</button>"
						  + "</div>");
			}
			
			// form to edit tasks
			out.println("</tbody>"
					  + "</table>"
					  + "<hr>"
					  + "</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin blue\" id=\"tasks\">Tasks</h3>"
					  + "<br>"
					  + "</div>"
					  + "<div class=\"horizontal-scroll\">"
					  + "<table class=\"table\">"
					  + "<thead>"
					  + "<tr>"
					  + "<th class=\"th-300 first-table\">Name</th>"
					  + "<th class=\"th-150\">Start</th>"
					  + "<th class=\"th-150\">End</th>"
					  + "<th class=\"th-150\">PMs</th>"
					  + "<th class=\"th-200\">Budget</th>"
					  + "<th class=\"th-300\">Workpackage</th>"
					  + "<th class=\"th-150\"></th>"
					  + "<th class=\"th-150\"></th>"
					  + "</tr>"
					  + "</thead>"
					  + "<tbody>");
			
			// form for every task
			for (Task t : project.getTasks()){
				out.println("<form method=\"post\" action=\"EditTask\" data-abide novalidate>"
						  + "<tr>"
						  + "<td>"
						  + "<input type=\"hidden\" name=\"id\" value=\"" + t.getID() + "\">"
						  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
						  + "<input type=\"text\" name=\"name\" value=\"" + t.getName() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"text\" name=\"start\" value=\"" + t.getStart() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"text\" name=\"end\" value=\"" + t.getEnd() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"number\" name=\"pm\" value=\"" + t.getPMs() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<div class=\"input-group\">"
						  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
						  + "<input class=\"input-group-field\" type=\"number\" name=\"budget\" value=\"" + t.getBudget() + "\" required>"
						  + "</div>" 
						  + "</div>"
						  + "</td>"
						  + "<td>"
						  + "<select name=\"workpackage\" required>");
				// select field for every workpackage
				for (Workpackage w : project.getWorkpackages()){
					if (w.getID() == t.getWorkpackageID()){
						out.println("<option value=\"" + w.getID() + "\" selected>" + w.getName() + "</option>");
					} else {
						out.println("<option value=\"" + w.getID() + "\">" + w.getName() + "</option>");
					}
				}
			    out.println("</select>"
						  + "</td>"
						  + "<td>"			  
						  + "<a class=\"expanded alert button\" data-open=\"deleteTask" + t.getID() + "\">Delete</a>"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
						  + "</td>"
						  + "</tr>"		
						  + "</form>"
						  + "<div class=\"reveal\" id=\"deleteTask" + t.getID() + "\" data-reveal>"
						  + "<h1 class=\"align-left\">Are you sure?</h1>"
						  + "<p class=\"lead\"></p>"
						  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteTask?projectID=" + project.getID() + "&taskID=" + t.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Task</a>"
						  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
						  + "<span aria-hidden=\"true\">&times;</span>"
						  + "</button>"
						  + "</div>");
			}
			
			// form to edit expenses
			out.println("</tbody>"
					  + "</table>"
					  + "</div>"
					  + "<hr>"
					  + "</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin blue\" id=\"expenses\">Expenses</h3>"
					  + "<br>"
					  + "</div>"
					  + "<div class=\"horizontal-scroll\">"
					  + "<table class=\"table\">"
					  + "<thead>"
					  + "<tr>"
					  + "<th class=\"th-300 first-table\">Employee</th>"
					  + "<th class=\"th-200\">Type</th>"
					  + "<th class=\"th-300\">Description</th>"
					  + "<th class=\"th-150\">Date</th>"
					  + "<th class=\"th-200\">Costs</th>"
					  + "<th class=\"th-150\"></th>"
					  + "<th class=\"th-150\"></th>"
					  + "</tr>"
					  + "</thead>"
					  + "<tbody>");
			
			// form for every expense
			for (Expense ex : project.getExpenses()){
				out.println("<form method=\"post\" action=\"EditExpense\" data-abide novalidate>"
						  + "<tr>"
						  + "<td>"
						  + "<input type=\"hidden\" name=\"id\" value=\"" + ex.getID() + "\">"
						  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
						  + "<select name=\"employee\" required>");
				// select field with every employee
				for (Employee e : project.getEmployees()){
					if (e.getID() == ex.getEmployeeID()){
						out.println("<option value=\"" + e.getID() + "\" selected>" + e.getFullName() + "</option>");
					} else {
						out.println("<option value=\"" + e.getID() + "\">" + e.getFullName() + "</option>");
					}
				}
			    out.println("</select>"
						  + "</td>"
						  + "<td>"
						  + "<select name=\"type\" required>");
			    // select field with every expense type
				for (ExpenseTypes type : ExpenseTypes.values()){
					if (type.toString().equals(ex.getType())){
						out.println("<option selected>" + type + "</option>");
					} else {
						out.println("<option>" + type + "</option>");
					}
				}
			    out.println("</select>"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"text\" name=\"description\" value=\"" + ex.getDescription() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<input type=\"text\" name=\"date\" value=\"" + ex.getDate() + "\">"
						  + "</td>"
						  + "<td>"
						  + "<div class=\"input-group\">"
						  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
						  + "<input class=\"input-group-field\" type=\"number\" name=\"costs\" value=\"" + ex.getCosts() + "\" required>"
						  + "</div>" 
						  + "</td>"
						  + "<td>"	  
						  + "<a class=\"expanded alert button\" data-open=\"deleteExpense" + ex.getID() + "\">Delete</a>"
						  + "</td>"
						  + "<td>"	
						  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
						  + "</td>"
						  + "</tr>"						  
						  + "</form>"
						  + "<div class=\"reveal\" id=\"deleteExpense" + ex.getID() + "\" data-reveal>"
						  + "<h1 class=\"align-left\">Are you sure?</h1>"
						  + "<p class=\"lead\"></p>"
						  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteExpense?projectID=" + project.getID() + "&expenseID=" + ex.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Expense</a>"
						  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
						  + "<span aria-hidden=\"true\">&times;</span>"
						  + "</button>"
						  + "</div>");
			}
		
			// form to edit effort
			out.println("</tbody>"
					  + "</table>"
					  + "</div>"
					  + "<hr>"
					  + "</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin blue\" id=\"effort\">Effort</h3>"
					  + "</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">"
					  + "<ul class=\"accordion\" data-accordion data-multi-expand=\"false\" data-allow-all-closed=\"true\" id=\"effortAnchor\">");
			
			// table for every employee
			for (Employee e : project.getEmployees()){
			
				out.println("<li class=\"accordion-item\" data-accordion-item>"
						  + "<a href=\"#\" class=\"accordion-title\">"
						  + "<h4 class=\"no-margin blue\">" + e.getName() + "</h4>"
						  + "</a>"
						  + "<div class=\"accordion-content\" data-tab-content>"
						  + "<div class=\"horizontal-scroll\">"
						  + "<table class=\"table\">"
						  + "<thead>"
						  + "<tr>"
						  + "<th class=\"th-300\">Task</th>"
						  + "<th class=\"th-200\">Month</th>"
						  + "<th class=\"th-200\">Hours</th>"
						  + "<th class=\"th-150\"></th>"
						  + "<th class=\"th-150\"></th>"
						  + "</tr>"
						  + "</thead>"
						  + "<tbody>");
				
				// get bookings
				ArrayList<Booking> bookings = null;
				Effort effort = new Effort(project.getTasks(), this.getServletContext().getRealPath("/"));
				try {
					bookings = effort.getBookings(e.getID());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				// form for every booking
				for (Booking b : bookings){
					Task t = project.getTask(b.getTaskID());
					
					out.println("<tr>"
							  + "<td>"
							  + "<input type=\"text\" value=\"" + t.getName() + "\" disabled>"
							  + "</td>"
							  + "<td>");
					
					// get possible months where the hours can be booked
					String dates [][] = DateFormatter.getInstance().getMonths(t.getStartAsDate(), t.getNumberOfMonths());
					
					// get dates in format "August 2016"
					// add them to the ArrayList
					out.println("<form method=\"post\" action=\"EditEffort\" data-abide novalidate>"
							  + "<input type=\"hidden\" name=\"id\" value=\"" + b.getID() + "\">"
							  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
							  + "<input type=\"hidden\" name=\"taskID\" value=\"" + t.getID() + "\">"
							  + "<select name=\"month\" required>");
					
					// select option for every month
					for (int z = 0; z < t.getNumberOfMonths(); z++){
						int monthNbr = DateFormatter.getInstance().getMonthsBetween(project.getStart(), dates[0][z]);						
						
						if (monthNbr == b.getMonth()){
							out.println("<option value=\"" + monthNbr + "\" selected>"
										+ dates[1][z]
										+ "</option>");
						} else {
							out.println("<option value=\"" + monthNbr + "\">"
									+ dates[1][z]
									+ "</option>");			
						}
					}
				    out.println("</select>"
							  + "</td>"
							  + "<td>"	 
							  + "<input type=\"text\" name=\"hours\" value=\"" + b.getHours() + "\">"
							  + "</td>"
							  + "<td>"	 
							  + "<a class=\"expanded alert button\" data-open=\"deleteEffort" + b.getID() + "\">Delete</a>"
							  + "</td>"
							  + "<td>"	 
							  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
							  + "</td>"
							  + "</tr>"	 
							  + "</form>"
							  + "</div>"
							  + "<div class=\"reveal\" id=\"deleteEffort" + b.getID() + "\" data-reveal>"
							  + "<h1 class=\"align-left\">Are you sure?</h1>"
							  + "<p class=\"lead\"></p>"
							  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteEffort?projectID=" + project.getID() + "&effortID=" + b.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Effort</a>"
							  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
							  + "<span aria-hidden=\"true\">&times;</span>"
							  + "</button>"
							  + "</div>");
				}
				
				out.println("</tbody>"
						  + "</table>"
						  + "</div>"
						  + "<hr>"
						  + "</div>"
						  + "</li>");
			}
			
			out.println("</ul></div>"
					  + "</section>"
					  + HTMLFooter.getInstance().printFooter(true)
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
