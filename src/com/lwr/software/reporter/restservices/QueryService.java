package com.lwr.software.reporter.restservices;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.connmgmt.ConnectionPool;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;
import com.lwr.software.reporter.utils.DWHUtility;

@Path("/query/")
public class QueryService {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String executeQuery(
			@QueryParam("sql") String sql,
			@QueryParam("charttype") String chartType,
			@QueryParam("dbAlias") String dbAlias
			){
		return testQuery(sql, chartType, dbAlias);
	}

	@Path("/report/")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String executeQuery(
			@QueryParam("reportName") String reportName,
			@QueryParam("rowIndex") Integer rowIndex,
			@QueryParam("innerRowIndex") Integer innerRowIndex,
			@QueryParam("columnIndex") Integer columnIndex
			){
		Report report = ReportManager.getReportManager().getReport(reportName);
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
					return testQuery(sql, chartType, dbAlias);
				}
			}
		}
		return "No Such Element";
	}
	
	private String testQuery(String sql, String chartType, String dbAlias) {
		boolean isError = false;
		StringBuffer html = new StringBuffer();
		List<String> messages = new ArrayList<String>();
		List<List<Object>> rows = new ArrayList<List<Object>>();
		Connection connection = null;
		try {
			connection = ConnectionPool.getInstance().getConnection(dbAlias);
			DWHUtility utility = new DWHUtility(connection);
			rows = utility.executeQuery(sql);
		} catch (SQLException e) {
			isError = true;
			messages.add("SQL Code : "+e.getSQLState());
			messages.add("Error Code : "+e.getErrorCode());
			messages.add("SQL Exception : "+e.getMessage());
			e.printStackTrace();
		} finally{
			if(connection != null){
				ConnectionPool.getInstance().releaseConnection(connection, dbAlias);
			}
		}

		if(!isError){
			html.append("<table style=\"background-color: #f2f2f2;border: 1px solid #6a6a6a\">");
			html.append("<tr>");
			List<Object> headers = rows.get(0);
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
				html.append("<th style=\"background-color: #6a6a6a;color: #ffffff;\">"+columnName+"</th>");
			}
			html.append("</tr>");
			
			Integer stringCount = dataTypeToCount.get(DashboardConstants.STRING);
			Integer timeCount = dataTypeToCount.get(DashboardConstants.DATETIME);
			Integer metricCount = dataTypeToCount.get(DashboardConstants.NUMBER);
			if(metricCount == null)
				metricCount=0;
			if(stringCount == null)
				stringCount=0;
			if(timeCount == null)
				timeCount=0;
			
			int dimCount = stringCount+timeCount;
			
			rows.remove(0);
			for (List<Object> row : rows) {
				html.append("<tr>");
				for (Object column : row) {
					html.append("<td style=\"border: 1px solid #6a6a6a\">"+column+"</td>");
				}
				html.append("</tr>");
			}
			html.append("</table>");
			int numRows = rows==null?0:rows.size();
			if(chartType.equalsIgnoreCase(DashboardConstants.PIE_CHART_TYPE)){
				if(dimCount != 1 ){
					isError = true;
					messages.add("Chart needs only 1 dimension but the query has "+dimCount+" dimension columns. Consider rewriting the query.");
				}
				if(metricCount != 1){
					isError = true;
					messages.add("Chart needs only one metric by the query has "+metricCount+" metric columns. Consider limiting the rows.");
				}
				if(rows.size()>50){
					isError = true;
					messages.add("Too many data point. Chart is best viewed with < 50 points but the query has "+numRows+" metric columns.");
				}
			}else if(
						chartType.equalsIgnoreCase(DashboardConstants.BAR_CHART_TYPE) 
						|| chartType.equalsIgnoreCase(DashboardConstants.BAR_STACK_CHART_TYPE)
						|| chartType.equalsIgnoreCase(DashboardConstants.COLUMN_CHART_TYPE)
						|| chartType.equalsIgnoreCase(DashboardConstants.COLUMN_STACK_CHART_TYPE)
						|| chartType.equalsIgnoreCase(DashboardConstants.LINE_CHART_TYPE)
					){
				if(dimCount > 2 || stringCount < 1){
					isError = true;
					messages.add("Chart needs not more than 2 dimension but the query has "+dimCount+" dimension columns. Consider rewriting the query");
				}
				if(metricCount <1  || metricCount>4){
					isError = true;
					messages.add("Chart needs atleast 1 and at max 4 metrics by the query has "+metricCount+" metric columns. Consider rewriting the query");
				}
				if(rows.size()>255 && !chartType.equalsIgnoreCase(DashboardConstants.LINE_CHART_TYPE)){
					isError = true;
					messages.add("Too many data point. Chart is best viewed with < 255 points but the query has "+numRows+" metric columns. Consider limiting the rows");
				}
			}
		}
		
		StringBuffer htmlToReturn = new StringBuffer();
		if(isError){
			htmlToReturn.append("<div>");
			htmlToReturn.append("<h3>Validation Status : <img src=\"images/red_cross.ico\"></></h3>");
			htmlToReturn.append("<ul>");
			for (String message : messages) {
				htmlToReturn.append("<li>"+message+"</li>");
			}
			htmlToReturn.append("</ul>");
			htmlToReturn.append("</div>");
		}else{
			htmlToReturn.append("<div>");
			htmlToReturn.append("<h3>Validation Status : <img src=\"images/right_tick.ico\"></></h3>");
			htmlToReturn.append("</div>");
		}
		htmlToReturn.append("<h3>Data Rows</h3>");
		htmlToReturn.append("<hr>");
		htmlToReturn.append(html);
		return htmlToReturn.toString();
	}
}
