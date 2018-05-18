package com.haojg.service;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import mybatis.customer.CustomerMapper;
import tk.mybatis.mapper.entity.Example;

public abstract class CustomService<T>{

	public abstract CustomerMapper<T> getMapper();

	public T getOne(Long id) {
		return getMapper().selectByPrimaryKey(id);
	}

	public T selectOne(T record) {
		return getMapper().selectOne(record);
	}
	public List<T> getAll() {
		return getListByExample(new Example(entityClass));
	}

	public List<T> getPageList(Page<T> page) {
		if (page == null) {
			return getAll();
		}

		Example e = new Example(entityClass);
		int offset = (page.getPageNum()-1) * page.getPageSize();
		int limit = page.getPageSize();
		RowBounds row = new RowBounds(offset, limit);

		if (StringUtils.isNotBlank(page.getOrderBy())) {
			if (page.isOrderByOnly()) {
				e.orderBy(page.getOrderBy()).asc();
			} else {
				e.orderBy(page.getOrderBy()).desc();
			}
		}

		return getMapper().selectByExampleAndRowBounds(e, row);
	}
	
	public List<T> getPageList(Integer pageNum, Integer pageSize){
		Page<T> page = new Page<>(pageNum, pageSize);
		return getPageList(page);
	}
	
	public Integer count(Example e) {
		return  getMapper().selectCountByExample(e);
	}
	
	public Integer count() {
		Example e = new Example(entityClass);
		return  getMapper().selectCountByExample(e);
	}

	public List<T> getPageList(Example e, Page<T> page) {
		if (page == null) {
			return getMapper().selectByExample(e);
		}

		int offset = (page.getPageNum() - 1) * page.getPageSize();
		int limit = page.getPageSize();
		RowBounds row = new RowBounds(offset, limit);

		if (StringUtils.isNotBlank(page.getOrderBy())) {
			if (page.isOrderByOnly()) {
				e.orderBy(page.getOrderBy()).asc();
			} else {
				e.orderBy(page.getOrderBy()).desc();
			}
		}

		return getMapper().selectByExampleAndRowBounds(e, row);
	}

	public List<T> getListByExample(Example e) {
		return getMapper().selectByExample(e);
	}

	@Transactional
	public int delete(Object id) {
		return getMapper().deleteByPrimaryKey(id);
	}
	@Transactional
	public int delete(Example e) {
		return getMapper().deleteByExample(e);
	}

	@Transactional
	public int updateByPrimaryKeySelective(T record) {
		return getMapper().updateByPrimaryKeySelective(record);
	}

	@Transactional
	public int updateByPrimaryKey(T record) {
		return getMapper().updateByPrimaryKey(record);
	}

	@Transactional
	public int insert(T record) {
		return getMapper().insert(record);
	}

	@Transactional
	public int insertSelective(T record) {
		return getMapper().insertSelective(record);
	}

	@Transactional
	public int insertList(List<T> recordList) {
		return getMapper().insertList(recordList);
	}
	
	@Transactional
	public int saveOrUpdate(T record) throws Exception {
		
		Method getId=entityClass.getDeclaredMethod("getId");
		Object id = getId.invoke(record);
		if(id == null) {
			return getMapper().insertSelective(record);
		}else {
			return getMapper().updateByPrimaryKeySelective(record);
		}
	}
	
	protected Class<T> entityClass;

	@PostConstruct
	public void init() {
		Type genType = getClass().getGenericSuperclass();  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
        entityClass = (Class) params[0];  
	}
	
}
