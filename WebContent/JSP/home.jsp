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
<ul id="homenavtabs" class="nav nav-tabs">
	<li id="public_li_id" class="active"><a href="javascript:showTab('public');">Public Reports</a></li>
	<li id="personal_li_id"><a href="javascript:showTab('personal');">Personal Reports</a></li>
	<li id="schedule_li_id"><a href="javascript:showTab('schedule');">Scheduled Reports</a></li>
</ul>
<script>
	$(document).ready(function(){
		showTab('public');
	})
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
	function showTab(tabName){
		if(tabName == "public"){
			$("#public_li_id").attr('class', 'active');
			$("#personal_li_id").attr('class', '');
			$("#schedule_li_id").attr('class', '');
		}else if(tabName == "personal"){
			$("#personal_li_id").attr('class', 'active');
			$("#public_li_id").attr('class', '');
			$("#schedule_li_id").attr('class', '');
		}else if(tabName == "schedule"){
			$("#schedule_li_id").attr('class', 'active');
			$("#public_li_id").attr('class', '');
			$("#personal_li_id").attr('class', '');
		}
		$("#report_div").html("<table style=\"width:100%;height:50%;border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"/lwr/images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
		var userName = "public";		
		if(tabName == "personal" || tabName == "schedule"){
			userName=$("#usernamehidden").val();
		}
		if(tabName == "schedule"){
			$.ajax({
				  url: '/lwr/rest/schedules/instances/'+userName,
				  type: 'GET',
				  dataType: 'JSON',
				  success: function(data) {
					  var htmlData="<table class=\"admincelltable\">";
						htmlData = htmlData +"<tr>";
						htmlData = htmlData +"<th>Schedule Name</th>";
						htmlData = htmlData +"<th>Report Name</th>";
						htmlData = htmlData +"<th>Status</th>";
						htmlData = htmlData +"<th>Last Run Time</th>";
						htmlData = htmlData +"<th>Next Run Time</th>";
						htmlData = htmlData +"</tr>";
						
						for (i = 0; i < data.length; i++) {
							var schedule = JSON.parse(data[i]);
							htmlData = htmlData +"<tr>";
							htmlData = htmlData +"<td>"+schedule.scheduleName+"</td>";
							htmlData = htmlData +"<td class=\"repotlisttable\">"+schedule.reportName+"</td>";
							htmlData = htmlData +"<td>"+schedule.status+"</td>";
							
							var date = new Date(schedule.lastRunTime);
							var dateString = (date.getDate()<10?"0"+date.getDate():date.getDate())
							+"-"+((date.getMonth()+1)<10?"0"+(date.getMonth()+1):(date.getMonth()+1))
							+"-"+date.getFullYear()
							+"-"+(date.getHours()<10?"0"+date.getHours():date.getHours())
							+"-"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes());
							var fileName = schedule.scheduleName+"-"+schedule.reportName+"-"+dateString+".html";

							date = new Date(schedule.nextRunTime);
							var nextDateString = (date.getDate()<10?"0"+date.getDate():date.getDate())
							+"-"+((date.getMonth()+1)<10?"0"+(date.getMonth()+1):(date.getMonth()+1))
							+"-"+date.getFullYear()
							+"-"+(date.getHours()<10?"0"+date.getHours():date.getHours())
							+"-"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes());

							
							htmlData = htmlData +"<td><a href=\"/lwr/showreport?filename="+fileName+"\">"+dateString+"</a></td>";
							htmlData = htmlData +"<td>"+nextDateString+"</td>";
							htmlData = htmlData +"</tr>";
						}
						htmlData = htmlData+"</table>";
						$("#report_div").html(htmlData);
				  }
				});
		}else{
			$.ajax({
				  url: '/lwr/rest/reports',
				  type: 'GET',
				  dataType: 'JSON',
				  data: {"userName":userName},
				  success: function(data) {
					  var htmlData="";
						for (i = 0; i < data.length; i++) { 
							var report = JSON.parse(data[i]);
							htmlData = htmlData+"<li class=\"list-group-item\" style=\"padding:0px\">";
							htmlData = htmlData+"<a class=\"homepagelinks\" title=\"Click to run report\" alt=\"Click to run report\" href=\"/lwr/report?name="+report.title+"&userName="+userName+"\">";
							htmlData = htmlData+"<h5 style=\"padding-left:10px;color:blue;font-weight: bold\">"+report.title+"</h5>";
							htmlData = htmlData+"</a>";
							htmlData = htmlData+"<span class=\"repotlisttable\" style=\"color:black;padding-left:10px\">"+report.description+"</span>";	
							htmlData = htmlData+"</li>";
						}
						$("#report_div").html(htmlData);
				  }
				});
		}
	}
</script>
<section id="report_section">
	<div align="right">
		<table id="homesearchtable">
			<tr>
				<td>Report Search</td>
				<td><input width="100%" type="text" onkeyup="search()" id="searchtext" value=""></input></td>
			</tr>
		</table>
	</div>
	<br>
	<div id="report_div">
	</div>
</section>
<%@ include file="footer.jsp"%>