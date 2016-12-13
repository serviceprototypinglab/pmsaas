package ch.zhaw.init.walj.projectmanagement.util.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Workpackage;

/**
 * creates a gantt chart with data from a given project
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class GanttChart {

	// variable declaration
	private Project project;
	private ArrayList<Workpackage> workpackages;
	private ArrayList<ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task> tasks;
	
	private int nbrOfObjects;
	
	/**
	 * Constructor of the GanttChart class
	 * @param project the project for which the chart should be created
	 */
	public GanttChart(Project project) {
		this.project = project;
		workpackages = project.getWorkpackages();
    	tasks = project.getTasks();	
    	nbrOfObjects = 0;
	}
    
	/**
	 * create a dataset with all tasks and workpackages
	 * @return dataset with dates from tasks and workpackages
	 * @throws SQLException
	 * @throws ParseException
	 */
    private IntervalCategoryDataset createDataset() throws SQLException, ParseException{
    	// create new task series
    	 TaskSeries taskSeries = new TaskSeries("Scheduled");
    	 
    	 // add a new task to the task series for every workpackage and every project task
    	 for (Workpackage w : workpackages){
	         taskSeries.add(new org.jfree.data.gantt.Task(w.getName(), new SimpleTimePeriod(w.getStartAsDate(), w.getEndAsDate())));
	         nbrOfObjects++;
	         for (ch.zhaw.init.walj.projectmanagement.util.dbclasses.Task t : tasks){
	        	 if (t.getWorkpackageID() == w.getID()){
	    	         taskSeries.add(new org.jfree.data.gantt.Task(t.getName(), new SimpleTimePeriod(t.getStartAsDate(), t.getEndAsDate())));	  
	    	         nbrOfObjects++;  
	        	 }
	         }
    	 }
    	 
    	 // add task series to collection
    	 TaskSeriesCollection collection = new TaskSeriesCollection();
    	 collection.add(taskSeries);
    	 return collection;
    }
     
    /**
     * creates a gantt chart with all workpackages and tasks
     * @param path place where the picture should be saved
     * @throws NumberFormatException
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public void createChart(String path) throws NumberFormatException, SQLException, IOException, ParseException{
    	
    	// get data
    	IntervalCategoryDataset dataset = createDataset();
    	
    	// create chart
    	JFreeChart chart = ChartFactory.createGanttChart(
    	         "", 		// chart title
    	         "",  		// domain axis label
    	         "",		// range axis label
    	         dataset,	// data
    	         false, 	// include legend
    	         false,		// tooltips
    	         false		// urls
    	);
    	
    	// set white background
        chart.setBackgroundPaint(new Color(255, 255, 255));	    
        
        // set color of bars
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 62, 102)); 
	    
        // set size of the chart
	    int width = 1200;
	    int height = (40 * nbrOfObjects) + 200;
	    
	    // save picture as JPEG
	    File gantChart = new File(path + "GanttProject" + project.getID() + ".jpg" ); 
	    ChartUtilities.saveChartAsJPEG( gantChart , chart , width , height );
    }    
    
}
