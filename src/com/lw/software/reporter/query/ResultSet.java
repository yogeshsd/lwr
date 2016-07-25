package com.lw.software.reporter.query;

import java.util.ArrayList;
import java.util.List;

public class ResultSet {

	private List<String> headers;
	
	private ArrayList<ArrayList<Object>> rows;

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public ArrayList<ArrayList<Object>> getRows() {
		return rows;
	}

	public void setRows(ArrayList<ArrayList<Object>> rows) {
		this.rows = rows;
	}
	
}
