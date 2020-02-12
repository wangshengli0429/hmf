package com.rest.service.dao;

public interface XiaochengxuDao {
	
	//通过手机号查询验证码
	String getCodeByPhone(String phone);

	//查询年级
	String findGradeByUdid(String udid);
	
}
