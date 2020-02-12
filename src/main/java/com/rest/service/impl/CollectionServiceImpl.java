package com.rest.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.entity.Collection;
import com.rest.service.CollectionService;
import com.rest.service.dao.CollectionDao;
import com.util.Constant;
import com.util.SecurityUtil;
import org.springframework.stereotype.Component;


@Component
public class CollectionServiceImpl implements CollectionService {

	private CollectionDao collectionDao;

	public CollectionDao getCollectionDao() {
		return collectionDao;
	}

	public void setCollectionDao(CollectionDao collectionDao) {
		this.collectionDao = collectionDao;
	}

	// 加入收藏
	@POST
	@Path("/icollection")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertCollection(String json) throws ParseException {
		Collection c = new Collection();
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.parseObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String compid = str1.getString("compid");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String dist = str1.getString("dist");
			c.setCompid(Integer.parseInt(compid));
			c.setUdid(udid);
			c.setDist(dist);
			if (SecurityUtil.isUserLogin(token)) {
				int rest = collectionDao.insertCollection(c, token);
				if (rest > 0) {
					SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == -2) {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"已收藏\"}";
				} else if (rest == -1) {
					return "{\"BM\":{},\"EC\":20006,\"EM\":\"收藏失败\"}";
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	// 加入收藏老师
	@POST
	@Path("/icollectionTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String icollectionTeacher(String json) throws ParseException {
		Collection c = new Collection();
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.parseObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String tid = str1.getString("tid");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			c.setCompid(Integer.parseInt(tid));
			c.setUdid(udid);
			if (SecurityUtil.isUserLogin(token)) {
				int rest = collectionDao.insertCollectionTeacher(c, token);
				if (rest > 0) {
					SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == -2) {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"已收藏\"}";
				} else if (rest == -1) {
					return "{\"BM\":{},\"EC\":20006,\"EM\":\"收藏失败\"}";
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 取消收藏
	@POST
	@Path("/dcollection")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteCollection(String json) throws ParseException {
		// json = "{\"BM\":{udid:\"2\",sign:2}}";
		Collection c = new Collection();
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.parseObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String sign = str1.getString("sign");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			c.setCompid(Integer.parseInt(sign));
			c.setUdid(udid);
			if (SecurityUtil.isUserLogin(token)) {
				int rest = collectionDao.deleteCollection(c, token);
				if (rest > 0) {
					SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == 0) {
					return "{\"BM\":{},\"EC\":20007,\"EM\":\"未收藏\"}";
				} else if (rest == -1) {
					return "{\"BM\":{},\"EC\":20008,\"EM\":\"取消收藏失败\"}";
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 收藏作文列表
	@POST
	@Path("/findCollection")
	@Produces(MediaType.APPLICATION_JSON)
	public String findCollection(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.parseObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			String state = "1";
			try {
				state = str1.getString("state");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			if ("2".equals(state)) {
				List<Map<String, Object>> slist = collectionDao.findTlist(udid, numPerPage, currentPage);
				// JSONObject jo = new JSONObject();
				if (slist != null && slist.size() > 0) {
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"slist\":" + JSON.toJSONString(slist) + "},\"EC\":0,\"EM\":\"\"}";
				}else {
					return "{\"BM\":{\"slist\":\"\"},\"EC\":0,\"EM\":\"\"}";
				}
			}else {
				List<Map<String, Object>> slist = collectionDao.findCollection(udid, numPerPage, currentPage);
				// if (slist.size() > 0) {
				String count = collectionDao.countCollection(udid);
				return "{\"BM\":{\"count\":" + count + ",\"slist\":" + JSON.toJSONString(slist) + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
	}

	// 编辑收藏
	@POST
	@Path("/editCollection")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteCollectionArray(String json) throws ParseException {
		JSONObject jsonObject = JSONObject.parseObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String token = str1.getString("token");
		String udid = str1.getString("udid");
		List<String> list = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();
		String type = "";
		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}
		try {
			jsonArray = str1.getJSONArray("signs");
			for (Object object : jsonArray) {
				list.add(object.toString());
			}
			if (list.size() > 0) {
				type = "com";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			jsonArray = str1.getJSONArray("tids");
			for (Object object : jsonArray) {
				list.add(object.toString());
			}
			if (list.size() > 0) {
				type = "tea";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int rest = collectionDao.deleteCollections(list, udid, type);
		if (rest > 0) {
			return "{\"BM\":{\"status\":\"" + true + "\"},\"EC\":0,\"EM\":\"\"}";
		}
		return "{\"BM\":{},\"EC\":20008,\"EM\":\"取消收藏失败\"}";
	}

	public String getSqlArrayByJSONArray(com.alibaba.fastjson.JSONArray jsonArray) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		for (int i = 0; i < jsonArray.size(); i++) {
			if ((i + 1) == jsonArray.size()) {
				buffer.append(jsonArray.getIntValue(i));
			} else {
				buffer.append(jsonArray.getIntValue(i) + ",");
			}
		}
		buffer.append(")");
		return buffer.toString();

	}

	@POST
	@Path("/dcollectionTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String dcollectionTeacher(String json) throws ParseException {
		JSONObject jsonObject = JSONObject.parseObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String token = str1.getString("token");
		String udid = str1.getString("udid");
		String sign = str1.getString("sign");
		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}
		int i = collectionDao.deleteCollectionTeacher(sign, udid);
		if (i > 0) {
			return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
		}
		return "{\"BM\":{},\"EC\":20008,\"EM\":\"取消收藏失败\"}";
	}

}
