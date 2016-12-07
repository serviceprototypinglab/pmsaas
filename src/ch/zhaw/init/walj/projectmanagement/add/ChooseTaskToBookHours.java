package ch.zhaw.init.walj.projectmanagement.add;

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

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task;

/**
 * Projectmanagement tool, Page to book hours (choose task)
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/bookHours/chooseTask")
public class ChooseTaskToBookHours extends HttpServlet{

	// connection to database
	private DBConnection con = new DBConnection();
	
	/*
	 * method to handle get requests
	 * Form to book hours, choose tasks
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user ID
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// variable declaration, get parameters
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
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
		if (project.getLeader() == id){
			
			// get employee ID from parameter
			int employeeID = Integer.parseInt(request.getParameter("employee"));
					
			// get all the tasks, the employee is already assigned to
			// get all tasks from the project
			ArrayList<Task> tasks = new ArrayList<Task>(); 
			ArrayList<Task> finalAssignedTasks = new ArrayList<Task>(); 
			tasks.addAll(project.getTasks());
			ArrayList<Integer> assignedTasks = null;
			try {
				assignedTasks = con.getAssignedTasks(employeeID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
					
			String message = "";
			String disabled = "";
			
			// get the tasks from this project, the employee is assigned to
			if (!tasks.isEmpty()){
				for (Task task : tasks){
					for (int assignedTask : assignedTasks){
						if (task.getID() == assignedTask){
							finalAssignedTasks.add(task);
						}
					}
				}
			}
			
			// if the employee is not assigned to any task
			if (finalAssignedTasks.isEmpty()){
				message = "<div class=\"row\">"
						+ "<div class=\"callout alert\">"
						+ "<h5>The employee is not assigned to any task</h5>"
						+ "<p>Assign the employee to a task first.</p>"
						+ "<a href=\"../assignEmployee?projectID=" + projectID + "\">Click here to assign employees to tasks</a>"
						+ "</div></div>";
				disabled = "disabled";
			}		
	
			// Print HTML head and header
			out.println(HTMLHeader.getInstance().printHeader("Book Hours", "../../../", "Book Hours", "", "<a href=\"../Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>"));
			
			// print HTML section with form
			out.println("<section>"
					  + message
					  + "<div class=\"row\">"
					  + "<h3>Choose Task(s)</h3>" 
					  + "<form method=\"post\" action=\"../bookHours/chooseTask\" data-abide novalidate>"
					  
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Choose at least 1 task.</p></div>"
	
					  // project and employee ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
					  + "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">"
					  
					  // select (multiple) tasks
					  + "<label class=\"small-12 medium-6 end columns\">Task "
					  + "<span class=\"grey\">multiple options possible</span> <select name=\"tasks\" size=\"5\" multiple required " + disabled +">");
			
			// option for every task
			for (Task task : finalAssignedTasks){
				out.println("<option value =\"" + task.getID() + ";" + task.getName() + "\">" + task.getName() + "</option>");
			}				
			
			out.println("</select>"
					  + "</label>"
					  + "</div>");
		
			// submit button
			out.println("<div class=\"row\">"
					  + "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Hours  <i class=\"fa fa-chevron-right\"></i></button>"
					  + "</div>"
					  + "</section>"
					  + "</div>"
					  + "<script src=\"../../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../../js/vendor/foundation.min.js\"></script>"
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
	 * Form to book hours, 
	 * list of all tasks where the user
	 * wants to book hours
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// get user and project ID
		int id = (int) request.getSession(false).getAttribute("ID");
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if user is the project leader
		if (project.getLeader() == id){
			
			// get parameters and create needed lists
			int employeeID = Integer.parseInt(request.getParameter("employeeID"));
			String[] task = request.getParameterValues("tasks");
			ArrayList<Integer> taskIDs = new ArrayList<Integer>();
			ArrayList<String> taskNames = new ArrayList<String>();
			ArrayList<Task> tasks = new ArrayList<Task>();
			ArrayList<Task> usedTasks = new ArrayList<Task>();
					
			String message = "";
			
			// split the task strings and add them to the arraylists
			for(String s : task){
				String[] helper = s.split(";");
				taskIDs.add(Integer.parseInt(helper[0]));
				taskNames.add(helper[1]);
			}		
			
			
			// get the tasks from the project
			tasks = project.getTasks();
			
			// search tasks where the user wants to book hours
			// and add them to usedTasks list
			for (Task t : tasks){
				for (int i : taskIDs){
					if(i == t.getID()){
						usedTasks.add(t);
					}
				}
			}
			
			// Print HTML
			out.println(HTMLHeader.getInstance().printHeader("Book Hours", "../../../", "Book Hours", "", "<a href=\"../Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  + "<section>"
					  + message
					  + "<div class=\"row\">"
					  + "<h3>Book Hours</h3>"
					  + "<form method=\"post\" action=\"../bookHours\" data-abide novalidate>"
	
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in the form</p></div>"
					
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + projectID + "\">"
					  + "<input type=\"hidden\" name=\"employeeID\" value=\"" + employeeID + "\">");
					
			// list with every task that was selected in the last form 
			for (int i = 0; i < usedTasks.size(); i++){
	
				// get possible months where the hours can be booked
				ArrayList<String> months = new ArrayList<String>();
				ArrayList<String> dates = new ArrayList<String>();
				String start = usedTasks.get(i).getStart();
				int nbrOfMonths = usedTasks.get(i).getNumberOfMonths();
				
				// get dates in format "01.08.2016" and "August 2016"
				// add them to the ArrayList
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
				
				// get assignment with the given employee and task ID
				int assignmentID = 0;
				try {
					assignmentID = con.getAssignment(employeeID, taskIDs.get(i)).getID();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				out.println("<input type=\"hidden\" name=\"assignmentID\" value=\"" + assignmentID + "\">");
				
				// task name, field for the hours and dropdownlist to select the month
				out.println("<p class=\"small-2 columns\">" + taskNames.get(i) + "</p>"
							+ "<p class=\"small-1 columns\">Hours</p>"
							+ "<div class=\"small-4 columns\">"
							+ "<input type=\"number\" name=\"hours\" required>"
							+ "</div>"
							+ "<p class=\"small-1 columns\">Month</p>"
							+ "<select name=\"months\" class=\"small-4 columns\" required>"
							+ "<option></option>");
				int z = 0;
				for (String s : months){
					// option for every month 
					// value is date with format "01.08.2016"
					// user sees date in format "August 2016"
					out.println("<option value=\"" + dates.get(z) + "\">"
								+ s
								+ "</option>");
					z++;
				}
				out.println("</select>");
			}				
					
			// return and submit button
			out.println("</div>"
					  + "<div class=\"row\">"
					  + "<a href=\"chooseTask?projectID=" + projectID +"&employee=" + employeeID + "\" class=\"small-3 columns large button back-button\"><i class=\"fa fa-chevron-left\"></i>  Choose Task</a>"
					  + "<button type=\"submit\" class=\"small-3 columns large button float-right create\">Book Hours</button>"
					  + "</div>");
			
			// required JavaScript
			out.println("</section>"
					  + "</div>"
					  + "<script src=\"../../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}
	}
}
