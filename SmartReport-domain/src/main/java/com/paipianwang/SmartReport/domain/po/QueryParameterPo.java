package com.paipianwang.SmartReport.domain.po;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 报表查询参数
 * 
 * @author Jack
 *
 */
public class QueryParameterPo implements Serializable {

	private static final long serialVersionUID = -468198018022142275L;
	/**
	 * 报表参数名称
	 */
	private String name;
	/**
	 * 报表参数对应的标题
	 */
	private String text;
	/**
	 * 表单元素(input / select)
	 */
	private String formElement;
	/**
	 * 报表查询参数对应的内容(sql语句 | 文本字符 | 空)
	 */
	private String content;
	/**
	 * 报表查询参数对应的默认值
	 */
	private String defaultValue;
	/**
	 * 报表查询参数对应的默认值内容
	 */
	private String defaultText;
	/**
	 * 获取报表参数的数据源
	 */
	private String dataSource;
	/**
	 * 报表查询参数的数据类型(string | float | integer | date)
	 * 默认值是string
	 */
	private String dataType = "string";
	/**
	 * 报表参数备注
	 */
	private String comment;
	/**
	 * 报表参数form元素宽度(单位:px)
	 * 默认值为 100px
	 */
	private int width = 100;
	/**
	 * 报表参数form元素高度(单位:px)
	 * 默认值为25px
	 */
	private int height = 25;
	/**
	 * 报表查询参数是否必选
	 */
	private boolean isRequired;
	/**
	 * 报表查询参数是否自动提示(主要用于下拉列表控件中)
	 */
	private boolean isAutoComplete;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		if(StringUtils.isBlank(this.text)) {
			return "noTitle";
		}
		return this.text.trim();
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFormElement() {
		return StringUtils.isBlank(this.formElement) ? "" : this.formElement.trim();
	}

	public void setFormElement(String formElement) {
		this.formElement = formElement;
	}

	public String getContent() {
		return StringUtils.isBlank(content) ? "" : this.content.trim();
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDefaultValue() {
		return StringUtils.isBlank(this.defaultValue) ? "noDefaultValue" : this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultText() {
		return StringUtils.isBlank(this.defaultText) ? "noDefaultText" : this.defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataType() {
		return StringUtils.isBlank(this.dataType) ? "string" : this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getComment() {
		return StringUtils.isBlank(this.comment) ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isAutoComplete() {
		return isAutoComplete;
	}

	public void setAutoComplete(boolean isAutoComplete) {
		this.isAutoComplete = isAutoComplete;
	}

	/**
	 * 判断是否设置默认值
	 * @return
	 */
	@JsonIgnore
	public boolean hasDefaultValue() {
		return !this.getDefaultValue().equals("noDefaultValue");
	}
	
	/**
	 * 判断当前参数是否设置了默认标题
	 * @return
	 */
	@JsonIgnore
	public boolean hasDefaultText() {
		return !this.getDefaultText().equals("noDefaultText");
	}
	
	/**
	 * 获取当前查询参数的真实默认标题
	 * @return
	 */
	@JsonIgnore
	public String getRealDefaultText() {
		return this.hasDefaultText() ? this.getDefaultText() : "";
	}
	
	/**
	 * 获取当前查询参数的真实默认值
	 * @return
	 */
	@JsonIgnore
	public String getRealDefaultValue() {
		return this.hasDefaultValue() ? this.getDefaultValue() : "";
	}
}
