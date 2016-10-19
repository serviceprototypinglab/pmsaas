package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.PasswordService;

@SuppressWarnings("serial")
@WebServlet("/login")
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");
    	
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
    	
		PrintWriter out = response.getWriter();
    	
    	out.println(HTMLHeader.getInstance().getHeader("Login", "") 
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
				  + "<h1 class=\"align-center\">Login</h1>"
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
				  + "<input type=\"submit\" class=\"expanded button\" value=\"Login\">" 
				  + "</div>"
				  + "</div>"
				  + "</form"
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");
    	
    	
    	// Database access information
    	String url = "jdbc:mysql://localhost:3306/";
    	String dbName = "projectmanagement";
    	String userName = "Janine";
    	String pw = "test123";

    	// connection to database
    	DBConnection con = new DBConnection(url, dbName, userName, pw);
    	
    	String user = request.getParameter("mail");
        String password = request.getParameter("password");
        
        password = PasswordService.getInstance().encrypt(password);
        Employee e = con.findUser(user, password);
        
        try {
        	int id = e.getID();
            request.getSession().setAttribute("user", e.getFirstName());
            request.getSession().setAttribute("ID", id);
            response.sendRedirect(request.getContextPath() + "/Projects/Overview");
        } catch (NullPointerException ex){
            request.setAttribute("error", "Unknown login, try again");
            doGet(request, response);        	
        }
    }
}
