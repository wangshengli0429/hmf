package com.entity;

import java.io.Serializable;
import java.util.Date;

/****
 *管理表实体
 */
public class ManageUsers implements Serializable{
	private Integer id ;
	private String username ;
	private String password ;
	private Date createdTime ;
	private Integer disable ;//是否禁用
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Integer getDisable() {
		return disable;
	}
	public void setDisable(Integer disable) {
		this.disable = disable;
	}
	
}
