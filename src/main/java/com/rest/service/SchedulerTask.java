/**   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service 
 * @author: think   
 * @date: 2018-2-1 上午11:08:37 
 */
package com.rest.service;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.rest.service.dao.OtherDao;

/**
 * @ClassName: SchedulerTask 
 * @Description: TODO
 * @author: think
 * @date: 2018-2-1 上午11:08:37  
 */
@Component
public class SchedulerTask {
	
	//@Scheduled(cron = "0/5 * * * * ?")
	public void myScheduled() throws ParseException {
		/*RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	    int id = Integer.valueOf(runtimeMXBean.getName().split("@")[0])  
	                .intValue(); 
	    logger.info("----------当前进程id：" + id + "--------------");*/
	    
		/*ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		String id = context.getId();
		
		logger.info("------------当前ApplicationContext id:"+ id + "-----------");*/
		
	    String name = Thread.currentThread().getName();
	    long l = Thread.currentThread().getId();
	    
	    logger.info("------------当前线程名字:"+name+",id："+l+"------------------------");
	    
		//检查红包过期
		otherDao.checkRed();
		//检查评点卡过期
		otherDao.checkDpCard(30);
		//对老师进行排序
		otherDao.sortTeacher();
		
	}
	private OtherDao otherDao;
	public OtherDao getOtherDao() {
		return otherDao;
	}
	public void setOtherDao(OtherDao otherDao) {
		this.otherDao = otherDao;
	}
	private static Logger logger = Logger.getLogger(SchedulerTask.class);
}
