package com.paipianwang.SmartReport.engine.data;

/**
 * 报表列类型
 * @author Jack
 *
 */
public enum ColumnType {
	
	/**
	 * 布局列
	 */
	LAYOUT(1),
	
	/**
	 * 维度列
	 */
	DIMENSION(2),
	
	/**
	 * 统计列
	 */
	STATISTICAL(3),
	
	/**
	 * 计算列
	 */
	COMPUTED(4);
	

	private final int value;

	private ColumnType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ColumnType valueOf(int arg){
		switch (arg) {
			case 1:
				return LAYOUT;
			case 2:
				return DIMENSION;
			case 3:
				return STATISTICAL;
			case 4:
				return COMPUTED;
			default:
				return DIMENSION;
			}
	}
	
}
