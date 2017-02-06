package com.prosnav.report.domain.db.upm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.prosnav.report.domain.db.MongoEntity;

@Document(collection="upm_workgroup")
public class WorkGroup extends MongoEntity {
	
	private String code;
	private String name;
	private String orgCode;
	private Worker[] workers;
	private boolean isDel;

	public WorkGroup() {}
	
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Worker[] getWorkers() {
		return workers;
	}

	public void setWorkers(Worker[] workers) {
		this.workers = workers;
	}

	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
			.append("_id", _id)
			.append("code", code)
			.append("name", name)
			.toString();
	}

	public class Worker {
		
		private long userid;
		private String[] workerRoles;
		
		public long getUserid() {
			return userid;
		}
		public void setUserid(long userid) {
			this.userid = userid;
		}
		public String[] getWorkerRoles() {
			return workerRoles;
		}
		public void setWorkerRoles(String[] workerRoles) {
			this.workerRoles = workerRoles;
		}
	}
}
