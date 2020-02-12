package com.rest.service;

import java.text.ParseException;

/*
 *学生评价接口
 * 
 */
public interface AppraiseService {
	
	//学生提交评价
	public String insertAppraise(String json) throws ParseException;
	
	//回复留言
	public String replyAppraise(String json) throws ParseException;
	//删除留言
	public String delAppraise(String json) throws ParseException;
	
	//学生评价列表
	public String findAppraises(String json) throws ParseException;
	
	//查看评价
	public String selectAppraise(String json) throws ParseException;
	public String selectAppraise2(String json) throws ParseException;
	
	//点评详情(老师)
	public String selectComDetails(String json) throws ParseException;
	
	//教师回复评价
	public String insertAppraiseReply(String json) throws ParseException;
	
}
