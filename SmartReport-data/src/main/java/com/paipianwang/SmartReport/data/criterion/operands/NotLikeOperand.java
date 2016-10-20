package com.paipianwang.SmartReport.data.criterion.operands;

public class NotLikeOperand extends AbstractOperand {

	private String columnName;
	private Object columnValue;
	
	public NotLikeOperand(final String columnName, final Object columnValue) {
		this.columnName = columnName;
		this.columnValue = columnValue;
	}

	@Override
	protected String toExpression() {
		return String.format("%1$s NOT LIKE '%2$s' ", this.columnName, this.columnValue);
	}

}
