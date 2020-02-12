package com.pc.teacher.dao;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.util.CommUtils;
import com.util.CurrentPage;

public class TeacherFindDaoImpl {

	private JdbcTemplate jt = CommUtils.getJdbcTemplate();

	// （年级,年级表grade） - ageDetail
	public List<Map<String, Object>> findAgedetail() {
		String sql = "select * from grade";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}

	/* 未点评List +条件查询+分页 */
	public CurrentPage weidianpingList(String geade, String name, int currentPage, int numPerPage, int userId) {
		String sql = "select com.ID id, com.NEW_TITLE title, s.NICKNAME author, com.GEADE geade, com.DRAFT draft, com.OLD_TITLE oldtitle, com.CREATE_TIME createTime" + " from s_order o , composition com , teacher t , student s where o.STATE=2 and com.STATE=2 and o.TEACHER_ID= " + userId + " and o.TEACHER_ID = t.ID and o.COMP_ID=com.ID and com.TID=t.ID and o.STU_ID=s.ID";
		if (geade != null && geade != "") {
			sql += " and com.GEADE='" + geade + "'";
		}
		if (name != null && name != "") {
			sql += " and ( com.NEW_TITLE like '%" + name + "%' or s.NICKNAME like '%" + name + "%' )";
		}
		sql += " ORDER BY com.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	/* 已点评List +条件查询+分页 */
	public CurrentPage yidianpingList(String geade, String startTime, String endTime, String name, int currentPage, int numPerPage, int userId) {
		String sql = "select ccom.ID id, com.NEW_TITLE title, s.NICKNAME author, com.GEADE geade, ccom.SCORE score, com.DRAFT draft," + " com.OLD_TITLE oldtitle, com.CREATE_TIME createTime, ccom.COM_TIME comtime" + " from s_order o , com_composition ccom , composition com , teacher t , student s" + " where o.STATE in (3,4) and com.STATE=3 and o.COMP_ID=com.ID and ccom.COMP_ID=com.ID and o.TEACHER_ID = t.ID and ccom.TEACHER_ID=t.ID and o.STU_ID=s.ID and o.TEACHER_ID = " + userId;
		if (geade != null && geade != "") {
			sql += " and com.GEADE='" + geade + "' ";
		}
		if (name != null && name != "") {
			sql += " and ( com.NEW_TITLE like '%" + name + "%' or s.NICKNAME like '%" + name + "%' )";
		}
		if (startTime != null && startTime != "") {
			sql += " and ccom.COM_TIME>='" + startTime + " 00:00:00'";
		}
		if (endTime != null && endTime != "") {
			sql += " and ccom.COM_TIME<='" + endTime + " 23:59:59'";
		}
		sql += " ORDER BY ccom.COM_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	// 点评作文年级
	public List<Map<String, Object>> grade(int id) {
		String sql = "select com.GEADE grade from composition com where com.ID =" + id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}

	// 查评分标准信息
	public List<Map<String, Object>> pingfen(String grade) {
		String sql1 = "select * from pschool";
		if (grade.equals("低年级") || grade.equals("小学") || grade.equals("一年级") || grade.equals("二年级") || grade.equals("三年级") || grade.equals("四年级") || grade.equals("五年级") || grade.equals("六年级")) {
			sql1 = "select * from pschool";
		}
		if (grade.equals("初中") || grade.equals("初一") || grade.equals("初二") || grade.equals("初三") || grade.equals("七年级") || grade.equals("八年级") || grade.equals("九年级")) {
			sql1 = "select * from jschool";
		}
		if (grade.equals("高中") || grade.equals("高一") || grade.equals("高二") || grade.equals("高三")) {
			sql1 = "select * from hschool";
		}
		List<Map<String, Object>> pingfen = jt.queryForList(sql1);
		return pingfen;
	}

	// 批改 未点评 - 查看、回显
	public List<Map<String, Object>> toupdateWdp(int id, int userId) {
		String sql = "select com.ID id, com.UDID udid, com.NEW_TITLE title, s.NICKNAME author, com.GEADE geade, com.DRAFT draft, com.IMAGE1 img1, com.IMAGE2 img2, com.IMAGE3 img3, com.CONTENT content, o.TOTEACHER toteacher from s_order o , composition com , teacher t , student s where o.STATE=2 and com.STATE=2 and o.TEACHER_ID=t.ID and o.COMP_ID=com.ID and com.TID=t.ID and o.STU_ID=s.ID and o.COMP_ID="+id+" and o.TEACHER_ID="+userId;
		//String sql = "SELECT	com.ID id,	com.UDID udid,	com.NEW_TITLE title,	s.NICKNAME author,	com.GEADE geade,	com.DRAFT draft,	com.IMAGE1 img1,	com.IMAGE2 img2,	com.IMAGE3 img3,	com.CONTENT content,	ccom.voice FROM	s_order o,	composition com,	com_composition ccom,	teacher t,	student s WHERE	o.STATE = 2 AND com.STATE = 2 AND o.TEACHER_ID = t.ID AND o.COMP_ID = com.ID AND com.TID = t.ID AND o.STU_ID = s.ID AND ccom.COMP_ID = com.ID AND ccom.TEACHER_ID = o.TEACHER_ID  and o.COMP_ID="+id+" and o.TEACHER_ID="+userId;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		String sql2 = "SELECT voice from com_composition WHERE TEACHER_ID = ? AND COMP_ID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{userId, id});
		if (list != null && list.size() > 0) {
			forList.addAll(list);
		}
		return forList;
	}

	// 查看已点评详情
	public List<Map<String, Object>> findYdp(int id) {
		String sql = "select ccom.ID id, com.NEW_TITLE title, s.NICKNAME author, com.GEADE geade, com.DRAFT draft, ccom.COM_IMAGE1 img1, ccom.COM_IMAGE2 img2, ccom.COM_IMAGE3 img3, ccom.SCORE score, ccom.CONTENT content, ccom.SCORING scoring, ccom.POINTS points, ccom.SUGGEST suggest, ccom.DP_CONTENT dpcontent, ccom.DP_CONTENT_CA categoryCa, ccom.DP_LANGUAGE dplanguage, ccom.DP_LANGUAGE_CA languageCa, ccom.DP_STRUCTURE dpstructure, ccom.DP_STRUCTURE_CA structureCa, ccom.DP_WRITING dpwriting, ccom.DP_WRITING_CA writingCa,ccom.voice from com_composition ccom , composition com , student s where com.STATE=3 and ccom.COMP_ID=com.ID and ccom.STU_ID=s.ID and ccom.ID=" + id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}

	// 评分标准
	public List<Map<String, Object>> findPfbz(String geade, String dpcontent, String dplanguage, String dpstructure, String dpwriting) {
		String sql1 = "";
		List<Map<String, Object>> forList = null;
		if (geade.equals("低年级") || geade.equals("小学") || geade.equals("一年级") || geade.equals("二年级") || geade.equals("三年级") || geade.equals("四年级") || geade.equals("五年级") || geade.equals("六年级")) {
			sql1 = "select * from pschool where content=? or language=? or writing=? ";
			forList = jt.queryForList(sql1, new Object[] { dpcontent, dplanguage, dpwriting }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
		}
		if (geade.equals("初中") || geade.equals("初一") || geade.equals("初二") || geade.equals("初三") || geade.equals("七年级") || geade.equals("八年级") || geade.equals("九年级")) {
			sql1 = "select * from jschool where content=? or language=? or structure=? or writing=? ";
			forList = jt.queryForList(sql1, new Object[] { dpcontent, dplanguage, dpstructure, dpwriting }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
		}
		if (geade.equals("高中") || geade.equals("高一") || geade.equals("高二") || geade.equals("高三")) {
			sql1 = "select category category,content content,express language,feature structure from hschool where content=? or express=? or feature=? ";
			forList = jt.queryForList(sql1, new Object[] { dpcontent, dplanguage, dpstructure }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
		}
		return forList;
	}

	// 查询学生id
	public int findSid(String sudid) {
		String sql = "select ID from student where UDID='" + sudid + "'";
		List<Map<String, Object>> queryForList = jt.queryForList(sql);
		if (queryForList.size() > 0) {
			int id = Integer.parseInt(queryForList.get(0).get("ID").toString());
			return id;
		}
		return 0;
	}

	//查询学生 id
	public Map<String, Object> findstuid(String stid) {
		String sql = "select ID from student where UDID='"+stid+"'";
		Map<String, Object> forMap = jt.queryForMap(sql);
		return forMap;
	}

	// 批改 未点评 - 提交 //STATE 状态(待上传1,待点评2,已点评3)
	public int addComment(int id, int userId, int stuid, String content, String score, String geade, String pcontent, String planguage, String pstructure, String pwriting, String scoring, String points, String suggest, String categoryCa, String languageCa, String structureCa, String writingCa) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		JdbcTemplate jt_temp = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		//添加事务管理
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jt_temp.getDataSource());
		TransactionStatus status = transactionManager.getTransaction(def);
		List<Map<String,Object>> forList = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		try {
			String sql = "select com.ID,com.STATE  from com_composition com where com.COMP_ID = ?";
			forList = jt_temp.queryForList(sql, new Object[]{id});
			
			String name = Thread.currentThread().getName();
		    long threadl = Thread.currentThread().getId();
		    logger.info("------------提交当前线程名字:"+name+",id："+threadl+"------------------------");
			logger.info("--------------提交forList.size():"+forList.size()+"----comp_id:"+id+"---currentTimeMillis:"+System.currentTimeMillis());
			//根据年级分数计算几类文
			String grading = null;
			try {
				grading = CommUtils.getCompositionLeveByScoreAndGrade(score, geade);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			//图片
			String sqlpic = "SELECT IMAGE1,IMAGE2,IMAGE3 FROM composition WHERE ID = ?";
			Object image1 = null;
			Object image2 = null;
			Object image3 = null;
			List<Map<String,Object>> list2 = jt.queryForList(sqlpic, new Object[]{id});
			if (list2 != null && list2.size() > 0) {
				if (list2.get(0).get("IMAGE1") != null && !list2.get(0).get("IMAGE1").toString().equals("")) {
					image1 = list2.get(0).get("IMAGE1");
				}
				if (list2.get(0).get("IMAGE2") != null && !list2.get(0).get("IMAGE2").toString().equals("")) {
					image2 = list2.get(0).get("IMAGE2");
				}
				if (list2.get(0).get("IMAGE3") != null && !list2.get(0).get("IMAGE3").toString().equals("")) {
					image3 = list2.get(0).get("IMAGE3");
				}
			}
			
			//添加事务
			try {
				int i = 0;
				if(forList.size()>0){
					int a = Integer.parseInt(forList.get(0).get("ID").toString());
					String sql1 = "update com_composition set TEACHER_ID = ?, STU_ID = ?, CONTENT = ?, SCORE = ?, SCORING = ?, "
							+ "POINTS = ?, SUGGEST = ?, COM_TIME = ?, STATE = 3," + " DP_CONTENT = ?, DP_CONTENT_CA = ?, DP_LANGUAGE = ?, "
									+ "DP_LANGUAGE_CA = ?, DP_WRITING = ?," + " DP_WRITING_CA = ?, DP_STRUCTURE = ?, DP_STRUCTURE_CA = ?, "
											+ "GRADING = ?,  COM_IMAGE1 = IF(COM_IMAGE1 IS NULL OR COM_IMAGE1 = '',?,COM_IMAGE1), "
											+ "COM_IMAGE2 = IF(COM_IMAGE2 IS NULL OR COM_IMAGE2 = '',?,COM_IMAGE1), "
											+ "COM_IMAGE3 = IF(COM_IMAGE3 IS NULL OR COM_IMAGE3 = '',?,COM_IMAGE3) where ID = ?";
					i = jt_temp.update(sql1, new Object[] { userId, stuid, content, score, scoring, points, suggest, time, pcontent, categoryCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa , grading, image1, image2, image3, a});
					logger.info("-------------作文提交更新评价表i="+i);
				}else{
					String sql1 = "insert into com_composition set COMP_ID = ?, TEACHER_ID = ?, STU_ID = ?, CONTENT = ?, SCORE = ?, SCORING = ?, "
							+ "POINTS = ?, SUGGEST = ?, COM_TIME = ?, STATE = 3," + " DP_CONTENT = ?, DP_CONTENT_CA = ?, DP_LANGUAGE = ?, "
									+ "DP_LANGUAGE_CA = ?, DP_WRITING = ?," + " DP_WRITING_CA = ?, DP_STRUCTURE = ?, DP_STRUCTURE_CA = ?, "
											+ "GRADING = ?,  COM_IMAGE1 = IF(COM_IMAGE1 IS NULL OR COM_IMAGE1 = '',?,COM_IMAGE1), "
											+ "COM_IMAGE2 = IF(COM_IMAGE2 IS NULL OR COM_IMAGE2 = '',?,COM_IMAGE1), "
											+ "COM_IMAGE3 = IF(COM_IMAGE3 IS NULL OR COM_IMAGE3 = '',?,COM_IMAGE3)";
					i = jt_temp.update(sql1, new Object[] { id, userId, stuid, content, score, scoring, points, suggest, time, pcontent, categoryCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa, grading, image1, image2, image3 });
					logger.info("-------------作文提交创建评价表i="+i);
				}
				String sql3 = "update composition set STATE=3 where ID=" + id;
				int i2 = jt_temp.update(sql3);
				logger.info("-------------作文提交更新作文表i="+i2);
				String sql2 = "update s_order set STATE=3, UPDATE_TIME='" + time + "' where STU_ID=" + stuid + " and COMP_ID=" + id + " and TEACHER_ID=" + userId;
				int i3 = jt_temp.update(sql2);
				logger.info("-------------作文提交更新订单表i="+i3);
				if (i == 0 || i2 == 0 || i3 == 0) {
					throw new Exception();
				}
				transactionManager.commit(status); // 持久化所有数据
				logger.info("-------------作文提交正常--------正常");
			} catch (Exception e) {
				transactionManager.rollback(status); // 所有的数据都会rollback
				logger.info("-------------作文提交失败回滚--------");
				throw new Exception();
			}
			//更新响应时间
			String sql4 = "select UNIX_TIMESTAMP(o.GMT_PAYMENT) time, UNIX_TIMESTAMP(NOW()) now,t.AVG_TIME avg_time from s_order o,teacher t where o.TEACHER_ID = t.ID AND o.COMP_ID = ?";
			List<Map<String, Object>> list = jt_temp.queryForList(sql4, new Object[]{id});
			String avg_time = "";
			if (list.get(0).get("avg_time") != null) {
				avg_time = list.get(0).get("avg_time").toString(); 
			}
			long time2 = ((long)list.get(0).get("now") - (long)list.get(0).get("time")); 
			long lavg_time = CommUtils.stringTimeToLong(avg_time);
			long l = CommUtils.getAvgBetween2Long(lavg_time, time2);
			String str = CommUtils.longTimeToString(l);
			String sql5 = "UPDATE teacher SET AVG_TIME = ? WHERE ID = ?";
			jt_temp.update(sql5, new Object[]{str, userId});
			return 1;
		} catch (Exception e) {
			logger.error("教师端PC 提交 dao 异常 Exception"+e.getMessage());
			if(forList.size()>0 && forList.get(0).get("STATE").toString().equals("2")){
				int a = Integer.parseInt(forList.get(0).get("ID").toString());
				String sql1 = "update com_composition set TEACHER_ID = ?, STU_ID = ?, CONTENT = ?, SCORE = ?, SCORING = ?, POINTS = ?, SUGGEST = ?, COM_TIME = ?, STATE = 2," + " DP_CONTENT = ?, DP_CONTENT_CA = ?, DP_LANGUAGE = ?, DP_LANGUAGE_CA = ?, DP_WRITING = ?," + " DP_WRITING_CA = ?, DP_STRUCTURE = ?, DP_STRUCTURE_CA = ? where ID = ?";
				int k = jt.update(sql1, new Object[] { userId, stuid, content, score, scoring, points, suggest, time, pcontent, categoryCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa, a }, new int[] { Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER });
				logger.info("--------------------作文提交失败异常，更新暂存i="+k);
			}else if(forList.size()<0){
				String sql1 = "insert into com_composition set COMP_ID = ?, TEACHER_ID = ?, STU_ID = ?, CONTENT = ?, SCORE = ?, SCORING = ?, POINTS = ?, SUGGEST = ?, COM_TIME = ?, STATE = 2," + " DP_CONTENT = ?, DP_CONTENT_CA = ?, DP_LANGUAGE = ?, DP_LANGUAGE_CA = ?, DP_WRITING = ?," + " DP_WRITING_CA = ?, DP_STRUCTURE = ?, DP_STRUCTURE_CA = ?";
				int k = jt.update(sql1, new Object[] { id, userId, stuid, content, score, scoring, points, suggest, time, pcontent, categoryCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa });
				logger.info("--------------------作文提交失败异常，提交暂存i="+k);
			}
		}
		return 0;
	}

	// 添加消息(点评作文)
	public void addInformation(int id, String title, int stuid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = new Date();
		String time = df.format(d);
		String a = "您的作文《" + title + "》老师已经给出了详细的点评，请到已点评中查看哦！";
		String sql1 = "insert into information (INF_CONTENT,CREATE_TIME,STU_ID,DELETE_STATE,STATE,ICON,TITLE)" + " values ('" + a + "','" + time + "'," + stuid + ",0,1,'1','作文已评点') ";
		jt.update(sql1);
	}

	// 分配预点评作文(查询)（范文表）
	public List<Map<String, Object>> yudianping(int id) {
		String sql = "select res.ID id , res.NAME name , res.TYPE type , base.CODE_NAME grade ," + " res.CONTENT content from t_res_composion res, t_base_code base where ID = " + id + " and res.AGE_SCALE=base.CODE ";
		List<Map<String, Object>> listMap = jt.queryForList(sql);
		if (listMap.size() > 0) {
			return listMap;
		}
		return listMap;
	}

	// 分配预点评作文(查询)（作文表）
	public List<Map<String, Object>> selectYdp(int userId) {
		String gradeSql = "select GRADE from teacher where ID =" + userId;
		Map<String, Object> forMap = jt.queryForMap(gradeSql);
		String nj = forMap.get("GRADE").toString();
		Object[] parseAge = CommUtils.parseAge(nj);
		String grade = parseAge[1].toString();
		int g = 0;
		if (grade.equals("低年级") || grade.equals("小学") || grade.equals("一年级") || grade.equals("二年级") || grade.equals("三年级") || grade.equals("四年级") || grade.equals("五年级") || grade.equals("六年级")) {
			g = 1;
		}
		if (grade.equals("初中") || grade.equals("初一") || grade.equals("初二") || grade.equals("初三") || grade.equals("七年级") || grade.equals("八年级") || grade.equals("九年级")) {
			g = 2;
		}
		if (grade.equals("高中") || grade.equals("高一") || grade.equals("高二") || grade.equals("高三")) {
			g = 3;
		}
		String sql = "select ID id , NEW_TITLE name , GEADE grade , CONTENT content from pre_composition where AGE_SCALE =" + g + " limit 0,10";
		List<Map<String, Object>> listMap = jt.queryForList(sql);
		return listMap;
	}

	// 预点评作文 -提交
	public int addExpected(int userId, int id, String content, String score, String pcontent, String planguage, String pstructure, String pwriting, String scoring, String points, String suggest, String contentCa, String languageCa, String structureCa, String writingCa) {
		String sql = "select * from expected_com where TEACHER_ID = "+userId;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		int i = 0;
		if(forList.size()==0){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d = new Date();
			String time = df.format(d);
			String sql1 = "insert into expected_com set COMP_ID=?,TEACHER_ID=?,STATE=0,COM_TIME=?,SCORE=?,CONTENT=?,SCORING=?,POINTS=?,SUGGEST=?,DP_CONTENT=?,DP_CONTENT_CA=?,DP_LANGUAGE=?,DP_LANGUAGE_CA=?,DP_WRITING=?,DP_WRITING_CA=?,DP_STRUCTURE=?,DP_STRUCTURE_CA=?";
			i = jt.update(sql1, new Object[] { id, userId, time, score, content, scoring, points, suggest, pcontent, contentCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa }, new int[] { Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
		}
		return i;
	}

	// 预点评作文 未通过 -重新点评
	public List<Map<String, Object>> tofindYdp(int id) {
		String sql = "select c.NEW_TITLE name, c.GEADE grade, e.ID id , e.CONTENT content, e.SCORE score, e.SCORING scoring," + " e.POINTS points, e.SUGGEST suggest, e.DP_CONTENT dpcontent, e.DP_LANGUAGE dplanguage," + " e.DP_STRUCTURE dpstructure, e.DP_WRITING dpwriting from expected_com e , pre_composition c" + " where e.STATE=2 and e.TEACHER_ID =" + id + " and e.COMP_ID=c.ID";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}

	// 预点评作文 未通过 -提交
	public int updateExpected(int id, String content, String score, String pcontent, String planguage, String pstructure, String pwriting, String scoring, String points, String suggest, String contentCa, String languageCa, String structureCa, String writingCa) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = new Date();
		String time = df.format(d);
		//String sql = "update expected_com set COM_TIME= '" + time + "', SCORE= '" + score + "', CONTENT= '" + content + "', SCORING= '" + scoring + "', POINTS= '" + points + "', SUGGEST= '" + suggest + "'," + " DP_CONTENT= '" + pcontent + "', DP_CONTENT_CA= '" + contentCa + "', DP_LANGUAGE= '" + planguage + "', DP_LANGUAGE_CA= '" + languageCa + "', DP_WRITING= '" + pwriting + "'," + " DP_WRITING_CA= '" + writingCa + "', DP_STRUCTURE= '" + pstructure + "', DP_STRUCTURE_CA= '" + structureCa + "', STATE=0" + " where ID=" + id;
		//int i = jt.update(sql);
		String sql = "update expected_com set COM_TIME=?, SCORE=?, CONTENT=?, SCORING=?, POINTS=?, SUGGEST=?,DP_CONTENT=?, DP_CONTENT_CA= ?, DP_LANGUAGE= ?, DP_LANGUAGE_CA=?, DP_WRITING=?,DP_WRITING_CA=?, DP_STRUCTURE=?, DP_STRUCTURE_CA=?, STATE=0 where ID=?";
		int i = jt.update(sql, new Object[] {time,score,content,scoring,points,suggest,pcontent,contentCa,planguage,languageCa,pwriting,writingCa,pstructure,structureCa,id}, new int[] { Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER });
		return i;
	}

	//批改 未点评   -暂存//STATE 状态(待上传1,待点评2,已点评3)
	public int temporary(int id, int userId, int stuid, String content, String score, String geade, String pcontent,
			String planguage, String pstructure, String pwriting, String scoring, String points, String suggest,
			String categoryCa, String languageCa, String structureCa, String writingCa) {
		try {
			String sql = "select ID from com_composition where COMP_ID="+id;
			List<Map<String,Object>> forList = jt.queryForList(sql);
			String name = Thread.currentThread().getName();
		    long threadl = Thread.currentThread().getId();
		    logger.info("------------暂存当前线程名字:"+name+",id："+threadl+"------------------------");
			logger.info("--------------暂存forList.size():"+forList.size()+"----comp_id:"+id+"---currentTimeMillis:"+System.currentTimeMillis());
			int i = 0;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			String time = df.format(d);
			if(forList.size()>0){
				int a = Integer.parseInt(forList.get(0).get("ID").toString());
				String sql1 = "update com_composition set TEACHER_ID = ?, STU_ID = ?, CONTENT = ?, SCORE = ?, SCORING = ?, POINTS = ?, SUGGEST = ?, COM_TIME = ?, STATE = 2," + " DP_CONTENT = ?, DP_CONTENT_CA = ?, DP_LANGUAGE = ?, DP_LANGUAGE_CA = ?, DP_WRITING = ?," + " DP_WRITING_CA = ?, DP_STRUCTURE = ?, DP_STRUCTURE_CA = ? where ID = ?";
				i = jt.update(sql1, new Object[] { userId, stuid, content, score, scoring, points, suggest, time, pcontent, categoryCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa, a }, new int[] { Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER });
				logger.info("----暂存更新评价表I-----"+i);
			}else{
				String sql1 = "insert into com_composition set COMP_ID = ?, TEACHER_ID = ?, STU_ID = ?, CONTENT = ?, SCORE = ?, SCORING = ?, POINTS = ?, SUGGEST = ?, COM_TIME = ?, STATE = 2," + " DP_CONTENT = ?, DP_CONTENT_CA = ?, DP_LANGUAGE = ?, DP_LANGUAGE_CA = ?, DP_WRITING = ?," + " DP_WRITING_CA = ?, DP_STRUCTURE = ?, DP_STRUCTURE_CA = ?";
				i = jt.update(sql1, new Object[] { id, userId, stuid, content, score, scoring, points, suggest, time, pcontent, categoryCa, planguage, languageCa, pwriting, writingCa, pstructure, structureCa });
				logger.info("----提交更新评价表I-----"+i);
			}
			return i;
		} catch (Exception e) {
			logger.error("教师端PC 插入暂存异常 Exception"+e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	//未点评  -暂存数据
	public List<Map<String, Object>> getTemporary(int id, int userId) {
		String sql = "select o.TOTEACHER toteacher, com.ID id, com.UDID udid, com.NEW_TITLE title, s.NICKNAME author, com.GEADE geade, com.DRAFT draft, com.IMAGE1 img1, com.IMAGE2 img2, com.IMAGE3 img3, ccom.SCORE score, ccom.CONTENT content, ccom.SCORING scoring, ccom.POINTS points, ccom.SUGGEST suggest, ccom.DP_CONTENT dpcontent, ccom.DP_CONTENT_CA categoryCa, ccom.DP_LANGUAGE dplanguage, ccom.DP_LANGUAGE_CA languageCa, ccom.DP_STRUCTURE dpstructure, ccom.DP_STRUCTURE_CA structureCa, ccom.DP_WRITING dpwriting, ccom.DP_WRITING_CA writingCa, ccom.voice from s_order o , com_composition ccom , composition com , student s where com.STATE=2 and ccom.STATE=2 and ccom.COMP_ID=com.ID and ccom.STU_ID=s.ID and o.COMP_ID=com.ID and o.COMP_ID=ccom.COMP_ID and com.ID=" + id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}

	//教师开始点评修改作文表状态
	public void updateCompositionTeacherState(int id) {
		String sql = "UPDATE composition SET TACHER_STATE = 1 WHERE ID = ?";
		jt.update(sql, new Object[]{id});
	}
	private static Logger logger = Logger.getLogger(TeacherFindDaoImpl.class);
}
