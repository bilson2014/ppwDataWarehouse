package com.paipianwang.SmartReport.engine.data;

/**
 * 报表元数据列
 * 
 * @author Jack
 *
 */
public class ReportMetaDataColumn {

	// 元数据列顺序
	private int ordinal;
	// 列名
	private String name;
	// 别名
	private String text;
	// 数据类型
	private String dataType;
	// 表达式
	private String expression;
	// 格式化
	private String format;
	// 备注
	private String comment;
	// 长度
	private int width;
	// 精度
	private int decimals;
	// 列类型
	private ColumnType type = ColumnType.DIMENSION;
	// 排序类型
	private ColumnSortType sortType = ColumnSortType.DEFAULT;
	// 是否支持小计
	private boolean isExtensions;
	// 是否显示footer
	private boolean isFootings;
	// 是否百分数显示
	private boolean isPercent;
	// 是否可选
	private boolean isOptional;
	// 是否在邮件显示
	private boolean isDisplayInMail;
	// 是否隐藏
	private boolean isHidden;

	public ReportMetaDataColumn() {

	}

	public ReportMetaDataColumn(String name, String text, ColumnType type) {
		this.name = name;
		this.text = text;
		this.type = type;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public String getName() {
		return this.name == null ? "" : name.trim().toLowerCase();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		if (this.text == null || this.text.trim().length() == 0) {
			return this.name;
		}
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getExpression() {
		return this.expression == null ? "" : this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * 获取报表元数据列的格式 String.format(format,text)
	 * 
	 * @return
	 */
	public String getFormat() {
		return this.format == null ? "" : this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getComment() {
		return this.comment == null ? "" : this.comment;
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

	public int getDecimals() {
		return decimals;
	}

	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	public ColumnType getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = ColumnType.valueOf(type);
	}

	public ColumnSortType getSortType() {
		return sortType;
	}

	public void setSortType(final int sortType) {
		this.sortType = ColumnSortType.valueOf(sortType);
	}

	public boolean isExtensions() {
		return isExtensions;
	}

	public void setExtensions(boolean isExtensions) {
		this.isExtensions = isExtensions;
	}

	public boolean isFootings() {
		return isFootings;
	}

	public void setFootings(boolean isFootings) {
		this.isFootings = isFootings;
	}

	public boolean isPercent() {
		return isPercent;
	}

	public void setPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public boolean isDisplayInMail() {
		return isDisplayInMail;
	}

	public void setDisplayInMail(boolean isDisplayInMail) {
		this.isDisplayInMail = isDisplayInMail;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public ReportMetaDataColumn clone() {
		return this.clone(this.name, this.text);
	}

	public ReportMetaDataColumn clone(final String name, final String text) {
		return this.clone(name, text, this.isPercent);
	}

	public ReportMetaDataColumn clone(final String name, final String text, final boolean isPercent) {
		final ReportMetaDataColumn column = new ReportMetaDataColumn();
		column.setName(name);
		column.setText(text);
		column.setPercent(isPercent);
		column.setDataType(dataType);
		column.setExpression(expression);
		column.setFootings(isFootings);
		column.setHidden(isHidden);
		column.setSortType(sortType.getValue());
		column.setType(type.getValue());
		column.setWidth(width);
		column.setDecimals(decimals);
		column.setOptional(isOptional);
		column.setDisplayInMail(isDisplayInMail);
		column.setComment(comment);
		return column;
	}
}
