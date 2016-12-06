package ch.zhaw.init.walj.projectmanagement.errorpages;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;

@SuppressWarnings("serial")
@WebServlet("/ProjectNotFound")
public class ProjectNotFound extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");
    	   	
		PrintWriter out = response.getWriter();
    	
    	out.println(HTMLHeader.getInstance().printHeader("Project not found - Project Management SaaS", "", "Project not found", "")  
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<section>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<h1 class=\"align-center\"><i class=\"fa fa-exclamation-triangle fa-3x\"></i></h1>"
				  + "<h1 class=\"align-center\">Project not found</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns text-center\">"
				  + "<p>The project you requested was not found.</p>"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\">Click here to go back to the overview</a>"
				  + "</div>"
				  + "</div>"
				  + "</section>"
				  + "</div>"
				  // required JavaScript
				  + "<script src=\"js/vendor/jquery.js\"></script>"
				  + "<script src=\"js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");  	
    }
}
