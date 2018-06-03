package com.haojg.model;

import java.util.Date;
import javax.persistence.*;

public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String username;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_no")
    private String bankNo;

    private String mobile;

    /**
     * 提现金额数字
     */
    @Column(name = "buy_num")
    private Integer buyNum;

    private Integer assets;

    /**
     * 状态
     */
    private Integer state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return bank_name
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return bank_no
     */
    public String getBankNo() {
        return bankNo;
    }

    /**
     * @param bankNo
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取提现金额数字
     *
     * @return buy_num - 提现金额数字
     */
    public Integer getBuyNum() {
        return buyNum;
    }

    /**
     * 设置提现金额数字
     *
     * @param buyNum 提现金额数字
     */
    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    /**
     * @return assets
     */
    public Integer getAssets() {
        return assets;
    }

    /**
     * @param assets
     */
    public void setAssets(Integer assets) {
        this.assets = assets;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}