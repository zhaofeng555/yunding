package com.haojg.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSONObject;

public class WebMiscMethod {

	public static Integer getInt(HttpServletRequest request, String name){
		String valStr = request.getParameter(name);
		if(NumberUtils.isNumber(valStr)){
			return Integer.valueOf(valStr);
		}else{
			return null;
		}
	}
	public static int getInt(HttpServletRequest request, String name, int defval){
		String valStr = request.getParameter(name);
		if(NumberUtils.isNumber(valStr)){
			return Integer.valueOf(valStr);
		}else{
			return defval;
		}
	}
	
	public static Integer[] getNumArr(HttpServletRequest request, String name){
		String valStrs = request.getParameter(name);
		String valStrArr[] =  valStrs.split(",");
		Integer valArr[]=new Integer[valStrArr.length];
		for (int i=0; i<valStrArr.length; i++) {
			valArr[i]=Integer.valueOf(valStrArr[i]);
		}
		return valArr;
	}
	public static String[] getArr(HttpServletRequest request, String name){
		String valStrs = request.getParameter(name);
		String valStrArr[] =  valStrs.split(",");
		return valStrArr;
	}
	
	public static Date getDate(HttpServletRequest request, String name) throws ParseException{
		return getDate(request, name, "yyyyMMdd");
	}
	public static Date getDate(HttpServletRequest request, String name, String pattern) throws ParseException{
		String valStr = request.getParameter(name);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(valStr);
	}
	public static Long getLong(HttpServletRequest request, String name){
		String valStr = request.getParameter(name);
		if(NumberUtils.isNumber(valStr)){
			return Long.valueOf(valStr);
		}else{
			return null;
		}
	}
	public static Long getLong(HttpServletRequest request, String name, Long value){
		String valStr = request.getParameter(name);
		if(NumberUtils.isNumber(valStr)){
			return Long.valueOf(valStr);
		}else{
			return value;
		}
	}
	public static String getStr(HttpServletRequest request, String name){
		return request.getParameter(name);
	}
	public static String getStr(HttpServletRequest request, String name, String defaultVal){
		String valStr = getStr(request, name);
		if(StringUtils.isBlank(valStr)){
			return defaultVal;
		}
		return valStr;
	}
	public static Boolean getBoolean(HttpServletRequest request, String name){
		String valStr = getStr(request, name);
		return Boolean.valueOf(valStr);
	}
	public static Boolean getBoolean(HttpServletRequest request, String name, boolean defVal){
		String valStr = getStr(request, name);
		if(StringUtils.isBlank(valStr)){
			return defVal;
		}
		return Boolean.valueOf(valStr);
	}
	
	public static Integer getLimit(HttpServletRequest request){
		return getInt(request, "limit");
	}
	public static Integer getOffset(HttpServletRequest request){
		return getInt(request, "offset");
	}

	public static String getSearch(HttpServletRequest request){
		return request.getParameter("search");
	}
	
	public static Integer getEndIndex(Integer total, Integer pageSize, Integer offset){
		Integer endIndex = pageSize+offset;
		return Math.min(total, endIndex);
	}
	
	public static <T>List<T> getResult(List<T> list,
			int totalRows, Integer offset, Integer pageSize) {
		Integer endIndex = WebMiscMethod.getEndIndex(totalRows, pageSize, offset);
		list = list.subList(offset, endIndex);
		return list;
	}
	
	public static void writeJson(HttpServletResponse response, Object rs){
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			if(rs instanceof String){
				out.println(rs);
			}else{
				out.println(JSONObject.toJSONString(rs, true));
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  
	
	//输出XML文档
	public static void writeXml(HttpServletResponse response, String xml) throws IOException{
		response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml");
			 PrintWriter out = response.getWriter();
			 out.print(xml);
			 out.flush();
			 out.close();
		}
	
	public static int getTotalPages(int records, int limit) {
		if(records <= 0) {
			return 0;
		}
		int totalPages = 0;
		totalPages = records / Math.max(limit, 1);
		totalPages = records % Math.max(limit, 1) == 0 ? totalPages : totalPages + 1;
		return totalPages;
	}
	
	public static int getRecordStart(int records, int page, int limit) {
		int totalPages =getTotalPages(records, limit);
		if (page > totalPages) {
			page = totalPages;
		}
		int start = (page - 1) * limit;
		//mysql中start不能为负
		if (start < 0) {
			start = 0;
		}
		return start;
	}
	public static String getOrderby(HttpServletRequest request) {
		String orderby2 = request.getParameter("orderby");
		if(StringUtils.isNotEmpty(orderby2)) {
			//如果存在自定义的排序方式，则使用这种，反之使用列表默认的排序方式
			return orderby2;
		}
		String orderby = request.getParameter("sidx"); //排序字段
		String sord = request.getParameter("sord"); //升降
		return StringUtils.isNotBlank(orderby) ? orderby + " " + StringUtils.defaultIfEmpty(sord,"") : "";
	}
	public static void setSuccessMessage(HttpServletRequest request, String msg) {
		request.setAttribute("success", true);
		request.setAttribute("msg", msg);
	}
	public static void setErrorMessage(HttpServletRequest request, String msg) {
		request.setAttribute("error", true);
		request.setAttribute("msg", msg);
	}
	
	
	
}
