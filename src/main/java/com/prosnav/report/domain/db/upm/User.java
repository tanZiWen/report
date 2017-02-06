package com.prosnav.report.domain.db.upm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_user")
public class User extends MongoEntity {
	
	private String username;
	private String password;
	private String realName;
	private String sex;
	private String position;
	private String status;
	private long employeeid;
	private String workno;
	private String extNo;
	private String email;
	private boolean online;
	private String orgCode;
	private String[] roleCodes;
	
	private Map<String, Role> roleMap = new HashMap<String, Role>();
	private List<Role> roleList = new ArrayList<Role>();
	private Map<String, Function> functionMap = new HashMap<String, Function>();
	private List<Function> functionList = new ArrayList<Function>();
	
	private Function menuTree;
	
	public User() {}
	
	public User(long _id, String username, String realName) {
		this._id = _id;
		this.username = username;
		this.realName = realName;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(long employeeid) {
		this.employeeid = employeeid;
	}

	public String getWorkno() {
		return workno;
	}

	public void setWorkno(String workno) {
		this.workno = workno;
	}

	public String getExtNo() {
		return extNo;
	}

	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String[] getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String[] roleCodes) {
		this.roleCodes = roleCodes;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("username", username)
			.append("realName", realName)
			.toString();
	}

	public Map<String, Role> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<String, Role> roleMap) {
		this.roleMap = roleMap;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public Map<String, Function> getFunctionMap() {
		return functionMap;
	}

	public void setFunctionMap(Map<String, Function> functionMap) {
		this.functionMap = functionMap;
	}

	public List<Function> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<Function> functionList) {
		this.functionList = functionList;
	}

	public Function getMenuTree() {
		return menuTree;
	}

	public void setMenuTree(Function menuTree) {
		this.menuTree = menuTree;
	}
}
