package ch.zhaw.init.walj.projectmanagement;

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
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.Mail;
import ch.zhaw.init.walj.projectmanagement.util.PasswordGenerator;
import ch.zhaw.init.walj.projectmanagement.util.PasswordService;

@SuppressWarnings("serial")
@WebServlet("/resetPassword")
public class ResetPassword extends HttpServlet {
		
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
    	
    	out.println(HTMLHeader.getInstance().getHeader("Reset password - Project Management Saas", "") 
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
				  + "<h1 class=\"align-center\">Reset your password</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns text-center\">"
				  + "<form method=\"post\" action=\"resetPassword\" data-abide novalidate>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Fill in your e-mail address.</p></div>"
				  + "</div>"
				  + "</div>"
				  + message
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
  				  +	"<div class=\"input-group\">"
  				  +	"<span class=\"input-group-label\"><i class=\"fa fa-envelope\"></i></span>"
				  + "<input type=\"email\" class=\"input-group-field\" name=\"mail\" required>" 
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns text-center\">"
				  + "<input type=\"submit\" class=\"expanded button\" value=\"Reset Password\">" 
				  + "</div>"
				  + "</div>"
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF8");

    	// connection to database
    	DBConnection con = new DBConnection();
    	
    	String user = request.getParameter("mail");
        
        Employee e = con.findUser(user);
                
        String message = "";
        
        try {
        String newPassword = PasswordGenerator.getInstance().getNewPassword();
        
        e.setNewPassword(newPassword);
        
        con.updatePassword(e.getID(), PasswordService.getInstance().encrypt(e.getPassword()));
        
        Mail mail = new Mail(e);
               
		mail.sendResetPasswordMail();
		

        response.sendRedirect(request.getContextPath() + "/login");
    	
	    	
	    	
		} catch (NullPointerException | MessagingException | SQLException e1) {
			message = "<div class=\"row\">" 
					   + "<div class=\"small-6 small-offset-3 end columns\">"
					   + "<div class=\"row\">" 
					   + "<div class=\"callout alert\">" 
					   + "<h5>E-mail not found.</h5>"
					   + "</div></div>"
					   + "</div></div>";
		}
        
		PrintWriter out = response.getWriter();
		
		out.println(HTMLHeader.getInstance().getHeader("Reset password - Project Management Saas", "") 
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
				  + "<h1 class=\"align-center\">Reset your password</h1>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns text-center\">"
				  + "<form method=\"post\" action=\"resetPassword\" data-abide novalidate>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
				  + "<div data-abide-error class=\"alert callout\" style=\"display: none;\">"
				  + "<p><i class=\"fa fa-exclamation-triangle\"></i> Fill in your e-mail address.</p></div>"
				  + "</div>"
				  + "</div>"
				  + message
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns\">"
  				  +	"<div class=\"input-group\">"
  				  +	"<span class=\"input-group-label\"><i class=\"fa fa-envelope\"></i></span>"
				  + "<input type=\"email\" class=\"input-group-field\" name=\"mail\" required>" 
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-6 small-offset-3 end columns text-center\">"
				  + "<input type=\"submit\" class=\"expanded button\" value=\"Reset Password\">" 
				  + "</div>"
				  + "</div>"
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
    }
}
