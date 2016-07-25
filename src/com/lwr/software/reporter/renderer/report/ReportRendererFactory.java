package com.lwr.software.reporter.renderer.report;

import com.lwr.software.reporter.DashboardConstants;

public class ReportRendererFactory {

	public static IReportRenderer getReportRenderer(String type,String reportName,String chartType){
		AbstractReportRenderer renderer = null;
		if(type.equalsIgnoreCase(DashboardConstants.HTML)){
			renderer = new HTMLReportRenderer(reportName);
		}else if(type.equalsIgnoreCase(DashboardConstants.CSV)){
			renderer = new CSVReportRenderer(reportName);
		}else if(type.equalsIgnoreCase(DashboardConstants.PDF)){
			renderer = new PDFReportRenderer(reportName);
		}
		renderer.setChartType(chartType);
		return renderer;
	}
}
