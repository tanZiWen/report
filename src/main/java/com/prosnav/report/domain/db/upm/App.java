package com.prosnav.report.domain.db.upm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_user")
public class App extends MongoEntity {
	
	private String code;
	private String name;
	
	public App(long _id, String code, String name) {
		this._id = _id;
		this.code = code;
		this.name = name;
	}
	
	public App() {}
	
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
	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("code", code)
			.append("name", name)
			.toString();
	}
}
