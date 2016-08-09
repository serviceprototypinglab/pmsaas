package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Overview/Project")
public class ProjectOverview extends HttpServlet {
	
	private String		url			= "jdbc:mysql://localhost:3306/";
	private String		dbName		= "projectmanagement";
	private String		userName	= "Janine";
	private String		password	= "test123";

	private Project project;
	
	private int	i = 0;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");

		int projectID = Integer.parseInt(request.getParameter("id"));
		
		DBConnection con = new DBConnection(url, dbName, userName, password);
		
		try {
			project = con.getProject(projectID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		
		// write HTML (head, header, nav)
		out.println("<!DOCTYPE html>\n" 
						+ "\t<html>\n" 
							+ "\t\t<head>\n" 
								+ "\t\t\t<meta charset=\"UTF-8\">\n"
								+ "\t\t\t<title>Projects</title>\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />\n"
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />\n" 
								+ "\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />\n" 
							+ "\t\t</head>\n" 
							+ "\t\t<body>\n"
								+ "\t\t\t<div id=\"wrapper\">\n" 
									+ "\t\t\t\t<header>\n" 
										+ "\t\t\t\t\t<div class=\"row\">\n" 
											+ "\t\t\t\t\t\t<div class=\"small-8 medium-6 columns\"><h1>Project A</h1></div>\n"
											+ "\t\t\t\t\t\t<div class=\"small-12 medium-6 columns\">\n" 
												+ "\t\t\t\t\t\t\t<div class=\"float-right\">\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"/Projektverwaltung/Overview\" class=\"button\">All Projects</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"../newProject.shtml\" class=\"button\">New Project</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"../newEmployee.shtml\" class=\"button\">New Employee</a>\n"
													+ "\t\t\t\t\t\t\t\t<a href=\"help.shtml\" class=\"button\">Help</a>\n" 
													+ "\t\t\t\t\t\t\t\t<a href=\"logout.shtml\" class=\"button\">Logout</a>\n" 
												+ "\t\t\t\t\t\t\t</div>\n" 
											+ "\t\t\t\t\t\t</div>\n"
										+ "\t\t\t\t\t</div>\n" 
									+ "\t\t\t\t</header>\n" 
								+ "\t\t\t<section>\n"
								+ "\t\t\t<div class=\"row\">\n"
								+ "\t\t\t<div class=\"panel\">\n");
		
		out.println("<div class=\"row round\"><div class=\"small-4 medium-2 columns\">Projectduration</div>" 
						+ "<div class=\"small-8 medium-4 columns\">" + project.getDuration() + "</div>"
						+ "<div class=\"small-4 medium-2 columns\">Total Budget</div>"
						+ "<div class=\"small-8 medium-4 columns\">" + project.getBudget() + "</div></div></div></div>");
		
		out.println(
				"</section></div><script src=\"../js/vendor/jquery.js\"></script><script src=\"../js/vendor/foundation.min.js\"></script><script>$(document).foundation();</script></body></html>");
	}
	
}
