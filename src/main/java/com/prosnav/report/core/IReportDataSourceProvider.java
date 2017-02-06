package com.prosnav.report.core;

import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

public interface IReportDataSourceProvider<T extends IParamModel> {
	
	//Jasper
	public Map<String, Object> getReportParameters(T paramModel);
	public JRDataSource getReportDataSource(T paramModel);
	
	//Html Page
	public Class<T> getParamModelType();
	public DateType getInitDateType();
	public Date getInitDate();
	public boolean isDayEnable();
	public boolean isMonthEnable();
	public boolean isYearEnable();
	public boolean isBeginDateEnable();
	public boolean isEndDateEnable();
	
	
	public static enum DateType {
		YEAR, MONTH, DAY
	}
}
