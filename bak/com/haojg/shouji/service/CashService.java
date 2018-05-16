package com.haojg.shouji.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.haojg.shouji.bean.Cash;
import com.haojg.shouji.dao.CashDao;
import com.haojg.shouji.form.CashForm;

@Service
public class CashService {

	@Autowired
	CashDao cashDao;
	
	public PageImpl<CashForm> findAllPro(Model m, Pageable pageable, Cash cach) {
		Example<Cash> e = Example.of(cach);
		List<CashForm> list = new ArrayList<>();
		Page<Cash> cashList = cashDao.findAll(e, pageable);
		for (Cash c : cashList) {
			CashForm nf = new CashForm();
			BeanUtils.copyProperties(c, nf);
			list.add(nf);
		}

		PageImpl<CashForm> rs = new PageImpl<>(list, pageable, cashList.getTotalElements());

		m.addAttribute("cashs", rs);
		
		return rs;
	}
	
	public PageImpl<CashForm> findByUserId(Model m, Long userId, Pageable pageable) {
		Cash cach = new Cash();
		cach.setUserId(userId);
		return findAllPro(m, pageable, cach);
	}

	
	
	
}
