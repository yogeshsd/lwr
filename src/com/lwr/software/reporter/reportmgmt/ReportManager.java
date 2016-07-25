package com.lwr.software.reporter.reportmgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;

public class ReportManager {
	
	private static ReportManager manager;
	
	private String reportsDir = DashboardConstants.PATH+File.separatorChar+"dashboard";
	
	private Map<String,Report> reportMap = new LinkedHashMap<String,Report>();

	static{
		File dir = new File(DashboardConstants.PATH+File.separatorChar+"dashboard");
		dir.mkdirs();
	}
	
	public static ReportManager getReportManager(){
		if(manager == null){
			synchronized (ReportManager.class) {
				if(manager == null){
					manager = new ReportManager();
				}
			}
		}
		return manager;
	}
	
	private ReportManager(){
		init();
	}

	private void init() {
		File dir = new File(reportsDir);
		String reportFiles[] = dir.list();
		for(String reportFile : reportFiles){
			if(reportFile.equals("drivers.json") || reportFile.equals("users.json"))
				continue;
		    try {
		    	ObjectMapper objectMapper = new ObjectMapper();
		        TypeFactory typeFactory = objectMapper.getTypeFactory();
		        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Report.class);
		        Set<Report> reports =  objectMapper.readValue(new File(dir.getAbsolutePath()+File.separatorChar+reportFile), collectionType);
		        for (Report report : reports) {
		        	System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(report));
		        	int maxRows = report.getmaxrows();
		        	List<RowElement> rowElements = report.getRows();
		        	for (RowElement rowElement : rowElements) {
		        		List<Element> elements = rowElement.getElements();
		        		if(elements == null || elements.isEmpty())
		        			continue;
		        		int maxCols = rowElement.getElements().size();
		        		for (Element element : elements) {
							element.setMaxColumn(maxCols);
							element.setMaxRow(maxRows);
						}
					}
		        	reportMap.put(report.getTitle(), report);
				}
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}

	public Report getReport(String reportTitle) {
		return reportMap.get(reportTitle);
	}

	private boolean serializeReport(String components,String dashboardname){
		try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	TypeFactory typeFactory = objectMapper.getTypeFactory();
	    	CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Report.class);
	    	Set<Report> reports =  objectMapper.readValue(components, collectionType);
	    	for (Report report : reports) {
	        	int maxRows = report.getmaxrows();
	        	List<RowElement> rowElements = report.getRows();
	        	for (RowElement rowElement : rowElements) {
	        		List<Element> elements = rowElement.getElements();
	        		if(elements == null || elements.isEmpty())
	        			continue;
	        		int maxCols = rowElement.getElements().size();
	        		for (Element element : elements) {
						element.setMaxColumn(maxCols);
						element.setMaxRow(maxRows);
					}
				}
				reportMap.put(report.getTitle(), report);
	    	}
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reports);
	        FileWriter writer = new FileWriter(reportsDir+File.separatorChar+dashboardname);
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
	        return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveReport(String components,String dashboardname) {
		return serializeReport(components,dashboardname);
	}

	public Set<Report> getReports() {
		Set<Report> reps = new LinkedHashSet<Report>();
		Collection<Report> reports = this.reportMap.values();
		for (Report report : reports) {
			reps.add(report);
		}
		return reps;
	}
	
	public void reload(){
		init();
	}
}
