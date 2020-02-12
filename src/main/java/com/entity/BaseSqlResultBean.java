package com.entity;

import java.io.Serializable;

public class BaseSqlResultBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int sqlCode = 0;
	
	private int sqlID = -1;

	public int getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(int sqlCode) {
		this.sqlCode = sqlCode;
	}

	public int getSqlID() {
		return sqlID;
	}

	public void setSqlID(int sqlID) {
		this.sqlID = sqlID;
	}
	
	

}
