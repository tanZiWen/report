package com.prosnav.report.domain.enums;

public enum WechatStatus {
	
	OPENED("opened"), UNOPEN("unopen");
	
	private String name;
	
	private WechatStatus(String name) {
		this.name = name;
	}
	
	public String val() {
		return name;
	}

}
