package com.haojg.model;

import java.util.Date;
import javax.persistence.*;

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String realname;

    private String no;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_no")
    private String bankNo;

    /**
     * 购买份额
     */
    @Column(name = "buy_num")
    private Integer buyNum;

    private String mail;

    @Column(name = "rec_user_id")
    private Long recUserId;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 股份数
     */
    @Column(name = "stock_num")
    private Integer stockNum;

    /**
     * 我的资产
     */
    private Double assets;

    private String remark;

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
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * @param realname
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * @return no
     */
    public String getNo() {
        return no;
    }

    /**
     * @param no
     */
    public void setNo(String no) {
        this.no = no;
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
     * 获取购买份额
     *
     * @return buy_num - 购买份额
     */
    public Integer getBuyNum() {
        return buyNum;
    }

    /**
     * 设置购买份额
     *
     * @param buyNum 购买份额
     */
    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    /**
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return rec_user_id
     */
    public Long getRecUserId() {
        return recUserId;
    }

    /**
     * @param recUserId
     */
    public void setRecUserId(Long recUserId) {
        this.recUserId = recUserId;
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

    /**
     * 获取股份数
     *
     * @return stock_num - 股份数
     */
    public Integer getStockNum() {
        return stockNum;
    }

    /**
     * 设置股份数
     *
     * @param stockNum 股份数
     */
    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    /**
     * 获取我的资产
     *
     * @return assets - 我的资产
     */
    public Double getAssets() {
        return assets;
    }

    /**
     * 设置我的资产
     *
     * @param assets 我的资产
     */
    public void setAssets(Double assets) {
        this.assets = assets;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}