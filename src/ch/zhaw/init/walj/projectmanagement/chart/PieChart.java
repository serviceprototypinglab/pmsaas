package ch.zhaw.init.walj.projectmanagement.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import ch.zhaw.init.walj.projectmanagement.util.dbclasses.Project;

public class PieChart {
		
	private Project project;
		
	public PieChart(Project project) {
		this.project = project;		
	}
    
    public void createChart(String path) throws NumberFormatException, SQLException, IOException{
	    DefaultPieDataset dataset = new DefaultPieDataset( );
		   	   
       dataset.setValue("spent", project.getUsedBudget());
       dataset.setValue("remaining", project.getRemainingBudget());
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
