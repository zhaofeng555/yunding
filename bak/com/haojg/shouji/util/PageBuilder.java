package com.haojg.shouji.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 仅仅只是对size进行了封装，太过简单
 * 
 * @author konghao
 *
 */
public class PageBuilder {

	public static final int size = 15;

	public static Pageable generate(int page, int size, Sort sort) {
		if (sort == null)
			return new PageRequest(page, size);
		return new PageRequest(page, size, sort);
	}

	public static Pageable generate(int page, int size) {
		Sort sort = SortBuilder.generateSort("id_d");
		return new PageRequest(page, size, sort);
	}
	
	public static Pageable generate(HttpServletRequest request) {
		
		Integer page = WebMiscMethod.getInt(request, "page", 0);
		if(page<0) {
			page = 0;
		}
		Integer size = WebMiscMethod.getInt(request, "limit", 10);
		if(size<0) {
			size=10;
		}
		Sort sort = SortBuilder.generateSort("id_d");
		return new PageRequest(page, size, sort);
	}

	public static Pageable generate(int page) {
		return generate(page, size, null);
	}

	public static Pageable generate(int page, Sort sort) {
		return generate(page, size, sort);
	}
}
