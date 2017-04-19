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

/**
 * Gives a string with the footer back.
 * Content of the footer depends on the parameters.
 * 
 * @author Janine Walther, ZHAW
 *
 */
public final class HTMLFooter {

	  private static HTMLFooter instance;
		  
	  /**
	   * gives a string with the footer back
	   * @param linkToTop true if there should be a link to the top of the page
	   * @return a string with the footer
	   */
	  public String printFooter(boolean linkToTop){
		  
		  String footer = "<footer>"
				  		+ "<div class=\"row\">"
				  		+ "<div class=\"large-12 columns\">";
		  if (linkToTop){
			  footer += "<span class=\"float-right\">"
			  		  + "<a href=\"#\"><i class=\"fa fa-chevron-up\"></i> to the top</a>"
			  		  + "</span>";
		  }
				  		
		  footer += "<p class=\"text-center\">Project Management SaaS | 2016 - 2017 | Zuercher Hochschule fuer Angewandte Wissenschaften | Janine Walther</p>"
		  		  + "</div>"
				  + "</div>"
				  + "</footer>";
		  
		  return footer;
	  }
	  
	  /**
	   * @return instance of HTMLFooter class
	   */
	  public static HTMLFooter getInstance(){
		    if(instance == null){
		       instance = new HTMLFooter(); 
		    } 
		    return instance;
	  }
}