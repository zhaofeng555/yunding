package com.haojg.shouji.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	/**
	 * 服务器当前时间，格式：yyyy-MM-dd HH:mm:ss
	 * @param date 时间
	 * @return
	 */
	public static String getCurrentDateTime(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 服务器当前时间，格式：yyyy-MM-dd
	 * @param date 时间
	 * @return
	 */
	public static String getCurrentDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 服务器当前时间，格式：自定义
	 * @param date 时间
	 * @param format 日期格式
	 * @return
	 */
	public static String dateToStr(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static Long getYyyyMMddHHmmss(Long currentTimeMillis) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return Long.parseLong(format.format(currentTimeMillis));
	}
	public static int daysBetween(Date smdate,Date bdate) throws ParseException{    
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	    smdate=sdf.parse(sdf.format(smdate));  
	    bdate=sdf.parse(sdf.format(bdate));  
	    Calendar cal = Calendar.getInstance();    
	    cal.setTime(smdate);    
	    long time1 = cal.getTimeInMillis();                 
	    cal.setTime(bdate);    
	    long time2 = cal.getTimeInMillis();         
	    long between_days=(time2-time1)/(1000*3600*24);  
	        
	   return Integer.parseInt(String.valueOf(between_days));           
	}

}
