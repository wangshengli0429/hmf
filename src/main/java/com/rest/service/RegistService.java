package com.rest.service;

import java.text.ParseException;


/*
 *注册登录接口
 * 
 */
public interface RegistService {
	
	//学生注册
    public String sregister(String json) throws ParseException;
    
    //老师注册
    public String tregister(String json) throws ParseException;
    

}
