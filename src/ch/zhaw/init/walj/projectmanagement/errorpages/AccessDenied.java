/*
 	Copyright 2016-2017 Zuercher Hochschule fuer Angewandte Wissenschaften
 	All Rights Reserved.

   Licensed under the Apache License, Version 2.0 (the "License"); you may
   not use this file except in compliance with the License. You may obtain
   a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
   License for the specific language governing permissions and limitations
   under the License.
 */

package ch.zhaw.init.walj.projectmanagement.errorpages;

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
 * Projectmanagement tool, Access Denied page
 *
 * @author Janine Walther, ZHAW
 *
 */
@SuppressWarnings("serial")
@WebServlet("/AccessDenied")
public class AccessDenied extends HttpServlet {

	/*
	 * 	User sees this page if he is not allowed to see the requested page
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepare response
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();

		// print HTML
		out.println(HTMLHeader.getInstance().printHeader("Access denied - Project Management SaaS", "", "Access denied", "")
				+ "<body>"
				+ "<div id=\"wrapper\">"
				+ "<section>"
				+ "<div class=\"row\">"
				+ "<div class=\"small-6 small-offset-3 end columns\">"
				+ "<h1 class=\"align-center\"><i class=\"fa fa-exclamation-triangle fa-3x\"></i></h1>"
				+ "<h1 class=\"align-center\">Access denied</h1>"
				+ "</div>"
				+ "</div>"
				+ "<div class=\"row\">"
				+ "<div class=\"small-12 columns text-center\">"
				+ "<p>You don't have the permission to access this site</p>"
				+ "<a href=\"/Projects/Overview\">Click here to go back to the overview</a>"
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
}
