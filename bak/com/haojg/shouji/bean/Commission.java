package com.haojg.shouji.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "t_commission")
@DynamicUpdate
@DynamicInsert
public class Commission {

	@GeneratedValue
	@Id
	Long id;
	Long recUserId;
	Long activeUserId;
	Integer num;
	Date createTime;
	String remark;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRecUserId() {
		return recUserId;
	}
	public void setRecUserId(Long recUserId) {
		this.recUserId = recUserId;
	}
	public Long getActiveUserId() {
		return activeUserId;
	}
	public void setActiveUserId(Long activeUserId) {
		this.activeUserId = activeUserId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
