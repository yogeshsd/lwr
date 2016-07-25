package com.lwr.software.reporter.renderer.element;

import com.lwr.software.reporter.DashboardConstants;

public class ElementRendererFactory {

	public static IELementRendererFactory getRendererFactory(String type){
		if(type.equalsIgnoreCase(DashboardConstants.HTML_GOOGLE)){
			return new HTMLGoogleElementRendererFactory();
		}else if(type.equalsIgnoreCase(DashboardConstants.CSV)){
			return new CSVElementRendererFactory();
		}else if(type.equalsIgnoreCase(DashboardConstants.HTML_JFREE)){
			return new HTMLJFreeElementRendererFactory();
		}else{
			return null;
		}
	}
}
