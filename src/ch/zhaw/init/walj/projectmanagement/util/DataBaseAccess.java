package ch.zhaw.init.walj.projectmanagement.util;

public class DataBaseAccess {

	private static DataBaseAccess instance;
	
	private static String URL = "jdbc:mysql://localhost:3306/";
	private static String DBNAME = "projectmanagement";
	private static String USERNAME = "Janine";
	private static String PASSWORD = "test123";
	private static String DRIVER = "com.mysql.jdbc.Driver";
	
	public static synchronized DataBaseAccess getInstance(){
		  
	    if(instance == null){
	       instance = new DataBaseAccess();
	    } 
	    return instance;
	}
	
	
	public String getURL(){
		return URL;
	}
	
	public String getDBName(){
		return DBNAME;
	}
	
	public String getUsername(){
		return USERNAME;
	}
	
	public String getPassword(){
		return PASSWORD;
	}
	
	public String getDriver(){
		return DRIVER;
	}	
}
