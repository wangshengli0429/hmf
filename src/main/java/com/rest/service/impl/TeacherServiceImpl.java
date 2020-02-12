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

import org.json.JSONException;
import org.json.JSONObject;

import com.rest.service.TeacherService;
import com.rest.service.dao.TeacherDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class TeacherServiceImpl implements TeacherService {

	private TeacherDao teacherDao;

	public TeacherDao getTeacherDao() {
		return teacherDao;
	}

	public void setTeacherDao(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	// 老师简介（学生）
	@POST
	@Path("/findTintroduc")
	@Produces(MediaType.APPLICATION_JSON)
	public String findTintroduc(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String tid = str1.getString("tid");
		String udid = "";
		try {
			udid = str1.getString("udid");
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Map<String, Object>> slist = teacherDao.findTintroduc(tid, udid);
		JSONObject jo = new JSONObject();
		if (slist != null && slist.size() > 0) {
			for (int i = 0; i < slist.size(); i++) {
				
				if (slist.get(i).get("comType") == null) {
					jo.put("comType", "0");
				} else {
					jo.put("comType", slist.get(i).get("comType").toString());// 是否收藏
				}
				
				if (slist.get(i).get("ACV") == null) {
					jo.put("acv", "");
				} else {
					jo.put("acv", slist.get(i).get("ACV").toString());// 教学成就
				}
				if (slist.get(i).get("AVE") == null) {
					jo.put("ave", "");
				} else {
					jo.put("ave", slist.get(i).get("AVE").toString());// 学生评分
				}
				if (slist.get(i).get("EDU_TIME") != null) {
					jo.put("edutime", slist.get(i).get("EDU_TIME").toString());// 教龄
				}else {
					jo.put("edutime", "");// 教龄
				}
				if (slist.get(i).get("EXPER") != null) {
					jo.put("exper", slist.get(i).get("EXPER").toString());// 教龄
				}else {
					jo.put("exper", "");
				}
				if (slist.get(i).get("FEATURES") != null) {
					jo.put("features", slist.get(i).get("FEATURES").toString());
				}else {
					jo.put("features", "");
				}
				if (slist.get(i).get("GRADE") != null) {
					jo.put("grade", slist.get(i).get("GRADE").toString());
				}else {
					jo.put("grade", "");
				}
				if (slist.get(i).get("HONOR") != null) {
					jo.put("honor", slist.get(i).get("HONOR").toString());
				}else {
					jo.put("honor", "");
				}
				if (slist.get(i).get("NAME") != null) {
					jo.put("name", slist.get(i).get("NAME").toString());
				}else {
					jo.put("name", "");
				}
				if (slist.get(i).get("PRICE") == null) {
					jo.put("price", "");// 价格
				} else {
					jo.put("price", slist.get(i).get("PRICE").toString());// 价格
				}
				if (new Integer(slist.get(i).get("STATE").toString()) == 1) {// 状态
					jo.put("state", "正常");
				} else if (new Integer(slist.get(i).get("STATE").toString()) == 2) {// 状态
					jo.put("state", "休息");
				}
				jo.put("tid", tid);// 老师唯一标识
				
				jo.put("url", CommUtils.judgeUrl(slist.get(i).get("HAND_URL")));// 头像地址
			}
		}
		return "{\"BM\":{\"slist\":" + jo.toString() + "},\"EC\":0,\"EM\":\"\"}";
		// }
	}

	// 老师列表（学生）
	@POST
	@Path("/findTlist")
	@Produces(MediaType.APPLICATION_JSON)
	public String findTlist(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String grade = str1.getString("grade");// 年级(搜索条件)
			String tname = str1.getString("tname");// 老师姓名(搜索条件)
			// String token = str1.getString("token");
			// String udid = str1.getString("udid");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			// int login=0;
			// List<String> sessionList = SecurityUtil.sessionList;
			// for(int i=0;i<sessionList.size();i++){
			// if(sessionList.get(i).equals(token)){
			// login = 1;
			// }
			// }
			// if(login==1){
			// int teaid = Integer.parseInt(tid);
			List<Map<String, Object>> slist = teacherDao.findTlist(grade, tname, numPerPage, currentPage);
			// JSONObject jo = new JSONObject();
			if (slist != null && slist.size() > 0) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < slist.size(); i++) {
					if(!slist.get(i).get("delayNo").toString().equals("")){
						int delayNo = Integer.parseInt(slist.get(i).get("delayNo").toString());
						if(delayNo<2){
							list.add(slist.get(i));
						}
					}
				}
				// SecurityUtil.sessionList.add(token);
				JSONArray jsonArray = JSONArray.fromObject(list);
				return "{\"BM\":{\"slist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
			return "{\"BM\":{\"slist\":[]},\"EC\":0,\"EM\":\"\"}";
			// }
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		// return Constant.PLEASE_LOGIN;
	}

	@POST
	@Path("/information")
	@Produces(MediaType.APPLICATION_JSON)
	public String information(String json) throws ParseException {
		// 我的主页
		Map map = new HashMap();
		JSONObject jsonObject;
		String rest = "{\"BM\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
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
				map = teacherDao.findInformation(udid);
				if (map.size() > 0) {
					// JSONArray jsonArray = JSONArray.fromObject(map);
					// rest+=jsonArray.toString();
					JSONObject j = new JSONObject();
					j.put("BM", map);
					j.put("EC", 0);
					j.put("EM", "");
					return j.toString();
				}
			} else {
				return "{\"BM\":{},\"EC\":10616,\"EM\":\"没有登录\"}";
			}
			return rest + ",\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取老师信息异常\"}";
		}
	}
	
	@POST
	@Path("/information2")
	@Produces(MediaType.APPLICATION_JSON)
	public String information2(String json) throws ParseException {
		// 我的主页
		Map map = new HashMap();
		JSONObject jsonObject;
		String rest = "{\"BM\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
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
				map = teacherDao.findInformation2(udid);
				if (map.size() > 0) {
					// JSONArray jsonArray = JSONArray.fromObject(map);
					// rest+=jsonArray.toString();
					JSONObject j = new JSONObject();
					j.put("BM", map);
					j.put("EC", 0);
					j.put("EM", "");
					return j.toString();
				}
			} else {
				return "{\"BM\":{},\"EC\":10616,\"EM\":\"没有登录\"}";
			}
			return rest + ",\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取老师信息异常\"}";
		}
	}

	@POST
	@Path("/refunds")
	@Produces(MediaType.APPLICATION_JSON)
	public String refunds(String json) throws ParseException {
		// 退款作文
		List<Map<String, Object>> list = null;
		JSONObject jsonObject;
		String rest = "{\"BM\":{";
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
				list = teacherDao.findRefunds(udid, numPerPage, currentPage);
				if (list != null && list.size() > 0) {
					JSONArray jsonArray = JSONArray.fromObject(list);
					rest += "\"clist\":" + jsonArray.toString();
				}
			}
			return rest + "},\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10045,\"EM\":\"获取退款作文异常\"}";
		}
	}

	@POST
	@Path("/bills")
	@Produces(MediaType.APPLICATION_JSON)
	public String bills(String json) throws ParseException {
		// 我的账单
		List<Map<String, Object>> list = null;
		// List<Map<String, Object>> relist=null;
		JSONObject jsonObject;
		String rest = "{\"BM\":{";
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
				list = teacherDao.findBills(udid, numPerPage, currentPage);// 本月账单
				if (list != null && list.size() > 0) {
					JSONArray jsonArray = JSONArray.fromObject(list);
					rest += "\"mlist\":" + jsonArray.toString();
				}
			}
			return rest + "},\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取用户信息异常\"}";
		}
	}

	/* (non Javadoc) 
	 * @Title: sortTeacher
	 * @Description: TODO 
	 * @see com.rest.service.TeacherService#sortTeacher() 
	 */
	@Override
	@POST
	@Path("/sortTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	public void sortTeacher() {
		//teacherDao.sortTeacher();
		teacherDao.jisuan();
	}

	//点评案例列表
	@POST
	@Path("/compositionList")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String compositionList(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (!SecurityUtil.isUserLogin(token)){
				return Constant.PLEASE_LOGIN;
			}
			List<Map<String, Object>> clist = teacherDao.compositionList(currentPage, numPerPage);
			if (clist != null && clist.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(clist);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (Exception e) {
			
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}

	//未选择老师作文列表
	@POST
	@Path("/compositionListNoTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String compositionListNoTeacher(String json) throws ParseException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (!SecurityUtil.isUserLogin(token)){
				return Constant.PLEASE_LOGIN;
			}
			List<Map<String, Object>> clist = teacherDao.compositionListNoTeacher(currentPage, numPerPage);
			if (clist != null && clist.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(clist);
				return "{\"BM\":{\"clist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}else {
				return "{\"BM\":{\"clist\":\"\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
		}
	}

	//老师抢单
	@POST
	@Path("/grabComposition")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String grabComposition(String json) throws ParseException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String cid = str1.getString("cid");
			if (!SecurityUtil.isUserLogin(token)){
				return Constant.PLEASE_LOGIN;
			}
			int i = teacherDao.grabComposition(cid, udid);
			if (i == 1) {
				return "{\"BM\":{\"state\":"+true+"},\"EC\":0,\"EM\":\"抢单成功！\"}";
			}else if (i == -1) {
				return "{\"BM\":{\"state\":"+false+"},\"EC\":51101,\"EM\":\"这篇作文已有老师点评了呦，去点评其他作文吧！\"}";
			}else {
				return "{\"BM\":{\"state\":"+false+"},\"EC\":51102,\"EM\":\"您还有作文未完成点评，请完成点评后再来抢单！\"}";
			}
		} catch (Exception e) {
			
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}

	//老师取消抢单
	@POST
	@Path("/unGrabComposition")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String unGrabComposition(String json) throws ParseException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String cid = str1.getString("cid");
			if (!SecurityUtil.isUserLogin(token)){
				return Constant.PLEASE_LOGIN;
			}
			boolean b = teacherDao.unGrabComposition(udid, cid);
			return "{\"BM\":{\"state\":"+b+"},\"EC\":0,\"EM\":\"\"}";
		} catch (Exception e) {
			
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
	
	//点评状态-PC端是否点评
	@POST
	@Path("/compositionState")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String compositionState(String json) throws ParseException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String cid = str1.getString("cid");
			if (!SecurityUtil.isUserLogin(token)){
				return Constant.PLEASE_LOGIN;
			}
			boolean state = teacherDao.compositionState(udid, cid);
			return "{\"BM\":{\"state\":" + state + "},\"EC\":0,\"EM\":\"\"}";
		} catch (Exception e) {
			
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
}
