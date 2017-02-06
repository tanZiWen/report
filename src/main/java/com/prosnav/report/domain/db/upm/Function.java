package com.prosnav.report.domain.db.upm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_function")
public class Function extends MongoEntity {
	
	private String code;
	private String name;
	private String funcAction;
	private String parentCode;
	private String treeLevel;
	private String type;
	private String appCode;
	private boolean hidden;
	
	private List<Function> children = new ArrayList<Function>();
	
	public Function() {}
	
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
	public String getFuncAction() {
		return funcAction;
	}
	public void setFuncAction(String funcAction) {
		this.funcAction = funcAction;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(String treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("code", code)
			.append("name", name)
			.toString();
	}

	public List<Function> getChildren() {
		return children;
	}

	public void setChildren(List<Function> children) {
		this.children = children;
	}
}
