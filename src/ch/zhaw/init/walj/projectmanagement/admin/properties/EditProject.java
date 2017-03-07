package ch.zhaw.init.walj.projectmanagement.admin.properties;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, Page edit project
 * 
 * @author Janine Walther, ZHAW
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/admin/editProject")
public class EditProject extends HttpServlet {
	
	/*
	 * 	method to handle get requests
	 * 	shows form to edit project
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get project ID
		int projectID = Integer.parseInt(request.getParameter("id"));

		// Database connection
		DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}
					
		// print html
		out.println(HTMLHeader.getInstance().printHeader("Edit " + project.getShortname(), "../", "Edit " + project.getShortname(), "", "<a href=\"properties\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to properties</a>", true)
				  + "<section>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-12 columns\">"
				  + "<h2 class=\"no-margin\">Edit Project</h2>"
				  + "</div>"
				  + "</div>" 
				  + "<div class=\"row\">"); 
		
		// set currency field (CHF or EUR selected)
		String currency = "";
		if (project.getCurrency().equals("CHF")){
			currency = "<select name=\"currency\" required>"
					 + "<option value=\"CHF\" selected>CHF</option>"
					 + "<option value=\"EUR\">EUR</option>"
					 + "</select>";
		} else {
			currency = "<select name=\"currency\" required>"
					 + "<option value=\"CHF\">CHF</option>"
					 + "<option value=\"EUR\" selected>EUR</option>"
					 + "</select>";
		}
			
		// form to edit project
		out.println("<form method=\"post\" action=\"editProject\" data-abide novalidate>"
				  + "<input type=\"hidden\" name=\"id\" value=\"" + project.getID() + "\">"
				  + "<p class=\"small-4 columns\">Project Name</p>"
				  + "<div class=\"small-8 end columns\">" 
				  + "<input type=\"text\" name=\"name\" value=\"" + project.getName() + "\">"
				  + "</div>"
				  + "<p class=\"small-4 columns\">Project Shortname</p>"
				  + "<div class=\"small-8 end columns\">" 
				  + "<input type=\"text\" name=\"shortname\" value=\"" + project.getShortname() + "\">"
				  + "</div>"
				  + "<p class=\"small-4 columns\">Project Start</p>"
				  + "<div class=\"small-8 end columns\">" 
				  + "<input type=\"text\" name=\"start\" value=\"" + project.getStart() + "\">"
				  + "</div>"
				  + "<p class=\"small-4 columns\">Project End</p>"
				  + "<div class=\"small-8 end columns\">" 
				  + "<input type=\"text\" name=\"end\" value=\"" + project.getEnd() + "\">"
				  + "</div>"
				  + "<p class=\"small-4 columns\">Currency</p>"
				  + "<div class=\"small-8 columns end\">" 
				  + currency
				  + "</div>"
				  + "<p class=\"small-4 columns\">Budget</p>"
				  + "<div class=\"small-8 end columns\">" 
				  + "<input type=\"number\" name=\"budget\" value=\"" + project.getBudget() + "\">"
				  + "</div>"
				  + "<p class=\"small-4 columns\">Partners</p>"
				  + "<div class=\"small-8 columns end\">" 
				  + "<input type=\"text\" name=\"partner\" value=\"" + project.getPartners() + "\">"
				  + "</div>"	
				  + "<div class=\"small-2 small-offset-10 columns end\">" 			  
				  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
				  + "</div>"
				  + "</form>"
				  + "</div>");
		
		out.println("</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
		
	}
	
	/*
	 * 	method to handle post requests
	 * 	makes changes in database
	 * 	returns error/success message
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{

		// Database connection
		DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		// get the parameters and user ID
		int projectID = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String shortname = request.getParameter("shortname");
		double budget = Double.parseDouble(request.getParameter("budget"));
		String currency = request.getParameter("currency");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String partners = request.getParameter("partner");
				
		// get project
		Project project = null;
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			String url = request.getContextPath() + "/ProjectNotFound";
            response.sendRedirect(url);
            return;
		}

		String message = "";
		// update project
		out.println(HTMLHeader.getInstance().printHeader("Edit " + project.getShortname(), "../", "Edit " + project.getShortname(), "", "<a href=\"properties\" class=\"back\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i> back to properties</a>", true));
		
		// success message
		message = "<div class=\"callout success\">"
				+ "<h5>Project successfully updated</h5>"
				+ "<p>The new project has succsessfully been updated with the following data:</p>"
				+ "<p>Name: " + name + "</p>"
				+ "<p>Shortname: " + shortname + "</p>"
				+ "<p>Budget: " + budget + "</p>"
				+ "<p>Currency: " + currency + ""
				+ "<p>Duration: " + start + " - " + end + "</p>"
				+ "<p>Partners: " + partners + "</p>"
				+ "<a href=\"/Projektverwaltung/admin/properties\">Click here to go back to the properties page</a>"
				+ "</div>";
					
		// print HTML
		out.println(HTMLHeader.getInstance().printHeader("Edit " + shortname, "../", "Edit " + shortname, "")
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
