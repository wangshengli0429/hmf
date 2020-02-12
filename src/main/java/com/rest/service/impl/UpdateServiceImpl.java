package com.rest.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.entity.Student;
import com.entity.Teacher;
import com.rest.service.UpdateService;
import com.rest.service.dao.UpdateDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;
import org.springframework.stereotype.Component;

@Component
public class UpdateServiceImpl implements UpdateService {

	private UpdateDao updateDao;

	public UpdateDao getUpdateDao() {
		return updateDao;
	}

	public void setUpdateDao(UpdateDao updateDao) {
		this.updateDao = updateDao;
	}

	@GET
	@Path("/aaa")
	@Produces(MediaType.APPLICATION_JSON)
	public String aaa(String j) throws UnsupportedEncodingException {
		j = URLDecoder.decode(j, "UTF-8");
		System.out.println("updateService测试响应" + j);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("acv", "aaa");// 教学成就
		map.put("city", "bbb");// 省市
		map.put("edutime", 2);// 教龄
		map.put("exper", "有");// 中高考阅卷经验
		map.put("features", "ccc");// 教学特色
		map.put("honor", "ddd");// 个人荣誉
		map.put("name", "eee");// 姓名
		map.put("school", "fff");// 学校
		map.put("sex", "女");// 性别
		map.put("url", "ggg");// 头像图片
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(map);
		JSONObject jo = new JSONObject();
		if (list.size() > 0) { // comStateS.equals("")
			for (int i = 0; i < list.size(); i++) {
				try {
					jo.put("acv", list.get(i).get("acv"));// 教学成就
					jo.put("city", list.get(i).get("city"));// 省市
					jo.put("edutime", list.get(i).get("edutime"));// 教龄
					jo.put("exper", list.get(i).get("exper"));// 中高考阅卷经验
					jo.put("features", list.get(i).get("features"));// 教学特色
					jo.put("honor", list.get(i).get("honor"));// 个人荣誉
					jo.put("name", list.get(i).get("name"));// 姓名
					jo.put("school", list.get(i).get("school"));// 学校
					jo.put("sex", list.get(i).get("sex"));// 性别
					jo.put("url", list.get(i).get("url"));// 头像图片
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				}
			}
			System.out.println(jo.toString());
		}
		return "{\"BM\":" + jo.toString() + ",\"EC\":0,\"EM\":\"\"}";
	}

	// 修改学生密码
	@POST
	@Path("/updateStudentPw")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateStudentPw(String json) throws ParseException {
		// json = "{\"BM\":{udid:1,oldpw:\"aaa\",newpw:\"bbb\"}}";
		Student stu = new Student();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String oldpw = str1.getString("oldpw");
			String pw = str1.getString("newpw");
			if (oldpw == pw) {
				return "{\"BM\":{\"isRegister\":false},\"EC\":20002,\"EM\":\"新密码和旧密码不能一致\"}";
			}
			stu.setPassword(pw);
			stu.setUdid(udid);
			if (SecurityUtil.isUserLogin(token)) {
				// 查询旧密码是否一致
				int s = updateDao.selectStudentPw(udid, oldpw);
				if (s == 1 || s == 2) {
					// 修改密码
					int rest = updateDao.updateStudentPw(stu, udid);
					if (rest > 0) {
						SecurityUtil.sessionList.add(token);
						return "{\"BM\":{\"isRegister\":true,\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"修改成功，请重新登录\"}";
					}
				} else {
					return "{\"BM\":{\"isRegister\":false},\"EC\":20002,\"EM\":\"请输入正确的密码\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 忘记密码(学生)
	@POST
	@Path("/updateForgotSpw")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateForgotSpw(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String password = str1.getString("password");
			String phone = str1.getString("phone");

			// 验证手机号
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			if (!b) {
				return "{\"BM\":{},\"EC\":20004,\"EM\":\"请正确填写手机号\"}";
			}
			// 验证手机号是否存在
			int s = updateDao.selectSphone(phone);
			if (s == 1) {
				// 修改密码
				int rest = updateDao.updateForgotSpw(password, phone);
				if (rest > 0) {
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";
				}
			} else {
				return "{\"BM\":{},\"EC\":20012,\"EM\":\"手机号不存在\"}";
			}

		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{\"isRegister\":false},\"EC\":20013,\"EM\":\"找回密码失败\"}";
	}

	// 修改老师密码
	@POST
	@Path("/updateTeacherPw")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTeacherPw(String json) throws ParseException {
		Teacher tea = new Teacher();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String udid = str1.getString("udid");
			String token = str1.getString("token");
			String oldpw = str1.getString("oldpw");
			String pw = str1.getString("newpw");
			if (oldpw.equals(pw)) {
				return "{\"BM\":{\"isRegister\":false},\"EC\":20002,\"EM\":\"新密码和旧密码不能一致\"}";
			}
			tea.setPassword(pw);
			tea.setUdid(udid);
			if (SecurityUtil.isUserLogin(token)) {
				// 查询旧密码是否一致
				int s = updateDao.selectTeacherPw(udid, oldpw);
				if (s == 1 || s == 2) {
					// 修改密码
					int rest = updateDao.updateTeacherPw(tea, udid);
					if (rest > 0) {
						SecurityUtil.sessionList.add(token);
						return "{\"BM\":{\"isRegister\":true,\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"修改成功，请重新登录\"}";
					}
				} else {
					return "{\"BM\":{\"isRegister\":false},\"EC\":20002,\"EM\":\"请输入正确的密码\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 忘记密码(老师)
	@POST
	@Path("/updateForgotTpw")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateForgotTpw(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String password = str1.getString("password");
			String phone = str1.getString("phone");

			// 验证手机号
			boolean b = CommUtils.isChinaPhoneLegal(phone);
			if (!b) {
				return "{\"BM\":{\"isRegister\":false},\"EC\":20004,\"EM\":\"请正确填写手机号\"}";
			}
			// 验证手机号是否存在
			int s = updateDao.selectTphone(phone);
			if (s == 1) {
				// //验证验证码
				// String codeValue=SecurityUtil.phoneMap.get(phone);
				// if(codeValue==null || !codeValue.equals(code)){
				// return "{\"BM\":{\"isRegister\":false},\"EC\":20016,\"EM\":\"验证码错误\"}";
				// }
				// 修改密码
				int rest = updateDao.updateForgotTpw(password, phone);
				if (rest > 0) {
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"isRegister\":true},\"EC\":0,\"EM\":\"\"}";
				}
			} else {
				return "{\"BM\":{\"isRegister\":false},\"EC\":20012,\"EM\":\"手机号不存在\"}";
			}

		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{\"isRegister\":false},\"EC\":20013,\"EM\":\"找回密码失败\"}";
	}

	// 修改学生手机号
	@POST
	@Path("/updateSphone")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateStudentPhone(String json) throws ParseException {
		// Student stu = new Student();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String pw = str1.getString("pw");
			//String oldphone = str1.getString("oldphone");
			String phone = str1.getString("newphone");
			String code = str1.getString("code");
			int login = 0;
			List<String> sessionList = SecurityUtil.sessionList;
			for (int i = 0; i < sessionList.size(); i++) {
				if (sessionList.get(i).equals(token)) {
					login = 1;
				}
			}
			if (login == 1) {
				/*if (oldphone == phone) {
					return "{\"BM\":{},\"EC\":20024,\"EM\":\"新手机号码一致\"}";
				}*/
				boolean b = CommUtils.isChinaPhoneLegal(phone);
				// 验证手机号和验证码
				if (!b) {
					return "{\"BM\":{},\"EC\":20004,\"EM\":\"请正确填写手机号码\"}";
				}
				String codeValue = SecurityUtil.phoneMap.get(phone);
				// if(codeValue==null || !codeValue.equals(code)){
				// return "{\"BM\":{},\"EC\":20016,\"EM\":\"验证码错误\"}";
				// }
				int rest = updateDao.findStudent(pw, phone, udid);

				if (rest > 0) {
					// SecurityUtil.phoneMap.remove(phone);
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == -4) {
					return "{\"BM\":{},\"EC\":20014,\"EM\":\"密码错误\"}";
				} else if (rest == -2) {
					return "{\"BM\":{},\"EC\":20015,\"EM\":\"手机号码已经注册\"}";
				} else {
					return "{\"BM\":{},\"EC\":20016,\"EM\":\"手机号码修改失败\"}";
				}
			}else{
				return Constant.PLEASE_LOGIN;
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
	}

	// 修改老师手机号
	@POST
	@Path("/updateTphone")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTeacherPhone(String json) throws ParseException {
		// Teacher tea = new Teacher();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String pw = str1.getString("pw");
			// String oldphone = str1.getString("oldphone");
			String phone = str1.getString("newphone");
			// String code = str1.getString("code");
			if (SecurityUtil.isUserLogin(token)) {
				// if(oldphone == phone){
				// return "{\"BM\":{},\"EC\":20004,\"EM\":\"手机号一致\"}";
				// }
				boolean b = CommUtils.isChinaPhoneLegal(phone);
				// 验证手机号和验证码
				if (!b) {
					return "{\"BM\":{},\"EC\":20004,\"EM\":\"请正确填写手机号\"}";
				}
				String codeValue = SecurityUtil.phoneMap.get(phone);
				// if(codeValue==null || !codeValue.equals(code)){
				// return "{\"BM\":{},\"EC\":20016,\"EM\":\"验证码错误\"}";
				// }
				int rest = updateDao.findTeacher(pw, phone, udid);

				if (rest > 0) {
					// SecurityUtil.phoneMap.remove(phone);
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
				}else if (rest == -2) {
					return "{\"BM\":{},\"EC\":20014,\"EM\":\"密码错误\"}";
				} else if (rest == -3) {
					return "{\"BM\":{},\"EC\":20015,\"EM\":\"手机号码已经注册\"}";
				} else {
					return "{\"BM\":{},\"EC\":20016,\"EM\":\"手机号码修改失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 获取基本信息（学生）
	@POST
	@Path("/getStudent")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStudent(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> list = updateDao.getStudent(udid);
				List<Map<String, Object>> list2 = updateDao.getStudentCard(udid);
				List<Map<String, Object>> list3 = updateDao.getDpayment(udid);//获取学生待支付数量
				List<Map<String, Object>> list4 = updateDao.getDcomment(udid);//获取学生待点评数量
				JSONObject jo = new JSONObject();
				if (list != null && list.size() > 0) {
					jo.put("city", list.get(0).get("AREA"));// 区域
					jo.put("grade", list.get(0).get("GRADE"));// 年级
					jo.put("intr", list.get(0).get("INTRODUCE"));// 个人简介
					jo.put("name", list.get(0).get("NAME"));// 姓名
					jo.put("nickname", list.get(0).get("NICKNAME"));// 昵称
					jo.put("school", list.get(0).get("SCHOOL"));// 学校
					jo.put("sex", list.get(0).get("SEX"));// 性别
					
					jo.put("head", CommUtils.judgeUrl(list.get(0).get("HEAD")));// 头像图片
					//有误密码处理
					if (list.get(0).get("PASSWORD") == null || "".equals(list.get(0).get("PASSWORD").toString())) {
						jo.put("hasPassWord", "1");
					}else {
						jo.put("hasPassWord", "0");
					}
					
					jo.put("token", token);
					if (list.get(0).get("COMPLETE_INFO") == (null)) {
						jo.put("completeInfo", "0");
					} else {
						jo.put("completeInfo", list.get(0).get("COMPLETE_INFO") + "");
					}
					jo.put("udid", list.get(0).get("UDID"));
					//微信qq账号
					if (list.get(0).get("MSG_NAME") != null) {
						jo.put("msg_name", list.get(0).get("MSG_NAME").toString());
					}else {
						jo.put("msg_name", "");
					}
					if (list.get(0).get("QQ_NAME") != null) {
						jo.put("qq_name", list.get(0).get("QQ_NAME").toString());
					}else {
						jo.put("qq_name", "");
					}
				}
				if (list2 != null && list2.size() > 0 && list2.get(0).get("time") != null && list2.get(0).get("count") != null) {
					jo.put("hasCard", "0");
					jo.put("c_count", list2.get(0).get("count"));
					jo.put("c_time", CommUtils.ObjectTime2String(list2.get(0).get("time")));
				}else {
					jo.put("hasCard", "1");
					jo.put("c_count", "0");
					jo.put("c_time", "无");
				}
				jo.put("dpayment", list3.get(0).get("number"));
				jo.put("dcomment", list4.get(0).get("number"));
				return "{\"BM\":" + jo.toString() + ",\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 修改基本信息（学生）
	@POST
	@Path("/updateStudent")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateStudent(String json) throws ParseException, IOException {
		Student stu = new Student();
		JSONObject jsonObject;
		String headurl = "";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String city = str1.getString("city");// 区域
			String grade = str1.getString("grade");// 年级
			String intr = str1.getString("intr");// 个人简介
			String name = str1.getString("name");// 姓名
			String nickname = str1.getString("nickname");// 昵称
			String school = str1.getString("school");// 学校
			String sex = str1.getString("sex");// 性别
			String extension = str1.getString("extension");// 扩展
			if (str1.getString("url") != ("")) {
				headurl = str1.getString("url");// 头像地址
			} else {
				headurl = "";
			}
			if (SecurityUtil.isUserLogin(token)) {
				stu.setUdid(udid);
				if (!TextUtils.isEmpty(city)) {
					stu.setArea(city);// 区域
				}
				if (!TextUtils.isEmpty(grade)) {
					stu.setGrade(grade);// 年级
				}
				if (!TextUtils.isEmpty(intr)) {
					stu.setIntroduce(intr);// 个人简介
				}
				if (!TextUtils.isEmpty(name)) {
					stu.setName(name);// 姓名
				}
				if (!TextUtils.isEmpty(nickname)) {
					stu.setNickname(nickname);// 昵称
				}
				if (!TextUtils.isEmpty(school)) {
					stu.setSchool(school);// 学校
				}
				if (!TextUtils.isEmpty(sex)) {
					stu.setSex(sex);// 性别
				}
				if (!TextUtils.isEmpty(headurl)) {
					stu.setHeadurl(headurl);
				}
				stu.setExtension(extension);
				int rest = updateDao.updateStudent(stu);
				if (rest > 0) {
					SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == -2) {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"修改失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{},\"EC\":0,\"EM\":\"\"}";
	}

	// 获取基本信息（老师）
	@POST
	@Path("/getTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTeacher(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> list = updateDao.getTeacher(udid);
				JSONObject jo = new JSONObject();
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(0).get("UDID") == (null)) {
							jo.put("token", "");
						} else {
							jo.put("token", list.get(0).get("UDID").toString());
						}
						jo.put("head", CommUtils.judgeUrl(list.get(0).get("HAND_URL")));// 头像图片
						jo.put("url", CommUtils.judgeUrl(list.get(0).get("HAND_URL")));// 头像图片
						//有无密码处理
						if (list.get(0).get("PASSWORD") == null || "".equals(list.get(0).get("PASSWORD").toString())) {
							jo.put("hasPassWord", "1");
						}else {
							jo.put("hasPassWord", "0");
						}
						
						if (list.get(0).get("NAME") == (null)) {
							jo.put("nickname", "");
						} else {
							jo.put("nickname", list.get(0).get("NAME").toString());
						}
						jo.put("udid", list.get(0).get("UDID"));
						if (list.get(i).get("ACV") == (null)) {
							jo.put("acv", "");
						} else {
							jo.put("acv", list.get(i).get("ACV"));// 教学成就
						}
						if (list.get(i).get("CITY") == (null)) {
							jo.put("city", "");
						} else {
							jo.put("city", list.get(i).get("CITY"));// 省市
						}
						if (list.get(i).get("EDU_TIME") == (null)) {
							jo.put("edutime", "");
						} else {
							jo.put("edutime", list.get(i).get("EDU_TIME"));// 教龄
						}
						if (list.get(i).get("EXPER") == (null)) {
							jo.put("exper", "");
						} else {
							jo.put("exper", list.get(i).get("EXPER"));// 中高考阅卷经验
						}
						if (list.get(i).get("FEATURES") == (null)) {
							jo.put("features", "");
						} else {
							jo.put("features", list.get(i).get("FEATURES"));// 教学特色
						}
						if (list.get(i).get("HONOR") == (null)) {
							jo.put("honor", "");
						} else {
							jo.put("honor", list.get(i).get("HONOR"));// 个人荣誉
						}
						if (list.get(i).get("NAME") == (null)) {
							jo.put("name", "");
						} else {
							jo.put("name", list.get(i).get("NAME"));// 姓名
						}
						if (list.get(i).get("SCHOOL") == (null)) {
							jo.put("school", "");
						} else {
							jo.put("school", list.get(i).get("SCHOOL"));// 学校
						}
						if (list.get(i).get("SEX") == (null)) {
							jo.put("sex", "");
						} else {
							jo.put("sex", list.get(i).get("SEX"));// 性别
						}
						if (list.get(i).get("STATE") == (null)) {
							jo.put("state", "");
						} else {
							jo.put("state", list.get(i).get("STATE"));
						}
						if (list.get(i).get("AUSTATE") == (null)) {
							jo.put("austate", "");
						} else {
							jo.put("austate", list.get(i).get("AUSTATE"));
						}
						if (list.get(i).get("GRADE") == (null)) {
							jo.put("grade", "");
						} else {
							jo.put("grade", list.get(i).get("GRADE"));
						}
						//微信qq账号
						if (list.get(0).get("MSG_NAME") != null) {
							jo.put("msg_name", list.get(0).get("MSG_NAME").toString());
						}else {
							jo.put("msg_name", "");
						}
						if (list.get(0).get("QQ_NAME") != null) {
							jo.put("qq_name", list.get(0).get("QQ_NAME").toString());
						}else {
							jo.put("qq_name", "");
						}
					}
					Map<String, String> map = updateDao.selectCompositionCount(udid);
					if (map != null && map.size() > 0) {
						jo.put("unRemark", map.get("unRemark"));
						jo.put("remark", map.get("remark"));
					}else {
						jo.put("unRemark", "");
						jo.put("remark", "");
					}
				}
				return "{\"BM\":" + jo.toString() + ",\"EC\":0,\"EM\":\"\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	// 获取基本信息（老师）
	@POST
	@Path("/getTeacher2")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTeacher2(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> list = updateDao.getTeacher(udid);
				JSONObject jo = new JSONObject();
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(0).get("UDID") == (null)) {
							jo.put("token", "");
						} else {
							jo.put("token", list.get(0).get("UDID").toString());
						}
						jo.put("head", CommUtils.judgeUrl(list.get(0).get("HAND_URL")));// 头像图片
						jo.put("url", CommUtils.judgeUrl(list.get(0).get("HAND_URL")));// 头像图片
						//有无密码处理
						if (list.get(0).get("PASSWORD") == null || "".equals(list.get(0).get("PASSWORD").toString())) {
							jo.put("hasPassWord", "1");
						}else {
							jo.put("hasPassWord", "0");
						}
						
						if (list.get(0).get("NAME") == (null)) {
							jo.put("nickname", "");
						} else {
							jo.put("nickname", list.get(0).get("NAME").toString());
						}
						jo.put("udid", list.get(0).get("UDID"));
						if (list.get(i).get("ACV") == (null)) {
							jo.put("acv", "");
						} else {
							jo.put("acv", list.get(i).get("ACV"));// 教学成就
						}
						if (list.get(i).get("CITY") == (null)) {
							jo.put("city", "");
						} else {
							jo.put("city", list.get(i).get("CITY"));// 省市
						}
						if (list.get(i).get("EDU_TIME") == (null)) {
							jo.put("edutime", "");
						} else {
							jo.put("edutime", list.get(i).get("EDU_TIME"));// 教龄
						}
						if (list.get(i).get("EXPER") == (null)) {
							jo.put("exper", "");
						} else {
							jo.put("exper", list.get(i).get("EXPER"));// 中高考阅卷经验
						}
						if (list.get(i).get("FEATURES") == (null)) {
							jo.put("features", "");
						} else {
							jo.put("features", list.get(i).get("FEATURES"));// 教学特色
						}
						if (list.get(i).get("HONOR") == (null)) {
							jo.put("honor", "");
						} else {
							jo.put("honor", list.get(i).get("HONOR"));// 个人荣誉
						}
						if (list.get(i).get("NAME") == (null)) {
							jo.put("name", "");
						} else {
							jo.put("name", list.get(i).get("NAME"));// 姓名
						}
						if (list.get(i).get("SCHOOL") == (null)) {
							jo.put("school", "");
						} else {
							jo.put("school", list.get(i).get("SCHOOL"));// 学校
						}
						if (list.get(i).get("SEX") == (null)) {
							jo.put("sex", "");
						} else {
							jo.put("sex", list.get(i).get("SEX"));// 性别
						}
						if (list.get(i).get("STATE") == (null)) {
							jo.put("state", "");
						} else {
							jo.put("state", list.get(i).get("STATE"));
						}
						
						if (list.get(i).get("AUSTATE") == (null)) {
							jo.put("austate", "");
						} else {
							//jo.put("austate", list.get(i).get("AUSTATE"));
							Map<String, Object> map2 = CommUtils.authentication2(list);
							jo.put("austate", map2.get("austate"));
							jo.put("cause", map2.get("cause"));
							jo.put("reason", map2.get("reason"));
						}
						
						if (list.get(i).get("GRADE") == (null)) {
							jo.put("grade", "");
						} else {
							jo.put("grade", list.get(i).get("GRADE"));
						}
						//微信qq账号
						if (list.get(0).get("MSG_NAME") != null) {
							jo.put("msg_name", list.get(0).get("MSG_NAME").toString());
						}else {
							jo.put("msg_name", "");
						}
						if (list.get(0).get("QQ_NAME") != null) {
							jo.put("qq_name", list.get(0).get("QQ_NAME").toString());
						}else {
							jo.put("qq_name", "");
						}
					}
					Map<String, String> map = updateDao.selectCompositionCount(udid);
					if (map != null && map.size() > 0) {
						jo.put("unRemark", map.get("unRemark"));
						jo.put("remark", map.get("remark"));
					}else {
						jo.put("unRemark", "");
						jo.put("remark", "");
					}
				}
				return "{\"BM\":" + jo.toString() + ",\"EC\":0,\"EM\":\"\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 修改基本信息（老师）
	@POST
	@Path("/updateTeacher")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTeacher(String json) throws ParseException {
		Teacher tea = new Teacher();
		JSONObject jsonObject;
		String headurl = "";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String acv = str1.getString("acv");
			String city = str1.getString("city");
			String edutime = str1.getString("edutime");
			String exper = str1.getString("exper");
			String features = str1.getString("features");
			String honor = str1.getString("honor");
			String name = str1.getString("name");
			String school = str1.getString("school");
			String sex = str1.getString("sex");
			String grade = str1.getString("grade");
			String extension = str1.getString("extension");// 扩展
			if (!TextUtils.isEmpty(str1.getString("url"))) {
				headurl = str1.getString("url");// 头像图片
			} else {
				headurl = "";
			}
			if (SecurityUtil.isUserLogin(token)) {
				if (!TextUtils.isEmpty(acv)) {
					tea.setAcv(acv);
				}
				if (!TextUtils.isEmpty(city)) {
					tea.setCity(city);
				}
				if (!TextUtils.isEmpty(edutime)) {
					tea.setEdutime(edutime);
				}
				if (!TextUtils.isEmpty(exper)) {
					tea.setExper(exper);
				}
				if (!TextUtils.isEmpty(features)) {
					tea.setFeatures(features);
				}
				if (!TextUtils.isEmpty(honor)) {
					tea.setHonor(honor);
				}
				if (!TextUtils.isEmpty(name)) {
					tea.setName(name);
				}
				if (!TextUtils.isEmpty(school)) {
					tea.setSchool(school);
				}
				if (!TextUtils.isEmpty(sex)) {
					tea.setSex(sex);
				}
				if (!TextUtils.isEmpty(udid)) {
					tea.setUdid(udid);
				}
				if (!TextUtils.isEmpty(headurl)) {
					tea.setHeadurl(headurl);// 头像图片
				}
				if (!TextUtils.isEmpty(grade)) {
					/*int grange = Integer.parseInt(grade);
					if (027000 < grange && grange < 027007) {
						tea.setGrange("小学");// 027001-027006 小学
					} else if (027010 < grange && grange < 027014) {
						tea.setGrange("初中");// 027011-027013 初中
					} else {
						tea.setGrange("高中");// 027021-027023 高中
					}*/
					if(grade.contains("02700")){
						tea.setGrange("小学");
					} else if (grade.contains("02701")) {
						tea.setGrange("初中");// 027011-027013 初中
					} else {
						tea.setGrange("高中");// 027021-027023 高中
					}
					tea.setGrade(grade);
				}
				tea.setExtension(extension);
				int rest = updateDao.updateTeacher(tea);
				if (rest > 0) {
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\"" + udid + "\"},\"EC\":0,\"EM\":\"\"}";
				} else if (rest == -2) {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"修改失败\"}";
				}
			}
		} catch (JSONException e) {
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 验证学生密码
	@POST
	@Path("/findPassword")
	@Produces(MediaType.APPLICATION_JSON)
	public String findPassword(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String udid = str1.getString("udid");
			String password = str1.getString("password");
			int bool = updateDao.findPassword(password, udid);
			if (bool > 0) {
				return "{\"BM\":{\"password\":true},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{\"password\":false},\"EC\":19070,\"EM\":\"密码错误\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"password\":false},\"EC\":10203,\"EM\":\"参数错误\"}";
		}
	}

	// 验证老师密码
	@POST
	@Path("/findTpassword")
	@Produces(MediaType.APPLICATION_JSON)
	public String findTpassword(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String password = str1.getString("password");
			int bool = updateDao.findTpassword(password, udid);
			if (bool > 0) {
				return "{\"BM\":{\"password\":true},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{\"password\":false},\"EC\":19070,\"EM\":\"密码错误\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"password\":false},\"EC\":10203,\"EM\":\"参数错误\"}";
		}
	}

	@POST
	@Path("/uplodeimage")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uplodeimage(InputStream file)  {
		OutputStream os = null;
		InputStream is = null;
		try {
			// 设置输出文件夹
			Calendar a = Calendar.getInstance();
			int year = a.get(Calendar.YEAR);
			int month = a.get(Calendar.MONTH) + 1;
			String rootPath = CommUtils.imgRootPath + "/composition/" + year + "/" + month + "/";
			File rootFolder = new File(rootPath);
			if (!rootFolder.exists()) {
				rootFolder.mkdirs();
			}
			File file2 = new File(rootPath);
			if (!file2.exists()) {
				file2.mkdir();
			}
			
			logger.info("------" + rootPath+"------" );
			
			//System.out.println("------" + rootPath);
			is = file;
			if (is.available() > 5242880) {
				return "{\"BM\":{},\"EC\":20444,\"EM\":\"文件大小超出5m\"}";
			}
			System.out.println("图片大小:------" + is.available());
			
			
			logger.info("图片大小:------" + is.available()+"------" );
			
			String imgName = new Date().getTime() + getExtention("222.jpg");
			os = new FileOutputStream(new File(rootPath + imgName));
			byte[] b = new byte[5242880];
			int bytesRead = 0;
			while ((bytesRead = is.read(b)) != -1) {
				
				logger.info("图片上传过程++++++++++++++:------" + bytesRead+"------" );
				
				os.write(b, 0, bytesRead);
			}
			String rest = "{\"BM\":{\"image\":\"" + "/files/pingdianapp_img/composition/" + year + "/" + month + "/" + imgName + "\"},\"EC\":0,\"EM\":\"\"}";
			
			logger.info("图片上传完成++++++++++++++:------" + rest+"------" );
			
			return rest;
		} catch (Exception e) {
			logger.info("-----------错误信息二进制文件上传------"+ e.getMessage() );
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.info("-----------流未正常关闭------"+ e.getMessage() );
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.info("-----------流未正常关闭------"+ e.getMessage() );
				}
			}
		}
		return null;
		
	}

	@POST
	@Path("/updateTstate")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTstate(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String state = str1.getString("state");
			int bool = updateDao.updateTstate(state, udid);
			if (bool > 0) {
				return "{\"BM\":{\"state\":true},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"state\":false},\"EC\":10553,\"EM\":\"请求错误\"}";
		}
		return "{\"BM\":{\"state\":false},\"EC\":19570,\"EM\":\"老师状态修改失败\"}";
	}

	// 获取文件类型
	private String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	// 修改师资认证 -回显
	public String updateRenzheng(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				List<Map<String, Object>> list = updateDao.getRenzheng(udid);
				JSONObject jo = new JSONObject();
				if (list.size() > 0) {
					SecurityUtil.sessionList.add(token);
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).get("CERT_URL") == (null)) {
							jo.put("image1", "");
						} else {
							jo.put("image1", list.get(i).get("CERT_URL"));
						}
						if (list.get(i).get("CARD1") == (null)) {
							jo.put("image2", "");
						} else {
							jo.put("image2", list.get(i).get("CARD1"));
						}
						if (list.get(i).get("CARD2") == (null)) {
							jo.put("image3", "");
						} else {
							jo.put("image3", list.get(i).get("CARD2"));
						}
					}
					// System.out.println(jo.toString());
				}
				return "{\"BM\":" + jo.toString() + ",\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			return "{\"BM\":{\"isRegister\":false},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	private static Logger logger = Logger.getLogger(UpdateServiceImpl.class);

}
