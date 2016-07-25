package com.lwr.software.reporter.renderer.report;

import java.io.OutputStream;

public interface IReportRenderer {

	public String render();
	
	public void render(OutputStream stream);
}
