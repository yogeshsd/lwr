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
<%
	String chartType = DashboardConstants.HTML_JFREE;
	if(user != null){
		chartType = user.getChartType();
	}
	String userName = request.getParameter("userName");
	request.getSession().setAttribute("userName", userName);
	Report report = ReportManager.getReportManager().getReport(name,userName);
	String html="";
	if(report == null){
		html="No Such Report "+name;		
	}
%>
<section>
	<div style="padding-top:5px">
		<ul class="list-group">
			<li class="list-group-item" style="padding:0px">
			<h4 style="padding-left:10px;"><%=report.getTitle()%></h4>
			<span class="reportdescr" style="color:blue;padding-left:10px"><%=report.getDescription()%></span>	
			</li>
		</ul>
	</div>
	<table id="mytable">
	<%
		int rows = report.getmaxrows();
		for(int i=0;i<rows;i++){
			RowElement row = report.getRows().get(i);
			%>
			<tr>
				<td>
					<table class="innertable" id="innertable_"<%=i%>>
					<tr>
					<%
						List<Element> elements = row.getElements();
						int colIndex = 0;
						int rowIndex = 0;
						for (Element element : elements) {
							String position = i + "_" + rowIndex + "_" + colIndex + "_cell";
							element.setPosition(position);
							%>
							<td>
								<table class="celltable">
									<tr>
										<th><%=element.getTitle()%>
										<img align="right" src="/lwr/images/show-data.png" onclick="runQueryDash(<%=rowIndex%>,<%=i%>,<%=colIndex%>,'<%=report.getTitle()%>','<%=userName%>')"></img></th>
									</tr>
									<tr>
										<td>
											<%
											String divId = "pos_"+element.getPosition();
											double width = 100/element.getMaxColumn();
											 %>
											<div id="<%=divId%>" style="width:<%=width%>%" onclick="refreshElement(this,'<%=report.getTitle()%>','<%=element.getTitle()%>',<%=user.getRefreshInterval()%>,'<%=userName%>')">
												<script>
													$("+divId+").load(loadElement(<%=divId%>,'<%=report.getTitle()%>','<%=element.getTitle()%>','<%=userName%>'))
												</script>
											</div>										
										</td>
									</tr>
								</table>
							</td>
							<%
							colIndex++;
							element.clear();
						}
						%>
					</tr>
					</table>
					<%
				}
			%>
	</table>
</section>

<%@ include file="footer.jsp" %>