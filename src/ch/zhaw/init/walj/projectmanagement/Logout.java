package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/Projects/logout")
public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");
    	
    	HttpSession session = request.getSession();
		session.invalidate();
    	
		PrintWriter out = response.getWriter();
    	
    	out.println("<!DOCTYPE html>" 
				  + "<html>" 
				  // HTML head
				  + "<head>" 
				  + "<meta charset=\"UTF-8\">"
				  + "<title>Login</title>" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />" 
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />" 
				  + "</head>" 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<section>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-4 small-offset-4 end columns\">"
				  + "<img src=\"../img/service_engineering_logo.png\">"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<h1 class=\"align-center\">Logout</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<div class=\"callout success\">" 
				  + "<h5>You were logged out</h5>"
				  + "<p><a href=\"../login\">Click here to log in again</a></p>"
				  + "</div>"
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
