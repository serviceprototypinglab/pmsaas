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
				  		
		  footer += "<p>ProjectmanagementSaaS</p>"
		  		  + "</div>"
				  + "</div>"
				  + "</footer>";
		  
		  return footer;
	  }
	  
	  /**
	   * @return instance of HTMLFooter class
	   */
	  public static synchronized HTMLFooter getInstance(){
		    if(instance == null){
		       instance = new HTMLFooter(); 
		    } 
		    return instance;
	  }
}