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
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;


// TODO /** kommentare
@SuppressWarnings("serial")
@WebServlet("/Projects/EditTask")
public class EditTask extends HttpServlet {
	
	// Variables for the database connection
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "projectmanagement";
	String userName	= "Janine";
	String password	= "test123";
	
	// create a new DB connection
	private DBConnection con = new DBConnection(url, dbName, userName, password);
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		// get the parameters
		int id = Integer.parseInt(request.getParameter("id"));
		int projectID = Integer.parseInt(request.getParameter("projectID"));
		String name = request.getParameter("name");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		int pm = Integer.parseInt(request.getParameter("pm"));
		double budget = Double.parseDouble(request.getParameter("budget"));
		int wp = Integer.parseInt(request.getParameter("workpackage"));
		

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(start);
			start = format2.format(date);
			date = format.parse(end);
			end = format2.format(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
				
		String message = "";
		
		try {
			con.updateTask(id, name, start, end, pm, budget, wp);
			
			message = "<div class=\"callout success\">"
					+ "<h5>Task successfully updated</h5>"
					+ "<p>The task has succsessfully been updated with the following data:</p>"
					+ "<p>Name: " + name + "</p>"
					+ "<p>Duration: " + start + " - " + end + "</p>"
					+ "<p>PMs: " + pm + ""
					+ "<p>Budget: " + budget + "</p>"
					+ "<p>Workpackage: " + wp + "</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#tasks\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
					+ "</div>";
		} catch (SQLException e) {
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
				    + "<h5>Task could not be updated</h5>"
				    + "<p>An error occured and the task could not be updated.</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + id + "#tasks\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + id + "\">Click here to go to the project overview</a>"
					+ "</div>";
		}
						
		out.println(HTMLHeader.getInstance().getHeader("Edit " + name, "../", "Edit " + name)
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
