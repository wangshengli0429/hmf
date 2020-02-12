package com.entity;

import java.io.Serializable;
import java.util.Date;

public class Clearing implements Serializable{
	private String tname ;
	private String tphone ;
	private String num ;
	private String price ;
	private String userName ;
	private String state ;
	private String billTime ;
	private String cleartime ;
	
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getTphone() {
		return tphone;
	}
	public void setTphone(String tphone) {
		this.tphone = tphone;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBillTime() {
		return billTime;
	}
	public void setBillTime(String billTime) {
		this.billTime = billTime;
	}
	public String getCleartime() {
		return cleartime;
	}
	public void setCleartime(String cleartime) {
		this.cleartime = cleartime;
	}
	
}
