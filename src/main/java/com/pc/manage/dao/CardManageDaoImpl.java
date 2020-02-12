package com.pc.manage.dao;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;

public class CardManageDaoImpl {
	private static Logger logger = Logger.getLogger(CardManageDaoImpl.class);
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//学生用户集合   - 条件+分页 - （评点卡赠送）
	public CurrentPage findStudentlist(int currentPage, int numPerPage, String name) {
		String sql = "select s.ID id,s.NICKNAME userName,s.NAME name,s.PHONE phone,s.SEX sex,s.SCHOOL school,g.GRADE grade,s.AREA city from student s,grade g where s.GRADE=g.`CODE` ";
		if(name!=null&&name!=""){
			sql += " and (s.PHONE like '%"+ name +"%' or s.NAME like '%"+ name +"%' or s.NICKNAME like '%"+ name +"%')";
		}
		sql += " ORDER BY s.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//评点卡管理   - 条件+分页
	public CurrentPage findCardlist(int currentPage, int numPerPage, String name) {
		String sql = "select dp.*,m.`NAME` USERNAME,m.USERNAME USERACCOUNT from dpcard_base dp,manage_users m where dp.USER_NO=m.ID and dp.STATE=0";
		if(name!=null&&name!=""){
			sql += " and dp.NAME like '%"+ name +"%'";
		}
		sql += " ORDER BY dp.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//删除评点卡
	public int deleteCard(String id) {
		String sql = "update dpcard_base set STATE=1 where ID="+id;
		int i = jt.update(sql);
		return i;
	}

	//修改评点卡（回显）
	public List<Map<String, Object>> getCardByid(String id) {
		String sql = "select ID,COUNT,NAME,TIME,PRICE,OLD_PRICE from dpcard_base where ID="+id;
		List<Map<String, Object>> forMap = jt.queryForList(sql);
		return forMap;
	}

	//修改评点卡（保存）
	public int updateCard(String id, String name, String count, String validitytime, String price, String old_price, int userId) {
		String sql = "update dpcard_base set USER_NO="+userId;
		if(name!=null&&name!=""){
			sql += " ,NAME='"+name+"'";
		}
		if(count!=null&&count!=""){
			sql += " ,COUNT="+count;
		}
		if(validitytime!=null&&validitytime!=""){
			sql += " ,TIME="+validitytime;
		}
		if(price!=null&&price!=""){
			sql += " ,PRICE='"+price+"'";
		}
		if(old_price!=null&&old_price!=""){
			sql += " ,OLD_PRICE='"+old_price+"'";
		}
		sql += " where ID="+id;
		int i = jt.update(sql);
		return i;
	}
	
	//赠送评点卡
	public String giveCardToStu(String sids, String cid, String suserId) {
		String[] sts = sids.split(",");
		int scount = 0;//成功次数
		int fcount = 0;//失败次数
		
		String sql = "INSERT INTO stu_dpcard (STU_ID,START_TIME,END_TIME,CARD_BASE_ID,TYPE,COUNT,MANAGE_ID) VALUES (?,?,?,?,?,?,?);";
		String sql2 = "SELECT COUNT,TIME FROM dpcard_base WHERE ID = ?";
		String sql3 = "UPDATE stu_dpcard_use SET REMAINING_COUNT = REMAINING_COUNT + ? ,STATE = 0, END_TIME = ? WHERE STU_ID = ?";
		String sql4 = "INSERT INTO stu_dpcard_use (STU_ID,USE_COUNT,REMAINING_COUNT,END_TIME) VALUES (?,?,?,?)";
		String sql5 = "SELECT END_TIME from stu_dpcard_use WHERE STU_ID = ?";
		String sql6 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
		
		StringBuffer result1 = new StringBuffer();
		result1.append("插入stu_dpcard失败：");//记录失败
		StringBuffer result2 = new StringBuffer();
		result2.append("更新stu_dpcard_use失败:");//记录失败
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar now = new GregorianCalendar();
		String start_time = df.format(now.getTime());
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{cid});
		String count = list.get(0).get("COUNT").toString();
		int time = (int) list.get(0).get("TIME");
		now.add(GregorianCalendar.DAY_OF_YEAR, time);
		String end_time = df.format(now.getTime());
		
		for (String sid : sts) {
			try {
				int i = jt.update(sql, new Object[]{sid, start_time, end_time, cid, 1, count, suserId});
				if (i <= 0) {
					fcount += 1;
					result1.append("，学生id："+sid);
				}else {
					int j = 0;
					try {
						List<Map<String,Object>> list2 = jt.queryForList(sql5, new Object[]{sid});
						if (list2 != null && list2.size() > 0 ) {
							Date date = df.parse(list2.get(0).get("END_TIME").toString());
							if (date.getTime() > now.getTime().getTime()) {
								j = jt.update(sql3, new Object[]{count, list2.get(0).get("END_TIME"), sid});
							}else {
								j = jt.update(sql3, new Object[]{count, end_time, sid});
							}
						}else {
							j = jt.update(sql4, new Object[]{sid, 0, count, end_time});
						}
						if (j <= 0) {
							fcount += 1;
							result2.append("，学生id："+sid);
						}else {
							scount += 1;
							jt.update(sql6, new Object[] { "系统通知", "管理员赠送给您一张评点卡，请您注意查收！", start_time, sid, 1, 2 });
						}
					} catch (Exception e) {
						result2.append("，学生id："+sid);
					}
				}
			} catch (Exception e) {
				fcount += 1;
				result1.append("，学生id："+sid);
			}
		}
		if (result1.length() == 0) {
			logger.info("-------"+result1);
		}
		if (result2.length() == 0) {
			logger.info("-------"+result2);
		}
		return "成功"+scount+"个，失败"+fcount+"个！";
	}
	
	//创建评点卡
	public int addCard(String name, String count, String validitytime, String price, int userId, String old_price) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "insert into dpcard_base set COUNT=?,NAME=?,TIME=?,PRICE=?,CREATE_TIME=?,USER_NO=?,STATE=0,OLD_PRICE=?";
		int i = jt.update(sql, new Object[] {count,name,validitytime,price,time,userId,old_price},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.INTEGER,Types.INTEGER});
		return i;
	}

	//次卡使用情况集合
	public CurrentPage findFrequencylist(int currentPage, int numPerPage, String name) {
		String sql = "select s.`NAME`,s.PHONE,sdu.USE_COUNT,sdu.REMAINING_COUNT,sdu.END_TIME from stu_dpcard_use sdu ,student s where sdu.STU_ID=s.ID";
		if(name!=null&&name!=""){
			sql += " and (s.`NAME` like '%"+ name +"%' or s.PHONE like '%"+ name +"%')";
		}
		sql += " ORDER BY sdu.END_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//赠送评点卡集合
	public CurrentPage findGiveCardlist(int currentPage, int numPerPage, String name) {
		String sql = "select sdp.ID,s.`NAME`,s.PHONE,dp.COUNT,m.`NAME` USERACCOUNT,m.USERNAME,sdp.END_TIME,sdp.START_TIME from stu_dpcard sdp ,dpcard_base dp ,student s, manage_users m where sdp.CARD_BASE_ID=dp.ID and sdp.STU_ID=s.ID and sdp.MANAGE_ID=m.ID and TYPE=1";
		if(name!=null&&name!=""){
			sql += " and (s.`NAME` like '%"+ name +"%' or s.PHONE like '%"+ name +"%')";
		}
		sql += " ORDER BY sdp.START_TIME  DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

}
