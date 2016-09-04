package com.lwr.software.reporter.renderer.report;

import com.lwr.software.reporter.DashboardConstants.OutputFormat;
import com.lwr.software.reporter.reportmgmt.Report;

public class ReportRendererFactory {

	public static IReportRenderer getReportRenderer(OutputFormat outputFormat,Report report,String chartType){
		AbstractReportRenderer renderer = null;
		if(outputFormat.equals(OutputFormat.HTML)){
			renderer = new HTMLReportRenderer(report);
		}else if(outputFormat.equals(OutputFormat.CSV)){
			renderer = new CSVReportRenderer(report);
		}else if(outputFormat.equals(OutputFormat.PDF)){
			renderer = new PDFReportRenderer(report);
		}
		renderer.setChartType(chartType);
		return renderer;
	}
}
