package com.rest.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.rest.service.ComStateService;
import com.rest.service.dao.AppraiseDao;
import com.rest.service.dao.ComStateDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;
@Component
public class ComStateServiceImpl implements ComStateService {

	private ComStateDao comStateDao;

	private static Logger logger = Logger.getLogger(ComStateServiceImpl.class);

	public ComStateDao getComStateDao() {
		return comStateDao;
	}

	public void setComStateDao(ComStateDao comStateDao) {
		this.comStateDao = comStateDao;
	}

	private AppraiseDao appraiseDao;

	public AppraiseDao getAppraiseDao() {
		return appraiseDao;
	}

	public void setAppraiseDao(AppraiseDao appraiseDao) {
		this.appraiseDao = appraiseDao;
	}

	// 作文状态(学生)
	@POST
	@Path("/comStateS")
	@Produces(MediaType.APPLICATION_JSON)
	public String comStateS(String json) throws ParseException {
		// json = "{\"BM\":{state: 1,udid: \"1\"}}";
		JSONObject jsonObject;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String state = str1.getString("state");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String numPerPage = str1.getString("numPerPage");
			String currentPage = str1.getString("currentPage");

			// List<Map<String, Object>> comStateS = new ArrayList<Map<String, Object>>();
			// String token=SecurityUtil.hash(udid);
			/*
			 * Map<String, Object> map = new HashMap<>(); map.put("cid", 1);
			 * map.put("draft", "第二稿"); map.put("name", 1); map.put("time", "2017/08/28");
			 * map.put("title", "aaa"); map.put("url", "D:/test.jpg"); comStateS.add(map);
			 */
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> comStateS = comStateDao.findOrderStateList(state, udid, currentPage, numPerPage);
				if (comStateS != null) {
					SecurityUtil.sessionList.add(token);
					for (int i = 0; i < comStateS.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cid", comStateS.get(i).get("cid") == null ? "" : comStateS.get(i).get("cid").toString());// 点评作文唯一标识
						map.put("tid", comStateS.get(i).get("tid") == null ? "" : comStateS.get(i).get("tid").toString());// 老师唯一标识
						map.put("draft", comStateS.get(i).get("draft") == null ? "" : comStateS.get(i).get("draft").toString());// 第几稿
						map.put("name", comStateS.get(i).get("name") == null ? "" : comStateS.get(i).get("name").toString());// 老师姓名
						map.put("price", comStateS.get(i).get("price") == null ? "" : comStateS.get(i).get("price").toString());// 老师价格
						map.put("time", CommUtils.ObjectTime2String(comStateS.get(i).get("time")));
						if (comStateS.get(i).get("teacher_state") != null && !"".equals(comStateS.get(i).get("teacher_state").toString())) {
							map.put("teacher_state", comStateS.get(i).get("teacher_state").toString());
						} else {
							map.put("teacher_state", "0");
						}

						if (comStateS.get(i).get("comType") != null && !"".equals(comStateS.get(i).get("comType").toString())) {
							map.put("comType", comStateS.get(i).get("comType").toString());
						} else {
							map.put("comType", "");
						}
						map.put("title", comStateS.get(i).get("title") == null ? "" : comStateS.get(i).get("title").toString());// 作文标题
						map.put("url", CommUtils.judgeUrl(comStateS.get(i).get("url")));
						map.put("out_trade_num", comStateS.get(i).get("out_trade_num") == null ? "" : comStateS.get(i).get("out_trade_num").toString());// 订单ID
						map.put("orderId", comStateS.get(i).get("orderId") == null ? "" : comStateS.get(i).get("orderId").toString());// 订单主键ID
						map.put("update_time", CommUtils.ObjectTime2String(comStateS.get(i).get("update_time")));
						map.put("have_comment", comStateS.get(i).get("have_comment") == null ? "" : comStateS.get(i).get("have_comment").toString());// 是否有学生评价
						map.put("back_time", CommUtils.ObjectTime2String(comStateS.get(i).get("back_time")));
						map.put("pid", comStateS.get(i).get("pid") == null ? "" : comStateS.get(i).get("pid").toString());// 评论ID
						map.put("pay_time", CommUtils.ObjectTime2String(comStateS.get(i).get("pay_time")));
						map.put("state", state);
						// 付款类型
						if (comStateS.get(i).get("PAYMENT") != null && "评点卡".equals(comStateS.get(i).get("PAYMENT").toString())) {
							map.put("buyType", "1");
						} else {
							map.put("buyType", "0");
						}
						list.add(map);
					}
				}
				JSONArray jsonArray = JSONArray.fromObject(list);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 退款和售后(学生)
	@POST
	@Path("/backAndSaleAfter")
	@Produces(MediaType.APPLICATION_JSON)
	public String comBackAndAfter(String json) throws ParseException {
		// json = "{\"BM\":{state: 1,udid: \"1\"}}";
		JSONObject jsonObject;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			// String state = str1.getString("state");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String scurrentPage = str1.getString("currentPage");
			String snumPerPage = str1.getString("numPerPage");
			Integer currentPage = 0;
			Integer numPerPage = 10;
			if (scurrentPage != null && !scurrentPage.equals("")) {
				currentPage = Integer.parseInt(snumPerPage) * (Integer.parseInt(scurrentPage) - 1);
			}
			if (snumPerPage != null && !snumPerPage.equals("")) {
				numPerPage = Integer.parseInt(snumPerPage);
			}
			// List<Map<String, Object>> comStateS = new ArrayList<Map<String, Object>>();
			// String token=SecurityUtil.hash(udid);
			/*
			 * Map<String, Object> map = new HashMap<>(); map.put("cid", 1);
			 * map.put("draft", "第二稿"); map.put("name", 1); map.put("time", "2017/08/28");
			 * map.put("title", "aaa"); map.put("url", "D:/test.jpg"); comStateS.add(map);
			 */
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> comStateS = comStateDao.findOrderStateListSaleAfter(udid, currentPage, numPerPage);
				if (comStateS != null) {
					SecurityUtil.sessionList.add(token);
					for (int i = 0; i < comStateS.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cid", comStateS.get(i).get("cid") == null ? "" : comStateS.get(i).get("cid").toString());// 点评作文唯一标识
						map.put("tid", comStateS.get(i).get("tid") == null ? "" : comStateS.get(i).get("tid").toString());// 老师唯一标识
						map.put("draft", comStateS.get(i).get("draft") == null ? "" : comStateS.get(i).get("draft").toString());// 第几稿
						map.put("name", comStateS.get(i).get("name") == null ? "" : comStateS.get(i).get("name").toString());// 老师姓名
						map.put("time", CommUtils.ObjectTime2String(comStateS.get(i).get("time")));
						map.put("title", comStateS.get(i).get("title") == null ? "" : comStateS.get(i).get("title").toString());// 作文标题
						map.put("url", CommUtils.judgeUrl(comStateS.get(i).get("url")));
						map.put("out_trade_num", comStateS.get(i).get("out_trade_num") == null ? "" : comStateS.get(i).get("out_trade_num").toString());// 老师头像地址
						map.put("orderId", comStateS.get(i).get("orderId") == null ? "" : comStateS.get(i).get("orderId").toString());// 老师头像地址
						map.put("update_time", CommUtils.ObjectTime2String(comStateS.get(i).get("update_time")));
						map.put("draft", comStateS.get(i).get("order_draft") == null ? "" : comStateS.get(i).get("order_draft").toString());// 第几稿
						map.put("title", comStateS.get(i).get("order_title") == null ? "" : comStateS.get(i).get("order_title").toString());// 标题
						map.put("state", comStateS.get(i).get("state") == null ? "" : comStateS.get(i).get("state").toString());// 状态
						map.put("have_comment", comStateS.get(i).get("have_comment") == null ? "" : comStateS.get(i).get("have_comment").toString());// 是否有学生评价
						map.put("back_time", CommUtils.ObjectTime2String(comStateS.get(i).get("back_time")));
						map.put("money", comStateS.get(i).get("money") == null ? "" : comStateS.get(i).get("money").toString());// 申请退款的时间
						map.put("payway", comStateS.get(i).get("payway") == null ? "" : comStateS.get(i).get("payway").toString());// 申请退款的时间
						list.add(map);
					}
					// System.out.println(jo.toString());
				}
				JSONArray jsonArray = JSONArray.fromObject(list);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 作文状态(老师)
	@POST
	@Path("/comStateT")
	@Produces(MediaType.APPLICATION_JSON)
	public String comStateT(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String state = str1.getString("state");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> comStateT = comStateDao.findComStateT(state, udid, numPerPage, currentPage);
				/*
				 * if (comStateT != null) { SecurityUtil.sessionList.add(token); for (int i = 0;
				 * i < comStateT.size(); i++) { Map<String, Object> map = new HashMap<String,
				 * Object>(); map.put("cid", comStateT.get(i).get("cid") == null ? "" :
				 * comStateT.get(i).get("cid").toString());// 点评作文唯一标识 map.put("tid",
				 * comStateT.get(i).get("tid") == null ? "" :
				 * comStateT.get(i).get("tid").toString());// 老师唯一标识 map.put("draft",
				 * comStateT.get(i).get("draft") == null ? "" :
				 * comStateT.get(i).get("draft").toString());// 第几稿 map.put("name",
				 * comStateT.get(i).get("name") == null ? "" :
				 * comStateT.get(i).get("name").toString());// 老师姓名 map.put("time",
				 * CommUtils.ObjectTime2String(comStateT.get(i).get("time"))); map.put("title",
				 * comStateT.get(i).get("title") == null ? "" :
				 * comStateT.get(i).get("title").toString());// 作文标题 map.put("url",
				 * CommUtils.judgeUrl(comStateT.get(i).get("url"))); map.put("surl",
				 * CommUtils.judgeUrl(comStateT.get(i).get("surl"))); if
				 * (comStateT.get(i).get("sname") != null) { map.put("sname",
				 * comStateT.get(i).get("sname").toString()); }else { map.put("sname", ""); }
				 * 
				 * map.put("out_trade_num", comStateT.get(i).get("out_trade_num") == null ? "" :
				 * comStateT.get(i).get("out_trade_num").toString());// 老师头像地址
				 * map.put("orderId", comStateT.get(i).get("orderId") == null ? "" :
				 * comStateT.get(i).get("orderId").toString());// 老师头像地址 map.put("update_time",
				 * CommUtils.ObjectTime2String(comStateT.get(i).get("update_time")));
				 * map.put("draft", comStateT.get(i).get("order_draft") == null ? "" :
				 * comStateT.get(i).get("order_draft").toString());// 第几稿 map.put("title",
				 * comStateT.get(i).get("order_title") == null ? "" :
				 * comStateT.get(i).get("order_title").toString());// 标题 map.put("state",
				 * comStateT.get(i).get("state") == null ? "" :
				 * comStateT.get(i).get("state").toString());// 状态 map.put("have_comment",
				 * comStateT.get(i).get("have_comment") == null ? "" :
				 * comStateT.get(i).get("have_comment").toString());// 是否有学生评价
				 * map.put("back_time",
				 * CommUtils.ObjectTime2String(comStateT.get(i).get("back_time")));
				 * map.put("money", comStateT.get(i).get("money") == null ? "" :
				 * comStateT.get(i).get("money").toString());// 金额 map.put("grade",
				 * comStateT.get(i).get("grade") == null ? "" :
				 * comStateT.get(i).get("grade").toString());// 年级 map.put("content",
				 * comStateT.get(i).get("content") == null ? "" :
				 * comStateT.get(i).get("content").toString());// 内容 if
				 * (comStateT.get(i).get("image1") != null &&
				 * !"".equals(comStateT.get(i).get("image1"))) { map.put("image1",
				 * CommUtils.getServerHost() + comStateT.get(i).get("image1").toString());// 图片1
				 * }else { map.put("image1", ""); } if (comStateT.get(i).get("image2") != null
				 * && !"".equals(comStateT.get(i).get("image2"))) { map.put("image2",
				 * CommUtils.getServerHost() + comStateT.get(i).get("image2").toString());// 图片2
				 * }else { map.put("image2", ""); } if (comStateT.get(i).get("image3") != null
				 * && !"".equals(comStateT.get(i).get("image3"))) { map.put("image3",
				 * CommUtils.getServerHost() + comStateT.get(i).get("image3").toString());// 图片3
				 * }else { map.put("image3", ""); } list.add(map); } }
				 */
				JSONArray jsonArray = JSONArray.fromObject(comStateT);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 批改作文详情(学生)
	@POST
	@Path("/comPart")
	@Produces(MediaType.APPLICATION_JSON)
	public String comPart(String json) throws ParseException {
		List<Map<String, Object>> list = null;
		JSONObject jsonObject;
		String url = "{\"BM\":{\"dlist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			/*
			 * String token = str1.getString("token"); String udid = str1.getString("udid");
			 */
			String cid = str1.getString("cid");// 点评作文唯一标识
			list = comStateDao.findComPart(cid);
			if (list == (null) || list.size() == 0) {
				return "{\"BM\":{},\"EC\":10025,\"EM\":\"获取作文详情空\"}";
			}
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				url += jsonArray.get(0).toString();
				List<Map<String, Object>> tlist = comStateDao.findTeacherByCompId(cid);
				if (tlist != null && tlist.size() > 0) {
					JSONArray jsontlist = JSONArray.fromObject(tlist);
					url += ",\"tlist\":" + jsontlist.get(0).toString();
				}
			}
			// 取出来评价内容
			String appraiseInfo = getAppraiseInfo(cid);
			if (!TextUtils.isEmpty(appraiseInfo)) {
				url += ",\"appraise\":" + appraiseInfo;
			}
			url += "},\"EC\":0,\"EM\":\"\"}";
			return url;
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取作文详情异常\"}";
		}
	}

	// 批改作文详情(学生)
	@POST
	@Path("/recommendCom")
	@Produces(MediaType.APPLICATION_JSON)
	public String recommendCom(String json) throws ParseException {
		List<Map<String, Object>> list = null;
		JSONObject jsonObject;
		String url = "{\"BM\":{\"dlist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			/*
			 * String token = str1.getString("token"); String udid = str1.getString("udid");
			 */
			String cid = str1.getString("cid");// 点评作文唯一标识
			list = comStateDao.recommendCom(cid);
			if (list == (null) || list.size() == 0) {
				return "{\"BM\":{},\"EC\":10025,\"EM\":\"获取作文详情空\"}";
			}
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				url += jsonArray.get(0).toString();
				List<Map<String, Object>> tlist = comStateDao.findTeacherByCompId(cid);
				if (tlist != null && tlist.size() > 0) {
					JSONArray jsontlist = JSONArray.fromObject(tlist);
					url += ",\"tlist\":" + jsontlist.get(0).toString();
				}
			}
			// 取出来评价内容
			String appraiseInfo = getAppraiseInfo(cid);
			if (!TextUtils.isEmpty(appraiseInfo)) {
				url += ",\"appraise\":" + appraiseInfo;
			}
			url += "},\"EC\":0,\"EM\":\"\"}";
			return url;
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取作文详情异常\"}";
		}
	}

	// 未批改作文详情(学生)
	@POST
	@Path("/unCommentDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public String unCommentCompDetail(String json) throws ParseException {
		List<Map<String, Object>> list = null;
		JSONObject jsonObject;
		String url = "{\"BM\":{\"dlist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String cid = str1.getString("cid");// 点评作文唯一标识
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			list = comStateDao.findUnCommentComPart(cid, udid);
			if (list == (null) || list.size() == 0) {
				return "{\"BM\":{},\"EC\":10025,\"EM\":\"获取作文详情空\"}";
			}
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				url += jsonArray.get(0).toString();
			}
			url += "},\"EC\":0,\"EM\":\"\"}";
			return url;
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取作文详情异常\"}";
		}
	}

	public String getAppraiseInfo(String cid) {
		JSONObject jo = new JSONObject();
		try {
			int compid = Integer.parseInt(cid);
			List<Map<String, Object>> slist = appraiseDao.queryAppraiseIdByCompId1(compid);
			List<Map<String, Object>> slist2 = appraiseDao.queryAppraiseIdByCompId2(compid);
			if (slist != null && slist.size() > 0) {
				jo.put("attit", CommUtils.judgeSqlInformation(slist.get(0).get("attit")));// 负责态度
				jo.put("ave", CommUtils.judgeSqlInformation(slist.get(0).get("ave")));// 负责态度
				jo.put("prof", CommUtils.judgeSqlInformation(slist.get(0).get("prof")));// 负责态度
				jo.put("sati", CommUtils.judgeSqlInformation(slist.get(0).get("sati")));// 负责态度
				jo.put("stuHead", CommUtils.judgeUrl(slist.get(0).get("shead")));
				jo.put("atime", CommUtils.ObjectTime2String(slist.get(0).get("TIME")));
			}
			if (slist2 != null && slist2.size() > 0) {
				jo.put("smessage", CommUtils.judgeSqlInformation(slist2.get(0).get("MESSAGE")));
				List<List<Map<String, String>>> list = CommUtils.arrangeAppraiseMessage(slist2);
				if (list != null && list.size() > 0) {
					List<Map<String, String>> list2 = list.get(0);
					for (Map<String, String> map : list2) {
						if ("tea".equals(map.get("userType"))) {
							jo.put("tmessage", CommUtils.judgeSqlInformation(map.get("message")));
						}
					}
				}
				jo.put("messageList", list);
			}
		} catch (JSONException e) {
			return "";
		}
		return jo.toString();
	}

	// 申请退款(学生)
	@POST
	@Path("/requestBack")
	@Produces(MediaType.APPLICATION_JSON)
	public String requestBack(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			int cid = str1.getInt("cid");// 点评作文唯一标识
			int orderId = str1.getInt("orderId");// 订单主键Id
			String out_trade_no = str1.getString("out_trade_no");// 订单ID
			if (SecurityUtil.isUserLogin(token)) {
				// 查询该订单是否是待点评作文
				List<Map<String, Object>> result = comStateDao.findOrderState(udid, cid, orderId, out_trade_no);
				if (result != null && result.size() > 0) {
					// 是待点评状态改为退款中
					if (2 == (int) result.get(0).get("state")) {
						// 订单状态(1待上传，2、待评点，3、已评点 4、已结算，5、退款中，6、已退款 7拒绝)
						int update = comStateDao.alterOrderState(5, udid, cid, orderId, out_trade_no);
						// ---在refund表里增加退款记录
						// 查询是否有该订单的退款记录
						logger.info("-------requestBack:udid:" + udid + "--orderId:" + (int) result.get(0).get("id"));
						List<Map<String, Object>> refundList = comStateDao.findRefundOrderInfo(udid, (int) result.get(0).get("id"));
						if (refundList != null && refundList.size() > 0) {
							// 修改该退款的订单的状态
							logger.info("-------requestBack 修改该退款的订单的状态:udid:" + udid + "--orderId:" + (int) result.get(0).get("id"));
							comStateDao.updateRefundOrderInfo(1, udid, (int) result.get(0).get("id"), (int) result.get(0).get("tid"), (String) result.get(0).get("title"));
						} else {
							// 增加该订单的退款订单
							logger.info("-------requestBack 增加该订单的退款订单:udid:" + udid + "--orderId:" + (int) result.get(0).get("id"));
							comStateDao.addRefundOrderInfo(udid, (int) result.get(0).get("id"));
						}
						if (update > 0) {
							return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";
						} else {
							return "{\"BM\":{},\"EC\":10023,\"EM\":\"修改订单状态失败\"}";
						}
					} else {
						return "{\"BM\":{},\"EC\":10022,\"EM\":\"该订单不能退款\"}";
					}
				} else {
					return "{\"BM\":{},\"EC\":10021,\"EM\":\"该订单不存在\"}";
				}
			} else {
				return Constant.PLEASE_LOGIN;
			}
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10020,\"EM\":\"申请退款异常\"}";
		}
	}

	// 取消退款(学生)
	@POST
	@Path("/cancelBack")
	@Produces(MediaType.APPLICATION_JSON)
	public String cancelBack(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			int cid = str1.getInt("cid");// 点评作文唯一标识
			int orderId = str1.getInt("orderId");// 订单主键Id
			String out_trade_no = str1.getString("out_trade_no");// 订单ID
			if (SecurityUtil.isUserLogin(token)) {
				// 查询该订单是否是待点评作文
				List<Map<String, Object>> result = comStateDao.findOrderState(udid, cid, orderId, out_trade_no);
				if (result != null && result.size() > 0) {
					// 是退款中改为待点评状态
					if (5 == (int) result.get(0).get("state")) {
						int update = comStateDao.alterOrderState(2, udid, cid, orderId, out_trade_no);
						// 修改退款表中的该订单为 用户取消退款 4
						update = comStateDao.updateRefundOrderInfo(4, udid, (int) result.get(0).get("id"), (int) result.get(0).get("tid"), (String) result.get(0).get("title"));
						if (update > 0) {
							return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";
						} else {
							return "{\"BM\":{},\"EC\":10023,\"EM\":\"修改订单状态失败\"}";
						}
					} else {
						return "{\"BM\":{},\"EC\":10022,\"EM\":\"该订单不能取消退款\"}";
					}
				} else {
					return "{\"BM\":{},\"EC\":10021,\"EM\":\"该订单不存在\"}";
				}
			} else {
				return Constant.PLEASE_LOGIN;
			}
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10020,\"EM\":\"参数错误\"}";
		}
	}

	// 修改作文回显
	@POST
	@Path("/updateCompositionShow")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateCompositionShow(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String cid = str1.getString("cid");
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			Map<String, String> map = comStateDao.updateCompositionShow(cid);
			if (map != null && map.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(map);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{},\"EC\":12151,\"EM\":\"获取作文异常\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{\"BM\":{},\"EC\":12151,\"EM\":\"获取作文异常\"}";
	}

	// 删除上传作文
	@POST
	@Path("/deleteComposition")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String deleteComposition(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String cid = str1.getString("cid");
			String udid = str1.getString("udid");
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			int i = comStateDao.deleteComposition(udid, cid);
			if (i > 0) {
				return "{\"BM\":{},\"EC\":0,\"EM\":\"删除成功\"}";
			} else {
				return "{\"BM\":{},\"EC\":12201,\"EM\":\"删除失败，老师已经开始点评！\"}";
			}
		} catch (Exception e) {
			logger.info("-------------删除作文失败" + json + "失败---------------");
			return "{\"BM\":{},\"EC\":12202,\"EM\":\"参数错误\"}";
		}
	}

	// 更换老师
	@POST
	@Path("/changeTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String changeTeacher(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);

			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String cid = str1.getString("cid");
			String newTid = str1.getString("newTid");
			String oldTid = str1.getString("oldTid");
			String oldTname = str1.getString("oldTname");
			String title = str1.getString("title");
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			try {
				int i = comStateDao.changeTeacher(cid, newTid, oldTid, udid, oldTname, title);
				if (i == 3) {
					return "{\"BM\":{},\"EC\":11271,\"EM\":\"更换老师失败，老师已经开始点评！\"}";
				} else if (i == 1) {
					if (oldTid == null || "".equals(oldTid)) {
						return "{\"BM\":{},\"EC\":0,\"EM\":\"选择老师成功！\"}";
					} else {
						return "{\"BM\":{},\"EC\":0,\"EM\":\"更换老师成功！\"}";
					}
				}
			} catch (Exception e) {

			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return "{\"BM\":{},\"EC\":11271,\"EM\":\"更换老师失败\"}";
	}

	// 作文状态(学生)
	@POST
	@Path("/comStateSDraft")
	@Produces(MediaType.APPLICATION_JSON)
	public String comStateSDraft(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String numPerPage = str1.getString("numPerPage");
			String currentPage = str1.getString("currentPage");

			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			List<Map<String, Object>> comStateS = comStateDao.findCompositionDraft(udid, numPerPage, currentPage);
			if (comStateS != null) {
				JSONArray jsonArray = JSONArray.fromObject(comStateS);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{\"clist\":\"\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
	}

}
