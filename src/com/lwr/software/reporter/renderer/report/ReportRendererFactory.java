package com.lwr.software.reporter.renderer.report;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;

public class ReportRendererFactory {

	public static IReportRenderer getReportRenderer(String type,Report report,String chartType){
		AbstractReportRenderer renderer = null;
		if(type.equalsIgnoreCase(DashboardConstants.HTML)){
			renderer = new HTMLReportRenderer(report);
		}else if(type.equalsIgnoreCase(DashboardConstants.CSV)){
			renderer = new CSVReportRenderer(report);
		}else if(type.equalsIgnoreCase(DashboardConstants.PDF)){
			renderer = new PDFReportRenderer(report);
		}
		renderer.setChartType(chartType);
		return renderer;
	}
}
