package com.paipianwang.SmartReport.domain.viewmodel;

public class HtmlCheckBox extends HtmlFormElement {

	private String value;
	private boolean checked;
	
	public HtmlCheckBox(String name, String text, String value) {
		super(name, text);
		this.value = value;
		this.type = "checkbox";
	}

	public String getValue() {
		return value;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
