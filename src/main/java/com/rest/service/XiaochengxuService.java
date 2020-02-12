package com.rest.service;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public interface XiaochengxuService {
	
	String wxLogin(String json) throws ParseException;
	
	String getPhoneNumber(String json) throws ParseException;
	
	String validateCode(String json,@Context HttpServletRequest servletRequest) throws ParseException;

	String getVerificationCode(String json,@Context HttpServletRequest servletRequest) throws ParseException;
	
	String insertComposi(String json) throws ParseException;
}
