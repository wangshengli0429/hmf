package com.rest.service.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.rest.service.LoginService;
import com.rest.service.dao.LoginDao;
import com.rest.service.dao.TeacherDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;
import org.springframework.stereotype.Component;

@Component
public class LoginServiceImpl implements LoginService {

	@POST
	@Path("/slogin")
	@Produces(MediaType.APPLICATION_JSON)
	public String slogin(String json) throws ParseException {
		// 学生登录
		JSONObject jsonObject;
		Map map = new HashMap();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String phone = str1.getString("phone");
			String password = str1.getString("password");
			// String code=str1.getString("code");
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			// 验证手机号和验证码
			if (!b) {
				return "{\"BM\":{},\"EC\":10002,\"EM\":\"请正确填写手机号\"}";
			}
			// String codeValue=SecurityUtil.phoneMap.get(phone);
			// if(codeValue==null || !codeValue.equals(code)){
			// return "{\"BM\":{},\"EC\":10016,\"EM\":\"验证码不正确\"}";
			// }
			map = loginDao.findStudent(phone, password);
			if (map != null && map.size() > 0) {
				SecurityUtil.sessionList.add(map.get("token").toString());
				SecurityUtil.phoneMap.remove(phone);
				JSONObject j = new JSONObject();
				j.put("BM", map);
				j.put("EC", 0);
				j.put("EM", "");
				// JSONArray jsonArray = JSONArray.fromObject(map);
				// return "{\"BM\":"+j.toString()+",\"EC\":0,\"EM\":\"\"}";
				return j.toString();
			} else {
				int i = loginDao.findStudent(phone);
				if (i == 1) {
					return "{\"BM\":{},\"EC\":15010,\"EM\":\"帐号或密码错误，请重新填写\"}";
				}else {
					return "{\"BM\":{},\"EC\":15010,\"EM\":\"该手机号尚未注册，请注册\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10003,\"EM\":\"参数错误\"}";
		}
	}

	@POST
	@Path("/tlogin")
	@Produces(MediaType.APPLICATION_JSON)
	public String tlogin(String json) throws ParseException {
		// 老师登录
		JSONObject jsonObject;
		Map map = new HashMap();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String phone = str1.getString("phone");
			String password = str1.getString("password");
			String deviceType = str1.getString("deviceType");
			// String code=str1.getString("code");
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			// 验证手机号和验证码
			if (!b) {
				return "{\"BM\":{},\"EC\":10002,\"EM\":\"请正确填写手机号\"}";
			}
			map = loginDao.findTeacher(phone, password);
			if (map != null && map.size() > 0) {
				SecurityUtil.sessionList.add(map.get("token").toString());
				SecurityUtil.phoneMap.remove(phone);
				JSONObject j = new JSONObject();
				j.put("BM", map);
				j.put("EC", 0);
				j.put("EM", "");
				if (deviceType != null && !deviceType.equals("")) {
					teacherDao.changeDevice(phone, deviceType);
				}
				return j.toString();
			} else {
				int i = loginDao.findTeacher(phone);
				if (i == 1) {
					return "{\"BM\":{},\"EC\":15010,\"EM\":\"帐号或密码错误，请重新填写\"}";
				}else {
					return "{\"BM\":{},\"EC\":15010,\"EM\":\"该手机号尚未注册，请注册\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10003,\"EM\":\"参数错误\"}";
		}
	}
	
	@POST
	@Path("/tlogin2")
	@Produces(MediaType.APPLICATION_JSON)
	public String tlogin2(String json) throws ParseException {
		// 老师登录
		JSONObject jsonObject;
		Map map = new HashMap();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String phone = str1.getString("phone");
			String password = str1.getString("password");
			String deviceType = str1.getString("deviceType");
			// String code=str1.getString("code");
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			// 验证手机号和验证码
			if (!b) {
				return "{\"BM\":{},\"EC\":10002,\"EM\":\"请正确填写手机号\"}";
			}
			map = loginDao.findTeacher2(phone, password);
			if (map != null && map.size() > 0) {
				SecurityUtil.sessionList.add(map.get("token").toString());
				SecurityUtil.phoneMap.remove(phone);
				JSONObject j = new JSONObject();
				j.put("BM", map);
				j.put("EC", 0);
				j.put("EM", "");
				if (deviceType != null && !deviceType.equals("")) {
					teacherDao.changeDevice(phone, deviceType);
				}
				return j.toString();
			} else {
				int i = loginDao.findTeacher(phone);
				if (i == 1) {
					return "{\"BM\":{},\"EC\":15010,\"EM\":\"帐号或密码错误，请重新填写\"}";
				}else {
					return "{\"BM\":{},\"EC\":15010,\"EM\":\"该手机号尚未注册，请注册\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10003,\"EM\":\"参数错误\"}";
		}
	}

	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String logout(String json) throws ParseException {
		JSONObject jsonObject;
		int i = 0;
		String token = "";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			token = str1.getString("token");
			String loginType = str1.getString("loginType");
			i = loginDao.logout(token, loginType);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (i > 0 && !token.equals("")) {
			SecurityUtil.sessionList.remove(token);
			return "{\"BM\":{},\"EC\":0,\"EM\":\"退出登陆成功\"}";
		}else {
			return "{\"BM\":{},\"EC\":11201,\"EM\":\"退出登录失败\"}";
		}
	}
	
	//微信qq登录
	@POST
	@Path("/thirdAccountLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String thirdAccountLogin(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String openid = str1.getString("openid");
			String unionid = null;
			try {
				unionid = str1.getString("unionid");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String deviceType = str1.getString("deviceType");
			String thirdAccountType = str1.getString("thirdAccountType");
			String userType = str1.getString("userType");
			Map<String, Object> map = null;
			if ("qq".equals(thirdAccountType)) {
				if ("tea".equals(userType)) {
					map = loginDao.findTeaByOpenid_qq(openid, unionid);
				}else {
					map = loginDao.findStuByOpenid_qq(openid, unionid);
				}
			}else {
				if ("tea".equals(userType)) {
					map = loginDao.findTeaByOpenid_msg(openid, unionid);
				}else {
					map = loginDao.findStuByOpenid_msg(openid, unionid);
				}
			}
			if (map == null) {
				return "{\"BM\":{\"state\":\"1\"},\"EC\":0,\"EM\":\"\"}";
			}else {
				SecurityUtil.sessionList.add(map.get("token").toString());
				SecurityUtil.phoneMap.remove(map.get("phone"));
				JSONObject j = new JSONObject();
				Map<Object, Object> result = new HashMap<>();
				if ("tea".equals(userType)) {
					result.put("tlist", map);
					result.put("slist", "");
				}else {
					result.put("slist", map);
					result.put("tlist", "");
				}
				result.put("state", "0");
				j.put("BM", result);
				j.put("EC", 0);
				j.put("EM", "");
				if (deviceType != null && !deviceType.equals("") && "tea".equals(userType)) {
					teacherDao.changeDevice(map.get("phone").toString(), deviceType);
				}
				return j.toString();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}		
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}

	//微信qq登录,判断手机号是否存在，存在绑定
	@POST
	@Path("/thirdAccountLoginBinding")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String thirdAccountLoginBinding(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String openid = str1.getString("openid");
			String unionid = null;
			try {
				unionid = str1.getString("unionid");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String phone = str1.getString("phone");
			String deviceType = str1.getString("deviceType");
			String thirdAccountType = str1.getString("thirdAccountType");
			String thirdAccountHead = str1.getString("thirdAccountHead");
			String thirdAccountName = str1.getString("thirdAccountName");
			String userType = str1.getString("userType");
			Map<String, Object> map = null;
			if ("qq".equals(thirdAccountType)) {
				if ("tea".equals(userType)) {
					map = loginDao.tea_binding_qq_openid_phone(unionid, openid, phone, thirdAccountHead, thirdAccountName);
				}else {
					map = loginDao.stu_binding_qq_openid_phone(unionid, openid, phone, thirdAccountHead, thirdAccountName);
				}
			}else {
				if ("tea".equals(userType)) {
					map = loginDao.tea_binding_msg_openid_phone(unionid, openid, phone, thirdAccountHead, thirdAccountName);
				}else {
					map = loginDao.stu_binding_msg_openid_phone(unionid, openid, phone, thirdAccountHead, thirdAccountName);
				}
			}
			if (map == null) {
				return "{\"BM\":{\"state\":\"1\"},\"EC\":0,\"EM\":\"\"}";
			}else {
				SecurityUtil.sessionList.add(map.get("token").toString());
				JSONObject j = new JSONObject();
				Map<Object, Object> result = new HashMap<>();
				if ("tea".equals(userType)) {
					result.put("tlist", map);
					result.put("slist", "");
				}else {
					result.put("slist", map);
					result.put("tlist", "");
				}
				result.put("state", "0");
				j.put("BM", result);
				j.put("EC", 0);
				j.put("EM", "");
				if (deviceType != null && !deviceType.equals("") && "tea".equals(userType)) {
					teacherDao.changeDevice(phone, deviceType);
				}
				return j.toString();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
	
	//已登录，绑定微信qq账号
	@POST
	@Path("/thirdAccountBinding")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String thirdAccountBinding(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String openid = str1.getString("openid");
			String unionid = null;
			try {
				unionid = str1.getString("unionid");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String thirdAccountName = str1.getString("thirdAccountName");
			String thirdAccountType = str1.getString("thirdAccountType");
			String thirdAccountHead = str1.getString("thirdAccountHead");
			String userType = str1.getString("userType");
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			int i = 0;
			if ("qq".equals(thirdAccountType)) {
				if ("tea".equals(userType)) {
					i = loginDao.tea_binding_qq_name(udid, openid, thirdAccountName, thirdAccountHead, unionid);
				}else {
					i = loginDao.stu_binding_qq_name(udid, openid, thirdAccountName, thirdAccountHead, unionid);
				}
			}else {
				if ("tea".equals(userType)) {
					i = loginDao.tea_binding_msg_name(udid, openid, thirdAccountName, thirdAccountHead, unionid);
				}else {
					i = loginDao.stu_binding_msg_name(udid, openid, thirdAccountName, thirdAccountHead, unionid);
				}
			}
			if (i > 0) {
				return "{\"BM\":{\"token\":\""+token+"\",\"udid\":\""+udid+"\",\"thirdAccountName\":\""+thirdAccountName+"\"},\"EC\":0,\"EM\":\"\"}";
			}else {
				return "{\"BM\":{\"token\":\""+token+"\",\"udid\":\""+udid+"\"},\"EC\":10002,\"EM\":\"绑定失败\"}";
			}
		}catch (Exception e) {
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
	
	//已登录，解绑微信qq账号
	@POST
	@Path("/thirdAccountUnBinding")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String thirdAccountUnBinding(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String thirdAccountType = str1.getString("thirdAccountType");
			String userType = str1.getString("userType");
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			int i = 0;
			if ("qq".equals(thirdAccountType)) {
				if ("tea".equals(userType)) {
					i = loginDao.tea_unbinding_qq(udid);
				}else {
					i = loginDao.stu_unbinding_qq(udid);
				}
			}else {
				if ("tea".equals(userType)) {
					i = loginDao.tea_unbinding_msg(udid);
				}else {
					i = loginDao.stu_unbinding_msg(udid);
				}
			}
			if (i > 0) {
				return "{\"BM\":{\"token\":\""+token+"\",\"udid\":\""+udid+"\"},\"EC\":0,\"EM\":\"\"}";
			}else {
				return "{\"BM\":{\"token\":\""+token+"\",\"udid\":\""+udid+"\"},\"EC\":10002,\"EM\":\"解绑失败\"}";
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
	
	private LoginDao loginDao;

	public LoginDao getLoginDao() {
		return loginDao;
	}

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}
	
	private TeacherDao teacherDao;
	
	public TeacherDao getTeacherDao() {
		return teacherDao;
	}

	public void setTeacherDao(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

}
