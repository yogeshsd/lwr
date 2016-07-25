<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="com.lwr.software.reporter.admin.connmgmt.ConnectionParams"%>
<%@page import="java.util.Set"%>
<%@page import="com.lwr.software.reporter.admin.connmgmt.ConnectionManager"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@include file="../JSP/header.jsp" %>
<section>
	<h4 style="padding-top:5px">Connection(s) List</h4>
	<table class="admincelltable" id="admintable">
		<tr>
			<th>Alias</th>
			<th>Username</th>
			<th>Password</th>
			<th>Driver</th>
			<th>URL</th>
			<th>Delete</th>
			<th>Status</th>
		</tr>
		<%
			ConnectionManager manager = ConnectionManager.getConnectionManager();
			Set<ConnectionParams> connParams = manager.getConnectionParams();
			for(ConnectionParams params : connParams){
				%>
				<tr id="<%=params.getAlias() %>" onclick="selectConnection(this)">
				<%
					if(params.getIsDefault().equalsIgnoreCase("true")){
						%>
						<td><%=params.getAlias() %><img alt="Default Connection" src="/lwr/images/sign-greentick.png"></td>
						<%
					}else{
						%>
						<td><%=params.getAlias() %></td>
						<%
					}
				%>
					<td><%=params.getUsername() %></td>
					<td><%=params.getPassword() %></td>
					<td><%=params.getDriver() %></td>
					<td><%=params.getUrl() %></td>
					<td style="min-width:30px"><img src="/lwr/images/sign-delete.png" onclick="deleteConnection('<%=params.getAlias()%>')"></img></td>
					<%
						if(params.getIsConnectionSuccess()!=null && params.getIsConnectionSuccess().equalsIgnoreCase("true")){
							%>
							<td id="<%=params.getAlias()%>_conn" style="min-width:30px"><span class="label label-success">Success</span></td>
							<%
						}else{
							%>
							<td id="<%=params.getAlias()%>_conn" style="min-width:30px"><span class="label label-danger">Failed</span></td>
							<%
						}
					%>
				</tr>
				<%
			}
		%>
	</table>
	<br>
	<hr>
	<h4>Create/Edit Connection</h4>
	<table style="width:50%" class="admincellinputtable">
		<tr>
			<td>
				Connection Alias
			</td>
			<td>
				<input name="alias" id="alias" type="text"></input>
			</td>
		</tr>
		<tr>
			<td>
				Default Connection
			</td>
			<td style="text-align: left;">
				<input style="width: 20px;text-align: left;" name="isdefault" id="isdefault" type="checkbox"></input>
			</td>
		</tr>
		<tr>
			<td>
				User Name
			</td>
			<td>
				<input name="username" id="username" type="text"></input>
			</td>
		</tr>
		<tr>
			<td>
				Password
			</td>
			<td>
				<input name="password" id="password" type="password"></input>
			</td>
		</tr>
		<tr>
			<td>
				Database Driver
			</td>
			<td>
				<input name="driver" id="driver" type="text"></input>
			</td>
		</tr>
		<tr>
			<td>
				Database URL
			</td>
			<td>
				<input name="url" id="url" type="text"></input>
			</td>
		</tr>
	</table>
	<br>
	<button type="button" onclick="saveConnection()" class="btn btn-primary" style="background:#679BB7">Submit</button>
	<button type="reset" onclick="saveUser()" class="btn btn-primary" style="background:#679BB7">Reset</button>
	<button type="button" onclick="testConnection()" class="btn btn-primary" style="background:#679BB7">Test Connection</button>
	<br>
	<br>
	<hr>
	<br>
	<h4 id="savetext" align="center" style="color:#1a398a"></h4>
</section>
<%@include file="../JSP/footer.jsp"%>