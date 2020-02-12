/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service.impl 
 * @author: think   
 * @date: 2017-10-24 下午3:02:40 
 */
package com.rest.service.impl;

import java.text.ParseException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.entity.Version;
import com.rest.service.SystemUpdateService;
import com.rest.service.dao.SystemUpdateDao;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SystemUpdateServiceImpl 
 * @Description: app升级
 * @author: think
 * @date: 2017-10-24 下午3:02:40  
 */
@Component
public class SystemUpdateServiceImpl implements SystemUpdateService {

	private SystemUpdateDao systemUpdateDao;
	public SystemUpdateDao getSystemUpdateDao() {
		return systemUpdateDao;
	}
	public void setSystemUpdateDao(SystemUpdateDao systemUpdateDao) {
		this.systemUpdateDao = systemUpdateDao;
	}

	/* (non Javadoc) 
	 * @Title: systemUpdate
	 * @Description: 系统升级
	 * @param json
	 * @return
	 * @throws ParseException 
	 * @see com.rest.service.SystemUpdateService#systemUpdate(java.lang.String) 
	 */
	@Override
	@POST
	@Path("/systemUpdate")
	@Produces(MediaType.APPLICATION_JSON)
	public String systemUpdate(String json) throws ParseException {
		//json:{"BM":{"currentVersion":"1.0.0","platform":"IOS","type":"teacher"}}
		Version version;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str = jsonObject.getJSONObject("BM");
			String nowVersion = str.getString("currentVersion");// 当前版本号
			String uplatform = str.getString("platform");//app类型ios和android
			String utype = str.getString("type");//app类型教师端和学生端
			//根据app类型查询数据库返回最新版本号
			version = systemUpdateDao.findCurrentVersionByType(uplatform, utype);
			String strVersion = version.getvVersion();
			int i = compareVersion(strVersion, nowVersion);
			if (i > 0) {
				String currentVersion = version.getvVersion();
				int code = Integer.parseInt(currentVersion.substring(0, 1));
				String fileUrl = version.getvFileUrl();
				String tips = version.getVtips();
				return "{\"BM\":{\"currentVersion\":\""+currentVersion+"\",\"fileUrl\":\""+fileUrl+"\",\"tips\":\""+tips+"\",\"versionCode\":\""+code+"\"},\"EC\":0,\"EM\":\"\"}";
			}else if (i == 0) {
				return "{\"BM\":{\"currentVersion\":\"\",\"fileUrl\":\"\",\"tips\":\"\",\"versionCode\":\"\"},\"EC\":11150,\"EM\":\"当前已经是最新版本\"}";
			}else {
				return "{\"BM\":{},\"EC\":11151,\"EM\":\"参数错误\"}";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":11151,\"EM\":\"参数错误\"}";
		}
	}

	/* (non Javadoc) 
	 * @Title: systemDetail
	 * @Description: 版本信息
	 * @param json
	 * @return
	 * @throws ParseException 
	 * @see com.rest.service.SystemUpdateService#systemDetail(java.lang.String) 
	 */
	@Override
	@POST
	@Path("/systemDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public String systemDetail(String json) throws ParseException {
		Version version;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str = jsonObject.getJSONObject("BM");
			String nowVersion = str.getString("currentVersion");// 当前版本号
			//根据版本号查询版本详细信息
			version = systemUpdateDao.findVersionDetail(nowVersion);
			if (version != null) {
				String version_number = version.getvVersion();
				String fileUrl = version.getvFileUrl();
				String tips = version.getVtips();
				String platform = version.getvPlatform();
				String user_type = version.getvType();
				String detail = "";
				if ("STUDENT".equals(user_type)) {
					detail = "您当前使用是学生端，版本号为"+version_number+",版本细节："+tips; 
				}else if("TEACHER".equals(user_type)){
					detail = "您当前使用是教师端，版本号为"+version_number+",版本细节："+tips; 
				}else {
					detail = "您当前使用的版本号为"+version_number+",版本细节："+tips; 
				}
				return "{\"BM\":{\"detail\":\""+detail+"\"},\"EC\":\"\",\"EM\":\"\"}";
			}else {
				return "{\"BM\":{},\"EC\":\"\",\"EM\":\"参数错误\"}";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":\"\",\"EM\":\"参数错误\"}";
		}
	}

	
	/**
	 * 
	* @Title: compareVersion  
	* @Description: TODO比较版本号大小
	* @param @param version1
	* @param @param version2
	* @param @return    设定文件  
	* @return int    返回类型  
	* @throws
	 */
	
	public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }

        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");

        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;

        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }

        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }

            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
	
}
