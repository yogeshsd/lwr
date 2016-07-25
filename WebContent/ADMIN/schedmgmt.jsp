<%@page import="com.lwr.software.reporter.DashboardConstants.OutputFormat"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Destination"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Frequency"%>
<%@page import="com.lwr.software.reporter.admin.schedmgmt.Schedule"%>
<%@page import="com.lwr.software.reporter.admin.schedmgmt.ScheduleManager"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Role"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@include file="../JSP/header.jsp" %>

<script type="text/javascript">
	$(document).ready(function(){
	    $('reportname').typeahead({
	      name: 'reports',
	      local: ['Audi', 'BMW', 'Bugatti', 'Ferrari', 'Ford', 'Lamborghini', 'Mercedes Benz', 'Porsche', 'Rolls-Royce', 'Volkswagen']
	    });
	}); 
</script>
<section>
	<h4 style="padding-top:5px">Schedule(s) List</h4>
	<table class="admincelltable" id="admintable">
		<tr>
			<th>Schedule Name</th>
			<th>Report Name</th>
			<th>Output Format</th>
			<th>Destination</th>
			<th>Frequency</th>
			<th>Interval</th>
			<th>Delete</th>
		</tr>
		<%
			ScheduleManager manager = ScheduleManager.getScheduleManager();
			Set<Schedule> schedules = manager.getSchedules();
			for(Schedule s : schedules){
				%>
				<tr onclick="selectSchedule(this)" id="<%=s.getName()%>">
					<td><%=s.getName() %></td>
					<td><%=s.getReportName() %></td>
					<td><%=s.getOutputFormat() %></td>
					<td><%=s.getDestination() %></td>
					<td><%=s.getFrequency() %></td>
					<td><%=s.getInterval()%></td>
					<td style="min-width:30px"><img src="/lwr/images/sign-delete.png" onclick="deleteSchedule('<%=s.getName()%>')"></img></td>
				</tr>
				<%
			}
		%>
	</table>
	<br>
	<hr>
	<h4>Create/Edit Schedule</h4>
	<table style="width:50%" class="admincellinputtable">
		<tr>
			<td>Schedule Name</td>
			<td>
				<input name="schedulename" id="schedulename" type="text"></input>
			</td>
		</tr>
		<tr>
			<td>Report Name</td>
			<td>
				<input name="reportname" id="reportname" type="text"></input>
			</td>
		</tr>
		<tr>
			<td>Output Format</td>
			<td>
				<select id="format_select">
				<%
					OutputFormat[] formats = OutputFormat.values();
					for(int i=0;i<formats.length;i++){
						%>
							<option value="<%=formats[i] %>"><%=formats[i] %></option>
						<%							
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Destination</td>
			<td>
				<select id="destination_select">
				<%
					Destination[] destinations = Destination.values();
					for(int i=0;i<destinations.length;i++){
						%>
							<option value="<%=destinations[i] %>"><%=destinations[i] %></option>
						<%							
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Frequency</td>
			<td>
				<select id="frequency_select">
				<%
					Frequency[] frequencies = Frequency.values();
					for(int i=0;i<frequencies.length;i++){
						%>
							<option value="<%=frequencies[i] %>"><%=frequencies[i] %></option>
						<%							
					}
				%>
				</select>
			</td>			
		</tr>
		
		<tr>
			<td>Interval</td>
			<td>
				<input name="ineterval" id="interval" type="text"></input>
			</td>
		</tr>
		
	</table>
	<br>
	<button type="button" onclick="saveSchedule()" class="btn btn-primary" style="background:#679BB7">Submit</button>
	<button type="reset" class="btn btn-primary" style="background:#679BB7">Reset</button>
	<br>
	<br>
	<hr>
	<br>
	<h4 id="savetext" align="center" style="color:#1a398a"></h4>
</section>
<%@include file="../JSP/footer.jsp" %>