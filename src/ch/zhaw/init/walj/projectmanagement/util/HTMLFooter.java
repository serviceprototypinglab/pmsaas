package ch.zhaw.init.walj.projectmanagement.util;

public final class HTMLFooter {
	  private static HTMLFooter instance;
		  
	  public String printFooter(boolean linkToTop){
		  
		  String footer = "<footer>"
				  		+ "<div class=\"row\">"
				  		+ "<div class=\"large-12 columns\">";
		  if (linkToTop){
			  footer += "<span class=\"float-right\">"
			  		  + "<a href=\"#\"><i class=\"fa fa-chevron-up\"></i> to the top</a>"
			  		  + "</span>";
		  }
				  		
		  footer += "<p>ProjectmanagementSaaS</p>"
		  		  + "</div>"
				  + "</div>"
				  + "</footer>";
		  
		  return footer;
	  }
	  
	  public static synchronized HTMLFooter getInstance(){
		    if(instance == null){
		       instance = new HTMLFooter(); 
		    } 
		    return instance;
	  }
}