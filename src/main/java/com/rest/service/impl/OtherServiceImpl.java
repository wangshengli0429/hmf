package com.rest.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;
import org.eclipse.jetty.util.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

import com.entity.Student;
import com.rest.service.OtherService;
import com.rest.service.dao.OtherDao;
import com.rest.service.dao.RegistDao;
import com.util.ComPicUtils;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;
import com.util.SmsUtils2;
import com.util.wxPayUtils.MyWXPayUtils;
import com.util.zfbPayUtils.SignUtils1;
import com.util.zfbPayUtils.SignUtils2;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class OtherServiceImpl implements OtherService {

	@POST
	@Path("/oldTitle")
	@Produces(MediaType.APPLICATION_JSON)
	public String oldTitle(String json) throws ParseException {
		List title = null;
		JSONObject jsonObject;
		String url = "{\"BM\":{\"otlist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String numPerPage = str1.getString("numPerPage");
			String currentPage = str1.getString("currentPage");
			
			if (currentPage.equals("")) {
				currentPage = "1";
			}
			if (numPerPage.equals("")) {
				numPerPage = "5";
			}
			
			title = otherDao.findOldTitle(udid, currentPage, numPerPage);
			if (title == (null)) {
				return "{\"BM\":{},\"EC\":10020,\"EM\":\"数据为空\"}";
			}
			JSONArray jsonArray = new JSONArray();
			if (title != null && title.size() > 0) {
				JSONArray jsonArray1 = JSONArray.fromObject(title);
				if (jsonArray1 != null) {
					jsonArray = jsonArray1;
				}
			}
			return "{\"BM\":{\"otlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
		} catch (JSONException e) {
			// 返回异常
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"获取原标题异常\"}";
		}
		// return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";
	}

	@POST
	@Path("/typeNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public String typeNumber(String json) throws ParseException {
		List rest = null;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			rest = otherDao.typeNumber();
			if (rest.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(rest);
				return "{\"BM\":{" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
			return "{\"BM\":{},\"EC\":0,\"EM\":\"空数据\"}";
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"获取作文类型列表异常\"}";
		}
	}

	@POST
	@Path("/grade")
	@Produces(MediaType.APPLICATION_JSON)
	public String grade(String json) throws ParseException {
		// TODO Auto-generated method stub
		Map<String, Object> rest = new HashMap<String, Object>();
		JSONObject jsonObject;
		// String url="{\"BM\":{\"glist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			rest = otherDao.findGrade();
			if (rest != null) {
				JSONObject j = new JSONObject();
				j.put("BM", rest);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			} else {
				return "{\"BM\":{},\"EC\":10220,\"EM\":\"数据为空\"}";
			}
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"获取年级异常\"}";
		}
		// return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";
	}

	@POST
	@Path("/draftNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public String draftNumber(String json) throws ParseException {
		List draft = null;
		JSONObject jsonObject;
		String url = "{\"BM\":{\"dlist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			// String udid=str1.getString("udid");
			// 判断token
			if (SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			draft = otherDao.findDraftN();
			if (draft == (null)) {
				return "{\"BM\":{},\"EC\":10025,\"EM\":\"获取作文稿数为空\"}";
			}
			if (draft.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(draft);
				url += jsonArray.toString();
			}
			url += "},\"EC\":0,\"EM\":\"\"}";
			return url;
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取作文稿数异常\"}";
		}
	}
	
	/*
	 * 微信支付
	 */
	@POST
	@Path("/wxAutograph")
	@Produces(MediaType.APPLICATION_JSON)
	public String wxAutograph(String json) throws Exception {
		// 支付签名
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String oldPrice = str1.getString("price");
			String tid = str1.getString("tid");
			String rid = str1.getString("rid");
			String buyType = str1.getString("buyType");
			String compId = str1.getString("compId");
			String dpCardId = str1.getString("dpCardId");
			//生成签名前，先检查作文id是否已经生成订单
			int i = otherDao.findCompositionHasPay(compId);
			if (i == 1) {
				return "{\"BM\":{},\"EC\":10012,\"EM\":\"作文已经付款，无法进行支付！\"}";
			}
			
			//转化金额
			String price = CommUtils.wxPayDoubleMultiply100(oldPrice);
			
			Map<String, String> map = new HashMap<>();
			map.put("u", udid);
			map.put("p", price);
			map.put("t", tid);
			map.put("r", rid);
			map.put("b", buyType);
			map.put("c", compId);
			map.put("d", dpCardId);
			
			JSONArray js = JSONArray.fromObject(map);
			String attach = js.toString();
			
			if (SecurityUtil.isUserLogin(token)) {
				Map<String, String> map2 = MyWXPayUtils.getWxSign(price, attach);
				if (map2 == null) {
					return "{\"BM\":{},\"EC\":10011,\"EM\":\"获取签名错误\"}";
				}
				JSONObject j = new JSONObject();
				j.put("BM", map2);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	/*
	 * 小程序支付
	 */
	@Override
	@POST
	@Path("/wxAutograph2")
	@Produces(MediaType.APPLICATION_JSON)
	public String wxAutograph2(String json) throws Exception {
		// 支付签名
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String oldPrice = str1.getString("price");
			String tid = str1.getString("tid");
			String rid = str1.getString("rid");
			String buyType = str1.getString("buyType");
			String compId = str1.getString("compId");
			String dpCardId = str1.getString("dpCardId");
			String openId = str1.getString("openId");
			//生成签名前，先检查作文id是否已经生成订单
			int i = otherDao.findCompositionHasPay(compId);
			if (i == 1) {
				return "{\"BM\":{},\"EC\":10012,\"EM\":\"作文已经付款，无法进行支付！\"}";
			}
			//String openid = otherDao.getOpenidByUdid(udid);
			if (openId == null) {
				return "{\"BM\":{},\"EC\":10012,\"EM\":\"openid为空\"}";
			}
			//转化金额
			String price = CommUtils.wxPayDoubleMultiply100(oldPrice);
			
			Map<String, String> map = new HashMap<>();
			map.put("u", udid);
			map.put("p", price);
			map.put("t", tid);
			map.put("r", rid);
			map.put("b", buyType);
			map.put("c", compId);
			map.put("d", dpCardId);
			
			JSONArray js = JSONArray.fromObject(map);
			String attach = js.toString();
			
			if (SecurityUtil.isUserLogin(token)) {
				Map<String, String> map2 = MyWXPayUtils.getWxSign2(price, attach, openId);
				if (map2 == null) {
					return "{\"BM\":{},\"EC\":10011,\"EM\":\"获取签名错误\"}";
				}
				JSONObject j = new JSONObject();
				j.put("BM", map2);
				j.put("EC", 0);
				j.put("EM", "");
				return j.toString();
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	/**
	 * 支付宝支付
	 */
	@POST
	@Path("/autograph")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String autograph(String json) throws ParseException {
		// 支付签名
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String content = str1.getString("content");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			/*String i = otherDao.findStudentComplete_info(udid);
			if (i.equals("0")) {
				return "{\"BM\":{},\"EC\":10011,\"EM\":\"请完善资料后再邀请老师点评\"}";
			}*/
			if (SecurityUtil.isUserLogin(token)) {
				SignUtils1 s = new SignUtils1();
				return "{\"BM\":{\"sign\":\"" + s.sign(content, true) + "\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	/**
	 * 支付宝支付2
	 */
	@POST
	@Path("/autograph2")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String autograph2(String json) throws ParseException {
		// 支付签名
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String content = str1.getString("content");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			/*String i = otherDao.findStudentComplete_info(udid);
			if (i.equals("0")) {
				return "{\"BM\":{},\"EC\":10011,\"EM\":\"请完善资料后再邀请老师点评\"}";
			}*/
			if (SecurityUtil.isUserLogin(token)) {
				SignUtils2 s = new SignUtils2();
				return "{\"BM\":{\"sign\":\"" + s.sign(content, true) + "\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":10010,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	

	@POST
	@Path("/feedback")
	@Produces(MediaType.APPLICATION_JSON)
	public String feedback(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String feedback = str1.getString("feedback");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String state = str1.getString("state");
			int rest = otherDao.feedback(state, feedback, udid);
			if (rest > 0) {
				return "{\"BM\":{},\"EC\":0,\"EM\":\"添加意见反馈成功\"}";
			}
		} catch (JSONException e) {
			// 返回异常
			return "{\"BM\":{},\"EC\":10011,\"EM\":\"意见反馈异常\"}";
		}
		return "{\"BM\":{},\"EC\":10111,\"EM\":\"添加意见反馈失败\"}";
	}

	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public String search(String json) throws ParseException {
		// 搜索作文
		List sear = null;
		JSONObject jsonObject;
		String url = "{\"BM\":{\"rlist\":";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String cond = str1.getString("cond");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			// 判断token
			// int login=0;
			// for(int i=0;i<SecurityUtil.sessionList.size();i++){
			// if(SecurityUtil.sessionList.get(i).equals(token)){
			// login=1;
			// }
			// }
			// if(login !=1){
			// return Constant.PLEASE_LOGIN;
			// }
			sear = otherDao.search(cond, numPerPage, currentPage);
			if (sear == (null)) {
				return "{\"BM\":{},\"EC\":10085,\"EM\":\"搜索作文异常\"}";
			}
			if (sear.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(sear);
				url += jsonArray.toString();
			}
			url += "},\"EC\":0,\"EM\":\"\"}";
			return url;
		} catch (JSONException e) {
			// 返回异常
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10095,\"EM\":\"搜索作文稿数异常\"}";
		}

	}
	
	//评点卡种类
	@POST
	@Path("/dpCard_base")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String dpCard_base(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			List<Map<String,Object>> list = otherDao.findDpCard_base();
			JSONArray jsonArray = JSONArray.fromObject(list);
			return "{\"BM\":{\"clist\":"+jsonArray.toString()+"},\"EC\":0,\"EM\":\"\"}";
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
	
	//我的评点卡
	@POST
	@Path("/dpCardStuList")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String dpCardStuList(String json) throws ParseException {
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
			}
			if (numPerPage.equals("")) {
				numPerPage = "3";
			}
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			List<Map<String,Object>> list = otherDao.findDpCard_stu_list(udid, currentPage, numPerPage);
			JSONArray jsonArray = JSONArray.fromObject(list);
			return "{\"BM\":{\"clist\":"+jsonArray.toString()+"},\"EC\":0,\"EM\":\"\"}";
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
	}
	
	//分享作文
	@POST
	@Path("/shareComposition")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String shareComposition(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String id = str1.getString("id");
		String condition = str1.getString("condition");
		if (condition != null && !condition.equals("")) {
			String[] split = condition.split("=");
			condition = split[1];
		}
		Map<String, Object> map = otherDao.getCompostionByID(id, condition);
		if (map != null && map.size() > 0) {
			return "{\"BM\":{\"list\":"+JSON.toJSONString(map)+"},\"EC\":0,\"EM\":\"\"}";
		}else {
			return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
		}
	}
	
	//展示作文点评内容
	@POST
	@Path("/showComposition")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String showComposition(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String idIndex = str1.getString("id");
		String[] split = idIndex.split("-");
		String id = split[0];
		String index = split[1];
		Map<String, Object> map = otherDao.getCompositionImageByIdAndIndex(id, index);
		if (map != null) {
			return "{\"BM\":{\"list\":"+JSON.toJSONString(map)+"},\"EC\":0,\"EM\":\"\"}";
		}else {
			return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
		}
	}
	
	//作文列表首页
	@POST
	@Path("/compositionHome")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String compositionHome(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String condition = str1.getString("condition");
		Map<String, Object> map = otherDao.getAllCom_composition(condition);
		if (map != null) {
			return "{\"BM\":{\"map\":"+JSON.toJSONString(map)+"},\"EC\":0,\"EM\":\"\"}";
		}else {
			return "{\"BM\":{},\"EC\":10001,\"EM\":\"参数异常\"}";
		}
	}
	
	//图片修改建立热区
	@POST
	@Path("/remakePic")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String remakePic(String json) throws ParseException {
		logger.info("-----------创建热区json：" + json);
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String data = str1.getString("data");
		String sid = str1.getString("sid");
		String tid = str1.getString("tid");
		String cid = str1.getString("cid");
		String image_id = str1.getString("image_id");
		try {
			List<Map<String, String>> list = ComPicUtils.readJsonFromFile(data);
			int i = otherDao.insertComPic(list, sid, tid, cid, image_id);
			if (i > 0) {
				return "{\"BM\":{\"state\":\"success\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (IOException e) {
		}
		return "{\"BM\":{\"state\":\"fail\"},\"EC\":0,\"EM\":\"\"}";
	}
	
	//获得图片路径
	@POST
	@Path("/findCompositionImg")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String findCompositionImg(String json) throws ParseException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String data = str1.getString("data");
			String[] sts = data.split("-");
			String id = sts[0];
			String imgIndex = sts[1];
			Map<String, Object> map = otherDao.findCompositionImg(id, imgIndex);
			Object url = map.get("url");
			Object width = map.get("width");
			Object height = map.get("height");
			if (url != null) {
				return "{\"BM\":{\"url\":\""+url+"\",\"width\":"+width+",\"height\":"+height+"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (Exception e) {
		}
		return "{\"BM\":{},\"EC\":1,\"EM\":\"\"}";
	}
	
	//查看图片是否已经被点评
	@POST
	@Path("/findCompositionImgHasCom")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String findCompositionImgHasCom(String json) throws ParseException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String data = str1.getString("data");
		String[] sts = data.split("-");
		String id = sts[0];
		String imgIndex = sts[1];
		boolean b = otherDao.findCompositionImgHasCom(id, imgIndex);
		return "{\"BM\":{\"has\":\""+b+"\"},\"EC\":0,\"EM\":\"\"}";	
	}

	//h5投稿检查用户登录
	@GET
	@Path("/h5TouGaoLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public void h5TouGaoLogin(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException {
		String phone = request.getParameter("phone").toString();
		String password = request.getParameter("password").toString();
		try {
			Map<String, String> map = otherDao.h5TouGaoLogin(phone, password);
			String userId = map.get("userid");
			String state = map.get("state");//0用户名密码正确，1有这个用户但是密码错误，-1没有这个用户
			String userType = map.get("usertype");
			JSONObject object = new JSONObject();
			object.put("userId", userId);
			object.put("state", state);
			object.put("userType", userType);
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.print("callback_fn1" + "(" + object.toString() + ")");
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//h5投稿获取验证码
	@GET
	@Path("/h5getVerificationCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public void h5getVerificationCode(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException {
		String phone = request.getParameter("phone").toString();
		try {
			String code = SmsUtils2.sendMessage(phone);
			JSONObject object = new JSONObject();
			object.put("code", code);
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.print("callback_fn3" + "(" + object.toString() + ")");
			outputStream.close();
		} catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
	}
	
	//h5投稿验证手机号是否注册
	@GET
	@Path("/h5validatePhone")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public void h5validatePhone(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException {
		String phone = request.getParameter("phone").toString();
		int i = otherDao.validatePhone(phone);//0没有这个用户，1有这个用户
		JSONObject object = new JSONObject();
		object.put("state", i + "");
		try {
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.print("callback_fn2" + "(" + object.toString() + ")");
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*if (i == 0) {
			return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";	
		}else {
			return "{\"BM\":{},\"EC\":1,\"EM\":\"\"}";	
		}*/
	}
	
	//h5投稿用户注册
	@GET
	@Path("/h5register")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public void h5register(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException {
		Student stu = new Student();
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			String nickName = "手机用户_" + request.getParameter("nickname").toString();
			String phone = request.getParameter("phone").toString();
			String grade = request.getParameter("grade").toString();
			String pw = request.getParameter("password").toString();
			stu.setNickname(nickName);
			stu.setGrade(grade);
			stu.setPhone(phone);
			stu.setPassword(pw);
			stu.setUserType("h5");
			stu.setCreatedTime(dateFormat.parse(ctime));
			String udid = SecurityUtil.hash(ctime + phone);//-6B083159859DE74F8A76E154D6400934277A1B29
			stu.setUdid(udid);
			int rest = registDao.insertStudent(stu, udid);
			JSONObject object = new JSONObject();
			if (rest > 0) {
				SecurityUtil.sessionList.add(udid);
				object.put("userId", rest);
			}
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.print("callback_fn4" + "(" + object.toString() + ")");
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//h5投稿文字投稿
	@POST
	@Path("/wordAndPicContribute")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String wordAndPicContribute(List<Attachment> attachments) throws ParseException {
		boolean picResult = true;//图片上传状态
		List<String> picList = new ArrayList<>();//保存图片路径集合
		String imgPath = null;//保存图片路径
		String title = null;//作文标题
		String userId = null;//用户id
		String userType = null;//用户平台
		String grade = null;//用户年级
		String content = null;//作文正文
		InputStream in = null;
		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();
			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("title".equals(type)) {
						title = writeToString(dh.getInputStream());
					}
					if ("userId".equals(type)) {
						userId = writeToString(dh.getInputStream());
					}
					if ("userType".equals(type)) {
						userType = writeToString(dh.getInputStream());
					}
					if ("grade".equals(type)) {
						grade = writeToString(dh.getInputStream());
					}
					if ("content".equals(type)) {
						content = writeToString(dh.getInputStream());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					in = dh.getInputStream();
					//上传图片
					if (in != null) {
						imgPath = "F:\\server\\cedu-files\\files\\tougao\\" + new Date().getTime() + ".jpg";
						try {
							BufferedImage image = ImageIO.read(in);
							File file = new File(imgPath);
							boolean write = ImageIO.write(image, "png", file); 
							if (!write) {
								picResult = false;
							}else {
								picList.add(imgPath);
							}
						} catch (IOException e) {
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//更新数据库
		if (picResult) {
			Map<String, String> map = new HashMap<>();
			map.put("title", title);
			map.put("grade", grade);
			map.put("content", content);
			map.put("userId", userId);
			map.put("userType", userType);
			int i = otherDao.touGaoinsertComposition(map, picList);
			System.out.println(i);
		}
		
		return null;
	}
	
	
	/*//定时任务---检查评点卡知否过期
	@Override
	//@Scheduled(cron = "0 0 6 * * ?")
	@Scheduled(cron = "0/5 * * * * ?")
	public void myScheduled() throws ParseException {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	    int id = Integer.valueOf(runtimeMXBean.getName().split("@")[0])  
	                .intValue(); 
	    logger.info("----------当前进程id：" + id + "--------------");
		//检查红包过期
		otherDao.checkRed();
		//检查评点卡过期
		otherDao.checkDpCard(30);
		//对老师进行排序
		otherDao.sortTeacher();
	}*/
	
	@POST
	@Path("/temp")
	@Override
	public void temp() throws ParseException {
		otherDao.tempInsertCom_composition();
	}
	
	public String seaTy(String mark) {
		// 体裁类型
		if ("ti".equals(mark)) {
			return "STYLE";
			// 作文类型
		} else if ("zt".equals(mark)) {
			return "C_TYPE";
			// 详细分类(作文表)
		} else if ("xf".equals(mark)) {
			return "TYPE";
			// 详细分类(素材表)
		} else if ("xfsc".equals(mark)) {
			return "STYLE";
			// 内容类型
		} else if ("nr".equals(mark)) {
			return "CT_TYPR";
			// 素材类型
		} else if ("tisc".equals(mark)) {
			return "C_TYPE";
			// 年级
		} else if ("gnj".equals(mark)) {
			return "AGE_DETAIL";
			// 技法体裁类型
		} else if ("tcjf".equals(mark)) {
			return "TYPE";
			// 命题类型
		} else if ("mtlx".equals(mark)) {
			return "E_TYPE";
			// 考区
		} else if ("kq".equals(mark)) {
			return "C_REGION";
			// 颜色分类
		} else if ("ys".equals(mark)) {
			return "COLOR_TYPE";
			// 图片分类
		} else if ("fl".equals(mark)) {
			return "SCENE_TYPE";
			// 年级分类
		} else if ("ye".equals(mark)) {
			return "C_YEAR";
		} else {
			return "";
		}
	}

	private OtherDao otherDao;

	public OtherDao getOtherDao() {
		return otherDao;
	}

	public void setOtherDao(OtherDao otherDao) {
		this.otherDao = otherDao;
	}
	
	private RegistDao registDao;

	public RegistDao getRegistDao() {
		return registDao;
	}

	public void setRegistDao(RegistDao registDao) {
		this.registDao = registDao;
	}
	private static Logger logger = Logger.getLogger(OtherServiceImpl.class);

	private String writeToString(InputStream ins) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int i = -1;
		while ((i = ins.read(b)) != -1) {
			out.write(b, 0, i);
		}
		ins.close();
		return new String(out.toByteArray(), "UTF-8");
	}
}
