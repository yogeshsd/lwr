package com.lwr.software.reporter.reportmgmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.connmgmt.ConnectionFactory;
import com.lwr.software.reporter.admin.connmgmt.ConnectionPool;
import com.lwr.software.reporter.utils.DWHUtility;

public class Element {

    protected String id;
    
    protected String dbalias;
    
    protected int rownumber;
    
    protected int column;
    
    protected int row;
    
    protected String title;
    
    protected String query;
    
    protected String chartType;
    
    protected String position;
    
    protected List<Object> header;
    
    protected List<List<Object>> data;
    
	@JsonIgnore
    protected int maxRow;
    
	@JsonIgnore
    protected int maxColumn;
    
	@JsonIgnore
    protected List<List<Object>> processedData = new ArrayList<List<Object>>();
    
    @JsonIgnore
    protected Map<Integer,String> indexToDataTypeMap = new HashMap<Integer,String>();
    
    @JsonIgnore
    protected Map<String,List<Integer>> dataTypeToIndex = new HashMap<String,List<Integer>>();
    
    public int getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	public int getMaxColumn() {
		if(maxColumn==0)
			return 1;
		else
			return maxColumn;
	}

	public void setMaxColumn(int maxColumn) {
		this.maxColumn = maxColumn;
	}

	public List<List<Object>> getProcessedData() {
		return processedData;
	}

	public void setProcessedData(List<List<Object>> processedData) {
		this.processedData = processedData;
	}

	public Map<Integer, String> getIndexToDataTypeMap() {
		return indexToDataTypeMap;
	}

	public void setIndexToDataTypeMap(Map<Integer, String> indexToDataTypeMap) {
		this.indexToDataTypeMap = indexToDataTypeMap;
	}

	public Map<String, List<Integer>> getDataTypeToIndex() {
		return dataTypeToIndex;
	}

	public void setDataTypeToIndex(Map<String, List<Integer>> dataTypeToIndex) {
		this.dataTypeToIndex = dataTypeToIndex;
	}

	public List<Object> getHeader() {
		return header;
	}

	public void setHeader(List<Object> header) {
		this.header = header;
	}

	public List<List<Object>> getData() {
		return data;
	}

	public void setData(List<List<Object>> data) {
		if(data == null)
			return;
		this.data = data;
		processData();
	}
	
	public void init(){
		if (this.getQuery() == null)
			return;
		String sql = this.getQuery();
		String dbalias = this.getDbalias();
		Connection connection;
		
		if (dbalias == null){
			dbalias="default";
		}
		connection = ConnectionPool.getInstance().getConnection(dbalias);
		if (connection == null)
			return;
		DWHUtility utility = new DWHUtility(connection);
		System.out.println(sql);
		List<List<Object>> rows = new ArrayList<>();
		try {
			rows = utility.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rows == null || rows.isEmpty())
			return;
		if (rows.size() > 0) {
			List<Object> headers = rows.get(0);
			this.setHeader(headers);
		}
		rows.remove(0);
		this.setData(rows);
		ConnectionPool.getInstance().releaseConnection(connection, dbalias);
	}

	private void processData() {
		int index=0;
		dataTypeToIndex.clear();
		for (Object column : header) {
			String head = ((String)column).replaceAll("'", "");
			String hs[] = head.split(":");
			List<Integer> indices = dataTypeToIndex.get(hs[0]);
			if(indices == null){
				indices = new ArrayList<Integer>();
				dataTypeToIndex.put(hs[0], indices);
			}
			indices.add(index);
			indexToDataTypeMap.put(index, hs[0]);
			index++;
		}
		
		for (List<Object> row : data) {
			index=0;
			List<Object> modifiedRow = new ArrayList<Object>();
			List<Integer> indices = dataTypeToIndex.get(DashboardConstants.DATETIME);
			if(indices != null){
				for (Integer ind : indices) {
					index++;
					modifiedRow.add(row.get(ind));
				}
			}

			indices = dataTypeToIndex.get(DashboardConstants.STRING);
			if(indices != null){
				StringBuffer mergedString = new StringBuffer();
				for (Integer ind : indices) {
					if(index==0){
						modifiedRow.add(row.get(ind));
						index++;
					}else {
						mergedString.append(row.get(ind));
					}
				}
				if(!mergedString.toString().isEmpty())
					modifiedRow.add(mergedString);
			}

			indices = dataTypeToIndex.get(DashboardConstants.BOOLEAN);
			if(indices != null){
				for (Integer ind : indices) {
					index++;
					modifiedRow.add(row.get(ind));
				}
			}

			indices = dataTypeToIndex.get(DashboardConstants.NUMBER);
			if(indices != null){
				for (Integer ind : indices) {
					index++;
					modifiedRow.add(row.get(ind));
				}
			}
			processedData.add(modifiedRow);
		}
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDbalias() {
		return dbalias;
	}

	public void setDbalias(String dbalias) {
		this.dbalias = dbalias;
	}

	public int getRownumber() {
		return rownumber;
	}

	public void setRownumber(int rownumber) {
		this.rownumber = rownumber;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	
	public void clear(){
		if(this.header!=null)
			this.header.clear();
		if(this.data!=null)
			this.data.clear();
		if(this.indexToDataTypeMap!=null)
			this.indexToDataTypeMap.clear();
		if(this.dataTypeToIndex!=null)
			this.dataTypeToIndex.clear();
		if(this.processedData!=null)
			this.processedData.clear();
	}
	
}
