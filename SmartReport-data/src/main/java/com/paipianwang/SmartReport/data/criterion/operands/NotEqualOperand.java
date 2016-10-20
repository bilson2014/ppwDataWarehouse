package com.paipianwang.SmartReport.data.criterion.operands;

public class NotEqualOperand extends AbstractOperand {

	private String columnName;
	private Object columnValue;
	
	public NotEqualOperand(final String columnName, final Object columnValue) {
		this.columnName = columnName;
		this.columnValue = columnValue;
	}


	@Override
	protected String toExpression() {
		return String.format("%1$s <> %2$s ", this.columnName, this.columnValue);
	}

}
