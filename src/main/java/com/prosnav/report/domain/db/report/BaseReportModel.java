/**
 * 
 */
package com.prosnav.report.domain.db.report;

import java.util.Date;

/**
 * @author wangnan
 *
 */
public class BaseReportModel {
	
	private int reportYear;
	private int reportMonth;
	private int reportDay;
	private Date ctime;
	
	/**
	 * @return the reportYear
	 */
	public int getReportYear() {
		return reportYear;
	}
	/**
	 * @param reportYear the reportYear to set
	 */
	public void setReportYear(int reportYear) {
		this.reportYear = reportYear;
	}
	/**
	 * @return the reportMonth
	 */
	public int getReportMonth() {
		return reportMonth;
	}
	/**
	 * @param reportMonth the reportMonth to set
	 */
	public void setReportMonth(int reportMonth) {
		this.reportMonth = reportMonth;
	}
	/**
	 * @return the reportDay
	 */
	public int getReportDay() {
		return reportDay;
	}
	/**
	 * @param reportDay the reportDay to set
	 */
	public void setReportDay(int reportDay) {
		this.reportDay = reportDay;
	}
	/**
	 * @return the ctime
	 */
	public Date getCtime() {
		return ctime;
	}
	/**
	 * @param ctime the ctime to set
	 */
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
}
