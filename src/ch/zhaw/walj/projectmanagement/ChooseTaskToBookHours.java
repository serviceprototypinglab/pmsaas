package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/Overview/bookHours/chooseTask")
public class ChooseTaskToBookHours extends HttpServlet{

	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	private DBConnection con = new DBConnection(url, dbName, userName, password);
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employee"));
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				

		ArrayList<Task> tasks = new ArrayList<Task>(); 
		ArrayList<Task> finalAssignedTasks = new ArrayList<Task>(); 
		tasks.addAll(project.getTasks());
		ArrayList<Integer> assignedTasks = null;
		try {
			assignedTasks = con.getAssignments(employeeID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		String message = "";
		String disabled = "";
		
		
		if (!tasks.isEmpty()){
			for (Task task : tasks){
				for (int assignedTask : assignedTasks){
					if (task.getID() == assignedTask){
						finalAssignedTasks.add(task);
					}
				}
			}
		}
				
		if (finalAssignedTasks.isEmpty()){
			message = "<div class=\"row\">"
					+ "<div class=\"callout alert\">"
					+ "<h5>The employee is not assigned to any task</h5>"
					+ "<p>Assign the employee to a task first.</p>"
					+ "<a href=\"../assignEmployee?projectID=" + projectID + "\">Click here to assign employees to tasks</a>"
					+ "</div></div>";
			disabled = "disabled";
		}
		
		
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
				+ "<html>\n" 
					+ "<head>\n" 
						+ "<meta charset=\"UTF-8\">\n"
						+ "<title>Book Hours</title>\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />\n"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />\n" 
					+ "</head>\n" 
					+ "<body>\n"
						+ "<div id=\"wrapper\">\n" 
							+ "<header>\n" 
								+ "<div class=\"row\">\n" 
									+ "<div class=\"small-8 medium-6 columns\">"
									+ "<h1>Book Hours</h1><a href=\"../Project?id=" + projectID + "\" class=\"back\">"
											+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>\n"
									+ "<div class=\"small-12 medium-6 columns\">\n" 
										+ "<div class=\"float-right menu\">\n"
											+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
											+ "<a href=\"../newProject\" class=\"button\">New Project</a>\n" 
											+ "<a href=\"../newEmployee\" class=\"button\">New Employee</a>\n"
											+ "<a href=\"help\" class=\"button\">Help</a>\n" 
											+ "<a href=\"logout\" class=\"button\">Logout</a>\n" 
										+ "</div>\n" 
									+ "</div>\n"
								+ "</div>\n" 
							+ "</header>\n"
							+ message
							+ "<section>\n");
		out.println("<div class=\"row\">"
				+ "<form method=\"post\" action=\"../bookHours/chooseTask\" data-abide novalidate>"
			
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p></div>"

				+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				+ "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">"
				+ "<label class=\"small-12 medium-6 end columns\">Task "
				+ "<span class=\"grey\">multiple options possible</span> <select name=\"tasks\" size=\"5\" multiple required " + disabled +">");
				
				for (Task task : finalAssignedTasks){
					out.println("<option value =\"" + task.getID() + ";" + task.getName() + "\">" + task.getName() + "</option>");
				}				
				
				out.println("</select></label></div>");
	
	out.println("<div class=\"row\">"
			+ "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Hours  <i class=\"fa fa-chevron-right\"></i></button>"
			+ "</div>");
	
	out.println("</section></div><script src=\"../../js/vendor/jquery.js\"></script><script src=\"../../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
			
		
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
						
		
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employeeID = Integer.parseInt(request.getParameter("employeeID"));
		
		int assignmentID = 0;
		
		String[] task = request.getParameterValues("tasks");
		ArrayList<Integer> taskIDs = new ArrayList<Integer>();
		ArrayList<String> taskNames = new ArrayList<String>();
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Task> usedTasks = new ArrayList<Task>();
		
		
		String message = "";
				
		for(String s : task){
			String[] helper = s.split(";");
			taskIDs.add(Integer.parseInt(helper[0]));
			taskNames.add(helper[1]);
		}
		
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		tasks = project.getTasks();
		
		for (Task t : tasks){
			for (int i : taskIDs){
				if(i == t.getID()){
					usedTasks.add(t);
				}
			}
		}
				
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
				+ "<html>\n" 
					+ "<head>\n" 
						+ "<meta charset=\"UTF-8\">\n"
						+ "<title>Book Hours</title>\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/foundation.css\" />\n"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/style.css\" />\n" 
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/font-awesome/css/font-awesome.min.css\" />\n" 
					+ "</head>\n" 
					+ "<body>\n"
						+ "<div id=\"wrapper\">\n" 
							+ "<header>\n" 
								+ "<div class=\"row\">\n" 
									+ "<div class=\"small-8 medium-6 columns\">"
									+ "<h1>Book Hours</h1><a href=\"Project?id=" + projectID + "\" class=\"back\">"
											+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a></div>\n"
									+ "<div class=\"small-12 medium-6 columns\">\n" 
										+ "<div class=\"float-right menu\">\n"
											+ "<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
											+ "<a href=\"../newProject\" class=\"button\">New Project</a>\n" 
											+ "<a href=\"../newEmployee\" class=\"button\">New Employee</a>\n"
											+ "<a href=\"help\" class=\"button\">Help</a>\n" 
											+ "<a href=\"logout\" class=\"button\">Logout</a>\n" 
										+ "</div>\n" 
									+ "</div>\n"
								+ "</div>\n" 
							+ "</header>\n"
							+ message
							+ "<section>\n");
		out.println("<div class=\"row\">"
				+ "<form method=\"post\" action=\"../bookHours\" data-abide novalidate>"
			
				+ "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				+ "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in the form</p></div>"
				
				+ "<h3></h3></br>" //TODO Titel überlegen
				+ "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
				+ "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">");
				
				for (int i = 0; i < usedTasks.size(); i++){

					ArrayList<String> months = new ArrayList<String>();
					ArrayList<String> dates = new ArrayList<String>();
					String start = usedTasks.get(i).getStart();
					int nbrOfMonths = usedTasks.get(i).getNumberOfMonths();
					
					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
					SimpleDateFormat formatMonthYear = new SimpleDateFormat("MMMM yyyy");
					try {
						Calendar c = Calendar.getInstance();
						Date date = format.parse(start);
						c.setTime(date);
						dates.add(format.format(c.getTime()));
						months.add(formatMonthYear.format(c.getTime()));
						for (int y = 0; y < nbrOfMonths; y++){
							c.add(Calendar.MONTH, 1);
							dates.add(format.format(c.getTime()));
							months.add(formatMonthYear.format(c.getTime()));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					try {
						assignmentID = con.getAssignment(employeeID, taskIDs.get(i));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					out.println("<input type=\"hidden\" name=\"assignmentID\" value=\"" + assignmentID + "\">");
					
					out.println("<p class=\"small-2 columns bold\">" + taskNames.get(i) + "</p>"
								+ "<p class=\"small-1 columns\">Hours</p>"
								+ "<div class=\"small-4 columns\">"
								+ "<input type=\"number\" name=\"hours\" required>"
								+ "</div>"
								+ "<p class=\"small-1 columns\">Month</p>"
								+ "<select name=\"months\" class=\"small-4 columns\" required>"
								+ "<option></option>");
					int z = 0;
					for (String s : months){
						out.println("<option value=\"" + dates.get(z) + "\">"
									+ s
									+ "</option>");
						z++;
					}
					out.println("</select>");
				}				
				
		out.println("</div>"
				+"<div class=\"row\">"
				+ "<a href=\"chooseTask?projectID=" + projectID +"&employee=" + employeeID + "\" class=\"small-3 columns large button back-button\"><i class=\"fa fa-chevron-left\"></i>  Choose Task</a>"
				+ "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Book Hours  <i class=\"fa fa-chevron-right\"></i></button>"
				+ "</div>");
		
		out.println("</section></div><script src=\"../../js/vendor/jquery.js\"></script><script src=\"../../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
			
	}
}
