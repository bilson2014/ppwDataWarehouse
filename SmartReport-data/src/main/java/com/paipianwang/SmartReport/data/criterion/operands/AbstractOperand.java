package com.paipianwang.SmartReport.data.criterion.operands;

import java.util.ArrayList;

public abstract class AbstractOperand {

	protected ArrayList<AbstractOperand> operands;
	
	public AbstractOperand append(AbstractOperand operand){
		if(this.operands == null) {
			this.operands = new ArrayList<AbstractOperand>(5);
		}
		
		this.operands.add(operand);
		return this;
	}
	
	protected abstract String toExpression();
	
	@Override
	public String toString() {
		if(this.operands == null) {
			return this.toExpression();
		}
		
		final StringBuilder expr = new StringBuilder();
		expr.append(this.toExpression());
		for (final AbstractOperand operand : this.operands) {
			expr.append(operand);
		}
		
		return expr.toString();
	}
}
