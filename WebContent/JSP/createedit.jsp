<%@page import="com.lwr.software.reporter.reportmgmt.Report"%>
<%@page import="com.lwr.software.reporter.reportmgmt.ReportManager"%>
<%@page import="com.lwr.software.menu.MenuItem"%>
<%@page import="com.lwr.software.menu.MenuFactory"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="com.lwr.software.reporter.admin.connmgmt.ConnectionParams"%>
<%@page import="java.util.Set"%>
<%@page import="com.lwr.software.reporter.admin.connmgmt.ConnectionManager"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@ include file="header.jsp"%>

	<%
		Set<ConnectionParams> params = ConnectionManager.getConnectionManager().getConnectionParams();
		StringBuffer jsonData = new StringBuffer();
		JSONArray jsData=null;
		String description="";
		String reportName = name;
		String userName = (String)request.getSession().getAttribute("userName");
		if(name!=null){
			BufferedReader buffReader;
			if(userName.equalsIgnoreCase(DashboardConstants.PUBLIC_USER))
				buffReader = new BufferedReader(new FileReader(DashboardConstants.PUBLIC_REPORT_DIR+File.separatorChar+reportName));
			else
				buffReader = new BufferedReader(new FileReader(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+reportName));
			while(true){
				String line = buffReader.readLine();
				if(line == null)
					break;
				jsonData.append(line);
			}
			System.out.println(jsonData.toString());
			JSONParser parser = new JSONParser();
			jsData = (JSONArray)parser.parse(jsonData.toString());
			Report report = ReportManager.getReportManager().getReport(reportName,userName);
			description = report.getDescription();
		}else{
			name="";
		}
	%>
	
	<script type="text/javascript">
	function buildCell(index,rowIndex, columnIndex, title,sql,ctype,dbalias){
		var id = index+"_"+rowIndex+"_"+columnIndex;
		var html="<table id=\"createedittable\">";
		html=html+"		<tr height=\"5%\">";
		html=html+"			<th align=\"left\">Title</th>";
		html=html+"			<td>";
		html=html+"				<div>";
		html=html+"					<input type=\"text\" id=\""+id+"_title\" value=\""+title+"\"/>";
		html=html+"				</div>";
		html=html+"			</td>";
		html=html+"		</tr>";
		html=html+"		<tr height=\"80%\">";
		html=html+"			<th align=\"left\">SQL Query</th>";
		html=html+"			<td>";
		html=html+"				<div>";
		html=html+"					<textarea id=\""+id+"_sql\" form=\"formid\" rows=\"4\" name=\"query\" value=\""+sql+"\">"+sql;
		html=html+"					</textarea>";
		html=html+"				</div>";
		html=html+"			</td>";
		html=html+"		</tr>";
		html=html+"		<tr height=\"5%\">";
		html=html+"			<th align=\"left\">Chart Type</th>";
		html=html+"			<td>";
		html=html+"				<div>";
		html=html+"					<select id=\""+id+"_select\">";
		html=html+"						<option value=\"pie\">Pie Chart</option>";
		html=html+"						<option value=\"bar\">Bar Chart</option>";
		html=html+"						<option value=\"barstack\">Bar Stack Chart</option>";
		html=html+"						<option value=\"line\">Line Chart</option>";
		html=html+"						<option value=\"column\">Column Chart</option>";
		html=html+"						<option value=\"columnstack\">Column Stack Chart</option>";
		html=html+"						<option value=\"table\">Table Chart</option>";
		if(ctype=="pie"){
			html=html+"						<option value=\"pie\" selected>Pie Chart</option>";
		}
		else if(ctype=="bar"){
			html=html+"						<option value=\"bar\" selected>Bar Chart</option>";
		}
		else if(ctype=="barstack"){
			html=html+"						<option value=\"barstack\" selected>Bar Stack Chart</option>";
		}
		else if(ctype=="line"){
			html=html+"						<option value=\"line\" selected>Line Chart</option>";
		}
		else if(ctype=="column"){
			html=html+"						<option value=\"column\" selected>Column Chart</option>";
		}
		else if(ctype=="columnstack"){
			html=html+"						<option value=\"columnstack\" selected>Column Stack Chart</option>";
		}
		else if(ctype=="table"){
			html=html+"						<option value=\"table\" selected>Table Chart</option>";
		}
		else if(ctype=="annotatedline"){
			html=html+"						<option value=\"annotated\" selected>Annotated Chart</option>";
		}
		
		
		html=html+"					</select>";
		html=html+"				</div>";
		html=html+"			</td>";
		html=html+"		</tr>";
		html=html+"		<tr height=\"5%\">";
		html=html+"			<th align=\"left\">Connection Alias</th>";
		html=html+"			<td>";
		html=html+"				<div>";
		html=html+"				<select id=\""+id+"_conn\">";
		html=html+"					<option value=\"default\">default</option>";
		<%
			for(ConnectionParams param : params){
				String alias = param.getAlias();
				%>
				html=html+"					<option value=\"<%=alias%>\"><%=alias%></option>";
				<%
			}
		%>
		html=html+"					</select>";
		html=html+"				</div>";
		html=html+"			</td>";
		html=html+"		</tr>";
		html=html+"		<tr height=\"5%\" width=\"100%\">";
		html=html+"			<th style=\"background-color:white;\"></th><td><button onclick=\"runQuery("+index+","+rowIndex+","+columnIndex+")\">Test Query</td>";
		html=html+"		</tr>";
		html=html+"	</table>";
		return html;
	}
	</script>
	<script type="text/javascript">
			var jsData = <%=jsonData%>;
	</script>
	<section onload="buildPage(jsData)">
		<%
			if(!(name == null || name.isEmpty()))
			{
				%><a href="javascript:buildPage(jsData)">Load Report</a><%				
			}
		%>
		<br>
		Report Name <input type="text" id="dashname" name="dash-name" value="<%=reportName%>"></input>
		<hr>
		Report Description <input type="text" id="description" name="description" value="<%=description %>"></input>
		<hr>
			<table id="mytable">
				<tr id="row_0" class="outerrow">
					<td>
						<table id="innertable_0" class="innertable_withborder">
							<tr class="cellrow">
								<td class="cellcolumn" id="0_0_0_cell">
									<img src="/lwr/images/sign-edit.png" alt="Edit Report Cell" title="Edit Report Cell" onclick="populateCell(0,0,0,'','','')">
								</td>
							</tr>
						</table>
					</td>
				<td>
					<img align="middle" alt="Add Report Column" title="Add Report Column" src="/lwr/images/sign-add.png" onclick="addColumn(0)"></img>
				</td>
				</tr>
			</table>
			<div style="position:relative"><img align="right" alt="Add Report Row" title="Add Report Row" src="/lwr/images/sign-add.png" onclick="addRow()"></img>
			<img align="right" alt="Delete Report Column" title="Delete Report Column" src="/lwr/images/sign-remove.png" onclick="removeRow()"></img></div>
		</section>
<%@ include file="footer.jsp"%>