package com.prosnav.report.domain.report;

import com.prosnav.report.domain.db.upm.User;

public class TestReportModel extends User {
	
	private int age;
	
	public TestReportModel(long _id, int age, String username, String realName) {
		this._id = _id;
		this.age = age;
		this.setUsername(username);
		this.setRealName(realName);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
