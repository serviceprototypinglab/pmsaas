package ch.zhaw.init.walj.projectmanagement.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Mail;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;

@SuppressWarnings("serial")
@WebServlet("/setup")
public class Setup extends HttpServlet {
	
	// connection to database
	DBConnection con = new DBConnection();
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");
    	
    	if (con.noUsers()){
			PrintWriter out = response.getWriter();	    	
			
			String message = "";
	    	
	    	if (request.getAttribute("error") != null){
	    		message = "<div class=\"row\">" 
	  					+ "<div class=\"small-6 small-offset-3 end columns\">"
	    				+ "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Something went wrong.</h5>"
					    + "</div></div>"
					    + "</div></div>";
	    	}
			
			
	    	out.println(HTMLHeader.getInstance().printHeader("Setup - Project Management Saas", "") 
					  + "<body>"
					  + "<div id=\"wrapper\">" 
					  + "<section>" 
					  + "<div class=\"row\">" 
					  + "<div class=\"small-4 columns\">"
					  + "<img src=\"img/logo.png\" class=\"add\">"
					  + "</div>"
					  + "</div>"
					  + "<div class=\"row\">" 
					  + "<div class=\"small-12 columns\">"
					  + "<h1 class=\"blue add\">Project Management SaaS</h1>"
					  + "</div>"
					  + "<div class=\"small-12 columns\">"
					  + "<h2 class=\"blue\">Initial setup</h2>"
					  + "</div>"
					  + "</div>"
					  + "<div class=\"row\">" 
					  + "<div class=\"small-12 columns\">"
					  + message
					  + "<form method=\"post\" action=\"setup\" data-abide novalidate>"
					  // error message (if something's wrong with the form)
					  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
					  + "<p><i class=\"fa fa-exclamation-triangle\"></i> There are some errors in your form.</p>"
					  + "</div>"
					  + "<div class=\"small-12 columns\">"
					  + "<p>Set the Admin's e-mail and password</p>"
					  + "</div>"
					  // field for e-mail address
					  + "<label class=\"small-12 columns\">Admin Mail "
					  + "<input type=\"email\" name=\"mail\" required>"
					  + "</label>"
					  // field for password
					  + "<label class=\"small-12 large-6 end columns\">New Password "
					  + "<input type=\"password\" name=\"password\" id=\"password\" required>"
					  + "<span class=\"form-error\">"
			          + "Password is required!"
			          + "</span>"
					  + "</label>"
					  // field to re-enter password
					  + "<label class=\"small-12 large-6 end columns\">Re-enter Password "
					  + "<input type=\"password\" data-equalto=\"password\" required>"
					  + "<span class=\"form-error\">"
			          + "Passwords don't match!"
			          + "</span>"
					  + "</label>"
					  // submit button
					  + "<input type=\"submit\" class=\"small-3 columns large button float-right create\" value=\"submit\">"
					  + "</form>"
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
    	} else {
    		String url = request.getContextPath() + "/login";
            response.sendRedirect(url);
    	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");

    	String mailAddress = request.getParameter("mail");
        String password = request.getParameter("password");
        
        
        try {
        	Employee e = con.newEmployee(0, "Admin", " ", "admin", mailAddress, password, 0);
        	int id = e.getID();
            request.getSession().setAttribute("user", e.getFirstName());
            request.getSession().setAttribute("ID", id);
            request.getSession().setAttribute("kuerzel", e.getKuerzel());
            request.getSession().setMaxInactiveInterval(60*60);
            
            Mail mail = new Mail(e);
            
            mail.sendInitialSetupMail();
            
            PrintWriter out = response.getWriter();	    	
	    	        
            out.println(HTMLHeader.getInstance().printHeader("Setup - Project Management Saas", "") 
					  + "<body>"
					  + "<div id=\"wrapper\">" 
					  + "<section>" 
					  + "<div class=\"row\">" 
					  + "<div class=\"small-4 columns\">"
					  + "<img src=\"img/logo.png\" class=\"add\">"
					  + "</div>"
					  + "</div>"
					  + "<div class=\"row\">" 
					  + "<div class=\"small-12 columns\">"
					  + "<h1 class=\"blue add\">Project Management SaaS</h1>"
					  + "</div>"
					  + "<div class=\"small-12 columns\">"
					  + "<h2 class=\"blue\">Initial setup</h2>"
					  + "</div>"
					  + "</div>"
					  + "<div class=\"row\">" 
  					  + "<div class=\"small-12 columns\">"
  					  + "<div class=\"row\">" 
  					  + "<div class=\"callout success\">" 
  					  + "<h5>Initial setup successful</h5>"
  					  + "<p><a href=\"\">Go to properties</a></p>"
  					  + "</div>"
  					  + "</div>"
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
        } catch (NullPointerException | SQLException | MessagingException ex){
            request.setAttribute("error", "Something went wrong");
            doGet(request, response);        	
        }
    }
}
