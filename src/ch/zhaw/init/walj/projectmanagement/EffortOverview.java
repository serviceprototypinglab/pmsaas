package ch.zhaw.init.walj.projectmanagement;

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
import ch.zhaw.init.walj.projectmanagement.util.DateHelper;
import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.Project;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/Effort")
public class EffortOverview extends HttpServlet {
	
	// database access information
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "projectmanagement";
	private String userName	= "Janine";
	private String password	= "test123";

	// variable declaration
	private Project project;
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private Effort effort;
	
	private DateHelper dateFormatter = new DateHelper();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
				
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		DBConnection con = new DBConnection(url, dbName, userName, password);
		
		try {
			project = con.getProject(projectID);
			employees = project.getEmployees();
			effort = new Effort(project.getTasks());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		
		
		out.println("<!DOCTYPE html>" 
				  + "<html>" 
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">"
				  + "<title>Projects</title>" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />" 
				  + "</head>" 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-8 medium-6 columns\">"
				  // title
				  + "<h1>Effort</h1>"
				  + "</div>"
				  // menu
				  + "<div class=\"small-12 medium-6 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\">All Projects</a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\">New Project</a> " 
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\">New Employee</a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\">Help</a> " 
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\">Logout</a> " 
				  + "</div>" 
				  + "</div>"
				  + "</div>" 
				  + "</header>" 
				  // HTML section
				  + "<section>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-12 columns\">"
				  + "<h2>Effort</h2>"
				  + "</div>"
				  // line chart
				  + "<div class=\"small-12 no-padding columns\">"
				  + "<img src=\"../../Charts/EffortProject" + project.getID() + ".jpg\">"
				  + "</div>"
				  + "<div class=\"small-3 columns\">"
				  + "<span class=\"bold\">Employee</span>"
				  + "</div>"
				  + "<div class=\"small-3 columns\">"
				  + "<span class=\"bold\">Month</span>"
				  + "</div>"
				  + "<div class=\"small-3 end columns\">"
				  + "<span class=\"bold\">Task</span>"
				  + "</div>"
				  + "<div class=\"small-1 end columns\">"
				  + "<span class=\"bold\">Hours</span>"
				  + "</div>"
				  + "<div class=\"small-1 end columns\">"
				  + "<span class=\"bold\">~" + project.getCurrency() + "</span>"
				  + "</div>"
				  + "<div class=\"small-1 end columns\">"
				  + "<span class=\"bold\">" + project.getCurrency() + "/h</span>"
				  + "</div>"
				  + "");

		// effort for every employee and total effort
		double totalEffort = 0;		
		for(Employee employee : employees){
			try {
				double effortEmployee = effort.getEffortPerEmployee(employee.getID());
				totalEffort += effortEmployee;
				out.println("<div class=\"small-6 columns\">" + employee.getName() + "</div>"
						   +"<div class=\"small-3 columns\">" + effortEmployee + "h</div>"
						   + "<div class=\"small-3 columns\">"
						   + "<a class=\"button float-right\">"
						   + "<i class=\"fa fa-info\"></i> Details</a>"
						   + "</div>");
				
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		out.println("<div class=\"small-6 columns\">"
				  + "<span class=\"bold\">Total</span>"
				  + "</div>"
				  + "<div class=\"small-3 columns\">"
				  + "<span class=\"bold\">" + totalEffort + "h</span>"
				  + "</div>"
				  +"<div class=\"small-3 columns\">"
				  + "<a class=\"button float-right\">"
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
				  + "<i class=\"fa fa-plus\"></i> Add Task</a></div>"
				  + "<div class=\"small-12 no-padding columns\">"
				  // load gantt chart
				  + "<img src=\"../../Charts/GanttProject" + project.getID() + ".jpg\">"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  // delete project button
				  + "<div class=\"panel small-12 columns\">"
				  + "<div class=\"row round\">"
				  + "<a class=\"large expanded button\" id=\"editProject\"><i class=\"fa fa-pencil-square-o\"></i> Edit Project</a>"
				  + "<a class=\"large expanded alert button\" id=\"deleteProject\" data-open=\"delete\"><i class=\"fa fa-trash\"></i> Delete Project</a>"
				  + "</div>"
				  + "</div>"
				  
				  + "<div class=\"reveal\" id=\"delete\" data-reveal>"
				  + "<h1 class=\"align-left\">Are you sure?</h1>"
				  + "<p class=\"lead\">The project can not be restored after delete.</p>"
				  + "<a class=\"expanded alert button\" id=\"finalDelete\" href=\"../deleteProject?projectID=" + project.getID() + "\">Delete</a>"
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
	}
	
}
