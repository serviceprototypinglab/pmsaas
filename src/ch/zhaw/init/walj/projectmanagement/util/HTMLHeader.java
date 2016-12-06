package ch.zhaw.init.walj.projectmanagement.util;

public final class HTMLHeader {
	  private static HTMLHeader instance;
		  
	  public String printHeader(String tabTitle, String path){
		  return printHeader(tabTitle, path, "");
	  }
	  	  
	  public String printHeader(String tabTitle, String path, String title, String script){
		  return printHeader(tabTitle, path, title, script, "");
	  }
	  
	  public String printHeader(String tabTitle, String path, String script){
		  String header = "<!DOCTYPE html>"
				  + "<html>"
				  // HTML head
				  + "<head>"
				  + "<meta charset=\"UTF-8\">"
				  + "<title>" + tabTitle + "</title>"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "css/font-awesome/css/font-awesome.min.css\">"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "css/foundation.css\" />"
				  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "css/style.css\" />"
				  + script
				  + "</head>";
	    return header;
	  }
	  	  
	  public String printHeader(String tabTitle, String path, String title, String script, String link){
		    String header = printHeader(tabTitle, path, script);
		    header += "<body>"
				    + "<div id=\"wrapper\">"
				    + "<header>"
				    + "<div class=\"row\">"
				    + "<div class=\"small-7 columns\">"
				    + "<a href=\"/Projektverwaltung/Projects/Overview\" title=\"Home\"><img src=\"" + path + "img/logo_small.png\" class=\"small-img left\"></a>"
				    + "<h1>" + title + "</h1>"
				    + link
				    + "</div>"
				    + "<div class=\"small-12 medium-5 columns\">"
				    + "<div class=\"float-right menu\">"
				    + "<a href=\"/Projektverwaltung/Projects/Overview\" class=\"button\" title=\"All Projects\"><i class=\"fa fa-list fa-fw\"></i></a> "
				    + "<a href=\"/Projektverwaltung/Projects/newProject\" class=\"button\" title=\"New Project\"><i class=\"fa fa-file fa-fw\"></i></a> "
				    + "<a href=\"/Projektverwaltung/Projects/newEmployee\" class=\"button\" title=\"New Employee\"><i class=\"fa fa-user-plus fa-fw\"></i></a> "
				    + "<a href=\"/Projektverwaltung/Projects/employee\" class=\"button\" title=\"My Profile\"><i class=\"fa fa-user fa-fw\"></i></a> "
				    + "<a href=\"/Projektverwaltung/Projects/help\" class=\"button\" title=\"Help\"><i class=\"fa fa-book fa-fw\"></i></a> "
				    + "<a href=\"/Projektverwaltung/Projects/properties\" class=\"button\" title=\"Properties\"><i class=\"fa fa-cog fa-fw\"></i></a> "
				    + "<a href=\"/Projektverwaltung/Projects/logout\" class=\"button\" title=\"Logout\"><i class=\"fa fa-sign-out fa-fw\"></i></a> "
				    + "</div>"
				    + "</div>"
				    + "</header>";
		    return header;
	  }
	  
	  public static synchronized HTMLHeader getInstance(){
		    if(instance == null){
		       instance = new HTMLHeader(); 
		    } 
		    return instance;
	  }
}