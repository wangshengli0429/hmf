package com.entity;

import java.io.Serializable;
import java.util.Date;


/****
 *评价表实体
 */
public class Appraise implements Serializable{
	private Integer id ;
	private int teacherid ;
	private String grade ;
	private int compid ;
	private int satisfactoin ;
	private int  attit;
	private int  profes;
	private float ave;
	private String  stumessage;
	private String  udid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getTeacherid() {
		return teacherid;
	}
	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public int getCompid() {
		return compid;
	}
	public void setCompid(int compid) {
		this.compid = compid;
	}
	public int getSatisfactoin() {
		return satisfactoin;
	}
	public void setSatisfactoin(int satisfactoin) {
		this.satisfactoin = satisfactoin;
	}
	public int getAttit() {
		return attit;
	}
	public void setAttit(int attit) {
		this.attit = attit;
	}
	public int getProfes() {
		return profes;
	}
	public void setProfes(int profes) {
		this.profes = profes;
	}
	public float getAve() {
		return ave;
	}
	public void setAve(float ave) {
		this.ave = ave;
	}
	public String getStumessage() {
		return stumessage;
	}
	public void setStumessage(String stumessage) {
		this.stumessage = stumessage;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getTeachermessage() {
		return teachermessage;
	}
	public void setTeachermessage(String teachermessage) {
		this.teachermessage = teachermessage;
	}
	public Date getAppetime() {
		return appetime;
	}
	public void setAppetime(Date appetime) {
		this.appetime = appetime;
	}
	public Date getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}
	private String teachermessage ;
	private Date  appetime;
	private Date messageTime ;


}
