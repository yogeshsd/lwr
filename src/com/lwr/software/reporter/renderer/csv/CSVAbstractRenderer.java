package com.lwr.software.reporter.renderer.csv;

import java.util.List;

import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.reportmgmt.Element;

public abstract class CSVAbstractRenderer implements IElementRenderer{
	
	public Element element;
	
	public String render(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\n");
		buffer.append(element.getTitle());
		buffer.append("\n\n");
		List<Object> headers = element.getHeader();
		for (Object header : headers) {
			buffer.append(header+",");
		}
		buffer.append("\n");
		List<List<Object>> rows = element.getData();
		for (List<Object> row : rows) {
			for (Object col : row) {
				buffer.append(col+",");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public CSVAbstractRenderer(Element element){
		this.element = element;
	}
	
}
