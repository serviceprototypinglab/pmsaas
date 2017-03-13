/**
 *	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 *	All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package ch.zhaw.init.walj.projectmanagement.admin.properties;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Employee;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * Projectmanagement tool, admin properties page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/admin/properties")
public class AdminProperties extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		DBConnection con = new DBConnection(this.getServletContext().getRealPath("/"));
		
		// set error/success message
    	String message = "";    	
    	if (request.getAttribute("msg") != null){
    		message = (String) request.getAttribute("msg");
    	}    	
		ArrayList<Employee> employees = new ArrayList<>();
		
		try {
			employees = con.getAllEmployees();
		} catch (SQLException e) {
			
		}
		
		ArrayList<Project> projects = new ArrayList<>();
		ArrayList<Project> p = new ArrayList<>();
		
		try {
			for (Employee e : employees){
				p = con.getProjects(e.getID(), false);
				if (p != null){
					projects.addAll(p);
				}
			}
		} catch (SQLException e) {}
		
		ArrayList<Project> archivedProjects = new ArrayList<>();
		
		try {
			for (Employee e : employees){
				p = con.getProjects(e.getID(), true);
				if (p != null){
					archivedProjects.addAll(p);
				}
			}
		} catch (SQLException e) {}
		
		out.println(HTMLHeader.getInstance().printHeader("Admin Properties", "../", "Properties", "", "", true)
				  // HTML section with panels
				  + "<section>"
				  + "<div class=\"row\">"
				  + "<h2>Hello Admin!</h2>"
				  + "</div>"
				  
// -------------- change logo panel -----------------------------------------------------------------------------------------------
				  + "<div class=\"row\">"
				  + "<div class=\"panel small-12 columns\">"
				  + "<div class=\"row round\">"
				  + "<div class=\"small-12 columns\">"
				  + "<h3>Change Logo</h3>"
				  + message
				  + "<p>Upload your company logo as a PNG file</p><br>"
				  + "</div>"
				  + "<hr>"
				  + "<div class=\"small-12 columns\">"
				  + "<h4>Large logo</h4>"
				  + "<form method=\"post\" action=\"?size=large\"  enctype=\"multipart/form-data\" data-abide novalidate>"
				  + "<input type=\"file\" name=\"file\">"
				  + "<input type=\"submit\" value=\"Upload Logo\" class=\"small-2 columns button float-right create\">"
				  + "</form>"
				  + "</div>"
				  + "<hr>"
				  + "<div class=\"small-12 columns\">"
				  + "<h4>Small logo</h4>"
				  + "<form method=\"post\" action=\"?size=small\"  enctype=\"multipart/form-data\" data-abide novalidate>"
				  + "<input type=\"file\" name=\"file\">"
				  + "<input type=\"submit\" value=\"Upload Logo\" class=\"small-2 columns button float-right create\">"
				  + "</form>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  
// -------------- Employees Panel -----------------------------------------------------------------------------------------------
				  + "<div class=\"row\">"
				  + "<div class=\"panel small-12 columns\">"
				  + "<div class=\"row round\">"
				  + "<div class=\"small-9 columns\">"
				  + "<h3>Edit Employees</h3>"
				  + "</div>"
				  + "<div class=\"small-3 columns\">"
				  + "<a href=\"newEmployee\" class=\"button float-right extended\">New Employee <i class=\"fa fa-user-plus\"></i> </a>"
				  + "</div>"
				  + "<div class=\"small-12 columns\">"
				  + "<table class=\"hover\">"
				  + "<tbody>");
		
		for (Employee employee : employees){
			out.println("<tr>"
					  + "<td>" + employee.getFullName() + "</td>"
					  + "<td></td>"
					  + "<td style=\"width: 10%\"><a href=\"editEmployee?id=" + employee.getID() + "\" title=\"Edit\"><i class=\"fa fa-pencil-square-o fa-lg\"></i></a></td>"
					  + "<td style=\"width: 10%\"><a title=\"Delete\" id=\"deleteUser\" data-open=\"deleteEmployee" + employee.getID() + "\"><i class=\"fa fa-trash fa-lg\"></i></a></td>"
					  + "</tr>"
					  + "<div class=\"reveal\" id=\"deleteEmployee" + employee.getID() + "\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded alert button\" href=\"deleteEmployee?id=" + employee.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Employee</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>");
		}
		
		out.println("</tbody>"
				  + "</table>"				  
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  
// -------------- Projects Panel -----------------------------------------------------------------------------------------------
				  + "<div class=\"row\">"
				  + "<div class=\"panel small-12 columns\">"
				  + "<div class=\"row round\">"
				  + "<div class=\"small-12 columns\">"
				  + "<h3>Edit Projects</h3>"
				  + "</div>"
				  + "<div class=\"small-12 columns\">"
				  + "<table class=\"hover\">"
				  + "<thead>"
				  + "<tr>"
				  + "<th>Project Name</th>"
				  + "<th>Project Owner</th>"
				  + "<th style=\"width: 10%\"></th>"
				  + "<th style=\"width: 10%\"></th>"
				  + "<th style=\"width: 10%\"></th>"
				  + "</tr>"
				  + "</thead>"
				  + "<tbody>");
		
		for (Project project : projects){
			try {
				out.println("<tr>"
						  + "<td>" + project.getName() + "</td>"
						  + "<td>" + con.getEmployee(project.getLeader()).getName() + "</td>"					  	
						  + "<td style=\"width: 10%\"><a href=\"editProject?id=" + project.getID() + "\" title=\"Edit\"><i class=\"fa fa-pencil-square-o fa-lg\"></i></a></td>"
						  + "<td style=\"width: 10%\"><a data-open=\"archive" + project.getID() + "\" title=\"Archive\"><i class=\"fa fa-archive fa-lg\"></i></a></td>"
						  + "<td style=\"width: 10%\"><a data-open=\"delete" + project.getID() + "\" title=\"Delete\"><i class=\"fa fa-trash fa-lg\"></i></a></td>"
						  + "</tr>");
			} catch (SQLException e) {}
			
			// archive project window
			out.println("<div class=\"reveal\" id=\"archive" + project.getID() + "\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded warning button\" href=\"archiveProject?projectID=" + project.getID() + "\"><i class=\"fa fa-archive\"></i> Archive Project</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>");
			
			// delete project window
			out.println("<div class=\"reveal\" id=\"delete" + project.getID() + "\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded alert button\" href=\"deleteProject?projectID=" + project.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Project</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>");
		}
		
		for (Project project : archivedProjects){
			try {
				out.println("<tr>"
						  + "<td>" + project.getName() + " (Archived)</td>"
						  + "<td>" + con.getEmployee(project.getLeader()).getName() + "</td>"	
						  + "<td style=\"width: 10%\"><a href=\"editProject?id=" + project.getID() + "\" title=\"Edit\"><i class=\"fa fa-pencil-square-o fa-lg\"></i></a></td>"
						  + "<td style=\"width: 10%\"><a data-open=\"restore" + project.getID() + "\" title=\"Restore\"><i class=\"fa fa-undo fa-lg\"></i></a></td>"
						  + "<td style=\"width: 10%\"><a data-open=\"delete" + project.getID() + "\" title=\"Delete\"><i class=\"fa fa-trash fa-lg\"></i></a></td>"
						  + "</tr>");
			} catch (SQLException e) {}
			
			// archive project window
			out.println("<div class=\"reveal\" id=\"restore" + project.getID() + "\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded warning button\" href=\"restoreProject?projectID=" + project.getID() + "\"><i class=\"fa fa-undo\"></i> Restore Project</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>");
			
			// delete project window
			out.println("<div class=\"reveal\" id=\"delete" + project.getID() + "\" data-reveal>"
					  + "<h1 class=\"align-left\">Are you sure?</h1>"
					  + "<p class=\"lead\"></p>"
					  + "<a class=\"expanded alert button\" href=\"deleteProject?projectID=" + project.getID() + "\"><i class=\"fa fa-trash\"></i> Delete Project</a>"
					  + "<button class=\"close-button\" data-close aria-label=\"Close reveal\" type=\"button\">"
					  + "<span aria-hidden=\"true\">&times;</span>"
					  + "</button>"
					  + "</div>");
		}
		
		out.println("</tbody>"
				  + "</table>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</div>"
				  + "</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  // required JavaScript
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	

    
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean isMultipart;
		String filePath;
	    int maxFileSize = 9000 * 1024;
	    int maxMemSize = 4 * 1024;
	    File file ;
		
		
	    isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter( );
		      
		boolean small = request.getParameter("size").equals("small");
		  
		String message = "";
		  
		if(!isMultipart){
			message = "<div class=\"row\">" 
					+ "<div class=\"small-12 columns\">"
					+ "<div class=\"row\">" 
				    + "<div class=\"callout alert\">" 
				    + "<h5>Upload failed</h5>"
				    + "</div>"
				    + "</div>"
				    + "</div>"
				    + "</div>";
		}
		  
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(this.getServletContext().getRealPath("/") + "img/"));
		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax( maxFileSize );
			
		try { 
			// Parse the request to get file items.
			List<?> fileItems = upload.parseRequest(request);
			
			// Process the uploaded file items
			Iterator<?> i = fileItems.iterator();
			
			  
			while (i.hasNext()){
				  
				FileItem fi = (FileItem)i.next();
			     
				if (!fi.isFormField()){
					// Get the uploaded file parameters
			        String contentType = fi.getContentType();
			        if (contentType.equals("image/png")){
				        String fileName;
				        if (small){
				        	fileName = "logo_small.png";
				        } else {
				        	fileName = "logo.png";
				        }
				        filePath = this.getServletContext().getRealPath("/") + "img/" + fileName;
				        // Write the file
				        file = new File(filePath) ;
				        fi.write( file ) ;	
			        } else {
			        	throw new Exception("type");
			        }
		        }
			}		  	
			message = "<div class=\"row\">" 
					+ "<div class=\"small-12 columns\">"
					+ "<div class=\"row\">" 
				    + "<div class=\"callout success\">" 
				    + "<h5>Logo uploaded successfully</h5>"
				    + "</div>"
				    + "</div>"
				    + "</div>"
				    + "</div>";	
			request.setAttribute("msg", message);
		} catch(Exception ex) {
			
			if (ex.getMessage().equals("type")){

				message = "<div class=\"row\">" 
						+ "<div class=\"small-12 columns\">"
						+ "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Wrong Type</h5>"
					    + "<p>Please upload a PNG image</p>"
					    + "</div>"
					    + "</div>"
					    + "</div>"
					    + "</div>";
			} else {
				message = "<div class=\"row\">" 
						+ "<div class=\"small-12 columns\">"
						+ "<div class=\"row\">" 
					    + "<div class=\"callout alert\">" 
					    + "<h5>Upload failed</h5>"
					    + "</div>"
					    + "</div>"
					    + "</div>"
					    + "</div>";
			}
			request.setAttribute("msg", message);
		}
		doGet(request, response);
	}
}
