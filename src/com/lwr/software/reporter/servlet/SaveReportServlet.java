package com.lwr.software.reporter.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserManager;
import com.lwr.software.reporter.renderer.report.HTMLReportRenderer;
import com.lwr.software.reporter.renderer.report.IReportRenderer;
import com.lwr.software.reporter.renderer.report.ReportRendererFactory;
import com.lwr.software.reporter.security.UserSecurityContext;

public class SaveReportServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = (String)request.getParameter("type");
		String reportName = (String)request.getParameter("name");
		
		String chartType = DashboardConstants.HTML_JFREE;
		UserSecurityContext loginCode = (UserSecurityContext) request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
		if(loginCode != null){
			User user = UserManager.getUserManager().getUser(loginCode.getUserName());
			chartType = user.getChartType();
		}
		
		OutputStream stream = response.getOutputStream();
		if(type.equalsIgnoreCase(DashboardConstants.CSV)){
			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(DashboardConstants.CSV, reportName,chartType);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+reportName+".csv");
			renderer.render(stream);
		}else if(type.equalsIgnoreCase(DashboardConstants.PDF)){
			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(DashboardConstants.PDF, reportName,chartType);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+reportName+".pdf");
			renderer.render(stream);
		}else if(type.equalsIgnoreCase(DashboardConstants.HTML)){
			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(DashboardConstants.HTML, reportName,chartType);
			((HTMLReportRenderer)renderer).setLoadData(true);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+reportName+".html");
			renderer.render(stream);
		}
	}
}
