package com.lwr.software.reporter.renderer.html.google;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.html.google.options.HTMLOptionFactory;
import com.lwr.software.reporter.reportmgmt.Element;

public class HTMLAnnotatedChartRenderer extends HTMLAbstractRenderer {

	public HTMLAnnotatedChartRenderer(Element element) {
		super(element);
	}

	@Override
	public String render() {
		StringBuffer html = new StringBuffer();
		html.append("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>\n");
		html.append("<script type=\"text/javascript\">\n");
		html.append("	google.charts.load('current', {'packages':['annotatedtimeline']});\n");
		html.append("	google.charts.setOnLoadCallback(drawChart);\n");
		html.append("	function drawChart() {\n");
		html.append("	var data = new google.visualization.DataTable();\n");
		html.append(getProcessedHeaderString());
		html.append("data.addRows([\n");
		html.append(getProcessedDataSetString());
		html.append("	]);\n");
		html.append("	var chart = new google.visualization.AnnotatedTimeLine(document.getElementById('pos_" + element.getPosition() + "'));\n");
		html.append(HTMLOptionFactory.getOption(DashboardConstants.CHART_OPTIONS).getOption(element.getTitle()));
		html.append("	chart.draw(data, options);\n");
		html.append("}\n");
		html.append("</script>\n");
		return html.toString();
	}

}
