package com.rest.service.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.entity.Collection;
import com.rest.service.dao.CollectionDao;
import com.util.CommUtils;
import com.util.CurrentPage;

public class CollectionDaoImpl implements CollectionDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	// 加入收藏
	public int insertCollection(Collection c, String token) {
		int rest = 0;
		String sql = "select * from collection where UDID ='" + c.getUdid() + "' and COMP_ID =" + c.getCompid();
		List<Map<String, Object>> queryList = jt.queryForList(sql);
		// 判断是否已收藏 //queryList.equals("")
		if (queryList.size() == 0) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			// 加入收藏
			String sql1 = "insert into collection (UDID,COMP_ID,CREATE_TIME,DIST) values ('" + c.getUdid() + "'," + c.getCompid() + ",'" + ctime + "','" + c.getDist() + "')";
			rest = jt.update(sql1);
			if (rest < 0) {
				rest = -1;
			}
		} else {
			rest = -2;
		}
		return rest;
	}
	// 加入收藏老师
	public int insertCollectionTeacher(Collection c, String token) {
		int rest = 0;
		String sql = "select * from collection where UDID ='" + c.getUdid() + "' and TEACHER_ID =" + c.getCompid();
		List<Map<String, Object>> queryList = jt.queryForList(sql);
		// 判断是否已收藏 //queryList.equals("")
		if (queryList.size() == 0) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			// 加入收藏
			String sql1 = "insert into collection (UDID,TEACHER_ID,CREATE_TIME) values ('" + c.getUdid() + "'," + c.getCompid() + ",'" + ctime + "')";
			rest = jt.update(sql1);
			if (rest < 0) {
				rest = -1;
			}
		} else {
			rest = -2;
		}
		return rest;
	}

	// 取消收藏
	public int deleteCollection(Collection c, String token) {
		int rest = 0;
		try {
			String sql = "select * from collection where UDID ='" + c.getUdid() + "' and COMP_ID =" + c.getCompid();
			List<Map<String, Object>> queryList = jt.queryForList(sql);
			// 判断是否已收藏
			if (queryList.size() > 0) {
				// 取消收藏
				String sql1 = "delete from collection where UDID ='" + c.getUdid() + "' and COMP_ID =" + c.getCompid();
				rest = jt.update(sql1);
				if (rest < 0) {
					rest = -1;
				}
			}
			// 用户不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return rest;
	}

	// 取消收藏列表
	public int deleteCollections(List<String> list, String udid, String type) {
		int rest = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < list.size() ; i++) {
			if (i < list.size() - 1) {
				sb.append(list.get(i));
				sb.append(",");
			}else {
				sb.append(list.get(i));
			}
		}
		sb.append(")");
		if ("com".equals(type)) {
			try {
				String sql = "select * from collection where UDID ='" + udid + "' and COMP_ID in " + sb.toString();
				//System.out.println(sql);
				List<Map<String, Object>> queryList = jt.queryForList(sql);
				// 判断是否已收藏
				if (queryList.size() > 0) {
					// 取消收藏
					String sql1 = "delete from collection where UDID ='" + udid + "' and COMP_ID in" + sb.toString();
					//System.out.println(sql1);
					rest = jt.update(sql1);
					if (rest < 0) {
						rest = -1;
					}
				}
				// 用户不存在返回空
			} catch (Exception e) {
				if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
					return 0;
			}
		}else {
			try {
				String sql = "select * from collection where UDID ='" + udid + "' and TEACHER_ID in " + sb.toString();
				//System.out.println(sql);
				List<Map<String, Object>> queryList = jt.queryForList(sql);
				// 判断是否已收藏
				if (queryList.size() > 0) {
					// 取消收藏
					String sql1 = "delete from collection where UDID ='" + udid + "' and TEACHER_ID in" + sb.toString();
					//System.out.println(sql1);
					rest = jt.update(sql1);
					if (rest < 0) {
						rest = -1;
					}
				}
				// 用户不存在返回空
			} catch (Exception e) {
				if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
					return 0;
			}
		}
		
		return rest;
	}

	// 收藏作文列表
	public List<Map<String, Object>> findCollection(String udid, String numPerPage, String currentPage) {
		try {
			CurrentPage cp = new CurrentPage();
			// 024 素材类型 026 技法类型 013 范文类型 015 推荐作文
			String sql = "SELECT a.ID sign ,a.NAME title , b.DIST dist,b.CREATE_TIME createTime, a.CONTENT content FROM t_res_composion a,collection b where b.COMP_ID=a.ID and b.DIST='013' and b.UDID='" + udid + "' UNION ALL SELECT a.ID sign ,a.NAME title , b.DIST dist,b.CREATE_TIME createTime, a.CONTENT content FROM t_res_composion_sc a,collection b where b.COMP_ID=a.ID and b.DIST='024' and b.UDID='" + udid + "' UNION ALL SELECT a.ID sign ,a.NAME title , b.DIST dist,b.CREATE_TIME createTime, a.CONTENT content FROM t_res_composion_jf a,collection b where b.COMP_ID=a.ID and b.DIST='026' and b.UDID='" + udid
					+ "' UNION ALL SELECT a.ID sign ,a.NAME title , b.DIST dist,b.CREATE_TIME createTime, a.CONTENT content FROM recommend_com a,collection b where b.COMP_ID=a.ID and b.DIST='015' and b.UDID='" + udid + "' order by createTime desc ";
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> queryList = cp.getResultList();
			if (queryList != null && queryList.size() > 0) {
				for (int i = 0; i < queryList.size(); i++) {
					int id = Integer.parseInt(queryList.get(i).get("sign").toString());
					String type = queryList.get(i).get("dist").toString();
					// 查询阅读人数
					String sql1 = "select READ_NUM from read_no where TYPE='" + type + "' and COMP_ID=" + id;
					int readNum = 0;
					List<Map<String, Object>> list = jt.queryForList(sql1);
					if (list != null && list.size() > 0 && list.get(0).get("READ_NUM") != null) {
						readNum = (int) list.get(0).get("READ_NUM");
					}
					queryList.get(i).put("number", readNum + "");
					if (queryList.get(i).get("content") != null && !TextUtils.isEmpty(queryList.get(i).get("content").toString())) {
						queryList.get(i).put("content", CommUtils.getArticleBrief(queryList.get(i).get("content").toString()));
					}
				}
				cp.setResultList(queryList);
			}
			return cp.getResultList();
			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return new ArrayList<>();
		}
		return new ArrayList<>();
	}

	// 收藏总数
	public String countCollection(String udid) {
		// int rest = 0;
		String sql = "select COUNT(ID) from collection where UDID ='" + udid + "'";
		String count = jt.queryForObject(sql, String.class);
		// rest=Integer.parseInt(count);
		return count;
	}

	//收藏老师列表 
	@Override
	public List<Map<String, Object>> findTlist(String udid, String numPerPage,
			String currentPage) {
		String sql1 = "SELECT c.TEACHER_ID id FROM collection c,teacher t WHERE c.UDID = ? AND c.TEACHER_ID IS NOT NULL AND c.TEACHER_ID = t.ID AND t.AUSTATE = 1 ";
		List<Map<String,Object>> list1 = jt.queryForList(sql1, new Object[]{udid});
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list1) {
			Map<String, Object> jo = new HashMap<String, Object>();
			String sql2 = "select COUNT(comp.ID) from  com_composition comp where comp.TEACHER_ID= ?";
			String sql3 = "SELECT count(*) count FROM student s,s_order o,teacher t WHERE o.TEACHER_ID = t.ID AND t.ID = ? AND o.STATE = 2 AND o.STU_ID = s.ID";
			String sql4 = "SELECT EDU_TIME edutime,Name name,PRICE price,STATE state,GRADE,grade,HAND_URL url,ID tid FROM teacher WHERE id = ?";
			String count = jt.queryForObject(sql2, new Object[] { map.get("id") }, String.class);
			String delayCount = jt.queryForObject(sql3, new Object[] { map.get("id") }, String.class);
			List<Map<String,Object>> queryForList = jt.queryForList(sql4, new Object[]{map.get("id")});
			for (int i = 0; i < queryForList.size(); i++) {
				if (delayCount != null) {
					jo.put("delayNo", delayCount);
				}
				jo.put("tid", map.get("id"));// 老师唯一标识
				if (count != null) {
					jo.put("comNo", count);
					if (queryForList.get(i).get("edutime") == null) {
						jo.put("edutime", "");
					} else {
						jo.put("edutime", queryForList.get(i).get("edutime").toString());// 教龄
					}
					if (queryForList.get(i).get("name") == null) {
						jo.put("name", "");
					} else {
						jo.put("name", queryForList.get(i).get("name").toString());// 老师姓名
					}
					if (queryForList.get(i).get("price") == null) {
						jo.put("price", "");
					} else {
						jo.put("price", queryForList.get(i).get("price").toString());// 价格
					}
					if (queryForList.get(i).get("state") == null) {
						jo.put("state", "");
					} else {
						if (new Integer(queryForList.get(i).get("state").toString()) == 1) {// 状态
							jo.put("state", "正常");
						} else if (new Integer(queryForList.get(i).get("state").toString()) == 2) {// 状态
							jo.put("state", "休息");
						}
					}
					if (queryForList.get(i).get("grade") == null) {
						jo.put("grade", "");
					} else {
						jo.put("grade", queryForList.get(i).get("grade").toString());
					}
					jo.put("url", CommUtils.judgeUrl(queryForList.get(i).get("url")));
					list.add(jo);
				}
			}
		}
			return list;
	}

	@Override
	public int deleteCollectionTeacher(String sign, String udid) {
		String sql = "DELETE FROM collection WHERE UDID = ? AND TEACHER_ID = ?";
		return jt.update(sql, new Object[]{udid, sign});
	}
}
