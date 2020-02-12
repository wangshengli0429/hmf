package com.rest.service.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.XiaochengxuDao;
import com.util.CommUtils;

public class XiaochengxuDaoImpl implements XiaochengxuDao {

	private static Logger logger = Logger.getLogger(AppraiseDaoImpl.class);
	private JdbcTemplate jt;
	public JdbcTemplate getJt() {
		return jt;
	}
	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}
	
	@Override
	public String getCodeByPhone(String phone) {
		long curren = System.currentTimeMillis();
        curren -= 30 * 60 * 1000;
        Date date = new Date(curren);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
		
		String sql = "SELECT CODE FROM verificationcode WHERE PHONE = ? AND TIME > ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{phone, time});
		if (list != null && list.size() > 0) {
			return CommUtils.judgeSqlInformation(list.get(0).get("CODE"));
		}
		return null;
	}

	@Override
	public String findGradeByUdid(String udid) {
		String sql = "SELECT GRADE FROM student WHERE UDID = ?";
		String string = "";
		try {
			string = jt.queryForObject(sql, new Object[]{udid}, String.class);
		} catch (Exception e) {
			logger.info("--------年级查询异常-------udid=" + udid);
		}
		return string;
	}
}
