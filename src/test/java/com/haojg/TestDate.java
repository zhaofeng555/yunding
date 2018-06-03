package com.haojg;

import java.util.Calendar;
import java.util.Date;

public class TestDate {
	
	public static void main(String[] args) {
		Date start = new Date(2017, 1, 10);
		Date end = new Date();
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(start);
		int day = startCal.get(Calendar.DAY_OF_YEAR);
		System.out.println(day);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		int endDay = endCal.get(Calendar.DAY_OF_YEAR);
		System.out.println(endDay);
		
	}

}
