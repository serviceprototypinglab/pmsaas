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

package ch.zhaw.init.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.zhaw.init.walj.projectmanagement.util.HTMLFooter;
import ch.zhaw.init.walj.projectmanagement.util.HTMLHeader;

/**
 * project management tool, help page
 * 
 * @author Janine Walther, ZHAW
 */
@SuppressWarnings("serial")
@WebServlet("/Projects/help")
public class Help extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF8");
		
		PrintWriter out = response.getWriter();
		
		out.println(HTMLHeader.getInstance().printHeader("Help", "../", "Help", "")
				  + "<section>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns\">"
				  + "<h2 id=\"overview\" class=\"no-margin\">Help page</h2>"
				  + "<ol>"
				  	  + "<li><a href=\"#overview\">Overview</a>"
						  + "<ol>"
							  + "<li><a href=\"#yourProjects\">Your Projects</a>"
							  + "</li>"
							  + "<li><a href=\"#otherProjects\">Other Projects</a>"
							  + "</li>"
							  + "<li><a href=\"#archivedProjects\">Archived Projects</a>"
							  + "</li>"
						  + "</ol>"
					  + "</li>"
					  + "<li><a href=\"#projectOverview\">Project Overview</a>"
						  + "<ol>"
							  + "<li><a href=\"#budget\">Budget</a>"
							  + "</li>"
							  + "<li><a href=\"#effort\">Effort</a>"
							  + "</li>"
							  + "<li><a href=\"#wAndT\">Workpackages & Tasks</a>"
							  + "</li>"
							  + "<li><a href=\"#SEAProject\">Share/Edit/Archive Project</a>"
							  + "</li>"
						  + "</ol>"
					  + "</li>"
					  + "<li><a href=\"#newProject\">New Project</a>"
					  + "</li>"
					  + "<li><a href=\"#newEmployee\">New Employee</a>"
					  + "</li>"
					  + "<li><a href=\"#myProfile\">MyProfile</a>"
					  + "</li>"
				  + "</ol>"
				  + "</div>"
				  + "</div>"
				  + "<div class=\"row\">" 
				  + "<div class=\"small-12 columns\">"
				  //--------------------------------------------- Overview ---------------------------------------------
				  + "<h2 id=\"overview\" class=\"no-margin blue\">Overview</h2>"
				  + "<p>"
				  + "On the Overview page, you can see your projects, projects others shared with you and your archived projects."
				  + "If there are no projects shared with you or no archived projects, you won’t see these categories. "
				  + "</p>"
				  + "<br>"
				  + "<h3 id=\"yourProjects\" class=\"no-margin\">Your Projects</h3>"
				  + "<p>"
				  + "You will see a list with every project you created, and that is not archived. "
				  + "After clicking on the name of the project you can see more details about it. These are the same details, "
				  + "that someone, with whom you shared the project, can see. "
				  + "</p>"
				  + "<hr>"
				  + "<h3 id=\"otherProjects\" class=\"no-margin\">Other Projects</h3>"
				  + "<p>"
				  + "\"Other Projects\" means projects, that someone has shared with you. "
				  + "First, you see the owner of the project and then the other details. "
				  + "You can see the details of these projects, but you cannot add, edit or delete data. "
				  + "</p>"
				  + "<hr>"
				  + "<h3 id=\"archivedProjects\" class=\"no-margin\">Archived Projects</h3>"
				  + "<p>"
				  + "Here you will find your archived projects. You can see the details of "
				  + "the project but not edit it. If you have to edit something, you have to ask your admin to restore it."
				  + "</p>"
				  + "<br><br>"
				  //--------------------------------------------- Project Overview ---------------------------------------------
				  + "<h2 id=\"projectOverview\" class=\"no-margin blue\">Project Overview</h2>"
				  + "<p>"
				  + "On the project overview page, you will find panels to monitor the budget, the effort and the workpackages and tasks. "
				  + "You can add expenses, assign employees, book hours, add workpackages and tasks, set a weight for every month, "
				  + "share, edit and archive your project. Different graphs arrange your data for a quicker and easier overview. "
				  + "</p>"
				  + "<br>"
				  + "<h3 id=\"budget\" class=\"no-margin\">Budget</h3>"
				  + "<p>"
				  + "A pie chart on the budget panel shows how much of your budget was spent and how much is left. "
				  + "Below this, you will see a table with all your expenses. Place your cursor on the type field to see "
				  + "the description of the expense. "
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Add Expenses</h4>"
				  + "<p>"
				  + "After a click on the Add Expenses button you can create a new expense for one of your employees (or yourself). "
				  + "Fill in all the fields and add a good description for better recognition. "
				  + "Click on the Add Expense button and your expense will be added to the project."
				  + "</p>"
				  + "<hr>"
				  + "<h3 id=\"effort\" class=\"no-margin\">Effort</h3>"
				  + "<p>"
				  + "The effort panel shows you the booked effort of your project, compared to your planned one in a line chart. "
				  + "Further you see every one of the assigned employees and how many hours they booked on the project. "
				  + "At the bottom, there is a total of all hours booked. To get a more detailed view of the effort "
				  + "you can click on the Details button next to the employee you like to see or the one next to the total effort. "
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Assign employees</h4>"
				  + "<p>"
				  + "Employees must be assigned to a task before you can book hours for them. To do this, "
				  + "you can click on the Assign Employees button on the top of the effort panel. "
				  + "After that, you will be asked to choose an employee. Choose your employee from the list "
				  + "and click on the button Choose Task. After tis you'll see a list of all tasks "
				  + "where your employee is not assigned yet. You can choose multiple tasks and "
				  + "assign your employee to these with clicking on the Assign button."
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Book hours</h4>"
				  + "<p>"
				  + "You can book hours for your employees with a click on the Book Hours button. "
				  + "The tool asks you to choose an employee from the list. After clicking on Choose Task "
				  + "you'll see a list of all tasks the employee is assigned to. Choose one or more tasks and click on Hours. "
				  + "On the next page, you can fill in how many hours there were and for which month you want to book them. "
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Details specific employee</h4>"
				  + "<p>"
				  + "If you click on the Details button next to an employee, you can get detailed information "
				  + "about the effort of this specific employee. First you see a line chart with the booked hours per month. "
				  + "Below that, you have a detailed list about every month where the employee has booked some hours. "
				  + "You see on which task they were booked and the costs."
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Details all employees</h4>"
				  + "<p>"
				  + "This page is very like the details page for a specific employee, but here you have "
				  + "the chart from the project overview page as a bigger image and the list contains the hours booked by all employees "
				  + "and all tasks."
				  + "</p>"
				  + "<hr>"
				  + "<h3 id=\"wAndT\" class=\"no-margin\">Workpackages & Tasks</h3>"
				  + "<p>"
				  + "The Workpackages & Tasks panel lets you have a look on all your workpackages and the tasks. "
				  + "If you must add a new workpackage or task, you can do it here. "
				  + "If needed, a different weight for each month and task can be set here. "
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Edit Weight</h4>"
				  + "<p>"
				  + "To set a different weight for each month, click on the Edit Weight button. "
				  + "You will see a list with every task and every month. Now you can set a different weight for every month. "
				  + "With a click on the button Edit Weight you can save all changes."
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Add Workpackages</h4>"
				  + "<p>"
				  + "After a click on the Add Workpackage button you can create a new workpackage. "
				  + "Make sure that the start and end date are within the start and end date of the project."
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Add Tasks</h4>"
				  + "<p>"
				  + "By clicking on the Add Task button you can add a new task to your project. "
				  + "Define the name, start and end date, number of PMs, your budget for this task and choose the workpackage "
				  + "this task belongs to. Make sure that the start and end date are within the start and end date of the workpackage."
				  + "</p>"
				  + "<hr>"
				  + "<h3 id=\"SEAProject\" class=\"no-margin\">Share/Edit/Archive Project</h3>"
				  + "<p>"
				  + "On the bottom of the page you have three buttons: Share Project, Edit Project and Archive Project."
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Share Project</h4>"
				  + "<p>"
				  + "If someone else should have access to the data of your project, you can share it with this person. "
				  + "The person will be able to see everything but cannot edit something. Click on the button "
				  + "and choose the employees you want to share the project with. You can choose as many as you want. "
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Edit Project</h4>"
				  + "<p>"
				  + "If you have made a mistake or something has changed, you can easily edit your project with a click on "
				  + "the Edit Project button. On this page, you can edit or delete nearly everything. "
				  + "The only thing you can't delete is your project (if you'd like to delete it you must talk to the admin, "
				  + "he can delete it for you). Don't forget to apply your changes!"
				  + "</p>"
				  + "<br>"
				  + "<h4 class=\"no-margin\">Archive Project</h4>"
				  + "<p>"
				  + "If your project is finished and you want to have a better overview, you can archive your project. "
				  + "Please notice that you can't restore the project by yourself and you must talk to the admin if you need it back. "
				  + "You will only see the most important details of an archived project, like name, duration, total budget, "
				  + "amount of workpackages and tasks, which employees worked on it and with which partners you realised the project."
				  + "</p>"
				  + "<br><br>"
				  //--------------------------------------------- New Project ---------------------------------------------
				  + "<h2 id=\"newProject\" class=\"no-margin blue\">New Project</h2>"
				  + "<p>"
				  + "You can create a new project by clicking on the <i class=\"fa fa-file\"></i> button. "
				  + "Fill in the fields and make sure the dates are correct. You can add as many workpackage "
				  + "and tasks as you want, but you must have at least once of each. If your project doesn’t have workpackages, "
				  + "you must make a workpackage with the same name and date like your task. In this way, "
				  + "the gant chart on the project overview page will only show the task and no workpackages. "
				  + "You will be asked for a workpackage for each task. Write here the name of your workpackage, "
				  + "make sure the writing is correct."
				  + "</p>"
				  + "<br><br>"
				  //--------------------------------------------- New Employee ---------------------------------------------
				  + "<h2 id=\"newEmployee\" class=\"no-margin blue\">New Employee</h2>"
				  + "<p>"
				  + "To create a new employee, click on the <i class=\"fa fa-user-plus\"></i> button. You then must fill in all the fields. "
				  + "Make sure the kuerzel and the e-mail address are correct. The new employee will get an e-mail with his user credentials "
				  + "and will be requested to change the auto generated password. "
				  + "</p>"
				  + "<br><br>"
				  //--------------------------------------------- My Profile ---------------------------------------------
				  + "<h2 id=\"myProfile\" class=\"no-margin blue\">My Profile</h2>"
				  + "<p>"
				  + "If you want to change your name, e-mail or password you can do so at My Profile. Click on the <i class=\"fa fa-user\"></i> "
				  + "button and you see your information and a section where you can enter a new password. "
				  + "</p>"
				  + "</div>"
				  + "</div>");
				  
		out.println("</section>"
				  + HTMLFooter.getInstance().printFooter(true)
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
