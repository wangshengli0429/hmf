package com.rest.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.rest.service.RedService;
import com.rest.service.dao.RedDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class RedServiceImpl implements RedService {

	private RedDao redDao;

	public RedDao getRedDao() {
		return redDao;
	}

	public void setRedDao(RedDao redDao) {
		this.redDao = redDao;
	}

	// 红包
	@POST
	@Path("/findRedPacket")
	@Produces(MediaType.APPLICATION_JSON)
	public String findRedPacket(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String fail = str1.getString("fail");
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
				List<Map<String, Object>> redPacket = redDao.findRedPacket(udid, fail, numPerPage, currentPage);
				if (redPacket != null) {
					SecurityUtil.sessionList.add(token);
					for (int i = 0; i < redPacket.size(); i++) {
						Map map = new HashMap();
						map.put("etime", CommUtils.ObjectTime2String(redPacket.get(i).get("END_TIME")));// 失效时间
						if (redPacket.get(i).get("PACKET_IMAGE") == null) {
							map.put("image", "");
						} else {
							map.put("image", redPacket.get(i).get("PACKET_IMAGE").toString());// 红包图片
						}
						if (redPacket.get(i).get("PACKET_NAME") == null) {
							map.put("name", "");
						} else {
							map.put("name", redPacket.get(i).get("PACKET_NAME").toString());// 红包名称
						}
						if (redPacket.get(i).get("PACKET_PRICE") == null) {
							map.put("price", "");
						} else {
							map.put("price", redPacket.get(i).get("PACKET_PRICE").toString());// 价格
						}
						if (redPacket.get(i).get("ID") == null) {
							map.put("rid", "");
						} else {
							map.put("rid", redPacket.get(i).get("ID").toString());// 红包唯一标识
						}
						map.put("stime", CommUtils.ObjectTime2String(redPacket.get(i).get("START_TIME")));// 开始时间
						list.add(map);
					}
					JSONArray jsonArray = JSONArray.fromObject(list);
					return "{\"BM\":{\"redlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
				}
				return "{\"BM\":{\"redlist\":\"\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	// 红包new
	@POST
	@Path("/findRedPacket2")
	@Produces(MediaType.APPLICATION_JSON)
	public String findRedPacket2(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String fail = str1.getString("fail");
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
				List<Map<String, Object>> redPacket = redDao.findRedPacket2(udid, fail, numPerPage, currentPage);
				if (redPacket != null) {
					SecurityUtil.sessionList.add(token);
					for (int i = 0; i < redPacket.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("etime", CommUtils.ObjectTime2String(redPacket.get(i).get("END_TIME")));// 失效时间
						if (redPacket.get(i).get("PACKET_IMAGE") == null) {
							map.put("image", "");
						} else {
							map.put("image", redPacket.get(i).get("PACKET_IMAGE").toString());// 红包图片
						}
						if (redPacket.get(i).get("PACKET_NAME") == null) {
							map.put("name", "");
						} else {
							map.put("name", redPacket.get(i).get("PACKET_NAME").toString());// 红包名称
						}
						if (redPacket.get(i).get("PACKET_PRICE") == null) {
							map.put("price", "");
						} else {
							map.put("price", redPacket.get(i).get("PACKET_PRICE").toString());// 价格
						}
						if (redPacket.get(i).get("ID") == null) {
							map.put("rid", "");
						} else {
							map.put("rid", redPacket.get(i).get("ID").toString());// 红包唯一标识
						}
						map.put("stime", CommUtils.ObjectTime2String(redPacket.get(i).get("START_TIME")));// 开始时间
						list.add(map);
					}
					JSONArray jsonArray = JSONArray.fromObject(list);
					return "{\"BM\":{\"redlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
				}
				return "{\"BM\":{\"redlist\":\"\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 红包
	@POST
	@Path("/findCard")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String findCard(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String currentPage = str1.getString("currentPage");
		String numPerPage = str1.getString("numPerPage");
		String token = str1.getString("token");
		String udid = str1.getString("udid");
		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}
		List<Map<String, Object>> cardlist = redDao.findCard(udid, numPerPage, currentPage);
		for (Map<String, Object> map : cardlist) {
			
		}
		if (cardlist == null || cardlist.size() < 0 ) {
			return "{\"BM\":{\"redlist\":\"\"},\"EC\":0,\"EM\":\"\"}";
		}
		for (Map<String, Object> map : cardlist) {
			map.put("stime", CommUtils.ObjectTime2String(map.get("start_time")));// 开始时间
			map.put("etime", CommUtils.ObjectTime2String(map.get("end_time")));// 失效时间
		}
		JSONArray jsonArray = JSONArray.fromObject(cardlist);
		return "{\"BM\":{\"cardlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
	}

}
