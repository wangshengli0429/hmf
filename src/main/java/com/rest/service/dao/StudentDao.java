package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface StudentDao {

	
		//我的主页
		public Map<String, Object>  findInformation(String udid);
		
		//首页
		public Map<String, Object>  findHomePage();
		public Map<String, Object> findHomePage2();
		
		//我的评价列表
		public List<Map<String, Object>> findAppraise(String udid);
		
		//我的本月账单
		public List<Map<String, Object>> findBills(String udid,String numPerPage,String currentPage);
		public List<Map<String, Object>> findBills2(String udid,String numPerPage,String currentPage);
		
		//查看订单详情
		public Map<String, Object> billDetails(String id,String type);
		
		//我的上月账单
		public List<Map<String, Object>> findLastBills(String udid);
		
		//老师list
		public List<Map<String, Object>> findTeacher(String pid,String numPerPage,String currentPage);
		
		public List<Map<String, Object>> findreAfter(String pid,String numPerPage,String currentPage);
		
		//ios测试环境
		public boolean checkIOS();

		//获取推荐作文新接口
		public List<Map<String, Object>> getTuijianCom(String grade);
		
}
