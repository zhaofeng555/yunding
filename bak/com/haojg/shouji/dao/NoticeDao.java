package com.haojg.shouji.dao;

import com.haojg.shouji.bean.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeDao extends JpaRepository<Notice, Long> {
}
