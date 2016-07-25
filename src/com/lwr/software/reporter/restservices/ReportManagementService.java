package com.lwr.software.reporter.restservices;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

	@Path("/save")
	@POST
	public Response saveReport(
			@FormParam("components") String components,
			@FormParam("dashboardname") String dashboardname)
	{
			boolean status = ReportManager.getReportManager().saveReport(components,dashboardname);
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
			@QueryParam("renderType") String renderType
			){
		Report report = ReportManager.getReportManager().getReport(reportName);
		System.out.println("Report Name = "+reportName+", Element Name = "+elementName);
		List<RowElement> reportElements = report.getRows();
		for (RowElement rowElement : reportElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				if(element.getTitle().equalsIgnoreCase(elementName)){
					element.init();
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
