package com.rest.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.json.JSONException;

public interface PayService {

	// 支付
	public String pay(String json) throws ParseException;

	// 支付宝回调
	// public String notify(Map<String,String> map) throws ParseException,
	// JSONException;
	public String notify(String json) throws ParseException, JSONException, UnsupportedEncodingException;
	public String notify2(String json) throws ParseException, JSONException, UnsupportedEncodingException;
	//微信回调
	public String wxNotify(String json)throws Exception; 
	//评点卡支付
	public String dpCardPay(String json);

}
