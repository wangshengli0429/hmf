package com.rest.service;

import java.text.ParseException;

/*
 *收藏接口
 * 
 */
public interface CollectionService {
	
	//加入收藏
    public String insertCollection(String json) throws ParseException;
    
    //加入收藏老师
    public String icollectionTeacher(String json) throws ParseException;
    
    //取消收藏
    public String deleteCollection(String json) throws ParseException;
    
    //取消收藏老师
    public String dcollectionTeacher(String json) throws ParseException;
    
    //编辑收藏列表
    public String deleteCollectionArray(String json) throws ParseException;
    
    //收藏作文列表
    public String findCollection(String json) throws ParseException;

	
}
