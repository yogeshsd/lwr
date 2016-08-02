package com.lwr.software.reporter.renderer.element;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeBarChart;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeBarStackChart;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeColumnChart;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeColumnStackChart;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeLineChart;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreePieChart;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeTableChart;
import com.lwr.software.reporter.reportmgmt.Element;

public class HTMLJFreeElementRendererFactory implements IELementRendererFactory {

	@Override
	public IElementRenderer getRenderer(Element element) {
		String type = element.getChartType();
		if(type==null)
			return null;
		if (type.equals(DashboardConstants.PIE_CHART_TYPE)) {
			return new HTMLJFreePieChart(element);
		} else if (type.equals(DashboardConstants.LINE_CHART_TYPE)) {
			return new HTMLJFreeLineChart(element);
		} else if (type.equals(DashboardConstants.BAR_CHART_TYPE)) {
			return new HTMLJFreeBarChart(element);
		} else if (type.equals(DashboardConstants.BAR_STACK_CHART_TYPE)) {
			return new HTMLJFreeBarStackChart(element);
		} else if (type.equals(DashboardConstants.COLUMN_CHART_TYPE)) {
			return new HTMLJFreeColumnChart(element);
		} else if (type.equals(DashboardConstants.COLUMN_STACK_CHART_TYPE)) {
			return new HTMLJFreeColumnStackChart(element);
		} else if (type.equalsIgnoreCase(DashboardConstants.TABLE_TYPE)) {
			return new HTMLJFreeTableChart(element);
		} 
		return null;
	}
}
