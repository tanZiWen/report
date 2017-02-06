package com.prosnav.report.domain.db.crm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="crm_customer_history")
public class CustomerHistory extends Customer {
	
	private long hisId;
	private String hisOperateType;
	private String hisOperateTime;
	private HisOperateUser hisOperateUser;
	private String[] changeColumns;
	private Customer changeCustomer;
	
	public CustomerHistory() {}

	public long getHisId() {
		return hisId;
	}

	public void setHisId(long hisId) {
		this.hisId = hisId;
	}

	public String getHisOperateType() {
		return hisOperateType;
	}

	public void setHisOperateType(String hisOperateType) {
		this.hisOperateType = hisOperateType;
	}

	public String getHisOperateTime() {
		return hisOperateTime;
	}

	public void setHisOperateTime(String hisOperateTime) {
		this.hisOperateTime = hisOperateTime;
	}

	public HisOperateUser getHisOperateUser() {
		return hisOperateUser;
	}

	public void setHisOperateUser(HisOperateUser hisOperateUser) {
		this.hisOperateUser = hisOperateUser;
	}

	public String[] getChangeColumns() {
		return changeColumns;
	}

	public void setChangeColumns(String[] changeColumns) {
		this.changeColumns = changeColumns;
	}

	public Customer getChangeCustomer() {
		return changeCustomer;
	}

	public void setChangeCustomer(Customer changeCustomer) {
		this.changeCustomer = changeCustomer;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("hisId", hisId)
			.append("code", this.getCode())
			.append("name", this.getName())
			.toString();
	}

	class HisOperateUser {	
		private long id;
		private String username;
		private String realName;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
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
	}
}
