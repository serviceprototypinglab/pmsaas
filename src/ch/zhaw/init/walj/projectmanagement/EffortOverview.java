package ch.zhaw.init.walj.projectmanagement;

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

import ch.zhaw.init.walj.projectmanagement.util.Booking;
import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.NumberFormatter;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/Effort")
public class EffortOverview extends HttpServlet {

	// variable declaration
	private Project project;
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private ArrayList<Booking> bookings = new ArrayList<Booking>();
	private Effort effort;
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		int employeeID;
		try {
			employeeID = Integer.parseInt(request.getParameter("employeeID"));
		} catch (NumberFormatException ex) {
			employeeID = 0;
		}
			
		DBConnection con = new DBConnection();
		
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		if (project.getLeader() == id){
			employees = project.getEmployees();
			Employee selectedEmployee = project.getSpecificEmployee(employeeID);
			effort = new Effort(project.getTasks());
			PrintWriter out = response.getWriter();
			
			String link = "<a href=\"Project?id=" + projectID + "\" class=\"back\">"
						+ "<i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i>"
						+ " back to Project</a>";
			
			out.println(HTMLHeader.getInstance().getHeader(project.getShortname(), "../../", "Effort", link));
			// HTML section
			if (employeeID == 0){
				try {
					bookings = effort.getBookings();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				out.println("<section>"
						  + "<div class=\"row\">"
						  + "<div class=\"small-12 columns\">"
						  + "<h2>Effort " + project.getShortname() + "  <small>" + project.getStart() + "-" + project.getEnd() + "</small></h2>"
						  + "</div>"
						  // line chart
						  + "<div class=\"small-12 no-padding columns\">"
						  + "<img src=\"../../Charts/EffortProject" + project.getID() + "_large.jpg\">"
						  + "</div>"
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
				for(Employee e: employees){
					if ((e.getID() == employeeID) || (employeeID == 0)){
						for (Booking b : bookings){
							if (b.getEmployeeID() == e.getID()){
								
								String month [][] = DateFormatter.getInstance().getMonths(
										DateFormatter.getInstance().stringToDate(project.getStart(), "dd.MM.yyyy"), 
										b.getMonth()
										);
								
								String task = "";
								for (ProjectTask t : project.getTasks()){
									if (t.getID() == b.getTaskID()){
										task = t.getName();
									}
								}
								
								double expense = b.getHours() * e.getWage();
								totalEffort += expense;
								totalHours += b.getHours();
								
								out.println("<tr>"
										  + "<td>" + e.getName() + "</td>"
										  + "<td>" + month[1][b.getMonth()-1] + "</td>"
										  + "<td>" + task + "</div>"
										  + "<td>" + NumberFormatter.getInstance().formatHours(b.getHours()) + "</td>"
										  + "<td>" + NumberFormatter.getInstance().formatDouble(expense) + "</td>"
										  + "<td>" + NumberFormatter.getInstance().formatDouble(e.getWage())+ "</td>"
										  + "</tr>");
							}
						}
					}
				}

				out.println("<tr class=\"bold\">"
						  + "<td>Total</td>"
						  + "<td></td>"
						  + "<td></div>"
						  + "<td>" + NumberFormatter.getInstance().formatHours(totalHours) + "</td>"
						  + "<td>" + NumberFormatter.getInstance().formatDouble(totalEffort) + "</td>"
						  + "<td></td>"
						  + "</tr>"
						  + "</tbody>"
						  + "</table>");
				
			} else {
				try {
					bookings = effort.getBookings(employeeID);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				out.println("<section>"
						  + "<div class=\"row\">"
						  + "<div class=\"small-12 columns\">"
						  + "<h2>Effort " + project.getShortname() + "  <small>" + project.getStart() + "-" + project.getEnd() + "</small></h2>"
						  + "</div>"
						  + "<div class=\"small-12 columns\">"
						  + "<h3>" + selectedEmployee.getName() + "</h3>"
						  + "</div>"
						  // line chart
						  + "<div class=\"small-12 no-padding columns\">"
						  + "<img src=\"../../Charts/EffortProject" + project.getID() + "_Employee" + employeeID + ".jpg\">"
						  + "</div>"
						  + "</div>"
						  + "<div class=\"row\" id=\"effortTable\">"
						  + "<table class=\"stack\">"
						  + "<thead>"
						  + "<tr>"
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
				for (Booking b : bookings){
						
						String month = "";
						SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
						SimpleDateFormat formatMonthYear = new SimpleDateFormat("MMMM yyyy");
						try {
							Calendar c = Calendar.getInstance();
							Date date = format.parse(project.getStart());
							c.setTime(date);
							for (int y = 1; y < b.getMonth(); y++){
								c.add(Calendar.MONTH, 1);
							}
							month = formatMonthYear.format(c.getTime());
						} catch (ParseException ex) {
							ex.printStackTrace();
						}
						
						String task = "";
						for (ProjectTask t : project.getTasks()){
							if (t.getID() == b.getTaskID()){
								task = t.getName();
							}
						}
						
						double expense = b.getHours() * selectedEmployee.getWage();

						totalEffort += expense;
						totalHours += b.getHours();
											
											
						out.println("<tr>"
								  + "<td>" + month + "</td>"
								  + "<td>" + task + "</div>"
								  + "<td>" + b.getHours() + "</td>"
								  + "<td>" + expense + "</td>"
								  + "<td>" + selectedEmployee.getWage()+ "</td>"
								  + "</tr>");
				}

				out.println("<tr class=\"bold\">"
						  + "<td>Total</td>"
						  + "<td></td>"
						  + "<td></div>"
						  + "<td>" + NumberFormatter.getInstance().formatHours(totalHours) + "</td>"
						  + "<td>" + NumberFormatter.getInstance().formatDouble(totalEffort) + "</td>"
						  + "<td></td>"
						  + "</tr>"
						  + "</tbody>"
						  + "</table>");
				
			}
			out.println("</div>"
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
