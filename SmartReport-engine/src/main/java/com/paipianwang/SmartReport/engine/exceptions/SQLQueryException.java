package com.paipianwang.SmartReport.engine.exceptions;

public class SQLQueryException extends RuntimeException {

	private static final long serialVersionUID = -3898353803479097435L;

	public SQLQueryException() {
        super();
    }

    public SQLQueryException(String message) {
        super(message);
    }

    public SQLQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLQueryException(Throwable cause) {
        super("报表SQL查询语句执行失败!请查看报表配置列与SQL语句中的列是否一致.详细信息：" + cause.getMessage(), cause);
    }
}
