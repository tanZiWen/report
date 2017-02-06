package com.prosnav.report.domain.view;

import com.prosnav.report.core.BaseParamModel;

public class DataMgrBusinessChanceReportParam extends BaseParamModel {
	private String orgCode;
	private String workGroup;
	private String user;
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "DataMgrBusinessChanceReportParam [orgCode=" + orgCode
				+ ", workGroup=" + workGroup + ", user=" + user + "]";
	}
}
