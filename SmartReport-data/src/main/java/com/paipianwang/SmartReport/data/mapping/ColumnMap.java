package com.paipianwang.SmartReport.data.mapping;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ColumnMap extends HashMap<String, ColumnProperty> {

	public ColumnMap() {
		
	}
	
	public ColumnMap(int initalCapacity) {
		super(initalCapacity);
	}
	
	public ColumnMap(int initalCapacity, float loadFactor) {
		super(initalCapacity, loadFactor);
	}
}
