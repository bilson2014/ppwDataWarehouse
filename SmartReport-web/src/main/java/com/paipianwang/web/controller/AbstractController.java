package com.paipianwang.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paipianwang.SmartReport.common.viewmodel.JsonResult;

public abstract class AbstractController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected void setSuccessResult(final JsonResult result, final String msg) {
		result.setSuccess(true);
		result.setMsg(StringUtils.isBlank(msg) ? "操作成功!" : msg);
	}
	
	protected void setFailureResult(final JsonResult result, final String msg) {
		result.setSuccess(false);
		result.setMsg(StringUtils.isBlank(msg) ? "操作失败!" : msg);
	}
	
	protected void setExceptionResult(final JsonResult result, final Exception ex) {
		result.setSuccess(false);
		result.setMsg(String.format("系统异常,原因: %s", ex.getMessage()));
		this.logException(ex);
	}
	
	
	protected void logException(final Exception ex) {
		this.logException("系统异常",ex);
	}
	
	protected void logException(final String msg, final Exception ex) {
		logger.error(msg,ex.getMessage());
	}
}
