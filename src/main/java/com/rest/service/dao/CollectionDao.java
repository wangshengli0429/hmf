package com.rest.service.dao;

import java.util.List;
import java.util.Map;

import com.entity.Collection;

public interface CollectionDao {

	//加入收藏
	int insertCollection(Collection c, String token);
	
	//加入收藏
	int insertCollectionTeacher(Collection c, String token);

	//取消收藏
	int deleteCollection(Collection c, String token);

	//收藏作文列表
	List<Map<String, Object>> findCollection(String udid,String numPerPage,String currentPage);
	
	//收藏总数
	public String countCollection(String token);
	
	//取消收藏列表
	int deleteCollections(List<String> list, String udid, String type);

	//收藏老师列表 
	List<Map<String, Object>> findTlist(String udid, String numPerPage,
			String currentPage);

	//取消收藏
	int deleteCollectionTeacher(String sign, String udid);

}
