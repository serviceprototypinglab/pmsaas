package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;

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
		
		out.println(HTMLHeader.getInstance().getHeader("Help", "../", "Help", "")
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
