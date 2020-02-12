package com.rest.service.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.InformationDao;
import com.util.CommUtils;
import com.util.CurrentPage;

public class InformationDaoImpl implements InformationDao {
	private static Logger logger = Logger.getLogger(InformationDaoImpl.class);
	public List<Map<String, Object>> findInformation(String udid, String state, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> map = null;
			if ("1".equals(state)) {// 学生消息
				String sql = "SELECT * FROM student WHERE UDID ='" + udid + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql);
				if (queryForList != null && queryForList.size() > 0) {
					String sql1 = "SELECT * FROM information WHERE DELETE_STATE =0 and STATE= " + state + " and STU_ID=" + queryForList.get(0).get("ID") + " order by CREATE_TIME desc  ";
					cp.Page(sql1, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
					List<Map<String, Object>> list1 = cp.getResultList();
					if (list1 != null && list1.size() > 0) {
						for (int i = 0; i < list1.size(); i++) {
							map = new HashMap<String, Object>();
							if (list1.get(i).get("ICON") == (null)) {
								map.put("icon", "");
							} else {
								map.put("icon", list1.get(i).get("ICON").toString());
							}
							if (list1.get(i).get("INF_CONTENT") == (null)) {
								map.put("infc", "");
							} else {
								map.put("infc", list1.get(i).get("INF_CONTENT").toString());
							}
							if (list1.get(i).get("ID") == (null)) {
								map.put("infid", "");
							} else {
								map.put("infid", list1.get(i).get("ID").toString());
							}
							if (list1.get(i).get("TITLE") == (null)) {
								map.put("title", "");
							} else {
								map.put("title", list1.get(i).get("TITLE").toString());
							}
							map.put("time", CommUtils.ObjectTime2String(list1.get(i).get("CREATE_TIME")));
							String sql5 = "update information set UNREAD = '1' where ID = '" + list1.get(i).get("ID").toString() + "'";
							jt.update(sql5);
							list.add(map);
						}
						return list;
					}
				}
			} else {// 老师消息
				String sql = "SELECT * FROM teacher WHERE UDID ='" + udid + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql);
				if (queryForList.size() == 0) {
					return null;
				}
				String sql1 = "SELECT * FROM information WHERE DELETE_STATE = 0 and STATE= " + state + " and TEACHER_ID=" + queryForList.get(0).get("ID") + " order by CREATE_TIME desc  ";
				cp.Page(sql1, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
				List<Map<String, Object>> list1 = cp.getResultList();
				if (list1 != null && list1.size() > 0) {
					for (int i = 0; i < list1.size(); i++) {
						map = new HashMap<String, Object>();
						if (list1.get(i).get("ICON") == (null)) {
							map.put("icon", "");
						} else {
							map.put("icon", list1.get(i).get("ICON").toString());
						}
						if (list1.get(i).get("INF_CONTENT") == (null)) {
							map.put("infc", "");
						} else {
							map.put("infc", list1.get(i).get("INF_CONTENT").toString());
						}
						if (list1.get(i).get("ID") == (null)) {
							map.put("infid", "");
						} else {
							map.put("infid", list1.get(i).get("ID").toString());
						}
						if (list1.get(i).get("TITLE") == (null)) {
							map.put("title", "");
						} else {
							map.put("title", list1.get(i).get("TITLE").toString());
						}
						map.put("time", CommUtils.ObjectTime2String(list1.get(i).get("CREATE_TIME")));
						String sql5 = "update information set UNREAD = '1' where ID = '" + list1.get(i).get("ID").toString() + "'";
						jt.update(sql5);
						list.add(map);
					}
					
					return list;
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	// 未读消息
	public List<Map<String, Object>> readInformation(String token, String state, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (state.equals("1")) {// 学生消息
				String sql = "SELECT * FROM student WHERE UDID ='" + token + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql);
				if (queryForList.size() == 0) {
					return null;
				}
				String sql1 = "SELECT * FROM information WHERE UNREAD=0 and DELETE_STATE =0 and STATE= " + state + " and STU_ID=" + queryForList.get(0).get("ID");
				cp.Page(sql1, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
				List<Map<String, Object>> list1 = cp.getResultList();
				if (list1.size() > 0) {
					for (int i = 0; i < list1.size(); i++) {
						if (list1.get(0).get("ICON") == (null)) {
							map.put("icon", "");
						} else {
							map.put("icon", list1.get(i).get("ICON").toString());
						}
						if (list1.get(0).get("INF_CONTENT") == (null)) {
							map.put("infc", "");
						} else {
							map.put("infc", list1.get(i).get("INF_CONTENT").toString());
						}
						if (list1.get(0).get("ID") == (null)) {
							map.put("infid", "");
						} else {
							map.put("infid", list1.get(i).get("ID").toString());
						}
						map.put("time", CommUtils.ObjectTime2String(list1.get(i).get("CREATE_TIME")));
						String sql5 = "update information set UNREAD = '1' where ID = '" + list1.get(i).get("ID").toString() + "'";
						jt.update(sql5);
						list.add(map);
					}
					return list;
				}
			} else {// 老师消息
				String sql = "SELECT * FROM teacher WHERE UDID ='" + token + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql);
				if (queryForList.size() == 0) {
					return null;
				}
				String sql1 = "SELECT * FROM information WHERE UNREAD=0 and DELETE_STATE =0 and STATE= " + state + " and TEACHER_ID=" + queryForList.get(0).get("ID");
				cp.Page(sql1, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
				List<Map<String, Object>> list1 = cp.getResultList();
				if (list1.size() > 0) {
					for (int i = 0; i < list1.size(); i++) {
						if (list1.get(0).get("ICON") == (null)) {
							map.put("icon", "");
						} else {
							map.put("icon", list1.get(i).get("ICON").toString());
						}
						if (list1.get(0).get("INF_CONTENT") == (null)) {
							map.put("infc", "");
						} else {
							map.put("infc", list1.get(i).get("INF_CONTENT").toString());
						}
						if (list1.get(0).get("ID") == (null)) {
							map.put("infid", "");
						} else {
							map.put("infid", list1.get(i).get("ID").toString());
						}
						map.put("time", CommUtils.ObjectTime2String(list1.get(i).get("CREATE_TIME")));
						String sql5 = "update information set UNREAD = '1' where ID = '" + list1.get(i).get("ID").toString() + "'";
						int rest = jt.update(sql5);
						list.add(map);
					}
					return list;
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	@Override
	public int unreadStatus(String udid, String state) {
		int unReadNum = 0;
		try {
			if ("1".equals(state)) {// 学生消息
				String sql = "SELECT * FROM student WHERE UDID ='" + udid + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql);
				String sql1 = "SELECT * FROM information WHERE UNREAD = 0 and DELETE_STATE =0 and STATE= " + state + " and STU_ID = " + queryForList.get(0).get("ID");
				List<Map<String, Object>> resultList = jt.queryForList(sql1);
				if (resultList != null && resultList.size() > 0) {
					return resultList.size();
				}
			} else {// 老师消息
				String sql = "SELECT * FROM teacher WHERE UDID ='" + udid + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql);
				String sql1 = "SELECT * FROM information WHERE UNREAD = 0 and DELETE_STATE = 0 and STATE = " + state + " and TEACHER_ID = " + queryForList.get(0).get("ID");
				List<Map<String, Object>> resultList = jt.queryForList(sql1);
				if (resultList != null && resultList.size() > 0) {
					return resultList.size();
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return unReadNum;
		}
		return unReadNum;
	}

	public int deleteInformation(List<String> list) {
		int rest = 0;
		try {
			for (int i = 0; i < list.size(); i++) {
				String sql1 = "update information set DELETE_STATE=? where ID=?";
				Object[] params = new Object[] { "1", list.get(i) };
				rest = jt.update(sql1, params);
			}

		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return rest;
		}
		return rest;
	}

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	/* (non Javadoc) 
	 * @Title: changeBadge
	 * @Description: TODO
	 * @param udid 
	 * @see com.rest.service.dao.InformationDao#changeBadge(java.lang.String) 
	 */
	@Override
	public void changeBadge(String udid) {
		try {
			String sql = "update teacher set badge = '0' where udid = ?";
			jt.update(sql, new Object[]{udid});
		} catch (Exception e) {
			logger.info("-------------载入首页更新教师badge失败---------------");
		}
	}

}
