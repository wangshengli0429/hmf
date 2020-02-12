/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service.dao.impl 
 * @author: think   
 * @date: 2017-10-24 下午3:05:04 
 */
package com.rest.service.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.entity.Version;
import com.rest.service.TomcatListener;
import com.rest.service.dao.SystemUpdateDao;

/**
 * @ClassName: SystemUpdateDaoImpl 
 * @Description: app升级
 * @author: think
 * @date: 2017-10-24 下午3:05:04  
 */
public class SystemUpdateDaoImpl implements SystemUpdateDao {
	private static Logger logger = Logger.getLogger(SystemUpdateDaoImpl.class);
	
	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}
	/* (non Javadoc) 
	 * @Title: findCurrentVersionByType
	 * @Description: 根据app类型查找当前最新的版本号
	 * @param udid
	 * @return 
	 * @see com.rest.service.dao.SystemUpdateDao#findCurrentVersionByType(java.lang.String) 
	 */
	@Override
	public Version findCurrentVersionByType(String uplatform,String utype) {
		List<Map<String,Object>> list = null;
		try {
			String sql = "select * from version where user_type = ? and platform = ? ORDER BY id DESC";
			list = jt.queryForList(sql, new Object[]{utype, uplatform});
		} catch (Exception e) {
			logger.info("--------------查询版本信息异常------------user_type="+utype+",platform="+uplatform);
		}
		
		Version version = new Version();
		int i = 0;
		if (list != null && list.size() > 0) {
			if (list.size() > 1) {
				i = getNewVersion(list);
			}
			Map<String, Object> map = list.get(i);
			Integer id = (Integer) map.get("id");
			String versionStr = (String) map.get("version_number");
			String vFileUrl = TomcatListener.serverHost + (String) map.get("fileUrl");
			String vTips = (String) map.get("tips");
			version.setvFileUrl(vFileUrl);
			version.setvId(id);
			version.setVtips(vTips);
			version.setvVersion(versionStr);
		}
		return version;
	}

	/* (non Javadoc) 
	 * @Title: findVersionDetail
	 * @Description: TODO
	 * @param nowVersion
	 * @return 
	 * @see com.rest.service.dao.SystemUpdateDao#findVersionDetail(java.lang.String) 
	 */
	@Override
	public Version findVersionDetail(String nowVersion) {
		String sql = "select * from version where version_number = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{nowVersion});
		Version version = new Version();
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			String version_number = (String) map.get("version_number");
			String user_type = (String) map.get("user_type");
			String platform = (String) map.get("platform");
			String vTips = (String) map.get("tips");
			String fileUrl = (String) map.get("fileUrl");
			version.setvFileUrl(fileUrl);
			version.setVtips(vTips);
			version.setvPlatform(platform);
			version.setvType(user_type);
			version.setvVersion(version_number);
			return version;
		}else {
			return null;
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
	/**
	 * 
	* @Title: getNewVersion  
	* @Description: TODO得到最新版本  
	* @param @param list
	* @param @return    设定文件  
	* @return int    返回类型  
	* @throws
	 */
	public int getNewVersion(List<Map<String,Object>> list){
		int i;
		List<String> versionList = new ArrayList<>();
		for (i = 0;i<list.size();i++){
			String version = (String) list.get(i).get("version_number");
			versionList.add(version);
		}
		return digui(versionList, 0, 1);
	}
	/*
	 * 递归找最新版本的index
	 */
	public int digui(List<String> versionList, int i, int j){
		if ((i == versionList.size() - 1 ) ||(j == versionList.size() - 1)) {
			String v1 = versionList.get(i);
			String v2 = versionList.get(j);
			int result = compareVersion(v1, v2);
			if (result > 0) {
				return i;
			}else {
				return j;
			}
		}
		String v1 = versionList.get(i);
		String v2 = versionList.get(j);
		int result = compareVersion(v1, v2);
		if (result > 0) {
			return digui(versionList, i, j+1);
		}else {
			return digui(versionList, j, i+1);
		}
	}
}
