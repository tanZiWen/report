package com.prosnav.report.domain.db.crm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="crm_customer")
public class Customer extends MongoEntity {

	private String code;
	private long preid;
	private String telNo;
	private String name;
	private String level;
	private String status;
	private String callStatus;
	private String auditStatus;
	private BelongArea belongArea;
	private String sex;
	private String city;
	private boolean free;
	private long belongUser;
	
	public Customer() {}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getPreid() {
		return preid;
	}

	public void setPreid(long preid) {
		this.preid = preid;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public BelongArea getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(BelongArea belongArea) {
		this.belongArea = belongArea;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public long getBelongUser() {
		return belongUser;
	}

	public void setBelongUser(long belongUser) {
		this.belongUser = belongUser;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("code", code)
			.append("name", name)
			.toString();
	}
	
	class BelongArea {
		
		private String areaCode;
		private String areaName;
		
		public String getAreaCode() {
			return areaCode;
		}
		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
	}
}
