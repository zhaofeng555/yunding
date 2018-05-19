package com.haojg.model;

import java.util.Date;
import javax.persistence.*;

public class Export {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "to_username")
    private String toUsername;

    /**
     * 导出
     */
    private Integer num;

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
     * @return to_user_id
     */
    public Long getToUserId() {
        return toUserId;
    }

    /**
     * @param toUserId
     */
    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    /**
     * @return to_username
     */
    public String getToUsername() {
        return toUsername;
    }

    /**
     * @param toUsername
     */
    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    /**
     * 获取导出
     *
     * @return num - 导出
     */
    public Integer getNum() {
        return num;
    }

    /**
     * 设置导出
     *
     * @param num 导出
     */
    public void setNum(Integer num) {
        this.num = num;
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