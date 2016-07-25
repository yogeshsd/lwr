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
	
	protected Report report;
	
	protected String type = DashboardConstants.HTML;
	
	protected String chartType = DashboardConstants.HTML_JFREE;
	
	public AbstractReportRenderer(String reportName) {
		this.reportName=reportName;
		init();
	}
	
	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	private void init() {
		report = ReportManager.getReportManager().getReport(reportName);
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
