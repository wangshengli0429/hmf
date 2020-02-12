/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.entity 
 * @author: think   
 * @date: 2017-10-24 下午3:22:05 
 */
package com.entity;

import java.io.Serializable;

/**
 * @ClassName: Version 
 * @Description: app版本号实体表
 * @author: think
 * @date: 2017-10-24 下午3:22:05  
 */
public class Version implements Serializable{
	//主键id
	private Integer vId;
	//类型，teacher代表老师端app，student代表学生端app
	private String vType;
	//类型，IOS代表ios用户，ANDROID代表android用户
	private String vPlatform;
	//版本号
	private String vVersion;
	//下载地址
	private String vFileUrl;
	//状态，00代表最新版本，01代表以前版本
	private String vState;
	//更新说明
	private String vtips;
	public Integer getvId() {
		return vId;
	}
	public void setvId(Integer vId) {
		this.vId = vId;
	}
	public String getvType() {
		return vType;
	}
	public void setvType(String vType) {
		this.vType = vType;
	}
	public String getvPlatform() {
		return vPlatform;
	}
	public void setvPlatform(String vPlatform) {
		this.vPlatform = vPlatform;
	}
	public String getvVersion() {
		return vVersion;
	}
	public void setvVersion(String vVersion) {
		this.vVersion = vVersion;
	}
	public String getvFileUrl() {
		return vFileUrl;
	}
	public void setvFileUrl(String vFileUrl) {
		this.vFileUrl = vFileUrl;
	}
	public String getvState() {
		return vState;
	}
	public void setvState(String vState) {
		this.vState = vState;
	}
	public String getVtips() {
		return vtips;
	}
	public void setVtips(String vtips) {
		this.vtips = vtips;
	}
	@Override
	public String toString() {
		return "Version [vId=" + vId + ", vType=" + vType + ", vPlatform="
				+ vPlatform + ", vVersion=" + vVersion + ", vFileUrl="
				+ vFileUrl + ", vState=" + vState + ", vtips=" + vtips + "]";
	}
	
}
