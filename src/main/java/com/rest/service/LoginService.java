package com.rest.service;

import java.text.ParseException;

/*
 *注册模块接口
 * 
 */
public interface LoginService {
	
	//学生登录
    public String slogin( String json) throws ParseException;
    
    //老师登录
    public String tlogin( String json) throws ParseException;
    
    //登出
    public String logout( String json) throws ParseException;
    
    //微信qq登录
    public String thirdAccountLogin(String json) throws ParseException;
    
    //微信qq登录,判断手机号是否存在，存在绑定
    public String thirdAccountLoginBinding(String json) throws ParseException;
    
    //已登录，绑定微信qq账号
    public String thirdAccountBinding(String json) throws ParseException;
    
    //已登录，解绑微信qq账号
    public String thirdAccountUnBinding(String json) throws ParseException;
    
}