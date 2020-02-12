package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface InformationDao {

	// 消息列表
	public List<Map<String, Object>> findInformation(String token, String state, String numPerPage, String currentPage);

	// 未读消息列表
	public List<Map<String, Object>> readInformation(String token, String state, String numPerPage, String currentPage);

	// 删除消息
	public int deleteInformation(List<String> list);

	// 删除消息
	public int unreadStatus(String udid, String state);

	/**  
	* @Title: changeBadge  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param udid    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	public void changeBadge(String udid);
}
