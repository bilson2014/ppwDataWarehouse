package com.paipianwang.SmartReport.data.criterion.operands;

public class LessThanOperand extends AbstractOperand {

	private String columnName;
	private Object columnValue;
	
	public LessThanOperand(final String columnName, final Object columnValue) {
		this.columnName = columnName;
		this.columnValue = columnValue;
	}


	@Override
	protected String toExpression() {
		return String.format("%1$s < %2$s ", this.columnName, this.columnValue);
	}

}
