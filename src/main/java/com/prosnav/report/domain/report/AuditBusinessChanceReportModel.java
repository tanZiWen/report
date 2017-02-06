package com.prosnav.report.domain.report;


public class AuditBusinessChanceReportModel {
	
	public long userId;
	public String username = "-";
	public String realName = "-";
	public int bc20Count;
	public int audit20Count;
	public int bc40Count;
	public int audit40Count;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getBc20Count() {
		return bc20Count;
	}
	public void setBc20Count(int bc20Count) {
		this.bc20Count = bc20Count;
	}
	public int getAudit20Count() {
		return audit20Count;
	}
	public void setAudit20Count(int audit20Count) {
		this.audit20Count = audit20Count;
	}
	public int getBc40Count() {
		return bc40Count;
	}
	public void setBc40Count(int bc40Count) {
		this.bc40Count = bc40Count;
	}
	public int getAudit40Count() {
		return audit40Count;
	}
	public void setAudit40Count(int audit40Count) {
		this.audit40Count = audit40Count;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
}
