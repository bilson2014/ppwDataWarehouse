package com.paipianwang.SmartReport.data.criterion;

import com.paipianwang.SmartReport.data.SortType;
import com.paipianwang.SmartReport.data.criterion.operands.AndConjOperand;
import com.paipianwang.SmartReport.data.criterion.operands.BetweenOperand;
import com.paipianwang.SmartReport.data.criterion.operands.Bracket;
import com.paipianwang.SmartReport.data.criterion.operands.BracketOperand;
import com.paipianwang.SmartReport.data.criterion.operands.ClauseOperand;
import com.paipianwang.SmartReport.data.criterion.operands.EqualOperand;
import com.paipianwang.SmartReport.data.criterion.operands.GreaterThanOperand;
import com.paipianwang.SmartReport.data.criterion.operands.GreaterThanOrEqualOperand;
import com.paipianwang.SmartReport.data.criterion.operands.GroupByOperand;
import com.paipianwang.SmartReport.data.criterion.operands.InOperand;
import com.paipianwang.SmartReport.data.criterion.operands.LessThanOperand;
import com.paipianwang.SmartReport.data.criterion.operands.LessThanOrEqualOperand;
import com.paipianwang.SmartReport.data.criterion.operands.LikeOperand;
import com.paipianwang.SmartReport.data.criterion.operands.NoneOperand;
import com.paipianwang.SmartReport.data.criterion.operands.NotEqualOperand;
import com.paipianwang.SmartReport.data.criterion.operands.NotInOperand;
import com.paipianwang.SmartReport.data.criterion.operands.NotLikeOperand;
import com.paipianwang.SmartReport.data.criterion.operands.OrConjOperand;
import com.paipianwang.SmartReport.data.criterion.operands.OrderByOperand;
import com.paipianwang.SmartReport.data.criterion.operands.SqlClause;

/**
 * 规则
 * @author Jack
 *
 */
public class Restrictions {

	public static final NoneOperand None = new NoneOperand();
	public static final AndConjOperand And = new AndConjOperand();
	public static final OrConjOperand Or = new OrConjOperand();
	
	public static BetweenOperand between(final String columnName, Object lowerValue, Object higherValue) {
		return new BetweenOperand(columnName, lowerValue, higherValue);
	}
	
	public static BracketOperand bracket(final Bracket bracket) {
		return new BracketOperand(bracket);
	}
	
	public static EqualOperand equal(final String columnName, final Object columnValue) {
		return new EqualOperand(columnName, columnValue);
	}
	
	public static ClauseOperand clause(SqlClause sqlClause) {
		return new ClauseOperand(sqlClause);
	}
	
	public static GreaterThanOperand greaterThan(final String columnName, final Object columnValue) {
		return new GreaterThanOperand(columnName, columnValue);
	}
	
	public static GreaterThanOrEqualOperand greaterThanOrEqual(final String columnName,final Object columnValue) {
		return new GreaterThanOrEqualOperand(columnName, columnValue);
	}
	
	public static InOperand in (final String columnName, final Object columnValue) {
		return new InOperand(columnName, columnValue);
	}
	
	public static LessThanOperand lessThan(final String columnName, final Object columnValue) {
		return new LessThanOperand(columnName, columnValue);
	}
	
	public static LessThanOrEqualOperand lessThanOrEqual(final String columnName, final Object columnValue) {
		return new LessThanOrEqualOperand(columnName, columnValue);
	}
	
	public static LikeOperand like(final String columnName, final Object columnValue) {
		return new LikeOperand(columnName, columnValue);
	}
	
	public static NotEqualOperand notEqual(final String columnName, final Object columnValue) {
		return new NotEqualOperand(columnName, columnValue);
	}
	
	public static NotInOperand notIn(String columnName, Object columnValue) {
		return new NotInOperand(columnName, columnValue);
	}
	
	public static NotLikeOperand notLike(String columnName, Object columnValue) {
		return new NotLikeOperand(columnName, columnValue);
	}

	public static OrderByOperand orderBy(SortType sortType, String... columnNames) {
		return new OrderByOperand(sortType, columnNames);
	}

	public static GroupByOperand groupBy(String... columnNames) {
		return new GroupByOperand(columnNames);
	}
}
