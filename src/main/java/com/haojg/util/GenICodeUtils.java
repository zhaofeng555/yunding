package com.haojg.util;

import java.util.Random;
import java.util.UUID;

public class GenICodeUtils {

	public static String getIdentifyCode(){
		StringBuilder buf = new StringBuilder();
	     Random random = new Random() ;
	     for(int i=0;i<4;i++){
	    	 buf.append(random.nextInt(10)) ;
	     }
	     return buf.toString();
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getIdentifyCode(int n){
		StringBuilder buf = new StringBuilder();
	     Random random = new Random() ;
	     for(int i=0;i<n;i++){
	    	 buf.append(random.nextInt(9)+1) ;
	     }
	     return buf.toString();
	}
}
