package com.paipianwang.SmartReport.domain.viewmodel;

import java.util.List;

public class HtmlCheckBoxList extends HtmlFormElement {

	private List<HtmlCheckBox> value;

	public HtmlCheckBoxList(String name, String text, List<HtmlCheckBox> value) {
		super(name, text);
		this.type = "checkboxlist";
		this.value = value;
	}

	public List<HtmlCheckBox> getValue() {
		return value;
	}

}
