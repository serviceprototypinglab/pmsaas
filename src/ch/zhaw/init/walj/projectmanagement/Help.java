package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Overview
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/help")
public class Help extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>" 
				  + "<html>"
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">"
				  + "<title>Projects</title>" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />" 
				  // function to redirect after click on select button
				  + "<script>function Redirect(ID) {var url = \"Overview/Project?id=\" + ID; window.location=url;}</script>"
				  + "</head>" 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<header>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-8 columns\">"
				  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
				  // title
				  + "<h1>Help</h1>"
				  + "</div>"
				  // menu
				  + "<div class=\"small-12 medium-4 columns\">" 
				  + "<div class=\"float-right menu\">"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\" title=\"All Projects\"><i class=\"fa fa-list fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\" title=\"New Project\"><i class=\"fa fa-file fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/employee\" class=\"button\" title=\"My Profile\"><i class=\"fa fa-user fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\" title=\"Help\"><i class=\"fa fa-book fa-fw\"></i></a> "
				  + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw\"></i></a> "
				  + "</div>" 
				  + "</div>"
				  + "</div>" 
				  + "</header>" 
				  // HTML section with list of all projects
				  + "<section>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns text-center\">"
				  + "<h2>This page is not avaible yet.</h2>"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\">Click here to go back to the overview</a>"
				  + "</div>"
				  + "</div>");
				  
		out.println("</section>"
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
