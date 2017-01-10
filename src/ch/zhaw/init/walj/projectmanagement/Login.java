package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.PasswordService;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;

/**
 * project management tool, login page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/login")
public class Login extends HttpServlet {
	
	
	/*
	 * method to handle get requests
	 * shows login form and error message in case of wrong user/password
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");

    	// connection to database
    	DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
    	
    	// send redirect to setup if there are no users yet (first initialization) 
    	if (con.noUsers()){
            String loginURI = request.getContextPath() + "/setup";
    		response.sendRedirect(loginURI);
    		return;
    	}
    	
    	// set error message (in case of wrong user/password)
    	String message = "";    	
    	if (request.getAttribute("error") != null){
    		message = "<div class=\"row\">" 
  					+ "<div class=\"small-6 small-offset-3 end columns\">"
    				+ "<div class=\"row\">" 
				    + "<div class=\"callout alert\">" 
				    + "<h5>E-Mail or password not correct</h5>"
				    + "</div></div>"
				    + "</div></div>";
    	}
    	
    	// get print writer
		PrintWriter out = response.getWriter();
    	
		// print html
    	out.println(HTMLHeader.getInstance().printHeader("Sign in - Project Management Saas", "") 
				  + "<body>"
				  + "<div id=\"wrapper\">" 
				  + "<section>" 
				  + "<div class=\"row\">" 
				  + "<div class=\"small-4 small-offset-4 end columns\">"
				  + "<img src=\"img/logo.png\">"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<h1 class=\"align-center\">Project Management SaaS</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns text-center\">"
				  + "<form method=\"post\" action=\"login\" data-abide novalidate>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Fill in your username and your password</p></div>"
				  + "</div>"
				  + "</div>"
				  + message
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
  				  +	"<div class=\"input-group\">"
  				  +	"<span class=\"input-group-label\"><i class=\"fa fa-user\"></i></span>"
				  + "<input type=\"text\" class=\"input-group-field\" name=\"mail\" required>" 
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns text-center\">"
				  +	"<div class=\"input-group\">"
  				  +	"<span class=\"input-group-label\"><i class=\"fa fa-key\"></i></span>"
				  + "<input type=\"password\" class=\"input-group-field\" name=\"password\" required>" 
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns text-center\">"
				  + "<input type=\"submit\" class=\"expanded button\" value=\"sign in\">" 
				  + "</div>"
				  + "</div>"
				  + "</form"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns text-center\">"
				  + "<a href=\"resetPassword\">Forgot your password?</p>" 
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  // required JavaScript
				  + "<script src=\"js/vendor/jquery.js\"></script>"
				  + "<script src=\"js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");  	
    }

    /*
	 * method to handle post requests
	 * adjusts login data with database
	 * sends redirect to overview page if login was correct
	 * calls get method with error attribute if login was wrong
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// connection to database
    	DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
    	
    	// get parameters
    	String user = request.getParameter("mail");
        String password = request.getParameter("password");
        
        // encrypt password
        password = PasswordService.getInstance().encrypt(password);
        
        // try to find user in database
        Employee e = con.findUser(user, password);
        
        // new session with attribute user (first name), id and kuerzel, 
        // set max inactive time to 60 minutes
        try {
        	int id = e.getID();
            request.getSession().setAttribute("user", e.getFirstName());
            request.getSession().setAttribute("ID", id);
            request.getSession().setAttribute("kuerzel", e.getKuerzel());
            request.getSession().setMaxInactiveInterval(60*60);
            response.sendRedirect(request.getContextPath() + "/Projects/Overview");
        } catch (NullPointerException ex){
            request.setAttribute("error", "Unknown login, try again");
            doGet(request, response);        	
        }
    }
}
