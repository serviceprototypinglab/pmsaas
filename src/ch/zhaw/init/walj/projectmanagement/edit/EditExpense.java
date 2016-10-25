package ch.zhaw.init.walj.projectmanagement.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Project;

@SuppressWarnings("serial")
@WebServlet("/Projects/EditExpense")
public class EditExpense extends HttpServlet {
		
	// create a new DB connection
	private DBConnection con = new DBConnection();
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		// get the parameters
		int id = Integer.parseInt(request.getParameter("id"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		int employee = Integer.parseInt(request.getParameter("employee"));
		String type = request.getParameter("type");
		double costs = Double.parseDouble(request.getParameter("costs"));
		String description = request.getParameter("description");
		String date = request.getParameter("date");
		
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = format.parse(date);
			date = format2.format(d);
		} catch (ParseException e1) {
			
		}
		
		final PrintWriter out = response.getWriter();
				
		String message = "";
		
		try {
			con.updateExpense(id, employee, costs, type, description, date);
			
			Employee e = project.getSpecificEmployee(employee);
			
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
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
				    + "<h5>Expense could not be updated</h5>"
				    + "<p>An error occured and the expense could not be updated.</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#expenses\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
					+ "</div>";
		}
						
		out.println(HTMLHeader.getInstance().getHeader("Edit Expense", "../", project.getName(), "")
				  // HTML section with form
				  + "<section>"
				  + "<div class=\"row\">"
				  + message
				  + "</div>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
}
