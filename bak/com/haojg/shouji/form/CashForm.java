package com.haojg.shouji.form;

import com.haojg.shouji.bean.Cash;
import com.haojg.shouji.util.DateUtils;

public class CashForm extends Cash {

	private String createTimeCn;
	private String statusCn;
	
	

	public String getStatusCn() {
		if(getStatus() == null) {
			return "-";
		}
		return StatusConst.cash_status[getStatus()];
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public String getCreateTimeCn() {
		if (getCreateTime() == null) {
			return "";
		}
		return DateUtils.getCurrentDateTime(getCreateTime());
	}

	public void setCreateTimeCn(String createTimeCn) {
		this.createTimeCn = createTimeCn;
	}

}
