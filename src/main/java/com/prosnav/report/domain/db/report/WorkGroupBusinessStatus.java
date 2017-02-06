/**
 * 
 */
package com.prosnav.report.domain.db.report;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wangnan
 * 顾问组商机统计表
 */
@Document
public class WorkGroupBusinessStatus extends BaseReportModel {
	
	private Long groupId;
	private String groupName;
	private Long userId;
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
	
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
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
	/**
	 * @return the potentialCount
	 */
	public int getPotentialCount() {
		return potentialCount;
	}
	/**
	 * @param potentialCount the potentialCount to set
	 */
	public void setPotentialCount(int potentialCount) {
		this.potentialCount = potentialCount;
	}
	/**
	 * @return the bc20Count
	 */
	public int getBc20Count() {
		return bc20Count;
	}
	/**
	 * @param bc20Count the bc20Count to set
	 */
	public void setBc20Count(int bc20Count) {
		this.bc20Count = bc20Count;
	}
	/**
	 * @return the bc40Count
	 */
	public int getBc40Count() {
		return bc40Count;
	}
	/**
	 * @param bc40Count the bc40Count to set
	 */
	public void setBc40Count(int bc40Count) {
		this.bc40Count = bc40Count;
	}
	/**
	 * @return the bc60Count
	 */
	public int getBc60Count() {
		return bc60Count;
	}
	/**
	 * @param bc60Count the bc60Count to set
	 */
	public void setBc60Count(int bc60Count) {
		this.bc60Count = bc60Count;
	}
	/**
	 * @return the bc80Count
	 */
	public int getBc80Count() {
		return bc80Count;
	}
	/**
	 * @param bc80Count the bc80Count to set
	 */
	public void setBc80Count(int bc80Count) {
		this.bc80Count = bc80Count;
	}
	/**
	 * @return the dealCount
	 */
	public int getDealCount() {
		return dealCount;
	}
	/**
	 * @param dealCount the dealCount to set
	 */
	public void setDealCount(int dealCount) {
		this.dealCount = dealCount;
	}
	/**
	 * @return the vipCount
	 */
	public int getVipCount() {
		return vipCount;
	}
	/**
	 * @param vipCount the vipCount to set
	 */
	public void setVipCount(int vipCount) {
		this.vipCount = vipCount;
	}
	/**
	 * @return the diamondVipCount
	 */
	public int getDiamondVipCount() {
		return diamondVipCount;
	}
	/**
	 * @param diamondVipCount the diamondVipCount to set
	 */
	public void setDiamondVipCount(int diamondVipCount) {
		this.diamondVipCount = diamondVipCount;
	}
	/**
	 * @return the crownedVipCount
	 */
	public int getCrownedVipCount() {
		return crownedVipCount;
	}
	/**
	 * @param crownedVipCount the crownedVipCount to set
	 */
	public void setCrownedVipCount(int crownedVipCount) {
		this.crownedVipCount = crownedVipCount;
	}
}
