package com.prosnav.report.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.util.StringUtils;

import utils.DateUtil;

public class BaseParamModel implements IParamModel {
	
	private String reportDateType;
	private String reportDayBegin;
	private String reportDayEnd;
	private String reportMonthBegin;
	private String reportMonthEnd;
	private String reportYearBegin;
	private String reportYearEnd;
	
	public String getSelectionBeginDate() {
		if(reportDateType == null) {
			return reportDayBegin;
		}
		if(reportDateType.equals(IReportDataSourceProvider.DateType.DAY.name())) {
			return reportDayBegin;
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.MONTH.name())) {
			return reportMonthBegin;
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.YEAR.name())) {
			return reportYearBegin;
		} else {
			return reportDayBegin;
		}
	}
	
	public String[] getSelectionBeginDateArray() {
		String beginDate = this.getSelectionBeginDate();
		if(StringUtils.isEmpty(beginDate)) {
			return null;
		} else {
			return beginDate.split("-");
		}
	}
	
	public String[] getMinSelectionBeginDate() {
		String[] beginDateArray = this.getSelectionBeginDateArray();
		if(beginDateArray == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, new Integer(beginDateArray[0]));
		if(beginDateArray.length > 1) {
			cal.set(Calendar.MONTH, new Integer(beginDateArray[1]) - 1);
		}
		if(beginDateArray.length > 2) {
			cal.set(Calendar.DAY_OF_MONTH, new Integer(beginDateArray[2]));
		}
		
		if(reportDateType.equals(IReportDataSourceProvider.DateType.DAY.name())) {
			return beginDateArray;
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.MONTH.name())) {
			return new String[] {
					String.valueOf(cal.get(Calendar.YEAR)), 
					String.valueOf(cal.get(Calendar.MONTH) + 1),
					String.valueOf(cal.getActualMinimum(Calendar.DAY_OF_MONTH))
				};
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.YEAR.name())) {
			cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
			return new String[] {
					String.valueOf(cal.get(Calendar.YEAR)), 
					String.valueOf(cal.get(Calendar.MONTH) + 1),
					String.valueOf(cal.getActualMinimum(Calendar.DAY_OF_MONTH))
				};
		} else {
			return null;
		}
	}
	
	public String getSelectionBeginDateISO() {
		String[] beginDate = getSelectionBeginDate().split("-");
		return beginDateToISO(beginDate);
	}
	
	public String getSelectionEndDateISO() {
		String[] endDate = getSelectionEndDate().split("-");
		return endDateToISO(endDate);
	}

	public String getSelectionEndDate() {
		if(reportDateType == null) {
			return reportDayEnd;
		}
		if(reportDateType.equals(IReportDataSourceProvider.DateType.DAY.name())) {
			return reportDayEnd;
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.MONTH.name())) {
			return reportMonthEnd;
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.YEAR.name())) {
			return reportYearEnd;
		} else {
			return reportDayEnd;
		}
	}
	
	public String[] getSelectionEndDateArray() {
		String endDate = this.getSelectionEndDate();
		if(StringUtils.isEmpty(endDate)) {
			return null;
		} else {
			return endDate.split("-");
		}
	}
	
	public String[] getMaxSelectionEndDate() {
		String[] endDateArray = this.getSelectionEndDateArray();
		if(endDateArray == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, new Integer(endDateArray[0]));
		if(endDateArray.length > 1) {
			cal.set(Calendar.MONTH, new Integer(endDateArray[1]) - 1);
		}
		if(endDateArray.length > 2) {
			cal.set(Calendar.DAY_OF_MONTH, new Integer(endDateArray[2]));
		}
		
		if(reportDateType.equals(IReportDataSourceProvider.DateType.DAY.name())) {
			return endDateArray;
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.MONTH.name())) {
			return new String[] {
					String.valueOf(cal.get(Calendar.YEAR)), 
					String.valueOf(cal.get(Calendar.MONTH) + 1),
					String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH))
				};
		} else if(reportDateType.equals(IReportDataSourceProvider.DateType.YEAR.name())) {
			cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
			return new String[] {
					String.valueOf(cal.get(Calendar.YEAR)), 
					String.valueOf(cal.get(Calendar.MONTH) + 1),
					String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH))
				};
		} else {
			return null;
		}
	}
	
	public String getReportDateType() {
		return reportDateType;
	}
	public void setReportDateType(String reportDateType) {
		this.reportDateType = reportDateType;
	}
	public String getReportDayBegin() {
		return reportDayBegin;
	}
	public void setReportDayBegin(String reportDayBegin) {
		this.reportDayBegin = reportDayBegin;
	}
	public String getReportDayEnd() {
		return reportDayEnd;
	}
	public void setReportDayEnd(String reportDayEnd) {
		this.reportDayEnd = reportDayEnd;
	}
	public String getReportMonthBegin() {
		return reportMonthBegin;
	}
	public void setReportMonthBegin(String reportMonthBegin) {
		this.reportMonthBegin = reportMonthBegin;
	}
	public String getReportMonthEnd() {
		return reportMonthEnd;
	}
	public void setReportMonthEnd(String reportMonthEnd) {
		this.reportMonthEnd = reportMonthEnd;
	}
	public String getReportYearBegin() {
		return reportYearBegin;
	}
	public void setReportYearBegin(String reportYearBegin) {
		this.reportYearBegin = reportYearBegin;
	}
	public String getReportYearEnd() {
		return reportYearEnd;
	}
	public void setReportYearEnd(String reportYearEnd) {
		this.reportYearEnd = reportYearEnd;
	}
	
	private String beginDateToISO(String[] beginDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		if(beginDate.length == 1) {
			return sdf.format(DateUtil.getCurrYearFirstISO(Integer.parseInt(beginDate[0])));
		} else if(beginDate.length == 2) {
			return sdf.format(DateUtil.getCurrMonthFirstISO(Integer.parseInt(beginDate[0]), Integer.parseInt(beginDate[1])));
		} else {
			return sdf.format(DateUtil.getCurrDayFirstISO(Integer.parseInt(beginDate[0]), Integer.parseInt(beginDate[1]), Integer.parseInt(beginDate[2])));
		}
	}
	
	private String endDateToISO(String[] endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		if(endDate.length == 1) {
			return sdf.format(DateUtil.getCurrYearLastISO(Integer.parseInt(endDate[0])));
		} else if(endDate.length == 2) {
			return sdf.format(DateUtil.getCurrMonthLastISO(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1])));
		} else {
			return sdf.format(DateUtil.getCurrDayLastISO(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2])));
		}
	}
	
	public static void main(String[] args) {
		BaseParamModel m = new BaseParamModel();
		m.setReportDateType(IReportDataSourceProvider.DateType.DAY.name());
		m.setReportDayBegin("2014-10-10");
		m.setReportDayEnd("2014-11-10");
		
		BaseParamModel m1 = new BaseParamModel();
		m1.setReportDateType(IReportDataSourceProvider.DateType.MONTH.name());
		m1.setReportMonthBegin("2014-10");
		m1.setReportMonthEnd("2014-11");
		
		BaseParamModel m2 = new BaseParamModel();
		m2.setReportDateType(IReportDataSourceProvider.DateType.YEAR.name());
		m2.setReportYearBegin("2013");
		m2.setReportYearEnd("2014");
		
		System.out.println(m.getMinSelectionBeginDate()[0] + "-" + m.getMinSelectionBeginDate()[1] + "-" + m.getMinSelectionBeginDate()[2]);
		System.out.println(m.getMaxSelectionEndDate()[0] + "-" + m.getMaxSelectionEndDate()[1] + "-" + m.getMaxSelectionEndDate()[2]);
		System.out.println(m1.getMinSelectionBeginDate()[0] + "-" + m1.getMinSelectionBeginDate()[1] + "-" + m1.getMinSelectionBeginDate()[2]);
		System.out.println(m1.getMaxSelectionEndDate()[0] + "-" + m1.getMaxSelectionEndDate()[1] + "-" + m1.getMaxSelectionEndDate()[2]);
		System.out.println(m2.getMinSelectionBeginDate()[0] + "-" + m2.getMinSelectionBeginDate()[1] + "-" + m2.getMinSelectionBeginDate()[2]);
		System.out.println(m2.getMaxSelectionEndDate()[0] + "-" + m2.getMaxSelectionEndDate()[1] + "-" + m2.getMaxSelectionEndDate()[2]);
	}
}
