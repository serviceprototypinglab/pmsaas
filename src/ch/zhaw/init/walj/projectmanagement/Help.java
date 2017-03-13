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
				  + "<div class=\"small-12 columns text-center\">"
				  + "<h2>When the last light of the Durins Day shines, this page will be avaible</h2>"
				  + "<a href=\"/Projektverwaltung/Projects/Overview\">Click here to go back to the overview</a>"
				  + "</div>"
				  + "</div>");
				  
		out.println("</section>"
				  + HTMLFooter.getInstance().printFooter(false)
				  + "</div>"
				  + "<script src=\"../js/vendor/jquery.js\"></script>"
				  + "<script src=\"../js/vendor/foundation.min.js\"></script>"
				  + "<script>$(document).foundation();</script>"
				  + "</body>"
				  + "</html>");
	}
	
}
