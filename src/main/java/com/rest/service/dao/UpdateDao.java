package com.rest.service.dao;

import java.util.List;
import java.util.Map;

import com.entity.Student;
import com.entity.Teacher;

public interface UpdateDao {
	
	//修改学生密码
	public int selectStudentPw(String udid, String oldpw);//查询学生旧密码是否一致
	public int updateStudentPw(Student student ,String token );//修改学生密码
	
	//忘记密码(学生)
	public int selectSphone(String phone);//验证手机号是否存在
	public int updateForgotSpw(String password, String phone);//修改密码
	
	//修改老师密码
	public int selectTeacherPw(String udid, String oldpw);//查询老师旧密码是否一致
	public int updateTeacherPw(Teacher teacher ,String token );//修改老师密码
	
	//忘记密码(老师)	
	public int selectTphone(String phone);//验证手机号是否存在
	public int updateForgotTpw(String password, String phone);//修改密码
	
	//修改学生手机号
	public int findStudent(String pw, String phone, String udid);
	
	//修改老师手机号
	public int findTeacher(String pw, String phone, String udid);
	
	//回显基本信息（学生）
	public List<Map<String, Object>> getStudent(String udid);
	
	//修改基本信息（学生）
	public int updateStudent(Student student);
	
	//回显基本信息（老师）
	public List<Map<String,Object>> getTeacher(String udid);
	
	//修改基本信息（老师）
	public int updateTeacher(Teacher teacher);
	
	//验证密码学生
	public int findPassword(String password,String udid);
	
	//验证密码老师 
	public int findTpassword(String password,String udid);
	
	//老师状态
	public int updateTstate(String state,String udid);
	
	//修改师资认证 -回显
	public List<Map<String, Object>> getRenzheng(String token);

	//获取学生购买评点卡记录
	public List<Map<String, Object>> getStudentCard(String udid);
	
	//获取学生待支付数量
	public List<Map<String, Object>> getDpayment(String udid);
	
	//获取学生待点评数量
	public List<Map<String, Object>> getDcomment(String udid);
	
	//查询老师的待点评和点评作文数量
	public Map<String, String> selectCompositionCount(String udid);
	
}
