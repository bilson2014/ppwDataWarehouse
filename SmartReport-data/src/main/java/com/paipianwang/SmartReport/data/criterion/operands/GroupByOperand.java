package com.paipianwang.SmartReport.data.criterion.operands;

import org.apache.commons.lang3.StringUtils;

public class GroupByOperand extends AbstractOperand {

	private String[] _columnNames;
	
	public GroupByOperand(final String[] _columnNames) {
		this._columnNames = _columnNames;
	}


	@Override
	protected String toExpression() {
		if (this._columnNames == null || this._columnNames.length <= 0)
			return "";
		
		return String.format("GROUP BY %s ", StringUtils.join(_columnNames));
	}

}
