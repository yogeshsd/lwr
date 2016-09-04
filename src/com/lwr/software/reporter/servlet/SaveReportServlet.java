package com.lwr.software.reporter.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.DashboardConstants.OutputFormat;
import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserManager;
import com.lwr.software.reporter.renderer.report.HTMLReportRenderer;
import com.lwr.software.reporter.renderer.report.IReportRenderer;
import com.lwr.software.reporter.renderer.report.ReportRendererFactory;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.security.UserSecurityContext;

public class SaveReportServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = (String)request.getParameter("type");
		String reportName = (String)request.getParameter("name");
		String userName = (String)request.getParameter("userName");
		
		String chartType = DashboardConstants.HTML_JFREE;
		UserSecurityContext loginCode = (UserSecurityContext) request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
		if(loginCode != null){
			User user = UserManager.getUserManager().getUser(loginCode.getUserName());
			chartType = user.getChartType();
		}
		
		Report report = ReportManager.getReportManager().getReport(reportName,userName);
		if(report == null)
			return;

		OutputStream stream = response.getOutputStream();
		if(type.equalsIgnoreCase(OutputFormat.CSV.toString())){
			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(OutputFormat.CSV, report,chartType);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+reportName+".csv");
			renderer.render(stream);
		}else if(type.equalsIgnoreCase(DashboardConstants.PDF)){
			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(OutputFormat.PDF, report,chartType);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+reportName+".pdf");
			renderer.render(stream);
		}else if(type.equalsIgnoreCase(DashboardConstants.HTML)){
			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(OutputFormat.HTML, report,chartType);
			((HTMLReportRenderer)renderer).setLoadData(true);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+reportName+".html");
			renderer.render(stream);
		}
	}
}
