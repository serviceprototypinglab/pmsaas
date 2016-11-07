package ch.zhaw.init.walj.projectmanagement.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;

/**
 * Servlet implementation class Edit
 */
@SuppressWarnings("serial")
@WebServlet("/admin/properties")
public class Properties extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		DBConnection con = new DBConnection();
		
	
	}
}
