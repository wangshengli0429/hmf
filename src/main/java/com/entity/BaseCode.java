package com.entity;

import java.io.Serializable;

/****
 *公共代码表实体
 */
public class BaseCode implements Serializable{
	private String code ; //代码
	private String codeName ; //代码名称
	private String codeType ; //代码类别
	private String codeTypeName ; //代码类别名称
	private String remark ; //备注
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCodeTypeName() {
		return codeTypeName;
	}
	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
