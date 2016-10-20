package com.paipianwang.SmartReport.common.viewmodel;

/**
 * Json 返回对象
 * @author Jack
 *
 */
public class JsonResult extends BaseModel{

	private static final long serialVersionUID = -715569279183647145L;
	
	private boolean isSuccess;
	
	private String msg;

	public JsonResult(boolean isSuccess, String msg) {
		super();
		this.isSuccess = isSuccess;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
