package ch.zhaw.init.walj.projectmanagement.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.format.DateFormatter;

/**
 * Projectmanagement tool, Page to edit expenses
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/EditExpense")
public class EditExpense extends HttpServlet {
		
	// create a new DB connection
	private DBConnection con;
		
	/*
	 * 	method to handle post requests
	 * 	makes changes in database
	 * 	returns error/success message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get parameters and user ID
		int expenseID = Integer.parseInt(request.getParameter("id"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employee = Integer.parseInt(request.getParameter("employee"));
		String type = request.getParameter("type");
		double costs = Double.parseDouble(request.getParameter("costs"));
		String description = request.getParameter("description");
		String date = request.getParameter("date");
		int userID = (int) request.getSession(false).getAttribute("ID");
		
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
			String message = "";
			
			try {
				// update expense
				con.updateExpense(expenseID, employee, costs, type, description, DateFormatter.getInstance().formatDateForDB(date));
				
				// get employee
				Employee e = project.getSpecificEmployee(employee);
				
				// success message
				message = "<div class=\"callout success\">"
						+ "<h5>Expense successfully updated</h5>"
						+ "<p>The expense has succsessfully been updated with the following data:</p>"
						+ "<p>Employee: " + e.getName() + "</p>"
						+ "<p>Costs: " + project.getCurrency() + " " + costs + "</p>"
						+ "<p>Type: " + type + "</p>"
						+ "<p>Description: " + description + "</p>"
						+ "<p>Date: " + date + "</p>"
						+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#expenses\">Click here to go back to the edit page</a>"
						+ "<br>"
						+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
						+ "</div>";
			} catch (SQLException e) {
				// error message
				message = "<div class=\"callout alert\">"
					    + "<h5>Expense could not be updated</h5>"
					    + "<p>An error occured and the expense could not be updated.</p>"
						+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#expenses\">Click here to go back to the edit page</a>"
						+ "<br>"
						+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
						+ "</div>";
			}
			
			// print HTML
			out.println(HTMLHeader.getInstance().printHeader("Edit Expense", "../", project.getName(), "")
					  + "<section>"
					  + "<div class=\"row\">"
					  + message
					  + "</div>"
					  + "</section>"
					  + HTMLFooter.getInstance().printFooter(false)
					  // required JavaScript
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
