package com.entity;

import java.io.Serializable;
import java.util.Date;


/****
 *学生表实体
 */
public class Student implements Serializable{
	private Integer id ;
	private String nickname ;
	private String grade ;
	private String sex ;
	private String name ;
	private String school ;
	private String phone ;
	private int loginid;
	private String area ;
	private String udid ;
	private String password ;
	private String headurl ;
	private String introduce ;
	private Date createdTime ;
	private String extension;
	private String qqOpenid;
	private String msgOpenid;
	private String qqName;
	private String msgName;
	private String qqUnionid;
	private String msgUnionid;
	private String userType;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getLoginid() {
		return loginid;
	}
	public void setLoginid(int loginid) {
		this.loginid = loginid;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getQqOpenid() {
		return qqOpenid;
	}
	public void setQqOpenid(String qqOpenid) {
		this.qqOpenid = qqOpenid;
	}
	public String getMsgOpenid() {
		return msgOpenid;
	}
	public void setMsgOpenid(String msgOpenid) {
		this.msgOpenid = msgOpenid;
	}
	public String getQqName() {
		return qqName;
	}
	public void setQqName(String qqName) {
		this.qqName = qqName;
	}
	public String getMsgName() {
		return msgName;
	}
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", nickname=" + nickname + ", grade="
				+ grade + ", sex=" + sex + ", name=" + name + ", school="
				+ school + ", phone=" + phone + ", loginid=" + loginid
				+ ", area=" + area + ", udid=" + udid + ", password="
				+ password + ", headurl=" + headurl + ", introduce="
				+ introduce + ", createdTime=" + createdTime + ", extension="
				+ extension + ", qqOpenid=" + qqOpenid + ", msgOpenid="
				+ msgOpenid + ", qqName=" + qqName + ", msgName=" + msgName
				+ "]";
	}
	public String getQqUnionid() {
		return qqUnionid;
	}
	public void setQqUnionid(String qqUnionid) {
		this.qqUnionid = qqUnionid;
	}
	public String getMsgUnionid() {
		return msgUnionid;
	}
	public void setMsgUnionid(String msgUnionid) {
		this.msgUnionid = msgUnionid;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	

}
