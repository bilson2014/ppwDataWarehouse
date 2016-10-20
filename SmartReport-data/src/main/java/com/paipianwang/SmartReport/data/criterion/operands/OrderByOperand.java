package com.paipianwang.SmartReport.data.criterion.operands;

import org.apache.commons.lang3.StringUtils;

import com.paipianwang.SmartReport.data.SortType;

public class OrderByOperand extends AbstractOperand {

	private SortType _sortType;
	private String[] _columnNames;
	
	
	
	public OrderByOperand(final SortType _sortType, final String[] _columnNames) {
		this._sortType = _sortType;
		this._columnNames = _columnNames;
	}

	@Override
	protected String toExpression() {
		if (this._columnNames == null || this._columnNames.length <= 0)
			return "";

		return String.format("ORDER BY %s %s", 
				StringUtils.join(this._columnNames, ","), this._sortType.toString());
	}

}
