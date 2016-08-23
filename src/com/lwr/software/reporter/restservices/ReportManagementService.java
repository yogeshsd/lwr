package com.lwr.software.reporter.restservices;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.ElementRendererFactory;
import com.lwr.software.reporter.renderer.element.IELementRendererFactory;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;

@Path("/reports/")
public class ReportManagementService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getReportNames(
			@QueryParam("userName") String userName
			){
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray reports = new JSONArray();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				try {
					 String reportString =  objectMapper.writeValueAsString(report);
					 reports.add(reportString);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return reports.toJSONString();
	}

	@Path("/save")
	@POST
	public Response saveReport(
			@FormParam("components") String components,
			@FormParam("dashboardname") String rName)
	{
			String patterns[] = rName.split(":");
			String userName = DashboardConstants.PUBLIC_USER;
			String reportName = rName;
			if(patterns.length==2){
				userName = patterns[0];
				reportName = patterns[1];
			}
			boolean status = ReportManager.getReportManager().saveReport(components,reportName,userName);
			if(status)
				return Response.ok("Report Saved.").build();
			else
				return Response.ok("Unable to save Report.").build();
	}
	
	@Path("/element/")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String executeQuery(
			@QueryParam("reportName") String reportName,
			@QueryParam("elementName") String elementName,
			@QueryParam("renderType") String renderType,
			@QueryParam("userName") String userName
			){
		Report report = ReportManager.getReportManager().getReport(reportName,userName);
		System.out.println("Report Name = "+reportName+", Element Name = "+elementName);
		List<RowElement> reportElements = report.getRows();
		for (RowElement rowElement : reportElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				if(element.getTitle().equalsIgnoreCase(elementName)){
					try {
						element.init();
					} catch (SQLException e) {
						return "<h6>"+e.getMessage()+"</h6>";
					}
					IELementRendererFactory factory = ElementRendererFactory.getRendererFactory(DashboardConstants.HTML_JFREE);
					IElementRenderer renderer = factory.getRenderer(element);
					String toReturn =  renderer.render();
					element.clear();
					return toReturn;
				}
			}
		}
		return "No Data";
	}
	
}
