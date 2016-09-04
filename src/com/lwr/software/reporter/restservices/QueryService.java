package com.lwr.software.reporter.restservices;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.ElementRendererFactory;
import com.lwr.software.reporter.renderer.element.IELementRendererFactory;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;

@Path("/query/")
public class QueryService {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String executeQuery(
			@QueryParam("sql") String sql,
			@QueryParam("charttype") String chartType,
			@QueryParam("dbAlias") String dbAlias
			){
		return processQuery(sql, chartType, dbAlias);
	}

	@Path("/report/")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String executeQuery(
			@QueryParam("reportName") String reportName,
			@QueryParam("rowIndex") Integer rowIndex,
			@QueryParam("innerRowIndex") Integer innerRowIndex,
			@QueryParam("columnIndex") Integer columnIndex,
			@QueryParam("userName") String userName
			){
		Report report = ReportManager.getReportManager().getReport(reportName,userName);
		List<RowElement> reportElements = report.getRows();
		for (RowElement rowElement : reportElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				int iri = element.getRow();
				int ci = element.getColumn();
				int ri = element.getRownumber();
				if(ri == rowIndex && ci == columnIndex && iri == innerRowIndex){
					String sql = element.getQuery();
					String dbAlias = element.getDbalias();
					String chartType = element.getChartType();
					return processQuery(sql, chartType, dbAlias);
				}
			}
		}
		return "No Such Element";
	}
	
	private String processQuery(String sql, String chartType, String dbAlias) {
		Element element = new Element(sql, chartType, dbAlias);
		try {
			element.init();
		} catch (SQLException e) {
			return "<h6>"+e.getMessage()+"</h6>";
		}
		IELementRendererFactory factory = ElementRendererFactory.getRendererFactory(DashboardConstants.HTML_JFREE);
		IElementRenderer renderer = factory.getRenderer(element);
		boolean validation = renderer.validate();
		Set<String> chartMessages = renderer.getChartMessages();
		List<List<Object>> rows = element.getData();
		return buildHTML(validation,chartMessages,element);
	}
	
	private String buildHTML(boolean isValid,Set<String> messages, Element element) {
		StringBuffer html = new StringBuffer();
		if(isValid){
			html.append("<table style=\"border: 1px solid #6a6a6a;border-collapse: collapse;\">");
			html.append("<tr>");
			List<Object> headers = element.getHeader();
			List<List<Object>> rows = element.getData();
			java.util.Map<String,Integer> dataTypeToCount = new HashMap<String,Integer>();
			for (Object header : headers) {
				String head = ((String)header).replaceAll("'", "");
				String hs[] = head.split(":");
				String dataType = hs[0];
				String columnName = hs[1];
				Integer count = dataTypeToCount.get(dataType);
				if(count == null){
					count = 0;
					dataTypeToCount.put(dataType,count);
				}
				count++;
				dataTypeToCount.put(dataType,count);
				html.append("<th style=\"background-color: #679BB7;color: #ffffff;border-collapse: collapse;font-family:Arial,Verdana,sans-serif;font-size:12px;\">"+columnName+"</th>");
			}
			html.append("</tr>");
			for (List<Object> row : rows) {
				html.append("<tr>");
				for (Object column : row) {
					html.append("<td style=\"border: 1px solid #679BB7;border-collapse: collapse;font-family:Arial,Verdana,sans-serif;font-size:12px;\">"+column+"</td>");
				}
				html.append("</tr>");
			}
			html.append("</table>");
		}
		
		StringBuffer htmlToReturn = new StringBuffer();
		if(!isValid){
			htmlToReturn.append("<div>");
			htmlToReturn.append("<h3><span style=\"font-family:Arial,Verdana,sans-serif;font-size:16px;\">Validation Status : <img src=\"images/red_cross.ico\"></></span></h3>");
			htmlToReturn.append("<ul>");
			for (String message : messages) {
				htmlToReturn.append("<li>"+message+"</li>");
			}
			htmlToReturn.append("</ul>");
			htmlToReturn.append("</div>");
		}else{
			htmlToReturn.append("<div>");
			htmlToReturn.append("<h3><span style=\"font-family:Arial,Verdana,sans-serif;font-size:16px;\">Validation Status : <img src=\"images/right_tick.ico\"></></span></h3>");
			htmlToReturn.append("</div>");
		}
		htmlToReturn.append("<h3><span style=\"font-family:Arial,Verdana,sans-serif;font-size:16px;\">Data Rows</span></h3>");
		htmlToReturn.append("<hr>");
		htmlToReturn.append(html);
		return htmlToReturn.toString();
	}
}
