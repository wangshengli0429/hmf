package com.rest.service.dao;

import java.util.List;
import java.util.Map;

import com.entity.BaseSqlResultBean;
import com.entity.Composition;
import com.entity.Teacher;

public interface UploadDao {

	// 上传作文
	BaseSqlResultBean insertComposi(Composition comp);

	// 上传师资认证
	int insertAuthent(Teacher tea);

	List<Map<String, Object>> getTeacherAuthInfo(String udid);

	/**  
	* @Title: updateComposi  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param comp
	* @param @return    设定文件  
	* @return BaseSqlResultBean    返回类型  
	* @throws  
	*/ 
	int updateComposi(Composition comp);
	int updateComposi2(Composition comp);
	/**
	 * @param recordTime 
	 * @param suggest 
	 * @param score   
	* @Title: insertCom_compositionBySpeech  
	* @Description: 点评插入语音 
	* @param @param cid
	* @param @param voice    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	int insertCom_compositionBySpeech(String cid, String voice, String score, String suggest, String recordTime);

	/**  
	* @Title: insertInformation  
	* @Description: 新流程插入作文后插入消息  
	* @param @param udid
	* @param @param tid    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	void insertInformation(String udid, String tid);

	/**
	 * 如果上传作文时年级为空，调用方法来补全年级  
	*/ 
	String findGradeByUdid(String udid);
	//上传批改图片热区
	int insertComPic(String data, String comPicUrl, String imageIndex, String compId, int oldwidth, int oldheight, int newwidth, int newheight);

	//查询是否已暂存批改
	int selectCom_composition(String udid, String cid);
	
	//修改点评内容
	int updateCom_compositionBySpeech(String cid, String voice, String score, String suggest, String recordTime);

}
