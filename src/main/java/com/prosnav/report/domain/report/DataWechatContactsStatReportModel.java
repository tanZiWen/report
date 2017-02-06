package com.prosnav.report.domain.report;


public class DataWechatContactsStatReportModel {
	
	private Long accountId;
	
	private String accountCode;
	
	private String status;
	
	private Long count = 0l;
	
	private Long totalCount = 0l;
	
	private Long successCount = 0l;
	
	private Long openedCount = 0l;
	
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Long successCount) {
		this.successCount = successCount;
	}

	public Long getOpenedCount() {
		return openedCount;
	}

	public void setOpenedCount(Long openedCount) {
		this.openedCount = openedCount;
	}
	
}
