package com.lwr.software.reporter.renderer.element;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.csv.CSVBarChartRenderer;
import com.lwr.software.reporter.renderer.csv.CSVColumnChartRenderer;
import com.lwr.software.reporter.renderer.csv.CSVLineChartRenderer;
import com.lwr.software.reporter.renderer.csv.CSVPieChartRenderer;
import com.lwr.software.reporter.renderer.csv.CSVTableRenderer;
import com.lwr.software.reporter.reportmgmt.Element;

public class CSVElementRendererFactory implements IELementRendererFactory {

	@Override
	public IElementRenderer getRenderer(Element element) {
		if(element == null)
			return null;
		String type = element.getChartType();
		if(type==null)
			return null;
		if (type.equals(DashboardConstants.PIE_CHART_TYPE)) {
			return new CSVPieChartRenderer(element);
		} else if (type.equals(DashboardConstants.LINE_CHART_TYPE)) {
			return new CSVLineChartRenderer(element);
		} else if (type.equals(DashboardConstants.BAR_CHART_TYPE) || type.equals(DashboardConstants.BAR_STACK_CHART_TYPE)) {
			return new CSVBarChartRenderer(element);
		} else if (type.equals(DashboardConstants.COLUMN_CHART_TYPE) || type.equals(DashboardConstants.COLUMN_STACK_CHART_TYPE)) {
			return new CSVColumnChartRenderer(element);
		} else if (type.equalsIgnoreCase(DashboardConstants.TABLE_TYPE)) {
			return new CSVTableRenderer(element);
		}
		return null;
	}

}
