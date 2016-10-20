package com.paipianwang.SmartReport.engine.data;

/**
 * 排序类型
 * @author Jack
 *
 */
public enum ColumnSortType {

	/**
	 * 默认
	 */
	DEFAULT(0),
	
	/**
	 * 数字优先升序排列
	 */
	DIGIT_ASCENDING(1),
	
	/**
	 * 数字优先降序排列
	 */
	DIGIT_DESCENDING(2),
	
	/**
	 * 字符优先升序排列
	 */
	CHAR_ASCENDING(3),
	
	/**
	 * 字符优先降序排列
	 */
	CHAR_DESCENDING(4);
	
	private final int value;

	private ColumnSortType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ColumnSortType valueOf(int arg) {
		switch (arg) {
			case 1:
				return DEFAULT;
			case 2:
				return DIGIT_ASCENDING;
			case 3:
				return DIGIT_DESCENDING;
			case 4:
				return CHAR_ASCENDING;
			case 5:
				return CHAR_DESCENDING;
			default:
				return DEFAULT;
		}
	}
}
