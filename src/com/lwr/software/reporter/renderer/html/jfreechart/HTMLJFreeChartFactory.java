package com.lwr.software.reporter.renderer.html.jfreechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYInterval;
import org.jfree.ui.RectangleEdge;

import com.lwr.software.reporter.DashboardConstants;

public class HTMLJFreeChartFactory {

	public static JFreeChart getChart(String type,String rowLabel, String columnLabel,Dataset dataset){
		JFreeChart chart = null;
		if(dataset instanceof TimeSeriesCollection && type.equalsIgnoreCase(DashboardConstants.LINE_CHART_TYPE)){
			chart = ChartFactory.createTimeSeriesChart(
					null, 
					rowLabel, 
					columnLabel, 
					(XYDataset)dataset,
					true,
					false,
					false);
		}else if(dataset instanceof TimeSeriesCollection && type.equalsIgnoreCase(DashboardConstants.BAR_CHART_TYPE)){
			chart = ChartFactory.createXYBarChart(
					null, 
					rowLabel, 
					true,
					columnLabel, 
					(IntervalXYDataset)dataset,
					PlotOrientation.VERTICAL,
					true,
					false,
					false);
			chart.getXYPlot().setRenderer(new ClusteredXYBarRenderer());
		}else if(dataset instanceof TimeSeriesCollection && type.equalsIgnoreCase(DashboardConstants.COLUMN_CHART_TYPE)){
			chart = ChartFactory.createXYBarChart(
					null, 
					rowLabel, 
					true,
					columnLabel, 
					(IntervalXYDataset)dataset,
					PlotOrientation.HORIZONTAL,
					true,
					false,
					false);
			chart.getXYPlot().setRenderer(new ClusteredXYBarRenderer());
		}else if(dataset instanceof TimeSeriesCollection&& type.equalsIgnoreCase(DashboardConstants.BAR_STACK_CHART_TYPE)){ 
			chart = ChartFactory.createXYBarChart(
					null, 
					rowLabel, 
					true,
					columnLabel, 
					(IntervalXYDataset)dataset,
					PlotOrientation.VERTICAL,
					true,
					false,
					false);
		} else if(dataset instanceof TimeSeriesCollection && type.equalsIgnoreCase(DashboardConstants.COLUMN_STACK_CHART_TYPE)){
			chart = ChartFactory.createXYBarChart(
					null, 
					rowLabel, 
					true,
					columnLabel, 
					(IntervalXYDataset)dataset,
					PlotOrientation.HORIZONTAL,
					true,
					false,
					false);
		} else if(type.equalsIgnoreCase(DashboardConstants.BAR_CHART_TYPE)){
			chart = ChartFactory.createBarChart(null, 
					rowLabel, 
					columnLabel, 
					(CategoryDataset)dataset,
					PlotOrientation.VERTICAL,
					true,
					false,
					false);
		}else if(type.equalsIgnoreCase(DashboardConstants.BAR_STACK_CHART_TYPE)){
			chart = ChartFactory.createStackedBarChart(null,
					rowLabel, 
					columnLabel, 
					(CategoryDataset)dataset,
					PlotOrientation.VERTICAL,
					true,
					false,
					false);
		}else if(type.equalsIgnoreCase(DashboardConstants.COLUMN_CHART_TYPE)){
			chart = ChartFactory.createBarChart(null, 
					rowLabel, 
					columnLabel, 
					(CategoryDataset)dataset,
					PlotOrientation.HORIZONTAL,
					true,
					false,
					false);
		}else if(type.equalsIgnoreCase(DashboardConstants.COLUMN_STACK_CHART_TYPE)){
			chart = ChartFactory.createStackedBarChart(null,
					rowLabel, 
					columnLabel, 
					(CategoryDataset)dataset,
					PlotOrientation.HORIZONTAL,
					true,
					false,
					false);
		}else if(type.equalsIgnoreCase(DashboardConstants.PIE_CHART_TYPE)){
			chart = ChartFactory.createPieChart(
					null, 
					(PieDataset)dataset,
					true,
					false,
					false  
					);
		}else if(type.equalsIgnoreCase(DashboardConstants.LINE_CHART_TYPE)){
			chart = ChartFactory.createLineChart(null,
					rowLabel, 
					columnLabel, 
					(CategoryDataset)dataset,
					PlotOrientation.VERTICAL,
					true,
					false,
					false);
		}
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setPosition(RectangleEdge.BOTTOM);
		chart.getPlot().setBackgroundPaint(Color.white);
		chart.getPlot().setOutlineVisible(false);
		chart.setBorderVisible(false);
		
		Font font = new Font("Arial", Font.PLAIN, 12);
		chart.getLegend().setItemFont(font);

		Plot plot = chart.getPlot();
		plot.setNoDataMessage("No Data - Not Supported");
		if(plot instanceof XYPlot){
			XYPlot xyplot = (XYPlot)plot;
			xyplot.getDomainAxis().setLabelFont(font);
			xyplot.getDomainAxis().setAxisLinePaint(Color.BLACK);
			xyplot.getDomainAxis().setTickLabelFont(font);
			xyplot.getRangeAxis().setLabelFont(font);
			xyplot.getRangeAxis().setAutoRange(true);
			xyplot.getRangeAxis().setAxisLinePaint(Color.BLACK);
			xyplot.getRangeAxis().setTickLabelFont(font);
//			xyplot.setDomainGridlinesVisible(true);
//			xyplot.setRangeGridlinesVisible(true);
//			xyplot.setRangeGridlinePaint(Color.black);
//			xyplot.setDomainGridlinePaint(Color.black);
			if(xyplot.getRenderer() instanceof XYBarRenderer){
				XYBarRenderer barRenderer = (XYBarRenderer) xyplot.getRenderer();
				barRenderer.setDrawBarOutline(false);
				XYBarPainter painter = new XYBarPainter() {
					@Override
					public void paintBarShadow(Graphics2D g2, XYBarRenderer renderer, int row, int column, RectangularShape bar, RectangleEdge base, boolean pegShadow) {
					}
					@Override
					public void paintBar(Graphics2D g2, XYBarRenderer renderer, int row, int column, RectangularShape bar, RectangleEdge base) {
						bar.setFrame(bar.getX(), bar.getY(), bar.getWidth() + 8, bar.getHeight());
						if(row == 0){
							g2.setColor(new Color(51, 102, 204));
						}else if(row == 1){
							g2.setColor(new Color(255, 153, 0));
						}else if(row == 2){
							g2.setColor(new Color(16, 150, 24));
						}else if(row == 3){
							g2.setColor(new Color(220, 57, 18));
						}
						g2.fill(bar);
					}
				};
				barRenderer.setBarPainter(painter);
			}
			if(xyplot.getRenderer() instanceof XYLineAndShapeRenderer){
				xyplot.setRenderer(new XYSplineRenderer());
				XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
				BasicStroke wideLine = new BasicStroke( 2.0f ); 
				renderer.setSeriesStroke(0, wideLine);
				renderer.setSeriesStroke(1, wideLine); 
				renderer.setSeriesStroke(2, wideLine); 
				renderer.setSeriesStroke(3, wideLine); 
				renderer.setSeriesPaint(0, new Color(51, 102, 204));
				renderer.setSeriesPaint(1, new Color(220, 57, 18));
				renderer.setSeriesPaint(2, new Color(16, 150, 24));
				renderer.setSeriesPaint(3, new Color(255, 153, 0));
			}
		}else if(plot instanceof CategoryPlot){
			CategoryPlot catPlot = (CategoryPlot)plot;
			catPlot.getDomainAxis().setLabelFont(font);
			catPlot.getDomainAxis().setAxisLinePaint(Color.BLACK);
			catPlot.getDomainAxis().setTickLabelFont(font);
			catPlot.getRangeAxis().setLabelFont(font);
			catPlot.getRangeAxis().setAxisLinePaint(Color.BLACK);
			catPlot.getRangeAxis().setTickLabelFont(font);
			
			CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
			renderer.setBaseItemLabelFont(font);
			renderer.setSeriesPaint(0, new Color(51, 102, 204));
			renderer.setSeriesPaint(1, new Color(255, 153, 0));
			renderer.setSeriesPaint(2, new Color(16, 150, 24));
			renderer.setSeriesPaint(3, new Color(220, 57, 18));
			
			if(catPlot.getRenderer() instanceof BarRenderer){
				((BarRenderer) catPlot.getRenderer()).setBarPainter(new StandardBarPainter());	
			}

		}else if (plot instanceof PiePlot){
			PiePlot piePlot = (PiePlot)plot;
			piePlot.setSectionPaint(0, new Color(51, 102, 204));
			piePlot.setSectionPaint(1, new Color(255, 153, 0));
			piePlot.setSectionPaint(2, new Color(16, 150, 24));
			piePlot.setSectionPaint(3, new Color(220, 57, 18));
		}
		return chart;
	}
}

