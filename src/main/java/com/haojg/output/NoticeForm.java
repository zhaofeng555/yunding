package com.haojg.output;

import com.haojg.model.Notice;
import com.haojg.util.DateUtils;

public class NoticeForm extends Notice {

	
	private String createTimeCn;
	private String simpleContent;

	public String getCreateTimeCn() {
		if(getCreateTime()==null) {
			return "";
		}
		return DateUtils.getCurrentDateTime(getCreateTime());
	}

	public void setCreateTimeCn(String createtimeCn) {
		this.createTimeCn = createtimeCn;
	}

	public String getSimpleContent() {
		String conTmp = getContent();
		if(conTmp == null || conTmp.length()<100){
			return conTmp;
		}
		return conTmp.substring(0, 100).concat("......");
	}
	
}
