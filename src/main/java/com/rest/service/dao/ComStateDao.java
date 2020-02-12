package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface ComStateDao {

	// 作文状态(学生)
	List<Map<String, Object>> findComStateS(String state, String udid);

	// 作文状态(老师)
	List<Map<String, Object>> findComStateT(String state, String udid, String numPerPage, String currentPage);

	// 作文详情(学生)
	List<Map<String, Object>> findComPart(String cid);
	
	// 作文详情(学生)
	List<Map<String, Object>> recommendCom(String cid);
	
	// 未批改的 作文详情(学生)
	List<Map<String, Object>> findUnCommentComPart(String cid, String udid);

	// 老师list(学生)
	public List<Map<String, Object>> findTeacher(String cid);

	// 老师list(学生)
	public List<Map<String, Object>> findTeacherByCompId(String cid);

	// 作文评分标准list(学生)
	public List<Map<String, Object>> findComStandard(String grade);

	// 获取订单状态的列表
	public List<Map<String, Object>> findOrderStateList(String state, String udid, String currentPage, String numPerPage);

	// 获取订单状态的列表(退款和售后)
	public List<Map<String, Object>> findOrderStateListSaleAfter(String udid, Integer currentPage, Integer numPerPage);

	// 查询订单的状态
	public List<Map<String, Object>> findOrderState(String udid, int cid, int orderId, String out_trade_no);

	// 修改订单的状态
	public int alterOrderState(int state, String udid, int cid, int orderId, String out_trade_no);

	public List<Map<String, Object>> findRefundOrderInfo(String stuUdid, int orderId);

	// 修改退款订单的信息
	public int updateRefundOrderInfo(int state, String stuUdid, int orderID, int teaId, String title);

	// 增加退款订单的信息
	public int addRefundOrderInfo(String udid, int orderID);

	//修改作文回显
	public Map<String, String> updateCompositionShow(String cid);

	//删除作文
	int deleteComposition(String udid, String cid);

	//更换老师
	int changeTeacher(String cid, String newTid, String oldTid, String udid, String oldTname, String title);

	//作文状态草稿
	List<Map<String, Object>> findCompositionDraft(String udid,
			String numPerPage, String currentPage);

}
