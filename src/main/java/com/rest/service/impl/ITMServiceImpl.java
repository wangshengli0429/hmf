package com.rest.service.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.rest.service.ITMService;
import com.rest.service.dao.AppraiseDao;
import com.rest.service.dao.ComStateDao;
import com.rest.service.dao.ITMDao;
import com.util.CommUtils;
import org.springframework.stereotype.Component;
@Component
@Path("/")
public class ITMServiceImpl implements ITMService {
	
	private static Logger logger = Logger.getLogger(ITMServiceImpl.class);
	
	@POST
	@Path("detail")
	@Produces(MediaType.APPLICATION_JSON)
	public String detail(String json) throws ParseException {
		
		//{"BM":{"dist":"013","sign":"93862",
		//"token":"47A9DC8C83DDB69AF68CEF84F468EB5F9B0704CA",
		//"udid":"4C61912AF50A26FA64B4BA21208CEDCFFAA96CF1"}}
		
		// 技法、范文、素材详细
		Map<String, Object> rest = new HashMap<String, Object>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String dist = str1.getString("dist");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			String sign = str1.getString("sign");
			String codeType = str1.getString("codeType");
			String sourceCompId = null;
			try {
				sourceCompId = str1.getString("sourceCompId");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			//logger.info("dist" + dist + "----" + "token" + token + "----" + "udid" + udid + "---" + "sign" + sign);
			/*String dist = "013";
			String token = "47A9DC8C83DDB69AF68CEF84F468EB5F9B0704CA";
			String udid = "4C61912AF50A26FA64B4BA21208CEDCFFAA96CF1";
			String sign = "93862";*/
			
			if (dist != null && sign != null && token != null && udid != null) {
				if (dist.equals("") && sign.equals("") && token.equals("")) {
					//logger.info("参数为空串");
					return "{\"BM\":{},\"EC\":10007,\"EM\":\"参数错误\"}";
				}
				if (dist.equals("013")) {// 范文
					rest = itmDao.findModel(dist, sign, udid, codeType);
				}
				if (dist.equals("024")) {// 素材
					rest = itmDao.findMaterial(dist, sign, udid);
				}
				if (dist.equals("026")) {// 技法
					rest = itmDao.findTechnical(dist, sign, udid, codeType);
				}
				if (dist.equals("015")) {// 推荐作文
					if (sourceCompId != null && !sourceCompId.equals("")) {
						String url = "{\"BM\":{\"dlist\":";
						try {
							List<Map<String, Object>> list = comStateDao.findComPart(sourceCompId);
							if (list == (null) || list.size() == 0) {
								return "{\"BM\":{},\"EC\":10025,\"EM\":\"获取作文详情空\"}";
							}
							if (list.size() > 0) {
								JSONArray jsonArray = JSONArray.fromObject(list);
								url += jsonArray.get(0).toString();
								List<Map<String, Object>> tlist = comStateDao.findTeacherByCompId(sourceCompId);
								if (tlist != null && tlist.size() > 0) {
									JSONArray jsontlist = JSONArray.fromObject(tlist);
									url += ",\"tlist\":" + jsontlist.get(0).toString();
								}
							}
							// 取出来评价内容
							String appraiseInfo = getAppraiseInfo(sourceCompId);
							if (!TextUtils.isEmpty(appraiseInfo)) {
								url += ",\"appraise\":" + appraiseInfo;
							}
							url += "},\"EC\":0,\"EM\":\"\"}";
							return url;
						} catch (Exception e) {
							e.printStackTrace();
							return "{\"BM\":{},\"EC\":10015,\"EM\":\"获取作文详情异常\"}";
						}
					}
					
					rest = itmDao.findRecomm(dist, sign, udid);
				}
				
				if (rest != null) {
					JSONObject j = new JSONObject();
					j.put("BM", rest);
					j.put("EC", 0);
					j.put("EM", "");
					//logger.info("已经获取到查询结果");
					return j.toString();
				}
			}
		} catch (JSONException e) {
			//logger.info("service出现异常");
			e.printStackTrace();
		}
		//logger.info("查询结果为空");
		return "{\"BM\":{},\"EC\":10110,\"EM\":\"获取数据空\"}";
	}

	// 范文list
	@POST
	@Path("ModelList")
	@Produces(MediaType.APPLICATION_JSON)
	public String ModelList(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = null;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String class1 = str1.getString("class");
			String subject = str1.getString("subject");
			String token = str1.getString("token");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			list = itmDao.findModelList(class1, subject, numPerPage, currentPage);
			if (list.equals(null)) {
				return "{\"BM\":{},\"EC\":10087,\"EM\":\"范文异常\"}";
			}
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				return "{\"BM\":{\"rlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10007,\"EM\":\"参数错误\"}";
		}

		return "{\"BM\":{},\"EC\":10997,\"EM\":\"查询范文异常\"}";

	}

	// 素材list
	@POST
	@Path("MaterialList")
	@Produces(MediaType.APPLICATION_JSON)
	public String MaterialList(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = null;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String class1 = str1.getString("class");
			String grade = str1.getString("grade");
			String token = str1.getString("token");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			list = itmDao.findMaterialList(class1, grade, numPerPage, currentPage);
			if (list.equals(null)) {
				return "{\"BM\":{},\"EC\":10088,\"EM\":\"素材异常\"}";
			}
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				return "{\"BM\":{\"rlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10008,\"EM\":\"参数错误\"}";
		}

		return "{\"BM\":{},\"EC\":10998,\"EM\":\"查询素材异常\"}";

	}

	// 技法list
	@POST
	@Path("TechniqueList")
	@Produces(MediaType.APPLICATION_JSON)
	public String TechniqueList(String json) throws ParseException {
		JSONObject jsonObject;
		List<Map<String, Object>> list = null;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String type = str1.getString("type");
			String grade = str1.getString("grade");
			String token = str1.getString("token");
			String currentPage = str1.getString("currentPage");
			String numPerPage = str1.getString("numPerPage");
			if (currentPage.equals("")) {
				currentPage = "1";
			} else if (numPerPage.equals("")) {
				numPerPage = "10";
			}
			list = itmDao.findTechniqueList(type, grade, numPerPage, currentPage);
			if (list.equals(null)) {
				return "{\"BM\":{},\"EC\":10088,\"EM\":\"技法异常\"}";
			}
			if (list.size() > 0) {
				JSONArray jsonArray = JSONArray.fromObject(list);
				return "{\"BM\":{\"rlist\":" + jsonArray.toString() + "},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10008,\"EM\":\"参数错误\"}";
		}

		return "{\"BM\":{},\"EC\":10998,\"EM\":\"查询技法异常\"}";
	}
	
	public String getAppraiseInfo(String cid) {
		JSONObject jo = new JSONObject();
		try {
			int compid = Integer.parseInt(cid);
			List<Map<String, Object>> slist = appraiseDao.queryAppraiseIdByCompId1(compid);
			List<Map<String, Object>> slist2 = appraiseDao.queryAppraiseIdByCompId2(compid);
			if (slist != null && slist.size() > 0) {
				jo.put("attit", CommUtils.judgeSqlInformation(slist.get(0).get("attit")));// 负责态度
				jo.put("ave", CommUtils.judgeSqlInformation(slist.get(0).get("ave")));// 负责态度
				jo.put("prof", CommUtils.judgeSqlInformation(slist.get(0).get("prof")));// 负责态度
				jo.put("sati", CommUtils.judgeSqlInformation(slist.get(0).get("sati")));// 负责态度
				jo.put("stuHead", CommUtils.judgeUrl(slist.get(0).get("shead")));
				jo.put("atime", CommUtils.ObjectTime2String(slist.get(0).get("TIME")));
			}
			if (slist2 != null && slist2.size() > 0) {
				jo.put("smessage", CommUtils.judgeSqlInformation(slist2.get(0).get("MESSAGE")));
				List<List<Map<String, String>>> list = CommUtils.arrangeAppraiseMessage(slist2);
				if (list != null && list.size() > 0) {
					List<Map<String, String>> list2 = list.get(0);
					for (Map<String, String> map : list2) {
						if ("tea".equals(map.get("userType"))) {
							jo.put("tmessage", CommUtils.judgeSqlInformation(map.get("message")));
						}
					}
				}
				jo.put("messageList", list);
			}
		} catch (JSONException e) {
			return "";
		}
		return jo.toString();
	}

	private ITMDao itmDao;

	public ITMDao getItmDao() {
		return itmDao;
	}

	public void setItmDao(ITMDao itmDao) {
		this.itmDao = itmDao;
	}
	private ComStateDao comStateDao;

	public ComStateDao getComStateDao() {
		return comStateDao;
	}

	public void setComStateDao(ComStateDao comStateDao) {
		this.comStateDao = comStateDao;
	}
	
	private AppraiseDao appraiseDao;

	public AppraiseDao getAppraiseDao() {
		return appraiseDao;
	}

	public void setAppraiseDao(AppraiseDao appraiseDao) {
		this.appraiseDao = appraiseDao;
	}
	
	
	

}
