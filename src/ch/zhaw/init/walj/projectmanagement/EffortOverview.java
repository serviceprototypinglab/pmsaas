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
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.NumberFormatter;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Booking;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Effort;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task;

/**
 * project management tool, effort overview page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/Effort")
public class EffortOverview extends HttpServlet {
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// set content type for response
		response.setContentType("text/html;charset=UTF8");

		// variable declaration
		int id;
		int projectID;
		int employeeID;
		Project project;
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		ArrayList<Employee> employees = new ArrayList<Employee>();
		
		
		// create a new connection to the database
		DBConnection con = new DBConnection();
		
		// get user ID from session
		id = (int) request.getSession(false).getAttribute("ID");
		
		// get project ID from parameter
		projectID = Integer.parseInt(request.getParameter("projectID"));
		
		// get the project from the database, 
		// if project can not be fount send redirect to project not found page
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
		
		// check if the project leader id and the id from the session is the same
		// if not, send redirect to access denied page
		if (project.getLeader() == id){
			
			// check if there is an employee id, if not set employeeID to 0 
			try {
				employeeID = Integer.parseInt(request.getParameter("employeeID"));
			} catch (NumberFormatException ex) {
				employeeID = 0;
			}
			
			// create a new effort object with the tasks of the project
			Effort effort = new Effort(project.getTasks());
			
			// prepare link for HTML header
			String link = "<a href=\"Project?id=" + projectID + "\" class=\"back\">"
						+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i>"
						+ " back to Project</a>";
			
			// get print writer
			PrintWriter out = response.getWriter();
			
			// print header
			out.println(HTMLHeader.getInstance().printHeader(project.getShortname(), "../../", "Effort", "", link)
					  + "<section>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-12 columns\">"
					  + "<h2>Effort " + project.getShortname() + "  <small>" + project.getStart() + "-" + project.getEnd() + "</small></h2>"
					  + "</div>");
			
			// check whether the effort of one employee or all employees should be shown
			if (employeeID == 0){
				// get all employees of the project
				employees = project.getEmployees();
				// get all bookings
				try {
					bookings = effort.getBookings();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				// get the selected employee
				employees.add(project.getSpecificEmployee(employeeID));
				// get all bookings that belong to the employee
				try {
					bookings = effort.getBookings(employeeID);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			// print line chart and effort table
			out.println("<div class=\"small-12 no-padding columns\">");
			if (employeeID == 0){
				out.println("<img src=\"../../Charts/EffortProject" + project.getID() + "_large.jpg\">");
			} else {
				out.println("<img src=\"../../Charts/EffortProject" + project.getID() + "_Employee" + employeeID + ".jpg\">");
			}
			out.println("</div>"
					  + "</div>"
					  + "<div class=\"row\" id=\"effortTable\">"
					  + "<table class=\"stack\">"
					  + "<thead>"
					  + "<tr>"
					  + "<th>Employee</th>"
					  + "<th>Month</th>"
					  + "<th>Task</th>"
					  + "<th>Hours</th>"
					  + "<th>~" + project.getCurrency() + "</th>"
					  + "<th>" + project.getCurrency() + "/h</th>"
					  + "</tr>"
					  + "</thead>"
					  + "<tbody>");
	
			// effort for every employee and total effort
			double totalEffort = 0;	
			double totalHours = 0;	
			// get bookings for every employee
			for(Employee employee: employees){
				for (Booking booking : bookings){
					if (booking.getEmployeeID() == employee.getID()){
						
						// get months
						String month [][] = DateFormatter.getInstance().getMonths(
								DateFormatter.getInstance().stringToDate(project.getStart(), "dd.MM.yyyy"), 
								booking.getMonth()
								);
						
						// get Task name
						String taskName = "";
						for (Task task : project.getTasks()){
							if (task.getID() == booking.getTaskID()){
								taskName = task.getName();
							}
						}
						
						// get the expense
						double expense = booking.getHours() * employee.getWage();
						totalEffort += expense;
						totalHours += booking.getHours();
						
						// print table row
						out.println("<tr>"
								  + "<td>" + employee.getName() + "</td>"
								  + "<td>" + month[1][booking.getMonth()-1] + "</td>"
								  + "<td>" + taskName + "</div>"
								  + "<td>" + NumberFormatter.getInstance().formatHours(booking.getHours()) + "</td>"
								  + "<td>" + NumberFormatter.getInstance().formatDouble(expense) + "</td>"
								  + "<td>" + NumberFormatter.getInstance().formatDouble(employee.getWage())+ "</td>"
								  + "</tr>");
					}
				}
			}
			
			// print hour total and effort total
			out.println("<tr class=\"bold\">"
					  + "<td>Total</td>"
					  + "<td></td>"
					  + "<td></div>"
					  + "<td>" + NumberFormatter.getInstance().formatHours(totalHours) + "</td>"
					  + "<td>" + NumberFormatter.getInstance().formatDouble(totalEffort) + "</td>"
					  + "<td></td>"
					  + "</tr>"
					  + "</tbody>"
					  + "</table>"
					  + "</div>"
					  + "<div class=\"row\">"
					  + "<div class=\"small-4 columns right\">"
					  + "<a class=\"button expanded\" href=\"/Projektverwaltung/Projects/Overview/bookHours?projectID=" + project.getID() + "\">"
					  + "<i class=\"fa fa-clock-o\"></i> Book Hours</a>"
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