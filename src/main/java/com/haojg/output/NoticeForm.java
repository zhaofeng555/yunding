package com.haojg.output;

import com.haojg.model.Notice;
import com.haojg.util.DateUtils;

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
