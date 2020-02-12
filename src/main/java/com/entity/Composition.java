package com.entity;

import java.io.Serializable;
import java.util.Date;


/****
 *作文表实体
 */
public class Composition implements Serializable{
	private Integer id ;
	private int createid ;
	private int cstate ;
	private String oldtitle ;
	private String newtitle ;
	private String draft ;
	private String propo ;
	private String author ;
	private String type ;
	private String grade ;
	private String image1 ;
	private String image2 ;
	private String image3 ;
	private String content ;
	private Date createTime ;
	private String udid;
	private String tid;
	private String state;
	private boolean open;
	public int getCstate() {
		return cstate;
	}
	public void setCstate(int cstate) {
		this.cstate = cstate;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getCreateid() {
		return createid;
	}
	public void setCreateid(int createid) {
		this.createid = createid;
	}
	public String getOldtitle() {
		return oldtitle;
	}
	public void setOldtitle(String oldtitle) {
		this.oldtitle = oldtitle;
	}
	public String getNewtitle() {
		return newtitle;
	}
	public void setNewtitle(String newtitle) {
		this.newtitle = newtitle;
	}
	public String getDraft() {
		return draft;
	}
	public void setDraft(String draft) {
		this.draft = draft;
	}
	public String getPropo() {
		return propo;
	}
	public void setPropo(String propo) {
		this.propo = propo;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}

	
}
