package com.rest.service.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.util.TextUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.StudentDao;
import com.util.CommUtils;
import com.util.CurrentPage;
import com.util.TimeCycle;

public class StudentDaoImpl implements StudentDao {

	public Map<String, Object> findInformation(String udid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "select COUNT(*) from collection where udid = ?";
			String count = jt.queryForObject(sql, new Object[] { udid }, String.class);
			if (count.equals(null)) {
				map.put("collection", "0");
			} else {
				map.put("collection", count);
			}
			String sql2 = "SELECT * FROM student WHERE  UDID= ?";
			List<Map<String, Object>> slist = jt.queryForList(sql2, new Object[] { udid });
			if (slist.size() > 0) {
				map.put("grade", TextUtils.isEmpty((String)slist.get(0).get("GRADE")) ? "" : (String)slist.get(0).get("GRADE"));
				map.put("nickname", TextUtils.isEmpty((String)slist.get(0).get("NICKNAME")) ? "" : (String)slist.get(0).get("NICKNAME"));
				map.put("url", CommUtils.judgeUrl(slist.get(0).get("HEAD")));
				String sql3 = "select COUNT(*) from red_packet where FAILURE =0 and REDUSE=0 and udid = ?";
				String redPacket = jt.queryForObject(sql3, new Object[] { udid }, String.class);
				// 尚未使用红包数量
				if (redPacket.equals(null)) {
					map.put("redPacket", "0");
				} else {
					map.put("redPacket", redPacket);
				}
				return map;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	public Map<String, Object> findHomePage() {
		// 首页
		Map<String, Object> map = new HashMap<String, Object>();
		CurrentPage cp = new CurrentPage();
		List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
		try {
			// 轮播图
			String sql2 = "select CONTENT,NAME,IMAGE,TYPE,URL,POSITION from slideshow where STATE=1 ORDER BY POSITION LIMIT 5";
			List<Map<String, Object>> list = jt.queryForList(sql2);
			Map<String, Object> slistItem = null;
			String host = CommUtils.getServerHost();
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					slistItem = new HashMap<String, Object>();
					if (list.get(j).get("IMAGE") != null && !TextUtils.isEmpty(list.get(j).get("IMAGE").toString())) {
						slistItem.put("image", host + list.get(j).get("IMAGE").toString());
					} else {
						slistItem.put("image", "");
					}
					if (list.get(j).get("CONTENT") != null && !TextUtils.isEmpty(list.get(j).get("CONTENT").toString())) {
						slistItem.put("content", list.get(j).get("CONTENT").toString());
					} else {
						slistItem.put("content", ""); 
					}
					slistItem.put("position", list.get(j).get("POSITION"));
					slistItem.put("type", list.get(j).get("TYPE"));
					slistItem.put("url", list.get(j).get("URL"));
					slistItem.put("title", list.get(j).get("NAME"));
					slist.add(slistItem);
				}
			}
			map.put("slist", slist);
			cp.Page("SELECT temp.*,com.COM_IMAGE1 image1,com.COM_IMAGE2 image2,com.COM_IMAGE3 image3 FROM (select r.ID, r.CONTENT, r.NAME ,r.PUBLISH_TIME,r.AGE_DETAIL,t.CODE_NAME,r.COM_ID from recommend_com r,t_base_code t where r.DISPLAY = 1 AND (r.STYLE = t.CODE or r.TYPE = t.CODE) order by r.PUBLISH_TIME desc)temp LEFT JOIN com_composition com ON temp.COM_ID = com.COMP_ID", 1, 50, jt);
			List<Map<String, Object>> re = cp.getResultList();
			if (re != null && re.size() > 0) {
				for (int i = 0; i < re.size(); i++) {
					Map<String, Object> m = new HashMap<String, Object>();
					if (re.get(i).get("CONTENT") != null && !TextUtils.isEmpty(re.get(i).get("CONTENT").toString())) {
						m.put("content", CommUtils.getArticleBrief(re.get(i).get("CONTENT").toString()));
					} else {
						m.put("content", "");
					}
					m.put("title", re.get(i).get("NAME").toString());
					// 024 素材类型 026 技法类型 013 范文类型015推荐作文
					List<Map<String, Object>> NumList = jt.queryForList("SELECT READ_NUM FROM read_no WHERE type = '015' AND COMP_ID = ?", new Object[] { re.get(i).get("ID") });
					if (NumList != null && NumList.size() > 0) {
						m.put("number", (int) NumList.get(0).get("READ_NUM") > 0 ? (int) NumList.get(0).get("READ_NUM") : 0);
					} else {
						m.put("number", 0);
					}
					m.put("grade", CommUtils.judgeSqlInformation(re.get(i).get("AGE_DETAIL")));
					m.put("codename", CommUtils.judgeSqlInformation(re.get(i).get("CODE_NAME")));
					m.put("dist", "015");
					m.put("cid", re.get(i).get("ID").toString());
					m.put("sourceCompId", CommUtils.judgeSqlInformation(re.get(i).get("COM_ID")));
					m.put("image1", CommUtils.judgeUrl(re.get(i).get("image1")));
					m.put("image2", CommUtils.judgeUrl(re.get(i).get("image2")));
					m.put("image3", CommUtils.judgeUrl(re.get(i).get("image3")));
					rlist.add(m);
				}
				map.put("rlist", rlist);
			} else {
				map.put("rlist", "");
			}
			return map;
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}
	
	public Map<String, Object> findHomePage2() {
		// 首页
		Map<String, Object> map = new HashMap<String, Object>();
		CurrentPage cp = new CurrentPage();
		List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
		try {
			// 轮播图
			String sql2 = "select CONTENT,NAME,IMAGE,TYPE,URL,POSITION from slideshow where STATE=1 ORDER BY POSITION LIMIT 5";
			List<Map<String, Object>> list = jt.queryForList(sql2);
			Map<String, Object> slistItem = null;
			String host = CommUtils.getServerHost();
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					slistItem = new HashMap<String, Object>();
					if (list.get(j).get("IMAGE") != null && !TextUtils.isEmpty(list.get(j).get("IMAGE").toString())) {
						slistItem.put("image", host + list.get(j).get("IMAGE").toString());
					} else {
						slistItem.put("image", "");
					}
					if (list.get(j).get("CONTENT") != null && !TextUtils.isEmpty(list.get(j).get("CONTENT").toString())) {
						slistItem.put("content", list.get(j).get("CONTENT").toString());
					} else {
						slistItem.put("content", ""); 
					}
					slistItem.put("position", list.get(j).get("POSITION"));
					slistItem.put("type", list.get(j).get("TYPE"));
					slistItem.put("url", list.get(j).get("URL"));
					slistItem.put("title", list.get(j).get("NAME"));
					slist.add(slistItem);
				}
			}
			map.put("slist", slist);
			cp.Page("SELECT temp.*,com.SCORE,com.COM_IMAGE1 image1,com.COM_IMAGE2 image2,com.COM_IMAGE3 image3 FROM (select r.ID, r.CONTENT, r.NAME ,r.PUBLISH_TIME,r.AGE_DETAIL,t.CODE_NAME,r.COM_ID,r.AUTHOR from recommend_com r,t_base_code t where r.DISPLAY = 1 AND (r.STYLE = t.CODE or r.TYPE = t.CODE))temp LEFT JOIN com_composition com ON temp.COM_ID = com.COMP_ID order by PUBLISH_TIME desc", 1, 50, jt);
			List<Map<String, Object>> re = cp.getResultList();
			if (re != null && re.size() > 0) {
				for (int i = 0; i < re.size(); i++) {
					Map<String, Object> m = new HashMap<String, Object>();
					if (re.get(i).get("CONTENT") != null && !TextUtils.isEmpty(re.get(i).get("CONTENT").toString())) {
						m.put("content", CommUtils.getArticleBrief(re.get(i).get("CONTENT").toString()));
					} else {
						m.put("content", "");
					}
					m.put("title", re.get(i).get("NAME").toString());
					// 024 素材类型 026 技法类型 013 范文类型015推荐作文
					List<Map<String, Object>> NumList = jt.queryForList("SELECT READ_NUM FROM read_no WHERE type = '015' AND COMP_ID = ?", new Object[] { re.get(i).get("ID") });
					if (NumList != null && NumList.size() > 0) {
						m.put("number", (int) NumList.get(0).get("READ_NUM") > 0 ? (int) NumList.get(0).get("READ_NUM") : 0);
					} else {
						m.put("number", 0);
					}
					m.put("grade", CommUtils.judgeSqlInformation(re.get(i).get("AGE_DETAIL")));
					m.put("codename", CommUtils.judgeSqlInformation(re.get(i).get("CODE_NAME")));
					m.put("dist", "015");
					m.put("cid", re.get(i).get("ID").toString());
					m.put("sourceCompId", CommUtils.judgeSqlInformation(re.get(i).get("COM_ID")));
					m.put("image1", CommUtils.judgeUrl(re.get(i).get("image1")));
					m.put("image2", CommUtils.judgeUrl(re.get(i).get("image2")));
					m.put("image3", CommUtils.judgeUrl(re.get(i).get("image3")));
					if (re.get(i).get("AUTHOR") == (null) || re.get(i).get("AUTHOR").toString().equals("")) {
						m.put("author", "佚名");
					} else {
						m.put("author", re.get(i).get("AUTHOR").toString());
					}
					if (re.get(i).get("PUBLISH_TIME") == (null)) {
						m.put("time", "");
					} else {
						m.put("time", CommUtils.ObjectTime2String(re.get(i).get("PUBLISH_TIME")));
					}
					m.put("score", CommUtils.judgeSqlInformation(re.get(i).get("SCORE")));
					rlist.add(m);
				}
				map.put("rlist", rlist);
			} else {
				map.put("rlist", "");
			}
			return map;
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	// 我的评价列表
	public List<Map<String, Object>> findAppraise(String udid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT t.NAME,t.HAND_URL,a.APPR_TIME,a.ID,c.DRAFT,c.NEW_TITLE FROM appraise as a,teacher as t,composition as c WHERE a.COMP_ID=c.id and a.TEACHER_ID=t.ID and a.udid= ? ORDER BY a.APPR_TIME DESC";
			List<Map<String, Object>> aplist = jt.queryForList(sql, new Object[] { udid });
			if (aplist.size() > 0) {
				for (int i = 0; i < aplist.size(); i++) {
					if (aplist.get(i).get("NEW_TITLE") == (null)) {
						map.put("title", "");
					} else {
						map.put("title", aplist.get(i).get("NEW_TITLE").toString());
					}
					map.put("time", CommUtils.ObjectTime2String(aplist.get(i).get("APPR_TIME")));
					if (aplist.get(i).get("DRAFT") == (null)) {
						map.put("draft", "");
					} else {
						map.put("draft", aplist.get(i).get("DRAFT").toString());
					}
					if (aplist.get(i).get("ID") == (null)) {
						map.put("pid", "");
					} else {
						map.put("pid", aplist.get(i).get("ID").toString());
					}
					if (aplist.get(i).get("NAME") == (null)) {
						map.put("name", "");
					} else {
						map.put("name", aplist.get(i).get("NAME").toString());
					}
					map.put("url", CommUtils.judgeUrl(aplist.get(i).get("HAND_URL")));
					list.add(map);
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findBills2(String udid, String numPerPage, String currentPage) {
		int num = Integer.parseInt(numPerPage) * (Integer.parseInt(currentPage) - 1);
		int count = Integer.parseInt(numPerPage);
		String sql1 = "SELECT * from ("
				+ "SELECT s.ID id,s.BUYER_PAY_AMOUNT price,s.GMT_PAYMENT time,s.COMP_TITLE title,'邀请点评' "
				+ "type FROM s_order s, student stu WHERE stu.UDID = ? AND s.STU_ID = stu.ID AND s.BUYER_PAY_AMOUNT != 0 "
				+ "union all "
				+ "SELECT r.ID id,r.MONEY price,r.DISPOSE_TIME time,r.COMP_TITLE title,'退款' type FROM refund r,"
				+ "student s WHERE r.STATE = 2 AND s.UDID = ? AND s.ID = r.STU_ID AND r.MONEY != 0 "
				+ "union all "
				+ "SELECT c.ID id,c.BUYER_PAY_AMOUNT price,c.GMT_PAYMENT time,'无' title,'购买评点卡' type   FROM c_order c, student s "
				+ "WHERE c.STU_ID = s.ID AND s.UDID = ? AND c.BUYER_PAY_AMOUNT != 0"
				+ ")a ORDER BY a.time desc limit ?,?";
		try {
			List<Map<String, Object>> list = jt.queryForList(sql1, new Object[]{udid, udid, udid, num, count});
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> map : list) {
				String time = map.get("time").toString();
				Calendar cal = new GregorianCalendar();
				Date date = sdf.parse(time);
				cal.setTime(date);
				String year = cal.get(Calendar.YEAR) + "";
				String month = cal.get(Calendar.MONTH) + 1 + "";
				String day = cal.get(Calendar.DAY_OF_MONTH) + "";
				String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
				String minute = cal.get(Calendar.MINUTE) + "";
				month = month.length()==1?"0"+month:month;
				day = day.length()==1?"0"+day:day;
				hour = hour.length()==1?"0"+hour:hour;
				minute = minute.length()==1?"0"+minute:minute;
				map.put("ctime", month + "月" + day + "日");
				map.put("may", month);
				map.put("time", hour + ":" + minute);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	//查看订单详情
	public Map<String, Object> billDetails(String id,String type) {
		try {
			Map<String, Object> list = new HashMap<String, Object>();
			String sql = "";
			if(type.equals("邀请点评")){
				sql = "SELECT t.HAND_URL handurl,t.`NAME` tname,s.BUYER_PAY_AMOUNT price,s.STATE state,'邀请点评' type,s.PAYMENT payment,s.GMT_PAYMENT time,s.GMT_PAYMENT ptime FROM s_order s LEFT JOIN teacher t ON s.TEACHER_ID=t.ID WHERE s.id=?";
			} else if(type.equals("购买评点卡")){
				sql = "SELECT '' handurl,'' tname,c.BUYER_PAY_AMOUNT price,'0' state,'购买评点卡' type,c.PAYMENT payment, c.GMT_PAYMENT time,c.GMT_PAYMENT ptime FROM c_order c WHERE c.id=?";
			} else if(type.equals("退款")){
				sql = "SELECT t.HAND_URL handurl,t.`NAME` tname,r.ORDER_PRICE price,r.STATE state,'退款' type,r.PAYMENT payment, r.CREATE_TIME time,r.DISPOSE_TIME ptime FROM refund r LEFT JOIN teacher t ON r.TEACHER_ID=t.ID WHERE r.id=?";
			}
			List<Map<String, Object>> flist = jt.queryForList(sql, new Object[]{id});
			if (flist.size() > 0) {
				list.put("handurl", CommUtils.judgeUrl(flist.get(0).get("handurl") ));// 头像地址
				list.put("tname", CommUtils.judgeSqlInformation(flist.get(0).get("tname")));
				list.put("price", CommUtils.judgeSqlInformation(flist.get(0).get("price")));
				list.put("state","交易成功");
				String state = CommUtils.judgeSqlInformation(flist.get(0).get("state"));
				if(type.equals("邀请点评")){
					if(state.equals("5")){
						list.put("state","退款中");
					}else if(state.equals("6")){
						list.put("state","交易关闭");
					}
					list.put("description","邀请"+flist.get(0).get("tname")+"点评");
				}else if(type.equals("退款")){
					list.put("state","退款成功");
					list.put("description","邀请"+flist.get(0).get("tname")+"点评");
				}else if(type.equals("购买评点卡")){
					list.put("description","购买评点卡");
				}
				
				list.put("type", CommUtils.judgeSqlInformation(flist.get(0).get("type")));
				list.put("payment", CommUtils.judgeSqlInformation(flist.get(0).get("payment")));
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//list.put("time", sdf.format(flist.get(0).get("time")));
				list.put("time", CommUtils.ObjectTime2String(flist.get(0).get("time")));
				list.put("ptime", CommUtils.ObjectTime2String(flist.get(0).get("ptime")));
				return list;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)

				return null;
		}
		return null;
	}

	// 我的本月账单
	public List<Map<String, Object>> findBills(String udid, String numPerPage, String currentPage) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Calendar date = Calendar.getInstance();
		Map<String, Object> map = null;
		try {
			CurrentPage cp = new CurrentPage();
			String may = String.valueOf(date.get(Calendar.MONTH) + 1);
			String sql = "SELECT b.* FROM (SELECT a.* , r.DISPOSE_TIME FROM (SELECT o.ID,o.BUYER_PAY_AMOUNT,o.COMP_TITLE,o.GMT_PAYMENT,o.STATE,t.HAND_URL FROM s_order as o, teacher as t , student as s WHERE t.ID = o.TEACHER_ID and o.STU_ID = s.ID and o.STATE in (1,2,3,4,5,6,7) and s.UDID= '" + udid + "' and o.BUYER_PAY_AMOUNT != 0)a LEFT JOIN refund as r on r.ORDER_ID=a.ID UNION  ALL SELECT a.* , r.DISPOSE_TIME FROM (SELECT o.ID,o.BUYER_PAY_AMOUNT,o.COMP_TITLE,o.UPDATE_TIME AS GMT_PAYMENT,o.STATE,t.HAND_URL FROM s_order as o, teacher as t , student as s WHERE t.ID = o.TEACHER_ID and o.STU_ID = s.ID and o.STATE in (6) and s.UDID= '" + udid + "')a LEFT JOIN refund as r on r.ORDER_ID=a.ID)b ORDER BY b.GMT_PAYMENT Desc";
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> aplist = cp.getResultList();
			if (aplist != null && aplist.size() > 0) {
				for (int i = 0; i < aplist.size(); i++) {
					map = new HashMap<String, Object>();
					if (aplist.get(i).get("DISPOSE_TIME") != null && !TextUtils.isEmpty(aplist.get(i).get("DISPOSE_TIME").toString()) && "6".equals(aplist.get(i).get("STATE").toString()) && aplist.get(i).get("GMT_PAYMENT").equals(aplist.get(i).get("DISPOSE_TIME"))) {
						Map<String, Object> map2 =refundData(aplist.get(i),may);
						if (!"0.00".equals(map2.get("price")) && !"0.0".equals(map2.get("price")) && !"0".equals(map2.get("price"))) {
							list.add(map2);
						}
					}else{
						Map<String, Object> map2 =commentData(aplist.get(i),may);
						if (!"0.00".equals(map2.get("price")) && !"0.0".equals(map2.get("price")) && !"0".equals(map2.get("price"))) {
							list.add(map2);
						}
					}
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}
	//邀请点评账单
	public Map<String, Object> commentData(Map<String, Object> map, String may){
		Map<String, Object> map2 =new HashMap<String, Object>();
		String time2 = map.get("GMT_PAYMENT").toString();
		String day2 = time2.substring(0, 10);
		//转化时间格式
		String[] sts2 = day2.split("-");
		StringBuilder sb2 = new StringBuilder();
		String yue2 = new Integer(sts2[1]).toString();
		String ri2 = new Integer(sts2[2]).toString();
		sb2.append(yue2).append("月").append(ri2).append("日");
		day2 = sb2.toString();
		
		String h2 = time2.substring(11, 16);
		if (map.get("COMP_TITLE") == (null)) {
			map2.put("title", "");
		} else {
			map2.put("title", map.get("COMP_TITLE"));
		}
		map2.put("time", h2);
		map2.put("ctime", day2);
		map2.put("price", map.get("BUYER_PAY_AMOUNT"));
		map2.put("refunds", "邀请批改");
		map2.put("may", may);
		map2.put("url", CommUtils.judgeUrl(map.get("HAND_URL")));
		String strs2[] = time2.split("-");
		map2.put("may", strs2[1]);
		return map2;
	}
	//退款账单
	public Map<String, Object> refundData(Map<String, Object> map, String may){
		Map<String, Object> map2 =new HashMap<String, Object>();
		String time2 = map.get("DISPOSE_TIME").toString();
		String day2 = time2.substring(0, 10);
		//转化时间格式
		String[] sts2 = day2.split("-");
		StringBuilder sb2 = new StringBuilder();
		String yue2 = new Integer(sts2[1]).toString();
		String ri2 = new Integer(sts2[2]).toString();
		sb2.append(yue2).append("月").append(ri2).append("日");
		day2 = sb2.toString();
		
		String h2 = time2.substring(11, 16);
		if (map.get("COMP_TITLE") == (null)) {
			map2.put("title", "");
		} else {
			map2.put("title", map.get("COMP_TITLE"));
		}
		map2.put("time", h2);
		map2.put("ctime", day2);
		map2.put("price", map.get("BUYER_PAY_AMOUNT"));
		map2.put("refunds", "退款");
		map2.put("may", may);
		map2.put("url", CommUtils.judgeUrl(map.get("HAND_URL")));
		String strs2[] = time2.split("-");
		map2.put("may", strs2[1]);
		return map2;
	}

	// 我的上月账单
	public List<Map<String, Object>> findLastBills(String udid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		Map<String, Object> map = new HashMap<String, Object>();
		TimeCycle t = new TimeCycle();
		try {
			String may = String.valueOf(date.get(Calendar.MONTH));
			// String da = String.valueOf(date.get(Calendar.DATE));
			String sql = "SELECT o.MONEY,c.NEW_TITLE,o.CREATE_TIME,s.HEAD FROM s_order as o,composition as c,student as s WHERE  c.ID=o.COMP_ID and o.STU_ID=s.ID and o.STATE=3 and s.udid= ? and o.CREATE_TIME >='" + year + "-" + may + "-1' and o.CREATE_TIME <'" + year + "-" + may + "-31'";
			List<Map<String, Object>> aplist = jt.queryForList(sql, new Object[] { udid });
			if (aplist.size() > 0) {
				for (int i = 0; i < aplist.size(); i++) {
					String time = aplist.get(i).get("CREATE_TIME").toString();
					String day = time.substring(0, 10);
					String h = time.substring(11, 16);
					map.put("title", aplist.get(i).get("NEW_TITLE").toString());
					map.put("time", h);
					map.put("ctime", t.getCustomStr(day));// 格式化时间
					map.put("price", aplist.get(i).get("MONEY").toString());
					map.put("refunds", "邀请批改");
					map.put("url", aplist.get(i).get("HEAD").toString());
					list.add(map);
				}
			}
			String sql1 = "SELECT r.MONEY,r.COMP_TITLE,r.CREATE_TIME,s.HEAD FROM refund as r,student as s WHERE  r.STU_ID=s.ID and r.STATE=2 and s.udid= ? and r.CREATE_TIME >='" + year + "-" + may + "-1' and r.CREATE_TIME <'" + year + "-" + may + "-31'";
			List<Map<String, Object>> relist = jt.queryForList(sql1, new Object[] { udid });
			if (relist.size() > 0) {
				for (int i = 0; i < relist.size(); i++) {
					String time = relist.get(i).get("CREATE_TIME").toString();
					String day = time.substring(0, 10);
					String h = time.substring(11, 16);
					map.put("title", relist.get(i).get("NEW_TITLE").toString());
					map.put("time", h);
					map.put("ctime", t.getCustomStr(day));// 格式化时间
					map.put("price", relist.get(i).get("MONEY").toString());
					map.put("refunds", "退款");
					map.put("url", relist.get(i).get("HEAD").toString());
					list.add(map);
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;
	}

	public List<Map<String, Object>> findTeacher(String pid, String numPerPage, String currentPage) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		CurrentPage cp = new CurrentPage();
		try {
			String sql = "SELECT c.ID, c.CONTENT, m.SCORE, c.NEW_TITLE, c.DRAFT, c.IMAGE1, c.IMAGE2, c.IMAGE3, c.GEADE ,s.NICKNAME ,m.COM_TIME FROM com_composition AS m,composition as c,student AS s WHERE COMP_ID=c.ID AND c.UDID = s.UDID AND c.`OPEN` = TRUE AND m.TEACHER_ID=" + pid + " AND c.STATE=3 order by COM_TIME DESC ";
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> aplist = cp.getResultList();
			if (aplist != null && aplist.size() > 0) {
				for (int i = 0; i < aplist.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("cid", aplist.get(i).get("ID").toString());
					if (aplist.get(i).get("CONTENT") == (null)) {
						map.put("content", "");
					} else {
						map.put("content", aplist.get(i).get("CONTENT").toString());
					}
					if (aplist.get(i).get("GEADE") == (null)) {
						map.put("grade", "");
					} else {
						map.put("grade", aplist.get(i).get("GEADE").toString());
					}
					if (aplist.get(i).get("SCORE") == (null)) {
						map.put("score", "");
					} else {
						map.put("score", aplist.get(i).get("SCORE").toString());
					}
					if (aplist.get(i).get("NEW_TITLE") == (null)) {
						map.put("title", "");
					} else {
						map.put("title", aplist.get(i).get("NEW_TITLE").toString());
					}
					if (aplist.get(i).get("DRAFT") == (null)) {
						map.put("draft", "");
					} else {
						map.put("draft", aplist.get(i).get("DRAFT").toString());
					}

					if (aplist.get(i).get("IMAGE1") != null && !TextUtils.isEmpty(aplist.get(i).get("IMAGE1").toString())) {
						map.put("image1", CommUtils.getServerHost() + aplist.get(i).get("IMAGE1").toString());
					} else {
						map.put("image1", "");
					}
					if (aplist.get(i).get("IMAGE2") != null && !TextUtils.isEmpty(aplist.get(i).get("IMAGE2").toString())) {
						map.put("image2", CommUtils.getServerHost() + aplist.get(i).get("IMAGE2").toString());
					} else {
						map.put("image2", "");
					}
					if (aplist.get(i).get("IMAGE3") != null && !TextUtils.isEmpty(aplist.get(i).get("IMAGE3").toString())) {
						map.put("image3", CommUtils.getServerHost() + aplist.get(i).get("IMAGE3").toString());
					} else {
						map.put("image3", "");
					}
					if (aplist.get(i).get("NICKNAME") == (null)) {
						map.put("nickname", "");
					} else {
						map.put("nickname", aplist.get(i).get("NICKNAME").toString());
					}
					if (aplist.get(i).get("COM_TIME") == (null)) {
						map.put("ctime", "");
					} else {
						map.put("ctime", CommUtils.ObjectTime2String(aplist.get(i).get("COM_TIME")));
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

	// 退款售后
	public List<Map<String, Object>> findreAfter(String udid, String numPerPage, String currentPage) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CurrentPage cp = new CurrentPage();
		try {
			String sql = "SELECT s.HEAD,s.ID,r.CREATE_TIME,r.STATE,r.MONEY,c.NEW_TITLE,c.DRAFT,c.AUTHOR FROM student as s,refund as r,composition as c WHERE r.STU_ID=s.ID and r.COM_ID=c.ID and s.UDID='" + udid + "'" + "  ORDER BY r.CREATE_TIME DESC";
			// List<Map<String, Object>> clist = jt.queryForList(sql, new Object[] { udid
			// });
			cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
			List<Map<String, Object>> clist = cp.getResultList();
			if (clist.size() > 0) {
				for (int i = 0; i < clist.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (clist.get(i).get("AUTHOR") == null) {
						map.put("author", "");
					} else {
						map.put("author", clist.get(i).get("AUTHOR").toString());
					}
					if (clist.get(i).get("DRAFT") == null) {
						map.put("author", "");
					} else {
						map.put("draft", clist.get(i).get("DRAFT").toString());
					}
					if (clist.get(i).get("MONEY") == null) {
						map.put("money", "");
					} else {
						map.put("money", clist.get(i).get("MONEY").toString());
					}
					if (clist.get(i).get("CREATE_TIME") == null) {
						map.put("retime", "");
					} else {
						String time = df.format(clist.get(i).get("CREATE_TIME"));
						map.put("retime", time);
					}
					if (clist.get(i).get("NEW_TITLE") == null) {
						map.put("title", "");
					} else {
						map.put("title", clist.get(i).get("NEW_TITLE").toString());
					}
					if (clist.get(i).get("ID") == null) {
						map.put("cid", "");
					} else {
						map.put("cid", clist.get(i).get("ID").toString());
					}
					if (clist.get(i).get("STATE") == null) {
						map.put("rstate", "");
					} else {
						map.put("rstate", clist.get(i).get("STATE").toString());
					}
					map.put("url", CommUtils.judgeUrl(clist.get(i).get("HEAD")));
					list.add(map);
				}
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return list;

	}

	@Override
	public boolean checkIOS() {
		String sql = "SELECT * from `check`";
		Integer i = jt.queryForObject(sql, Integer.class);
		if (i == 1) {
			return false;
		}else {
			return true;
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
	public List<Map<String, Object>> getTuijianCom(String grade) {
		List<Map<String, Object>> list = new ArrayList<>();
		String sql1 = "SELECT temp.*,com.COM_IMAGE1 image1,com.COM_IMAGE2 image2,com.COM_IMAGE3 image3 FROM (select r.ID, r.CONTENT, r.NAME ,r.PUBLISH_TIME,r.AGE_DETAIL,t.CODE_NAME,r.COM_ID from recommend_com r,t_base_code t where r.DISPLAY = 1 AND (r.STYLE = t.CODE or r.TYPE = t.CODE) order by r.PUBLISH_TIME desc)temp LEFT JOIN com_composition com ON temp.COM_ID = com.COMP_ID ORDER BY  RAND() LIMIT 4";
		String sql2 = "SELECT temp.*,com.COM_IMAGE1 image1,com.COM_IMAGE2 image2,com.COM_IMAGE3 image3 FROM (select r.ID, r.CONTENT, r.NAME ,r.PUBLISH_TIME,r.AGE_DETAIL,t.CODE_NAME,r.COM_ID from recommend_com r,t_base_code t where r.DISPLAY = 1 AND r.AGE_DETAIL = ? AND (r.STYLE = t.CODE or r.TYPE = t.CODE) order by r.PUBLISH_TIME desc)temp LEFT JOIN com_composition com ON temp.COM_ID = com.COMP_ID ORDER BY  RAND() LIMIT 4";
		if (grade != null && !"".equals(grade)) {
			list = jt.queryForList(sql2, new Object[]{grade});
		}else {
			list = jt.queryForList(sql1);
		}
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				if (map.get("CONTENT") != null && !TextUtils.isEmpty(map.get("CONTENT").toString())) {
					map.put("content", CommUtils.getArticleBrief(map.get("CONTENT").toString()));
				} else {
					map.put("content", "");
				}
				map.put("title", map.get("NAME").toString());
				map.remove("NAME");
				map.remove("CODE_NAME");
				// 024 素材类型 026 技法类型 013 范文类型015推荐作文
				List<Map<String, Object>> NumList = jt.queryForList("SELECT READ_NUM FROM read_no WHERE type = '015' AND COMP_ID = ?", new Object[] { map.get("ID") });
				if (NumList != null && NumList.size() > 0) {
					map.put("number", (int) NumList.get(0).get("READ_NUM") > 0 ? (int) NumList.get(0).get("READ_NUM") : 0);
				} else {
					map.put("number", 0);
				}
				map.put("grade", CommUtils.judgeSqlInformation(map.get("AGE_DETAIL")));
				map.put("codename", CommUtils.judgeSqlInformation(map.get("CODE_NAME")));
				map.put("dist", "015");
				map.put("cid", map.get("ID").toString());
				map.put("sourceCompId", CommUtils.judgeSqlInformation(map.get("COM_ID")));
				map.put("image1", CommUtils.judgeUrl(map.get("image1")));
				map.put("image2", CommUtils.judgeUrl(map.get("image2")));
				map.put("image3", CommUtils.judgeUrl(map.get("image3")));
				map.remove("NAME");
				map.remove("CODE_NAME");
				map.remove("PUBLISH_TIME");
				map.remove("ID");
				map.remove("AGE_DETAIL");
			}
		}
		return list;
	}


}
