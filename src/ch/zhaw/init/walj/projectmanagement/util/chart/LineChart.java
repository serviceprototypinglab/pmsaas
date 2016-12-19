package ch.zhaw.init.walj.projectmanagement.util.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task;

/**
 * creates a line chart with data from a given project
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class LineChart {

	private Project project;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	/**
	 * Constructor of the LineChart class
	 * @param project the project for which the chart should be created
	 */
	public LineChart(Project project) {
		this.project = project;
    	tasks = project.getTasks();
			
	}
    
	/**
	 * creates a dataset with all data about planned and booked effort
	 * @return dataset with planned and booked PMs
	 * @throws SQLException
	 */
    private XYDataset createDataset() throws SQLException{
    	
    	// initialize variables
    	Effort effort = new Effort(tasks);
    	double plannedEffort = 0;
    	double bookedEffort = 0;
    	XYSeries planned = new XYSeries("Planned");
    	XYSeries booked = new XYSeries("Booked");
    	int projectMonths = project.getNumberOfMonths();

    	// get planned and booked effort for every month
    	for (double i = 1; i <= projectMonths; i++){
        	effort.getPlannedEffort(i);
    		plannedEffort += effort.getPlannedEffort(i);
    		planned.add(i, plannedEffort);
    		
    		if (effort.getBookedEffort(i) != 0) {
	    		bookedEffort += effort.getBookedEffort(i);
	    		booked.add(i, bookedEffort);
    		}
    			
    	}
    	
    	// add booked and planned effort to dataset
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	dataset.addSeries(booked);
        dataset.addSeries(planned);
        
    	return dataset;
    }
    
    /**
     * creates a dataset with the booked effort (in hours) of a specific employee
     * @param employeeID ID of an employee 
     * @return dataset with booked hours
     */
	private XYSeriesCollection createDataset(int employeeID){

    	// initialize variables
		Effort effort = new Effort(tasks);
    	double bookedEffort = 0;
    	XYSeries booked = new XYSeries("Booked");
    	int projectMonths = project.getNumberOfMonths();

    	// get booked effort for every month
    	for (double i = 1; i <= projectMonths; i++){
    	
    		if (effort.getBookedEffortPerMonth(i, employeeID) != 0){
	    		bookedEffort = effort.getBookedEffortPerMonth(i, employeeID);
	    		booked.add(i, bookedEffort);
    		}
    			
    	}

    	// add booked effort to dataset
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	dataset.addSeries(booked);
        
    	return dataset;
	}
        
	/**
     * creates a line chart with all booked and planned PMs
     * @param path place where the picture should be saved
	 */
    public void createChart(String path) {
    	
    	// get dataset
    	XYSeriesCollection dataset = null;
		try {
			dataset = (XYSeriesCollection) createDataset();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		// create line chart
    	JFreeChart xylineChart = ChartFactory.createXYLineChart(
    	         "",
    	         "Month" ,
    	         "PM" ,
    	         dataset ,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
    	
    	// set color of chart
	    XYPlot plot = xylineChart.getXYPlot( );
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    renderer.setSeriesPaint( 0 , new Color(0, 101, 166) );
	    renderer.setSeriesPaint( 1 , new Color(0, 62, 102) );
	    plot.setRenderer(renderer); 
	    
	    // set size of the chart and save it as small JPEG for project overview
	    int width = 600; 
	    int height = 400;
	    File lineChart = new File(path + "EffortProject" + project.getID() + ".jpg" ); 
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}

	    // set size of the chart and save it as large JPEG for effort detail page
	    width = 1200;
	    height = 600; 
	    lineChart = new File(path + "EffortProject" + project.getID() + "_large.jpg" ); 
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * creates a line chart with all booked hours of a specific employee
     * @param path place where the picture should be saved
     * @param employeeID 
     */
    public void createChart(String path, int employeeID) {

    	// get dataset
    	XYSeriesCollection dataset = null;
    	dataset = createDataset(employeeID);
    	
    	// create chart
    	JFreeChart xylineChart = ChartFactory.createXYLineChart(
    	         "",
    	         "Month" ,
    	         "Hours" ,
    	         dataset ,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
    	
    	// set color of chart
	    XYPlot plot = xylineChart.getXYPlot( );
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    renderer.setSeriesPaint( 0 , new Color(0, 101, 166) );
	    renderer.setSeriesPaint( 1 , new Color(0, 62, 102) );
	    plot.setRenderer(renderer); 
	    

	    // set size of the chart and save it as JPEG
	    int width = 1200;
	    int height = 600; 
	    File lineChart = new File(path + "EffortProject" + project.getID() + "_Employee" + employeeID + ".jpg" ); 
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
