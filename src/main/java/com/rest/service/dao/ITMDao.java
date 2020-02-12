package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface ITMDao {

	// 范文
	public Map<String, Object> findModel(String dist, String sign, String udid, String codeType);

	// 素材
	public Map<String, Object> findMaterial(String dist, String sign, String udid);

	// 技法
	public Map<String, Object> findTechnical(String dist, String sign, String udid, String codeType);

	// 推荐作文
	public Map<String, Object> findRecomm(String dist, String sign, String udid);

	// 范文list
	public List<Map<String, Object>> findModelList(String class1, String subject, String numPerPage, String currentPage);

	// 素材list
	public List<Map<String, Object>> findMaterialList(String class1, String grade, String numPerPage, String currentPage);

	// 技法list
	public List<Map<String, Object>> findTechniqueList(String type, String grade, String numPerPage, String currentPage);

}
