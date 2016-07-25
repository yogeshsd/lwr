<%@page import="com.lwr.software.reporter.admin.usermgmt.User"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.UserManager"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="javax.swing.text.StyledEditorKit.ForegroundAction"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@include file="header.jsp" %>
<section>
	<br>
	<br>
		<div class="panel panel-primary">
			<div class="panel-heading" style="color:#679BB7">
    		    <h6 class="panel-title" style="color:white">User Preferences</h6>
    		</div>
			<table style="width:50%">
				<tr>
					<td>
						User Display Name
					</td>
					<td>
						<input name="displayname" id="displayname" type="text" value="<%=user.getDisplayName()%>"></input>
					</td>
				</tr>
				<tr>
					<td>
						User Name
					</td>
					<td>
						<input name="username" id="username" type="text" value=<%=user.getUsername() %>></input>
					</td>
				</tr>
				<tr>
					<td>
						User Role
					</td>
					<td>
						<input name="role" id="role" type="text" value=<%=user.getRole() %> readonly></input>
					</td>
				</tr>
			</table>
		</div>
		<div class="panel panel-primary">
			<div class="panel-heading" style="color:#679BB7">
    		    <h4 class="panel-title" style="color:white">Report Preferences</h4>
    		</div>
			<table style="width:50%">
				<tr>
					<td>
						Chart Preference
					</td>
					<td>
						<select id="chartoption">
							<%
								if(user.getChartType()==null || user.getChartType().equalsIgnoreCase(DashboardConstants.HTML_GOOGLE)){
									%>
										<option value="<%=DashboardConstants.HTML_GOOGLE %>" selected>Google Charts</option>
										<option value="<%=DashboardConstants.HTML_JFREE %>" >JFree Charts</option>
									<%
								}else{
									%>
										<option value="<%=DashboardConstants.HTML_GOOGLE %>" >Google Charts</option>
										<option value="<%=DashboardConstants.HTML_JFREE %>" selected>JFree Charts</option>
									<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						Refresh Interval
					</td>
					<td>
						<input name="refreshInterval" id="refreshInterval" type="text" value="<%=user.getRefreshInterval()%>"></input>
					</td>
				</tr>
			</table>
		</div>

		<div class="panel panel-primary">
			<div class="panel-heading" style="color:#679BB7">
    		    <h6 class="panel-title" style="color:white">Email Preferences</h6>
    		</div>
			<table style="width:50%">
				<tr>
					<td>
						SMTP Server Name
					</td>
					<td>
						<input name="smtphost" id="smtphost" type="text" value="<%=user.getRefreshInterval()%>"></input>
					</td>
				</tr>
				<tr>
					<td>
						SMTP Host Name
					</td>
					<td>
						<input name="smtpport" id="smtpport" type="text" value="<%=user.getRefreshInterval()%>"></input>
					</td>
				</tr>
				<tr>
					<td>
						Reciepents Address(s) ( Semicolon separate)
					</td>
					<td>
						<input name="recptaddr" id="recptaddr" type="text" value="<%=user.getRefreshInterval()%>"></input>
					</td>
				</tr>
			</table>
		</div>
		<input name="password" id="password" type="hidden" value=<%=user.getPassword()%>></input>
	<br>
	<button type="button" onclick="saveProfile()">Submit</button>
	<button type="reset">Reset</button>
	<h4 id="savetext" align="center" style="color:blue"></h4>
</section>
<%@include file="footer.jsp" %>