package com.lwr.software.reporter.renderer.element;

import java.util.Set;

public interface IElementRenderer {

	public String render();
	
	public boolean validate();
	
	public Set<String> getChartMessages();
	
}
