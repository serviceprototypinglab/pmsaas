package ch.zhaw.init.walj.projectmanagement.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import ch.zhaw.init.walj.projectmanagement.util.Employee;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;

public class PieChart {
	
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "projectmanagement";
	private String userName	= "Janine";
	private String password	= "test123";
	private Connection conn;
	private Statement st;
	private ResultSet res;
	private Statement st2;
	private ResultSet res2;
	
	private Project project;
	private ArrayList<ProjectTask> tasks = new ArrayList<ProjectTask>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	
	
	
	public PieChart(Project project) {
		this.project = project;
    	tasks = project.getTasks();
    	employees = project.getEmployees();
		
		
	}
    
    public double getUsedBudget() throws SQLException{
    	double usedBudget = 0;
    	int employeeID;    	
    	
    	try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			st2 = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	for (ProjectTask task : tasks){
    		res = st.executeQuery("select * from Assignments where TaskIDFS = " + task.getID() );
    		while (res.next()){
    			res2 = st2.executeQuery("select * from Bookings where AssignmentIDFS = " + res.getInt("AssignmentID"));
    			while (res2.next()){
    				employeeID = res.getInt("EmployeeIDFS");
    				for (Employee employee : employees){
    					if (employee.getID() == employeeID){
    						usedBudget += (employee.getWage() * res2.getDouble("Hours"));
    					}
    				}
    			}
    		}
    	}
    	
		res = st.executeQuery("select Costs from Expenses where ProjectIDFS = " + project.getID() );
    	while (res.next()){
    		usedBudget += res.getDouble("Costs");
    	}
    	conn.close();
    	return usedBudget;
    }
    
    
    public double getRemainingBudget() throws SQLException{
    	double remainingBudget = project.getBudget() - getUsedBudget();
    	return remainingBudget;
    }
    
    
    public void createChart(String path) throws NumberFormatException, SQLException, IOException{
	    DefaultPieDataset dataset = new DefaultPieDataset( );
		   	   
       dataset.setValue("remaining", getRemainingBudget());
       dataset.setValue("spent", getUsedBudget());
       JFreeChart chart = ChartFactory.createPieChart("", dataset, false, false, false );
       PiePlot plot = (PiePlot) chart.getPlot();
       plot.setSectionPaint("remaining", new Color(0, 101, 166));
       plot.setSectionPaint("spent", new Color(0, 62, 102));
       plot.setLabelGenerator(null);
       plot.setBackgroundPaint(Color.WHITE);
       plot.setShadowXOffset(0);
       plot.setShadowYOffset(0);
       plot.setOutlineVisible(false);
       plot.setInteriorGap(0);
       
       
       
       int width = 350; /* Width of the image */
       int height = 350; /* Height of the image */ 
       File pieChart = new File(path + "BudgetProject" + project.getID() + ".jpg" ); 
       ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
    }
    
    
    
    
}
