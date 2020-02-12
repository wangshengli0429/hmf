package com.entity;

import java.io.Serializable;
import java.util.Date;


/****
 *老师表实体
 */
public class Teacher implements Serializable{
	private Integer id ;
	private String username ;
	private String grade ;
	private String sex ;
	private String name ;
	private String school ;
	private String phone ;
	private String city ;
	private String udid ;
	private String password ;
	private String headurl ;
	private Date createdTime ;
	private String  honor;
	private String  features;
	private String certurl;
	private String card1;
	private String card2;
	private String price;
	private String acv;
	private String ave;
	private String austate;
	private String cause;
	private String loginid;
	private String edutime;
	private String exper;
	private String state;
	private String extension;
	private String grange;
	private String uploadTime ;
	private String perfectTime ;
	private String completeInfo;
	private String qqOpenid;
	private String msgOpenid;
	private String qqName;
	private String msgName;
	private String qqUnionid;
	private String msgUnionid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getHonor() {
		return honor;
	}
	public void setHonor(String honor) {
		this.honor = honor;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getCerturl() {
		return certurl;
	}
	public void setCerturl(String certurl) {
		this.certurl = certurl;
	}
	public String getCard1() {
		return card1;
	}
	public void setCard1(String card1) {
		this.card1 = card1;
	}
	public String getCard2() {
		return card2;
	}
	public void setCard2(String card2) {
		this.card2 = card2;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAcv() {
		return acv;
	}
	public void setAcv(String acv) {
		this.acv = acv;
	}
	public String getAve() {
		return ave;
	}
	public void setAve(String ave) {
		this.ave = ave;
	}
	public String getAustate() {
		return austate;
	}
	public void setAustate(String austate) {
		this.austate = austate;
	}
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getEdutime() {
		return edutime;
	}
	public void setEdutime(String edutime) {
		this.edutime = edutime;
	}
	public String getExper() {
		return exper;
	}
	public void setExper(String exper) {
		this.exper = exper;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getGrange() {
		return grange;
	}
	public void setGrange(String grange) {
		this.grange = grange;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getPerfectTime() {
		return perfectTime;
	}
	public void setPerfectTime(String perfectTime) {
		this.perfectTime = perfectTime;
	}
	
	public String getCompleteInfo() {
		return completeInfo;
	}
	public void setCompleteInfo(String completeInfo) {
		this.completeInfo = completeInfo;
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
		return "Teacher [id=" + id + ", username=" + username + ", grade=" + grade + ", sex=" + sex + ", name=" + name
				+ ", school=" + school + ", phone=" + phone + ", city=" + city + ", udid=" + udid + ", password="
				+ password + ", headurl=" + headurl + ", createdTime=" + createdTime + ", honor=" + honor
				+ ", features=" + features + ", certurl=" + certurl + ", card1=" + card1 + ", card2=" + card2
				+ ", price=" + price + ", acv=" + acv + ", ave=" + ave + ", austate=" + austate + ", cause=" + cause
				+ ", loginid=" + loginid + ", edutime=" + edutime + ", exper=" + exper + ", state=" + state
				+ ", extension=" + extension + ", grange=" + grange + ", uploadTime=" + uploadTime + ", perfectTime="
				+ perfectTime + ", completeInfo=" + completeInfo + ", qqOpenid=" + qqOpenid + ", msgOpenid=" + msgOpenid
				+ ", qqName=" + qqName + ", msgName=" + msgName + ", qqUnionid=" + qqUnionid + ", msgUnionid="
				+ msgUnionid + "]";
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
	
}
