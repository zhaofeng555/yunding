package com.haojg.component;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.haojg.model.IncreaseInfo;
import com.haojg.model.User;
import com.haojg.service.IncreaseInfoService;
import com.haojg.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class IncreaseInfoSysScheduler {

	@Autowired
	UserService userService;
	
	@Autowired
	IncreaseInfoService increaseInfoService;
	
	//每天1点钟触发
    @Scheduled(cron = "0 0/5 * * * ?")
    public void scheduler() {
        log.info(">>>>>>>>>>>>> scheduled ...{}", new Date());
        
        List<User> all = userService.getAll();
        for (User u : all) {
			
        	IncreaseInfo info = new IncreaseInfo();
        	info.setCreateTime(new Date());
        	info.setUserId(u.getId());
        	
        	Double buyNum = u.getBuyNum();
        	Double assets = u.getAssets();
        	
        	Double num = buyNum * 0.01;
        	Double sum = assets + num;
        	info.setNum(num);
        	info.setSum(sum);
        	increaseInfoService.insertSelective(info);        	
        	
        	User updateUser = new User();
        	updateUser.setId(u.getId());
        	updateUser.setAssets(sum);
        	userService.updateByPrimaryKeySelective(updateUser);
        	
		}
        
    }
}

