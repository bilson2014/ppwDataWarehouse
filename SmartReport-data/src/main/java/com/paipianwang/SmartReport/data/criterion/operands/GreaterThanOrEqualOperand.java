package com.paipianwang.SmartReport.data.criterion.operands;

public class GreaterThanOrEqualOperand extends AbstractOperand {

	private String columnName;
	private Object columnValue;
	
	public GreaterThanOrEqualOperand(String columnName, Object columnValue) {
		super();
		this.columnName = columnName;
		this.columnValue = columnValue;
	}

	@Override
	protected String toExpression() {
		return String.format("%1$s >= %2$s ", this.columnName, this.columnValue);
	}

}
