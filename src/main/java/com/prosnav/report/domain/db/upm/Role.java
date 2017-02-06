package com.prosnav.report.domain.db.upm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_role")
public class Role extends MongoEntity {
	
	private String code;
	private String name;
	private String appCode;
	private String[] fnCodes;
	
	public Role() {}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String[] getFnCodes() {
		return fnCodes;
	}
	public void setFnCodes(String[] fnCodes) {
		this.fnCodes = fnCodes;
	}
	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("code", code)
			.append("name", name)
			.toString();
	}
}
