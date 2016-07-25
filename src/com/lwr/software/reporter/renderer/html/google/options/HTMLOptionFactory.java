package com.lwr.software.reporter.renderer.html.google.options;

import com.lwr.software.reporter.DashboardConstants;

public class HTMLOptionFactory {

	public static IHTMLOption getOption(String type){
		if(type.equalsIgnoreCase(DashboardConstants.CHART_OPTIONS))
			return new HTMLChartOption();
		if(type.equalsIgnoreCase(DashboardConstants.TABLE_OPTIONS))
			return new HTMLTableOption();
		return null;
	}
}
