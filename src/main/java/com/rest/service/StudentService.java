package com.rest.service;

import java.text.ParseException;

public interface StudentService {
	
	//我的主页
    public String information(String json) throws ParseException;
    
    //ios审核专用接口
    public String iosCheck();
    
    //首页
    public String homePage(String json) throws ParseException;
    public String tuijian(String json) throws ParseException;
    public String homePage2(String json) throws ParseException;
    
    //我的点评
    public String review(String json) throws ParseException;
    
    //我的账单
    public String bills(String json) throws ParseException;
    public String bills2(String json) throws ParseException;
    
    //查看订单详情
    public String billDetails(String json) throws ParseException;
    
    //老师精品服务列表
    public String teacherList(String json) throws ParseException;
    
    //退款售后
    public String reAfter(String json) throws ParseException;
    
}
