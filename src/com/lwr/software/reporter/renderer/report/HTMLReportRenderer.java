package com.lwr.software.reporter.renderer.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.RowElement;

public class HTMLReportRenderer extends AbstractReportRenderer {
	
	private long refreshInterval = DashboardConstants.DEFAULT_REFRESH_INTERVAL_MILLIS;
	
	private boolean loadData = false;
	
	public HTMLReportRenderer(Report report) {
		super(report);
	}
	
	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	
	public void setLoadData(boolean loadData) {
		this.loadData = loadData;
	}
	
	@Override
	public String render() {
		try {
			return buildSection();
		} catch (Exception e) {
			e.printStackTrace();
			return "Cannot Build Report";
		}
	}
	
	public String buildSection() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<section>\n");
		html.append("<div style=\"padding-top:5px\">");
		html.append("<ul class=\"list-group\">");
		html.append("<li class=\"list-group-item\" style=\"padding:0px\">");
		html.append("<h4 style=\"padding-left:10px;\">"+report.getTitle()+"</h4>");
		html.append("<span class=\"reportdescr\" style=\"color:blue;padding-left:10px\">"+report.getDescription()+"</span>");	
		html.append("</li>");
		html.append("</ul>");
		html.append("</div>");
		html.append("<table id=\"mytable\">\n");
		html.append(buildTable(report));
		html.append("</table>\n");
		html.append("</section>");
		System.out.println(html);
		return html.toString();
	}

	private String buildTable(Report report) {
		StringBuffer html = new StringBuffer();
		List<RowElement> rows = report.getRows();
		int rowNumber = 0;
		for (RowElement row : rows) {
			html.append("<tr><td>");
			html.append("<table class=\"innertable\" id=\"innertable_" + rowNumber + "\">");
			html.append("<tr>");
			List<Element> elements = row.getElements();
			int colIndex = 0;
			int rowIndex = 0;
			for (Element element : elements) {
				String position = rowNumber + "_" + rowIndex + "_" + colIndex + "_cell";
				element.setPosition(position);
				html.append("<td>\n");
				html.append("<table class=\"celltable\">");
				html.append("<tr>");
				html.append("<th>\n");
				html.append(element.getTitle());
				html.append("<img align=\"right\" src=\"/lwr/images/show-data.png\" onclick=\"runQueryDash(" + rowIndex	+ "," + rowNumber + "," + colIndex + ",'" +report.getTitle() + "')\"></img>");
				html.append("</th>");
				html.append("</tr><td>");
				String divId = "pos_"+element.getPosition();
				html.append("<div id=\""+divId+"\" onclick=\"refreshElement(this,'"+report.getTitle()+"','"+element.getTitle()+"',"+refreshInterval+")\">\n");
				if(loadData)
					html.append(buildElement(element));
				else
					html.append("<script>$("+divId+").load(loadElement("+divId+",'"+report.getTitle()+"','"+element.getTitle()+"'))</script>");
				html.append("</div>\n");
				html.append("</td>");
				html.append("</tr>");
				html.append("</table>");
				html.append("</td>\n");
				colIndex++;
				element.clear();
			}
			rowNumber++;
			html.append("</tr>");
			html.append("</table>");
			html.append("</td></tr>");
		}
		return html.toString();
	}

	@Override
	public void render(OutputStream stream) {
		OutputStreamWriter writer = new OutputStreamWriter(stream);
		try {
			writer.write(this.render());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
