package com.rest.service;

import java.text.ParseException;

public interface TeacherService {
	
	//老师简介（学生）
	public String findTintroduc(String json) throws ParseException;
	
	//老师列表（学生）
	public String findTlist(String json) throws ParseException;
	
	//我的主页
    public String information(String json) throws ParseException;
    
    //我的账单
    public String bills(String json) throws ParseException;
    
    //退款作文
    public String refunds(String json) throws ParseException;
    
    //老师排序，异步请求
    public void sortTeacher();
    
    //点评案例列表
    public String compositionList(String json);
    
    //未选择老师作文列表
    public String compositionListNoTeacher(String json)throws ParseException;
    
    //老师抢单进行语音点评
    public String grabComposition(String json)throws ParseException;
    
    //老师抢单取消语音点评
    public String unGrabComposition(String json)throws ParseException;

    //点评状态-PC端是否点评
    public String compositionState(String json)throws ParseException;
    
}
