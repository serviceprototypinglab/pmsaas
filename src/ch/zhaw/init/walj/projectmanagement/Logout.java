package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;

/**
 * project management tool, logout page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/logout")
public class Logout extends HttpServlet {

	/*
	 * method to handle get requests
	 * invalidates session (logout)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	// set response content type
    	response.setContentType("text/html;charset=UTF8");
    	
    	// get session and invalidate it
    	HttpSession session = request.getSession();
		session.invalidate();
    	
		// get print writer
		PrintWriter out = response.getWriter();
    	
		// print html
    	out.println(HTMLHeader.getInstance().getHeader("Logout", "../") 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<section>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-4 small-offset-4 end columns\">"
				  + "<img src=\"../img/logo.png\">"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<h1 class=\"align-center\">Logout</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns align-center\">"
				  + "<h5>Thank you for using Project Management SaaS</h5>"
				  + "<p><a href=\"../login\">Click here to log in again</a></p>"
				  + "</div>"
				  + "</section>"
				  + "</div>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");  	
    }
}
