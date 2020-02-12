/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service.dao 
 * @author: think   
 * @date: 2017-10-24 下午3:03:30 
 */
package com.rest.service.dao;

import com.entity.Version;

/**
 * @ClassName: SystemUpdateDao 
 * @Description: app升级
 * @author: think
 * @date: 2017-10-24 下午3:03:30  
 */
public interface SystemUpdateDao {
	
	/**
	 * @param utype   
	* @Title: findCurrentVersionByType  
	* @Description: 根据app类型查找当前最新的版本号
	* @param @param udid
	* @param @return    设定文件  
	* @return Version    返回类型  
	* @throws  
	*/ 
	Version findCurrentVersionByType(String uplatform, String utype);

	/**  
	* @Title: findVersionDetail  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param nowVersion
	* @param @return    设定文件  
	* @return Version    返回类型  
	* @throws  
	*/ 
	Version findVersionDetail(String nowVersion);
	
}
