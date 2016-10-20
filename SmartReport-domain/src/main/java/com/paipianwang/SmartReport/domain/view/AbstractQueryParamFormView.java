package com.paipianwang.SmartReport.domain.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paipianwang.SmartReport.common.viewmodel.NameTextPair;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlCheckBox;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlCheckBoxList;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlComboBox;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlDateBox;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlFormElement;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlTextBox;

/**
 * 报表参数表单视图基类
 * @author Jack
 *
 */
public abstract class AbstractQueryParamFormView {

	public List<NameTextPair> getTextList(List<HtmlFormElement> formElements) {
		if(formElements == null || formElements.isEmpty()) {
			return new ArrayList<NameTextPair>(0);
		}
		
		final List<NameTextPair> list = new ArrayList<>(formElements.size());
		for (final HtmlFormElement element : formElements) {
			final NameTextPair nameTextPair = this.createNameTextPair(element);
			if(nameTextPair != null) {
				list.add(nameTextPair);
			}
		}
		return list;
	}
	
	public Map<String, String> getTextMap(final List<HtmlFormElement> formElements) {
		if(formElements == null || formElements.isEmpty()) {
			return new HashMap<String, String>(0);
		}
		
		Map<String, String> textMap = new HashMap<String, String>(formElements.size());
		for (final HtmlFormElement element : formElements) {
			NameTextPair nameTextPair = this.createNameTextPair(element);
			if (nameTextPair != null)
                textMap.put(nameTextPair.getName(), nameTextPair.getText());
		}
		
		return textMap;
	}
	
	public String getFormHtmlText(final HtmlFormElement formElement) {
		final List<HtmlFormElement> list = new ArrayList<HtmlFormElement>(1);
		list.add(formElement);
		return this.getFormHtmlText(list);
	}

	public String getFormHtmlText(final List<HtmlFormElement> formElements) {
		List<NameTextPair> list = this.getTextList(formElements);
		int count = list.size();
		final StringBuilder htmlTextBuilder = new StringBuilder();
		for(int index = 0; index < count; index ++) {
			htmlTextBuilder.append(list.get(index).getText());
		}
		return htmlTextBuilder.toString();
	}

	protected NameTextPair createNameTextPair(final HtmlFormElement element) {
		if(element == null) {
			return null;
		}
		
		if("datebox".equals(element.getType())) {
			return new NameTextPair(element.getName(), this.getDateBoxText((HtmlDateBox) element));
		}
		
		if("textbox".equals(element.getType())) {
			return new NameTextPair(element.getName(), this.getTexBoxText((HtmlTextBox) element));
		}
		
		if ("checkbox".equals(element.getType())) {
            return new NameTextPair(element.getName(), this.getCheckBoxText((HtmlCheckBox) element));
        }
        if ("combobox".equals(element.getType())) {
            return new NameTextPair(element.getName(), this.getComboBoxText((HtmlComboBox) element));
        }
        if ("checkboxlist".equals(element.getType())) {
            return new NameTextPair(element.getName(), this.getCheckboxListText((HtmlCheckBoxList) element));
        }
		return null;
	}

	protected abstract String getDateBoxText(HtmlDateBox element);
	
	protected abstract String getTexBoxText(HtmlTextBox textBox);

    protected abstract String getCheckBoxText(HtmlCheckBox checkBox);

    protected abstract String getComboBoxText(HtmlComboBox comboBox);

    protected abstract String getCheckboxListText(HtmlCheckBoxList checkBoxList);
}
