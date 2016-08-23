package com.lwr.software.reporter.renderer.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.ElementRendererFactory;
import com.lwr.software.reporter.renderer.element.IELementRendererFactory;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.RowElement;

public class CSVReportRenderer extends AbstractReportRenderer {

	public CSVReportRenderer(Report report) {
		super(report);
	}
	@Override
	public String render() {
		throw new RuntimeException("Not Implemented");
		
	}
	@Override
	public void render(OutputStream stream) {
		List<RowElement> rowElements = this.report.getRows();
		for (RowElement rowElement : rowElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				try {
					BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(stream));
					IELementRendererFactory factory = ElementRendererFactory.getRendererFactory(DashboardConstants.CSV);
					IElementRenderer renderer = factory.getRenderer(element);
					String dataToWrite = renderer.render();
					bufWriter.write(dataToWrite);
					bufWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
