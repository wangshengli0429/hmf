package com.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/****
 *订单表实体
 */
public class Order implements Serializable{
	private Integer id ;
	private int stuid ;
	private String orderno ;
	private String payment ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getStuid() {
		return stuid;
	}
	public void setStuid(int stuid) {
		this.stuid = stuid;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getCompid() {
		return compid;
	}
	public void setCompid(int compid) {
		this.compid = compid;
	}
	public int getTeacherid() {
		return teacherid;
	}
	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}
	public int getRpack() {
		return rpack;
	}
	public void setRpack(int rpack) {
		this.rpack = rpack;
	}
	public int getRedid() {
		return redid;
	}
	public void setRedid(int redid) {
		this.redid = redid;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	private String state ;
	private int compid ;
	private int teacherid ;
	private int rpack ;
	private int redid ;
	private BigDecimal price ;
	private BigDecimal money ;
	private Date  createTime;


}
