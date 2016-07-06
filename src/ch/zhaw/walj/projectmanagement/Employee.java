package ch.zhaw.walj.projectmanagement;

<<<<<<< HEAD

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/newEmployee")
public class Employee extends HttpServlet{
	protected void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
			// TODO Auto-generated method stub
	}
}
=======
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/newEmployee")
public class Employee extends HttpServlet{
	
}
>>>>>>> branch 'master' of https://srv-lab-t-401.zhaw.ch/walj/project_management.git
