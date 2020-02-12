package com.rest.service.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.LoginDao;
import com.util.CommUtils;

public class LoginDaoImpl implements LoginDao {

	// public Map findStudent(String phone,String udid) {
	public Map findStudent(String phone, String password) {
		// 学生登录
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// String sql = "select * from student where password='"+password+"' and phone
			// ='"+phone+"'";
			// List<Map<String, Object>> slist =jt.queryForList(sql);
			String sql = "select * from student where password=? and phone =?";
			List<Map<String, Object>> slist = jt.queryForList(sql, new Object[] { password, phone });
			if (slist.size() > 0) {
				// if(slist.get(0).get("phone").equals(phone)){
				// 用户存在更改登陆状态，获取token
				String sql1 = "update login set LOGINSTATE=? where id=?";
				Object[] params = new Object[] { "2", slist.get(0).get("LOGIN_ID") };
				int rest = jt.update(sql1, params);
				String sql2 = "select * from login where id = ?";
				List<Map<String, Object>> l = jt.queryForList(sql2, new Object[] { slist.get(0).get("LOGIN_ID") });
				if (rest > 0 && l.get(0).get("TOKEN") != null) {
					
					map.put("token", l.get(0).get("TOKEN").toString());
					map.put("grade", CommUtils.judgeSqlInformation(slist.get(0).get("GRADE")));
					map.put("head", CommUtils.judgeUrl(slist.get(0).get("HEAD")));
					map.put("completeInfo", CommUtils.judgeSqlInformation(slist.get(0).get("COMPLETE_INFO")));
					map.put("nickname", CommUtils.judgeSqlInformation(slist.get(0).get("NICKNAME")));
					map.put("phone", CommUtils.judgeSqlInformation(slist.get(0).get("PHONE")));
					map.put("udid", CommUtils.judgeSqlInformation(slist.get(0).get("UDID")));
					map.put("sex", CommUtils.judgeSqlInformation(slist.get(0).get("SEX")));
					map.put("city", CommUtils.judgeSqlInformation(slist.get(0).get("AREA")));
					map.put("intr", CommUtils.judgeSqlInformation(slist.get(0).get("INTRODUCE")));
					map.put("name", CommUtils.judgeSqlInformation(slist.get(0).get("NAME")));
					map.put("school", CommUtils.judgeSqlInformation(slist.get(0).get("SCHOOL")));
					return map;
				}
				// }
			}
			// 用户不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return map;
	}

	// public Map findTeacher(String phone,String udid) {
	public Map<String, Object> findTeacher(String phone, String password) {
		// 老师登录
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT t.*,e.STATE estate FROM teacher t LEFT JOIN expected_com e ON e.TEACHER_ID=t.ID WHERE `PASSWORD`=? and PHONE =?";
			List<Map<String, Object>> tlist = jt.queryForList(sql, new Object[] { password, phone });
			// if(tlist.get(0).get("phone").equals(phone)){
			// 用户存在更改登陆状态，获取token
			String sql1 = "update login set LOGINSTATE=? where id=?";
			Object[] params = new Object[] { "2", tlist.get(0).get("LOGIN_ID") };
			int rest = jt.update(sql1, params);
			String sql2 = "select * from login where id = ?";
			List<Map<String, Object>> l = jt.queryForList(sql2, new Object[] { tlist.get(0).get("LOGIN_ID") });
			if (rest > 0 && !l.get(0).get("TOKEN").equals("")) {
				
				map.put("token", CommUtils.judgeSqlInformation(tlist.get(0).get("UDID")));
				map.put("head", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				map.put("url", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				map.put("nickname", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME")));
				map.put("name", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME")));
				map.put("acv", CommUtils.judgeSqlInformation(tlist.get(0).get("ACV")));
				map.put("city", CommUtils.judgeSqlInformation(tlist.get(0).get("CITY")));
				map.put("edutime", CommUtils.judgeSqlInformation(tlist.get(0).get("EDU_TIME")));
				map.put("exper", CommUtils.judgeSqlInformation(tlist.get(0).get("EXPER")));
				map.put("features", CommUtils.judgeSqlInformation(tlist.get(0).get("FEATURES")));
				map.put("honor", CommUtils.judgeSqlInformation(tlist.get(0).get("HONOR")));
				map.put("school", CommUtils.judgeSqlInformation(tlist.get(0).get("SCHOOL")));
				map.put("sex", CommUtils.judgeSqlInformation(tlist.get(0).get("SEX")));
				map.put("state", CommUtils.judgeSqlInformation(tlist.get(0).get("STATE")));
				map.put("grade", CommUtils.judgeSqlInformation(tlist.get(0).get("GRADE")));
				map.put("udid", tlist.get(0).get("UDID"));
				map.put("phone", phone);
				Map<String, Object> map2 = CommUtils.authentication(tlist);
				map.put("austate", map2.get("austate"));
				map.put("cause", map2.get("cause"));
				/*
				map.put("austate", CommUtils.judgeSqlInformation(tlist.get(0).get("AUSTATE")));
				String cause = CommUtils.judgeSqlInformation(tlist.get(0).get("CAUSE"));
				String cause1 = "";
				String cause2 = "";
				String cause3 = "";
				String uploadTime = CommUtils.judgeSqlInformation(tlist.get(0).get("UPLOAD_TIME"));
				if(uploadTime.equals("")){
					cause2 = "未提交师资认证";
					cause = cause + "-" + cause2;
				}
				String perfectTime = CommUtils.judgeSqlInformation(tlist.get(0).get("PERFECT_TIME"));
				if(perfectTime.equals("")){
					cause1 = "未完善个人信息";
					cause = cause + "-" + cause1;
				}
				
				String tid = CommUtils.judgeSqlInformation(tlist.get(0).get("ID"));
				map.put("expectedState", "");
				if(!tid.equals("")){
					String sql3 = "select * from expected_com where TEACHER_ID ="+tid;
					List<Map<String,Object>> expected = jt.queryForList(sql3);
					if(expected.size()>0){
						map.put("expectedState", CommUtils.judgeSqlInformation(expected.get(0).get("STATE")));
					}else{
						cause3 = "未试点评";
						cause = cause + "-" + cause3;
					}
				}
				map.put("cause", cause);
				*/
				return map;
			}
			// }
			// 用户不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return map;
	}

	public Map<String, Object> findTeacher2(String phone, String password) {
		// 老师登录
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT t.*,e.STATE estate FROM teacher t LEFT JOIN expected_com e ON e.TEACHER_ID=t.ID WHERE `PASSWORD`=? and PHONE =?";
			List<Map<String, Object>> tlist = jt.queryForList(sql, new Object[] { password, phone });
			// if(tlist.get(0).get("phone").equals(phone)){
			// 用户存在更改登陆状态，获取token
			String sql1 = "update login set LOGINSTATE=? where id=?";
			Object[] params = new Object[] { "2", tlist.get(0).get("LOGIN_ID") };
			int rest = jt.update(sql1, params);
			String sql2 = "select * from login where id = ?";
			List<Map<String, Object>> l = jt.queryForList(sql2, new Object[] { tlist.get(0).get("LOGIN_ID") });
			if (rest > 0 && !l.get(0).get("TOKEN").equals("")) {
				
				map.put("token", CommUtils.judgeSqlInformation(tlist.get(0).get("UDID")));
				map.put("head", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				map.put("url", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				map.put("nickname", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME")));
				map.put("name", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME")));
				map.put("acv", CommUtils.judgeSqlInformation(tlist.get(0).get("ACV")));
				map.put("city", CommUtils.judgeSqlInformation(tlist.get(0).get("CITY")));
				map.put("edutime", CommUtils.judgeSqlInformation(tlist.get(0).get("EDU_TIME")));
				map.put("exper", CommUtils.judgeSqlInformation(tlist.get(0).get("EXPER")));
				map.put("features", CommUtils.judgeSqlInformation(tlist.get(0).get("FEATURES")));
				map.put("honor", CommUtils.judgeSqlInformation(tlist.get(0).get("HONOR")));
				map.put("school", CommUtils.judgeSqlInformation(tlist.get(0).get("SCHOOL")));
				map.put("sex", CommUtils.judgeSqlInformation(tlist.get(0).get("SEX")));
				map.put("state", CommUtils.judgeSqlInformation(tlist.get(0).get("STATE")));
				map.put("grade", CommUtils.judgeSqlInformation(tlist.get(0).get("GRADE")));
				map.put("udid", tlist.get(0).get("UDID"));
				map.put("phone", phone);
				Map<String, Object> map2 = CommUtils.authentication2(tlist);
				map.put("austate", map2.get("austate"));
				map.put("cause", map2.get("cause"));
				map.put("reason", map2.get("reason"));
				/*
				map.put("austate", CommUtils.judgeSqlInformation(tlist.get(0).get("AUSTATE")));
				String cause = CommUtils.judgeSqlInformation(tlist.get(0).get("CAUSE"));
				String cause1 = "";
				String cause2 = "";
				String cause3 = "";
				String uploadTime = CommUtils.judgeSqlInformation(tlist.get(0).get("UPLOAD_TIME"));
				if(uploadTime.equals("")){
					cause2 = "未提交师资认证";
					cause = cause + "-" + cause2;
				}
				String perfectTime = CommUtils.judgeSqlInformation(tlist.get(0).get("PERFECT_TIME"));
				if(perfectTime.equals("")){
					cause1 = "未完善个人信息";
					cause = cause + "-" + cause1;
				}
				
				String tid = CommUtils.judgeSqlInformation(tlist.get(0).get("ID"));
				map.put("expectedState", "");
				if(!tid.equals("")){
					String sql3 = "select * from expected_com where TEACHER_ID ="+tid;
					List<Map<String,Object>> expected = jt.queryForList(sql3);
					if(expected.size()>0){
						map.put("expectedState", CommUtils.judgeSqlInformation(expected.get(0).get("STATE")));
					}else{
						cause3 = "未试点评";
						cause = cause + "-" + cause3;
					}
				}
				map.put("cause", cause);
				*/
				return map;
			}
			// }
			// 用户不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return map;
	}
	
	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	@Override
	public int logout(String token, String logintype) {
		String sql = "update login set LOGINSTATE = ? where token = ? and LOGINTYPE = ? ";
		int i = jt.update(sql, new Object[]{"1",token,logintype});
		return i;
	}

	@Override
	public String findToken(String phone) {
		String sql = "select token from login where phone = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{phone});
		String token = "";
		if (list != null && list.size() > 0) {
			token = (String) list.get(0).get("token");
		}
		return token;
	}

	@Override
	public int findStudent(String phone) {
		String sql = "select id from student where phone =?";
		List<Map<String, Object>> list = jt.queryForList(sql, new Object[]{phone});
		if (list != null && list.size() > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public int findTeacher(String phone) {
		String sql = "select id from teacher where phone =?";
		List<Map<String, Object>> list = jt.queryForList(sql, new Object[]{phone});
		if (list != null && list.size() > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public Map<String, Object> findStuByOpenid_msg(String openid, String unionid) {
		Map<String, Object> map = null;
		if (unionid != null) {
			map = stuGetInfor(unionid, "UNIONID_", "MSG");
			if (map != null) {//客户端更新，正常登陆
				return map;
			}else {//客户端第一次更新，检查openid是否注册过
				map = stuGetInfor(openid, "OPENID_", "MSG");
				if (map != null) {//客户端第一次更新，openid已经注册过，绑定上unionid即可
					bindingUnionid("student", "MSG", unionid, openid);
					return map;
				}
			}
		}else {
			map = stuGetInfor(openid, "OPENID_", "MSG");
		}
		//unionid和openid都没有返回结果
		return map;
	}
	
	@Override
	public Map<String, Object> findStuByOpenid_qq(String openid, String unionid) {
		Map<String, Object> map = null;
		if (unionid != null) {
			map = stuGetInfor(unionid, "UNIONID_", "QQ");
			if (map != null) {//客户端更新，正常登陆
				return map;
			}else {//客户端第一次更新，检查openid是否注册过
				map = stuGetInfor(openid, "OPENID_", "QQ");
				if (map != null) {//客户端第一次更新，openid已经注册过，绑定上unionid即可
					bindingUnionid("student", "QQ", unionid, openid);
					return map;
				}
			}
		}else {
			map = stuGetInfor(openid, "OPENID_", "QQ");
		}
		//unionid和openid都没有返回结果
		return map;
	}
	
	@Override
	public Map<String, Object> findTeaByOpenid_msg(String openid, String unionid) {
		Map<String, Object> map = null;
		if (unionid != null) {
			map = teaGetInfor(unionid, "UNIONID_", "MSG");
			if (map != null) {//客户端更新，正常登陆
				return map;
			}else {//客户端第一次更新，检查openid是否注册过
				map = teaGetInfor(openid, "OPENID_", "MSG");
				if (map != null) {//客户端第一次更新，openid已经注册过，绑定上unionid即可
					bindingUnionid("teacher", "MSG", unionid, openid);
					return map;
				}
			}
		}else {
			map = teaGetInfor(openid, "OPENID_", "MSG");
		}
		//unionid和openid都没有返回结果
		return map;
	}
	
	@Override
	public Map<String, Object> findTeaByOpenid_qq(String openid, String unionid) {
		Map<String, Object> map = null;
		if (unionid != null) {
			map = teaGetInfor(unionid, "UNIONID_", "QQ");
			if (map != null) {//客户端更新，正常登陆
				return map;
			}else {//客户端第一次更新，检查openid是否注册过
				map = teaGetInfor(openid, "OPENID_", "QQ");
				if (map != null) {//客户端第一次更新，openid已经注册过，绑定上unionid即可
					bindingUnionid("teacher", "QQ", unionid, openid);
					return map;
				}
			}
		}else {
			map = teaGetInfor(openid, "OPENID_", "QQ");
		}
		//unionid和openid都没有返回结果
		return map;
	}

	@Override
	public Map<String, Object> stu_binding_qq_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName) {
		//map存放用户信息
		Map<String, Object> map = new HashMap<String, Object>();
		//查询用户是否存在
		String sql1 = "UPDATE student SET OPENID_QQ = ? ,QQ_NAME = ? ,UNIONID_QQ = ? WHERE PHONE = ?";
		int i = jt.update(sql1, new Object[]{openid, thirdAccountName, unionid, phone});
		if (i == 1) {
			String sql2 = "select * from student where phone =?";
			String sql3 = "select TOKEN from login where id = ?";
			String sql4 = "update login set LOGINSTATE=? where id=?";
			
			//判断昵称头像是否存在
			StringBuffer sql5 = new StringBuffer();
			sql5.append("UPDATE student SET ");
			int count = 0;
			
			//查询用户所有信息
			List<Map<String, Object>> slist = jt.queryForList(sql2, new Object[]{phone});
			
			//如果昵称头像不存在，则存入
			if (slist.get(0).get("HEAD") == null || "".equals(slist.get(0).get("HEAD").toString())) {
				count++;
				sql5.append(" HEAD = \"" + thirdAccountHead + "\" ");
			}
			if (slist.get(0).get("NICKNAME") == null || "".equals(slist.get(0).get("NICKNAME").toString())) {
				if (count == 1) {
					sql5.append(" , NICKNAME = \"" + thirdAccountName + "\" ");
				}else {
					sql5.append(" NICKNAME = \"" + thirdAccountName + "\" ");
				}
				count++;
			}
			sql5.append(" WHERE PHONE = \"" + phone + "\"");
			if (count != 0) {
				jt.update(sql5.toString());
			}
			
			//查找token
			String token = jt.queryForObject(sql3, new Object[] {slist.get(0).get("LOGIN_ID")}, String.class);
			//改变login表登录状态
			int rest = jt.update(sql4, new Object[]{2, slist.get(0).get("LOGIN_ID")});
			
			if (rest > 0) {
				map.put("token", token);
				map.put("grade", CommUtils.judgeSqlInformation(slist.get(0).get("GRADE")));
				map.put("head", CommUtils.judgeUrl(slist.get(0).get("HEAD"), thirdAccountHead));
				map.put("completeInfo", CommUtils.judgeSqlInformation(slist.get(0).get("COMPLETE_INFO")));
				map.put("phone", CommUtils.judgeSqlInformation(slist.get(0).get("PHONE")));
				map.put("udid", CommUtils.judgeSqlInformation(slist.get(0).get("UDID")));
				map.put("sex", CommUtils.judgeSqlInformation(slist.get(0).get("SEX")));
				map.put("city", CommUtils.judgeSqlInformation(slist.get(0).get("AREA")));
				map.put("intr", CommUtils.judgeSqlInformation(slist.get(0).get("INTRODUCE")));
				map.put("name", CommUtils.judgeSqlInformation(slist.get(0).get("NAME")));
				map.put("nickname", CommUtils.judgeSqlInformation(slist.get(0).get("NICKNAME"), thirdAccountName));
				map.put("school", CommUtils.judgeSqlInformation(slist.get(0).get("SCHOOL")));
			}
			return map;
		}else {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> stu_binding_msg_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName) {
		//map存放用户信息
		Map<String, Object> map = new HashMap<String, Object>();
		//查询用户是否存在
		String sql1 = "UPDATE student SET OPENID_MSG = ? ,MSG_NAME = ? , UNIONID_MSG = ? WHERE PHONE = ?";
		int i = jt.update(sql1, new Object[]{openid, thirdAccountName, unionid, phone});
		if (i == 1) {
			String sql2 = "select * from student where phone =?";
			String sql3 = "select TOKEN from login where id = ?";
			String sql4 = "update login set LOGINSTATE=? where id=?";
			
			//判断昵称头像是否存在
			StringBuffer sql5 = new StringBuffer();
			sql5.append("UPDATE student SET ");
			int count = 0;
			
			//查询用户所有信息
			List<Map<String, Object>> slist = jt.queryForList(sql2, new Object[]{phone});
			
			//如果昵称头像不存在，则存入
			if (slist.get(0).get("HEAD") == null || "".equals(slist.get(0).get("HEAD").toString())) {
				count++;
				sql5.append(" HEAD = \"" + thirdAccountHead + "\" ");
			}
			if (slist.get(0).get("NICKNAME") == null || "".equals(slist.get(0).get("NICKNAME").toString())) {
				if (count == 1) {
					sql5.append(" , NICKNAME = \"" + thirdAccountName + "\" ");
				}else {
					sql5.append(" NICKNAME = \"" + thirdAccountName + "\" ");
				}
				count++;
			}
			sql5.append(" WHERE PHONE = \"" + phone + "\"");
			if (count != 0) {
				jt.update(sql5.toString());
			}
			
			//查找token
			String token = jt.queryForObject(sql3, new Object[] {slist.get(0).get("LOGIN_ID")}, String.class);
			//改变login表登录状态
			int rest = jt.update(sql4, new Object[]{2, slist.get(0).get("LOGIN_ID")});
			
			if (rest > 0) {
				map.put("token", token);
				map.put("grade", CommUtils.judgeSqlInformation(slist.get(0).get("GRADE")));
				map.put("head", CommUtils.judgeUrl(slist.get(0).get("HEAD"), thirdAccountHead));
				map.put("completeInfo", CommUtils.judgeSqlInformation(slist.get(0).get("COMPLETE_INFO")));
				map.put("phone", CommUtils.judgeSqlInformation(slist.get(0).get("PHONE")));
				map.put("udid", CommUtils.judgeSqlInformation(slist.get(0).get("UDID")));
				map.put("sex", CommUtils.judgeSqlInformation(slist.get(0).get("SEX")));
				map.put("city", CommUtils.judgeSqlInformation(slist.get(0).get("AREA")));
				map.put("intr", CommUtils.judgeSqlInformation(slist.get(0).get("INTRODUCE")));
				map.put("name", CommUtils.judgeSqlInformation(slist.get(0).get("NAME")));
				map.put("nickname", CommUtils.judgeSqlInformation(slist.get(0).get("NICKNAME"), thirdAccountName));
				map.put("school", CommUtils.judgeSqlInformation(slist.get(0).get("SCHOOL")));
			}
			return map;
		}else {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> tea_binding_msg_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName) {
		//map存放用户信息
		Map<String, Object> map = new HashMap<String, Object>();
		//查询用户是否存在
		String sql1 = "UPDATE teacher SET OPENID_MSG = ?,MSG_NAME=? , UNIONID_MSG = ? WHERE PHONE = ?";
		int i = jt.update(sql1, new Object[]{openid, thirdAccountName, unionid, phone});
		if (i == 1) {
			String sql2 = "select * from teacher where phone =?";
			String sql3 = "select TOKEN from login where id = ?";
			String sql4 = "update login set LOGINSTATE=? where id=?";
			
			//判断昵称头像是否存在
			StringBuffer sql5 = new StringBuffer();
			sql5.append("UPDATE teacher SET ");
			int count = 0;
			
			//查询用户所有信息
			List<Map<String, Object>> tlist = jt.queryForList(sql2, new Object[]{phone});
			
			//如果昵称头像不存在，则存入
			if (tlist.get(0).get("HAND_URL") == null || "".equals(tlist.get(0).get("HAND_URL").toString())) {
				count++;
				sql5.append(" HAND_URL = \"" + thirdAccountHead + "\" ");
			}
			if (tlist.get(0).get("NAME") == null || "".equals(tlist.get(0).get("NAME").toString())) {
				if (count == 1) {
					sql5.append(" , NAME = \"" + thirdAccountName + "\" ");
				}else {
					sql5.append(" NAME = \"" + thirdAccountName + "\" ");
				}
				count++;
			}
			sql5.append(" WHERE PHONE = \"" + phone + "\"");
			if (count != 0) {
				jt.update(sql5.toString());
			}
			
			//查找token
			String token = jt.queryForObject(sql3, new Object[] {tlist.get(0).get("LOGIN_ID")}, String.class);
			//改变login表登录状态
			int rest = jt.update(sql4, new Object[]{2, tlist.get(0).get("LOGIN_ID")});
			
			if (rest > 0) {
				map.put("token", token);
				map.put("head", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL"), thirdAccountHead));
				map.put("url", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL"), thirdAccountHead));
				map.put("nickname", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME"), thirdAccountName));
				map.put("name", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME"), thirdAccountName));
				map.put("acv", CommUtils.judgeSqlInformation(tlist.get(0).get("ACV")));
				map.put("city", CommUtils.judgeSqlInformation(tlist.get(0).get("CITY")));
				map.put("edutime", CommUtils.judgeSqlInformation(tlist.get(0).get("EDU_TIME")));
				map.put("exper", CommUtils.judgeSqlInformation(tlist.get(0).get("EXPER")));
				map.put("features", CommUtils.judgeSqlInformation(tlist.get(0).get("FEATURES")));
				map.put("honor", CommUtils.judgeSqlInformation(tlist.get(0).get("HONOR")));
				map.put("school", CommUtils.judgeSqlInformation(tlist.get(0).get("SCHOOL")));
				map.put("sex", CommUtils.judgeSqlInformation(tlist.get(0).get("SEX")));
				map.put("state", CommUtils.judgeSqlInformation(tlist.get(0).get("STATE")));
				map.put("austate", CommUtils.judgeSqlInformation(tlist.get(0).get("AUSTATE")));
				map.put("grade", CommUtils.judgeSqlInformation(tlist.get(0).get("GRADE")));
				map.put("udid", tlist.get(0).get("UDID"));
				map.put("phone", phone);
			}
			return map;
		}else {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> tea_binding_qq_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName) {
		//map存放用户信息
		Map<String, Object> map = new HashMap<String, Object>();
		//查询用户是否存在
		String sql1 = "UPDATE teacher SET OPENID_QQ = ?,QQ_NAME=? ,UNIONID_QQ = ? WHERE PHONE = ?";
		int i = jt.update(sql1, new Object[]{openid, thirdAccountName, unionid, phone});
		if (i == 1) {
			String sql2 = "select * from teacher where phone =?";
			String sql3 = "select TOKEN from login where id = ?";
			String sql4 = "update login set LOGINSTATE=? where id=?";
			
			//判断昵称头像是否存在
			StringBuffer sql5 = new StringBuffer();
			sql5.append("UPDATE teacher SET ");
			int count = 0;
			//String sql5 = "UPDATE teacher SET HAND_URL = ?,NAME = ？ WHERE PHONE = ?";
			
			//查询用户所有信息
			List<Map<String, Object>> tlist = jt.queryForList(sql2, new Object[]{phone});
			//如果昵称头像不存在，则存入
			if (tlist.get(0).get("HAND_URL") == null || "".equals(tlist.get(0).get("HAND_URL").toString())) {
				count++;
				sql5.append(" HAND_URL = \"" + thirdAccountHead + "\" ");
			}
			if (tlist.get(0).get("NAME") == null || "".equals(tlist.get(0).get("NAME").toString())) {
				if (count == 1) {
					sql5.append(" , NAME = \"" + thirdAccountName + "\" ");
				}else {
					sql5.append(" NAME = \"" + thirdAccountName + "\" ");
				}
				count++;
			}
			sql5.append(" WHERE PHONE = \"" + phone + "\"");
			if (count != 0) {
				jt.update(sql5.toString());
			}
			
			//查找token
			String token = jt.queryForObject(sql3, new Object[] {tlist.get(0).get("LOGIN_ID")}, String.class);
			//改变login表登录状态
			int rest = jt.update(sql4, new Object[]{2, tlist.get(0).get("LOGIN_ID")});
			
			if (rest > 0) {
				map.put("token", token);
				map.put("head", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL"), thirdAccountHead));
				map.put("url", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL"), thirdAccountHead));
				map.put("nickname", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME"), thirdAccountName));
				map.put("name", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME"), thirdAccountName));
				map.put("acv", CommUtils.judgeSqlInformation(tlist.get(0).get("ACV")));
				map.put("city", CommUtils.judgeSqlInformation(tlist.get(0).get("CITY")));
				map.put("edutime", CommUtils.judgeSqlInformation(tlist.get(0).get("EDU_TIME")));
				map.put("exper", CommUtils.judgeSqlInformation(tlist.get(0).get("EXPER")));
				map.put("features", CommUtils.judgeSqlInformation(tlist.get(0).get("FEATURES")));
				map.put("honor", CommUtils.judgeSqlInformation(tlist.get(0).get("HONOR")));
				map.put("school", CommUtils.judgeSqlInformation(tlist.get(0).get("SCHOOL")));
				map.put("sex", CommUtils.judgeSqlInformation(tlist.get(0).get("SEX")));
				map.put("state", CommUtils.judgeSqlInformation(tlist.get(0).get("STATE")));
				map.put("austate", CommUtils.judgeSqlInformation(tlist.get(0).get("AUSTATE")));
				map.put("grade", CommUtils.judgeSqlInformation(tlist.get(0).get("GRADE")));
				map.put("udid", tlist.get(0).get("UDID"));
				map.put("phone", phone);
			}
			return map;
		}else {
			return null;
		}
	}

	@Override
	public int stu_binding_qq_name(String udid, String openid, String qqName, String thirdAccountHead, String unionid) {
		//String sql = "UPDATE student SET OPENID_QQ = ?, QQ_NAME = ? WHERE UDID = ?";
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE student SET OPENID_QQ = ?, QQ_NAME = ? ,UNIONID_QQ = ? ");
		String sql2 = "SELECT NICKNAME,HEAD FROM student WHERE UDID = ?";
		//判断昵称和头像是否存在
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{udid});
		if (list != null && list.size() > 0) {
			if (list.get(0).get("NICKNAME") == null || "".equals(list.get(0).get("NICKNAME").toString())) {
				sb.append(" ,NICKNAME = \"" + qqName + "\"");
			}
			if (list.get(0).get("HEAD") == null || "".equals(list.get(0).get("HEAD").toString())) {
				sb.append(" ,HEAD = \"" + thirdAccountHead + "\"");
			}
		}
		sb.append(" WHERE UDID = ?");
		int i = jt.update(sb.toString(), new Object[]{openid, qqName, unionid, udid});
		return i;
	}

	@Override
	public int stu_binding_msg_name(String udid, String openid, String msgName, String thirdAccountHead, String unionid) {
		//String sql = "UPDATE student SET OPENID_MSG = ?, MSG_NAME = ? WHERE UDID = ?";
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE student SET OPENID_MSG = ?, MSG_NAME = ? ,UNIONID_MSG = ? ");
		String sql2 = "SELECT NICKNAME,HEAD FROM student WHERE UDID = ?";
		//判断昵称和头像是否存在
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{udid});
		if (list != null && list.size() > 0) {
			if (list.get(0).get("NICKNAME") == null || "".equals(list.get(0).get("NICKNAME").toString())) {
				sb.append(" ,NICKNAME = \"" + msgName + "\"");
			}
			if (list.get(0).get("HEAD") == null || "".equals(list.get(0).get("HEAD").toString())) {
				sb.append(" ,HEAD = \"" + thirdAccountHead + "\"");
			}
		}
		sb.append(" WHERE UDID = ?");
		int i = jt.update(sb.toString(), new Object[]{openid, msgName, unionid, udid});
		return i;
	}

	@Override
	public int tea_binding_qq_name(String udid, String openid, String qqName, String thirdAccountHead, String unionid) {
		//String sql = "UPDATE teacher SET OPENID_QQ = ?, QQ_NAME = ? WHERE UDID = ?";
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE teacher SET OPENID_QQ = ?, QQ_NAME = ? ,UNIONID_QQ = ?  ");
		String sql2 = "SELECT NAME,HAND_URL FROM teacher WHERE UDID = ?";
		//判断昵称和头像是否存在
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{udid});
		if (list != null && list.size() > 0) {
			if (list.get(0).get("NAME") == null || "".equals(list.get(0).get("NAME").toString())) {
				sb.append(" ,NAME = \"" + qqName + "\"");
			}
			if (list.get(0).get("HAND_URL") == null || "".equals(list.get(0).get("HAND_URL").toString())) {
				sb.append(" ,HAND_URL = \"" + thirdAccountHead + "\"");
			}
		}
		sb.append(" WHERE UDID = ?");
		int i = jt.update(sb.toString(), new Object[]{openid, qqName, unionid, udid});
		return i;
	}

	@Override
	public int tea_binding_msg_name(String udid, String openid, String msgName, String thirdAccountHead, String unionid) {
		//String sql = "UPDATE teacher SET OPENID_MSG = ?, MSG_NAME = ? WHERE UDID = ?";
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE teacher SET OPENID_MSG = ?, MSG_NAME = ? ,UNIONID_MSG = ?   ");
		String sql2 = "SELECT NAME,HAND_URL FROM teacher WHERE UDID = ?";
		//判断昵称和头像是否存在
		List<Map<String,Object>> list = jt.queryForList(sql2, new Object[]{udid});
		if (list != null && list.size() > 0) {
			if (list.get(0).get("NAME") == null || "".equals(list.get(0).get("NAME").toString())) {
				sb.append(" ,NAME = \"" + msgName + "\"");
			}
			if (list.get(0).get("HAND_URL") == null || "".equals(list.get(0).get("HAND_URL").toString())) {
				sb.append(" ,HAND_URL = \"" + thirdAccountHead + "\"");
			}
		}
		sb.append(" WHERE UDID = ?");
		int i = jt.update(sb.toString(), new Object[]{openid, msgName, unionid, udid});
		return i;
	}

	@Override
	public int stu_unbinding_qq(String udid) {
		String sql = "UPDATE student SET OPENID_QQ = NULL, QQ_NAME = NULL , UNIONID_QQ = NULL WHERE UDID = ?";
		int i = jt.update(sql, new Object[]{udid});
		return i;
	}

	@Override
	public int stu_unbinding_msg(String udid) {
		String sql = "UPDATE student SET OPENID_MSG = NULL, MSG_NAME = NULL  , UNIONID_MSG = NULL WHERE UDID = ?";
		int i = jt.update(sql, new Object[]{udid});
		return i;
	}

	@Override
	public int tea_unbinding_qq(String udid) {
		String sql = "UPDATE teacher SET OPENID_QQ = NULL, QQ_NAME = NULL  , UNIONID_QQ = NULL WHERE UDID = ?";
		int i = jt.update(sql, new Object[]{udid});
		return i;
	}

	@Override
	public int tea_unbinding_msg(String udid) {
		String sql = "UPDATE teacher SET OPENID_MSG = NULL, MSG_NAME = NULL  , UNIONID_MSG = NULL WHERE UDID = ?";
		int i = jt.update(sql, new Object[]{udid});
		return i;
	}
	
	private Map<String, Object> stuGetInfor(String openid, String base, String type){
		String sql = "SELECT * FROM student WHERE "+base + type+" = ?";
		List<Map<String,Object>> slist = jt.queryForList(sql, new Object[]{openid});
		if (slist != null && slist.size() > 0) {
			Map<String, Object> map = new HashMap<>();
			String sql1 = "update login set LOGINSTATE=? where id=?";
			int rest = jt.update(sql1, new Object[] { "2", slist.get(0).get("LOGIN_ID")});
			String sql2 = "select TOKEN from login where id = ?";
			List<Map<String, Object>> l = jt.queryForList(sql2, new Object[] { slist.get(0).get("LOGIN_ID") });
			if (rest > 0 && l.get(0).get("TOKEN") != null) {
				map.put("token", l.get(0).get("TOKEN").toString());
				map.put("grade", CommUtils.judgeSqlInformation(slist.get(0).get("GRADE")));
				map.put("head", CommUtils.judgeUrl(slist.get(0).get("HEAD")));
				map.put("completeInfo", CommUtils.judgeSqlInformation(slist.get(0).get("COMPLETE_INFO")));
				map.put("nickname", CommUtils.judgeSqlInformation(slist.get(0).get("NICKNAME")));
				map.put("phone", CommUtils.judgeSqlInformation(slist.get(0).get("PHONE")));
				map.put("udid", CommUtils.judgeSqlInformation(slist.get(0).get("UDID")));
				map.put("sex", CommUtils.judgeSqlInformation(slist.get(0).get("SEX")));
				map.put("city", CommUtils.judgeSqlInformation(slist.get(0).get("AREA")));
				map.put("intr", CommUtils.judgeSqlInformation(slist.get(0).get("INTRODUCE")));
				map.put("name", CommUtils.judgeSqlInformation(slist.get(0).get("NAME")));
				map.put("school", CommUtils.judgeSqlInformation(slist.get(0).get("SCHOOL")));
			}
			return map;
		}
		return null;
	}
	private Map<String, Object> teaGetInfor(String openid, String base, String type){
		Map<String, Object> map = new HashMap<>();
		String sql = "SELECT * FROM teacher WHERE "+base + type+" = ?";
		List<Map<String,Object>> tlist = jt.queryForList(sql, new Object[]{openid});
		if (tlist != null && tlist.size() > 0) {
			String sql1 = "update login set LOGINSTATE=? where id=?";
			int rest = jt.update(sql1, new Object[] { "2", tlist.get(0).get("LOGIN_ID")});
			String sql2 = "select TOKEN from login where id = ?";
			List<Map<String, Object>> l = jt.queryForList(sql2, new Object[] { tlist.get(0).get("LOGIN_ID") });
			if (rest > 0 && !l.get(0).get("TOKEN").equals("")) {
				map.put("token", l.get(0).get("TOKEN").toString());
				map.put("head", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				map.put("url", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				map.put("nickname", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME")));
				map.put("name", CommUtils.judgeSqlInformation(tlist.get(0).get("NAME")));
				map.put("acv", CommUtils.judgeSqlInformation(tlist.get(0).get("ACV")));
				map.put("city", CommUtils.judgeSqlInformation(tlist.get(0).get("CITY")));
				map.put("edutime", CommUtils.judgeSqlInformation(tlist.get(0).get("EDU_TIME")));
				map.put("exper", CommUtils.judgeSqlInformation(tlist.get(0).get("EXPER")));
				map.put("features", CommUtils.judgeSqlInformation(tlist.get(0).get("FEATURES")));
				map.put("honor", CommUtils.judgeSqlInformation(tlist.get(0).get("HONOR")));
				map.put("school", CommUtils.judgeSqlInformation(tlist.get(0).get("SCHOOL")));
				map.put("sex", CommUtils.judgeSqlInformation(tlist.get(0).get("SEX")));
				map.put("state", CommUtils.judgeSqlInformation(tlist.get(0).get("STATE")));
				map.put("austate", CommUtils.judgeSqlInformation(tlist.get(0).get("AUSTATE")));
				map.put("grade", CommUtils.judgeSqlInformation(tlist.get(0).get("GRADE")));
				map.put("udid", tlist.get(0).get("UDID"));
				map.put("phone", tlist.get(0).get("PHONE"));
			}
			return map;
		}
		return null;
	}
	
	private void bindingUnionid(String table, String type, String unionid, String openid){
		String sql = "UPDATE "+table+" SET UNIONID_"+type+" = ? WHERE OPENID_"+type+" = ?";
		jt.update(sql, new Object[]{unionid, openid});
	}

}
