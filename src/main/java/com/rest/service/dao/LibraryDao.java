package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface LibraryDao {

	public List<Map<String, Object>> getDataByTitle(String cond, String tableFrom, Map<String, String> param, String pageIndex, String pageNum, List<Integer> ikanalyzerId );
	public List<Map<String, Object>> getDataByContent(String cond, String tableFrom, Map<String, String> param, String pageIndex, String pageNum, List<Integer> ikanalyzerId );
}
