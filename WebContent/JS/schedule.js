/**
 * 
 */
	function deleteSchedule(schedulename){
		var x = confirm("You are deleting schedule '"+schedulename+"'!");
		if(x){
			var request = $.ajax({
				url: "http://localhost:8080/lwr/rest/schedules/remove/"+schedulename,
				type: "POST",
				success: function(resp){  
					$('table#admintable tr#'+schedulename).remove();
					$("#savetext").html(resp);
				},
				error: function(e){  
					$("#savetext").html(e.responseText);
				}
			});
		}
	}
	function selectSchedule(row){
		var index = 0;
		$("#admintable tr").css('background','#FFFFFF');
		$(row).css('background','#8ec252')
		$(row).find('td').each(function(){
			var column = $(this);
			if(index == 0 ){
				$("#schedulename").val(column.html());
				index++;
			}else if(index == 1){
				$("#reportname").val(column.html());
				index++;
			}else if(index == 2){
				$("#format_select").val(column.html());
				index++;
			}else if(index == 3){
				$("#destination_select").val(column.html());
				index++;
			}else if(index == 4){
				$("#frequency_select").val(column.html());
				index++;
			}else if(index == 5){
				$("#interval").val(column.html());
				index++;
			}

		});
	}
	function saveSchedule(){
		var root=[];
		var schedules={};
		var schedulename = $("#schedulename").val();
		var reportname = $("#reportname").val();
		var format = $("#format_select").val();
		var destination = $("#destination_select").val();
		var frequency = $("#frequency_select").val();
		var interval = $("#interval").val();
		if( schedulename == ''){
			alert("Schedule name cannot be null!");
		}else if (reportname ==''){
			alert("Report name cannot be null!");
		}else if (interval ==''){
			alert("interval cannot be null!");
		}else{
			schedules["schedulename"]=schedulename;
			schedules["reportname"]=reportname;
			schedules["format"]=format;
			schedules["destination"]=destination;
			schedules["frequency"]=frequency;
			schedules["interval"]=interval;
			var request = $.ajax({
				url: "http://localhost:8080/lwr/rest/schedules/save?schedulename="+schedulename+"&reportname="+reportname+"&format="+format+"&destination="+destination+"&frequency="+frequency+"&interval="+interval,
				type: "POST",
				success: function(resp){
							var row = $("#"+schedulename);
							if( row.length == 0){
								var row = "<tr onclick=\"selectSchedule(this)\" id=\""+schedulename+"\"><td>"+schedulename+"</td><td>"+reportname+"</td><td>"+format+"</td><td>"+destination+"</td><td>"+frequency+"</td><td>"+interval+"</td><td><img src=\"/lwr/images/sign-delete.png\" onclick=\"deleteSchedule(\'"+schedulename+"\')\"></img></td></tr>";
								$("#admintable").append(row);
								$("#savetext").html(resp);
							}
						},
				error: function(e){
						$("#savetext").html(e.responseText);
					}
				});
		}
	}
	
	