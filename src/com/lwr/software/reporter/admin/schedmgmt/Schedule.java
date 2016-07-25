package com.lwr.software.reporter.admin.schedmgmt;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.DashboardConstants.Destination;
import com.lwr.software.reporter.DashboardConstants.Frequency;
import com.lwr.software.reporter.DashboardConstants.OutputFormat;
import com.lwr.software.reporter.DashboardConstants.Status;

public class Schedule {

	private String name;
	
	private String reportName;
	
	private OutputFormat outputFormat = OutputFormat.HTML;
	
	private Destination destination = Destination.EMAIL;
	
	private long lastRunTime;
	
	private Status status = Status.UNKNOWN;
	
	private Frequency frequency = Frequency.DAILY;
	
	private Long interval;
	
	public Schedule(){
		
	}

	public Schedule(String scheduleName, String reportName, OutputFormat outputFormat, Destination destination,
			Frequency frequency, Long interval) {
		this.name=scheduleName;
		this.reportName=reportName;
		this.outputFormat=outputFormat;
		this.destination=destination;
		this.frequency=frequency;
		this.interval=interval;
	}

	public DashboardConstants.Frequency getFrequency() {
		return frequency;
	}


	public void setFrequency(DashboardConstants.Frequency freq) {
		this.frequency = freq;
	}


	public Long getInterval() {
		return interval;
	}


	public void setInterval(Long interval) {
		this.interval = interval;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public OutputFormat getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		Schedule inSched = (Schedule)obj;
		return inSched.getName().equalsIgnoreCase(this.getName());
	}
	
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
}
