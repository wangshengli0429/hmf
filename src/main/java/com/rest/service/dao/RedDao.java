package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface RedDao {

	//红包
	List<Map<String, Object>> findRedPacket(String udid, String fail ,String numPerPage,String currentPage);
	
	//红包new
	List<Map<String, Object>> findRedPacket2(String udid, String fail ,String numPerPage,String currentPage);

	/**  
	* @Title: findCard  
	* @Description: 月卡  
	* @param @param udid
	* @param @param numPerPage
	* @param @param currentPage
	* @param @return    设定文件  
	* @return List<Map<String,Object>>    返回类型  
	* @throws  
	*/ 
	List<Map<String, Object>> findCard(String udid, String numPerPage,
			String currentPage);

}
