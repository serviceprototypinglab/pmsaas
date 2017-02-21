package ch.zhaw.init.walj.projectmanagement.admin.properties;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 */

// TODO implementation
@SuppressWarnings("serial")
@WebServlet("/admin/uploadLogo")
public class LogoUpload extends HttpServlet {
	
    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 9000 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file ;
    
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/html");
      PrintWriter out = response.getWriter( );
          
      boolean small = request.getParameter("size").equals("small");
      
      String message = "";
      
      if(!isMultipart){
    	  message = "<div class=\"row\">" 
  					+ "<div class=\"small-12 columns\">"
    				+ "<div class=\"row\">" 
				    + "<div class=\"callout alert\">" 
				    + "<h5>Upload failed</h5>"
				    + "</div></div>"
				    + "</div></div>";
      }
      
      DiskFileItemFactory factory = new DiskFileItemFactory();
      // maximum size that will be stored in memory
      factory.setSizeThreshold(maxMemSize);
      // Location to save data that is larger than maxMemSize.
      factory.setRepository(new File(this.getServletContext().getRealPath("/") + "img/"));

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      // maximum file size to be uploaded.
      upload.setSizeMax( maxFileSize );

      try{ 
	      // Parse the request to get file items.
	      List fileItems = upload.parseRequest(request);
		
	      // Process the uploaded file items
	      Iterator i = fileItems.iterator();

	      out.println("<html>");
	      out.println("<head>");
	      out.println("<title>Servlet upload</title>");  
	      out.println("</head>");
	      out.println("<body>");
	      
	      while (i.hasNext()){
	    	  
	         FileItem fi = (FileItem)i.next();
	         
	         if (!fi.isFormField()){
	            // Get the uploaded file parameters
	            String fieldName = fi.getFieldName();
	            String fileName;
	            if (small){
	            	fileName = "logo_small.png";
	            } else {
	            	fileName = "logo.png";
	            }
	            String contentType = fi.getContentType();
	            boolean isInMemory = fi.isInMemory();
	            long sizeInBytes = fi.getSize();
	            filePath = this.getServletContext().getRealPath("/") + "img/" + fileName;
	            // Write the file
	            file = new File(filePath) ;
	            fi.write( file ) ;
	            out.println("Uploaded Filename: " + fileName + "<br>");
	         }
	         
	      }
	      
	      out.println("</body>");
	      out.println("</html>");
	      
	   } catch(Exception ex) {
	       System.out.println(ex);
	   }
	   
	}
}	
