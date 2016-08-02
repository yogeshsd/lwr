package com.lwr.software.reporter.renderer.report;

import java.util.List;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.ElementRendererFactory;
import com.lwr.software.reporter.renderer.element.IELementRendererFactory;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;

public abstract class AbstractReportRenderer implements IReportRenderer {

	protected String reportName;
	
	protected String userName  = DashboardConstants.PUBLIC_USER;
	
	protected Report report;
	
	protected String type = DashboardConstants.HTML;
	
	protected String chartType = DashboardConstants.HTML_JFREE;
	
	public AbstractReportRenderer(String name) {
		String patterns[] = name.split(":");
		if(patterns.length==2){
			this.userName = patterns[0];
			this.reportName = patterns[1];
		}else{
			this.reportName=name;
		}
		init();
	}
	
	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	private void init() {
		report = ReportManager.getReportManager().getReport(reportName,userName);
		if(report == null)
			return;
		List<RowElement> rowElements = report.getRows();
		for (RowElement rowElement : rowElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				element.init();
			}
		}
	}

	public String buildElement(Element element) {
		IELementRendererFactory factory = ElementRendererFactory.getRendererFactory(chartType);
		IElementRenderer renderer = factory.getRenderer(element);
		if (renderer != null)
			return renderer.render();
		else
			return "No Data";
	}
}
