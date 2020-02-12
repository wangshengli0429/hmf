package com.rest.service.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.rest.service.StudentService;
import com.rest.service.dao.StudentDao;
import com.util.SecurityUtil;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class StudentServiceImpl implements StudentService {

	private static Logger logger = Logger.getLogger(StudentServiceImpl.class);

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
			if (SecurityUtil.isUserLogin(token)) {
				map = studentDao.findInformation(udid);
				if (map.size() > 0) {
					// JSONArray jsonArray = JSONArray.fromObject(map);
					// rest+=jsonArray.toString();
					JSONObject j = new JSONObject();
					j.put("BM", map);
					j.put("EC", 0);
					j.put("EM", "");
					return j.toString();
				}
			}
			// return rest+",\"EC\":0,\"EM\":\"\"}";
			return "{\"BM\":{},\"EC\":10515,\"EM\":\"获取用户信息空\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取用户信息异常\"}";
		}
	}

	@POST
	@Path("/teacherList")
	@Produces(MediaType.APPLICATION_JSON)
	public String teacherList(String json) throws ParseException {
		List<Map<String, Object>> list = null;
		JSONObject jsonObject;
		String rest = "{\"BM\":{";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String pid = str1.getString("pid");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			list = studentDao.findTeacher(pid, numPerPage, currentPage);
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				rest += "\"clist\":" + jsonArray.toString();
			}
			return rest + "},\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取用户信息异常\"}";
		}
	}

	@POST
	@Path("/homePage")
	@Produces(MediaType.APPLICATION_JSON)
	public String homePage(String json) throws ParseException {
		// 首页
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			map = studentDao.findHomePage();
			if (map != null && map.size() > 0) {
				JSONObject j = new JSONObject();
				j.put("BM", map);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			}
			return "{\"BM\":{},\"EC\":10515,\"EM\":\"获取首页信息失败\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取用户信息异常\"}";
		}
	}
	
	@POST
	@Path("/homePage2")
	@Produces(MediaType.APPLICATION_JSON)
	public String homePage2(String json) throws ParseException {
		// 首页
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			map = studentDao.findHomePage2();
			if (map != null && map.size() > 0) {
				JSONObject j = new JSONObject();
				j.put("BM", map);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			}
			return "{\"BM\":{},\"EC\":10515,\"EM\":\"获取首页信息失败\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取用户信息异常\"}";
		}
	}
	
	@POST
	@Path("/tuijian")
	@Produces(MediaType.APPLICATION_JSON)
	public String tuijian(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String grade = str1.getString("grade");
			List<Map<String, Object>> list = studentDao.getTuijianCom(grade);
			if (list != null && list.size() > 0) {
				JSONObject j = new JSONObject();
				j.put("BM", list);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			}
			return "{\"BM\":{},\"EC\":10515,\"EM\":\"获取推荐作文失败失败\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取推荐作文失败失败\"}";
		}
	}

	@POST
	@Path("/review")
	@Produces(MediaType.APPLICATION_JSON)
	public String review(String json) throws ParseException {
		// 我的评价列表
		List<Map<String, Object>> list = null;
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
				list = studentDao.findAppraise(udid);
				if (list.size() > 0) {
					JSONArray jsonArray = JSONArray.fromObject(list);
					rest += jsonArray.toString();
				}
			}
			return rest + ",\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取用户信息异常\"}";
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
				list = studentDao.findBills(udid, numPerPage, currentPage);// 账单列表
				if (list.size() > 0) {
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
	
	@POST
	@Path("/bills2")
	@Produces(MediaType.APPLICATION_JSON)
	public String bills2(String json) throws ParseException {
		// 我的账单
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
				list = studentDao.findBills2(udid, numPerPage, currentPage);// 账单列表
				if (list.size() > 0) {
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
	
	@POST
	@Path("/billDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public String billDetails(String json) throws ParseException {
		// 查看订单详情
		Map<String, Object> list = null;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String id = str1.getString("id");
			String type = str1.getString("type");
			if (SecurityUtil.isUserLogin(token)) {
				list = studentDao.billDetails(id, type);// 账单列表
				if (list != null) {
					JSONObject j = new JSONObject();
					j.put("BM", list);
					j.put("EC", 0);
					j.put("EM", "");
					return j.toString();
				}
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10191,\"EM\":\"获取订单信息异常\"}";
		}
		return "{\"BM\":{},\"EC\":10190,\"EM\":\"获取数据空\"}";
	}

	@POST
	@Path("/reAfter")
	@Produces(MediaType.APPLICATION_JSON)
	public String reAfter(String json) throws ParseException {
		// 退款售后
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> relist = null;
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
			int login = 0;
			List<String> sessionList = SecurityUtil.sessionList;
			for (int i = 0; i < sessionList.size(); i++) {
				if (sessionList.get(i).equals(token)) {
					login = 1;
				}
			}
			if (login == 1) {
				list = studentDao.findreAfter(udid, numPerPage, currentPage);
				if (list != null) {
					JSONArray jsonArray = JSONArray.fromObject(list);
					rest += "\"clist\":" + jsonArray.toString();
				}
			}
			return rest + "},\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10045,\"EM\":\"获取退款售后异常\"}";
		}
	}

	@POST
	@Path("/iosCheck")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String iosCheck() {
		boolean b = studentDao.checkIOS();
		return "{\"BM\":{\"check\":"+b+"},\"EC\":\"\",\"EM\":\"\"}";
	}
	
	private StudentDao studentDao;

	public StudentDao getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

}
