package com.rest.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.rest.service.dao.OrderDao;
import com.util.CommUtils;
import com.util.SmsUtils;
import com.util.XingeUtils;

public class OrderDaoImpl implements OrderDao {
	
	
	private static Logger logger = Logger.getLogger(OrderDaoImpl.class);

	public int InsterOrder2(Map map) {
		logger.info("-----------开始插入订单表daoImpl------------");
		Object GMT_PAYMENT = map.get("gmt_payment");// 交易付款时间
		Object BUYER_PAY_AMOUNT = map.get("buyer_pay_amount");// 付款金额
		Object SUBJECT = map.get("subject");// 订单标题
		String FUND_BILL_LIST = (String) map.get("fund_bill_list");// 支付金额信息
		double TOTAL_AMOUNT = Double.parseDouble(map.get("total_amount").toString());// 订单金额
		Object SIGN = map.get("sign");// 签名
		Object SIGN_TYPE = map.get("sign_type");// 签名类型
		Object APP_ID = map.get("app_id");// 开发者的应用Id
		Object OUT_TRADE_NO = map.get("out_trade_no");// 商户订单号
		Object TRADE_NO = map.get("trade_no");// 支付宝交易号
		Object PAYMENT = map.get("payment");// 付款方式
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String buyType = (String) map.get("buyType");
		
		String ssql = "select ID,NICKNAME from student where UDID ='" + map.get("udid") + "'";// 查询该学生的ID
		Integer STU_ID = 0;
		String sname = "";
		List<Map<String,Object>> slist = jt.queryForList(ssql);
		String TEACHER_ID = null;
		if (slist != null && slist.size() > 0) {
			STU_ID = (Integer) slist.get(0).get("ID");
			sname = (String) slist.get(0).get("NICKNAME");
		}
		
		if ("2".equals(buyType)) {
			logger.info("------------购买评点卡---------");
			String dpCardId = (String) map.get("dpCardId");
			String sql = "SELECT COUNT,TIME,PRICE FROM dpcard_base WHERE id = ?";
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{dpCardId});
			GregorianCalendar now2 = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
			String start_time = fmtrq.format(now2.getTime());
			now2.add(GregorianCalendar.DAY_OF_YEAR,(Integer)list.get(0).get("time")); 
			String end_time = fmtrq.format(now2.getTime()); 
			Object count = list.get(0).get("count");
			
			//插入会员卡，并获取id
			Number id = getCardId(STU_ID, start_time, end_time, dpCardId, count.toString());
			//更新用户评点卡次数
			String sql4 = "UPDATE stu_dpcard_use SET REMAINING_COUNT = REMAINING_COUNT + ?, END_TIME =?, STATE = 0 WHERE STU_ID = ?";
			String sql5 = "INSERT INTO stu_dpcard_use (STU_ID,USE_COUNT,REMAINING_COUNT,END_TIME) VALUES (?,?,?,?)";
			String sql6 = "SELECT ID FROM stu_dpcard_use WHERE STU_ID = ?";
			List<Map<String, Object>> list2 = jt.queryForList(sql6, new Object[]{STU_ID});
			if (list2 == null || list2.size() == 0) {
				jt.update(sql5, new Object[]{STU_ID, 0, count, end_time});
			}else {
				jt.update(sql4, new Object[]{count, end_time, STU_ID});
			}
			//插入订单
			String sql2 = "INSERT INTO c_order SET OUT_TRADE_NO=?,STU_ID=?,FUND_BILL_LIST=?,PAYMENT=?," +
					"GMT_PAYMENT=?,R_PACK=?,TOTAL_AMOUNT=?,BUYER_PAY_AMOUNT=?,SUBJECT=?," +
					"SIGN_TYPE=?,APP_ID=?,TRADE_NO=?,CARD_ID=?";
			String R_PACK = "2";//未使用红包
			String SUBJECT2 = "购买评点卡";
			int i = jt.update(sql2, new Object[]{OUT_TRADE_NO, STU_ID, FUND_BILL_LIST, PAYMENT, GMT_PAYMENT, R_PACK,
					TOTAL_AMOUNT, BUYER_PAY_AMOUNT, SUBJECT2, SIGN_TYPE, APP_ID, TRADE_NO, id});
			if (i > 0) {
				String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql3, new Object[] { "支付成功", "您已成功购买了一张评点卡。", start_time, STU_ID, 1, 2 });
			}
			return i;
		}else {
			//新流程
			String compId = (String) map.get("compId");
			Object update_time = null;
			Object title = null;
			Object draft = null;
			int state = 2;
			logger.info("------------新流程支付compId=" + compId + "---------");
			String sql = "SELECT CREATE_TIME time,NEW_TITLE title,DRAFT draft from composition where id = ?";
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{compId});
			if (list != null && list.size() > 0) {
				update_time = list.get(0).get("time");
				if (list.get(0).get("title") != null) {
					title = list.get(0).get("title");
				}
				if (list.get(0).get("draft") != null) {
					draft = list.get(0).get("draft");
				}
			}
			if (map.get("tid") != null && !"".equals(map.get("tid").toString())) {
				TEACHER_ID = map.get("tid").toString();
			}
			
			int rest = 0;
			if ("0".equals(map.get("rid"))) { // 没有使用红包 --- 使用红包1,未使用红包2
				String sql2 = "insert into s_order set PAYMENT = ?, GMT_PAYMENT = ?, BUYER_PAY_AMOUNT = ?, SUBJECT= ? ,FUND_BILL_LIST = ? , TOTAL_AMOUNT = ? ,SIGN = ? ,SIGN_TYPE = ?, APP_ID = ? ,OUT_TRADE_NO = ? ,TRADE_NO = ?,TEACHER_ID= ? ,STATE = ? ,STU_ID = ?,R_PACK=2,UPDATE_TIME=?,COMP_TITLE=?,DRAFT=?,COMP_ID=?,TOTEACHER=?";
				try {
					if (TEACHER_ID == null) {
						rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, update_time, title, draft, compId, 1});
					}else {
						rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, update_time, title, draft, compId, 0});
					}
				} catch (Exception e) {
					logger.info("插入订单失败-------------------update_time：" + update_time + ",compId：" + compId + ",OUT_TRADE_NO：" + OUT_TRADE_NO);
				}
				logger.info("-----插入订单---GMT_PAYMENT="+GMT_PAYMENT+",BUYER_PAY_AMOUNT="+BUYER_PAY_AMOUNT+",FUND_BILL_LIST="+FUND_BILL_LIST);
			} else {
				String sql2 = "insert into s_order set PAYMENT = ?, GMT_PAYMENT = ?, BUYER_PAY_AMOUNT = ?, SUBJECT= ? ,FUND_BILL_LIST = ? , TOTAL_AMOUNT = ? ,SIGN = ? ,SIGN_TYPE = ?, APP_ID = ? ,OUT_TRADE_NO = ? ,TRADE_NO = ?,TEACHER_ID= ? ,STATE = ? ,STU_ID = ?, R_PACK=1 , REDID=? ,UPDATE_TIME=?,COMP_TITLE=?,DRAFT=?,COMP_ID=?,TOTEACHER=?";
				if (TEACHER_ID == null) {
					rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, map.get("rid"), update_time, title, draft, compId, 1});
				}else {
					rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, map.get("rid"), update_time, title, draft, compId, 0});
				}
				// 修改红包
				String sql3 = "update red_packet set REDUSE = '1' where ID = '" + map.get("rid") + "'";
				rest = jt.update(sql3);
			}
			String ctime = dateFormat.format(now);// 时间
			String teacherName = null;
			String teacherPhone = null;
			if (TEACHER_ID != null) {
				String teaInfo = "select NAME,PHONE from teacher where ID = " + TEACHER_ID;
				List<Map<String,Object>> list2 = jt.queryForList(teaInfo);
				if (list2 != null && list2.size() > 0) {
					teacherName = CommUtils.judgeSqlInformation(list2.get(0).get("NAME"));
					teacherPhone = CommUtils.judgeSqlInformation(list2.get(0).get("PHONE"));
				}
			}
			
			//学生插入信息
			String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
			if (TEACHER_ID != null) {
				jt.update(sql3, new Object[] { "支付成功", "您已成功支付" + teacherName + "老师的服务，老师将在48小时内为您点评，请耐心等待。", ctime, STU_ID, 1, 10 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
			}else {
				jt.update(sql3, new Object[] { "支付成功", "您已成功支付这篇作文，马上会有老师为您点评，请耐心等待。", ctime, STU_ID, 1, 10 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
			}
			if (TEACHER_ID != null) {
				//老师插入信息
				final String cctime = dateFormat.format(now);
				String sql4 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql4, new Object[] { "被邀请点评", "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评！", cctime, map.get("tid"), 2, 7 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
				//老师推送
				try {
					//调用信鸽推送
					List<String> list2 = findTeacherIdByOrder(TEACHER_ID + "");
					if (list != null && list.size()!=0) {
						String device = list2.get(0);
						String udid = list2.get(1);
						if ("IOS".equals(device)) {
							int badgeNum = 0;
							String sqlBadge = "select badge from teacher where udid = ?";
							List<Map<String, Object>> listBadge1 = jt.queryForList(sqlBadge, new Object[]{udid});
							if (listBadge1 != null && listBadge1.size() > 0) {
								badgeNum = Integer.parseInt((String)((listBadge1.get(0).get("badge"))));
							}
							String sqlUpdateBadge = "update teacher set badge = ? where udid = ?";
							jt.update(sqlUpdateBadge, new Object[]{badgeNum + 1,udid});
							
							JSONObject jsonObject = XingeUtils.pushSingleAccountIos(udid, (badgeNum + 1));
							logger.info("IOS:----------udid="+udid+",badgeNum="+badgeNum+",result="+jsonObject);
						}else if("ANDROID".equals(device)){
							JSONObject jsonObject = XingeUtils.pushSingleAccountAndroid(udid);
							logger.info("ANDROID:------------udid="+udid+",result=" + jsonObject);
						}else {
							logger.info("------推送获取daviceType异常-------" + device);
						}
					}
					//发送短信
					SmsUtils.sendMessage(teacherPhone, sname);
				} catch (Exception e) {
					logger.info("信鸽推送异常:-------------");
				}
			}
			//更改作文状态
			String sql5 = "UPDATE composition SET STATE = 2, TID = ? WHERE id = ?";
			jt.update(sql5, new Object[]{TEACHER_ID, compId});
			
			return rest;
		}
	}
	
	public int InsterOrder(Map map) {
		logger.info("-----------开始插入订单表daoImpl------------");
		Object GMT_PAYMENT = map.get("gmt_payment");// 交易付款时间
		Object BUYER_PAY_AMOUNT = map.get("buyer_pay_amount");// 付款金额
		Object SUBJECT = map.get("subject");// 订单标题
		String FUND_BILL_LIST = (String) map.get("fund_bill_list");// 支付金额信息
		double TOTAL_AMOUNT = Double.parseDouble(map.get("total_amount").toString());// 订单金额
		Object SIGN = map.get("sign");// 签名
		Object SIGN_TYPE = map.get("sign_type");// 签名类型
		Object APP_ID = map.get("app_id");// 开发者的应用Id
		Object OUT_TRADE_NO = map.get("out_trade_no");// 商户订单号
		Object TRADE_NO = map.get("trade_no");// 支付宝交易号
		Object PAYMENT = map.get("payment");// 付款方式
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String buyType = (String) map.get("buyType");
		
		String ssql = "select ID,NICKNAME from student where UDID ='" + map.get("udid") + "'";// 查询该学生的ID
		Integer STU_ID = 0;
		String sname = "";
		String TEACHER_ID = null;
		List<Map<String,Object>> slist = jt.queryForList(ssql);
		if (slist != null && slist.size() > 0) {
			STU_ID = (Integer) slist.get(0).get("ID");
			sname = (String) slist.get(0).get("NICKNAME");
		}
		
		if ("2".equals(buyType)) {
			logger.info("------------购买评点卡---------");
			String dpCardId = (String) map.get("dpCardId");
			String sql = "SELECT COUNT,TIME,PRICE FROM dpcard_base WHERE id = ?";
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{dpCardId});
			GregorianCalendar now2 = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
			String start_time = fmtrq.format(now2.getTime());
			now2.add(GregorianCalendar.DAY_OF_YEAR,(Integer)list.get(0).get("time")); 
			String end_time = fmtrq.format(now2.getTime()); 
			Object count = list.get(0).get("count");
			
			//插入会员卡，并获取id
			Number id = getCardId(STU_ID, start_time, end_time, dpCardId, count.toString());
			//更新用户评点卡次数
			String sql4 = "UPDATE stu_dpcard_use SET REMAINING_COUNT = REMAINING_COUNT + ?, END_TIME =?, STATE = 0 WHERE STU_ID = ?";
			String sql5 = "INSERT INTO stu_dpcard_use (STU_ID,USE_COUNT,REMAINING_COUNT,END_TIME) VALUES (?,?,?,?)";
			String sql6 = "SELECT ID FROM stu_dpcard_use WHERE STU_ID = ?";
			List<Map<String, Object>> list2 = jt.queryForList(sql6, new Object[]{STU_ID});
			if (list2 == null || list2.size() == 0) {
				jt.update(sql5, new Object[]{STU_ID, 0, count, end_time});
			}else {
				jt.update(sql4, new Object[]{count, end_time, STU_ID});
			}
			//插入订单
			String sql2 = "INSERT INTO c_order SET OUT_TRADE_NO=?,STU_ID=?,FUND_BILL_LIST=?,PAYMENT=?," +
					"GMT_PAYMENT=?,R_PACK=?,TOTAL_AMOUNT=?,BUYER_PAY_AMOUNT=?,SUBJECT=?," +
					"SIGN_TYPE=?,APP_ID=?,TRADE_NO=?,CARD_ID=?";
			String R_PACK = "2";//未使用红包
			String SUBJECT2 = "购买评点卡";
			int i = jt.update(sql2, new Object[]{OUT_TRADE_NO, STU_ID, FUND_BILL_LIST, PAYMENT, GMT_PAYMENT, R_PACK,
					TOTAL_AMOUNT, BUYER_PAY_AMOUNT, SUBJECT2, SIGN_TYPE, APP_ID, TRADE_NO, id});
			if (i > 0) {
				String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql3, new Object[] { "支付成功", "您已成功购买了一张评点卡。", start_time, STU_ID, 1, 2 });
			}
			return i;
		}else {
			//新流程
			String compId = (String) map.get("compId");
			Object update_time = null;
			Object title = null;
			Object draft = null;
			int state = 1;
			if (compId != null && !"".equals(compId)) {
				logger.info("------------新流程支付compId=" + compId + "---------");
				String sql = "SELECT CREATE_TIME time,NEW_TITLE title,DRAFT draft from composition where id = ?";
				List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{compId});
				if (list != null && list.size() > 0) {
					if (list.get(0).get("time") != null) {
						update_time = list.get(0).get("time");
					}
					if (list.get(0).get("title") != null) {
						title = list.get(0).get("title");
					}
					if (list.get(0).get("draft") != null) {
						draft = list.get(0).get("draft");
					}
				}
				state = 2;
			}else {
				compId = null;
			}
			
			//区分是否选择老师
			if (map.get("tid") != null && !"".equals(map.get("tid").toString())) {
				TEACHER_ID = map.get("tid").toString();
			}
			
			int rest = 0;
			if ("0".equals(map.get("rid"))) { // 没有使用红包 --- 使用红包1,未使用红包2
				String sql2 = "insert into s_order set PAYMENT = ?, GMT_PAYMENT = ?, BUYER_PAY_AMOUNT = ?, SUBJECT= ? ,FUND_BILL_LIST = ? , TOTAL_AMOUNT = ? ,SIGN = ? ,SIGN_TYPE = ?, APP_ID = ? ,OUT_TRADE_NO = ? ,TRADE_NO = ?,TEACHER_ID= ? ,STATE = ? ,STU_ID = ?,R_PACK=2,UPDATE_TIME=?,COMP_TITLE=?,DRAFT=?,COMP_ID=?,TOTEACHER=?";
				try {
					if (TEACHER_ID == null) {
						rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, update_time, title, draft, compId, 1});
					}else {
						rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, update_time, title, draft, compId, 0});
					}
				} catch (Exception e) {
					logger.info("插入订单失败-------------------update_time：" + update_time + ",compId：" + compId + ",OUT_TRADE_NO：" + OUT_TRADE_NO);
				}
				logger.info("-----插入订单---GMT_PAYMENT="+GMT_PAYMENT+",BUYER_PAY_AMOUNT="+BUYER_PAY_AMOUNT+",FUND_BILL_LIST="+FUND_BILL_LIST);
			} else {
				String sql2 = "insert into s_order set PAYMENT = ?, GMT_PAYMENT = ?, BUYER_PAY_AMOUNT = ?, SUBJECT= ? ,FUND_BILL_LIST = ? , TOTAL_AMOUNT = ? ,SIGN = ? ,SIGN_TYPE = ?, APP_ID = ? ,OUT_TRADE_NO = ? ,TRADE_NO = ?,TEACHER_ID= ? ,STATE = ? ,STU_ID = ?, R_PACK=1 , REDID=? ,UPDATE_TIME=?,COMP_TITLE=?,DRAFT=?,COMP_ID=?,TOTEACHER=?";
				if (TEACHER_ID == null) {
					rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, map.get("rid"), update_time, title, draft, compId, 0});
				}else {
					rest = jt.update(sql2, new Object[] { PAYMENT, GMT_PAYMENT, BUYER_PAY_AMOUNT, SUBJECT, FUND_BILL_LIST, TOTAL_AMOUNT, SIGN, SIGN_TYPE, APP_ID, OUT_TRADE_NO, TRADE_NO, TEACHER_ID, state, STU_ID, map.get("rid"), update_time, title, draft, compId, 1});
				}
				// 修改红包
				String sql3 = "update red_packet set REDUSE = '1' where ID = '" + map.get("rid") + "'";
				rest = jt.update(sql3);
			}
			String ctime = dateFormat.format(now);// 时间
			String teacherName = null;
			String teacherPhone = null;
			if (TEACHER_ID != null) {
				String teaInfo = "select NAME,PHONE from teacher where ID = " + TEACHER_ID;
				List<Map<String,Object>> list2 = jt.queryForList(teaInfo);
				if (list2 != null && list2.size() > 0) {
					teacherName = CommUtils.judgeSqlInformation(list2.get(0).get("NAME"));
					teacherPhone = CommUtils.judgeSqlInformation(list2.get(0).get("PHONE"));
				}
			}
			
			//新流程
			if (compId != null && !"".equals(compId)) {
				//学生插入信息
				String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				if (TEACHER_ID != null) {
					jt.update(sql3, new Object[] { "支付成功", "您已成功支付" + teacherName + "老师的服务，老师将在48小时内为您点评，请耐心等待。", ctime, STU_ID, 1, 10 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
				}else {
					jt.update(sql3, new Object[] { "支付成功", "您已成功支付这篇作文，马上会有老师为您点评，请耐心等待。", ctime, STU_ID, 1, 10 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
				}
				//老师插入信息
				if (TEACHER_ID != null) {
					final String cctime = dateFormat.format(now);
					String sql4 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql4, new Object[] { "被邀请点评", "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评！", cctime, map.get("tid"), 2, 7 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
					//老师推送
					try {
						//调用信鸽推送
						List<String> list = findTeacherIdByOrder(TEACHER_ID + "");
						if (list != null && list.size()!=0) {
							String device = list.get(0);
							String udid = list.get(1);
							if ("IOS".equals(device)) {
								int badgeNum = 0;
								String sqlBadge = "select badge from teacher where udid = ?";
								List<Map<String, Object>> listBadge1 = jt.queryForList(sqlBadge, new Object[]{udid});
								if (listBadge1 != null && listBadge1.size() > 0) {
									badgeNum = Integer.parseInt((String)((listBadge1.get(0).get("badge"))));
								}
								String sqlUpdateBadge = "update teacher set badge = ? where udid = ?";
								jt.update(sqlUpdateBadge, new Object[]{badgeNum + 1,udid});
								
								JSONObject jsonObject = XingeUtils.pushSingleAccountIos(udid, (badgeNum + 1));
								logger.info("IOS:----------udid="+udid+",badgeNum="+badgeNum+",result="+jsonObject);
							}else if("ANDROID".equals(device)){
								JSONObject jsonObject = XingeUtils.pushSingleAccountAndroid(udid);
								logger.info("ANDROID:------------udid="+udid+",result=" + jsonObject);
							}else {
								logger.info("------推送获取daviceType异常-------" + device);
							}
						}
						//发送短信
						SmsUtils.sendMessage(teacherPhone, sname);
						
					} catch (Exception e) {
						logger.info("信鸽推送异常:-------------");
					}
				}
				//更改作文状态
				String sql5 = "UPDATE composition SET STATE = 2 ,TID=? WHERE id = ?";
				jt.update(sql5, new Object[]{TEACHER_ID, compId});
				
			}else {
				String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql3, new Object[] { "支付成功", "您已成功支付" + teacherName + "老师的服务，还没有上传作文的赶紧去上传吧，上传作文后老师将在48小时内为您点评，请耐心等待。", ctime, STU_ID, 1, 10 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
				logger.info("-------老流程付款成功orderDao-----------");
			}
			
			return rest;
		}
	}

	public int FindOrder(Map map) {
		// 补全订单
		int rest = 0;
		String sql = "select * from s_order where out_trade_no ='" + map.get("out_trade_no") + "'";// 订单表
		List<Map<String, Object>> queryForList = jt.queryForList(sql);
		if (queryForList != null) {
			if (queryForList.get(0).get("sign").equals(map.get("sign"))) {
				// 补全订单
				String ssql = "select ID from student where UDID ='" + map.get("udid") + "'";// 订单表
				String stuid = jt.queryForObject(ssql, String.class);
				if ("0".equals(map.get("rid"))) { // 没有使用红包 --- 使用红包1,未使用红包2
					String sql1 = "update s_order set PAYMENT = '" + map.get("pay") + "',STATE=1,STU_ID=" + stuid + ",TEACHER_ID='" + map.get("tid") + "',R_PACK=2 where OUT_TRADE_NO = '" + map.get("out_trade_no") + "'";
					rest = jt.update(sql1);
				} else {
					String sql2 = "update s_order set PAYMENT = '" + map.get("pay") + "',STATE=1,STU_ID=" + stuid + ",R_PACK=1,TEACHER_ID='" + map.get("tid") + "',REDID=" + map.get("rid") + " where OUT_TRADE_NO = '" + map.get("out_trade_no") + "'";
					rest = jt.update(sql2);
					// 修改红包
					String sql3 = "update red_packet set REDUSE = '1' where ID = '" + map.get("rid") + "'";
					rest = jt.update(sql3);
				}
			}
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

	@Override
	public int AddCompToOrder(Map<String, Object> map, String tid) {
		int rest = 0;
		// 修改该订单为待评点状态
		String sql1 = "update s_order set COMP_ID = " + map.get("compId") + ",UPDATE_TIME = '" + map.get("update_time") + "',COMP_TITLE = '" + map.get("title") + "', DRAFT = '" + map.get("draft") + "',STATE = 2 where OUT_TRADE_NO='" + map.get("outTradeNo") + "'";// + "'and STU_ID=" + map.get("stuId");
		rest = jt.update(sql1);
		if (rest > 0) {
			// 插入消息表
			String stuSql = "select NICKNAME from student where UDID = '" + map.get("udid") + "'";
			String stuNickName = "";
			String stuResutl = jt.queryForObject(stuSql, String.class);
			if (stuResutl != null) {
				stuNickName = stuResutl;
			}
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String ctime = dateFormat.format(now);
			String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
			//logger.info("-----------------------------------------------------------------------------------");
			//logger.info("-----------------------------------------------------------------------------------");
			jt.update(sql3, new Object[] { "被邀请点评", "您已收到" + stuNickName + "同学的一篇作文，请在48小时内为其点评", ctime, map.get("tid"), 2, 7 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
			try {
				//调用信鸽推送
				List<String> list = findTeacherIdByOrder(tid);
				if (list != null && list.size()!=0) {
					String device = list.get(0);
					String udid = list.get(1);
					if (device.equals("IOS")) {
						int badgeNum = 0;
						String sqlBadge = "select badge from teacher where udid = ?";
						List<Map<String, Object>> listBadge1 = jt.queryForList(sqlBadge, new Object[]{udid});
						if (listBadge1 != null && listBadge1.size() > 0) {
							badgeNum = Integer.parseInt((String)((listBadge1.get(0).get("badge"))));
						}
						String sqlUpdateBadge = "update teacher set badge = ? where udid = ?";
						jt.update(sqlUpdateBadge, new Object[]{badgeNum + 1,udid});
						
						JSONObject jsonObject = XingeUtils.pushSingleAccountIos(udid, (badgeNum + 1));
						logger.info("IOS:----------" + jsonObject);
					}else if(device.equals("ANDROID")){
						JSONObject jsonObject = XingeUtils.pushSingleAccountAndroid(udid);
						logger.info("ANDROID:-------------" + jsonObject);
					}
				}
			} catch (Exception e) {
				logger.info("信鸽推送异常:-------------");
			}
			
			
			return 1;
		}
		return rest;
	}

	@Override
	public int findOrderByOutTradeNo(String outTradeNo) {
		int rest = 0;
		String sql1 = "select id from s_order where out_trade_no ='" + outTradeNo + "'";// 订单表
		String sql2 = "select id from c_order where out_trade_no ='" + outTradeNo + "'";// 订单表
		List<Map<String, Object>> list1 = jt.queryForList(sql1);
		List<Map<String, Object>> list2 = jt.queryForList(sql2);
		if (list1 != null && list1.size() > 0) {
			rest = 1;
		}
		if (list2 != null && list2.size() > 0) {
			rest = 1;
		}
		return rest;
	}

	/* (non Javadoc) 
	 * @Title: findTeacherIdByOrder
	 * @Description: TODO
	 * @param tid
	 * @return 
	 * @see com.rest.service.dao.OrderDao#findTeacherIdByOrder(java.lang.String) 
	 */
	@Override
	public List<String> findTeacherIdByOrder(String tid) {
		int id = Integer.parseInt(tid);
		String sql = "select LASTLOGINDEVICETYPE,UDID from teacher where id = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{id});
		if (list != null && list.size() != 0) {
			String device = (String) list.get(0).get("LASTLOGINDEVICETYPE");
			String UDID = (String) list.get(0).get("UDID");
			if (device!=null&&!device.equals("")&&UDID!=null&&!UDID.equals("")) {
				List<String> returnlist = new ArrayList<>();
				returnlist.add(device);
				returnlist.add(UDID);
				return returnlist;
			}
		}
		return null;
	}

	/**
	 * 检查作文id是否已经生成订单
	*/
	@Override
	public int findCompositionHasPay(String compId) {
		String sql = "SELECT ID FROM s_order WHERE COMP_ID = ? AND STATE = 2";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{compId});
		if (list != null && list.size() > 0) {
			return 1;
		}
		return 0;
	}
	
	/* (non Javadoc) 
	 * @Title: InsterOrderByCard
	 * @Description: TODO
	 * @param udid
	 * @param cid 
	 * @see com.rest.service.dao.OrderDao#InsterOrderByCard(java.lang.String, java.lang.String) 
	 */
	@Override
	public int InsterOrderByCard(String cid, String udid, String ttid) {
		String sql1 = "insert into s_order set PAYMENT = ?, GMT_PAYMENT = ?, SUBJECT= ? ,FUND_BILL_LIST = ? ," +
				"SIGN_TYPE = ?,TEACHER_ID= ? ,STATE = ? ,STU_ID = ?,COMP_ID =?,COMP_TITLE=?," +
				"DRAFT=?,R_PACK=?,OUT_TRADE_NO=?,TOTAL_AMOUNT=?,BUYER_PAY_AMOUNT=?,UPDATE_TIME=?,TOTEACHER=?";
		String sql2 = "SELECT ID sid,NICKNAME sname FROM student WHERE UDID = ?";
		String sql9 = "SELECT t.PHONE tphone,t.id tid,t.NAME tname,c.NEW_TITLE title,c.DRAFT draft FROM composition c,teacher t WHERE c.ID = ? AND c.TID = t.ID";
		String sql10 = "SELECT NEW_TITLE title,DRAFT draft FROM	composition WHERE ID = ?";
		String sql11 = "UPDATE composition SET TID = ? WHERE ID = ?";
		List<Map<String,Object>> list1 = jt.queryForList(sql2, new Object[]{udid});
		List<Map<String,Object>> list2 = null;
		//ttid为null老流程，ttid为空未选择老师，ttid有值选择老师
		if (ttid == null) {
			list2 = jt.queryForList(sql9, new Object[]{cid});
		}else if("".equals(ttid)){
			list2 = jt.queryForList(sql10, new Object[]{cid});
		}else {
			jt.update(sql11, new Object[]{ttid, cid});
			list2 = jt.queryForList(sql9, new Object[]{cid});
		}
				
		if (list2 == null || list2.size() == 0) {
			logger.info("-------查询作文信息失败---------");
			return 3;
		}
		String SUBJECT = "邀请点评";// subject订单标题
		String FUND_BILL_LIST = null;// fund_bill_list支付金额信息
		String SIGN_TYPE = "DPCARD";// sign_type签名类型
		String PAYMENT = "评点卡";// payment付款方式
		String COMP_TITLE = list2.get(0).get("title").toString();
		String DRAFT = list2.get(0).get("draft").toString();
		String state = "2";
		String R_PACK = "2";
		String OUT_TRADE_NO = CommUtils.getOutTradeNo();
		Object TOTAL_AMOUNT = 0;
		Object BUYER_PAY_AMOUNT = 0;
		Object tname = list2.get(0).get("tname");
		Object sid = list1.get(0).get("sid");
		Object tid = list2.get(0).get("tid");
		Object tphone = list2.get(0).get("tphone");
		Object sname = list1.get(0).get("sname");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String GMT_PAYMENT = df.format(d);//gmt_payment 交易付款时间
		String ctime = df.format(d);
		// buyer_pay_amount付款金额,total_amount订单金额,sign签名
		
		String sql3 = "UPDATE stu_dpcard_use SET USE_COUNT = USE_COUNT + 1, REMAINING_COUNT = REMAINING_COUNT - 1 WHERE STU_ID = ? AND REMAINING_COUNT > 0 AND STATE = 0";
		Integer j = jt.update(sql3, new Object[]{sid});
		if (j <= 0) {
			logger.info("---------更新评点卡次数失败-------------");
			return 2;
		}
		int i =0;
		if ("".equals(ttid)) {
			i = jt.update(sql1, new Object[] { PAYMENT, GMT_PAYMENT, SUBJECT, FUND_BILL_LIST, SIGN_TYPE, tid, 
					state, sid, cid, COMP_TITLE, DRAFT, R_PACK, OUT_TRADE_NO, TOTAL_AMOUNT, BUYER_PAY_AMOUNT, ctime, 1});
		}else {
			i = jt.update(sql1, new Object[] { PAYMENT, GMT_PAYMENT, SUBJECT, FUND_BILL_LIST, SIGN_TYPE, tid, 
					state, sid, cid, COMP_TITLE, DRAFT, R_PACK, OUT_TRADE_NO, TOTAL_AMOUNT, BUYER_PAY_AMOUNT, ctime, 0});
		}
		if (i > 0){
			String sql4 = "update composition set state = 2, TID = ? where id = ?";
			int y = jt.update(sql4, new Object[]{tid, cid});
			if (y <= 0) {
				logger.info("----------更新作文状态失败------------");
				return 4;
			}
			//插入学生信息
			String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
			if (tname != null) {
				jt.update(sql5, new Object[] { "支付成功", "您已成功支付" + tname + "老师的服务，老师将在48小时内为您点评，请耐心等待。", ctime, sid, 1, 10 });
			}else {
				jt.update(sql5, new Object[] { "支付成功", "您已成功支付这篇作文，马上会有老师为您点评，请耐心等待。", ctime, sid, 1, 10 });
			}
			//插入老师信息
			if (tid != null) {
				String sql6 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql6, new Object[] { "被邀请点评", "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评！", ctime, tid, 2, 7});
				//老师推送
				try {
					//调用信鸽推送
					List<String> slist = findTeacherIdByOrder(tid + "");
					String device = slist.get(0);
					String sudid = slist.get(1);
					if ("IOS".equals(device)) {
						int badgeNum = 0;
						String sqlBadge = "select badge from teacher where udid = ?";
						List<Map<String, Object>> listBadge1 = jt.queryForList(sqlBadge, new Object[]{sudid});
						if (listBadge1 != null && listBadge1.size() > 0) {
							badgeNum = Integer.parseInt((String)((listBadge1.get(0).get("badge"))));
						}
						String sqlUpdateBadge = "update teacher set badge = ? where udid = ?";
						jt.update(sqlUpdateBadge, new Object[]{badgeNum + 1,sudid});
						
						JSONObject jsonObject = XingeUtils.pushSingleAccountIos(sudid, (badgeNum + 1));
						logger.info("IOS:----------udid="+sudid+",badgeNum="+badgeNum+",result="+jsonObject);
					}else if("ANDROID".equals(device)){
						JSONObject jsonObject = XingeUtils.pushSingleAccountAndroid(sudid);
						logger.info("ANDROID:------------udid="+sudid+",result=" + jsonObject);
					}else {
						logger.info("------推送获取daviceType异常-------" + device);
					}
					//发送短信
					if (tphone != null && !tphone.toString().equals("")) {
						SmsUtils.sendMessage(tphone.toString(), sname.toString());
					}
				} catch (Exception e) {
					logger.info("信鸽推送异常:-------------");
				}
			}
			String sql7 = "UPDATE stu_dpcard_use SET STATE = 1 WHERE STU_ID = ? AND REMAINING_COUNT = 0";
			int k = jt.update(sql7, new Object[]{sid});
			if (k > 0) {
				String sql8 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql8, new Object[] { "系统通知", "您的评点卡剩余使用次数为0，下一次无法继续使用评点卡支付，请您及时充值续费！", ctime, sid, 1, 2 });
			}
		
		}
		return i;
	}
	
	//插入月卡，返回id
	private Number getCardId(final Integer STU_ID, final String start_time, final String end_time,
			final String dpCardId, final String count) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();  
		final String sql = "INSERT INTO stu_dpcard SET STU_ID =?, START_TIME = ?, END_TIME = ?, CARD_BASE_ID = ?, TYPE = 2, COUNT = ? ";
		int i = jt.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {

				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 
				ps.setInt(1, STU_ID);
				ps.setString(2, start_time);
				ps.setString(3, end_time);
				ps.setInt(4, Integer.parseInt(dpCardId));
				ps.setInt(5, Integer.parseInt(count));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey();
	}
	
}
