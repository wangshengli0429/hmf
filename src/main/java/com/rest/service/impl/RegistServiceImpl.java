package com.rest.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.entity.Student;
import com.entity.Teacher;
import com.rest.service.RegistService;
import com.rest.service.dao.RegistDao;
import com.util.CommUtils;
import com.util.SecurityUtil;
import org.springframework.stereotype.Component;

@Component
public class RegistServiceImpl implements RegistService {
	private static Logger logger = Logger.getLogger(RegistServiceImpl.class);
	// 测试
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public String test(String j) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		j = URLDecoder.decode(j, "UTF-8");
		System.out.println("测试" + j);
		return "{\"BM\":{\"token\": \"76455XXX\",\"udid\": \"331XXXX45\"},\"EC\":10000,\"EM\":\"测试\"}";

	}

	@POST
	@Path("/sregister")
	@Produces(MediaType.APPLICATION_JSON)
	public String sregister(String json) throws ParseException {
		// 学生注册
		Student stu = new Student();
		JSONObject jsonObject;
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			// String code=str1.getString("code");
			String nickName = str1.getString("nickname");
			String phone = str1.getString("phone");
			String deviceId = str1.getString("deviceId");
			String grade = str1.getString("grade");
			String pw = str1.getString("password");
			String realName = null;
			String openid = null;
			String unionid = null;
			String thirdAccountName = null;
			String thirdAccountType = null;
			String thirdAccountHead = null;
			try {
				realName = str1.getString("realName");
			} catch (Exception e) {
			}
			try {
				openid = str1.getString("openid");
			} catch (Exception e) {
			}
			try {
				unionid = str1.getString("unionid");
			} catch (Exception e) {
			}
			try {
				thirdAccountName = str1.getString("thirdAccountName");
				nickName = str1.getString("thirdAccountName");
			} catch (Exception e) {
			}
			try {
				thirdAccountType = str1.getString("thirdAccountType");
			} catch (Exception e) {
			}
			try {
				thirdAccountHead = str1.getString("thirdAccountHead");
			} catch (Exception e) {
			}
			if ("qq".equals(thirdAccountType)) {
				stu.setQqOpenid(openid);
				stu.setQqUnionid(unionid);
				stu.setQqName(thirdAccountName);
			}else {
				stu.setMsgName(thirdAccountName);
				stu.setMsgOpenid(openid);
				stu.setMsgUnionid(unionid);
			}
			stu.setHeadurl(thirdAccountHead);
			stu.setName(realName);
			stu.setNickname(nickName);
			stu.setGrade(grade);
			stu.setPhone(phone);
			stu.setPassword(pw);
			// 验证手机号和验证码
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			if (!b) {
				return "{\"BM\":{},\"EC\":10052,\"EM\":\"请输入正确手机号\"}";
			}
			// String codeValue=SecurityUtil.phoneMap.get(phone);
			// if(codeValue==null ||code.equals("") || !codeValue.equals(code)){
			// return "{\"BM\":{},\"EC\":10016,\"EM\":\"验证码错误\"}";
			// }
			stu.setCreatedTime(dateFormat.parse(ctime));
			//String token = SecurityUtil.hash(phone + pw);//-3E18EACA793D0FB94349245DFC5E6594E3F37279
			stu.setCreatedTime(dateFormat.parse(ctime));
			String udid = SecurityUtil.hash(ctime + deviceId);//-6B083159859DE74F8A76E154D6400934277A1B29
			stu.setUdid(udid);
			int rest = registDao.insertStudent(stu, udid);
			if (rest > 0) {
				SecurityUtil.phoneMap.remove(phone);
				SecurityUtil.sessionList.add(udid);
				return "{\"BM\":{\"isRegister\":true,\"token\":\"" + udid + "\",\"udid\":\"" + udid + "\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{\"isRegister\":false},\"EC\":10002,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{\"isRegister\":false},\"EC\":10002,\"EM\":\"手机号或udid已经存在\"}";
	}

	@POST
	@Path("/tregister")
	@Produces(MediaType.APPLICATION_JSON)
	public String tregister(String json) throws ParseException {
		// 老师注册
		Teacher stu = new Teacher();
		JSONObject jsonObject;
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			// String code=str1.getString("code");
			String nname = str1.getString("name");
			String phone = str1.getString("phone");
			String deviceId = str1.getString("deviceId");
			// String grade=str1.getString("grade");
			String pw = str1.getString("password");
			
			//平台类型
			String deviceType = "ANDROID";
			try {
				deviceType = str1.getString("deviceType");
			} catch (Exception e) {
				logger.info("------获取deviceType为空-----");
			}
			String openid = null;
			String unionid = null;
			String thirdAccountName = null;
			String thirdAccountType = null;
			String thirdAccountHead = null;
			try {
				openid = str1.getString("openid");
			} catch (Exception e) {
			}
			try {
				unionid = str1.getString("unionid");
			} catch (Exception e) {
			}
			try {
				thirdAccountName = str1.getString("thirdAccountName");
			} catch (Exception e) {
			}
			try {
				thirdAccountType = str1.getString("thirdAccountType");
			} catch (Exception e) {
			}
			try {
				thirdAccountHead = str1.getString("thirdAccountHead");
			} catch (Exception e) {
			}
			if ("qq".equals(thirdAccountType)) {
				stu.setQqOpenid(openid);
				stu.setQqName(thirdAccountName);
				stu.setQqUnionid(unionid);
			}else {
				stu.setMsgName(thirdAccountName);
				stu.setMsgOpenid(openid);
				stu.setMsgUnionid(unionid);
			}
			stu.setHeadurl(thirdAccountHead);
			stu.setName(nname);
			// stu.setGrade(grade);
			stu.setPhone(phone);
			stu.setPassword(pw);
			// 验证手机号和验证码
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			if (!b) {
				return "{\"BM\":{},\"EC\":10002,\"EM\":\"请输入正确手机号\"}";
			}
			// String codeValue=SecurityUtil.phoneMap.get(phone);
			// if(codeValue==null ||code.equals("") || !codeValue.equals(code)){
			// return "{\"BM\":{},\"EC\":10016,\"EM\":\"验证码错误\"}";
			// }
			stu.setCreatedTime(dateFormat.parse(ctime));
			//String token = SecurityUtil.hash(phone + pw);
			stu.setCreatedTime(dateFormat.parse(ctime));
			String udid = SecurityUtil.hash(ctime + deviceId);
			int rest = registDao.insertTeacher(stu, udid, deviceType);
			stu.setUdid(udid);
			if (rest > 0) {
				SecurityUtil.phoneMap.remove(phone);
				SecurityUtil.sessionList.add(udid);
				return "{\"BM\":{\"isRegister\":true,\"token\":\"" + udid + "\",\"udid\":\"" + udid + "\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{\"isRegister\":false},\"EC\":10002,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{\"isRegister\":false},\"EC\":10002,\"EM\":\"手机号已经存在\"}";
	}
	
	@POST
	@Path("/tregister2")
	@Produces(MediaType.APPLICATION_JSON)
	public String tregister2(String json) throws ParseException {
		//老师注册2 -年级
		Teacher stu = new Teacher();
		JSONObject jsonObject;
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			// String code=str1.getString("code");
			String nname = str1.getString("name");
			String phone = str1.getString("phone");
			String deviceId = str1.getString("deviceId");
			String grade = str1.getString("grade");
			String pw = str1.getString("password");
			
			//平台类型
			String deviceType = "ANDROID";
			try {
				deviceType = str1.getString("deviceType");
			} catch (Exception e) {
				logger.info("------获取deviceType为空-----");
			}
			String openid = null;
			String unionid = null;
			String thirdAccountName = null;
			String thirdAccountType = null;
			String thirdAccountHead = null;
			try {
				openid = str1.getString("openid");
			} catch (Exception e) {
			}
			try {
				unionid = str1.getString("unionid");
			} catch (Exception e) {
			}
			try {
				thirdAccountName = str1.getString("thirdAccountName");
			} catch (Exception e) {
			}
			try {
				thirdAccountType = str1.getString("thirdAccountType");
			} catch (Exception e) {
			}
			try {
				thirdAccountHead = str1.getString("thirdAccountHead");
			} catch (Exception e) {
			}
			if ("qq".equals(thirdAccountType)) {
				stu.setQqOpenid(openid);
				stu.setQqName(thirdAccountName);
				stu.setQqUnionid(unionid);
			}else {
				stu.setMsgName(thirdAccountName);
				stu.setMsgOpenid(openid);
				stu.setMsgUnionid(unionid);
			}
			stu.setHeadurl(thirdAccountHead);
			stu.setName(nname);
			stu.setGrade(grade);
			stu.setPhone(phone);
			stu.setPassword(pw);
			// 验证手机号和验证码
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			if (!b) {
				return "{\"BM\":{},\"EC\":10002,\"EM\":\"请输入正确手机号\"}";
			}
			// String codeValue=SecurityUtil.phoneMap.get(phone);
			// if(codeValue==null ||code.equals("") || !codeValue.equals(code)){
			// return "{\"BM\":{},\"EC\":10016,\"EM\":\"验证码错误\"}";
			// }
			stu.setCreatedTime(dateFormat.parse(ctime));
			//String token = SecurityUtil.hash(phone + pw);
			stu.setCreatedTime(dateFormat.parse(ctime));
			String udid = SecurityUtil.hash(ctime + deviceId);
			int rest = registDao.insertTeacher2(stu, udid, deviceType);
			stu.setUdid(udid);
			if (rest > 0) {
				SecurityUtil.phoneMap.remove(phone);
				SecurityUtil.sessionList.add(udid);
				return "{\"BM\":{\"isRegister\":true,\"token\":\"" + udid + "\",\"udid\":\"" + udid + "\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{\"isRegister\":false},\"EC\":10002,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{\"isRegister\":false},\"EC\":10002,\"EM\":\"手机号已经存在\"}";
	}

	private RegistDao registDao;

	public RegistDao getRegistDao() {
		return registDao;
	}

	public void setRegistDao(RegistDao registDao) {
		this.registDao = registDao;
	}

}
