package com.lwr.software.reporter.renderer.html.jfreechart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;

public abstract class HTMLJFreeAbstract implements IElementRenderer{

	protected Element element;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
	
	protected Map<Integer,String> headerIndexToTypeMap = new HashMap<Integer,String>();
	
	static{
		File tempDir = new File(DashboardConstants.TEMP_PATH);
		tempDir.mkdirs();
	}
	
	public HTMLJFreeAbstract(Element element){
		this.element=element;
		initHeaderLookup();
	}
	
	public String render() {
		try {
			File file = getImage();
			if(file == null)
				return "No Data";
			BufferedImage image = ImageIO.read(file);
			byte[] src = ChartUtilities.encodeAsPNG(image);
			String encoded = Base64.encodeBase64String(src);
			String html = "<img class=\"jfreeimage\" src=\"data:image/png;base64,"+encoded+"\"></img>";
			return html;
		} catch (IOException e) {
			e.printStackTrace();
			return "No Data";
		}
	}

	private void initHeaderLookup() {
		List<Object> headers = element.getHeader();
		int index=0;
		for (Object header : headers) {
			String head = ((String)header).replaceAll("'", "");
			String hs[] = head.split(":");
			headerIndexToTypeMap.put(index,hs[0]);
			index++;
		}
	}
	
	public Dataset getDataset(){
		Dataset dataset = null;
		try {
			if(this.element.getChartType().equalsIgnoreCase(DashboardConstants.PIE_CHART_TYPE))
				dataset = createPiDataset();
			else
				dataset = createDataset();
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		return dataset;
	}
	
	public File getImage() {
		Dataset dataset = getDataset();
		if(dataset == null)
			return null;
		String rowLabel = element.getHeader().get(0).toString();
		String columnLabel = element.getHeader().get(1).toString();
		String fileName = DashboardConstants.TEMP_PATH+element.getId()+element.getChartType()+".jpeg";
		JFreeChart chart = HTMLJFreeChartFactory.getChart(this.element.getChartType(), rowLabel, columnLabel, dataset);
		try {
			File file = new File(fileName);
			int width = 1600/element.getMaxColumn();
			ChartUtilities.saveChartAsJPEG(file, chart, width, 275);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	protected PieDataset createPiDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		List<List<Object>> rows = element.getProcessedData();
		Map<String, List<Integer>> map = element.getDataTypeToIndex();
		if(map==null)
			return dataset;
		
		List<Integer> stringIndices = map.get(DashboardConstants.STRING);
		int stringCount = stringIndices==null?0:stringIndices.size();
		
		List<Integer> timeIndices = map.get(DashboardConstants.DATETIME);
		int timeCount = timeIndices==null?0:timeIndices.size();
		
		int dimCount = stringCount+timeCount;
		
		List<Integer> metricIndices = map.get(DashboardConstants.NUMBER);
		int metricCount = metricIndices==null?0:metricIndices.size();
		
		if(dimCount != 1 )
			throw new RuntimeException("Not Supported!");
		if(metricCount != 1)
			throw new RuntimeException("Not Supported!");
		for (List<Object> row : rows) {
			dataset.setValue(row.get(0).toString(),Double.parseDouble(row.get(1).toString()));
		}
		return dataset;
	}
	
	protected Dataset createDataset() throws Exception {
		Dataset dataset = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		List<List<Object>> rows = element.getProcessedData();
		Map<String, List<Integer>> map = element.getDataTypeToIndex();
		int numMetrics = element.getHeader().size();
		List<Integer> indices = map.get(DashboardConstants.DATETIME);
		int dateDims = indices==null?0:indices.size();
		boolean isDateTime = indices != null && !indices.isEmpty()?true:false;
		
		indices = map.get(DashboardConstants.STRING);
		int stringDims = indices==null?0:indices.size();
		
		boolean isString = indices != null && !indices.isEmpty()?true:false;
		
		int totalDims = dateDims+stringDims;
		
		if(totalDims>2)
			throw new RuntimeException("Not Supported!");
		
		if(numMetrics == 2 && isString && !isDateTime){
			dataset = new DefaultCategoryDataset();
			for (List<Object> row : rows) {
				((DefaultCategoryDataset)dataset).setValue(Double.parseDouble(row.get(1).toString()),row.get(0).toString(),"");
			}
		}else if((numMetrics == 2 && !isString && isDateTime)){
			dataset = new TimeSeriesCollection();
			TimeSeries series = new TimeSeries(element.getHeader().get(1).toString());
			for (List<Object> row : rows) {
				series.addOrUpdate(new Minute(formatter.parse(row.get(0).toString())), Double.parseDouble(row.get(1).toString()));
			}
			((TimeSeriesCollection)dataset).addSeries(series);
		}else if(numMetrics >= 3 && isDateTime && isString){
			Map<String,TimeSeries> tsmap = new HashMap<String,TimeSeries>();
			dataset = new TimeSeriesCollection();
			for (int i = 2; i <= numMetrics-1; i++) {
				for (List<Object> row : rows) {
					String columnHeader = element.getHeader().get(i).toString();
					String valueKey = row.get(1).toString();
					String key = valueKey+"_"+columnHeader;
					TimeSeries series = tsmap.get(key);
					if(series == null){
						series = new TimeSeries(key);
						tsmap.put(key, series);
					}
					series.addOrUpdate(new Minute(formatter.parse(row.get(0).toString())), Double.parseDouble(row.get(i).toString()));
				}
			}
			Collection<TimeSeries> timeSeries = tsmap.values();
			for (TimeSeries ts : timeSeries) {
				((TimeSeriesCollection)dataset).addSeries(ts);
			}
		}else if(numMetrics >= 3 && isString && !isDateTime){
			dataset = new DefaultCategoryDataset();
			for (int i = 2; i <= numMetrics-1; i++) {
				for (List<Object> row : rows) {
					((DefaultCategoryDataset)dataset).setValue(Double.parseDouble(row.get(i).toString()),row.get(0).toString()+element.getHeader().get(i),row.get(1).toString());
				}
			}
		}else if(numMetrics >= 3 && !isString && isDateTime){
			Map<String,TimeSeries> tsmap = new HashMap<String,TimeSeries>();
			dataset = new TimeSeriesCollection();
			for (int i = 1; i <= numMetrics-1; i++) {
				for (List<Object> row : rows) {
					String columnHeader = element.getHeader().get(i).toString();
					String valueKey = row.get(1).toString();
					String key = valueKey+"_"+columnHeader;
					TimeSeries series = tsmap.get(key);
					if(series == null){
						series = new TimeSeries(key);
						tsmap.put(key, series);
					}
					series.addOrUpdate(new Minute(formatter.parse(row.get(0).toString())), Double.parseDouble(row.get(i).toString()));
				}
			}
			Collection<TimeSeries> timeSeries = tsmap.values();
			for (TimeSeries ts : timeSeries) {
				((TimeSeriesCollection)dataset).addSeries(ts);
			}
		}
		return dataset;
	}
}
