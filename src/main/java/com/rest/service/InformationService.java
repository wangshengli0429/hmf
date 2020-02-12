package com.rest.service;

import java.text.ParseException;

public interface InformationService {
	/*
	 *消息接口
	 * 
	 */
		//查看消息list
		public String selectInformation(String json) throws ParseException;
		
		//删除消息
		public String deleteInformation(String json) throws ParseException;
	
}
