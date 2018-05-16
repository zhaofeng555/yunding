package com.haojg.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import mybatis.customer.CustomerMapper;
import tk.mybatis.mapper.entity.Example;

public abstract class CustomService<T>{

	public abstract CustomerMapper<T> getMapper();

	public T getOne(Object id) {
		return getMapper().selectByPrimaryKey(id);
	}

	public T selectOne(T record) {
		return getMapper().selectOne(record);
	}
	public List<T> getAll(Class<T> clazz) {
		return getListByExample(new Example(clazz));
	}

	public List<T> getPageList(Class<T> clazz, Page<T> page) {
		if (page == null) {
			return getAll(clazz);
		}

		Example e = new Example(clazz);
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
	
	public List<T> getPageList(Class<T> clazz, Integer pageNum, Integer pageSize){
		Page<T> page = new Page<>(pageNum, pageSize);
		return getPageList(clazz, page);
	}
	
	public Integer count(Example e) {
		return  getMapper().selectCountByExample(e);
	}
	
	public Integer count(Class<T> clazz) {
		Example e = new Example(clazz);
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
	
}
