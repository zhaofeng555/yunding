package com.haojg.shouji.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_rec_user")
@Data
public class RecUser {

    @GeneratedValue
    @Id()
    Long id;
    Long userId;
    Long recUserId;
    Date createTime;
    String remark;
}
