package com.haojg.shouji.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "t_user")
@DynamicUpdate
@DynamicInsert
public class User{

    @Id()
    @GeneratedValue
    Long id;
    
    @Column(unique=true)
    String username;
    String password;
    Integer status;
    String cardId;
    String realName;
    String bankName;
    String bankNo;

    Long recUserId;
    Integer role;

    String telephoneNo;
    String mobilType;
    String mobileApplyTime;
    Integer mobileTotalMoney;
    Integer mobilePerMoney;
    Integer mobileNum;

    Date createTime;
    Date updateTime;
    Date activeTime;

    Integer moneyNum;
    
    String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public Long getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(Long recUserId) {
		this.recUserId = recUserId;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getMobilType() {
		return mobilType;
	}

	public void setMobilType(String mobilType) {
		this.mobilType = mobilType;
	}

	public String getMobileApplyTime() {
		return mobileApplyTime;
	}

	public void setMobileApplyTime(String mobileApplyTime) {
		this.mobileApplyTime = mobileApplyTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getMobileTotalMoney() {
		return mobileTotalMoney;
	}

	public void setMobileTotalMoney(Integer mobileTotalMoney) {
		this.mobileTotalMoney = mobileTotalMoney;
	}

	public Integer getMobilePerMoney() {
		return mobilePerMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setMobilePerMoney(Integer mobilePerMoney) {
		this.mobilePerMoney = mobilePerMoney;
	}

	public Integer getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(Integer mobileNum) {
		this.mobileNum = mobileNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Integer getMoneyNum() {
		return moneyNum;
	}

	public void setMoneyNum(Integer moneyNum) {
		this.moneyNum = moneyNum;
	}

}
