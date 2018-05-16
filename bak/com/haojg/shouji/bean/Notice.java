package com.haojg.shouji.bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_notice")
public class Notice {
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private Date createTime;

	private String content;


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}