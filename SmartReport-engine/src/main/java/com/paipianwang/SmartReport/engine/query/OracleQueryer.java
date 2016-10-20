package com.paipianwang.SmartReport.engine.query;

import java.sql.Connection;
import java.util.List;

import com.paipianwang.SmartReport.engine.data.ReportDataSource;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataRow;
import com.paipianwang.SmartReport.engine.data.ReportParameter;
import com.paipianwang.SmartReport.engine.data.ReportQueryParamItem;

public class OracleQueryer extends AbstractQueryer implements Queryer {

	public OracleQueryer(ReportDataSource ds, ReportParameter param) {
		super(ds, param);
	}
	
	@Override
	public List<ReportMetaDataColumn> parseMetaDataColumns(String sqlText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportQueryParamItem> parseQueryParamItems(String sqlText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportMetaDataRow> getMetaDataRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportMetaDataColumn> getMetaDataColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Connection getJdbcConnection() {
		// TODO Auto-generated method stub
		return null;
	}

}
