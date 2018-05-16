package com.haojg.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OutpubResult {

	private Integer code;
	private Object data;
	private String message;
	
	public static OutpubResult getSuccess(Object data){
		return new OutpubResult(0, data, null);
	}
	
	public static OutpubResult getError(Integer code, String message){
		return new OutpubResult(code, null, message);
	}
	
	public static OutpubResult getError(String message){
		return new OutpubResult(1, null, message);
	}
	public OutpubResult() {}
	public OutpubResult(Integer code, Object data, String message) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}
	
}
