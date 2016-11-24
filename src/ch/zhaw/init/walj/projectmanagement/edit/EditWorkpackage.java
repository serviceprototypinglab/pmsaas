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
@WebServlet("/Projects/EditWorkpackage")
public class EditWorkpackage extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con = new DBConnection();
		
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
		

		System.out.println(start);
		System.out.println(end);
		
		final PrintWriter out = response.getWriter();
				
		String message = "";
		
		try {
			con.updateWorkpackage(id, name, start, end);
			
			message = "<div class=\"callout success\">"
					+ "<h5>Workpackage successfully updated</h5>"
					+ "<p>The new workpackage has succsessfully been updated with the following data:</p>"
					+ "<p>Name: " + name + "</p>"
					+ "<p>Duration: " + start + " - " + end + "</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "#workpackages\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
					+ "</div>";
		} catch (SQLException e) {
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
				    + "<h5>Workpackage could not be updated</h5>"
				    + "<p>An error occured and the workpackage could not be updated.</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + projectID + "\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + projectID + "\">Click here to go to the project overview</a>"
					+ "</div>";
		}
						
		out.println(HTMLHeader.getInstance().getHeader("Edit Workpackage", "../", name, "") 
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
