package com.paipianwang.SmartReport.data.criterion.operands;

public class BracketOperand extends AbstractOperand {

	private Bracket bracket;

	public BracketOperand(final Bracket bracket) {
		this.bracket = bracket;
	}

	@Override
	protected String toExpression() {
		switch (this.bracket) {
			case Left:
				return "(";
			case Right:
				return ")";
			default:
				return "";
		}
	}

}
