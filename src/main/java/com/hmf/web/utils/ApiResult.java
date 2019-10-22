package com.hmf.web.utils;

import com.hmf.web.utils.enums.HttpStateEnum;

import java.io.Serializable;

/**
 * @Description 添加描述
 * @Param
 * @return 
 **/
public class ApiResult<T extends Object> implements Serializable {

	private static final long serialVersionUID = -1;

	/**
	 * 状态编码
	 */
	private String status;

	/**
	 * 错误消息
	 */
	private String message="";

	/**
	 * 返回值对象
	 */
	private T data;

	public ApiResult() {
		this.status = HttpStateEnum.OK.getIndex();
	}

	public ApiResult(T data) {
		this.status = HttpStateEnum.OK.getIndex();
		this.data = data;
	}

	public ApiResult(String status, String message){
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
