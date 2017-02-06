package com.prosnav.report.domain.db.upm;

import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_dictionary")
public class Dictionary extends MongoEntity {
	
	private String key;
	private String type;
	private String name;
	private String parentKey;
	
	public Dictionary() {}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
}
