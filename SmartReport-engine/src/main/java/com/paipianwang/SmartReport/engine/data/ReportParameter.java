package com.paipianwang.SmartReport.engine.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 报表参数类
 * @author Jack
 *
 */
public class ReportParameter implements Serializable {

	private static final long serialVersionUID = -4880511437347685763L;

	/**
	 * 报表唯一ID
	 */
	private String id;
	/**
	 * 报表名称
	 */
	private String name;
	/**
	 * 报表布局列形式(1:横向, 2:纵向)
	 */
	private LayoutType layout;
	/**
	 * 报表统计列或计算列布局形式(1:横向, 2:纵向)
	 */
	private LayoutType statColumnLayout;
	/**
	 * 报表SQL查询语句
	 */
	private String sqlText;
	/**
	 * 报表元数据列集合
	 */
	private List<ReportMetaDataColumn> metaColumns;
	/**
	 * 报表中启用的统计(含计算列)列名集合
	 */
	private Set<String> enabledStatColumns = new HashSet<String>();
	/**
	 * 是否生成跨行表格
	 * 默认true
	 */
	private boolean isRowSpan = true;
	

	public ReportParameter() {
		
	}
	

	public ReportParameter(String id, String name, int layout, int statColumnLayout,
            List<ReportMetaDataColumn> metaColumns, Set<String> enabledStatColumns, boolean isRowSpan, String sqlText) {
			this.id = id;
			this.name = name;
			this.layout = LayoutType.valueOf(layout);
			this.statColumnLayout = LayoutType.valueOf(statColumnLayout);
			this.metaColumns = metaColumns;
			this.enabledStatColumns = enabledStatColumns;
			this.isRowSpan = isRowSpan;
			this.sqlText = sqlText;
}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LayoutType getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = LayoutType.valueOf(layout);
	}

	public LayoutType getStatColumnLayout() {
		return statColumnLayout;
	}

	public void setStatColumnLayout(int statColumnLayout) {
		this.statColumnLayout = LayoutType.valueOf(statColumnLayout);
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public List<ReportMetaDataColumn> getMetaColumns() {
		return metaColumns;
	}

	public void setMetaColumns(List<ReportMetaDataColumn> metaColumns) {
		this.metaColumns = metaColumns;
	}

	public Set<String> getEnabledStatColumns() {
		return enabledStatColumns;
	}

	public void setEnabledStatColumns(Set<String> enabledStatColumns) {
		this.enabledStatColumns = enabledStatColumns;
	}

	public boolean isRowSpan() {
		return isRowSpan;
	}

	public void setRowSpan(boolean isRowSpan) {
		this.isRowSpan = isRowSpan;
	}

}
