package com.entity;

import java.io.Serializable;
import java.util.Date;

/****
 *范文表实体
 */
public class Rescomposion implements Serializable {
	private Integer id ;
	private String name ; //作文名称
	private String content ; //作文内容
	private Date createTime ; //创建时间
	private int creatorId ; //创建人ID
	private Date publishTime ; //发布时间
	private int originalId ; //原始版本作文ID
	private String cause ; //写作缘由（课后作业、个人写作等）：T_BASE_CODE.CODE_TYPE=008
	private int objId ; //写作目标ID：如果写作缘由是“课后作业”，则记录“作文任务”的ID
	private String type ; //题材类型，公共代码033
	private String etype ; //命题类型，公共代码029
	private String style ; //文体类型(记叙文、议论文等：T_BASE_CODE.CODE_TYPE=013
	private String ageScale ; //年级等级(小学低段、小学高段等)：T_BASE_CODE.CODE_TYPE=014
	private String keyWord ; //关键字：以空格分隔
	private String status ; //资源状态：T_BASE_CODE.CODE_TYPE=004
	private String ctype ; //作文类型：T_BASE_CODE.CODE_TYPE=022
	private String cyeae ; //作文年份：存储4位年份
	private String cregion ; //作文考区
	private String ageDetail ; //年级细分：一年级、二年级、三年级、四年级、五年级、六年级、小升初、初一、初二、初三、中考、高一、高二、高三、高考
	private String author ; //作者：存储作者名称
	private String islocal ; //是否历史数据
	private String isuserSubmis ; //是否用户投稿，是：Y，否：N
	private String usersubStatus ; //用户投稿资源状态：T_BASE_CODE.CODE_TYPE=004
	private int localId ; //作文一期数据ID,非迁移数据该字段为空
	private int format ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public int getOriginalId() {
		return originalId;
	}
	public void setOriginalId(int originalId) {
		this.originalId = originalId;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getAgeScale() {
		return ageScale;
	}
	public void setAgeScale(String ageScale) {
		this.ageScale = ageScale;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public String getCyeae() {
		return cyeae;
	}
	public void setCyeae(String cyeae) {
		this.cyeae = cyeae;
	}
	public String getCregion() {
		return cregion;
	}
	public void setCregion(String cregion) {
		this.cregion = cregion;
	}
	public String getAgeDetail() {
		return ageDetail;
	}
	public void setAgeDetail(String ageDetail) {
		this.ageDetail = ageDetail;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIslocal() {
		return islocal;
	}
	public void setIslocal(String islocal) {
		this.islocal = islocal;
	}
	public String getIsuserSubmis() {
		return isuserSubmis;
	}
	public void setIsuserSubmis(String isuserSubmis) {
		this.isuserSubmis = isuserSubmis;
	}
	public String getUsersubStatus() {
		return usersubStatus;
	}
	public void setUsersubStatus(String usersubStatus) {
		this.usersubStatus = usersubStatus;
	}
	public int getLocalId() {
		return localId;
	}
	public void setLocalId(int localId) {
		this.localId = localId;
	}
	public int getFormat() {
		return format;
	}
	public void setFormat(int format) {
		this.format = format;
	} 
	
}
