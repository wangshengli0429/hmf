package com.rest.service.dao.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.rest.service.TomcatListener;
import com.rest.service.dao.OtherDao;
import com.util.CommUtils;
import com.util.CurrentPage;
import com.util.TeacherSortUtils;

public class OtherDaoImpl implements OtherDao {
	private static Logger logger = Logger.getLogger(OtherDaoImpl.class);
	public List findOldTitle(String udid, String currentPage, String numPerPage) {
		try {
			int currentNum = (Integer.parseInt(currentPage) - 1) * Integer.parseInt(numPerPage);
			int numPerPageNum = Integer.parseInt(numPerPage);
			
			List list = new ArrayList();
			String sql1 = "SELECT NEW_TITLE oldTitle,PROPO ask,CREATE_TIME createTime FROM composition WHERE UDID= ? order by createTime desc limit ?,?";
			list = jt.queryForList(sql1, new Object[] { udid, currentNum, numPerPageNum });
			if (list != null) {
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	public List findDraftN() {
		try {
			List list = new ArrayList();
			String sql1 = "SELECT NAME FROM draft_number";
			list = jt.queryForList(sql1);
			if (list.size() != 0) {
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	public Map<String, Object> findGrade() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> xlist = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> glist = new ArrayList<Map<String, Object>>();
			String sql1 = "SELECT GRADE FROM grade";
			list = jt.queryForList(sql1);
			if (list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					if (i < 6) {
						xlist.add(list.get(i));
					} else if (i > 5 && i < 9) {
						clist.add(list.get(i));
					} else if (i > 8 && i < 12) {
						glist.add(list.get(i));
					}
				}
				map.put("xlist", xlist);
				map.put("clist", clist);
				map.put("glist", glist);
				return map;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	public List typeNumber() {
		// 类型编号(文体033，体裁013，技法026)
		List list = new ArrayList();
		try {
			String sql1 = "SELECT CODE_NAME as tname FROM t_base_code  WHERE CODE_TYPE in ('033','013','026')";
			list = jt.queryForList(sql1);
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	// 搜索作文
	public List<Map<String, Object>> search(String cond, String numPerPage, String currentPage) {
		try {
			List<Map<String, Object>> listrest = new ArrayList<Map<String, Object>>();
			CurrentPage cp = new CurrentPage();
			String count = null;
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list1 = null;
			List<Map<String, Object>> list2 = null;
			List<Map<String, Object>> list3 = null;
			String sql1 = "SELECT ID,NAME,CONTENT FROM t_res_composion WHERE MATCH (name,content) AGAINST ('+" + cond + "*' IN BOOLEAN MODE)";
			// list1 =jt.queryForList(sql1);
			cp.Page(sql1, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			list1 = cp.getResultList();
			String sql2 = "SELECT ID,NAME,CONTENT FROM t_res_composion_sc WHERE MATCH (name,content) AGAINST ('+" + cond + "*' IN BOOLEAN MODE)";
			// list2 =jt.queryForList(sql2);
			cp.Page(sql2, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			list2 = cp.getResultList();
			String sql3 = "SELECT ID,NAME,CONTENT FROM t_res_composion_jf WHERE MATCH (name,content) AGAINST ('+" + cond + "*' IN BOOLEAN MODE)";
			// list3 =jt.queryForList(sql3);
			cp.Page(sql3, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			list3 = cp.getResultList();
			if (list1.size() != 0) {
				for (int i = 0; i < list1.size(); i++) {
					count = jt.queryForObject("SELECT COUNT(*) FROM read_no WHERE type=0 AND COMP_ID=?", new Object[] { list1.get(i).get("ID") }, String.class);
					map.put("content", list1.get(i).get("CONTENT").toString());
					map.put("sign", list1.get(i).get("ID").toString());
					map.put("title", list1.get(i).get("NAME").toString());
					if (count.equals(null)) {
						map.put("number", 0);
					} else
						map.put("number", count);
				}
				count = null;
			}
			if (list2.size() != 0) {
				for (int i = 0; i < list2.size(); i++) {
					count = jt.queryForObject("SELECT COUNT(*) FROM read_no WHERE type=0 AND COMP_ID=?", new Object[] { list2.get(i).get("ID") }, String.class);
					map.put("content", list2.get(i).get("CONTENT").toString());
					map.put("sign", list2.get(i).get("ID").toString());
					map.put("title", list2.get(i).get("NAME").toString());
					if (count.equals(null)) {
						map.put("number", 0);
					} else {
						map.put("number", count);
					}
				}
				count = null;
			}
			if (list3.size() != 0) {
				for (int i = 0; i < list3.size(); i++) {
					count = jt.queryForObject("SELECT COUNT(*) FROM read_no WHERE type=0 AND COMP_ID=?", new Object[] { list3.get(i).get("ID") }, String.class);
					map.put("content", list3.get(i).get("CONTENT").toString());
					map.put("sign", list3.get(i).get("ID").toString());
					map.put("title", list3.get(i).get("NAME").toString());
					if (count.equals(null)) {
						map.put("number", 0);
					} else
						map.put("number", count);
				}
				count = null;
			}
			if (map.size() <= 0) {
				return null;
			} else {
				listrest.add(map);
				return listrest;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	public int feedback(String state, String feedback, String udid) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		if (state.equals("1")) {
			String sql = "insert into feedback(FEEDBACK,UDID,CREATE_TIME,STATE)values(?,?,?,?)";
			return jt.update(sql, new Object[] { feedback, udid, ctime, "1" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
		} else {
			String sql = "insert into feedback(FEEDBACK,UDID,CREATE_TIME,STATE)values(?,?,?,?)";
			return jt.update(sql, new Object[] { feedback, udid, ctime, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
		}
	}

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	@Override
	public String findStudentComplete_info(String udid) {
		String i = "0";
		String sql = "SELECT COMPLETE_INFO FROM student WHERE UDID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
		if (list != null && list.size() > 0) {
			if (list.get(0).get("COMPLETE_INFO") != null && !list.get(0).get("COMPLETE_INFO").equals("")){
				i = list.get(0).get("COMPLETE_INFO").toString();
			}
		}
		return i;
	}

	@Override
	public List<Map<String,Object>> findDpCard_base() {
		String sql = "SELECT ID id,COUNT count,NAME name,PRICE new_price,TIME time,OLD_PRICE old_price FROM dpcard_base WHERE STATE = 0";
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			list = jt.queryForList(sql);
			return list;
		} catch (Exception e) {
		}
		return null;
		
	}

	@Override
	public void checkDpCard(int i) {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq1 = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		SimpleDateFormat fmtrq2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		//当前时间待时分秒
		String ntime = fmtrq2.format(now.getTime());
		//当前时间不待时分秒
		String ntime2 = fmtrq1.format(now.getTime());
		//30天后时间不带时分秒
		now.add(GregorianCalendar.DAY_OF_YEAR, i);
		String ctime = fmtrq1.format(now.getTime());
		
		logger.info("-----------开始检查评点卡过期------------"+ntime);
		//更新过期
		String sql3 = "UPDATE stu_dpcard_use SET STATE = 1 WHERE REMAINING_COUNT = 0";
		String sql4 = "UPDATE stu_dpcard_use SET REMAINING_COUNT = 0 WHERE END_TIME < ?";
		String sql5 = "UPDATE stu_dpcard SET STATE = 1 WHERE END_TIME < ?";
		jt.update(sql4, new Object[]{ntime2});
		jt.update(sql5, new Object[]{ntime2});
		jt.update(sql3);
		
		//过期时间提醒
		String sql1 = "SELECT STU_ID,END_TIME FROM stu_dpcard_use WHERE STATE = 0";
		String sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
		try {
			List<Map<String,Object>> list = jt.queryForList(sql1);
			if (list != null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					Object id = map.get("STU_ID");
					if (map.get("END_TIME") == null) {
						continue;
					}
					String end_time = map.get("END_TIME").toString();
					if (end_time.contains(ctime)) {
						//因为实例化问题，定时语句会执行多次
						String sql6 = "SELECT CREATE_TIME FROM information WHERE STU_ID = ? AND  INF_CONTENT = ?";
						List<Map<String,Object>> list2 = jt.queryForList(sql6, new Object[]{id, "您的评点卡即将在"+i+"天后过期，过期后评点卡将无法使用，避免给您带来不必要的麻烦，请您及时使用或充值续费！"});
						if (list2 == null || list2.size() == 0) {
							jt.update(sql2, new Object[] { "系统通知", "您的评点卡即将在"+i+"天后过期，过期后评点卡将无法使用，避免给您带来不必要的麻烦，请您及时使用或充值续费！", ntime, id, 1, 2 });
						}else {
							int c = 0;
							for (Map<String, Object> map2 : list2) {
								String oldTime = map2.get("CREATE_TIME").toString();
								if (oldTime.contains(ntime2)) {
									c++;
								}
							}
							if (c == 0) {
								jt.update(sql2, new Object[] { "系统通知", "您的评点卡即将在"+i+"天后过期，过期后评点卡将无法使用，避免给您带来不必要的麻烦，请您及时使用或充值续费！", ntime, id, 1, 2 });
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("---------------评点卡过期通知异常---------------");
		}
	}
	
	@Override
	public void checkRed() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String nowTime = df.format(date);
		String sqlTime = df2.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		String overTime = df.format(cal.getTime());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		logger.info("-----------开始检查红包过期------------"+nowTime);
		String firstSql = "UPDATE red_packet SET FAILURE = 1 where END_TIME < ? AND FAILURE = 0 AND REDUSE = 0";
		String students = "SELECT s.ID FROM student s, red_packet r WHERE r.END_TIME = ? AND r.FAILURE = 0 AND r.UDID = s.UDID";
		List<Map<String, Object>> sIdList = null;
		try {
			jt.update(firstSql, new Object[]{nowTime});
		} catch (Exception e) {
			logger.info("-----------红包自动过期异常------------"+e.getMessage());
			logger.error(e.getMessage());
		}
		try {
			sIdList = jt.queryForList(students, new Object[]{overTime});
			if (sIdList != null &&sIdList.size() > 0) {
				for (Map<String, Object> map : sIdList) {
					String information = "您的红包将在"+year+"年"+month+"月"+day+"日之前过期，请尽快使用！";
					String sql6 = "SELECT CREATE_TIME FROM information WHERE STU_ID = ? AND  INF_CONTENT = ?";
					List<Map<String, Object>> list = jt.queryForList(sql6, new Object[]{map.get("ID"), information});
					if ((list.size() == 0) || (list.size() > 0 && list.get(0).get("CREATE_TIME") != null && !list.get(0).get("CREATE_TIME").toString().contains(nowTime))) {
						String insertInfor = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
						jt.update(insertInfor, new Object[] { "红包过期提醒", information, sqlTime, map.get("ID"), 1, 9 });
					}
				}
			}
			
		} catch (Exception e) {
			logger.info("-----------红包自动过期发送消息异常------------"+e.getMessage());
		}
		
		
	}

	@Override
	public List<Map<String, Object>> findDpCard_stu_list(String udid, String currentPage, String numPerPage) {
		String sql = "SELECT c.COUNT count,c.START_TIME time,d.NAME name,d.PRICE price FROM stu_dpcard c," +
				"student s,dpcard_base d WHERE s.UDID = ? AND s.ID = c.STU_ID AND c.STATE = 0 " +
				"AND d.ID = c.CARD_BASE_ID ORDER BY c.START_TIME DESC LIMIT ?,?";
		int num = Integer.parseInt(numPerPage);
		int current = (Integer.parseInt(currentPage) - 1) * num;
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid, current, num});
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("time", CommUtils.ObjectTime2String(list.get(i).get("time")));
			}
		}
		return list;
	}

	@Override
	public void sortTeacher() {
		String sql = "SELECT udid FROM teacher WHERE AUSTATE = 1";
		List<Map<String,Object>> list = jt.queryForList(sql);
		for (Map<String, Object> map : list) {
			String udid = map.get("udid").toString();
			try {
				DecimalFormat df = new DecimalFormat(".##");
				String sql6 = "SELECT AVG(a.ATTIT) ATTIT,AVG(a.PROFES) PROFES,AVG(a.SATISFACTION)SATISFACTION,COUNT(a.ATTIT)count,tea.id,tea.EDU_TIME from teacher tea,com_composition com,appraise a where tea.id = com.teacher_id and a.COMP_ID = com.COMP_ID and tea.udid = ?";
				List<Map<String, Object>> list2 = jt.queryForList(sql6, new Object[]{udid});
				Map<String, Object> map2 = new HashMap<>();
				if (list2.get(0).get("ATTIT") != null && !list2.get(0).get("ATTIT").equals("")) {
					map2.put("ATTIT", df.format(list2.get(0).get("ATTIT")));
				}else {
					map2.put("ATTIT", "");
				}
				if (list2.get(0).get("PROFES") != null && !list2.get(0).get("PROFES").equals("")) {
					map2.put("PROFES", df.format(list2.get(0).get("PROFES")));
				}else {
					map2.put("PROFES", "");
				}
				if (list2.get(0).get("SATISFACTION") != null && !list2.get(0).get("SATISFACTION").equals("")) {
					map2.put("SATISFACTION", df.format(list2.get(0).get("SATISFACTION")));
				}else {
					map2.put("SATISFACTION", "");
				}
				if (list2.get(0).get("EDU_TIME") != null && !list2.get(0).get("EDU_TIME").equals("")) {
					map2.put("EDU_TIME", list2.get(0).get("EDU_TIME").toString());
				}else {
					map2.put("EDU_TIME", "");
				}
				if (list2.get(0).get("count") != null && !list2.get(0).get("count").equals("")) {
					map2.put("count", list2.get(0).get("count").toString());
				}else {
					map2.put("count", "");
				}
				double num = TeacherSortUtils.getTeacherNum(map2);
				
				String sql7 = "update teacher set sortNum = ? where udid = ?";
				jt.update(sql7, new Object[]{num, udid});
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	//分享作文
	@Override
	public Map<String, Object> getCompostionByID(String id, String condition) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT s.HEAD shead,c.CONTENT,c.CREATE_TIME,s.NICKNAME sname,t.NAME tname,t.HAND_URL thead,c.NEW_TITLE title,c.DRAFT draft,"
					+ "c.GEADE grade,c.PROPO propo,COM_IMAGE1 image1,COM_IMAGE2 image2,COM_IMAGE3 image3,com.* "
					+ "FROM composition c,com_composition com, student s, teacher t WHERE com.COMP_ID = c.ID "
					+ "AND c.ID = ? AND c.UDID = s.UDID AND c.TID = t.ID";
			List<Map<String,Object>> clist = jt.queryForList(sql, new Object[]{id});
			if (clist.size() != 0 && clist.size() != 0) {
				//用户信息集合
				map.put("sname", CommUtils.judgeSqlInformation(clist.get(0).get("sname")));
				map.put("tname", CommUtils.judgeSqlInformation(clist.get(0).get("tname")));
				map.put("thead", CommUtils.judgeUrl(clist.get(0).get("thead")));
				map.put("shead", CommUtils.judgeUrl(clist.get(0).get("shead")));
				//作文信息集合
				map.put("wordcount", CommUtils.judgeUrl(clist.get(0).get("CONTENT")).length());//字数统计
				map.put("create_time", CommUtils.ObjectTime2String(clist.get(0).get("CREATE_TIME")));
				map.put("title", CommUtils.judgeSqlInformation(clist.get(0).get("title")));
				map.put("draft", CommUtils.judgeSqlInformation(clist.get(0).get("draft")));
				if(clist.get(0).get("grade")!=null){
					Object[] parseAge = CommUtils.parseAge(clist.get(0).get("grade").toString());
					String grade = parseAge[1].toString();
					map.put("grade", grade);
				}
				map.put("propo", CommUtils.judgeSqlInformation(clist.get(0).get("propo")));
				String host = CommUtils.getServerHost();
				if(clist.get(0).get("image1")!=null&&!clist.get(0).get("image1").equals("")){
					map.put("image1", host+clist.get(0).get("image1"));
				}else {
					map.put("image1", "");
				}
				if(clist.get(0).get("image2")!=null&&!clist.get(0).get("image2").equals("")){
					map.put("image2", host+clist.get(0).get("image2"));
				}else {
					map.put("image2", "");
				}
				if(clist.get(0).get("image3")!=null&&!clist.get(0).get("image3").equals("")){
					map.put("image3", host+clist.get(0).get("image3"));
				}else {
					map.put("image3", "");
				}
				//点评信息集合
				map.put("score", CommUtils.judgeSqlInformation(clist.get(0).get("SCORE")));
				map.put("com_time", CommUtils.ObjectTime2String(clist.get(0).get("COM_TIME")));
				map.put("content", CommUtils.shareConvertComposition(clist.get(0).get("CONTENT")));
				map.put("grading", CommUtils.judgeSqlInformation(clist.get(0).get("GRADING")));
				map.put("scoring", CommUtils.judgeSqlInformation(clist.get(0).get("SCORING")));
				map.put("points", CommUtils.judgeSqlInformation(clist.get(0).get("POINTS")));
				map.put("suggest", CommUtils.judgeSqlInformation(clist.get(0).get("SUGGEST")));
				map.put("dp_content", CommUtils.judgeSqlInformation(clist.get(0).get("DP_CONTENT")));
				map.put("dp_content_ca", CommUtils.judgeSqlInformation(clist.get(0).get("DP_CONTENT_CA")));
				map.put("dp_language", CommUtils.judgeSqlInformation(clist.get(0).get("DP_LANGUAGE")));
				map.put("dp_language_ca", CommUtils.judgeSqlInformation(clist.get(0).get("DP_LANGUAGE_CA")));
				map.put("dp_writing", CommUtils.judgeSqlInformation(clist.get(0).get("DP_WRITING")));
				map.put("dp_writing_ca", CommUtils.judgeSqlInformation(clist.get(0).get("DP_WRITING_CA")));
				map.put("dp_structure", CommUtils.judgeSqlInformation(clist.get(0).get("DP_STRUCTURE")));
				map.put("dp_structure_ca", CommUtils.judgeSqlInformation(clist.get(0).get("DP_STRUCTURE_CA")));
				if(clist.get(0).get("VOICE")!=null&&!clist.get(0).get("VOICE").equals("")){
					String pathsss = "F:\\server\\cedu-files"+clist.get(0).get("VOICE").toString();
					String base64 = CommUtils.fileToBase64(pathsss);
					map.put("voice", base64);
				}else {
					map.put("voice", "");
				}
			}
			//评价内容
			String sql2 = "SELECT * FROM appraise a WHERE COMP_ID = ? ORDER BY APPR_TIME";
			String sql3 = "SELECT * FROM appraise_message WHERE COMP_ID = ? ORDER BY TIME";
			List<Map<String,Object>> alist = jt.queryForList(sql2, new Object[]{id});
			if (alist != null && alist.size() > 0) {
				map.put("attit", CommUtils.judgeSqlInformation(alist.get(0).get("ATTIT")));//负责态度
				map.put("profe", CommUtils.judgeSqlInformation(alist.get(0).get("PROFES")));//专业水平
				map.put("ave", CommUtils.judgeSqlInformation(alist.get(0).get("AVERAGE")));//平均分
				map.put("sati", CommUtils.judgeSqlInformation(alist.get(0).get("SATISFACTION")));//满意度
			}
			List<Map<String,Object>> alist2 = jt.queryForList(sql3, new Object[]{id});
			List<List<Map<String, String>>> list = CommUtils.arrangeAppraiseMessage(alist2);
			map.put("message", list);
			
			//查询下一篇作文id
			condition = URLDecoder.decode(condition, "utf-8");
			String nextId = null;
			String sql4 = "SELECT c.ID FROM	composition c,com_composition com WHERE	c.ID = com.COMP_ID "
					+ "AND com.ID IS NOT NULL AND com.STATE = 3 AND c.STATE = 3 AND c.UDID IS NOT NULL "
					+ "AND c.CREATE_TIME IS NOT NULL ORDER BY c.GEADE DESC,c.ID";
			String sql5 = "SELECT c.ID FROM	composition c,com_composition com WHERE	c.ID = com.COMP_ID  "
					+ "AND com.ID IS NOT NULL AND com.STATE = 3 AND c.STATE = 3 AND c.UDID IS NOT NULL "
					+ "AND c.CREATE_TIME IS NOT NULL AND c.NEW_TITLE LIKE '%"+condition+"%' ORDER BY c.GEADE DESC,c.ID";
			List<Map<String, Object>> idList = null;
			if (condition != null && !condition.equals("")) {
				idList = jt.queryForList(sql5);
			}else {
				idList = jt.queryForList(sql4);
			}
			for (int i = 0; i < idList.size(); i++){
				if (id.equals(idList.get(i).get("ID").toString())) {
					if (i != idList.size() - 1) {
						nextId = idList.get(i + 1).get("ID").toString();
					}else {
						nextId = idList.get(0).get("ID").toString();
					}
					break;
				}
			}
			map.put("nextId", nextId);
			return map;
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public void tempInsertAppraise() {
		String sql = "SELECT * FROM appraise";
		String sql2 = "INSERT INTO appraise_message SET MESSAGE = ?,TIME=?,USER_TYPE=?,FATHER_ID=?,COMP_ID=?";
		List<Map<String,Object>> list = jt.queryForList(sql);
		for (Map<String, Object> map : list) {
			Number id = null;
			if (map.get("STU_MESSAGE") != null && !map.get("STU_MESSAGE").toString().equals("")) {
				id = getTempId(map.get("STU_MESSAGE").toString(),map.get("APPR_TIME"),"stu",null,map.get("COMP_ID"));
			}
			if (map.get("TEACHER_MESSAGE") != null && !map.get("TEACHER_MESSAGE").toString().equals("")) {
				jt.update(sql2, new Object[]{map.get("TEACHER_MESSAGE").toString(),map.get("MESSAGE_TIME"),"tea",id,map.get("COMP_ID")});
			}
		}
	}
	private Number getTempId(final String message, final Object time, final String type, final Object id, final Object cid) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();  
		final String sql = "INSERT INTO appraise_message SET MESSAGE = ?,TIME=?,USER_TYPE=?,FATHER_ID=?,COMP_ID=?";
		int i = jt.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {

				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 
				ps.setString(1, message);
				ps.setObject(2, time);
				ps.setString(3, type);
				ps.setObject(4, id);
				ps.setObject(5, cid);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey();
	}

	@Override
	public int findCompositionHasPay(String compId) {
		String sql = "SELECT ID FROM s_order WHERE COMP_ID = ? AND STATE = 2";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{compId});
		if (list != null && list.size() > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public Map<String, Object> getCompositionImageByIdAndIndex(String id, String index) {
		String com_image = "COM_IMAGE" + index;
		String sql1 = "SELECT COM_IMAGE1,COM_IMAGE2,COM_IMAGE3 FROM com_composition  WHERE COMP_ID = ?";
		String sql2 = "SELECT CONTENT content,COORDS_X1 x1,COORDS_X2 x2,COORDS_Y1 y1,COORDS_Y2 y2 FROM comment WHERE COMP_ID = ? AND COM_IMAGE_ID = ?";
		List<Map<String, Object>> list1 = jt.queryForList(sql1, new Object[]{id});
		if (list1 == null || list1.size() == 0 || list1.get(0).get(com_image) == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("image", CommUtils.getServerHost() + list1.get(0).get(com_image).toString());
		map.put("count", 3);
		if (list1.get(0).get("COM_IMAGE3") == null) {
			map.put("count", 2);
		}
		if (list1.get(0).get("COM_IMAGE2") == null) {
			map.put("count", 1);
		}
		List<Map<String, Object>> list2 = jt.queryForList(sql2, new Object[]{id, index});
		if (list2 != null && list2.size() > 0) {
			map.put("detailList", list2);
		}
		return map;
	}

	//获取所有点评过的作文集合
	@Override
	public Map<String, Object> getAllCom_composition(String condition) {
		String sql = null;
		if (condition != null && !condition.equals("")) {
			sql = "SELECT c.ID id,c.NEW_TITLE title FROM composition c,com_composition com WHERE c.STATE = 3 "
					+ "AND com.STATE = 3 AND c.ID = com.COMP_ID AND com.ID IS NOT NULL AND c.NEW_TITLE IS NOT NULL "
					+ "AND c.UDID IS NOT NULL AND c.CREATE_TIME IS NOT NULL AND c.GEADE LIKE ? AND c.NEW_TITLE LIKE '%"+condition+"%' AND c.`OPEN` = FALSE ORDER BY c.GEADE DESC,c.ID  ";
		}else {
			sql = "SELECT c.ID id,c.NEW_TITLE title FROM composition c,com_composition com WHERE c.STATE = 3 "
					+ "AND com.STATE = 3 AND c.ID = com.COMP_ID AND com.ID IS NOT NULL AND c.NEW_TITLE IS NOT NULL "
					+ "AND c.UDID IS NOT NULL AND c.CREATE_TIME IS NOT NULL AND c.GEADE LIKE ? AND c.`OPEN` = FALSE ORDER BY c.GEADE DESC,c.ID  ";
		}
		
		List<Map<String,Object>> xiaoxue = jt.queryForList(sql, new Object[]{"02700%"});
		List<Map<String,Object>> chuzhong = jt.queryForList(sql, new Object[]{"02701%"});
		List<Map<String,Object>> gaozhong = jt.queryForList(sql, new Object[]{"02702%"});
		Map<String, Object> map = new HashMap<>();
		map.put("xiaoxue", xiaoxue);
		map.put("chuzhong", chuzhong);
		map.put("gaozhong", gaozhong);
		String sql2 = "SELECT fileUrl FROM version WHERE user_type = 'STUDENT' AND platform = 'ANDROID' ORDER BY id DESC LIMIT 1";
		String url = jt.queryForObject(sql2, String.class);
		map.put("url", TomcatListener.serverHost + url);
		return map;
	}

	@Override
	public String getOpenidByUdid(String udid) {
		String sql = "SELECT OPENID_MSG FROM student WHERE UDID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
		if (list != null && list.size() > 0 && list.get(0).get("OPENID_MSG") != null && !list.get(0).get("OPENID_MSG").toString().equals("") ) {
			return list.get(0).get("OPENID_MSG").toString();
		}else {
			return null;
		}
	}

	@Override
	public void tempInsertCom_composition() {
		String sql1 = "SELECT ID,IMAGE1,IMAGE2,IMAGE3 FROM composition WHERE STATE = 3";
		List<Map<String,Object>> list = jt.queryForList(sql1);
		for (Map<String, Object> map : list) {
			String id = map.get("ID").toString();
			Object i1 = null;
			Object i2 = null;
			Object i3 = null;
			if (map.get("IMAGE1") != null && !map.get("IMAGE1").toString().equals("")) {
				i1 = map.get("IMAGE1");
			}
			if (map.get("IMAGE2") != null && !map.get("IMAGE2").toString().equals("")) {
				i2 = map.get("IMAGE2");
			}
			if (map.get("IMAGE3") != null && !map.get("IMAGE3").toString().equals("")) {
				i3 = map.get("IMAGE3");
			}
			String sql2 = "UPDATE com_composition SET COM_IMAGE1 = ? ,COM_IMAGE2 = ? ,COM_IMAGE3 = ? WHERE COMP_ID = ?";
			jt.update(sql2, new Object[]{i1, i2, i3, id});
		}
	}

	@Override
	public int insertComPic(List<Map<String, String>> list, String sid, String tid, String cid, String image_id) {
		String sql = "INSERT INTO comment (STU_ID,TEA_ID,COMP_ID,COM_IMAGE_ID,CONTENT,COORDS_X1,COORDS_Y1,COORDS_X2,COORDS_Y2) VALUES (?,?,?,?,?,?,?,?,?)";		
		int i = 0;
		for (Map<String, String> map : list) {
			String content = map.get("content");
			String cords_x1 = map.get("cords_x1");
			String cords_x2 = map.get("cords_x2");
			String cords_y1 = map.get("cords_y1");
			String cords_y2 = map.get("cords_y2");
			i = jt.update(sql, new Object[]{sid, tid, cid, image_id, content, cords_x1, cords_y1, cords_x2, cords_y2});
			if (i <= 0) {
				break;
			}
		}
		return i;
	}

	@Override
	public Map<String, Object> findCompositionImg(String id, String imgIndex) {
		String url = null;
		int width = 0;
		int height = 0;
		String sql = "SELECT IMAGE"+imgIndex+" FROM composition WHERE ID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{id});
		if (list.size() == 0) {
			return null;
		}
		if (imgIndex.equals("1")) {
			url = list.get(0).get("IMAGE1").toString();
		}
		if (imgIndex.equals("2")) {
			url = list.get(0).get("IMAGE2").toString();
		}
		if (imgIndex.equals("3")) {
			url = list.get(0).get("IMAGE3").toString();
		}
		try {
			BufferedImage image = ImageIO.read(new File("F:\\server\\cedu-files" + url));
			width = image.getWidth();
			height = image.getHeight();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Map<String, Object> map = new HashMap<>();
		map.put("url", url);
		map.put("width", width);
		map.put("height", height);
		return map;
	}

	@Override
	public boolean findCompositionImgHasCom(String id, String imgIndex) {
		String sql = "SELECT COM_IMAGE" + imgIndex + " FROM com_composition WHERE COMP_ID = ?";
		boolean b = false;
		try {
			String image = jt.queryForObject(sql, new Object[]{id}, String.class);
			if (image != null && !image.equals("") && image.startsWith("/files/pingdianapp_img/com_composition")) {
				b = true;
			}
		} catch (Exception e) {
		}
		return b;
	}

	@Override
	public Map<String, String> h5TouGaoLogin(String phone, String password) {
		Map<String, String> map = new HashMap<>();
		map.put("state", "-1");//0用户名密码正确，1有这个用户但是密码错误，-1没有这个用户
		String sql1 = "SELECT ID,PASSWORD FROM student WHERE PHONE = ? ";
		String sql2 = "SELECT ID,PASSWORD FROM t_base_user WHERE PHONE = ? AND USER_TYPE = '001011'";
		List<Map<String,Object>> list = jt.queryForList(sql1, new Object[]{phone});
		if (list != null &&list.size() > 0) {
			if (list.get(0).get("PASSWORD") != null && password.equals(list.get(0).get("PASSWORD").toString())) {
				map.put("state", "0");
				map.put("usertype", "app");
				map.put("userid", list.get(0).get("ID").toString());
				return map;
			}else {
				map.put("state", "1");
			}
		}
		if (!map.get("state").equals("0")) {
			ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext2.xml");
			JdbcTemplate jt2 = (JdbcTemplate) ctx.getBean("jdbcTemplate");
			List<Map<String,Object>> list2 = jt2.queryForList(sql2, new Object[]{phone});
			if (list2 != null && list2.size() > 0) {
				if (list2.get(0).get("PASSWORD") != null && password.equals(list2.get(0).get("PASSWORD").toString())) {
					map.put("state", "0");
					map.put("usertype", "h5");
					map.put("userid", list2.get(0).get("ID").toString());
					return map;
				}else {
					map.put("state", "1");
				}
			}
			
		}
		return map;
	}

	@Override
	public int validatePhone(String phone) {
		String sql = "SELECT id FROM student WHERE PHONE = ?";
		try {
			jt.queryForObject(sql, new Object[]{phone}, String.class);
			return 1;
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public int touGaoinsertComposition(Map<String, String> map, List<String> picList) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		String image1 = picList.size() > 0 ? picList.get(0) : null;
		String image2 = picList.size() > 1 ? picList.get(1) : null;
		String image3 = picList.size() > 2 ? picList.get(2) : null;
		String sql = "INSERT INTO h5_tougao_composition (USER_ID,USER_TYPE,CREATE_TIME,TITLE,GRADE,CONTENT,IMAGE1,IMAGE2,IMAGE3) VALUES (?,?,?,?,?,?,?,?,?)";
		int i = jt.update(sql, new Object[]{map.get("userId"), map.get("userType"), ctime, map.get("title"), map.get("grade"), map.get("content"), image1, image2, image3});
		return i;
	}
	
}
