package com.lwr.software.reporter.renderer.html.jfreechart;

import java.util.List;

import com.lwr.software.reporter.reportmgmt.Element;

public class HTMLJFreeTableChart extends HTMLJFreeAbstract {

	public HTMLJFreeTableChart(Element element) {
		super(element);
	}

	@Override
	public String render() {
		StringBuffer html = new StringBuffer();
		html.append("<table class=\"charttable\">");
		html.append("<tr>");
		List<Object> headers = element.getHeader();
		for (Object header : headers) {
			String head = ((String)header).replaceAll("'", "");
			String hs[] = head.split(":");
			html.append("<th>"+hs[1]+"</th>");

		}
		html.append("</tr>");
		List<List<Object>> rows = element.getData();
		for (List<Object> row : rows) {
			html.append("<tr>");
			for (Object column : row) {
				html.append("<td>"+column+"</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>");
		return html.toString();
	}
}
