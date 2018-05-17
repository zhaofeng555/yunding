package com.haojg.controller;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haojg.output.OutpubResult;
import com.haojg.service.CustomService;

public abstract class BaseController<T> {
	
	protected Class entityClass;

	public abstract CustomService<T> getService();

	@PostConstruct
	public void init() {
		Type genType = getClass().getGenericSuperclass();  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
        entityClass = (Class) params[0];  
	}
	
	@RequestMapping(value="/getById", method=RequestMethod.GET)
	@ResponseBody
	public OutpubResult get(Long id) {
		 T data = getService().getOne(id);
		return OutpubResult.getSuccess(data);
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult save(T record) throws Exception {
		
		@SuppressWarnings("unchecked")
		Method getId=entityClass.getDeclaredMethod("getId");
		Object id = getId.invoke(record);
		
		int ct = 0;
		if(id == null) {
			ct = getService().insertSelective(record);
		}else {
			ct = getService().updateByPrimaryKeySelective(record);
		}
		
		if(ct == 0) {
			return OutpubResult.getError("保存失败");
		}
		return OutpubResult.getSuccess(record);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult delete(Long id) {
		int ct = getService().delete(id);
		if(ct == 0) {
			return OutpubResult.getError("删除失败");
		}
		return OutpubResult.getSuccess(id);
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public OutpubResult list(
			@RequestParam(required=false, defaultValue="1")Integer pageNum, 
			@RequestParam(required=false, defaultValue="15")Integer pageSize) {
		List<T> list = getService().getPageList(entityClass, pageNum, pageSize);
		return OutpubResult.getSuccess(list);
	}
	
	@RequestMapping(value="/listing", method=RequestMethod.GET)
	public String list(
			@RequestParam(required=false, defaultValue="1")Integer pageNum, 
			@RequestParam(required=false, defaultValue="15")Integer pageSize,
			ModelMap map) {
		
		String simpleClassName = entityClass.getSimpleName();
		
		List<T> list = getService().getPageList(entityClass, pageNum, pageSize);
		
		Integer sum = getService().count(entityClass);
		Integer total = sum/pageSize;
		if(sum%pageSize != 0){
			++total;
		}
		
		map.put("data", list);
		map.put("pageNum", pageNum);
		map.put("total", total);
		map.put("isFirst", (pageNum<=1));
		map.put("isLast", (pageNum>=total));
		map.put("pageSize", pageSize);
		return simpleClassName+"/list";
	}
	
}
