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

import ch.zhaw.init.walj.projectmanagement.util.Booking;
import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.Expense;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;
import ch.zhaw.init.walj.projectmanagement.util.Workpackage;

/**
 * Servlet implementation class Edit
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Edit")
public class Edit extends HttpServlet {
	
	// TODO Enum 
	private String[] expenseTypes = {"Travel", "Overnight Stay", "Meals", "Office Supplies", "Events"};
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		int id = (int) request.getSession(false).getAttribute("ID");
		
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		DBConnection con = new DBConnection();
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
				
		if (project.getLeader() == id) {
			out.println(HTMLHeader.getInstance().getHeader("Edit " + project.getShortname(), "../", "Edit " + project.getShortname(), "", "<a href=\"Overview/Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  // HTML section with list of all projects
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
						
			ArrayList <Employee> employees = project.getEmployees();
			ArrayList <Workpackage> workpackages = project.getWorkpackages();
			ArrayList <ProjectTask> tasks = project.getTasks();
			ArrayList <Expense> expenses = project.getExpenses();
			Effort effort = new Effort(project.getTasks());
			
			String currency = "";
			if (project.getCurrency().equals("CHF")){
				currency = "<select name=\"currency\" required>"
						  + "<option value=\"CHF\" selected>CHF</option>"
						  + "<option value=\"EUR\">EUR</option>"
						  + "</select>";
			} else {
				currency = "<select name=\"pcurrency\" required>"
						  + "<option value=\"CHF\">CHF</option>"
						  + "<option value=\"EUR\" selected>EUR</option>"
						  + "</select>";
			}
				
			// print edit project item
			out.println("<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin edit\" id=\"project\">Project</h3>"
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
			
			// print edit workpackages item
			out.println("<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin edit\" id=\"workpackages\">Workpackages</h3>"
					  + "</div>");
			
			for (Workpackage w : workpackages){
				out.println("<form method=\"post\" action=\"EditWorkpackage\" data-abide novalidate>"
						  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
						  + "<input type=\"hidden\" name=\"id\" value=\"" + w.getID() + "\">"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Workpackage Name</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"name\" value=\"" + w.getName() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Workpackage Start</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"start\" value=\"" + w.getStart() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Workpackage End</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"end\" value=\"" + w.getEnd() + "\">"
						  + "</div>"
						  + "<div class=\"small-2 small-offset-8 columns\">" 			  
						  + "<a class=\"expanded alert button\" data-open=\"deleteWorkpackage" + w.getID() + "\">Delete</a>"
						  + "</div>"
						  + "<div class=\"small-2 columns end\">" 			  
						  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
						  + "</div>"
						  + "</form>"
						  + "<div class=\"reveal\" id=\"deleteWorkpackage" + w.getID() + "\" data-reveal>"
						  + "<h1 class=\"align-left\">Are you sure?</h1>"
						  + "<p class=\"lead\">If you delete this workpackage, all tasks and bookings that belong to the workpackage will be deleted too.</p>"
						  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteWorkpackage?projectID=" + project.getID() + "&workpackageID=" + w.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Workpackage</a>"
						  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
						  + "<span aria-hidden=\"true\">&times;</span>"
						  + "</button>"
						  + "</div>"
						  + "<hr>");
			}
			
			// print edit tasks item
			out.println("</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin edit\" id=\"tasks\">Tasks</h3>"
					  + "</div>");
			
			for (ProjectTask t : tasks){
				out.println("<form method=\"post\" action=\"EditTask\" data-abide novalidate>"
						  + "<input type=\"hidden\" name=\"id\" value=\"" + t.getID() + "\">"
						  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Task Name</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"name\" value=\"" + t.getName() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Task Start</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"start\" value=\"" + t.getStart() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Task End</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"end\" value=\"" + t.getEnd() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>PMs</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"number\" name=\"pm\" value=\"" + t.getPMs() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Budget</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<div class=\"input-group\">"
						  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
						  + "<input class=\"input-group-field\" type=\"number\" name=\"budget\" value=\"" + t.getBudget() + "\" required>"
						  + "</div>" 
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Workpackage</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<select name=\"workpackage\" required>");
				for (Workpackage w : workpackages){
					if (w.getID() == t.getWorkpackageID()){
						out.println("<option value=\"" + w.getID() + "\" selected>" + w.getName() + "</option>");
					} else {
						out.println("<option value=\"" + w.getID() + "\">" + w.getName() + "</option>");
					}
				}
			    out.println("</select>"
						  + "</div>"
						  + "<div class=\"small-2 small-offset-8 columns\">" 			  
						  + "<a class=\"expanded alert button\" data-open=\"deleteTask" + t.getID() + "\">Delete</a>"
						  + "</div>"
						  + "<div class=\"small-2 columns end\">" 			  
						  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
						  + "</div>"
						  + "</form>"
						  + "<div class=\"reveal\" id=\"deleteTask" + t.getID() + "\" data-reveal>"
						  + "<h1 class=\"align-left\">Are you sure?</h1>"
						  + "<p class=\"lead\"></p>"
						  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteTask?projectID=" + project.getID() + "&taskID=" + t.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Task</a>"
						  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
						  + "<span aria-hidden=\"true\">&times;</span>"
						  + "</button>"
						  + "</div>"
						  + "<hr>");
			}
			
			// print edit expenses item
			out.println("</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin edit\" id=\"expenses\">Expenses</h3>"
					  + "</div>");
			
			for (Expense ex : expenses){
				out.println("<form method=\"post\" action=\"EditExpense\" data-abide novalidate>"
						  + "<input type=\"hidden\" name=\"id\" value=\"" + ex.getID() + "\">"
						  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Employee</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<select name=\"employee\" required>");
				for (Employee e : employees){
					if (e.getID() == ex.getEmployeeID()){
						out.println("<option value=\"" + e.getID() + "\" selected>" + e.getName() + "</option>");
					} else {
						out.println("<option value=\"" + e.getID() + "\">" + e.getName() + "</option>");
					}
				}
			    out.println("</select>"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Type</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<select name=\"type\" required>");
				for (String s : expenseTypes){
					if (s.equals(ex.getType())){
						out.println("<option selected>" + s + "</option>");
					} else {
						out.println("<option>" + s + "</option>");
					}
				}
			    out.println("</select>"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Description</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"description\" value=\"" + ex.getDescription() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Date</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<input type=\"text\" name=\"date\" value=\"" + ex.getDate() + "\">"
						  + "</div>"
						  + "<div class=\"small-4 columns\">" 
						  + "<p>Costs</p>"
						  + "</div>"
						  + "<div class=\"small-8 end columns\">" 
						  + "<div class=\"input-group\">"
						  + "<span class=\"input-group-label\">" + project.getCurrency() + "</span>"
						  + "<input class=\"input-group-field\" type=\"number\" name=\"costs\" value=\"" + ex.getCosts() + "\" required>"
						  + "</div>" 
						  + "</div>" 
						  + "<div class=\"small-2 small-offset-8 columns\">" 			  
						  + "<a class=\"expanded alert button\" data-open=\"deleteExpense" + ex.getID() + "\">Delete</a>"
						  + "</div>"
						  + "<div class=\"small-2 columns end\">" 			  
						  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
						  + "</div>"
						  + "</form>"
						  + "<div class=\"reveal\" id=\"deleteExpense" + ex.getID() + "\" data-reveal>"
						  + "<h1 class=\"align-left\">Are you sure?</h1>"
						  + "<p class=\"lead\"></p>"
						  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"deleteExpense?projectID=" + project.getID() + "&expenseID=" + ex.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Expense</a>"
						  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
						  + "<span aria-hidden=\"true\">&times;</span>"
						  + "</button>"
						  + "</div>"
						  + "<hr>");
			}
		
			// print edit effort item
			out.println("</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">" 
					  + "<h3 class=\"no-margin edit\" id=\"effort\">Effort</h3>"
					  + "</div>"
					  + "<ul class=\"menu\">");
			for (Employee e : employees){
				out.println("<li><a href=\"#"+ e.getKuerzel() + "\">"+ e.getName() + "</a></li>");
			}
			out.println("</ul>"
					  + "<hr>"
					  + "</div>");
			
			for (Employee e : employees){
			
				out.println("<div class=\"row\">"
						  + "<div class=\"small-12 columns\">" 
						  + "<h4 class=\"no-margin blue\" id=\"" + e.getKuerzel() + "\">" + e.getName() + "</h4>"
						  + "</div>"
						  + "</div>");
				
				ArrayList<Booking> bookings = null;
				try {
					bookings = effort.getBookings(e.getID());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				for (Booking b : bookings){
					ProjectTask t = project.getTask(b.getTaskID());
					
					out.println("<div class=\"row\">"
							  + "<div class=\"small-12 columns\">" 
							  + "<h5 class=\"no-margin\">" + t.getName() + "</h5>"
							  + "</div>");
					
					// get possible months where the hours can be booked
					String dates [][] = DateFormatter.getInstance().getMonths(t.getStartAsDate(), t.getNumberOfMonths());
					
					// get dates in format "August 2016"
					// add them to the ArrayList

					out.println("<form method=\"post\" action=\"EditEffort\" data-abide novalidate>"
							  + "<input type=\"hidden\" name=\"id\" value=\"" + b.getID() + "\">"
							  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">"
							  + "<input type=\"hidden\" name=\"taskID\" value=\"" + t.getID() + "\">"
							  + "<div class=\"small-4 columns\">" 
							  + "<p>Month</p>"
							  + "</div>"
							  + "<div class=\"small-8 end columns\">" 
							  + "<select name=\"month\" required>");
					
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
							  + "</div>"
							  + "<div class=\"small-4 columns\">" 
							  + "<p>Hours</p>"
							  + "</div>"
							  + "<div class=\"small-8 end columns\">" 
							  + "<input type=\"text\" name=\"hours\" value=\"" + b.getHours() + "\">"
							  + "</div>"
							  + "<div class=\"small-2 small-offset-8 columns\">" 			  
							  + "<a class=\"expanded alert button\" data-open=\"deleteEffort" + b.getID() + "\">Delete</a>"
							  + "</div>"
							  + "<div class=\"small-2 columns end\">" 			  
							  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
							  + "</div>"
							  + "</form>"
							  + "<hr>"
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
			}
			
			
			
			out.println("</div>"
					  + "</section>"
					  + "</div>"
					  + "<script src=\"../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		}
	}
}
