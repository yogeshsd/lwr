package com.lwr.software.reporter.renderer.element;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.html.google.HTMLAnnotatedChartRenderer;
import com.lwr.software.reporter.renderer.html.google.HTMLBarChartRenderer;
import com.lwr.software.reporter.renderer.html.google.HTMLColumnChartRenderer;
import com.lwr.software.reporter.renderer.html.google.HTMLLineChartRenderer;
import com.lwr.software.reporter.renderer.html.google.HTMLPieChartRenderer;
import com.lwr.software.reporter.renderer.html.google.HTMLTableRenderer;
import com.lwr.software.reporter.reportmgmt.Element;

public class HTMLGoogleElementRendererFactory implements IELementRendererFactory {

	@Override
	public IElementRenderer getRenderer(Element element) {
		String type = element.getChartType();
		if(type==null)
			return null;
		if (type.equals(DashboardConstants.PIE_CHART_TYPE)) {
			return new HTMLPieChartRenderer(element);
		} else if (type.equals(DashboardConstants.LINE_CHART_TYPE)) {
			return new HTMLLineChartRenderer(element);
		} else if (type.equals(DashboardConstants.BAR_CHART_TYPE)) {
			return new HTMLBarChartRenderer(element);
		} else if (type.equals(DashboardConstants.BAR_STACK_CHART_TYPE)) {
			return new HTMLBarChartRenderer(element,true);
		} else if (type.equals(DashboardConstants.COLUMN_CHART_TYPE)) {
			return new HTMLColumnChartRenderer(element);
		} else if (type.equals(DashboardConstants.COLUMN_STACK_CHART_TYPE)) {
			return new HTMLColumnChartRenderer(element,true);
		} else if (type.equalsIgnoreCase(DashboardConstants.TABLE_TYPE)) {
			return new HTMLTableRenderer(element);
		} else if (type.equalsIgnoreCase(DashboardConstants.ANNOTATED_TYPE)) {
			return new HTMLAnnotatedChartRenderer(element);
		}
		return null;
	}

}
