package com.rest.service.dao;

import java.util.List;
import java.util.Map;

import com.entity.Appraise;

public interface AppraiseDao {

	//学生提交评价
	int insertAppraise(Appraise a);
	//教师提交回复
	int insertReply(Appraise a);
	
	//回复留言
	int insertReply(String aid, String message, String cid, String userType);
	
	//修改该作文对应订单的一个评论状态
	int alterOrderCommentStatus(int compId);

	//学生评价列表
	List<Map<String, Object>> findAppraises(String udidm,String numPerPage,String currentPage);

	//查看评价
	List<Map<String, Object>> selectAppraise(int compid, String udid);
	
	//查看评价
	List<Map<String, Object>> queryAppraiseIdByCompId1(int compid);
	List<Map<String, Object>> queryAppraiseIdByCompId2(int compid);

	//点评详情(老师)
	Map<String, Object> selectComDetails(int compid, String udid);
	
	//删除留言
	int delAppraise(String aid, String userType);

}
