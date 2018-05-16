package com.haojg.shouji.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haojg.shouji.bean.Cash;

public interface CashDao extends JpaRepository<Cash, Long> {
}
