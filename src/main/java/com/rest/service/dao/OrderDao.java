package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface OrderDao {

	// 生成订单
	public int InsterOrder(Map map);
	public int InsterOrder2(Map map);

	public int FindOrder(Map map);

	public int AddCompToOrder(Map<String, Object> map, String tid);

	public int findOrderByOutTradeNo(String outTradeNo);

	/**  
	* @Title: findTeacherIdByOrder  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param tid
	* @param @return    设定文件  
	* @return String    返回类型  
	* @throws  
	*/ 
	public List<String> findTeacherIdByOrder(String tid);

	/**
	 * @param tid 
	 * @param draft 
	 * @param title   
	* @Title: InsterOrderByCard  
	* @Description: 创建订单使用月卡
	* @param @param udid
	* @param @param cid    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	int InsterOrderByCard(String cid, String udid, String tid);
	
	/**
	 * 检查作文id是否已经生成订单
	*/
	public int findCompositionHasPay(String cid);
}
