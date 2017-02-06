package com.prosnav.report.domain.db.upm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_area")
public class Area extends MongoEntity {
	
	private String code;
	private String name;
	private String[] orgCodes;
	
	public Area() {}
	
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
	public String[] getOrgCodes() {
		return orgCodes;
	}
	public void setOrgCodes(String[] orgCodes) {
		this.orgCodes = orgCodes;
	}
	public String toString() {
		return new ToStringBuilder(this)
		.append("_id", _id)
		.append("code", code)
		.append("name", name)
		.toString();
	}
}
