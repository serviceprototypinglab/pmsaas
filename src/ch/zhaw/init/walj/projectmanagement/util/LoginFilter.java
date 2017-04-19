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

package ch.zhaw.init.walj.projectmanagement.util;

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
 * checks if a user is logged in
 * if not redirects to login page
 * 
 * @author Janine Walther, ZHAW
 *
 */
@WebFilter("/Projects/*")
public class LoginFilter implements Filter {

	private FilterConfig filterConfig = null;

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;		
	}
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {    
    	
    	if (filterConfig == null) {
    		return;
    	}
    	
    	// variable initialization
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/login";
        String adminURI = request.getContextPath() + "/admin/properties";

        // check if the user is logged in
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        // check if the user is logged in as admin
        boolean loggedInAsAdmin = session != null && session.getAttribute("kuerzel").equals("admin");
        // check if the request URI is the same as the login URI
        boolean loginRequest = request.getRequestURI().equals(loginURI);

        // redirect to admin/login page or show the requested page
        if (loggedIn || loginRequest) {
        	if (loggedInAsAdmin){
        		response.sendRedirect(adminURI);
        	} else {
        		chain.doFilter(request, response);
        	}
        } else {
            response.sendRedirect(loginURI);
        }
    }


}
