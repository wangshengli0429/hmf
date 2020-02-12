package com.rest.service;

import java.text.ParseException;

/*
 *红包接口
 * 
 */
public interface RedService {
	
	//红包
	public String findRedPacket(String json) throws ParseException;
	//月卡
	public String findCard(String json) throws ParseException;
	
}
