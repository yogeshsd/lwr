package com.lwr.software.reporter.renderer.html.google.options;

public class HTMLChartOption implements IHTMLOption {
	
	boolean isStacked = false;
	
	public void setStacked(boolean isStacked) {
		this.isStacked = isStacked;
	}

	@Override
	public String getOption(String title) {
		StringBuffer html = new StringBuffer();
		html.append("	var options = {\n");
		html.append("		legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},\n");
		html.append("		backgroundColor: '#ffffff',\n");
		html.append("		is3D: true,\n");
		html.append("		titleTextStyle : { color: \"black\", fontSize: 12, bold: true},\n");
		if(isStacked){
			html.append("		isStacked: true,\n");
		}else{
			html.append("		isStacked: false,\n");
		}
		html.append("		width: '100%',\n");
		html.append("		hight: '100%',\n");
		html.append("		groupWidth: 20,\n");
		html.append("		pieHole: 0.4,\n");
		html.append("	};\n");
		return html.toString();
	}

}
