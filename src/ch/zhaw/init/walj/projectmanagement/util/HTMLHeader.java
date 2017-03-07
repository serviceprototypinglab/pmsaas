package ch.zhaw.init.walj.projectmanagement.util;

/**
 * Gives a string with the header back.
 * Content of the header depends on the parameters.
 * 
 * @author Janine Walther, ZHAW
 *
 */
public final class HTMLHeader {
	  private static HTMLHeader instance;
		  
	  /**
	   * prints a simple header without navigation
	   * @param tabTitle Title of the tab
	   * @param path path for css, etc. (Example: "../../")
	   * @return string with the header
	   */
	  public String printHeader(String tabTitle, String path){
		  return printHeader(tabTitle, path, "");
	  }
	  	  
	  /**
	   * prints a header with navigation, without a link next to the title
	   * @param tabTitle Title of the tab
	   * @param path path for CSS, etc. (Example: "../../")
	   * @param title the title of the page
	   * @param script JavaScript code
	   * @return string with the header
	   */
	  public String printHeader(String tabTitle, String path, String title, String script){
		  return printHeader(tabTitle, path, title, script, "", false);
	  }
	  
	  /**
	   * prints a header without navigation, but with JavaScript code
	   * @param tabTitle Title of the tab
	   * @param path path for CSS, etc. (Example: "../../")
	   * @param script JavaScript code
	   * @return string with the header
	   */
	  public String printHeader(String tabTitle, String path, String script){
		  String header = "<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>PMSaaS - " + tabTitle + "</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "css/font-awesome/css/font-awesome.min.css\">"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "css/style.css\" />"
				  + script
				  + "</head>";
	    return header;
	  }
	  	  
	  /**
	   * prints a header with navigation, title and link next to the title
	   * @param tabTitle Title of the tab
	   * @param path path for CSS, etc. (Example: "../../")
	   * @param title title of the page 
	   * @param script JavaScript code
	   * @param link link shown next to the title
	   * @param admin set true if admin is logged in
	   * @return string with the header
	   */
	  public String printHeader(String tabTitle, String path, String title, String script, String link, boolean admin){
		    String header = printHeader(tabTitle, path, script);
		    header += "<body>"
				    + "<div id=\"wrapper\">"
				    + "<header>"
				    + "<div class=\"row\">"
				    + "<div class=\"small-1 columns logo\">"
				    + "<a href=\"/Projektverwaltung/Projects/Overview\" title=\"Home\"><img src=\"" + path + "img/logo_small.png\" class=\"small-img\"></a>"
		    		+ "</div>"
				    + "<div class=\"small-6 columns\">"
				    + "<h1>" + title + "</h1>"
				    + link
				    + "</div>"
				    + "<div class=\"small-12 medium-5 columns\">"
				    + "<div class=\"float-right menu\">";
		    
		    if (!admin){
				header += "<a href=\"/Projektverwaltung/Projects/Overview\" title=\"All Projects\"><i class=\"fa fa-list fa-fw fa-lg\"></i></a> "
					    + "<a href=\"/Projektverwaltung/Projects/newProject\" title=\"New Project\"><i class=\"fa fa-file fa-fw fa-lg\"></i></a> "
					    + "<a href=\"/Projektverwaltung/Projects/newEmployee\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw fa-lg\"></i></a> "
					    + "<a href=\"/Projektverwaltung/Projects/employee\" title=\"My Profile\"><i class=\"fa fa-user fa-fw fa-lg\"></i></a> ";
					 // + "<a href=\"/Projektverwaltung/Projects/properties\" title=\"Properties\"><i class=\"fa fa-cog fa-fw fa-lg\"></i></a> "
				     // + "<a href=\"/Projektverwaltung/Projects/help\" title=\"Help\"><i class=\"fa fa-book fa-fw fa-lg\"></i></a> "
		    } else {
		    	
		    }
			header += "<a href=\"/Projektverwaltung/logout\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw fa-lg\"></i></a> "
				    + "</div>"
				    + "</div>"
				    + "</header>";
		    return header;
	  }
	  
	  /**
	   * @return instance of HTMLHeader class
	   */
	  public static synchronized HTMLHeader getInstance(){
		    if(instance == null){
		       instance = new HTMLHeader(); 
		    } 
		    return instance;
	  }
}