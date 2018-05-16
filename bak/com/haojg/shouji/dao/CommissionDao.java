package com.haojg.shouji.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haojg.shouji.bean.Commission;

public interface CommissionDao extends JpaRepository<Commission, Long> {
}
