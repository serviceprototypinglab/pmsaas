package ch.zhaw.walj.projectmanagement.chart;

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
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import ch.zhaw.walj.projectmanagement.Project;
import ch.zhaw.walj.projectmanagement.ProjectTask;
import ch.zhaw.walj.projectmanagement.Workpackage;

public class GanttChart {

	private Project project;
	private ArrayList<Workpackage> workpackages = new ArrayList<Workpackage>();
	private ArrayList<ProjectTask> tasks = new ArrayList<ProjectTask>();
	
	private int nbrOfObjects = 0;
	
	public GanttChart(Project project) {
		this.project = project;
		workpackages = project.getWorkpackages();
    	tasks = project.getTasks();		
	}
    
    private IntervalCategoryDataset createDataset() throws SQLException, ParseException{
    	 TaskSeries s1 = new TaskSeries("Scheduled");
    	 for (Workpackage w : workpackages){
	         s1.add(new Task(w.getName(), new SimpleTimePeriod(w.getStartAsDate(), w.getEndAsDate())));
	         nbrOfObjects++;
	         for (ProjectTask t : tasks){
	        	 if (t.getWorkpackageID() == w.getID()){
	    	         s1.add(new Task(w.getName(), new SimpleTimePeriod(t.getStartAsDate(), t.getEndAsDate())));	  
	    	         nbrOfObjects++;  
	        	 }
	         }
    	 }
    	 
    	 TaskSeriesCollection collection = new TaskSeriesCollection();
    	 collection.add(s1);
    	 return collection;
    }
        
    public void createChart(String path) throws NumberFormatException, SQLException, IOException, ParseException{
    	
    	IntervalCategoryDataset dataset = createDataset();
    	
    	JFreeChart chart = ChartFactory.createGanttChart(
    	         "", 		// chart title
    	         "",  		// domain axis label
    	         "",		// range axis label
    	         dataset,	// data
    	         false, 	// include legend
    	         false,		// tooltips
    	         false		// urls
    	);
    	
        chart.setBackgroundPaint(new Color(255, 255, 255));	    
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 101, 166)); 
	    
	    int width = 1200; /* Width of the image */
	    int height = (40 * nbrOfObjects) + 200; /* Height of the image */ 
	    File gantChart = new File(path + "GanttProject" + project.getID() + ".jpg" ); 
	    ChartUtilities.saveChartAsJPEG( gantChart , chart , width , height );
    }    
    
}
