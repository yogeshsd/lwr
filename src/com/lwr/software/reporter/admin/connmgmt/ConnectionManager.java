package com.lwr.software.reporter.admin.connmgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;

public class ConnectionManager {

	private static volatile ConnectionManager manager;
	
	private Set<ConnectionParams> connParams = new HashSet<ConnectionParams>();
	
	private String connParamFileName = DashboardConstants.PATH+File.separatorChar+"dashboard"+File.separatorChar+"drivers.json";
	
	static{
		File dir = new File(DashboardConstants.PATH+File.separatorChar+"dashboard");
		dir.mkdirs();
	}
	
	public static ConnectionManager getConnectionManager(){
		if(manager == null){
			synchronized (ConnectionManager.class) {
				if(manager == null){
					manager = new ConnectionManager();
				}
			}
		}
		return manager;
	}
	
	private ConnectionManager(){
		init();
	}
	
	private void init(){
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        TypeFactory typeFactory = objectMapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, ConnectionParams.class);
	        connParams =  objectMapper.readValue(new File(connParamFileName), collectionType);
	        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(connParams));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean saveConnectionParams(ConnectionParams params){
		try{
			if(connParams.contains(params)){
				connParams.remove(params);
				connParams.add(params);
			}
			connParams.add(params);
			if(params.getIsDefault().equalsIgnoreCase("true")){
				for (ConnectionParams connParam : connParams) {
					if(connParam.getIsDefault().equalsIgnoreCase("true") && !connParam.equals(params))
						connParam.setIsDefault("false");
				}
			}
			serializeConnectionParams();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public Set<ConnectionParams> getConnectionParams(){
		return this.connParams;
	}
	
	public ConnectionParams getConnectionParams(String alias){
		ConnectionParams defConnParam = null;
		boolean isFirst = true;
		for (ConnectionParams connectionParams : connParams) {
			if(isFirst){
				defConnParam = connectionParams;
				isFirst = false;
			}
			if(connectionParams.getAlias().equalsIgnoreCase(alias) || (connectionParams.getIsDefault().equalsIgnoreCase("true") && alias.equalsIgnoreCase("default")))
				return connectionParams;
		}
		if(alias.equalsIgnoreCase("default"))
			return defConnParam;
		return null;
	}

	
	private void serializeConnectionParams(){
		try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(connParams);
	        FileWriter writer = new FileWriter(connParamFileName);
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean removeConnection(String alias) {
		ConnectionParams paramToDelete = null;
		for (ConnectionParams param : connParams) {
			if(param.getAlias().equalsIgnoreCase(alias)){
				paramToDelete = param;
				break;
			}
		}
		if(paramToDelete == null)
			return false;
		connParams.remove(paramToDelete);
		serializeConnectionParams();
		return true;
	}
}