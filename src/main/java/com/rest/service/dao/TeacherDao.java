package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface TeacherDao {

	//老师简介（学生）
	List<Map<String, Object>> findTintroduc(String tid, String udid);

	//老师列表（学生）
	List<Map<String, Object>> findTlist(String grade, String tname,String numPerPage,String currentPage);
	
	//我的主页
	public Map findInformation(String udid);
	//我的主页
	public Map findInformation2(String udid);
	
	//我的本月账单
	public List<Map<String, Object>> findBills(String udid,String numPerPage,String currentPage);
	
	//我的上月账单
	public List<Map<String, Object>> findLastBills(String udid,String numPerPage,String currentPage);

	//退款作文
	public List<Map<String, Object>> findRefunds(String udid,String numPerPage,String currentPage);
	
	//更改最后登录的平台信息
	public void changeDevice(String phone, String deviceType);

	//点评案例列表
	public List<Map<String, Object>> compositionList(String currentPage, String numPerPage);
	
	//未选择老师作文列表
	List<Map<String, Object>> compositionListNoTeacher(String currentPage, String numPerPage);
	
	//老师排序，异步请求 --------一次性
	void sortTeacher();
	
	//计算响应时间-------一次性
	void jisuan();

	//抢单
	int grabComposition(String cid, String udid);

	//取消抢单
	boolean unGrabComposition(String udid, String cid);

	//点评状态-PC端是否点评
	boolean compositionState(String udid, String cid);

}
