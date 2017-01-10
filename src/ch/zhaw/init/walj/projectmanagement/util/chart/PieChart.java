package ch.zhaw.init.walj.projectmanagement.util.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import ch.zhaw.init.walj.projectmanagement.util.DBConnection;
import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

/**
 * creates a pie chart with data from a given project
 * 
 * @author Janine Walther, ZHAW
 *
 */
public class PieChart {
		
	private Project project;
	private DBConnection con;
		
	/**
	 * Constructor of the PieChart class
	 * @param project the project for which the chart should be created
	 */
	public PieChart(Project project, String path) {
		this.project = project;		
		con = new DBConnection(path);
	}
    
	/**
	 * creates the pie chart
	 * @param path place where the picture should be saved
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws IOException
	 */
    public void createChart(String path) throws NumberFormatException, SQLException, IOException{
    	
    	// create dataset with used and remaining budget
    	DefaultPieDataset dataset = new DefaultPieDataset( );
    	dataset.setValue("spent", con.getUsedBudget(project));
    	dataset.setValue("remaining", con.getRemainingBudget(project));
    	
    	// create chart
    	JFreeChart chart = ChartFactory.createPieChart("", dataset, false, false, false );
    	
    	// set color and style
    	PiePlot plot = (PiePlot) chart.getPlot();
    	plot.setSectionPaint("remaining", new Color(0, 101, 166));
    	plot.setSectionPaint("spent", new Color(0, 62, 102));
    	plot.setLabelGenerator(null);
    	plot.setBackgroundPaint(Color.WHITE);
    	plot.setShadowXOffset(0);
    	plot.setShadowYOffset(0);
    	plot.setOutlineVisible(false);
    	plot.setInteriorGap(0);       
    	
    	// set size and save it as JPEG
    	int width = 350; /* Width of the image */
    	int height = 350; /* Height of the image */ 
    	File pieChart = new File(path + "BudgetProject" + project.getID() + ".jpg" ); 
    	ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
    }    
}
