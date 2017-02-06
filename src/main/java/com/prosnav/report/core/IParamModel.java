package com.prosnav.report.core;

public interface IParamModel {
	public void setReportDateType(String reportDateType);
	public String getReportDateType();
	
	public void setReportDayBegin(String reportDayBegin);
	public String getReportDayBegin();
	public void setReportDayEnd(String reportDayBegin);
	public String getReportDayEnd();
	
	public void setReportMonthBegin(String reportMonthBegin);
	public String getReportMonthBegin();
	public void setReportMonthEnd(String reportMonthBegin);
	public String getReportMonthEnd();
	
	public void setReportYearBegin(String reportYearBegin);
	public String getReportYearBegin();
	public void setReportYearEnd(String reportYearBegin);
	public String getReportYearEnd();
}
