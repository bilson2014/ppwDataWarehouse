package com.paipianwang.SmartReport.common.viewmodel;

public class NameTextPair {

	private String name;
	private String text;
	private boolean selected;

	public NameTextPair() {

	}

	public NameTextPair(String name, String text, boolean selected) {
		super();
		this.name = name;
		this.text = text;
		this.selected = selected;
	}

	public NameTextPair(String name, String text) {
		super();
		this.name = name;
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
