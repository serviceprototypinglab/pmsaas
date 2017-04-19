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

	private final Project project;
	private final String path;
	private ArrayList<Task> tasks = new ArrayList<>();
	
	/**
	 * Constructor of the LineChart class
	 * @param project the project for which the chart should be created
	 * @param path path where PMSaaS is located
	 */
	public LineChart(Project project, String path) {
		this.project = project;
    	tasks = project.getTasks();
		this.path = path;
	}
    
	/**
	 * creates a dataset with all data about planned and booked effort
	 * @return dataset with planned and booked PMs
	 * @throws SQLException
	 */
    private XYDataset createDataset() throws SQLException{
    	
    	// initialize variables
    	Effort effort = new Effort(tasks, path);
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
		Effort effort = new Effort(tasks, path);
    	double bookedEffort;
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
	 */
    public void createChart() {
    	
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
	    lineChart = new File(path + "/Charts/EffortProject" + project.getID() + "_large.jpg" );
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * creates a line chart with all booked hours of a specific employee
     * @param employeeID ID of the employee for which a chart should be created
     */
    public void createChart(int employeeID) {

    	// get dataset
    	XYSeriesCollection dataset;
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
	    File lineChart = new File(path + "/Charts/EffortProject" + project.getID() + "_Employee" + employeeID + ".jpg" );
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
