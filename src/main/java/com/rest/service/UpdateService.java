package com.rest.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

/*
 *修改(密码、手机号)接口
 * 
 */
public interface UpdateService {

	// 修改学生密码
	public String updateStudentPw(String json) throws ParseException;

	// 忘记密码(学生)
	public String updateForgotSpw(String json) throws ParseException;

	// 修改老师密码
	public String updateTeacherPw(String json) throws ParseException;

	// 忘记密码(老师)
	public String updateForgotTpw(String json) throws ParseException;

	// 修改学生手机号
	public String updateStudentPhone(String json) throws ParseException;

	// 修改老师手机号
	public String updateTeacherPhone(String json) throws ParseException;

	// 回显基本信息（学生）
	public String getStudent(String json) throws ParseException;

	// 修改基本信息（学生）
	public String updateStudent(String json) throws ParseException, IOException;

	// 回显基本信息（老师）
	public String getTeacher(String json) throws ParseException;

	// 修改基本信息（老师）
	public String updateTeacher(String json) throws ParseException;

	// 验证密码
	public String findPassword(String json) throws ParseException;

	// 上传图片
	public String uplodeimage(@Multipart(value = "file", type = "application/octet-stream") InputStream file) throws ParseException, IOException;

	// 修改老师状态
	public String updateTstate(String json) throws ParseException;

	// 修改师资认证
	public String updateRenzheng(String json) throws ParseException;
	
}
