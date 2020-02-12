package com.rest.service.dao;

import java.util.Map;


public interface LoginDao {
	
	  
    //学生登录
    public Map findStudent(String phone,String password);
    
    //老师登录
    public Map findTeacher(String phone,String password);
	//老师登录2
    public Map findTeacher2(String phone, String password);
    
    //登出
	public int logout(String phone, String logintype);

	public String findToken(String phone);

	public int findStudent(String phone);

	public int findTeacher(String phone);
	
	//微信openid查找用户是否存在
	public Map<String, Object> findStuByOpenid_msg(String openid, String unionid);
	
	//qq openid查找用户是否存在
	public Map<String, Object> findStuByOpenid_qq(String openid, String unionid);
	
	//微信openid查找用户是否存在
	public Map<String, Object> findTeaByOpenid_msg(String openid, String unionid);
	
	//qq openid查找用户是否存在
	public Map<String, Object> findTeaByOpenid_qq(String openid, String unionid);

	//是否已有账户你，没有则绑定
	public Map<String, Object> stu_binding_qq_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName);
	public Map<String, Object> stu_binding_msg_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName);
	public Map<String, Object> tea_binding_qq_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName);
	public Map<String, Object> tea_binding_msg_openid_phone(String unionid, String openid, String phone, String thirdAccountHead, String thirdAccountName);

	//已登录状态绑定账户
	public int stu_binding_qq_name(String udid, String openid, String qqName, String thirdAccountHead, String unionid);
	public int stu_binding_msg_name(String udid, String openid, String msgName, String thirdAccountHead, String unionid);
	public int tea_binding_qq_name(String udid, String openid, String qqName, String thirdAccountHead, String unionid);
	public int tea_binding_msg_name(String udid, String openid, String msgName, String thirdAccountHead, String unionid);
	
	//已登录状态解绑
	public int stu_unbinding_qq(String udid);
	public int stu_unbinding_msg(String udid);
	public int tea_unbinding_qq(String udid);
	public int tea_unbinding_msg(String udid);

}
