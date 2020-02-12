package com.rest.service.dao.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.TomcatListener;
import com.rest.service.dao.TeacherDao;
import com.util.CommUtils;
import com.util.CurrentPage;
import com.util.TeacherSortUtils;
import com.util.TimeCycle;

public class TeacherDaoImpl implements TeacherDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	private static Logger logger = Logger.getLogger(TeacherDaoImpl.class);

	// 老师简介（学生）
	public List<Map<String, Object>> findTintroduc(String tid, String udid) {
		String sql = "";
		List<Map<String, Object>> queryForList = new ArrayList<>();
		if ("".equals(udid)) {
			sql = "SELECT * FROM teacher WHERE ID = ?";
			queryForList = jt.queryForList(sql,new Object[]{tid});
		}else {
			sql = "SELECT DISTINCT t.*,IF((SELECT ID FROM collection WHERE TEACHER_ID = ? AND UDID = ?) IS NULL, 0, 1) comType " +
					"FROM teacher t,collection c WHERE t.ID = ?";
			queryForList = jt.queryForList(sql, new Object[]{tid, udid, tid});
		}
		return queryForList;
	}

	// 老师列表（学生）
	public List<Map<String, Object>> findTlist(String grade, String tname, String numPerPage, String currentPage) {
		String sql = "";
		CurrentPage cp = new CurrentPage();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			// 判断grade年级、tname老师姓名是否为空
			String gradeRange = "";
			switch (grade) {
			case "xx":
				gradeRange = "(" + "'" + CommUtils.gradeCode[0] + "'," + "'" + CommUtils.gradeCode[1] + "'," + "'" + CommUtils.gradeCode[2] + "'," + "'" + CommUtils.gradeCode[3] + "'," + "'" + CommUtils.gradeCode[4] + "'," + "'" + CommUtils.gradeCode[5] + "')";
				break;
			case "cz":
				gradeRange = "(" + "'" + CommUtils.gradeCode[6] + "'," + "'" + CommUtils.gradeCode[7] + "'," + "'" + CommUtils.gradeCode[8] + "')";
				break;
			case "gz":
				gradeRange = "(" + "'" + CommUtils.gradeCode[9] + "'," + "'" + CommUtils.gradeCode[10] + "'," + "'" + CommUtils.gradeCode[11] + "')";
				break;
			}
			// AUSTATE 资料审核状态(审核中0，已通过1，未通过2 , 禁用3)
			if (!TextUtils.isEmpty(grade) && !TextUtils.isEmpty(tname)) {
				sql = "select t.GRADE grade,t.ID id,t.EDU_TIME edutime,t.NAME name,t.PRICE price,t.STATE state,t.HAND_URL url from teacher t , expected_com e where  t.GRADE in " + gradeRange + " and t.NAME like '%" + tname + "%' and AUSTATE = 1 and e.TEACHER_ID = t.ID and e.STATE = 1";
			}
			if (!TextUtils.isEmpty(grade) && TextUtils.isEmpty(tname)) {
				sql = "select t.GRADE grade,t.ID id,t.EDU_TIME edutime,t.NAME name,t.PRICE price,t.STATE state,t.HAND_URL url from teacher t , expected_com e where  t.GRADE in " + gradeRange + " and AUSTATE = 1 and e.TEACHER_ID = t.ID and e.STATE = 1";
			}
			if (!TextUtils.isEmpty(tname) && TextUtils.isEmpty(grade)) {
				sql = "select t.GRADE grade,t.ID id,t.EDU_TIME edutime,t.NAME name,t.PRICE price,t.STATE state,t.HAND_URL url from teacher t , expected_com e where t.NAME like '%" + tname + "%' and AUSTATE = 1 and e.TEACHER_ID = t.ID and e.STATE = 1";
			}
			if (TextUtils.isEmpty(grade) && TextUtils.isEmpty(tname)) {
				sql = "select t.GRADE grade,t.ID id,t.EDU_TIME edutime,t.NAME name,t.PRICE price,t.STATE state,t.HAND_URL url from teacher t , expected_com e where AUSTATE = 1  and e.TEACHER_ID = t.ID and e.STATE = 1";
			}
			// List<Map<String,Object>> queryForList = jt.queryForList(sql);
			
			sql += " ORDER BY t.STATE,t.sortNum DESC,t.id  ";
			System.out.println(sql);
			
			
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> queryForList = cp.getResultList();
			if (queryForList.size() > 0) {
				for (int i = 0; i < queryForList.size(); i++) {
					Map<String, Object> jo = new HashMap<String, Object>();
					String sql1 = "select COUNT(comp.ID) from  com_composition comp where  comp.TEACHER_ID= ?";
					String count = jt.queryForObject(sql1, new Object[] { queryForList.get(i).get("id") }, String.class);
					String sql2 = "SELECT count(*) count FROM student s,s_order o,teacher t WHERE o.TEACHER_ID = t.ID AND t.ID = ? AND o.STATE = 2 AND o.STU_ID = s.ID";
					String delayCount = jt.queryForObject(sql2, new Object[] { queryForList.get(i).get("id") }, String.class);
					if (delayCount != null) {
						jo.put("delayNo", delayCount);
					}
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
						if (queryForList.get(i).get("id") == null) {
							jo.put("tid", "");
						} else {
							jo.put("tid", queryForList.get(i).get("id").toString());// 老师唯一标识
						}
						jo.put("url", CommUtils.judgeUrl(queryForList.get(i).get("url")));
						//越过审核添加字段
						jo.put("check", "1");
						
						list.add(jo);
					}
				}
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	// 我的主页
	public Map<String, Object> findInformation(String udid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql2 = "SELECT t.*,e.STATE estate FROM teacher t LEFT JOIN expected_com e ON e.TEACHER_ID=t.ID WHERE  UDID= ?";
			List<Map<String, Object>> slist = jt.queryForList(sql2, new Object[] { udid });
			if (slist.size() > 0) {
				map.put("name", CommUtils.judgeSqlInformation(slist.get(0).get("NAME")));
				map.put("url", CommUtils.judgeUrl(slist.get(0).get("HAND_URL")));
				map.put("state", CommUtils.judgeSqlInformation(slist.get(0).get("STATE")));
				/*if (slist.get(0).get("AUSTATE").equals("0")) {
					map.put("austate", "审核中");
				} else if (slist.get(0).get("AUSTATE").equals("1")) {
					map.put("austate", "已通过");
				} else if (slist.get(0).get("AUSTATE").equals("2")){
					map.put("austate", "未通过");
				}else if (slist.get(0).get("AUSTATE").equals("3")){
					map.put("austate", "已禁用");
				}
				if (slist.get(0).get("STATE").equals("1")) {
					map.put("state", "正常");
				}
				if (slist.get(0).get("STATE").equals("2")) {
					map.put("state", "休息");authentication
				}*/
				Map<String, Object> map2 = CommUtils.authentication(slist);
				map.put("austate", map2.get("austate"));
				map.put("cause", map2.get("cause"));
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return map;
	}
	// 我的主页
	public Map<String, Object> findInformation2(String udid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql2 = "SELECT t.*,e.STATE estate FROM teacher t LEFT JOIN expected_com e ON e.TEACHER_ID=t.ID WHERE  UDID= ?";
			List<Map<String, Object>> slist = jt.queryForList(sql2, new Object[] { udid });
			if (slist.size() > 0) {
				map.put("name", CommUtils.judgeSqlInformation(slist.get(0).get("NAME")));
				map.put("url", CommUtils.judgeUrl(slist.get(0).get("HAND_URL")));
				map.put("state", CommUtils.judgeSqlInformation(slist.get(0).get("STATE")));
				Map<String, Object> map2 = CommUtils.authentication2(slist);
				map.put("austate", map2.get("austate"));
				map.put("cause", map2.get("cause"));
				map.put("reason", map2.get("reason"));
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return map;
	}

	// 老师的账单
	public List<Map<String, Object>> findBills(String udid, String numPerPage, String currentPage) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		Map<String, Object> map = null;
		TimeCycle t = new TimeCycle();
		try {
			CurrentPage cp = new CurrentPage();
			String may = String.valueOf(date.get(Calendar.MONTH) + 1);
			String sql = "SELECT o.BUYER_PAY_AMOUNT,c.NEW_TITLE,o.CLEARING_TIME,t.HAND_URL FROM s_order as o,composition as c,teacher as t , student as s WHERE  c.ID=o.COMP_ID and t.ID = o.TEACHER_ID and o.STU_ID=s.ID and o.STATE = 4 and t.UDID= '" + udid + "' ORDER BY o.CLEARING_TIME Desc";
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> aplist = cp.getResultList();
			if (aplist != null && aplist.size() > 0) {
				for (int i = 0; i < aplist.size(); i++) {
					map = new HashMap<String, Object>();
					String time = aplist.get(i).get("CLEARING_TIME").toString();
					String day = time.substring(0, 10);
					//转化时间格式
					String[] sts = day.split("-");
					StringBuilder sb = new StringBuilder();
					String nian = sts[0];
					String yue = new Integer(sts[1]).toString();
					String ri = new Integer(sts[2]).toString();
					sb.append(yue).append("月").append(ri).append("日");
					day = sb.toString();
					
					String h = time.substring(11, 16);
					if (aplist.get(i).get("NEW_TITLE") == (null)) {
						map.put("title", "");
					} else {
						map.put("title", aplist.get(i).get("NEW_TITLE"));
					}
					map.put("time", h);
					//map.put("ctime", t.getCustomStr(day));// 格式化时间
					map.put("ctime", day);// 格式化时间
					map.put("price", aplist.get(i).get("BUYER_PAY_AMOUNT"));
					map.put("refunds", "邀请批改");
					map.put("may", may);
					map.put("url", CommUtils.judgeUrl(aplist.get(i).get("HEAD")));
					String strs[] = time.split("-");
					map.put("may", strs[1]);
					list.add(map);
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	// 我的上月账单
	public List<Map<String, Object>> findLastBills(String udid, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		Map<String, Object> map = new HashMap<String, Object>();
		TimeCycle t = new TimeCycle();
		try {
			String may = String.valueOf(date.get(Calendar.MONTH));
			// String da = String.valueOf(date.get(Calendar.DATE));
			String sql = "SELECT o.MONEY,c.NEW_TITLE,o.CREATE_TIME,t.HAND_URL FROM s_order as o,composition as c,teacher as t WHERE  c.ID=o.COMP_ID and o.STU_ID=t.ID and o.STATE=3 and t.udid= '" + udid + "' and o.CREATE_TIME >='" + year + "-" + may + "-1' and o.CREATE_TIME <'" + year + "-" + may + "-31'";
			// List<Map<String, Object>> aplist = jt.queryForList(sql, new Object[] { udid
			// });
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> aplist = cp.getResultList();
			if (aplist.size() > 0) {
				for (int i = 0; i < aplist.size(); i++) {
					String time = aplist.get(i).get("CREATE_TIME").toString();
					String day = time.substring(0, 10);
					String h = time.substring(11, 16);
					map.put("title", aplist.get(i).get("NEW_TITLE").toString());
					map.put("time", h);
					map.put("ctime", t.getCustomStr(day));// 格式化时间
					map.put("price", aplist.get(i).get("MONEY").toString());
					map.put("url", aplist.get(i).get("HAND_URL").toString());
					list.add(map);
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	public List<Map<String, Object>> findRefunds(String udid, String numPerPage, String currentPage) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		TimeCycle t = new TimeCycle();
		CurrentPage cp = new CurrentPage();
		try {
			// 查询 该教师 下 STATE 为1退款中和2已经退款的作文 的 退款账单
			String sql = "SELECT c.ID,r.CREATE_TIME,r.STATE,c.NEW_TITLE,c.DRAFT,c.GEADE,s.NICKNAME ,s.HEAD FROM teacher as t,refund as r,composition as c,student s WHERE r.STATE in (1,2) and r.TEACHER_ID=t.ID and r.COM_ID=c.ID and c.UDID = s.UDID and t.UDID= '" + udid + "' order by r.CREATE_TIME desc ";
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> clist = cp.getResultList();
			if (clist != null && clist.size() > 0) {
				for (int i = 0; i < clist.size(); i++) {
					map = new HashMap<String, Object>();
					if (clist.get(i).get("NICKNAME") != null) {
						map.put("author", clist.get(i).get("NICKNAME").toString());
					} else {
						map.put("author", "");
					}
					map.put("stuHead", CommUtils.judgeUrl(clist.get(i).get("HEAD")));
					if (clist.get(i).get("STATE") != null) {
						map.put("state", clist.get(i).get("STATE").toString());
					} else {
						map.put("state", "");
					}
					if (clist.get(i).get("DRAFT") != null) {
						map.put("draft", clist.get(i).get("DRAFT").toString());
					} else {
						map.put("draft", "");
					}
					if (clist.get(i).get("GEADE") != null) {
						map.put("grade", clist.get(i).get("GEADE").toString());
					} else {
						map.put("grade", "");
					}
					map.put("retime", CommUtils.ObjectTime2String(clist.get(i).get("CREATE_TIME")));
					if (clist.get(i).get("NEW_TITLE") != null) {
						map.put("title", clist.get(i).get("NEW_TITLE").toString());
					} else {
						map.put("title", "");
					}
					if (clist.get(i).get("ID") != null) {
						map.put("cid", clist.get(i).get("ID").toString());
					} else {
						map.put("cid", "");
					}
					list.add(map);
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	/* (non Javadoc) 
	 * @Title: changeDevice
	 * @Description: TODO
	 * @param phone 
	 * @see com.rest.service.dao.TeacherDao#changeDevice(java.lang.String) 
	 */
	@Override
	public void changeDevice(String phone, String deviceType) {
		String sql = "update teacher set LASTLOGINDEVICETYPE='"+ deviceType +"' where phone = '" + phone + "'";
		jt.update(sql);
	}

	/* (non Javadoc) 
	 * @Title: sortTeacher
	 * @Description: TODO 
	 * @see com.rest.service.dao.TeacherDao#sortTeacher() 
	 */
	@Override
	public void sortTeacher() {
		DecimalFormat df = new DecimalFormat(".##");
		String sql = "SELECT tea.id ID FROM com_composition comp RIGHT JOIN teacher tea ON	tea.ID = comp.teacher_id GROUP BY tea.id";
		List<Map<String, Object>> list1 = jt.queryForList(sql);
		for (Map<String, Object> map : list1) {
			String id = map.get("id").toString();
			String sql2 = "SELECT AVG(a.ATTIT) ATTIT,AVG(a.PROFES) PROFES,AVG(a.SATISFACTION)SATISFACTION,COUNT(a.ATTIT)count,tea.id,tea.EDU_TIME from teacher tea,com_composition com,appraise a where tea.id = com.teacher_id and a.COMP_ID = com.COMP_ID and tea.id = ?";
			List<Map<String, Object>> list2 = jt.queryForList(sql2, new Object[]{id});
			if (list2.get(0).get("ATTIT") != null && !list2.get(0).get("ATTIT").equals("")) {
				map.put("ATTIT", df.format(list2.get(0).get("ATTIT")));
			}else {
				map.put("ATTIT", "");
			}
			if (list2.get(0).get("PROFES") != null && !list2.get(0).get("PROFES").equals("")) {
				map.put("PROFES", df.format(list2.get(0).get("PROFES")));
			}else {
				map.put("PROFES", "");
			}
			if (list2.get(0).get("SATISFACTION") != null && !list2.get(0).get("SATISFACTION").equals("")) {
				map.put("SATISFACTION", df.format(list2.get(0).get("SATISFACTION")));
			}else {
				map.put("SATISFACTION", "");
			}
			if (list2.get(0).get("EDU_TIME") != null && !list2.get(0).get("EDU_TIME").equals("")) {
				map.put("EDU_TIME", list2.get(0).get("EDU_TIME").toString());
			}else {
				map.put("EDU_TIME", "");
			}
			if (list2.get(0).get("count") != null && !list2.get(0).get("count").equals("")) {
				map.put("count", list2.get(0).get("count").toString());
			}else {
				map.put("count", "");
			}
		}
		List<Map<String, Object>> list = TeacherSortUtils.sortTeacher(list1);
		for (Map<String, Object> map2 : list) {
			for (Map.Entry<String, Object> entry : map2.entrySet()) {  
				String id = entry.getKey();
				double num = (double) entry.getValue();
				String sql2 = "update teacher set sortNum = ? where id = ?";
				jt.update(sql2, new Object[]{num, id});
			}  
			
		}
	}

	//点评案例列表
	@Override
	public List<Map<String, Object>> compositionList(String currentPage,
			String numPerPage) {
		int currentNum = 0;
		int numPerNum = 10;
		if (numPerPage != null && !numPerPage.equals("")) {
			numPerNum = Integer.parseInt(numPerPage);
		}
		if (currentPage != null && !currentPage.equals("")) {
			currentNum = (Integer.parseInt(currentPage) - 1) * numPerNum;
		}
		
		String sql = "SELECT com.CONTENT content,ccom.SCORE score,com.NEW_TITLE title,com.GEADE grade,com.ID cid from com_composition ccom," +
				"composition com WHERE com.STATE = 3 AND com.ID = ccom.COMP_ID ORDER BY ccom.COM_TIME DESC LIMIT ?,?";
		try {
			List<Map<String, Object>> list = jt.queryForList(sql, new Object[]{currentNum, numPerNum});
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public void jisuan() {
		String sql1 = "SELECT id from teacher";
		String sql2 = "SELECT UNIX_TIMESTAMP(c.CREATE_TIME) time1,UNIX_TIMESTAMP(cc.COM_TIME) time2 from composition c,com_composition cc,s_order s WHERE c.ID in (SELECT DISTINCT ID FROM composition c WHERE c.TID = ?) and cc.COMP_ID = c.ID AND c.STATE = 3 AND cc.STATE = 3 AND s.COMP_ID = c.ID AND s.STATE = 3 ORDER BY cc.COM_TIME DESC";
		String sql3 = "UPDATE teacher SET AVG_TIME = ? WHERE ID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql1);
		for (Map<String, Object> map : list) {
			Object id = map.get("id");
			List<Map<String,Object>> list2 = jt.queryForList(sql2, new Object[]{id});
			if (list2.size() > 0) {
				int count = 0;
				long l = 0;
				for (Map<String, Object> map2 : list2) {
					if (map2.get("time1") == null || map2.get("time2") == null) {
						continue;
					}
					long time1 = (long) map2.get("time1");
					long time2 = (long) map2.get("time2");
					l += (time2 - time1);
					count += 1;
				}
				long result = l / count;
				String avg_time = CommUtils.longTimeToString(result);
				jt.update(sql3, new Object[]{avg_time, id});
				System.out.println("id="+id+",couint="+count+",avg_time="+avg_time);
			}
		}
	}

	@Override
	public List<Map<String, Object>> compositionListNoTeacher(String currentPage, String numPerPage) {
		int num = Integer.parseInt(numPerPage) * (Integer.parseInt(currentPage) - 1);
		int count = Integer.parseInt(numPerPage);
		String sql = "SELECT c.ID cid,c.GEADE grade,c.NEW_TITLE title,c.CREATE_TIME time,s.NICKNAME sname,c.CONTENT content,c.IMAGE1 image1,c.IMAGE2 image2,c.IMAGE3 image3,s.HEAD shead FROM composition c,student s,s_order so WHERE c.STATE = 2 AND so.STATE=2 AND c.TID IS null AND c.UDID = s.UDID AND c.ID = so.COMP_ID ORDER BY so.GMT_PAYMENT DESC LIMIT ?,?";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{num, count});
			for (Map<String, Object> map : list) {
				String shead = map.get("shead").toString();
				shead = CommUtils.judgeUrl(shead);
				map.put("shead", shead);
				String time = map.get("time").toString();
				Date com_date = sdf.parse(time);
				/*Date date = new Date();
				long diff = date.getTime() - com_date.getTime();
				long day = diff / (24 * 60 * 60 * 1000);    
				long hour = (diff / (60 * 60 * 1000) - day * 24);    
				long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
				if (min != 0) {
					map.put("time", min+"分钟前");
				}
				if (hour != 0) {
					map.put("time", hour+"小时前");
				}
				if (day != 0) {
					map.put("time", day+"天前");
				}*/
				map.put("time", CommUtils.formatDate(com_date));
				if (map.get("image1") != null && !"".equals(map.get("image1").toString())) {
					map.put("image1", TomcatListener.serverHost + map.get("image1"));
				}else {
					map.put("image1", "");
				}
				if (map.get("image2") != null && !"".equals(map.get("image2").toString())) {
					map.put("image2", TomcatListener.serverHost + map.get("image2"));
				}else {
					map.put("image2", "");
				}
				if (map.get("image3") != null && !"".equals(map.get("image3").toString())) {
					map.put("image3", TomcatListener.serverHost + map.get("image3"));
				}else {
					map.put("image3", "");
				}
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public int grabComposition(String cid, String udid) {
		String first = "SELECT s.ID FROM s_order s,teacher t WHERE s.TEACHER_ID = t.ID AND s.STATE = 2 AND s.TOTEACHER = 1 AND t.UDID = ?";
		List<Map<String,Object>> list2 = jt.queryForList(first, new Object[]{udid});
		if (list2.size() > 0) {
			return 3;//已有未点评抢单
		}
		String sql = "SELECT t.ID,t.NAME tname,c.TID,c.NEW_TITLE title,s.ID sid FROM composition c,teacher t,student s WHERE c.ID = ? AND t.UDID = ? AND c.UDID = s.UDID";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{cid, udid});
		if (list.get(0).get("TID") == null) {
			String sql2 = "UPDATE composition SET TID = ? WHERE ID = ?";
			String sql3 = "UPDATE s_order SET TEACHER_ID = ? WHERE COMP_ID = ?";
			int i = jt.update(sql2, new Object[]{list.get(0).get("ID"), cid});
			if (i > 0) {
				int j = jt.update(sql3, new Object[]{list.get(0).get("ID"), cid});
				if (j > 0) {
					Date now = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String ctime = dateFormat.format(now);
					String content = "您的这篇《"+list.get(0).get("title")+"》，"+list.get(0).get("tname")+"老师已经开始为您点评，请耐心等待点评结果。";
					String sql4 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,1,2)";
					jt.update(sql4, new Object[] { "系统通知", content, ctime, list.get(0).get("sid")});
					return 1;//抢单成功
				}else {
					jt.update(sql2, new Object[]{null, cid});
				}
			}
		}
		return -1;//抢单失败
	}

	@Override
	public boolean unGrabComposition(String udid, String cid) {
		String sql1 = "SELECT t.ID tid,t.NAME tname,s.ID sid,c.NEW_TITLE title FROM teacher t,student s,composition c WHERE t.UDID = ? AND c.ID = ? AND c.UDID = s.UDID";
		List<Map<String, Object>> list = jt.queryForList(sql1, new Object[]{udid, cid});
		String sql2 = "UPDATE composition SET TID = NULL WHERE id = ? AND TID = ?";
		String sql4 = "UPDATE composition SET TID = ? WHERE id = ?";
		String sql3 = "UPDATE s_order SET TEACHER_ID = NULL WHERE COMP_ID = ?";
		int i = jt.update(sql2, new Object[]{cid, list.get(0).get("tid")});
		if (i > 0) {
			int j = jt.update(sql3, new Object[]{cid});
			if (j > 0) {
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ctime = dateFormat.format(now);
				String content = "您的这篇《"+list.get(0).get("title")+"》，"+list.get(0).get("tname")+"老师已经取消为您点评，请耐心其他老师为您点评。";
				String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,1,2)";
				jt.update(sql5, new Object[] { "系统通知", content, ctime, list.get(0).get("sid")});
				
				return true;
			}else {
				jt.update(sql4, new Object[]{list.get(0).get("tid"), cid});
			}
		}
		return false;
	}

	@Override
	public boolean compositionState(String udid, String cid) {
		String sql = "SELECT * from com_composition c,teacher t WHERE c.TEACHER_ID=t.ID AND t.UDID=? AND c.COMP_ID=?";
		List<Map<String, Object>> list = jt.queryForList(sql, new Object[]{udid, cid});
		if(list.size()>0){
			return true;
		}
		return false;
	}
	
}
