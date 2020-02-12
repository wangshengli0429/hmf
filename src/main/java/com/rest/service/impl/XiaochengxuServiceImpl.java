package com.rest.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.entity.BaseSqlResultBean;
import com.entity.Composition;
import com.rest.service.XiaochengxuService;
import com.rest.service.dao.UploadDao;
import com.rest.service.dao.XiaochengxuDao;
import com.util.Constant;
import com.util.SecurityUtil;
import com.util.SmsUtils2;
import com.util.XiaochengxuUtils;
import org.springframework.stereotype.Component;

@Component
public class XiaochengxuServiceImpl implements XiaochengxuService {
	
	private static Logger logger = Logger.getLogger(XiaochengxuServiceImpl.class);
	private XiaochengxuDao xiaochengxuDao;
	public XiaochengxuDao getXiaochengxuDao() {
		return xiaochengxuDao;
	}
	public void setXiaochengxuDao(XiaochengxuDao xiaochengxuDao) {
		this.xiaochengxuDao = xiaochengxuDao;
	}
	private UploadDao uploadDao;
	public UploadDao getUploadDao() {
		return uploadDao;
	}
	public void setUploadDao(UploadDao uploadDao) {
		this.uploadDao = uploadDao;
	}
	
	//登录
	@POST
	@Path("/wxLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String wxLogin(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String code = str1.getString("code");
		return XiaochengxuUtils.httpsRequest(code);
	}
	
	//获取手机号
	@POST
	@Path("/getPhoneNumber")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String getPhoneNumber(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String encryptedData = str1.getString("encryptedData");
		String iv = str1.getString("iv");
		String session_key = str1.getString("session_key");
		return XiaochengxuUtils.getDecryptJSON(encryptedData, session_key, iv);
	}

	//获取验证码
	@POST
	@Path("/getVerificationCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String getVerificationCode(String json,@Context HttpServletRequest servletRequest) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String phone = str1.getString("phone");
		ServletContext context = servletRequest.getSession().getServletContext();
		String result = SmsUtils2.sendMessage(phone, context);
		if (result != null && result.equals("success")) {
			return "{\"status\": \"success\"}";
		}else {
			return "{\"status\": \"fail\"}";
		}
	}

	//检验验证码
	@POST
	@Path("/validateCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String validateCode(String json,@Context HttpServletRequest servletRequest) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String code = str1.getString("code");
		String phone = str1.getString("phone");
		
		ServletContext context = servletRequest.getSession().getServletContext();
		Object object = context.getAttribute("validateCode" + phone);
		if (Objects.equals(code, object)) {
			return "{\"status\": \"success\"}";
		}
		return "{\"status\": \"fail\"}";
	}
	
	// 上传作文
	@POST
	@Path("/insertComposi")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertComposi(String json) throws ParseException {
		// json = "{\"BM\":{\"token\": \"1\",\"udid\": \"1\",\"content\":
		// \"content\",\"draft\": \"draft\",\"grade\": \"grade\",\"tid\":
		// \"1\",\"out_trade_no\":\"out_trade_no\",\"image1\":\"E:/wuhuan/b.jpg\",\"newtitle\":
		// \"newtitle\",\"oldtitle\":\"oldtitle\",\"propo\": \"propo\"}}";
		logger.info("----小程序insertComposi json:" + json);
		Composition comp = new Composition();
		JSONObject jsonObject;
		String image1 = null;
		String image2 = null;
		String image3 = null;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				String content = str1.getString("content");// 正文
				String draft = str1.getString("draft");// 第几稿
				String grade = str1.getString("grade");
				String tid = str1.getString("tid");
				String newtitle = str1.getString("newtitle");// 新标题
				String oldtitle = str1.getString("oldtitle");// 原标题
				String propo = str1.getString("propo");// 命题要求
				logger.info("----insertComposi方法，grade=" + grade);
				//年级没有上传的情况
				if ("".equals(grade)) {
					grade = uploadDao.findGradeByUdid(udid);
				}
				if (str1.has("image1") && !TextUtils.isEmpty(str1.getString("image1"))) {
					image1 = str1.getString("image1");// 作文图片
				} 
				if (str1.has("image2") && !TextUtils.isEmpty(str1.getString("image2"))) {
					image1 = str1.getString("image2");// 作文图片
				} 
				if (str1.has("image3") && !TextUtils.isEmpty(str1.getString("image3"))) {
					image1 = str1.getString("image3");// 作文图片
				} 

				comp.setUdid(udid);
				comp.setContent(content);
				comp.setDraft(draft);
				comp.setNewtitle(newtitle);
				comp.setOldtitle(oldtitle);
				comp.setPropo(propo);
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String ctime = dateFormat.format(now);
				comp.setCreateTime(dateFormat.parse(ctime));// 创建时间
				comp.setCstate(1);// 状态
				comp.setType("4");// 题材类型
				comp.setState("0");
				comp.setGrade(grade);
				comp.setImage1(image1);
				comp.setImage2(image2);
				comp.setImage3(image3);
				logger.info("----insertComposi image1:" + image1 + "image2"
						+ image2 + "image3" + image3);
				if (tid != null && !"".equals(tid)) {
					comp.setTid(tid);
				}else {
					comp.setTid(null);
				}
				BaseSqlResultBean rest = uploadDao.insertComposi(comp);
				if (rest.getSqlCode() > 0) { // 插入作文成功
					return "{\"BM\":{\"token\":\"" + token
								+ "\",\"udid\":\"" + udid
								+ "\",\"cid\":\""+rest.getSqlID()+"\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"作文上传失败\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
}
