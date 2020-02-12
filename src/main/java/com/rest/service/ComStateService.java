package com.rest.service;

import java.text.ParseException;

/*
 *作文状态接口
 * 
 */
public interface ComStateService {

	// 作文状态(学生)
	public String comStateS(String json) throws ParseException;
	// 作文状态(学生)草稿箱
	public String comStateSDraft(String json) throws ParseException;

	// 作文状态(老师)
	public String comStateT(String json) throws ParseException;
	
	// 申请退款
	public String requestBack(String json) throws ParseException;
	
	// 取消申请退款
	public String cancelBack(String json) throws ParseException;

	// 批改作文详情(学生)
	public String comPart(String json) throws ParseException;
	
	// 未批改作文详情(学生)
	public String unCommentCompDetail(String json) throws ParseException;

	// 作文状态(学生)
	public String comBackAndAfter(String json) throws ParseException;
	
	//修改待点评作文
	public String updateCompositionShow(String json);
	
	//待点评作文删除
	public String deleteComposition(String json);
	
	//待点评作文更换老师
	public String changeTeacher(String json);

}
