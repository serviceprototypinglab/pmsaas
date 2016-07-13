package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Overview
 */
@WebServlet("/Overview")
public class Overview extends HttpServlet {
	
	private String		url			= "jdbc:mysql://localhost:3306/";
	private String		dbName		= "projectmanagement";
	private String		userName	= "Janine";
	private String		password	= "test123";
	
	private ResultSet	res;
	
	private int			i			= 0;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		DBConnection con = new DBConnection(url, dbName, userName, password);
		
		res = con.getProjects(1);
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\n" 
						+ "\t<html>\n" 
							+ "\t\t<head>\n" 
								+ "\t\t\t<meta charset=\"UTF-8\">\n"
								+ "\t\t\t<title>Projects</title>\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/foundation.css\" />\n"
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome/css/font-awesome.min.css\" />\n" 
							+ "\t\t</head>\n" 
							+ "\t\t<body>\n"
								+ "\t\t\t<div id=\"wrapper\">\n" 
									+ "\t\t\t\t<header>\n" 
										+ "\t\t\t\t\t<div class=\"row\">\n" 
											+ "\t\t\t\t\t\t<div class=\"small-8 medium-6 columns\"><h1>Projects</h1></div>\n"
											+ "\t\t\t\t\t\t<div class=\"small-12 medium-6 columns\">\n" 
												+ "\t\t\t\t\t\t\t<div class=\"float-right\">\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"newProject.shtml\" class=\"button\">New Project</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"newEmployee.shtml\" class=\"button\">New Employee</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"help.shtml\" class=\"button\">Help</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"logout.shtml\" class=\"button\">Logout</a>\n" 
												+ "\t\t\t\t\t\t\t</div>\n" 
											+ "\t\t\t\t\t\t</div>\n"
										+ "\t\t\t\t\t</div>\n" 
									+ "\t\t\t\t</header>\n" 
								+ "\t\t\t<section>\n" 
								+ "\t\t\t<div class=\"row\">\n" 
									+ "\t\t\t\t<ul class=\"accordion\" data-accordion>");
		
		try {
			while (res.next()) {
				out.println("<li class=\"accordion-item\" data-accordion-item><a href=\"#\" class=\"accordion-title\">");
				out.println("<span class=\"bigtext small-3 columns down\">" + res.getString("ProjectShortname") + "</span>");
				out.println("<span class=\"middletext small-6 columns down\">" + res.getString("ProjectName") + "</span>");
				out.println("<button class=\"button small-2 columns down smalltext\">Select</button>");
				out.println("<span class=\"success badge\">0</span>");
				out.println("</a>");
				out.println("<div class=\"accordion-content\" data-tab-content>");
				out.println(
						"<p><span class=\"small-2 columns\">Projectduration</span><span class=\"small-6 columns\">" + res.getString("ProjectStart")
								+ " - " + res.getString("ProjectEnd") + "</span><span class=\"small-4 columns\">Project ends in X Days</span></p>");
				out.println("<p><span class=\"small-2 columns\">Budget</span><span class=\"small-6 columns\">" + res.getString("Currency") + " "
						+ res.getString("TotalBudget") + "</span><span class=\"small-4 columns\">" + res.getString("Currency")
						+ " XXXXX left</span></p>");
				out.println("</div>");
				out.println("</li>");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		out.println(
				"</ul></div></section></div><script src=\"js/vendor/jquery.js\"></script><script src=\"js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
	}
	
}
