package com.paipianwang.SmartReport.engine.data;

import java.util.Map;

import com.paipianwang.SmartReport.engine.utils.VelocityUtils;

public class ReportSqlTemplate {

	private String sqlTemplate;
	private Map<String, Object> parameters;

	public ReportSqlTemplate(String sqlTemplate, Map<String, Object> parameters) {
		super();
		this.sqlTemplate = sqlTemplate;
		this.parameters = parameters;
	}

	public String execute() {
		return VelocityUtils.parse(this.sqlTemplate, this.parameters);
	}
}
