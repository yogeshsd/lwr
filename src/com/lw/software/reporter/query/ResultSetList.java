package com.lw.software.reporter.query;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ResultSetList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultSetList {

	@XmlElement(name="ResultSetList")
	private ArrayList<ResultSet> resultSet;

	public ArrayList<ResultSet> getResultSet() {
		return resultSet;
	}

	public void setResultSet(ArrayList<ResultSet> resultSet) {
		this.resultSet = resultSet;
	}
}
