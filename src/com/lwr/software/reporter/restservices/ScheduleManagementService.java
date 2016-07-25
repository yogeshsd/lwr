package com.lwr.software.reporter.restservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.DashboardConstants.OutputFormat;
import com.lwr.software.reporter.admin.schedmgmt.Schedule;
import com.lwr.software.reporter.admin.schedmgmt.ScheduleList;
import com.lwr.software.reporter.admin.schedmgmt.ScheduleManager;

@Path("/schedules/")
public class ScheduleManagementService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleList getSchedules(){
		ScheduleList scheduleList = new ScheduleList();
		Set<Schedule> schedules = ScheduleManager.getScheduleManager().getSchedules();
		scheduleList.setScheduleList(schedules);
		return scheduleList;
	}

	@Path("/{param1}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleList getUser(@PathParam("param1") String scheduleName){
		ScheduleList scheduleList = new ScheduleList();
		Schedule schedule = ScheduleManager.getScheduleManager().getSchedule(scheduleName);
		Set<Schedule> sl = new HashSet<Schedule>();
		sl.add(schedule);
		scheduleList.setScheduleList(sl);
		return scheduleList;
	}

	@Path("/remove/{param1}")
	@POST
	public Response removeUser(@PathParam("param1") String scheduleName){
		boolean status = ScheduleManager.getScheduleManager().removeSchedule(scheduleName);
		if(status)
			return Response.ok("Schedule '"+scheduleName+"' Deleted.").build();
		else
			return Response.serverError().entity("Schedule '"+scheduleName+"' cannot be deleted").build();
	}
	
	@Path("/save")
	@POST
	public Response updateSchedule(
			@QueryParam("schedulename") String scheduleName,
			@QueryParam("reportname") String reportName,
			@QueryParam("format") DashboardConstants.OutputFormat outputFormat,
			@QueryParam("destination") DashboardConstants.Destination destination,
			@QueryParam("frequency") DashboardConstants.Frequency frequency,
			@QueryParam("interval") Long interval){
		Schedule schedule = new Schedule(scheduleName,reportName,outputFormat,destination,frequency,interval);
		boolean status = ScheduleManager.getScheduleManager().saveSchedule(schedule);
		if(status)
			return Response.ok("Schedule '"+scheduleName+"' Saved.").build();
		else
			return Response.serverError().entity("Unable to save schedule '"+scheduleName+"'.").build();
	}
}
