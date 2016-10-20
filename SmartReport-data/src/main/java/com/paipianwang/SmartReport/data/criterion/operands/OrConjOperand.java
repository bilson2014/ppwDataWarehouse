package com.paipianwang.SmartReport.data.criterion.operands;

public class OrConjOperand extends AbstractOperand {

	@Override
	protected String toExpression() {
		return " OR ";
	}

}
