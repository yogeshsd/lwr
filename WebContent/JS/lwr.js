/**
 * 
 */
function addRow() {
	var rowIndex = $("#mytable .outerrow").last().index()+1;
	var rowId = rowIndex+"_0_0_cell";
	var row = "<tr class=\"outerrow\" id=\"row_"+rowIndex+"\"><td><table id=\"innertable_"+rowIndex+"\" class=\"innertable_withborder\"><tr class=\"cellrow\"><td class=\"cellcolumn\" id=\""+rowId+"\"><img src=\"/lwr/images/sign-edit.png\" alt=\"Edit Report Cell\" onclick=\"populateCell("+rowIndex+",0,0,'','','')\"></td></tr></table></td><td><img src=\"/lwr/images/sign-add.png\" alt=\"Add Report Column\" title=\"Add Report Column\" onclick=\"addColumn("+rowIndex+")\"></img></td></tr>";
	$("#mytable").append(row);
}

function removeRow() {
	var rowIndex = $("#mytable").find(".outerrow").last().index();
	var x = confirm("You are deleting row "+rowIndex);
	if(x){
		$('#row_'+rowIndex).remove();
	}
}

function addColumn(inRowIndex){
	var rows = $("#innertable_"+inRowIndex).find(".cellrow");
	$(rows).each(function() {
		var row = $(this);
		var columns = $(row).find(".cellcolumn");
		var columnIndex = $(columns).last().index()+1;
		var rowIndex = 0;
		var column = "<td class=\"cellcolumn\" id=\""+inRowIndex+"_"+rowIndex+"_"+columnIndex+"_cell\"><img src=\"/lwr/images/sign-edit.png\" alt=\"Edit Report Cell\" title=\"Edit Report Cell\" onclick=\"populateCell("+inRowIndex+","+rowIndex+","+columnIndex+",'','','')\"></img></td>";
		row.append(column);
	});
}

function removeColumn(inRowIndex){
	var rows = $("#innertable_"+inRowIndex+" tr");
	$(rows).each(function() {
		var row = $(this);
		var columns = $(row).find("td");
		var columnIndex = $(columns).index();
		var rowIndex = $(row).index();
		$('#row_'+rowIndex+'_column_'+columnIndex).remove();
	});
}

function populateCell(index,rowIndex,columnIndex,title,sql,ctype){
	var cell = buildCell(index,rowIndex,columnIndex,title,sql,ctype);
	var id = "#"+index+"_"+rowIndex+"_"+columnIndex+"_cell";
	$(id).html(cell);
}

function buildPage(json){
	var table = $("#mytable");
	table.empty();
	var myArr = JSON.parse(JSON.stringify(json));
	var maxrows = myArr[0].maxrows;
	for(var i=0;i<maxrows;i++){
		var row = myArr[0].rows[i];
		addRow();
		var numcols = row.elements.length;
		for(var j=0;j<numcols;j++){
			if(j>0)
				addColumn(i);
			var column = row.elements[j];
			var rowNum = column.rownumber;
			var rowId = column.row;
			var columnId = column.column;
			populateCell(rowNum,rowId,columnId,column.title,column.query,column.chartType);
		}
	}
}

function save(mode){
	var username = "public";
	if(mode == "personal"){
		username=$("#usernamehidden").val();
	}
	var dashname = $("#dashname").val();
	var description = $("#description").val();
	if(dashname !== ''){
		var rowNumber=0;
		var rows = $("#mytable .outerrow");
		var rowsJson=[];
		$(rows).each(function() {
			var row = $(this);
			if(row !== null){
				var innerrow = $("#innertable_"+rowNumber+" .cellrow");
				var elementsJson={};
				var innercolumns = $(innerrow).find("td.cellcolumn");
				var columnIndex=0;
				var rowJson=[];
				$(innercolumns).each(function(){
					var cell = $(this);
					var id = $(cell).attr("id");
					var title = $(cell).find("input").val();
					var query = $(cell).find("textarea").val();
					var ctype = $("#"+rowNumber+"_0_"+columnIndex+"_select").val();
					var dbalias = $("#"+rowNumber+"_0_"+columnIndex+"_conn").val();
					var data={};
					data["title"]=title;
					data["query"]=query;
					data["chartType"]=ctype;
					data["id"]=id;
					data["rownumber"]=rowNumber;
					data["row"]=0;
					data["column"]=columnIndex;
					data["dbalias"]=dbalias;
					rowJson[columnIndex]=data;
					columnIndex++;
				})
			}
			elementsJson["elements"]=rowJson;
			rowsJson[rowNumber]=elementsJson;
			rowNumber++;
		});
		var dashboard={};
		dashboard["title"]=dashname;
		dashboard["description"]=description;
		dashboard["maxrows"]=rowNumber;
		dashboard["rows"]=rowsJson;
		var reportName = username+":"+dashname;
		var root=[];
		root[0]=dashboard;
		var request = $.ajax({
			url: "/lwr/rest/reports/save",
			type: "POST",
			dataType: "html",
			data: {"components":JSON.stringify(root),"dashboardname":reportName},
			success: function(resp){
					alert(resp+". Go to Home Page to view the report");	
				},
			error: function(e,status,error){
				    alert("Error Saving "+error+status+e);
				}
			});
	}else{
		alert('Name cannot be null!');
	}
}

function runQuery(row,rowIndex,columnIndex){
	var id = row+"_"+rowIndex+"_"+columnIndex;
	var sqlTextArea =$("#"+id+"_sql").val();
	var dbalias = $("#"+id+"_conn").val();
	var chartType = $("#"+id+"_select").val();
	var request = $.ajax({
		url: "/lwr/rest/query",
		type: "GET",
		data: {"sql":sqlTextArea,"charttype":chartType,"dbAlias":dbalias},
		success: function(resp) {
				var x=window.open("","","width=400, height=300");
				x.document.open();
				x.document.write(resp);
				x.document.close();
			},
		error: function(e,status,error){
			    alert("Unable to query. Error "+error);
			}
		});
}

function runQueryDash(innerRowIndex,rowIndex,columnIndex,dashBoardName){
	var request = $.ajax({
		url: "/lwr/rest/query/report",
		type: "GET",
		data: {
			"reportName":dashBoardName,
			"rowIndex":rowIndex,
			"innerRowIndex":innerRowIndex,
			"columnIndex":columnIndex},
		success: function(resp) {
				var x=window.open("","","width=400, height=300");
				x.document.open();
				x.document.write(resp);
				x.document.close();
			},
		error: function(e,status,error){
			    alert("Unable to query. Error "+error);
			}
		});
}

function refreshElement(id,reportName,elementName,timeout){
	setInterval(function() {
		var height = $(id).height();
		var width = $(id).width();
		$(id).html("<table style=\"width:"+width+";height:"+height+";border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"/lwr/images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
		var request = $.ajax({
			url: "/lwr/rest/reports/element",
			type: "GET",
			data: {
				"reportName":reportName,
				"elementName":elementName,
				"chartType":"html_jfree"},
			success: function(resp) {
					$(id).html(resp);
				},
			error: function(e,status,error){
				    alert("Unable to query. Error "+error);
				}
			});
	},timeout);
}


function loadElement(id,reportName,elementName){
	var height = $(id).height();
	var width = $(id).width();
	$(id).html("<table style=\"width:"+width+";height:"+height+";border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"/lwr/images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
	var request = $.ajax({
		url: "/lwr/rest/reports/element",
		type: "GET",
		data: {
			"reportName":reportName,
			"elementName":elementName,
			"chartType":"html_jfree"},
		success: function(resp) {
				$(id).html(resp);
			},
		error: function(e,status,error){
			    alert("Unable to query. Error "+error);
			}
	});
}
