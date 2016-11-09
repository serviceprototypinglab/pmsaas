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
import ch.zhaw.init.walj.projectmanagement.util.DateFormatter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.ProjectTask;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Weight;

// TODO /** kommentare
@SuppressWarnings("serial")
@WebServlet("/Projects/Overview/editWeight")
public class EditWeight extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con = new DBConnection();
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		int id = (int) request.getSession(false).getAttribute("ID");
		
		int pID = Integer.parseInt(request.getParameter("projectID"));
			
		Project project = null;
		
		try {
			project = con.getProject(pID);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (project.getLeader() == id){
			ArrayList<ProjectTask> tasks = project.getTasks();
			
			PrintWriter out = response.getWriter();
					
			out.println(HTMLHeader.getInstance().getHeader("Edit Weight", "../../", "Edit Weight", "", "<a href=\"Project?id=" + pID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  // HTML section with form
					  + "<section>"
					  + "<form method=\"post\" action=\"editWeight\" data-abide novalidate>"
					  + "<div class=\"row\">"
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + project.getID() + "\">");
					  
			for (ProjectTask task : tasks){
				
				boolean firstInitialisation = false;
				
				int nbrOfMonths = task.getNumberOfMonths();
				int startMonth = task.getStartMonth();
				String months[] = DateFormatter.getInstance().getMonthStrings(task.getStartAsDate(), nbrOfMonths);
				
				ArrayList<Weight> weight = task.getWeight();
				if (weight.size() != nbrOfMonths){
					firstInitialisation = true;
				}
				
				if (firstInitialisation){
					out.println("<input type=\"hidden\" name=\"firstInitialisation\" value=\"true\">");
				} else {
					out.println("<input type=\"hidden\" name=\"firstInitialisation\" value=\"false\">");
				}
				
				out.println("<h2 class=\"blue\">" + task.getName() + "</h2>"
						  // task ID
						  + "<input type=\"hidden\" name=\"taskID\" value=\"" + task.getID() + "\">"
						  // error message (if something's wrong with the form)
						  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
						  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
						  + "</div>");
				out.println("<table>"
						  + "<tr>"
						  + "<th class=\"th-200\">Month</th>"
						  + "<th class=\"th-300\">Weight</th>");
				if (months.length > 1) {
					out.println("<th class=\"space\"></th>"	
							  + "<th class=\"th-200\">Month</th>"
							  + "<th class=\"th-300\">Weight</th>");	
				}
				out.println("</tr>");
				for (int i = 0; i < months.length; i++){
					if (i % 2 != 1) {
						out.println("<tr>");
					} else {
						out.println("<td class=\"space\"></td>");
					}
					out.println("<input type=\"hidden\" name=\"month" + task.getID() + "\" value=\"" + startMonth + "\">"
							  + "<td>" + months[i] + "</td>"
							  + "<td>"
							  + "<input type=\"text\" name=\"weight" + task.getID() + "\" value=\"");
					if (firstInitialisation){
						out.println("1");
					} else {
						for (Weight w : weight){
							if (w.getMonth() == startMonth){
								out.println(w.getWeight());
							}
						}
					}
					out.println("\" required>"
							  + "</td>");
					if ((i % 2 == 1) || (i + 1 == months.length)) {
						out.println("</tr>");						
					}
					startMonth++;
				}
				out.println("</table>");	
			}
			out.println("<input type=\"submit\" class=\"small-3 columns large button float-right create\"value=\"Edit Weight\">"
					  + "</div>"
					  + "</form>"
					  + "</section>"
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
		
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");

		int id = (int) request.getSession(false).getAttribute("ID");
		
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		
		Project project = null;
		
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		if (project.getLeader() == id){
		
			// get the parameters
			String taskIDs[] = request.getParameterValues("taskID");
			String firstInitialisation[] = request.getParameterValues("firstInitialisation");
	
			String successMessage = "";
			String errorMessage = "";
			
			for (String tID : taskIDs) {
				String month[] = request.getParameterValues("month" + tID);
				String weight[] = request.getParameterValues("weight" + tID);
				
				ProjectTask task = project.getTask(Integer.parseInt(tID));
				
					try {
					for (int i = 0; i < month.length; i++){
						if (firstInitialisation[0].equals("true")){
							con.newWeight(Integer.parseInt(tID), Integer.parseInt(month[i]), Double.parseDouble(weight[i]));
						} else {
							con.updateWeight(Integer.parseInt(tID), Integer.parseInt(month[i]), Double.parseDouble(weight[i]));
						}
					}
					successMessage += "<p>" + task.getName() + "</p>";
				} catch (SQLException e){
					errorMessage += "<p>" + task.getName() + "</p>";					
				}
			}
		
			if (!successMessage.equals("")){
				successMessage = "<div class=\"callout success small-12 columns\">"
						       + "<h5>Weight of the following tasks changed:</h5>"
						       + successMessage
							   + "<a href=\"Project?id=" + projectID + "\">go back to project overview</a>"
							   + "</div>";
			}
			
			if (!errorMessage.equals("")){
				errorMessage = "<div class=\"callout alert small-12 columns\">"
						       + "<h5>Weight of the following tasks could not be changed:</h5>"
						       + errorMessage
							   + "<a href=\"editWeight?projectID=" + projectID + "\">try again</a>"
							   + "</div>";
			}
			
			final PrintWriter out = response.getWriter();
			
			out.println(HTMLHeader.getInstance().getHeader("Edit Weight", "../../", "Edit Weight", "", "<a href=\"Project?id=" + projectID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  // HTML section with form
					  + "<section>"
					  + "<div class=\"row\">"
					  + successMessage
					  + errorMessage
					  + "</div>"
					  + "</section>"
					  // required JavaScript
					  + "<script src=\"../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
			
			
			
			
		}
		
	/*	
		if (project.getLeader() == id){
			ArrayList<Workpackage> workpackages = project.getWorkpackages();		
			
			
			final PrintWriter out = response.getWriter();
					
			String message = "";
			
			try {	
				// create the new workpackages in the DB
				con.newTask(taskWP, taskName, taskStart, taskEnd, taskPM, taskBudget);
										
				message = "<div class=\"callout success small-12 columns\">"
						  + "<h5>Task successfully created</h5>"
						  + "<p>The new task has succsessfully been created.</p>"
						  + "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + pID + "\">Click here to go back to the project overview</a>"
						  + "</div>";
				
			} catch (SQLException e) {
				e.printStackTrace();
				message = "<div class=\"callout alert small-12 columns\">"
						  + "<h5>Task could not be created</h5>"
						  + "<p>An error occured and the task could not be created.</p>"
						  + "</div>";
			}
					
			out.println(HTMLHeader.getInstance().getHeader("Add Tasks", "../../", "Add Tasks", "", "<a href=\"Project?id=" + pID + "\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to Project</a>")
					  // HTML section with form
					  + "<section>"
					  + "<div class=\"row\">"
					  + "<h2 class=\"small-12\">Add Task</h2>"
					  + message
					  + "</div>"
					  + "<form method=\"post\" action=\"addTask\" data-abide novalidate>"
					  // project ID
					  + "<input type=\"hidden\" name=\"projectID\" value=\"" + pID + "\">"
					  + "<div class=\"row\">"
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
					  + "</div>"
					  + "</div>"
					  // fields for the tasks
					  + "<div class=\"row\">"
					  + "<h2 class=\"small-12\">Tasks</h2>"
					  + "</div>"
					  + "<div class=\"row\">"
					  // labels
					  + "<p class=\"small-2 columns\">Name</p>"
					  + "<p class=\"small-2 columns\">Start<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
					  + "<p class=\"small-2 columns\">End<span class =\"grey\"> (dd.mm.yyyy)</span></p>"
					  + "<p class=\"small-2 columns\">PMs</p>"
					  + "<p class=\"small-2 columns\">Budget</p>"
					  + "<p class=\"small-2 columns\">Workpackage</p>"
					  + "<div id=\"task\">"
					  + "<div class=\"small-2 columns\">"
					  // field for name
					  + "<input type=\"text\" name=\"taskName\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for start date
					  + "<input type=\"text\" name=\"taskStart\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for end date
					  + "<input type=\"text\" name=\"taskEnd\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for amount of PMs
					  + "<input type=\"number\" name=\"taskPM\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field for the budget per task
					  + "<input type=\"number\" name=\"taskBudget\" required>"
					  + "</div>"
					  + "<div class=\"small-2 columns\">"
					  // field to assign the task to a workpackage
					  + "<select name=\"wpID\" required>"
					  + "<option></option>");
			
			// option for every task
			for (Workpackage w : workpackages){
				out.println("<option value =\"" + w.getID() + "\">" + w.getName() + "</option>");
			}				
			
			out.println("</select></div>"
					  // submit button
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"Add Task\">"
					  + "</form>"
					  +	"</div>"
					  + "</section>"
					  // required JavaScript
					  + "<script src=\"../../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body></html>");
		} else {
			String url = request.getContextPath() + "/AccessDenied";
            response.sendRedirect(url);
		}*/
	}
}
