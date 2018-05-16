package com.haojg.shouji.form;

import com.haojg.shouji.bean.Notice;
import com.haojg.shouji.util.DateUtils;

public class NoticeForm extends Notice {

	
	private String createTimeCn;

	public String getCreateTimeCn() {
		if(getCreateTime()==null) {
			return "";
		}
		return DateUtils.getCurrentDateTime(getCreateTime());
	}

	public void setCreateTimeCn(String createtimeCn) {
		this.createTimeCn = createtimeCn;
	}
	
}
