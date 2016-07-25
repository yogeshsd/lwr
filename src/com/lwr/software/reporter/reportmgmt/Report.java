package com.lwr.software.reporter.reportmgmt;

import java.util.List;

public class Report {

	private String title;
	
	private String description;
	
	private int maxrows;
	
	private List<RowElement> rows;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getmaxrows() {
		return maxrows;
	}

	public void setmaxrows(int maxrows) {
		this.maxrows = maxrows;
	}

	public List<RowElement> getRows() {
		return rows;
	}

	public void setRows(List<RowElement> rows) {
		this.rows = rows;
	}
}
