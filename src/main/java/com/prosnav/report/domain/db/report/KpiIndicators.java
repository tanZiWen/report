package com.prosnav.report.domain.db.report;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author tanyuan
 * KPI各项指标统计
 *
 */
@Document
public class KpiIndicators extends BaseReportModel{
	private long groupId;
	private String groupName;
	private long userId;
	private String username;
	private String userRealName;
	private int potentialCount;
	private int bc20Count;
	private int bc40Count;
	private int bc60Count;
	private int bc80Count;
	private int dealCount;
	private int vipCount;
	private int diamondVipCount;
	private int crownedVipCount;
	private int connectedCount;
	private int proxyConnectedCount;
	private int noResponseCount;
	private int closedCount;
	private int busySignalCount;
	private int refusedCount;
	private int vacantCount;
	private int faxCount;
	private int reAssignCount;
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	public int getPotentialCount() {
		return potentialCount;
	}
	public void setPotentialCount(int potentialCount) {
		this.potentialCount = potentialCount;
	}
	public int getBc20Count() {
		return bc20Count;
	}
	public void setBc20Count(int bc20Count) {
		this.bc20Count = bc20Count;
	}
	public int getBc40Count() {
		return bc40Count;
	}
	public void setBc40Count(int bc40Count) {
		this.bc40Count = bc40Count;
	}
	public int getBc60Count() {
		return bc60Count;
	}
	public void setBc60Count(int bc60Count) {
		this.bc60Count = bc60Count;
	}
	public int getBc80Count() {
		return bc80Count;
	}
	public void setBc80Count(int bc80Count) {
		this.bc80Count = bc80Count;
	}
	public int getDealCount() {
		return dealCount;
	}
	public void setDealCount(int dealCount) {
		this.dealCount = dealCount;
	}
	public int getVipCount() {
		return vipCount;
	}
	public void setVipCount(int vipCount) {
		this.vipCount = vipCount;
	}
	public int getDiamondVipCount() {
		return diamondVipCount;
	}
	public void setDiamondVipCount(int diamondVipCount) {
		this.diamondVipCount = diamondVipCount;
	}
	public int getCrownedVipCount() {
		return crownedVipCount;
	}
	public void setCrownedVipCount(int crownedVipCount) {
		this.crownedVipCount = crownedVipCount;
	}
	public int getConnectedCount() {
		return connectedCount;
	}
	public void setConnectedCount(int connectedCount) {
		this.connectedCount = connectedCount;
	}
	public int getProxyConnectedCount() {
		return proxyConnectedCount;
	}
	public void setProxyConnectedCount(int proxyConnectedCount) {
		this.proxyConnectedCount = proxyConnectedCount;
	}
	public int getNoResponseCount() {
		return noResponseCount;
	}
	public void setNoResponseCount(int noResponseCount) {
		this.noResponseCount = noResponseCount;
	}
	public int getClosedCount() {
		return closedCount;
	}
	public void setClosedCount(int closedCount) {
		this.closedCount = closedCount;
	}
	public int getBusySignalCount() {
		return busySignalCount;
	}
	public void setBusySignalCount(int busySignalCount) {
		this.busySignalCount = busySignalCount;
	}
	public int getRefusedCount() {
		return refusedCount;
	}
	public void setRefusedCount(int refusedCount) {
		this.refusedCount = refusedCount;
	}
	public int getVacantCount() {
		return vacantCount;
	}
	public void setVacantCount(int vacantCount) {
		this.vacantCount = vacantCount;
	}
	public int getFaxCount() {
		return faxCount;
	}
	public void setFaxCount(int faxCount) {
		this.faxCount = faxCount;
	}
	public int getReAssignCount() {
		return reAssignCount;
	}
	public void setReAssignCount(int reAssignCount) {
		this.reAssignCount = reAssignCount;
	}
	@Override
	public String toString() {
		return "KpiIndicators [groupId=" + groupId + ", groupName=" + groupName
				+ ", userId=" + userId + ", username=" + username
				+ ", userRealName=" + userRealName + ", potentialCount="
				+ potentialCount + ", bc20Count=" + bc20Count + ", bc40Count="
				+ bc40Count + ", bc60Count=" + bc60Count + ", bc80Count="
				+ bc80Count + ", dealCount=" + dealCount + ", vipCount="
				+ vipCount + ", diamondVipCount=" + diamondVipCount
				+ ", crownedVipCount=" + crownedVipCount + ", connectedCount="
				+ connectedCount + ", proxyConnectedCount="
				+ proxyConnectedCount + ", noResponseCount=" + noResponseCount
				+ ", closedCount=" + closedCount + ", busySignalCount="
				+ busySignalCount + ", refusedCount=" + refusedCount
				+ ", vacantCount=" + vacantCount + ", faxCount=" + faxCount
				+ ", reAssignCount=" + reAssignCount + "]";
	}
}
