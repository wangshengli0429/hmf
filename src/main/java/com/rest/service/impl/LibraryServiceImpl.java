package com.rest.service.impl;

import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;

import org.apache.http.util.TextUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.rest.service.dao.LibraryDao;
import com.util.CommUtils;
@Component
public class LibraryServiceImpl {

	private LibraryDao libraryDao;

	public LibraryDao getLibraryDao() {
		return libraryDao;
	}

	public void setLibraryDao(LibraryDao libraryDao) {
		this.libraryDao = libraryDao;
	}

	@POST
	@Path("/searchCompList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCompListByTag(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String code = str1.getString("code");// 033001
			String grade = str1.getString("grade");// ""
			// String category = str1.getString("category");
			// String udid = str1.getString("udid");
			String codeType = str1.getString("codeType");// 033
			String cond = str1.getString("cond");
			String type = str1.getString("type");// 026
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			// 分词参数格式：上次搜索使用的关键字-搜索关键字-其他需要考虑的搜索条件-已经查询出来的id
			String ikanalyzer = str1.getString("ikanalyzer");
			boolean b = true;
			if (!ikanalyzer.equals("")) {
				b = false;
			}
			// 分页参数初始化赋值
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			// 将分页参数转化成直接的查询起始数，在查询数据库时直接使用limit currentNum，numPerPage
			String currentNum = (Integer.parseInt(currentPage) - 1)* Integer.parseInt(numPerPage) + "";
			/*// 分词参数初始化赋值
			if (ikanalyzer == null || ikanalyzer.equals("")) {
				ikanalyzer = cond + "-" + currentNum;
			}*/
			// 将年级、范文、技法、素材先分类存储,封装搜索条件
			Map<String, String> map2 = before(type, code, grade, codeType);
			// 存储查询结果
			List<Map<String, Object>> list = new ArrayList<>();
			// 创建id集合，存储已经查出来的作文id，用于对搜索关键字进行分词查找时，排除掉已经查询过的结果
			List<Integer> ikanalyzerId = new ArrayList<>();

			// 对分词参数切分
			String[] sts = ikanalyzer.split("-");
			// 分词后继续查找的关键字
			String ikanalyzerValue = cond;
			//记录最初搜索使用的关键字
			String firstCond = "";
			String searchBy = "";
			if (sts.length > 3) {
				ikanalyzerValue = sts[0];
				firstCond = sts[1];
				searchBy = sts[2];
			}
			// 分词后继续上次查找的位置
			//String ikanalyzerCurrentNum = "0";
			// 其他搜索条件
			String newCode = "";
			String newGrade = "";
			String newCodeType = "";
			String newType = "";
			if (sts.length > 5) {
				newCode = sts[3].substring(4, sts[3].length());
				newGrade = sts[4].substring(5, sts[4].length());
				newCodeType = sts[5].substring(8, sts[5].length());
				newType = sts[6].substring(4, sts[6].length());
			}

			// 判断传回的分词参数中是否包含已经查询过的id，如果存在id值，代表这些id已经被查出，添加到集合中
			if (sts.length > 7) {
				for (int i = 7; i < sts.length; i++) {
					ikanalyzerId.add(Integer.parseInt(sts[i]));
				}
			}
			// 对搜索关键字进行分词
			List<String> condList = getCondList(cond);

			// 如果有搜索条件，且搜索条件没有改变，并且查找的是范文类型，才进行分词
			boolean b1 = (cond != null && !cond.equals(""));
			boolean b2 = type.equals("013");
			boolean b3 = newCode.equals(code);
			boolean b4 = newGrade.equals(grade);
			boolean b5 = newCodeType.equals(codeType);
			boolean b6 = newType.equals(type);
			
			//查询分几种情况：
			//1.搜索技巧、素材，搜索没有关键字的范文查询
			//2.有关键字的范文搜索：1）第一次查询
			//2.有关键字的范文搜索：2）第二次查询，带有上次查询的分词信息
			//2.有关键字的范文搜索：3）改变搜索条件的第一次查询
			
			//1.搜索技巧、素材，type不是013
			if (!b2) {
				List<Map<String, Object>> list2 = getListByTitle(type, cond, currentNum, numPerPage, map2,
						null);
				list.addAll(list2);
				ikanalyzer = "";
			}
			
			//2.搜索范文，type是013，但是搜索关键字为空
			if (b2 && !b1) {
				List<Map<String, Object>> list2 = getListByTitle(type, cond, currentNum, numPerPage, map2,
						null);
				list.addAll(list2);
				ikanalyzer = "";
			}
			//3.搜索范文，type是013，搜索关键字不为空
			if (b2 && b1) {
				//1）第一次查询，分词信息为空
				if (b) {
					Map<String, String> map = firstSearchByTitle(type, cond, "0", numPerPage, map2, ikanalyzerId, list, condList);
					ikanalyzerValue = map.get("ikanalyzerValue");
					searchBy = map.get("searchBy");
					if (list.size() < Integer.parseInt(numPerPage)) {
						int count = Integer.parseInt(numPerPage) - list.size();
						Map<String, String> map3 = firstSearchByContent(type, cond, "0", count + "", map2, ikanalyzerId, list, condList);
						ikanalyzerValue = map3.get("ikanalyzerValue");
						searchBy = map3.get("searchBy");
					}
				}
				//2）第二次查询，带有上次查询的分词信息
				//传回来的分词信息不为空，代表第二次查询，再判断搜索条件有没有改变
				if (!b && b3 && b4 && b5 && b6 && cond.equals(sts[1])) {
					// a.如果传回来的关键字和分词中的关键字可以进行匹配，代表还没有进行分词，直接继续进行查询即可
					if (sts[0].equals(cond) && sts[2].equals("title")) {
						Map<String, String> map = firstSearchByTitle(type, cond, "0", numPerPage, map2, ikanalyzerId, list, condList);
						ikanalyzerValue = map.get("ikanalyzerValue");
						searchBy = map.get("searchBy");
						if (list.size() < Integer.parseInt(numPerPage)) {
							int count = Integer.parseInt(numPerPage) - list.size();
							Map<String, String> map3 = firstSearchByContent(type, cond, "0", count + "", map2, ikanalyzerId, list, condList);
							ikanalyzerValue = map3.get("ikanalyzerValue");
							searchBy = map3.get("searchBy");
						}
					}
					//b.已经进行分词查询了，继续查询即可
					if (!sts[0].equals(cond) && sts[2].equals("title")) {
						Map<String, String> map = continueSearchByTitle(ikanalyzerValue, list, ikanalyzerId, numPerPage, condList, type, map2);
						ikanalyzerValue = map.get("ikanalyzerValue");
						searchBy = map.get("searchBy");
						if (list.size() < Integer.parseInt(numPerPage)) {
							int count = Integer.parseInt(numPerPage) - list.size();
							Map<String, String> map3 = firstSearchByContent(type, cond, "0", count + "", map2, ikanalyzerId, list, condList);
							ikanalyzerValue = map3.get("ikanalyzerValue");
							searchBy = map3.get("searchBy");
						}
					}
					//c.
					if (sts[0].equals(cond) && sts[2].equals("content")) {
						Map<String, String> map = firstSearchByContent(type, cond, "0", numPerPage, map2, ikanalyzerId, list, condList);
						ikanalyzerValue = map.get("ikanalyzerValue");
						searchBy = map.get("searchBy");
					}
					//d.
					if (!sts[0].equals(cond) && sts[2].equals("content")) {
						Map<String, String> map = continueSearchByContent(ikanalyzerValue, list, ikanalyzerId, numPerPage, condList, type, map2);
						ikanalyzerValue = map.get("ikanalyzerValue");
						searchBy = map.get("searchBy");
					}
					
				}
				
				//3）改变搜索条件的第一次查询，或者搜索关键字已经改变
				if (!b && (!b3 || !b4 || !b5 || !b6 || !cond.equals(sts[1]))){
					//改变分词了，把以前的id清空，分词参数清空
					ikanalyzerId.clear();
					Map<String, String> map = firstSearchByTitle(type, cond, "0", numPerPage, map2, ikanalyzerId, list, condList);
					ikanalyzerValue = map.get("ikanalyzerValue");
					searchBy = map.get("searchBy");
					if (list.size() < Integer.parseInt(numPerPage)) {
						int count = Integer.parseInt(numPerPage) - list.size();
						Map<String, String> map3 = firstSearchByContent(type, cond, "0", count + "", map2, ikanalyzerId, list, condList);
						ikanalyzerValue = map3.get("ikanalyzerValue");
						searchBy = map3.get("searchBy");
					}
				}

				// 全部查询完毕后，改变分词参数
				StringBuilder newIkanalyzerBuilder = new StringBuilder();
				newIkanalyzerBuilder.append(ikanalyzerValue).append("-");
				newIkanalyzerBuilder.append(cond).append("-");
				newIkanalyzerBuilder.append(searchBy).append("-");
				newIkanalyzerBuilder.append("code").append(code).append("-");
				newIkanalyzerBuilder.append("grade").append(grade).append("-");
				newIkanalyzerBuilder.append("codeType").append(codeType).append("-");
				newIkanalyzerBuilder.append("type").append(type).append("-");
				if (ikanalyzerId != null && ikanalyzerId.size() > 0) {
					for (Integer integer : ikanalyzerId) {
						newIkanalyzerBuilder.append(integer).append("-");
					}
				}
				ikanalyzer = newIkanalyzerBuilder.toString();
			}
			
			if (list != null && list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);

				String returnStr = "{\"BM\":{\"compList\":" + jsonArray.toString()
						+ ",\"ikanalyzer\":\"" + ikanalyzer + "\",\"codeType\":\"" + codeType
						+ "\"},\"EC\":\"0\",\"EM\":\"\"}";
				System.out.println(returnStr);
				return returnStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10007,\"EM\":\"参数错误\"}";
		}
		return "{\"BM\":{},\"EC\":10997,\"EM\":\"查询列表为空\"}";
		// String tableFrom = request.getParameter("tf");// 所属表
		// String cty = request.getParameter("cty");
		// String condition = request.getParameter("condition");
		// String grade = request.getParameter("grade");
		// String year = request.getParameter("year");
		// try {
		// if (condition != null) {
		// condition = new String(condition.getBytes("iso-8859-1"), "utf-8");//
		// 乱码控制
		// }
		// if (cty != null) {
		// cty = new String(cty.getBytes("iso-8859-1"), "utf-8");
		// }
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
	
	//从0根据标题查询
	public Map<String, String> firstSearchByTitle(String type, String cond, String currentNum,
			String numPerPage, Map<String, String> param,
			List<Integer> ikanalyzerId, List<Map<String, Object>> list, List<String> condList){
		
		String ikanalyzerValue = cond;
		String searchBy = "title";
		addListByTitle(type, cond, currentNum, numPerPage, param, ikanalyzerId, list);
		//原始的关键字查询出的结果数量不满足要求时，使用分词查询
		if (list.size() < Integer.parseInt(numPerPage) 
				&& condList.size() > 0 && !condList.get(0).equals(cond)) {
			// 使用分词后进行查询
			for (String string : condList) {
				if (list.size() < Integer.parseInt(numPerPage)) {
					// 结果集中缺少几个元素，就查询几个元素，开始查询位置为0（limit 0，count）
					int count = Integer.parseInt(numPerPage)
							- list.size();
					addListByTitle(type, string, "0", count + "", param, ikanalyzerId, list);
					// 查询完成后改变分词参数，将最后一次查询使用的词语存储在分词参数中，最后一次查询出来的个数存储也进行存储，便于下次继续查询
					ikanalyzerValue = string;
				}
			}
		}
		Map<String, String> map = new HashMap<>();
		map.put("ikanalyzerValue", ikanalyzerValue);
		map.put("searchBy", searchBy);
		return map;
	} 
	
	//从0根据内容查询
	public Map<String, String> firstSearchByContent(String type, String cond, String currentNum,
			String numPerPage, Map<String, String> param,
			List<Integer> ikanalyzerId, List<Map<String, Object>> list, List<String> condList){
		
		String ikanalyzerValue = cond;
		String searchBy = "content";
		addListByContent(type, cond, currentNum, numPerPage, param, ikanalyzerId, list);
		//原始的关键字查询出的结果数量不满足要求时，使用分词查询
		if (list.size() < Integer.parseInt(numPerPage) 
				&& condList.size() > 0 && !condList.get(0).equals(cond)) {
			// 使用分词后进行查询
			for (String string : condList) {
				if (list.size() < Integer.parseInt(numPerPage)) {
					// 结果集中缺少几个元素，就查询几个元素，开始查询位置为0（limit 0，count）
					int count = Integer.parseInt(numPerPage)
							- list.size();
					addListByContent(type, string, "0", count + "", param, ikanalyzerId, list);
					// 查询完成后改变分词参数，将最后一次查询使用的词语存储在分词参数中，最后一次查询出来的个数存储也进行存储，便于下次继续查询
					ikanalyzerValue = string;
				}
			}
		}
		Map<String, String> map = new HashMap<>();
		map.put("ikanalyzerValue", ikanalyzerValue);
		map.put("searchBy", searchBy);
		return map;
	} 
		
	
	
	
	//继续根据标题查询
	public Map<String, String> continueSearchByTitle(String ikanalyzerValue,
			List<Map<String, Object>> list,
			List<Integer> ikanalyzerId, String numPerPage,
			List<String> condList, String type, Map<String, String> map2) {

		int nowSize = list.size();
		// 进行关键字分词后查询，存储
		List<Map<String, Object>> newList = getListByTitle(type, ikanalyzerValue,
				"0", numPerPage, map2, ikanalyzerId);
		for (Map<String, Object> map : newList) {
			if (!list.contains(map)) {
				list.add(map);
				Integer i = (Integer) map.get("cid");
				ikanalyzerId.add(i);
			}
		}
		// 如果此次分词的查询结果数量不满足要求，切换到下一次分词
		if (newList.size() < Integer.parseInt(numPerPage) - nowSize) {
			for (int i = 0; i < condList.size(); i++) {
				if (ikanalyzerValue.equals(condList.get(i))
						&& i != condList.size() - 1) {
					ikanalyzerValue = condList.get(i + 1);
				}
			}
		}
		// 创建集合，方法结束后返回需要的参数
		Map<String, String> map = new HashMap<>();
		map.put("ikanalyzerValue", ikanalyzerValue);
		map.put("searchBy", "title");
		if (list.size() < Integer.parseInt(numPerPage)) {
			// 当结果集数量不满足要求时进行判断，如果最后一次用的已经是分词的最后一个单词，代表所有查询的结果都已经查出，返回结果集
			if (condList.get(condList.size() - 1).equals(ikanalyzerValue)) {
				return map;
			} else {
				// 如果最后一次用的分词不是最后一个单词，递归继续查找，知道查找出所有结果
				return continueSearchByTitle(ikanalyzerValue, 
						list, ikanalyzerId, (Integer.parseInt(numPerPage) - list.size()) + "", condList, type, map2);
			}
		}
		return map;
	}
	//继续根据内容查询
	public Map<String, String> continueSearchByContent(String ikanalyzerValue,
				List<Map<String, Object>> list,
				List<Integer> ikanalyzerId, String numPerPage,
				List<String> condList, String type, Map<String, String> map2) {

		int nowSize = list.size();
		// 进行关键字分词后查询，存储
		List<Map<String, Object>> newList = getListByContent(type, ikanalyzerValue,
				"0", numPerPage, map2, ikanalyzerId);
		for (Map<String, Object> map : newList) {
			if (!list.contains(map)) {
				list.add(map);
				Integer i = (Integer) map.get("cid");
				ikanalyzerId.add(i);
			}
		}
		// 如果此次分词的查询结果数量不满足要求，切换到下一次分词
		if (newList.size() < Integer.parseInt(numPerPage) - nowSize) {
			for (int i = 0; i < condList.size(); i++) {
				if (ikanalyzerValue.equals(condList.get(i))
						&& i != condList.size() - 1) {
					ikanalyzerValue = condList.get(i + 1);
				}
			}
		}
		// 创建集合，方法结束后返回需要的参数
		Map<String, String> map = new HashMap<>();
		map.put("ikanalyzerValue", ikanalyzerValue);
		map.put("searchBy", "content");
		if (list.size() < Integer.parseInt(numPerPage)) {
			// 当结果集数量不满足要求时进行判断，如果最后一次用的已经是分词的最后一个单词，代表所有查询的结果都已经查出，返回结果集
			if (condList.get(condList.size() - 1).equals(ikanalyzerValue)) {
				return map;
			} else {
				// 如果最后一次用的分词不是最后一个单词，递归继续查找，知道查找出所有结果
				return continueSearchByContent(ikanalyzerValue, 
						list, ikanalyzerId, (Integer.parseInt(numPerPage) - list.size()) + "" , condList, type, map2);
			}
		}
		return map;
	}
	/*
	 * 根据标题查询数据添加进集合中
	 */
	public void addListByTitle(String type, String cond, String from, String numPerPage, 
			Map<String, String> param, List<Integer> ikanalyzerId, List<Map<String, Object>> list){
		
		List<Map<String, Object>> newList = getListByTitle(type, cond,
				from, numPerPage, param, ikanalyzerId);
		for (Map<String, Object> map : newList) {
			if (!list.contains(map)) {
				list.add(map);
				Integer i = (Integer) map.get("cid");
				ikanalyzerId.add(i);
			}
		}
	}
	/*
	 * 根据内容查询数据添加进集合中
	 */
	public void addListByContent(String type, String cond, String from, String numPerPage,
			Map<String, String> param, List<Integer> ikanalyzerId, List<Map<String, Object>> list){
		List<Map<String, Object>> newList = getListByContent(type, cond,
				from, numPerPage, param, ikanalyzerId);
		for (Map<String, Object> map : newList) {
			if (!list.contains(map)) {
				list.add(map);
				Integer i = (Integer) map.get("cid");
				ikanalyzerId.add(i);
			}
		}
	}
	/*
	 * 分词
	 */
	public List<String> getCondList(String str) throws Exception {
		StringReader reader = new StringReader(str);
		IKSegmenter ik = new IKSegmenter(reader, true);// 当为true时，分词器进行最大词长切分
		Lexeme lexeme = null;
		List<String> strList = new ArrayList<>();
		while ((lexeme = ik.next()) != null) {
			String string = lexeme.getLexemeText();
			strList.add(string);
		}
		return strList;
	}
	/*
	 * 前置判断搜索类型和范围
	 */
	public Map<String, String> before(String type, String code, String grade,
			String codeType) {
		Map<String, String> param = new HashMap<String, String>();
		// type 024 素材类型 026 技法类型 013 范文类型
		if ("024".equals(type) || "026".equals(type)) {
			if ("dnj".equals(grade)) {
				param.put("AGE_SCALE", "014001");
			} else if ("xx".equals(grade)) {
				param.put("AGE_SCALE", "014002");
			} else if ("cz".equals(grade)) {
				param.put("AGE_SCALE", "014003");
			} else if ("gz".equals(grade)) {
				param.put("AGE_SCALE", "014004");
			}
		}
		if ("013".equals(type) && !TextUtils.isEmpty(grade)) {
			Object[] objects = CommUtils.parseAge(grade);
			if (objects != null && objects.length > 1) {
				param.put("AGE_DETAIL", (String) objects[1]);
				//param.put("age_sclae", (String) objects[1]);
			}
		}
		if ("024".equals(codeType)) {// 素材分类里的 C_TYPE
			param.put("C_TYPE", code);
		}
		if ("013".equals(codeType)) {// 范文分类里的 STYLE
			param.put("STYLE", code);
		}
		if ("033".equals(codeType) && "013".equals(type)) {// 范文分类里的 TYPE
			param.put("TYPE", code);
		}
		if ("032".equals(codeType)) {// 技法分类里的 TYPE
			param.put("TYPE", code);
		}
		if ("033".equals(codeType) && "026".equals(type)) {// 技法分类里的 STYLE
			param.put("STYLE", code);
		}
		return param;
	}
	/*
	 * 根据标题去数据库查找
	 */
	public List<Map<String, Object>> getListByTitle(String type, String cond,
			String currentPage, String numPerPage, Map<String, String> param,
			List<Integer> ikanalyzerId) {
		List<Map<String, Object>> list2 = libraryDao.getDataByTitle(cond,
				getTableFrom(type), param, currentPage, numPerPage,
				ikanalyzerId);
		if (list2 == null) {
			return new ArrayList<>();
		}
		return list2;
	}
	/*
	 * 根据内容和关键字去数据库查找
	 */
	public List<Map<String, Object>> getListByContent(String type, String cond,
			String currentPage, String numPerPage, Map<String, String> param,
			List<Integer> ikanalyzerId) {
		List<Map<String, Object>> list2 = libraryDao.getDataByContent(cond,
				getTableFrom(type), param, currentPage, numPerPage,
				ikanalyzerId);
		if (list2 == null) {
			return new ArrayList<>();
		}
		return list2;
	}
	/*
	 * 体裁判断
	 */
	public String seaTy(String mark) {
		// 体裁类型
		if ("ti".equals(mark)) {
			return "STYLE";
			// 作文类型
		} else if ("zt".equals(mark)) {
			return "C_TYPE";
			// 详细分类(作文表)
		} else if ("xf".equals(mark)) {
			return "TYPE";
			// 详细分类(素材表)
		} else if ("xfsc".equals(mark)) {
			return "STYLE";
			// 内容类型
		} else if ("nr".equals(mark)) {
			return "CT_TYPR";
			// 素材类型
		} else if ("tisc".equals(mark)) {
			return "C_TYPE";
			// 年级
		} else if ("gnj".equals(mark)) {
			return "AGE_DETAIL";
			// 技法体裁类型
		} else if ("tcjf".equals(mark)) {
			return "TYPE";
			// 命题类型
		} else if ("mtlx".equals(mark)) {
			return "E_TYPE";
			// 考区
		} else if ("kq".equals(mark)) {
			return "C_REGION";
			// 颜色分类
		} else if ("ys".equals(mark)) {
			return "COLOR_TYPE";
			// 图片分类
		} else if ("fl".equals(mark)) {
			return "SCENE_TYPE";
			// 年级分类
		} else if ("ye".equals(mark)) {
			return "C_YEAR";
		} else {
			return "";
		}
	}
	/*
	 * 类型判断
	 */
	private String getTableFrom(String type) {
		// 024 素材类型 026 技法类型 013 范文类型
		if ("013".equals(type)) {
			return "composion";
		}
		if ("024".equals(type)) {
			return "sc";
		}
		if ("026".equals(type)) {
			return "jf";
		}
		return "composion";
	}
}
