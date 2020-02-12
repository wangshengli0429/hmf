package com.rest.service.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.RedDao;
import com.util.CurrentPage;

public class RedDaoImpl implements RedDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	// 红包
	public List<Map<String, Object>> findRedPacket(String udid, String fail, String numPerPage, String currentPage) {
		try {
			int failure = Integer.parseInt(fail);// FAILURE 1失效0有效 REDUSE 1使用0未使用
			
			int startNum = (Integer.parseInt(currentPage) - 1) * Integer.parseInt(numPerPage);
			int countNum = Integer.parseInt(numPerPage);
			
			if (failure == 1) { 
				String sql = "select * from red_packet where UDID = ? and (REDUSE = 1 or FAILURE = 1) ORDER BY START_TIME LIMIT ?,?";
				List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid, startNum, countNum});
				if (list != null && list.size() > 0) {
					return list;
				}
			}
			if (failure == 0) { 
				String sql = "select * from red_packet where UDID = ? and REDUSE = 0 and FAILURE = 0 ORDER BY START_TIME LIMIT ?,?";
				System.out.println(sql);
				List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid, startNum, countNum});
				if (list != null && list.size() > 0) {
					return list;
				}
			}

			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}
	
	// 红包
	public List<Map<String, Object>> findRedPacket2(String udid, String fail, String numPerPage, String currentPage) {
		try {
			int failure = Integer.parseInt(fail);// FAILURE 1失效0有效 REDUSE 1使用0未使用
			
			int startNum = (Integer.parseInt(currentPage) - 1) * Integer.parseInt(numPerPage);
			int countNum = Integer.parseInt(numPerPage);
			
			if (failure == 0) { //0未使用（在有效期内）
				String sql = "select * from red_packet where UDID = ? and REDUSE = 0 and FAILURE = 0 ORDER BY START_TIME LIMIT ?,?";
				List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid, startNum, countNum});
				if (list != null && list.size() > 0) {
					return list;
				}
			}else if (failure == 1) { //1已使用（因使用过而不可用）
				String sql = "select * from red_packet where UDID = ? and REDUSE = 1 and FAILURE = 0 ORDER BY START_TIME LIMIT ?,?";
				List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid, startNum, countNum});
				if (list != null && list.size() > 0) {
					return list;
				}
			}else if (failure == 2) { //2已失效（因过期而不可用）
				String sql = "select * from red_packet where UDID = ? and FAILURE = 1 and REDUSE = 0 ORDER BY START_TIME LIMIT ?,?";
				List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid, startNum, countNum});
				if (list != null && list.size() > 0) {
					return list;
				}
			}

			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findCard(String udid, String numPerPage,
			String currentPage) {
		//更新月卡状态
		String sql1 = "update card SET failure = 1 where end_time < SYSDATE() AND udid = ?";
		jt.update(sql1, new Object[]{udid});
		//String sql2 = "select * from card where udid = ? and failure = 0 order by end_time desc ";
		String sql2 = "select * from card where udid = ? order by end_time desc ";
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{udid});
		return list;
	}

}
