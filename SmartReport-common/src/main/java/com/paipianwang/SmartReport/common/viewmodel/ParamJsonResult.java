package com.paipianwang.SmartReport.common.viewmodel;

import java.io.Serializable;

/**
 * 参数化类型JSON返回结果类
 * @author Administrator
 *
 * @param <T>
 */
public class ParamJsonResult<T> extends JsonResult implements Serializable{

	private static final long serialVersionUID = -7515423701333010415L;

	public ParamJsonResult(boolean isSuccess, String msg) {
		super(isSuccess, msg);
	}

	private T data;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


}
