package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.chart.GanttChart;
import ch.zhaw.init.walj.projectmanagement.util.chart.LineChart;
import ch.zhaw.init.walj.projectmanagement.util.chart.PieChart;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Expense;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.format.NumberFormatter;

/**
 * project management tool, project overview page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/Project")
public class ProjectOverview extends HttpServlet {

	// variable declaration
	private Project project;
	/*
	 * method to handle get requests
	 * prints panels for overview, budget, effort and workpackages & tasks
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String path = this.getServletContext().getRealPath("/");
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
				
		// get user id from session
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// get project id
		int projectID = Integer.parseInt(request.getParameter("id"));
		
		// Database connection
		DBConnection con = new DBConnection(path);
		
		// get project
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			// send redirect to project not found page
			String url = "/Projektverwaltung/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is project leader
		if (project.getLeader() == id) {
	
			// get expenses, employees, effort, charts
			ArrayList<Expense> expenses = project.getExpenses();
			ArrayList<Employee> employees = project.getEmployees();
			Effort effort = new Effort(project.getTasks(), path);
			
			PieChart pieChart = new PieChart(project, path);
			LineChart lineChart = new LineChart(project, path);
			GanttChart ganttChart = new GanttChart(project);
			
			// create charts and save them in Charts folder
			try {
				pieChart.createChart(path + "/Charts/");
				lineChart.createChart(path + "/Charts/");
				ganttChart.createChart(path + "/Charts/");
			} catch (NumberFormatException | SQLException | ParseException e) {
				e.printStackTrace();
			}
			
			// create chart for every employee and save them in Charts folder
			for (Employee e : employees){
				lineChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/", e.getID());
			}
			
			
			out.println(HTMLHeader.getInstance().printHeader(project.getShortname(), "../../", project.getShortname(), "")
					  // HTML section with panels
					  + "<section>"
					  + "<div class=\"row\">"
					  // panel for project overview
					  + "<div class=\"panel small-12 columns\">"
					  // project duration
					  + "<div class=\"row round\">"
					  + "<div class=\"small-4 medium-2 columns bold\">Projectduration</div>" 
					  + "<div class=\"small-8 medium-4 columns\">" + project.getDuration() + "</div>"
					  // total budget
					  + "<div class=\"small-4 medium-2 columns bold\">Budget</div>"
					  + "<div class=\"small-8 medium-4 columns\">" + project.getCurrency() 
					  + " " + NumberFormatter.getInstance().formatDouble(project.getBudget()) + "</div>"
					  // number of workpackages
					  + "<div class=\"small-4 medium-2 columns bold\">Workpackages</div>" 
					  + "<div class=\"small-8 medium-4 columns\">" + project.nbrOfWorkpackages() + "</div>"
					  // number of tasks
				  	  + "<div class=\"small-4 medium-2 columns bold\">Tasks</div>"
					  + "<div class=\"small-8 medium-4 columns\">" + project.nbrOfTasks() + "</div>"
					  // number of employees
					  + "<div class=\"small-4 medium-2 columns bold\">Employees</div>" 
					  + "<div class=\"small-8 medium-4 columns\">" + project.nbrOfEmployees() + "</div>"
					  // number of partners
					  + "<div class=\"small-4 medium-2 columns bold\">Partner</div>"
					  + "<div class=\"small-8 medium-4 columns\">" + project.getPartners() + "</div>"
					  + "</div>"
					  + "</div>"
					  // budget panel
					  + "<div class=\"panel small-12 medium-6 columns\">"
					  + "<div class=\"row round\">"
					  + "<div class=\"small-12 columns\">"
					  + "<h2>Budget</h2>"
					  + "</div>"
					  + "<div class=\"small-12 medium-8 columns\">"
					  // load pie chart
					  + "<img src=\"../../Charts/BudgetProject" + project.getID() + ".jpg\">"
					  + "</div>"
					  // pie chart legend
					  + "<div class=\"small-12 medium-4 columns\">"
					  + "</br>"
					  + "<span class=\"legend-1  smallbadge\"></span> "
					  // remaining budget
					  + "Remaining: </br>" + project.getCurrency() + " " + NumberFormatter.getInstance().formatDouble(con.getRemainingBudget(project))
					  + "</br>"
					  + "<span class=\"legend-2 smallbadge\"></span> "
					  // used budget
					  + "Spent: </br>" + project.getCurrency() + " " + NumberFormatter.getInstance().formatDouble(con.getUsedBudget(project)) 
					  + "</div>"
					  + "<div class=\"small-8 columns\">"
					  // Expenses
					  + "<h3>Expenses</h3>"
					  + "</div>"
					  + "<div class=\"small-4 columns\">"
					  // add expense button
					  + "<a class=\"button\" href=\"addExpense?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Add Expenses</a>"
					  + "</div>"
					  // expenses table
					  + "<table class=\"scroll\">"
					  + "<thead>"
					  + "<tr>"
					  + "<th>Employee</th>"
					  + "<th>Type</th>"
					  + "<th>Date</th>"
					  + "<th>Costs</th>"
					  + "<th></th>"
					  + "</tr>"
					  + "</thead>"
					  + "<tbody>");
			
			
			// print expenses
			for(Expense ex : expenses){
				// get employee
				Employee employee = project.getSpecificEmployee(ex.getEmployeeID());
				
				// print table row
				out.println("<tr>"
						  + "<td>" + employee.getName() + "</td>"
						  + "<td title=\"" + ex.getDescription() + "\">" + ex.getType() + "</td>"
						  + "<td>" + DateFormatter.getInstance().formatDate(ex.getDate()) + "</td>"
						  + "<td>" + project.getCurrency() + "</td>"
						  + "<td class=\"align-right\">" + NumberFormatter.getInstance().formatDouble(ex.getCosts()) + "</td>"
						  + "</tr>");
			}
			
			// total expenses and end of panel
			out.println("</tbody>"
					  + "</tfoot>"
					  + "<tr>"
					  + "<td></td>"
					  + "<td></td>"
					  + "<td>Total</td>"
					  + "<td>" + project.getCurrency() + "</td>"
					  + "<td class=\"align-right\">" + NumberFormatter.getInstance().formatDouble(project.getTotalExpenses()) + "</td>"
					  + "</tr>"
					  + "</tfoot>"
					  + "</table>"
					  + "</div>"
					  + "</div>");
					  
			// effort panel
			out.println("<div class=\"panel small-12 medium-6 columns\">"
					  + "<div class=\"row round\">"
					  + "<div class=\"small-4 columns\">"
					  + "<h2>Effort</h2>"
					  + "</div>"
					  + "<div class=\"small-8 columns align-right padding-top-10\">"
					  // button to assign employees
					  + "<a class=\"button\" href=\"assignEmployee?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Assign Employees</a> "
					  // button to book hours
					  + "<a class=\"button\" href=\"bookHours?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-clock-o\"></i> Book Hours</a>"
					  + "</div>"
					  // line chart planned and booked PMs
					  + "<div class=\"small-12 no-padding columns\">"
					  + "<img src=\"../../Charts/EffortProject" + project.getID() + ".jpg\">"
					  + "</div>"
					  // Effort per employee
					  + "<div class=\"small-6 columns\">"
					  + "<span class=\"bold\">Employee</span>"
					  + "</div>"
					  + "<div class=\"small-4 end columns\">"
					  + "<span class=\"bold\">Effort</span>"
					  + "</div>");
	
			// effort for every employee and total effort
			double totalEffort = 0;		
			String disabled = "";
			String link = "";
			// effort for every employee
			for(Employee employee : employees){
				// get effort of employee and add it to total effort
				double effortEmployee = effort.getEffortPerEmployee(employee.getID());
				totalEffort += effortEmployee;
				
				// disable button if there are no bookings for this employee
				if (effortEmployee == 0){
					disabled ="disabled ";	
					link = "";
				} else {
					disabled = "";
					link = "Effort?projectID=" + project.getID() + "&employeeID=" + employee.getID();
				}
				
				// print effort
				out.println("<div class=\"small-6 columns\">" + employee.getName() + "</div>"
						   +"<div class=\"small-3 columns\">" + NumberFormatter.getInstance().formatHours(effortEmployee) + " h</div>"
						   + "<div class=\"small-3 columns\">"
						   + "<a class=\"" + disabled + "button float-right\" href=\"" + link + "\">"
						   + "<i class=\"fa fa-info\"></i> Details</a>"
						   + "</div>");
			}
			
			// disable button if there are no bookings
			if (totalEffort == 0){
				disabled ="disabled ";	
				link = "";
			} else {
				disabled = "";
				link = "Effort?projectID=" + project.getID();
			}
				
			// print effort total and end of panel 
			out.println("<div class=\"small-6 columns\">"
					  + "<span class=\"bold\">Total</span>"
					  + "</div>"
					  + "<div class=\"small-3 columns\">"
					  + "<span class=\"bold\">" + NumberFormatter.getInstance().formatHours(totalEffort) + " h</span>"
					  + "</div>"
					  +"<div class=\"small-3 columns\">"
					  + "<a class=\"" + disabled + "button float-right\" href=\"" + link + "\">"
					  + "<i class=\"fa fa-info\"></i> Details</a>"
					  + "</div>"
					  + "</div>"
					  + "</div>");
			
			// Panel for workpackages and tasks (Gantt chart)
			out.println("<div class=\"panel small-12 columns\">"
					  + "<div class=\"row round\">"
					  + "<div class=\"small-7 columns\">"
					  + "<h2>Workpackages & Tasks</h2>"
					  + "</div>"
					  + "<div class=\"small-5 columns align-right padding-top-10\">"
					  // button to edit weight
					  + "<a class=\"button\" href=\"editWeight?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-pencil-square-o\"></i> Edit Weight</a> "
					  // button to add workpackages
					  + "<a class=\"button\" href=\"addWorkpackage?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Add Workpackage</a> "
					  // button to add tasks
					  + "<a class=\"button\" href=\"addTask?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Add Task</a>"
					  + "</div>"
					  + "<div class=\"small-12 no-padding columns\">"
					  // print gantt chart
					  + "<img src=\"../../Charts/GanttProject" + project.getID() + ".jpg\">"
					  + "</div>"
					  + "</div>"
					  + "</div>"
					  // share/edit/archive project buttons
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns button-bar\">"
					  + "<div class=\"expanded large button-group\">"
					  + "<a class=\"large button\" id=\"shareProject\" href=\"../Share?projectID=" + project.getID() + "\"><i class=\"fa fa-share\"></i> Share Project</a>"
					  + "<a class=\"large button\" id=\"editProject\" href=\"../Edit?projectID=" + project.getID() + "\"><i class=\"fa fa-pencil-square-o\"></i> Edit Project</a>"
					  + "<a class=\"large button\" id=\"deleteProject\" data-open=\"delete\"><i class=\"fa fa-archive\"></i> Archive Project</a>"
					  + "</div>"
					  + "</div>"
					  + "</div>"	
					  // archive project window
					  + "<div class=\"reveal\" id=\"delete\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded warning button\" id=\"finalDelete\" href=\"../archiveProject?projectID=" + project.getID() + "\"><i class=\"fa fa-archive\"></i> Archive Project</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>"					  
					  + "</section>"
					  + HTMLFooter.getInstance().printFooter(false)
					  + "</div>"
					  // required JavaScript
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
}
