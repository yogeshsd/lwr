<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@page import="com.lwr.software.reporter.reportmgmt.Report"%>
<%@page import="java.util.Set"%>
<%@page import="com.lwr.software.reporter.reportmgmt.ReportManager"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="header.jsp"%>
<br>
<ul class="nav nav-tabs">
	<li class="active"><a href="javascript:showTab('public');">Public Reports</a></li>
	<li><a href="javascript:showTab('personal');">Personal Reports</a></li>
</ul>
<script>
	function search(){
		var text = $("#searchtext").val().toLowerCase();
		var rows = $(".repotlisttable");
		
		$(rows).each(function() {
			var link = $(this).html();
			if(link.toLowerCase().indexOf(text)>=0){
				$(this).parent().show();							
			}else{
				var description = $(this).parent().find(".reportdescr").text().toLowerCase();
				if(description.indexOf(text)>=0){
					$(this).parent().show();							
				}else{
					$(this).parent().hide();
				}
			}
		});
	}
</script>
<script>
	function showTab(name){
		if(name == "public"){
			$("#public").show();
			$("#personal").hide();
		}else if(name == "personal"){
			$("#personal").show();
			$("#public").hide();
		}
	}
</script>
<%
	ReportManager.getReportManager().reload(user.getUsername());
	ReportManager.getReportManager().reload(DashboardConstants.PUBLIC_USER);
	Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(user.getUsername());
	Collection<Report> userReps = new ArrayList<Report>();
	Collection<Report> publicReps = new ArrayList<Report>();
	
	if(userToReport != null){
		Map<String,Report> userRepMap = userToReport.get(user.getUsername());
		Map<String,Report> publicRepMap = userToReport.get(DashboardConstants.PUBLIC_USER);
		if(userRepMap != null){
			userReps = userRepMap.values();
		}
			if(publicRepMap != null){
			publicReps = publicRepMap.values();
		}
	}
%>
<section id="public">
	<div align="right">
		<table id="homesearchtable">
			<tr>
				<td>Report Search</td>
				<td><input width="100%" type="text" onkeyup="search()" id="searchtext" value=""></input></td>
			</tr>
		</table>
	</div>
	<br>
	<div>
	<ul class="list-group">
			<%
				for(Report report: publicReps){
					String title = report.getTitle();
					String descrption = report.getDescription();
					String repLink = DashboardConstants.PUBLIC_USER+":"+title;
					%>
		    			<li class="list-group-item" style="padding:0px">
		            			<a class="homepagelinks" title="Click to run report" alt="Click to run report" href="/lwr/report?name=<%=repLink%>">
		            				<h5 style="padding-left:10px;color:blue;font-weight: bold"><%=title%></h5>
		            			</a>
		            			<span class="repotlisttable" style="color:black;padding-left:10px"><%=descrption%></span>	
		        		</li>
					<%
				}
			%>
		</ul>
	</div>
</section>
<section id="personal" style="display:none">
	<div align="right">
		<table id="homesearchtable">
			<tr>
				<td>Report Search</td>
				<td><input width="100%" type="text" onkeyup="search()" id="searchtext" value=""></input></td>
			</tr>
		</table>
	</div>
	<br>
	<div>
	<ul class="list-group">
			<%
			for(Report report: userReps){
				String title = report.getTitle();
				String descrption = report.getDescription();
				String repLink = user.getUsername()+":"+title;
				%>
	    			<li class="list-group-item" style="padding:0px">
	            			<a class="homepagelinks" title="Click to run report" alt="Click to run report" href="/lwr/report?name=<%=repLink%>">
	            				<h5 style="padding-left:10px;color:blue;font-weight: bold"><%=title%></h5>
	            			</a>
	            			<span class="repotlisttable" style="color:black;padding-left:10px"><%=descrption%></span>	
	        		</li>
				<%
			}
		%>
		</ul>
	</div>
</section>
<%@ include file="footer.jsp"%>