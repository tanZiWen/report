package com.prosnav.report.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

public class JasperView extends JasperReportsMultiFormatView {

	public JasperView() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN", false);
		this.setExporterParameters(parameters);
	}
	
}
