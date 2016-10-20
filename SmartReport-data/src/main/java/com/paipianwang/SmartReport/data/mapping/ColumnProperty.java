package com.paipianwang.SmartReport.data.mapping;

public class ColumnProperty {

	public Object value;
	public int sqlType;
	public ColumnProperty(Object value, int sqlType) {
		super();
		this.value = value;
		this.sqlType = sqlType;
	}
	public Object getValue() {
		return value;
	}

	public int getSqlType() {
		return sqlType;
	}

}
