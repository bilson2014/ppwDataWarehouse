package com.paipianwang.SmartReport.data.jdbc;

import java.util.List;

import com.paipianwang.SmartReport.data.mapping.ColumnProperty;

public class SqlExpression {

	private String commandText;
	private Object[] parameters;
	private int[] paramTypes;
	private List<List<ColumnProperty>> batchParameters;
	
	public SqlExpression(String commandText, List<List<ColumnProperty>> batchParameters) {
		this.commandText = commandText;
		this.batchParameters = batchParameters;
	}

	public SqlExpression(String commandText, Object[] parameters, int[] paramTypes) {
		super();
		this.commandText = commandText;
		this.parameters = parameters;
		this.paramTypes = paramTypes;
	}

	public String getCommandText() {
		return commandText;
	}

	public void setCommandText(String commandText) {
		this.commandText = commandText;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public int[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(int[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public List<List<ColumnProperty>> getBatchParameters() {
		return batchParameters;
	}

	public void setBatchParameters(List<List<ColumnProperty>> batchParameters) {
		this.batchParameters = batchParameters;
	}

	
	
}
