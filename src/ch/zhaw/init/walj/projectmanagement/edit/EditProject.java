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
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;


// TODO /** kommentare
@SuppressWarnings("serial")
@WebServlet("/Projects/EditProject")
public class EditProject extends HttpServlet {
	
	// create a new DB connection
	private DBConnection con = new DBConnection();
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		// get the parameters
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String shortname = request.getParameter("shortname");
		double budget = Double.parseDouble(request.getParameter("budget"));
		String currency = request.getParameter("currency");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String partners = request.getParameter("partner");
		

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
			con.updateProject(id, name, shortname, budget, currency, start, end, partners);
			
			message = "<div class=\"callout success\">"
					+ "<h5>Project successfully updated</h5>"
					+ "<p>The new project has succsessfully been updated with the following data:</p>"
					+ "<p>Name: " + name + "</p>"
					+ "<p>Shortname: " + shortname + "</p>"
					+ "<p>Budget: " + budget + "</p>"
					+ "<p>Currency: " + currency + ""
					+ "<p>Duration: " + start + " - " + end + "</p>"
					+ "<p>Partners: " + partners + "</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + id + "\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + id + "\">Click here to go to the project overview</a>"
					+ "</div>";
		} catch (SQLException e) {
			e.printStackTrace();
			message = "<div class=\"callout alert\">"
				    + "<h5>Project could not be updated</h5>"
				    + "<p>An error occured and the project could not be updated.</p>"
					+ "<a href=\"/Projektverwaltung/Projects/Edit?projectID=" + id + "\">Click here to go back to the edit page</a>"
					+ "<br>"
					+ "<a href=\"/Projektverwaltung/Projects/Overview/Project?id=" + id + "\">Click here to go to the project overview</a>"
					+ "</div>";
		}
						
		out.println(HTMLHeader.getInstance().printHeader("Edit " + shortname, "../", "Edit " + shortname, "")
				  // HTML section with form
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
	}
}
