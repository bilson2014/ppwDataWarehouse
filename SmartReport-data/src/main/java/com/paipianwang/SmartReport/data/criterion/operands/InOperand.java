package com.paipianwang.SmartReport.data.criterion.operands;

public class InOperand extends AbstractOperand {

	private String columnName;
	private Object columnValue;
	
	public InOperand(final String columnName, final Object columnValue) {
		this.columnName = columnName;
		this.columnValue = columnValue;
	}


	@Override
	protected String toExpression() {
		return String.format("%1$s IN (%2$s) ", this.columnName, this.columnValue);
	}

}
