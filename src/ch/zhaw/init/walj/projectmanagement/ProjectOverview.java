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

import ch.zhaw.init.walj.projectmanagement.chart.GanttChart;
import ch.zhaw.init.walj.projectmanagement.chart.LineChart;
import ch.zhaw.init.walj.projectmanagement.chart.PieChart;
import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.Expense;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.NumberFormatter;
import ch.zhaw.init.walj.projectmanagement.util.Project;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/Project")
public class ProjectOverview extends HttpServlet {

	// variable declaration
	private Project project;
	private ArrayList<Expense> expenses = new ArrayList<Expense>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private Effort effort;
	private PieChart pieChart;
	private LineChart lineChart;
	private GanttChart ganttChart;
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
				
		int id = (int) request.getSession(false).getAttribute("ID");
		
		int projectID = Integer.parseInt(request.getParameter("id"));
		
		DBConnection con = new DBConnection();
		
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
				
		if (project.getLeader() == id) {
	
			expenses = project.getExpenses();
			employees = project.getEmployees();
			effort = new Effort(project.getTasks());
			
			pieChart = new PieChart(project);
			lineChart = new LineChart(project);
			ganttChart = new GanttChart(project);
			try {
				pieChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
				lineChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
				ganttChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/");
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			for (Employee e : employees){
				lineChart.createChart("/home/janine/workspace/Projektverwaltung/WebContent/Charts/", e.getID());
			}
			
			
			try {
				out.println(HTMLHeader.getInstance().getHeader(project.getShortname(), "../../", project.getShortname())
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
						  + "Remaining: </br>" + project.getCurrency() + " " + NumberFormatter.getInstance().formatDouble(pieChart.getRemainingBudget())
						  + "</br>"
						  + "<span class=\"legend-2 smallbadge\"></span> "
						  // used budget
						  + "Spent: </br>" + project.getCurrency() + " " + NumberFormatter.getInstance().formatDouble(pieChart.getUsedBudget()) 
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
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			// print expenses
			for(Expense ex : expenses){
				Employee employee = project.getSpecificEmployee(ex.getEmployeeID());
				out.println("<tr>"
						  + "<td>" + employee.getName() + "</td>"
						  + "<td title=\"" + ex.getDescription() + "\">" + ex.getType() + "</td>"
						  + "<td>" + DateFormatter.getInstance().formatDate(ex.getDate()) + "</td>"
						  + "<td>" + project.getCurrency() + "</td>"
						  + "<td class=\"align-right\">" + NumberFormatter.getInstance().formatDouble(ex.getCosts()) + "</td>"
						  + "</tr>");
			}
			
			out.println("</tbody>"
					  + "</tfoot>"
					  + "<tr>"
					  + "<td></td>"
					  + "<td></td>"
					  + "<td>Total</td>"
					  + "<td>" + project.getCurrency() + "</td>"
					  + "<td class=\"align-right\">" + project.getTotalExpensesAsString() + "</td>"
					  + "</tr>"
					  + "</tfoot>"
					  + "</table>"
					  + "</div>"
					  + "</div>"
					  
					  // effort panel
					  + "<div class=\"panel small-12 medium-6 columns\">"
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
					  // line chart
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
			for(Employee employee : employees){
				try {
					double effortEmployee = effort.getEffortPerEmployee(employee.getID());
					totalEffort += effortEmployee;
					if (effortEmployee == 0){
						disabled ="disabled ";	
						link = "";
					} else {
						disabled = "";
						link = "Effort?projectID=" + project.getID() + "&employeeID=" + employee.getID();
					}
					
					out.println("<div class=\"small-6 columns\">" + employee.getName() + "</div>"
							   +"<div class=\"small-3 columns\">" + NumberFormatter.getInstance().formatHours(effortEmployee) + " h</div>"
							   + "<div class=\"small-3 columns\">"
							   + "<a class=\"" + disabled + "button float-right\" href=\"" + link + "\">"
							   + "<i class=\"fa fa-info\"></i> Details</a>"
							   + "</div>");
					
				} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (totalEffort == 0) {
				disabled ="disabled ";	
				link = "";
			} else {
				disabled = "";
				link = "Effort?projectID=" + project.getID();
			}
			
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
					  + "</div>"
					  // Panel for workpackages and tasks (Gantt chart)
					  + "<div class=\"panel small-12 columns\">"
					  + "<div class=\"row round\">"
					  + "<div class=\"small-7 columns\">"
					  + "<h2>Workpackages & Tasks</h2>"
					  + "</div>"
					  + "<div class=\"small-5 columns align-right padding-top-10\">"
					  // button to add workpackages
					  + "<a class=\"button\" href=\"addWorkpackage?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Add Workpackage</a> "
					  // button to add tasks
					  + "<a class=\"button\" href=\"addTask?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-plus\"></i> Add Task</a>"
					  + "</div>"
					  + "<div class=\"small-12 no-padding columns\">"
					  // load gantt chart
					  + "<img src=\"../../Charts/GanttProject" + project.getID() + ".jpg\">"
					  + "</div>"
					  + "</div>"
					  + "</div>"
					  // delete project button
					  + "<div class=\"panel small-12 columns\">"
					  + "<div class=\"row round\">"
					  + "<a class=\"large expanded button\" id=\"editProject\" href=\"../Edit?projectID=" + project.getID() + "\"><i class=\"fa fa-pencil-square-o\"></i> Edit Project</a>"
					  + "<a class=\"large expanded warning button\" id=\"deleteProject\" data-open=\"delete\"><i class=\"fa fa-archive\"></i> Archive Project</a>"
					  + "</div>"
					  + "</div>"
					  
					  + "<div class=\"reveal\" id=\"delete\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded warning button\" id=\"finalDelete\" href=\"../archiveProject?projectID=" + project.getID() + "\"><i class=\"fa fa-archive\"></i> Archive Project</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>"
					  
					  + "</section>"
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
