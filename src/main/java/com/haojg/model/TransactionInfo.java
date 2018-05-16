package com.haojg.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "transaction_info")
public class TransactionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long from;

    private Long to;

    private Double price;

    private Integer num;

    private Integer state;

    @Column(name = "create_time")
    private Date createTime;

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
     * @return from
     */
    public Long getFrom() {
        return from;
    }

    /**
     * @param from
     */
    public void setFrom(Long from) {
        this.from = from;
    }

    /**
     * @return to
     */
    public Long getTo() {
        return to;
    }

    /**
     * @param to
     */
    public void setTo(Long to) {
        this.to = to;
    }

    /**
     * @return price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return num
     */
    public Integer getNum() {
        return num;
    }

    /**
     * @param num
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state
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
}