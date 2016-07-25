<%@page import="java.io.FileWriter"%>
<%@page import="java.io.File"%>
<%@page import="com.lwr.software.reporter.renderer.report.IReportRenderer"%>
<%@page import="com.lwr.software.reporter.renderer.report.ReportRendererFactory"%>
<%@page import="com.lwr.software.reporter.renderer.report.HTMLReportRenderer"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.User"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.UserManager"%>
<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="header.jsp" %>
<%
	String chartType = DashboardConstants.HTML_JFREE;
	if(user != null){
		chartType = user.getChartType();
	}
	IReportRenderer renderer = ReportRendererFactory.getReportRenderer(DashboardConstants.HTML, name,chartType);
	((HTMLReportRenderer)renderer).setRefreshInterval(user.getRefreshInterval());
	String html = renderer.render();
%>
<%=html%> 
<%@ include file="footer.jsp" %>