package com.lwr.software.reporter.admin.schedmgmt;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;

public class ScheduleManager {

	private static volatile ScheduleManager manager;
	
	private Set<Schedule> schedules = new HashSet<Schedule>();
	
	static{
		File configDir = new File(DashboardConstants.CONFIG_PATH);
		configDir.mkdirs();
	}
	
	private String fileName = DashboardConstants.CONFIG_PATH+"schedules.json";
	
	public static ScheduleManager getScheduleManager(){
		if(manager == null){
			synchronized (ScheduleManager.class) {
				if(manager == null){
					manager = new ScheduleManager();
				}
			}
		}
		return manager;
	}
	
	private ScheduleManager(){
		init();
	}
	
	private void init(){
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        TypeFactory typeFactory = objectMapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Schedule.class);
	        schedules =  objectMapper.readValue(new File(fileName), collectionType);
	        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schedules));
	    } catch (Throwable e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean saveSchedule(Schedule schedule){
		boolean isSaved=true;
		if(schedules.contains(schedule)){
			schedules.remove(schedule);
			schedules.add(schedule);
		}else{
			schedules.add(schedule);
		}
		serialize();
		return isSaved;
	}
	
	public Schedule getSchedule(String name){
		for (Schedule schedule : schedules) {
			if(schedule.getScheduleName().equalsIgnoreCase(name))
				return schedule;
		}
		return null;
	}
	
	public boolean removeSchedule(String name){
		boolean toReturn = false;
		for (Schedule schedule : schedules) {
			if(schedule.getScheduleName().equalsIgnoreCase(name)){
				toReturn = schedules.remove(schedule);
				break;
			}
		}
		serialize();
		return toReturn;
	}
	
	private void serialize(){
		try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schedules);
	        FileWriter writer = new FileWriter(fileName);
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Set<Schedule> getSchedules() {
		return this.schedules;
	}
}