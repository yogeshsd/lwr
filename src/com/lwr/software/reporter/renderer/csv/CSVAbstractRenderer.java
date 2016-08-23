package com.lwr.software.reporter.renderer.csv;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;

public abstract class CSVAbstractRenderer implements IElementRenderer{
	
	public Element element;
	
	protected Set<String> chartMessages = new HashSet<String>();
	
	public String render(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\n");
		buffer.append(element.getTitle());
		buffer.append("\n\n");
		List<Object> headers = element.getHeader();
		for (Object header : headers) {
			buffer.append(header+",");
		}
		buffer.append("\n");
		List<List<Object>> rows = element.getData();
		for (List<Object> row : rows) {
			for (Object col : row) {
				buffer.append(col+",");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public Set<String> getChartMessages() {
		return chartMessages;
	}

	public void setChartMessages(Set<String> chartMessages) {
		this.chartMessages = chartMessages;
	}

	public CSVAbstractRenderer(Element element){
		this.element = element;
	}

	public boolean validate() {
		if(element.getDimCount()>2){
			chartMessages.add("The query has "+element.getDimColNames()+" dimensions. Minimum one and maximum two dimensions are supported.");
			return false;
		}
		if(element.getMetricCount()>4){
			chartMessages.add("The query has "+element.getMetricColNames()+" metrics. At max only 4 metrics can be graphed.");
			return false;
		}
		if(this.element.getChartType().equalsIgnoreCase(DashboardConstants.PIE_CHART_TYPE))
			if(element.getDimCount()!=1){
				chartMessages.add("The query has "+element.getDimColNames()+" dimension. Pie chart is supported with only one dimension.");
				return false;
			}else if(element.getMetricCount()!=1){
				chartMessages.add("The query has "+element.getMetricColNames()+" metrics. Pie chart is supported with only one metric.");
				return false;
			}
		return true;
	}
}
