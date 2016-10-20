package com.paipianwang.SmartReport.engine.exceptions;

public class NotFoundLayoutColumnException extends RuntimeException {

	private static final long serialVersionUID = -7343608528829331974L;

	public NotFoundLayoutColumnException() {
		super();
	}

	public NotFoundLayoutColumnException(String message, Throwable cause) {
		super("没找到报表中的布局列，请配置布局列!", cause);
	}

	public NotFoundLayoutColumnException(String message) {
		super(message);
	}

	public NotFoundLayoutColumnException(Throwable cause) {
		super(cause);
	}

}
