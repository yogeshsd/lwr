package com.lwr.software.reporter.renderer.html.google;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;

public abstract class HTMLAbstractRenderer implements IElementRenderer{
	
	public Element element;
	
	public abstract String render();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
	
	protected Map<Integer,String> headerIndexToTypeMap = new HashMap<Integer,String>();
	
	protected Map<Integer,String> indexToName = new HashMap<Integer,String>();
	
	protected Map<Long,Object[]> timeSeries = new LinkedHashMap<Long,Object[]>();
	
	protected Map<String,Object[]> keySeries = new HashMap<String,Object[]>();
	
	private Map<String, Integer> newHeaderToIndex = new LinkedHashMap<String, Integer>();
	
	protected boolean timeSeriesChart = false;

	private boolean isSimpleDataSet = true;
	
	protected HTMLAbstractRenderer(Element element){
		this.element=element;
	}
	
	protected String getRawHeaderString() {
		StringBuffer html = new StringBuffer();
		List<Object> headers = element.getHeader();
		if(headers !=null){
			for (int i = 0; i < headers.size(); i++) {
				String head = ((String)headers.get(i)).replaceAll("'", "");
				String hs[] = head.split(":");
				headerIndexToTypeMap.put(i,hs[0]);
				html.append("data.addColumn('"+hs[0]+"', '"+hs[1]+"');\n");
			}
		}
		return html.toString();
	}
	
	protected String getRawDataSetString(){
		StringBuffer htmlDataSet = new StringBuffer();
		List<List<Object>> rows = element.getData();
		if(rows == null)
			return "";
		for (List<Object> row : rows) {
			StringBuffer rowBuffer = new StringBuffer();
			rowBuffer.append("[");
			for (int i = 0; i < row.size(); i++) {
				Object column = row.get(i);
				if (column == null){
					String dataType = headerIndexToTypeMap.get(i);
					if(dataType.equalsIgnoreCase(DashboardConstants.STRING))
						rowBuffer.append("'',");
					else if(dataType.equalsIgnoreCase(DashboardConstants.DATETIME))
						rowBuffer.append("'',");
					else
						rowBuffer.append("null,");
				}
				else{
					String dataType = headerIndexToTypeMap.get(i);
					if(dataType.equalsIgnoreCase(DashboardConstants.STRING))
						rowBuffer.append("'"+column.toString() + "',");
					else if(dataType.equalsIgnoreCase(DashboardConstants.DATETIME)){
						try {
							Date ts = sdf.parse(column.toString());
							String timeString = (ts.getYear()+1900)+","+(ts.getMonth())+","+ts.getDate()+","+ts.getHours()+","+ts.getMinutes();
							rowBuffer.append("new Date("+timeString+"),");
						} catch (ParseException e) {
							rowBuffer.append("'"+column.toString() + "',");
							e.printStackTrace();
						}
					}
					else
						rowBuffer.append(column.toString() + ",");
				}
			}
			htmlDataSet.append(rowBuffer.subSequence(0,
					rowBuffer.lastIndexOf(","))
					+ "],\n");
		}
		if (htmlDataSet.lastIndexOf(",") > 1)
			return htmlDataSet.subSequence(0, htmlDataSet.lastIndexOf(","))
					+ "\n";
		else
			return htmlDataSet.toString();
	}
	
	protected String getProcessedHeaderString(){
		normalizeData();
		if(isSimpleDataSet)
			return getSimpleProcessedHeaderString();
		else
			return getComplexProcessedHeaderString();
		
	}
	
	protected String getSimpleProcessedHeaderString() {
		StringBuffer toReturn = new StringBuffer();
		StringBuffer html = new StringBuffer();
		String keyColumn = "";
		String timeColumn = "";
		List<Object> headers = element.getHeader();
		if(headers !=null){
			for (int i = 0; i < headers.size(); i++) {
				String head = ((String)headers.get(i)).replaceAll("'", "");
				String hs[] = head.split(":");
				headerIndexToTypeMap.put(i,hs[0]);
				if(hs[0].equalsIgnoreCase("string"))
					keyColumn=keyColumn+"data.addColumn('string','"+hs[1]+"');\n";
				else if(hs[0].equalsIgnoreCase("datetime"))
					timeColumn="data.addColumn('datetime','Time');\n";
				else
					html.append("data.addColumn('"+hs[0]+"', '"+hs[1]+"');\n");
			}
		}
		if(timeColumn != null && !timeColumn.isEmpty())
			toReturn.append(timeColumn);
		if(keyColumn != null && !keyColumn.isEmpty())
			toReturn.append(keyColumn);
		toReturn.append(html);
		return toReturn.toString();
	}

	
	protected String getComplexProcessedHeaderString() {
		StringBuffer toReturn = new StringBuffer();
		StringBuffer html = new StringBuffer();
		if(timeSeriesChart)
			toReturn.append("data.addColumn('datetime','Time');\n");
		else
			toReturn.append("data.addColumn('string','Time');\n");
		Set<String> keys = newHeaderToIndex.keySet();
		for (String key : keys) {
			toReturn.append("data.addColumn('number','"+key+"');\n");
		}
		toReturn.append(html);
		return toReturn.toString();
	}
	
	protected String getProcessedDataSetString(){
		if(isSimpleDataSet)
			return getSimpleProcessedDataSetString();
		else
			return getComplexProcessedDataSetString();
	}
	
	protected String getSimpleProcessedDataSetString(){
		StringBuffer htmlDataSet = new StringBuffer();
		List<List<Object>> rows = element.getData();
		if(rows == null)
			return "";
		for (List<Object> row : rows) {
			StringBuffer rowData = new StringBuffer();
			rowData.append("[");
			StringBuffer rowBuffer = new StringBuffer();
			String timeColumn = "";
			for (int i = 0; i < row.size(); i++) {
				Object column = row.get(i);
				if (column == null){
					String dataType = headerIndexToTypeMap.get(i);
					if(dataType.equalsIgnoreCase(DashboardConstants.STRING))
						rowBuffer.append("'',");
					else if(dataType.equalsIgnoreCase(DashboardConstants.DATETIME))
						rowBuffer.append("'',");
					else
						rowBuffer.append("null,");
				}
				else{
					String dataType = headerIndexToTypeMap.get(i);
					if(dataType.equalsIgnoreCase(DashboardConstants.STRING)){
						rowBuffer.append("'"+column.toString() + "',");
					}else if(dataType.equalsIgnoreCase(DashboardConstants.DATETIME)){
						try {
							Date ts = sdf.parse(column.toString());
							String timeString = (ts.getYear()+1900)+","+(ts.getMonth())+","+ts.getDate()+","+ts.getHours()+","+ts.getMinutes();
							timeColumn = "new Date("+timeString+"),";
						} catch (ParseException e) {
							rowBuffer.append("'"+column.toString() + "',");
							e.printStackTrace();
						}
					}
					else
						rowBuffer.append(column.toString() + ",");
				}
			}
			if(timeColumn != null && !timeColumn.isEmpty())
				rowData.append(timeColumn);
			rowData.append(rowBuffer.subSequence(0,	rowBuffer.lastIndexOf(","))+ "],\n");
			htmlDataSet.append(rowData);
		}
		if (htmlDataSet.lastIndexOf(",") > 1)
			return htmlDataSet.subSequence(0, htmlDataSet.lastIndexOf(","))
					+ "\n";
		else
			return htmlDataSet.toString();
	}
	
	protected String getComplexProcessedDataSetString(){
		StringBuffer htmlDataSet = new StringBuffer();
		if(timeSeriesChart){
			Set<Long> timeStamps = timeSeries.keySet();
			for (Long timeStamp : timeStamps) {
				StringBuffer rowData = new StringBuffer();
				rowData.append("[");
				StringBuffer rowBuffer = new StringBuffer();
				Date ts = new Date(timeStamp);
				String timeString = (ts.getYear()+1900)+","+(ts.getMonth())+","+ts.getDate()+","+ts.getHours()+","+ts.getMinutes();
				String timeColumn = "new Date("+timeString+")";
				rowBuffer.append(timeColumn+",");
				
				Object rows[] = timeSeries.get(timeStamp);
				for(int i = 0;i<newHeaderToIndex.size();i++)
					rowBuffer.append(rows[i]+",");
				rowData.append(rowBuffer.subSequence(0,	rowBuffer.lastIndexOf(","))+ "],\n");
				htmlDataSet.append(rowData);
			}
		}else{
			Set<String> keys = keySeries.keySet();
			for (String key : keys) {
				StringBuffer rowData = new StringBuffer();
				rowData.append("[");
				StringBuffer rowBuffer = new StringBuffer();
				rowBuffer.append("'"+key+"',");
				
				Object rows[] = keySeries.get(key);
				for(int i = 0;i<newHeaderToIndex.size();i++)
					rowBuffer.append(rows[i]+",");
				rowData.append(rowBuffer.subSequence(0,	rowBuffer.lastIndexOf(","))+ "],\n");
				htmlDataSet.append(rowData);
			}
			
		}
		if (htmlDataSet.lastIndexOf(",") > 1)
			return htmlDataSet.subSequence(0, htmlDataSet.lastIndexOf(","))
					+ "\n";
		else
			return htmlDataSet.toString();
	}

	private void normalizeData() {
		List<Object> headers = element.getHeader();
		if(headers !=null){
			for (int i = 0; i < headers.size(); i++) {
				String head = ((String)headers.get(i)).replaceAll("'", "");
				String hs[] = head.split(":");
				headerIndexToTypeMap.put(i,hs[0]);
				indexToName.put(i, hs[1]);
			}
		}
		List<List<Object>> rows = element.getData();
		if(rows == null)
			return;
		int maxIndex = 0;
		for (List<Object> row : rows) {
			String keyColumn = "";
			Date timeStamp = null;
			int keyCount = 0;
			int timeCount = 0;
			List<Object> tempRow = new ArrayList<Object>();
			for (int i = 0; i < row.size(); i++) {
				Object column = row.get(i);
				String dataType = headerIndexToTypeMap.get(i);
				if(dataType.equalsIgnoreCase(DashboardConstants.STRING)){
					keyColumn = keyColumn + "_"+column.toString();
					keyCount++;
				}
				else if(dataType.equalsIgnoreCase(DashboardConstants.DATETIME)){
					timeCount++;
					try {
						timeStamp = sdf.parse(column.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					tempRow.add(i+":"+column);
				}
			}
			
			if(timeSeriesChart == false && timeCount>0)
				timeSeriesChart = true;
			if(isSimpleDataSet == true && keyCount>1)
				isSimpleDataSet = false;
			
			if(timeSeriesChart)
				isSimpleDataSet=false;
			
			for (Object r : tempRow) {
				String patterns[] = r.toString().split(":");
				int index = Integer.parseInt(patterns[0]);
				String headerName = indexToName.get(index);
				String lookupKey = keyColumn+"_"+headerName;
				
				Integer ind = newHeaderToIndex.get(lookupKey);
				if(ind == null){
					ind = maxIndex;
					newHeaderToIndex.put(lookupKey,maxIndex++);
				}

				if(timeSeriesChart){
					Object rsd[] = timeSeries.get(timeStamp.getTime());
					if(rsd == null){
						rsd = new Object[100];
						timeSeries.put(timeStamp.getTime(), rsd);
					}
					rsd[ind]= patterns[1];
				}else{
					Object rsd[] = keySeries.get(keyColumn);
					if(rsd == null){
						rsd = new Object[100];
						keySeries.put(keyColumn, rsd);
					}
					rsd[ind]= patterns[1];
				}
			}
		}
	}
}
