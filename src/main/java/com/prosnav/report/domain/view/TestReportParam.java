package com.prosnav.report.domain.view;

import com.prosnav.report.core.BaseParamModel;

public class TestReportParam extends BaseParamModel {
	
	private String name;
	private String email;
	private String sex;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}
