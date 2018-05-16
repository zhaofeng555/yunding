package com.haojg.shouji.form;

import com.haojg.shouji.bean.User;

public class UserForm extends User {

	private String recUsername;
	private String statusCn;

	public String getRecUsername() {
		return recUsername;
	}

	public void setRecUsername(String recUsername) {
		this.recUsername = recUsername;
	}

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

}
