package com.paipianwang.SmartReport.engine.query;

import java.util.List;

import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataRow;
import com.paipianwang.SmartReport.engine.data.ReportQueryParamItem;

/**
 * 报表查询器借口
 * @author Jack
 *
 */
public interface Queryer {

	/**
	 * 从SQL语句中解析出报表的元数据列集合
	 * 
	 * @param sqlText SQL语句
	 * @return 元数据列表
	 */
	public List<ReportMetaDataColumn> parseMetaDataColumns(final String sqlText);
	
	/**
	 * 从SQL语句中解析出报表查询参数(如下拉列表参数)的列表项集合
	 * @param sqlText SQL语句
	 * @return 查询参数集合
	 */
	public List<ReportQueryParamItem> parseQueryParamItems(final String sqlText);
	
	/**
	 * 获取报表元数据行集合
	 * @return
	 */
	public List<ReportMetaDataRow> getMetaDataRows();
	
	/**
	 * 获取报表元数据列集合
	 * @return
	 */
	public List<ReportMetaDataColumn> getMetaDataColumns();
}
