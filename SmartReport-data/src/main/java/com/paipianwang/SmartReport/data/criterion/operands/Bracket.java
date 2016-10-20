package com.paipianwang.SmartReport.data.criterion.operands;

public enum Bracket {
	Left("("), Right(")");
	
	private Bracket(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation() {
		return this.abbreviation;
	}
	
	private String abbreviation;
}
