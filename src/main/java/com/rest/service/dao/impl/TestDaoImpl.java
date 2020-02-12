/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service.dao.impl 
 * @author: think   
 * @date: 2017-12-6 下午3:25:00 
 */
package com.rest.service.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @ClassName: TestDaoImpl
 * @Description: TODO
 * @author: think
 * @date: 2017-12-6 下午3:25:00
 */
public class TestDaoImpl {
	private static Logger logger = Logger.getLogger(TestDaoImpl.class);

	@Test
	public void test() throws Exception {
		System.setProperty("env", "db");
		System.setProperty("scheduler", "scheduler2");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		/*final JdbcTemplate jt = (JdbcTemplate) ctx.getBean("jdbcTemplate");

		String sql1 = "SELECT TOP 1 * FROM composition";
		List<Map<String,Object>> list = jt.queryForList(sql1);
		System.out.println(list);*/
		OtherDaoImpl dao = (OtherDaoImpl) ctx.getBean("otherDao");
		dao.checkRed();

	}

}
