package com.pc.manage.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
/**
 * 修改密码
 */
public class ManageUpdateDaoImpl {
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
		
	public int updatePw(String oldPw, String newPw, int id) {
		String sql = "update manage_users set PASSWORD = '"+ newPw +"' where ID = " + id ;
		int i = jt.update(sql);
		return i;
	}

	
}
