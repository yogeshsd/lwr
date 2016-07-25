package com.lwr.software.reporter.renderer.element;

import com.lwr.software.reporter.reportmgmt.Element;

public interface IELementRendererFactory {

	public IElementRenderer getRenderer(Element element);
	
}
