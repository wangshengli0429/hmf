/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service 
 * @author: think   
 * @date: 2017-10-24 下午2:54:52 
 */
package com.rest.service;

import java.text.ParseException;

/**
 * 
 * @ClassName: SystemUpdateService 
 * @Description: app升级
 * @author: think
 * @date: 2017-10-24 下午3:00:03
 */
public interface SystemUpdateService {
	
	//系统升级  
	String systemUpdate(String json) throws ParseException;
	
	//版本信息 
	String systemDetail(String json) throws ParseException;
}
