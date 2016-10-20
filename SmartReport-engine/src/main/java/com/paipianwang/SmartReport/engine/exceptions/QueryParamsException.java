package com.paipianwang.SmartReport.engine.exceptions;

public class QueryParamsException extends RuntimeException {

	private static final long serialVersionUID = -4952550305955989565L;

	public QueryParamsException() {
		super();
	}

	public QueryParamsException(String message, Throwable cause) {
		super("报表查询参数设置有错误,可能未设置SQL语句或SQL语句不正确，或不正确的参数设置!", cause);
	}

	public QueryParamsException(String message) {
		super(message);
	}

	public QueryParamsException(Throwable cause) {
		super(cause);
	}

	
}
