<%@page import="com.lwr.software.reporter.admin.usermgmt.UserManager"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.User"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=DashboardConstants.PRODUCT_NAME %></title>
<link rel="stylesheet" type="text/css" href="/lwr/CSS/lwr-dash.css">
<link rel="stylesheet" href="/lwr/CSS/bootstrap.min.css">
<link rel="stylesheet" href="/lwr/CSS/bootstrap-theme.min.css">
<script type="text/javascript" src="JS/jquery.min.js"></script>
<script type="text/javascript" src="JS/lwr.js"></script>
<script type="text/javascript" src="JS/connection.js"></script>
<script type="text/javascript" src="JS/user.js"></script>
<script type="text/javascript" src="JS/schedule.js"></script>
<script type="text/javascript" src="JS/angular.js"></script>
<script type="text/javascript" src="JS/bootstrap.min.js"></script>
<script type="text/javascript" src="JS/bootstrap-typeahead.js"></script>

<link rel="shortcut icon" href="images/lwr_logo.png">
</head>
<body class="no-js">
<header>
	<table>
		<tr>
			<td width="5%">
				<img src="/lwr/images/lwr_logo.png" alt="" height="60">
			</td>
			<td style="vertical-align:middle;padding-left:50px" width="92%">
				<font size="6" color="white"><%=DashboardConstants.PRODUCT_NAME %></font>
			</td>
			<%
				Object loginName = request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
				if(loginName == null){
					loginName = "";
				}
				
			%>
		</tr>
	</table>
</header>
<nav>
<div>
	<ul class="nav nav-tabs" style="border-color:#ffffff;padding:5px">
	<%
		String name = (String)request.getParameter("name");
		UserSecurityContext context = (UserSecurityContext) request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
		User user = null;
		if(context != null){
			user = UserManager.getUserManager().getUser(context.getUserName());
			List<MenuItem> menus = MenuFactory.getInstance().getMenu(context.getRole(),request.getRequestURI());
			int i =0;
			for(MenuItem menu : menus){
				i++;
				List<MenuItem> subMenus = menu.getSubMenuItems();
				if( subMenus !=null && !subMenus.isEmpty()){
					%>
					 		<li class="dropdown">
					 			<a href="#" data-toggle="dropdown" class="dropdown-toggle"><%=menu.getMenuLabel() %><b class="caret" style="margin-left: 5px;"></b></a>
					 		<ul class="dropdown-menu">
					<%
					for(MenuItem subMenu : subMenus){
						if(subMenu.isUseReportParam()){
							String action = subMenu.getMenuAction();
							if(action.contains("="))
								action=action+"&name=";
							else
								action=action+"?name=";
							%>
								<li><a href="<%=action%><%=name%>"><%=subMenu.getMenuLabel() %></a></li>
							<%
						}else{
							%>
								<li><a href="<%=subMenu.getMenuAction()%>"><%=subMenu.getMenuLabel() %></a></li>
							<%
						}
					}
					%>
							</ul>
							</li>
					<%
				}else{
					if(menu.isUseReportParam()){
						String action = menu.getMenuAction();
						if(action.contains("="))
							action=action+"&name=";
						else
							action=action+"?name=";
						%>
							<li><a class="dropdown-toggle" href="<%=action%>"><%=menu.getMenuLabel()%></a></li>
						<%
					}else{
							%>							
							<li>
								<a class="dropdown-toggle" href="<%=menu.getMenuAction()%>"><%=menu.getMenuLabel()%></a>
							</li>
							<%
					}
				}
			}
		}
	if(user != null){
		%>
			<li class="dropdown pull-right">
				<a href="#" data-toggle="dropdown" class="dropdown-toggle"><%=user.getDisplayName()%><b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="/lwr/profile">Profile</a></li>
					<li><a href="/lwr/logout">Logout</a></li>
				</ul>
			</li>
		<%		
	}
	%>
	<input id="usernamehidden" type="hidden" name="<%=user.getUsername()%>" value="<%=user.getUsername()%>"></input>
</ul>
</div>
</nav>
