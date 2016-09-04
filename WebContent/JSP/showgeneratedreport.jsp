<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="com.lwr.software.reporter.reportmgmt.RowElement"%>
<%@page import="com.lwr.software.reporter.reportmgmt.Element"%>
<%@page import="com.lwr.software.reporter.reportmgmt.ReportManager"%>
<%@page import="com.lwr.software.reporter.reportmgmt.Report"%>
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
<section>
<%
	String reportFileName = (String)request.getParameter("filename");
	String path = DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+user.getUsername()+File.separatorChar+"schedule";
	String reportFileFullPath = path+File.separatorChar+reportFileName;
	FileReader reader = new FileReader(reportFileFullPath);
	BufferedReader bufReader = new BufferedReader(reader);
	while(true){
		String line = bufReader.readLine();
		if(line == null)
			break;
		%>
		<%=line%>
		<%
	}
%>
</section>

<%@ include file="footer.jsp" %>