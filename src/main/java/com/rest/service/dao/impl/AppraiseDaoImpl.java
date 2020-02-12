package com.rest.service.dao.impl;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.entity.Appraise;
import com.rest.service.dao.AppraiseDao;
import com.util.CommUtils;
import com.util.CurrentPage;

public class AppraiseDaoImpl implements AppraiseDao {

	private static Logger logger = Logger.getLogger(AppraiseDaoImpl.class);
	
	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	// 学生提交评价
	public int insertAppraise(Appraise a) {
		try {
			String sql = "select * from student where UDID ='" + a.getUdid() + "'";// 学生表
			List<Map<String, Object>> queryForList = jt.queryForList(sql);
			/*if (queryForList.size() > 0 && queryForList.get(0).get("GRADE") != null) {
				a.setGrade(queryForList.get(0).get("GRADE").toString());// 年级
			}*/
			String sql1 = "select c.NEW_TITLE,c.GEADE from composition c where c.UDID=? and c.ID=?";// 点评作文表、学生表
			List<Map<String, Object>> queryList = jt.queryForList(sql1, new Object[]{a.getUdid(), a.getCompid()});
			String title = "";
			if (queryList != null && queryList.size() > 0) {
				a.setGrade(queryList.get(0).get("GEADE").toString());// 年级
				title = queryList.get(0).get("NEW_TITLE") == null ? "" : queryList.get(0).get("NEW_TITLE").toString();
			}
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);// 评价时间
			a.setAve(((float) a.getAttit() + (float) a.getProfes()) / (float) 2);// AVERAGE评价分
			// ----- 修改教师表里对应教师的 TOTAL_SCORE TOTAL_APPRAISE
			String queryTeacherSql = "select TOTAL_SCORE , TOTAL_APPRAISE  from teacher where ID = ?";
			List<Map<String, Object>> teacherList = jt.queryForList(queryTeacherSql,new Object[]{a.getTeacherid()});
			if (teacherList != null && teacherList.size() > 0) {
				float totalScore = (float) teacherList.get(0).get("TOTAL_SCORE");
				int totalNum = (int) teacherList.get(0).get("TOTAL_APPRAISE");
				totalScore += a.getAve();
				logger.info("-----totalScore分" + totalScore);
				totalNum += 1;
				float ave = (float) totalScore / (float) totalNum;
				String updateTScoreSql = "update teacher set AVE = " + ave + ", TOTAL_SCORE = " + totalScore + " , TOTAL_APPRAISE = " + totalNum + " where ID = " + a.getTeacherid();
				jt.update(updateTScoreSql);// 修改教师的平均分
			}
			// -----
			String sql2 = "insert into appraise(UDID,ATTIT,PROFES,AVERAGE,STU_MESSAGE,SATISFACTION,TEACHER_ID,GRADE,COMP_ID,APPR_TIME) values(?,?,?,?,?,?,?,?,?,?)";
			String sql3 = "INSERT INTO appraise_message (MESSAGE,TIME,USER_TYPE,COMP_ID) VALUES (?,?,?,?)";
			int i = jt.update(sql2, new Object[] { a.getUdid(), a.getAttit(), a.getProfes(), a.getAve() + "", a.getStumessage(), a.getSatisfactoin(), a.getTeacherid(), a.getGrade(), a.getCompid(), ctime }, new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP });
			jt.update(sql3, new Object[]{a.getStumessage(), ctime, "stu", a.getCompid()});
			// 插入消息表
			String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
			jt.update(sql5, new Object[] { "点评被评价", "您评点的作文《" + title + "》已被" + queryForList.get(0).get("NICKNAME") + "同学评价，请到我的点评中查看这篇作文的评价！", ctime, a.getTeacherid(), 2, "5" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
			return i;
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}
	// 学生提交留言
	@Override
	public int insertReply(String aid, String stumessage, String cid, String userTpye) {
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);// 评价时间
			//查询作文标题
			String tid = null;
			String title = null;
			String fid = null;
			String sql1 = "SELECT c.NEW_TITLE title, c.TID tid, a.FATHER_ID fid FROM appraise_message a,composition c WHERE a.ID = ? AND c.ID = ?";
			List<Map<String,Object>> list = jt.queryForList(sql1, new Object[]{aid, cid});
			if (list != null && list.size() > 0) {
				title = CommUtils.judgeSqlInformation(list.get(0).get("title").toString());
				tid = CommUtils.judgeSqlInformation(list.get(0).get("tid").toString());
				if (list.get(0).get("fid") != null && !"".equals(list.get(0).get("fid").toString()) ) {
					fid = list.get(0).get("fid").toString();
				}else {
					fid = aid;
				}
			}
			//插入留言
			String sql2 = "INSERT INTO appraise_message (MESSAGE,TIME,USER_TYPE,COMP_ID,FATHER_ID) VALUES (?,?,?,?,?)";
			int i = 0;
			if (aid == null || aid.equals("")) {
				i = jt.update(sql2, new Object[]{stumessage, ctime, userTpye, cid, null});
			}else {
				i = jt.update(sql2, new Object[]{stumessage, ctime, userTpye, cid, fid});
			}
			// 插入消息表
			if (tid != null && title != null) { 
				if ("stu".equals(userTpye)) {
					String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql3, new Object[] { "新的留言", "您的作文《"+title+"》收到老师的回复信息，请到已点评中查看！", ctime, tid, 2, "5" });
				}else {
					String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql3, new Object[] { "新的留言", "您点评的作文《"+title+"》收到新的留言信息，请到已点评中查看并及时回复！", ctime, tid, 2, "5" });
				}
			}
			return i;
		} catch (Exception e) {
			return 0;
		}
	}

	// 学生评价列表
	public List<Map<String, Object>> findAppraises(String udid, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		try {
			String sql = "SELECT c.DRAFT draft, t.NAME name, a.ID pid, a.APPR_TIME time, c.NEW_TITLE title, t.HAND_URL url, " 
					+ " c.ID cid FROM appraise a, com_composition comp, composition c, teacher t " + "WHERE a.UDID = '" 
					+ udid + "' AND a.COMP_ID = c.ID AND comp.COMP_ID = c.ID AND a.TEACHER_ID = t.ID" 
					+ " ORDER BY time DESC";
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> queryForList = cp.getResultList();
			if (queryForList.size() > 0) {
				for (int i = 0; i < queryForList.size(); i++) {
					queryForList.get(i).put("url", CommUtils.judgeUrl(queryForList.get(i).get("url")));
					queryForList.get(i).put("time", CommUtils.ObjectTime2String(queryForList.get(i).get("time")));
				}
				return queryForList;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}
	
	@Override
	public int alterOrderCommentStatus(int compId) {
		int rest = 0;
		try {
			String sql = "update s_order set HAVE_COMMENT = true WHERE COMP_ID = ?";
			int status = jt.update(sql, new Object[] { compId });
			if (status > 0) {
				return status;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return rest;
		}
		return rest;
	}

	// 查看评价
	@Override
	public List<Map<String, Object>> selectAppraise(int compid, String udid) {
		try {
			String sql = "select a.ATTIT attit,a.AVERAGE ave,a.PROFES profes,a.SATISFACTION sat,a.STU_MESSAGE smessage,a.TEACHER_MESSAGE tmessage,a.APPR_TIME atime,t.NAME name,t.HAND_URL url,c.NEW_TITLE title,c.DRAFT draft,cc.COM_TIME ctime,s.HEAD shead from appraise a,com_composition cc,teacher t ,composition c,student s where a.ID =" + compid + " and a.COMP_ID = c.ID and a.COMP_ID = cc.COMP_ID AND a.TEACHER_ID = t.ID AND a.UDID = s.UDID";
			List<Map<String, Object>> queryForList = jt.queryForList(sql);
			if (queryForList.size() > 0) {
				return queryForList;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	// 根据作文查询评价
	@Override
	public List<Map<String, Object>> queryAppraiseIdByCompId1(int compid) {
		try {
			String sql = "SELECT a.APPR_TIME time,a.ATTIT attit,a.AVERAGE ave,a.PROFES prof,a.SATISFACTION sati,"
					+ "s.HEAD shead,s.NICKNAME sname,t.NAME tname,t.HAND_URL thead,c.NEW_TITLE title,c.DRAFT draft "
					+ "FROM appraise a,student s,teacher t,composition c WHERE a.COMP_ID = c.ID "
					+ "AND a.UDID = s.UDID AND c.ID = ? AND c.TID = t.ID ";
			List<Map<String, Object>> queryForList = jt.queryForList(sql, new Object[]{compid});
			if (queryForList.size() > 0) {
				return queryForList;
			}
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public List<Map<String, Object>> queryAppraiseIdByCompId2(int compid) {
		try {
			String sql = "SELECT * FROM appraise_message WHERE COMP_ID = ? ORDER BY TIME";
			List<Map<String, Object>> queryForList = jt.queryForList(sql, new Object[]{compid});
			if (queryForList.size() > 0) {
				return queryForList;
			}
		} catch (Exception e) {
		}
		return null;
	}

	// 点评详情(老师)
	@Override
	public Map<String, Object> selectComDetails(int compid, String udid) {
		try {
			Map<String, Object> list = new HashMap<String, Object>();
			// 作文clist
			String sql1 = "select c.PROPO ask , c.IMAGE1 image1 , c.IMAGE2 image2 , c.IMAGE3 image3," + " comp.CONTENT content , c.DRAFT draft , c.GEADE grade , comp.SCORE score , c.CREATE_TIME stime , c.NEW_TITLE title" + " from com_composition comp , composition c" + " where comp.ID = " + compid + " and comp.COMP_ID = c.ID" + "ORDER BY stime DESC";
			List<Map<String, Object>> list1 = jt.queryForList(sql1);
			Map<String, Object> clist = new HashMap<String, Object>();
			if (list1.size() > 0) {
				clist.put("content", list1.get(0).get("content").toString());// 批改后作文内容
				clist.put("draft", list1.get(0).get("draft").toString());// 第几稿
				clist.put("grade", list1.get(0).get("grade").toString());// 年级
				clist.put("score", list1.get(0).get("score").toString());// 批改评分
				clist.put("stime", CommUtils.ObjectTime2String(list1.get(0).get("stime")));// 提交时间
				clist.put("title", list1.get(0).get("title").toString());// 作文名称
				list.put("ask", list1.get(0).get("ask").toString());// 要求
				list.put("image1", list1.get(0).get("image1").toString());// 图片1
				list.put("image2", list1.get(0).get("image2").toString());// 图片2
				list.put("image3", list1.get(0).get("image3").toString());// 图片3
				list.put("clist", clist);
			}
			// 作文评分标准cslist
			String sql2 = "select s.CONTENT content , s.LANGUAGE language , s.STRUCTURE structure" + " from com_composition comp , composition c , standard s" + " where comp.ID = " + compid + " and comp.COMP_ID = c.ID and c.GEADE = s.GRADE";
			List<Map<String, Object>> list2 = jt.queryForList(sql2);
			Map<String, Object> cslist = new HashMap<String, Object>();
			if (list2.size() > 0) {
				cslist.put("content", list1.get(0).get("content").toString());// 内容
				cslist.put("language", list1.get(0).get("language").toString());// 语言
				cslist.put("structure", list1.get(0).get("structure").toString());// 结构
				list.put("cslist", cslist);
			}
			// 学生评价slist
			String sql3 = "select a.ATTIT attit , a.AVERAGE ave , a.PROFES prof , a.SATISFACTION sati , a.STU_MESSAGE smessage , a.TEACHER_MESSAGE tmessage" + " from appraise a , teacher t" + " where a.COMP_ID = " + compid + " and t.UDID = '" + udid + "' and t.ID = a.TEACHER_ID";
			List<Map<String, Object>> list3 = jt.queryForList(sql3);
			Map<String, Object> slist = new HashMap<String, Object>();
			if (list3.size() > 0) {
				slist.put("attit", list1.get(0).get("attit").toString());// 负责态度
				slist.put("ave", list1.get(0).get("ave").toString());// 评分
				slist.put("prof", list1.get(0).get("prof").toString());// 专业水平
				slist.put("sati", list1.get(0).get("sati").toString());// 满意度
				slist.put("smessage", list1.get(0).get("smessage").toString());// 学生留言
				slist.put("tmessage", list1.get(0).get("tmessage").toString());// 老师回复
				list.put("slist", slist);
			}
			// 总评sulist
			String sql4 = "select POINTS points , SCORING scoring , SUGGEST suggest , TYPE type" + " from com_composition where ID = " + compid;
			List<Map<String, Object>> list4 = jt.queryForList(sql4);
			Map<String, Object> sulist = new HashMap<String, Object>();
			if (list4.size() > 0) {
				sulist.put("points", list1.get(0).get("points").toString());// 失分点
				sulist.put("scoring", list1.get(0).get("scoring").toString());// 得分点
				sulist.put("suggest", list1.get(0).get("suggest").toString());// 修改建议
				sulist.put("type", list1.get(0).get("type").toString());// 第几类作文
				list.put("sulist", sulist);
			}
			// 老师tlist
			String sql5 = "select comp.COM_TIME atime , t.NAME name , t.HAND_URL url" + " from teacher t , com_composition comp" + " where t.UDID = '" + udid + "' and comp.ID = " + compid + " and comp.TEACHER_ID = t.ID " + "ORDER BY atime DESC";
			List<Map<String, Object>> list5 = jt.queryForList(sql5);
			Map<String, Object> tlist = new HashMap<String, Object>();
			if (list5.size() > 0 && tlist.size() > 0) {
				tlist.put("atime", CommUtils.ObjectTime2String(list1.get(0).get("atime")));// 评价时间
				tlist.put("url", CommUtils.judgeUrl(list1.get(0).get("url") ));// 头像地址
				if (list1.get(0).get("name") == (null)) {
					tlist.put("name", "");
				} else {
					tlist.put("name", list1.get(0).get("name").toString());// 老师姓名
				}
				list.put("tlist", tlist);
			}

			return list;
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	@Override
	public int delAppraise(String aid, String userType) {
		String sql1 = "SELECT USER_TYPE FROM appraise_message WHERE "
				+ "FATHER_ID = (SELECT FATHER_ID FROM appraise_message WHERE ID = ?) "
				+ "AND TIME > (SELECT TIME FROM appraise_message WHERE ID = ?)";
		List<Map<String, Object>> list = jt.queryForList(sql1, new Object[]{aid, aid});
		String newUserType = null;
		if ("stu".equals(userType)) {
			newUserType = "tea";
		}else {
			newUserType = "stu";
		}
		int i = 0;
		int j = 0;
		for (Map<String, Object> map : list) {
			if (Objects.equals(newUserType, map.get("USER_TYPE"))) {
				j = 1;
			}
		}
		if (j == 0){
			String sql = "DELETE FROM appraise_message WHERE ID = ?";
			i = jt.update(sql, new Object[]{aid});
		}
		return i;
	}
	
	@Override
	public int insertReply(Appraise a) {
		// a.setUdid(udid);
		// a.setTeacherid(Integer.parseInt(teacherid));// 老师唯一标识
		// a.setCompid(cid);// 评价的作文的id
		// a.setTeachermessage(content);//教师评价的内容
		// COMP_ID TEACHER_ID TEACHER_MESSAGE MESSAGE_TIME
		try {
			String sql1 = "select c.NEW_TITLE , s.ID from composition c , student s where c.ID=" + a.getCompid() + " and c.UDID = s.UDID ";// 作文表
			List<Map<String, Object>> queryList = jt.queryForList(sql1);
			String title = "";
			if (queryList != null && queryList.size() > 0) {
				title = queryList.get(0).get("NEW_TITLE") == null ? "" : queryList.get(0).get("NEW_TITLE").toString();
				// 作文标题点击查看详情（跳转到已点评页面）
				// icon 5
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ctime = dateFormat.format(now);// 评价时间
				String sql2 = "update appraise set TEACHER_MESSAGE = ? ,MESSAGE_TIME = ?  where COMP_ID = ? and TEACHER_ID = ? ";
				jt.update(sql2, new Object[] { a.getTeachermessage(), ctime, a.getCompid(), a.getTeacherid() });
				//查询id
				String sql4 = "SELECT ID FROM appraise_message WHERE COMP_ID = ? AND USER_TYPE = 'stu' AND FATHER_ID IS NULL ORDER BY TIME";
				List<Map<String, Object>> list = jt.queryForList(sql4, new Object[]{a.getCompid()});
				Object aid = null;
				if (list != null && list.size() > 0) {
					aid = list.get(0).get("ID");
				}
				String sql3 = "INSERT INTO appraise_message (MESSAGE,TIME,USER_TYPE,COMP_ID,FATHER_ID) VALUES (?,?,?,?,?)";
				int i = jt.update(sql3, new Object[] { a.getTeachermessage(), ctime, "tea" ,a.getCompid(), aid});
				// 插入消息表
				String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql5, new Object[] { "评价被回复", "您针对《" + title + "》给出的评价收到老师的回复，请到我的评价页面查看详情！", ctime, queryList.get(0).get("ID"), 1, "5" });
				return i;
			}
		} catch (Exception e) {
		}
		return 0;
	}

}
