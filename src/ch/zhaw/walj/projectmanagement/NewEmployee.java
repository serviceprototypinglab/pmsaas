package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/newEmployee")
public class NewEmployee extends HttpServlet {
	
	private String			firstname;
	private String			lastname;
	private String			kuerzel;
	private String			mail;
	private String			wage;
	
	String					url			= "jdbc:mysql://localhost:3306/";
	String					dbName		= "projectmanagement";
	String					userName	= "Janine";
	String					password	= "test123";
	
	private DBConnection	con			= new DBConnection(url, dbName, userName, password);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		firstname = request.getParameter("firstname");
		lastname = request.getParameter("lastname");
		kuerzel = request.getParameter("kuerzel");
		mail = request.getParameter("mail");
		wage = request.getParameter("wage");
		
		try {
			con.newEmployee(1, firstname, lastname, kuerzel, mail, wage);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		final PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		out.println("<p>Neuer Mitarbeiter:</p>");
		out.println("<p>Name: " + firstname + " " + lastname + "</p>");
		out.println("<p>Kürzel: " + kuerzel + "</p>");
		out.println("<p>Mail: " + mail + "</p>");
		out.println("<p>Stundenlohn: " + wage + "</p>");
		out.println("</body>");
		out.println("</html>");
		
	}
}
