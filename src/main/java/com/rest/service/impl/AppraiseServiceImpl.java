package com.rest.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.entity.Appraise;
import com.rest.service.AppraiseService;
import com.rest.service.dao.AppraiseDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;
import com.util.SensitivewordFilter;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class AppraiseServiceImpl implements AppraiseService  {

	private static Logger logger = Logger.getLogger(AppraiseServiceImpl.class);
	private AppraiseDao appraiseDao;

	public AppraiseDao getAppraiseDao() {
		return appraiseDao;
	}

	public void setAppraiseDao(AppraiseDao appraiseDao) {
		this.appraiseDao = appraiseDao;
	}

	// 学生提交评价
	public String insertAppraise(String json) throws ParseException {
		Appraise a = new Appraise();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String attit = str1.getString("attit");// 负责态度
			String stumessage = str1.getString("message");// 学生留言
			String profes = str1.getString("prof");// 专业水平
			String satisfactoin = str1.getString("sati");// 满意度
			String teacherid = str1.getString("tid");// 老师唯一标识
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			int cid = str1.getInt("cid");
			try {
				List<String> list = SensitivewordFilter.sensitiveWord(stumessage);
				if (list.size()>0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0;i < list.size(); i++) {
						sb.append(list.get(i));
						sb.append("，");
					}
					return "{\"BM\":{},\"EC\":12121,\"EM\":\"您的评价中包含敏感词汇："+ sb.toString() +"请重新输入\"}";
				}
				
			} catch (Exception e) {
				logger.info("-----------敏感词过滤异常-------------");
			}
			
			if (SecurityUtil.isUserLogin(token)) {
				a.setUdid(udid);
				a.setAttit(Integer.parseInt(attit));// 负责态度
				a.setStumessage(stumessage);// 学生留言
				a.setProfes(Integer.parseInt(profes));// 专业水平
				a.setSatisfactoin(Integer.parseInt(satisfactoin));// 满意度
				a.setTeacherid(Integer.parseInt(teacherid));// 老师唯一标识
				a.setCompid(cid);// 评价的作文的id
				int rest = appraiseDao.insertAppraise(a);
				if (rest > 0) {
					int status = appraiseDao.alterOrderCommentStatus(cid);
					if (status > 0) {
						return "{\"BM\":{\"status\":\"success\"},\"EC\":0,\"EM\":\"\"}";
					} else {
						return "{\"BM\":{\"status\":\"success\"},\"EC\":0,\"EM\":\"修改评论状态失败\"}";
					}
				} else {
					return "{\"BM\":{},\"EC\":20014,\"EM\":\"评价失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	// 回复留言
	@POST
	@Path("/replyAppraise")
	@Produces(MediaType.APPLICATION_JSON)
	public String replyAppraise(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String message = str1.getString("message");// 学生留言
			String aid = str1.getString("aid");// 留言id
			String udid = str1.getString("udid");
			String userType = str1.getString("userType");
			String token = str1.getString("token");
			String cid = str1.getString("cid");
			try {
				List<String> list = SensitivewordFilter.sensitiveWord(message);
				if (list.size()>0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0;i < list.size(); i++) {
						sb.append(list.get(i));
						sb.append("，");
					}
					return "{\"BM\":{},\"EC\":12121,\"EM\":\"您的评价中包含敏感词汇："+ sb.toString() +"请重新输入\"}";
				}
				
			} catch (Exception e) {
				logger.info("-----------敏感词过滤异常-------------");
			}
			
			if (SecurityUtil.isUserLogin(token)) {
				int rest = appraiseDao.insertReply(aid, message, cid, userType);
				if (rest > 0) {
						return "{\"BM\":{\"status\":\"success\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":20014,\"EM\":\"留言失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	// 教师回复评价
	@Override
	@POST
	@Path("/insertTeaReply")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertAppraiseReply(String json) throws ParseException {
		Appraise a = new Appraise();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String teacherid = str1.getString("tid");// 老师唯一标识
			String udid = str1.getString("udid");
			String content = str1.getString("content");
			String token = str1.getString("token");
			int cid = str1.getInt("cid");
			if (SecurityUtil.isUserLogin(token)) {
				a.setUdid(udid);
				a.setTeacherid(Integer.parseInt(teacherid));// 老师唯一标识
				a.setCompid(cid);// 评价的作文的id
				a.setTeachermessage(content);//教师评价的内容
				int rest = appraiseDao.insertReply(a);
				if (rest > 0) {
					return "{\"BM\":{\"status\":\"success\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":20014,\"EM\":\"回复失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	// 删除留言
	@POST
	@Path("/delAppraise")
	@Produces(MediaType.APPLICATION_JSON)
	public String delAppraise(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String aid = str1.getString("aid");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String userType = str1.getString("userType");
			if (SecurityUtil.isUserLogin(token)) {
				int rest = appraiseDao.delAppraise(aid, userType);
				if (rest == 1) {
					return "{\"BM\":{\"status\":\"success\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == 2){
					return "{\"BM\":{},\"EC\":20015,\"EM\":\"留言已回复无法删除\"}";
				}else {
					return "{\"BM\":{},\"EC\":20014,\"EM\":\"删除留言失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 学生评价列表
	@POST
	@Path("/findAppraises")
	@Produces(MediaType.APPLICATION_JSON)
	public String findAppraises(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> slist = appraiseDao.findAppraises(udid, numPerPage, currentPage);
				if (slist != null) {
					SecurityUtil.sessionList.add(token);
					JSONArray jsonArray = JSONArray.fromObject(slist);
					return "{\"BM\":{\"alist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
				}
				return "{\"BM\":{\"alist\":\"\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 查看评价
	@POST
	@Path("/selectAppraise")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectAppraise(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String pid = str1.getString("pid");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				int compid = Integer.parseInt(pid);
				List<Map<String, Object>> slist = appraiseDao.selectAppraise(compid, udid);
				JSONObject jo = new JSONObject();
				String atime = "";
				String ctime = "";
				if (slist != null) {
					SecurityUtil.sessionList.add(token);
					for (int i = 0; i < slist.size(); i++) {
						if (slist.get(i).get("attit") == null) {
							jo.put("attit", "");
						} else {
							jo.put("attit", slist.get(i).get("attit").toString());// 负责态度
						}
						if (slist.get(i).get("ave") == null) {
							jo.put("ave", "");
						} else {
							jo.put("ave", slist.get(i).get("ave").toString());// 平均分
						}
						if (slist.get(i).get("draft") == null) {
							jo.put("draft", "");
						} else {
							jo.put("draft", slist.get(i).get("draft").toString());// 第几稿
						}
						if (slist.get(i).get("name") == null) {
							jo.put("name", "");
						} else {
							jo.put("name", slist.get(i).get("name").toString());// 老师名称
						}
						if (slist.get(i).get("profes") == null) {
							jo.put("prof", "");
						} else {
							jo.put("prof", slist.get(i).get("profes").toString());// 专业水平
						}
						if (slist.get(i).get("atime") == null) {
							jo.put("atime", "");
						}else {
							Date date = (Date) slist.get(i).get("atime");
							atime = df.format(date);
						}
						if (slist.get(i).get("ctime") == null) {
							jo.put("ctime", "");
						}else {
							Date date1 = (Date) slist.get(i).get("ctime");
							ctime = df.format(date1);
						}
						if (slist.get(i).get("sat") == null) {
							jo.put("sati", "");
						} else
							jo.put("sati", slist.get(i).get("sat").toString());// 满意程度
						if (slist.get(i).get("smessage") == null) {
							jo.put("smessage", "");
						} else {
							jo.put("smessage", slist.get(i).get("smessage").toString());// 学生留言
						}
						if (slist.get(i).get("title") == null) {
							jo.put("title", "");
						} else {
							jo.put("title", slist.get(i).get("title").toString());// 作文标题
						}
						if (slist.get(i).get("tmessage") == null) {
							jo.put("tmessage", "");
						} else {
							jo.put("tmessage", slist.get(i).get("tmessage").toString());// 老师回复
						}
						jo.put("url", CommUtils.judgeUrl(slist.get(i).get("url")));
						jo.put("shead", CommUtils.judgeUrl(slist.get(i).get("shead")));
						jo.put("atime", atime);// 学生评价时间
						jo.put("ctime", ctime);// 作文评价时间
					}
					// System.out.println(jo.toString();
				}
				return "{\"BM\":{\"slist\":" + jo.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
			return "{\"BM\":{\"slist\":\"\"},\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
	}
	// 查看评价2
	@POST
	@Override
	@Path("/selectAppraise2")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectAppraise2(String json) throws ParseException {
		JSONObject jsonObject;
		JSONObject jo = new JSONObject();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String cid = str1.getString("cid");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				int compid = Integer.parseInt(cid);
				List<Map<String, Object>> slist = appraiseDao.queryAppraiseIdByCompId1(compid);
				List<Map<String, Object>> slist2 = appraiseDao.queryAppraiseIdByCompId2(compid);
				if (slist != null && slist.size() > 0) {
					jo.put("attit", CommUtils.judgeSqlInformation(slist.get(0).get("attit")));
					jo.put("ave", CommUtils.judgeSqlInformation(slist.get(0).get("ave")));
					jo.put("prof", CommUtils.judgeSqlInformation(slist.get(0).get("prof")));
					jo.put("sati", CommUtils.judgeSqlInformation(slist.get(0).get("sati")));
					jo.put("attit", CommUtils.judgeSqlInformation(slist.get(0).get("attit")));
					jo.put("draft", CommUtils.judgeSqlInformation(slist.get(0).get("draft")));
					jo.put("title", CommUtils.judgeSqlInformation(slist.get(0).get("title")));
					jo.put("attit", CommUtils.judgeSqlInformation(slist.get(0).get("attit")));
					jo.put("sname", CommUtils.judgeSqlInformation(slist.get(0).get("sname")));
					jo.put("tname", CommUtils.judgeSqlInformation(slist.get(0).get("tname")));
					jo.put("shead", CommUtils.judgeUrl(slist.get(0).get("shead")));
					jo.put("thead", CommUtils.judgeUrl(slist.get(0).get("thead")));
					jo.put("atime", CommUtils.ObjectTime2String(slist.get(0).get("TIME")));
				}
				if (slist2 != null && slist2.size() > 0) {
					List<List<Map<String, String>>> list = CommUtils.arrangeAppraiseMessage(slist2);
					jo.put("messageList", list);
				}
				JSONObject j = new JSONObject();
				j.put("BM", jo);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			}else {
				return Constant.PLEASE_LOGIN;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
	}

	// 点评详情(老师)
	@POST
	@Path("/selectComDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectComDetails(String json) throws ParseException {
		// json = "{\"BM\":{\"token\": \"76455XXX\",\"udid\": \"1\",\"cid\": \"1\"}}";
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String cid = str1.getString("cid");// 点评作文唯一标识
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			int login = 0;
			List<String> sessionList = SecurityUtil.sessionList;
			for (int i = 0; i < sessionList.size(); i++) {
				if (sessionList.get(i).equals(token)) {
					login = 1;
				}
			}
			if (login == 1) {
				int compid = Integer.parseInt(cid);
				Map<String, Object> list = appraiseDao.selectComDetails(compid, udid);
				if (list != null) {
					SecurityUtil.sessionList.add(token);
					JSONObject jo = new JSONObject();
					if (list.get("ask") == null) {
						jo.put("ask", "");
					} else
						jo.put("ask", list.get("ask"));// 要求
					if (list.get("image1") == null) {
						jo.put("image1", "");
					} else
						jo.put("image1", list.get("image1"));// 图片1
					if (list.get("image2") == null) {
						jo.put("image2", "");
					} else
						jo.put("image2", list.get("image2"));// 图片2
					if (list.get("image3") == null) {
						jo.put("image3", "");
					} else
						jo.put("image3", list.get("image3"));// 图片3
					if (list.get("clist") == null) {
						jo.put("clist", "");
					} else
						jo.put("clist", list.get("clist"));// 作文clist
					if (list.get("cslist") == null) {
						jo.put("cslist", "");
					} else
						jo.put("cslist", list.get("cslist"));// 作文评分标准cslist
					if (list.get("slist") == null) {
						jo.put("slist", "");
					} else
						jo.put("slist", list.get("slist"));// 学生评价slist
					jo.put("sulist", list.get("sulist"));// 总评sulist
					jo.put("tlist", list.get("tlist"));// 老师tlist
					return "{\"BM\":" + jo.toString() + ",\"EC\":0,\"EM\":\"\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

}
