package com.haojg.shouji.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.haojg.shouji.bean.User;

public interface UserDao extends JpaRepository<User, Long> ,JpaSpecificationExecutor<User>{

    User findByUsernameAndPassword(String username, String password);
    List<User> findByStatus(Integer status);
    Integer countByCardId(String cardId);
    User findByUsername(String username);
    List<User> findByRecUserIdIn(Long[] recUserIds);
    Long countByRecUserIdIn(Long[] recUserIds);

}
