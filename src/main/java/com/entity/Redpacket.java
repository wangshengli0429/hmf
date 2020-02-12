package com.entity;

import java.io.Serializable;
import java.util.Date;

public class Redpacket implements Serializable{
	private Integer id ;//红包唯一标识
	private String packetName ;//红包名称
	private String packetPrice ;//价钱
	private String udid ;
	private int failure ;//1失效0未失效
	private int use ;//1使用0未使用
	private Date startTime ;//开始时间
	private Date endTime ;//失效时间
	private String packetImage ;//红包图片
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPacketName() {
		return packetName;
	}
	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}
	public String getPacketPrice() {
		return packetPrice;
	}
	public void setPacketPrice(String packetPrice) {
		this.packetPrice = packetPrice;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public int getFailure() {
		return failure;
	}
	public void setFailure(int failure) {
		this.failure = failure;
	}
	public int getUse() {
		return use;
	}
	public void setUse(int use) {
		this.use = use;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getPacketImage() {
		return packetImage;
	}
	public void setPacketImage(String packetImage) {
		this.packetImage = packetImage;
	}
	
}
