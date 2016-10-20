package com.paipianwang.SmartReport.engine.data;

public enum LayoutType {
	/**
	 * 横向布局
	 */
	HORIZONTAL(1),
	/**
	 * 纵向布局
	 */
	VERTICAL(2);
	
	private final int value;

	public int getValue() {
		return value;
	}

	private LayoutType(int value) {
		this.value = value;
	}
	
	public static LayoutType valueOf(int arg) {
		switch (arg) {
			case 1:
				return HORIZONTAL;
			case 2:
				return VERTICAL;
			default:
				return HORIZONTAL;
		}
	}
}
