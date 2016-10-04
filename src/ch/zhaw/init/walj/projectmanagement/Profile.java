package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.PasswordService;

/**
 * Servlet implementation class Edit
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/employee")
public class Profile extends HttpServlet {
	
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "projectmanagement";
	private String userName	= "Janine";
	private String password	= "test123";
	
	DBConnection con = new DBConnection(url, dbName, userName, password);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		int id = (int) request.getSession(false).getAttribute("ID");
						
		Employee employee = null;
		employee = con.getEmployee(id);
		
		
		PrintWriter out = response.getWriter();
				
			out.println("<!DOCTYPE html>" 
					  + "<html>"
					  // HTML head
					  + "<head>" 
					  + "<meta charset=\"UTF-8\">"
					  + "<title>" + employee.getName() + "</title>" 
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\" />" 
					  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />" 
					  + "</head>" 
					  + "<body>"
					  + "<div id=\"wrapper\">" 
					  + "<header>" 
					  + "<div class=\"row\">" 
					  + "<div class=\"small-8 columns\">"
					  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
					  // title
					  + "<h1>" + employee.getName() + "</h1>"
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
					  + "<div class=\"small-12 columns\">"
					  + "<ul class=\"menu\">"
					  + "<li><a href=\"#user\">My Profile</a></li>"
					  + "<li><a href=\"#changePassword\">Change Password</a></li>"
					  + "<li><a href=\"#employees\">Other Employees</a></li>"
					  + "</ul>"
					  + "<hr>"
					  + "<br>"
					  + "</div>"
					  + "</div>"
					  
					  // edit own account
					  + "<div class=\"row\" id=\"user\">"
					  + "<div class=\"small-2 columns\">"
					  + "<i class=\"fa fa-user blue big-icon\"></i>"
					  + "</div>"
					  + "<div class=\"small-10 columns\">"
					  + "<form method=\"post\" action=\"employee\" data-abide novalidate>"
					  + "<input type=\"hidden\" name=\"id\" value=\"" + employee.getID() + "\">"
					  + "<label class=\"small-10 end columns\">First Name "
					  + "<input type=\"text\" value=\"" + employee.getFirstName() + "\" name=\"firstname\" required>"
					  + "</label>"
					  + "<label class=\"small-10 end columns\">Last Name "
					  + "<input type=\"text\" value=\"" + employee.getLastName() + "\" name=\"lastname\" required>"
					  + "</label>"
					  + "<label class=\"small-10 end columns\">Kuerzel "
					  + "<input type=\"text\" value=\"" + employee.getKuerzel() + "\" name=\"kuerzel\" required>"
					  + "</label>"
					  + "<label class=\"small-10 end columns\">E-Mail "
					  + "<input type=\"text\" value=\"" + employee.getMail() + "\" name=\"mail\" required>"
					  + "</label>"
					  + "<label class=\"small-10 end columns\">Wage "
					  + "<input type=\"number\" value=\"" + employee.getWage() + "\" name=\"wage\" required>"
					  + "</label>"
					  + "<input type=\"hidden\" name=\"oldwage\" value=\"" + employee.getWage() + "\">"
					  + "<div class=\"small-3 small-offset-7 columns end\">" 			  
					  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
					  + "</div>"
					  + "</form>"
					  + "</div>"
					  + "</div>"

					  // change password
					  + "<div class=\"row\" id=\"changePassword\">"
					  + "<div class=\"small-2 columns\">"
					  + "<i class=\"fa fa-key blue big-icon\"></i>"
					  + "</div>"
					  + "<div class=\"small-10 columns\">"
					  + "<form method=\"post\" action=\"employee\" data-abide novalidate>"
					  + "<input type=\"hidden\" name=\"id\" value=\"" + employee.getID() + "\">"
					  + "<label class=\"small-10 end columns\">New Password "
					  + "<input type=\"password\" name=\"password\" id=\"password\" required>"
					  + "<span class=\"form-error\">"
			          + "Password is required!"
			          + "</span>"
					  + "</label>"
					  + "<label class=\"small-10 end columns\">Re-enter Password "
					  + "<input type=\"password\" data-equalto=\"password\" required>"
					  + "<span class=\"form-error\">"
			          + "Passwords don't match!"
			          + "</span>"
					  + "</label>"
					  + "<div class=\"small-3 small-offset-7 columns end\">" 			  
					  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
					  + "</div>"
					  + "</form>"
					  + "</div>"
					  + "</div>");
					  
			// other users
			ArrayList<Employee> employees;
			try {
				employees = con.getAllEmployees(id);
				
				if(employees.size() > 1){
					out.println("<div class=\"row\" id=\"employees\">"
							  + "<div class=\"small-2 columns\">"
							  + "<i class=\"fa fa-users blue big-icon\"></i>"
							  + "</div>");
					
					int i = 0;
					for (Employee e : employees){
						if (e.getID() != id){
							
							String offset = "";
							String hr = "";
							if (i != 0){
								offset = "small-offset-2 ";
								hr = "<div class=\"small-10 end columns\">"
								   + "<hr>"
								   + "<br>"
								   + "</div>";
							}
							i++;
							out.println("<div class=\"small-10 " + offset + "columns\">"
									  + hr
									  + "<form method=\"post\" action=\"employee\" data-abide novalidate>"
									  + "<input type=\"hidden\" name=\"id\" value=\"" + e.getID() + "\">"
									  + "<label class=\"small-10 end columns\">First Name "
									  + "<input type=\"text\" value=\"" + e.getFirstName() + "\" name=\"firstname\" required>"
									  + "</label>"
									  + "<label class=\"small-10 end columns\">Last Name "
									  + "<input type=\"text\" value=\"" + e.getLastName() + "\" name=\"lastname\" required>"
									  + "</label>"
									  + "<label class=\"small-10 end columns\">Kuerzel "
									  + "<input type=\"text\" value=\"" + e.getKuerzel() + "\" name=\"kuerzel\" required>"
									  + "</label>"
									  + "<label class=\"small-10 end columns\">E-Mail "
									  + "<input type=\"text\" value=\"" + e.getMail() + "\" name=\"mail\" required>"
									  + "</label>"
									  + "<label class=\"small-10 end columns\">Wage "
									  + "<input type=\"number\" value=\"" + e.getWage() + "\" name=\"wage\" required>"
									  + "</label>"
									  + "<input type=\"hidden\" name=\"oldwage\" value=\"" + e.getWage() + "\">"
									  + "<div class=\"small-3 small-offset-7 columns end\">" 			  
									  + "<input type=\"submit\" class=\"expanded button\" value=\"Apply\">"
									  + "</div>"
									  + "</form>"
									  + "</div>");
						}
					}					 
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		   			
			
			
			
			out.println("</section>"
					  + "</div>"
					  + "<script src=\"../js/vendor/jquery.js\"></script>"
					  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
					  + "<script>$(document).foundation();</script>"
					  + "</body>"
					  + "</html>");
		
	}
	
	@Override
	// method to handle post-requests
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		int id = (int) request.getSession(false).getAttribute("ID");
		
		// variable declaration, get parameters
		int userID = Integer.parseInt(request.getParameter("id"));
		
		String firstname;
		String lastname;
		String kuerzel;
		String mail;
		int wage;
		int oldWage;
		
		String message = "";
		
		String password = request.getParameter("password");
		
		if (password == null){
			firstname = request.getParameter("firstname");
			lastname = request.getParameter("lastname");
			kuerzel = request.getParameter("kuerzel");
			mail = request.getParameter("mail");
			wage = Integer.parseInt(request.getParameter("wage"));
			oldWage = Integer.parseInt(request.getParameter("oldwage"));
			
			Date today = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(today);
		
			try {
				con.updateUser(userID, firstname, lastname, kuerzel, mail);
				
				if (wage != oldWage){
					con.newWage(userID, wage, date);
				}
				

				message = "<div class=\"callout success\">"
						+ "<h5>Project successfully updated</h5>"
						+ "<p>The user has succsessfully been updated with the following data:</p>"
						+ "<p>First Name: " + firstname + "</p>"
						+ "<p>Last Name: " + lastname + "</p>"
						+ "<p>Kuerzel: " + kuerzel + "</p>"
						+ "<p>E-Mail: " + mail + "</p>"
						+ "<p>Wage per Hour: " + wage + "</p>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
				
			} catch (SQLException e) {
				message = "<div class=\"callout alert\">"
					    + "<h5>User could not be updated</h5>"
					    + "<p>An error occured and the user could not be updated.</p>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
			}	
		} else {
			password = PasswordService.getInstance().encrypt(password);
			try {
				con.updatePassword(userID, password);
				
				message = "<div class=\"callout success\">"
						+ "<h5>Password successfully updated</h5>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
			} catch (SQLException e) {
				message = "<div class=\"callout alert\">"
					    + "<h5>Password could not be updated</h5>"
					    + "<p>An error occured and the password could not be updated.</p>"
						+ "<a href=\"/Projektverwaltung/Projects/employee\">Click here to go back to the user page</a>"
						+ "</div>";
			}
		}		
		
		out.println("<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>New Project</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/font-awesome/css/font-awesome.min.css\">"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\" />"
				  + "</head>"
				  + "<body>"
				  + "<div id=\"wrapper\"><header>"
				  + "<div class=\"row\">"
				  + "<div class=\"small-8 columns\">"
				  + "<img src=\"../img/logo_small.png\" class=\"small-img left\">"
				  // title
				  + "<h1>Update User</h1>"
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
				  + "</header>"
				  // HTML section with form
				  + "<section>"
				  + "<div class=\"row\">"
				  + message
				  + "</div>"
				  + "</section>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
		
	}
}
