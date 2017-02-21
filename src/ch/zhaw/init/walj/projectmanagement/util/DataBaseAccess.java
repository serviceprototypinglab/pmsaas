package ch.zhaw.init.walj.projectmanagement.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataBaseAccess{
	
	private String url;
	private String dbname;
	private String username;
	private String password;
	private String helper[];
 	
	public DataBaseAccess(String path){
		try {
			// reads the url, the database name, username and password from the .config file 
			FileReader fr = new FileReader(path + ".config");
		    BufferedReader br = new BufferedReader(fr);
		    
		    url = br.readLine();
		    helper = url.split("=");
		    url = helper[1];
		    
		    dbname = (br.readLine());
		    helper = dbname.split("=");
		    dbname = helper[1];
		    
		    username = br.readLine();
		    helper = username.split("=");
		    username = helper[1];
		    
		    password = br.readLine();
		    helper = password.split("=");
		    password = helper[1];
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the url of the database
	 */
	public String getUrl(){
		return url;
	}

	/**
	 * @return the database name
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
}
