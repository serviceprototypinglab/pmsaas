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

package ch.zhaw.init.walj.projectmanagement.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * project management tool, 
 * webfilter for /admin/*
 * sends users without permission (not admin) to access denied page
 * 
 * @author Janine Walther, ZHAW
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

	private FilterConfig filterConfig = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	@Override
	public void destroy() {
	   this.filterConfig = null;
	}
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {    
        
    	if (filterConfig == null) {
    		return;
    	}
    	
    	// get request, response and session
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        
        // set login URI
        String loginURI = request.getContextPath() + "/login";

        // check if user is logged in and if the user is the admin
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loggedInAsAdmin = session != null && session.getAttribute("kuerzel").equals("admin");

        if (loggedIn) {
        	if (loggedInAsAdmin){
        		// everything ok, send user to requested page
        		chain.doFilter(request, response);
        	} else {
        		// user is logged in but not as admin, send to access denied page
        		String url = request.getContextPath() + "/AccessDenied";
                response.sendRedirect(url);
        	}
        } else {
        	// user is not logged in, send him to login page
            response.sendRedirect(loginURI);
        }
    }
}
