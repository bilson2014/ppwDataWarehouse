package com.paipianwang.SmartReport.domain.viewmodel;

import org.apache.commons.lang3.StringUtils;

public abstract class HtmlFormElement {

	protected String name;
	protected String text;
	protected String type;
	protected String dataType = "string";
	protected String comment;
	protected int width = 100;
	protected int height = 25;
	protected boolean isRequired;
	private String defaultText;
	private String defaultValue;

	protected HtmlFormElement(String name, String text) {
		this.name = name;
		this.text = text;
	}

	/**
     * 获取Html表单(Form)元素名称
     *
     * @return Html表单(Form)元素名称
     */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	 /**
     * 获取Html表单(Form)元素对应的标题(中文名)
     *
     * @return Html表单(Form)元素对应的标题(中文名)
     */
	public String getText() {
		return StringUtils.isBlank(text) ? this.name : text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
     * 获取Html表单(Form)元素对应的控件类型
     *
     * @return text|select|checkbox|datebox|checkboxlist
     */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
     * 获取Html表单(Form)元素的数据来源
     *
     * @param dataType
     */
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
     * 获取Html表单(Form)元素的备注
     *
     * @return
     */
	public String getComment() {
		return StringUtils.isBlank(comment) ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
     * 获取Html表单(Form)元素表单控件的宽度(单位：像素)
     * 默认像素100px
     * @return
     */
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
     * 获取Html表单(Form)元素表单控件的高度(单位：像素)
     *
     * @return 高度的像素值, 默认为25
     */
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
     * 获取Html表单(Form)元素的是否必选
     *
     * @return
     */
	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
     * 获取Html表单(Form)元素对应的默认值
     *
     * @return Html表单(Form)元素对应的默认值
     */
	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	/**
     * 获取Html表单(Form)元素的默认值对应的标题
     *
     * @return Html表单(Form)元素的默认值对应的标题
     */
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
