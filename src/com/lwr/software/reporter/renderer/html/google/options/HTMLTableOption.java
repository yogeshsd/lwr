package com.lwr.software.reporter.renderer.html.google.options;

public class HTMLTableOption implements IHTMLOption {

	@Override
	public String getOption(String title) {
		StringBuffer html = new StringBuffer();
		html.append("	var options = {\n");
		html.append("		title: '" + title + "',\n");
		html.append("		backgroundColor: '#ffffff',\n");
		html.append("		width: '100%',\n");
		html.append("		hight: '100%',\n");
		html.append("		titleTextStyle : { color: \"black\", fontSize: 12, bold: true},\n");
		html.append("		alternatingRowStyle: true,\n");
		html.append("	};\n");
		return html.toString();
	}

}
