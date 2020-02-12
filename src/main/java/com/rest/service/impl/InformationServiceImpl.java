package com.rest.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rest.service.InformationService;
import com.rest.service.dao.InformationDao;
import com.util.Constant;
import com.util.SecurityUtil;
import org.springframework.stereotype.Component;

@Component
public class InformationServiceImpl implements InformationService {

	private static Logger logger = Logger.getLogger(InformationServiceImpl.class);

	@POST
	@Path("/selectInformation")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectInformation(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = null;
		try {
			jsonObject = JSONObject.parseObject(json);
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
			// 判断token
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}

			if (state.equals("2")) {
				infDao.changeBadge(udid);
			}

			list = infDao.findInformation(udid, state, numPerPage, currentPage);
			if (list == null) {
				return "{\"BM\":{},\"EC\":10085,\"EM\":\"搜索消息异常\"}";
			}
			if (list.size() > 0) {
				System.out.println("--------jsonArray.toString() " + JSON.toJSONString(list));
				return "{\"BM\":{\"ilist\":" + JSON.toJSONString(list) + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":10003,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{},\"EC\":10990,\"EM\":\"查询异常\"}";
	}

	@POST
	@Path("/readInformation")
	@Produces(MediaType.APPLICATION_JSON)
	public String readInformation(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = null;
		try {
			jsonObject = JSONObject.parseObject(json);
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
			// 判断token
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			list = infDao.findInformation(udid, state, numPerPage, currentPage);
			if (list == null) {
				return "{\"BM\":{},\"EC\":10085,\"EM\":\"搜索消息异常\"}";
			}
			if (list.size() > 0) {
				return "{\"BM\":{\"ilist\":" + JSON.toJSONString(list) + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":10003,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{},\"EC\":10990,\"EM\":\"查询异常\"}";
	}

	@POST
	@Path("/unreadstatus")
	@Produces(MediaType.APPLICATION_JSON)
	public String unreadstatus(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject =JSONObject.parseObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String state = str1.getString("state");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			// 判断token
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			int unReadNum = infDao.unreadStatus(udid, state);
			if (unReadNum > 0) {
				return "{\"BM\":{\"unRead\":true,\"number\":" + unReadNum + "},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{\"unRead\":false,\"number\":" + unReadNum + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":10003,\"EM\":\"参数错误\"}";
		}
	}

	@POST
	@Path("/deleteInformation")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteInformation(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.parseObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			//String inflist = str1.getString("inflist");
			JSONArray jsonArray = str1.getJSONArray("inflist");
			List<String> list = new ArrayList<>();
			for (Object object : jsonArray) {
				list.add(object.toString());
			}
			String token = str1.getString("token");
			// 消息唯一标识list
			//List<String> list = (List<String>) JSONArray.toList(JSONArray.fromObject(inflist), String.class);
			if (list != null && list.size() > 0) {
				int rest = infDao.deleteInformation(list);
				if (rest > 0) {
					return "{\"BM\":{},\"EC\":0,\"EM\":\"删除成功\"}";
				}
			} else {
				return "{\"BM\":{},\"EC\":10303,\"EM\":\"inflist空\"}";
			}
			// 判断token
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":11103,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{\"isRegister\":false},\"EC\":0,\"EM\":\"请注册\"}";
	}

	private InformationDao infDao;

	public InformationDao getInfDao() {
		return infDao;
	}

	public void setInfDao(InformationDao infDao) {
		this.infDao = infDao;
	}
}
