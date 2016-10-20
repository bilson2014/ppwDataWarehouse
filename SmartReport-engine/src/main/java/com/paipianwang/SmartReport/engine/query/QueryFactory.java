package com.paipianwang.SmartReport.engine.query;

import org.apache.commons.lang.StringUtils;

import com.paipianwang.SmartReport.engine.data.ReportDataSource;
import com.paipianwang.SmartReport.engine.data.ReportParameter;

public class QueryFactory {

	public static Queryer create(final String jdbcUrl, final String user, final String password) {
		final ReportDataSource ds = new ReportDataSource(jdbcUrl, user, password);
		return create(ds, null);
	}

	public static Queryer create(final ReportDataSource ds, final ReportParameter param) {
		if(ds != null) {
			final String jdbcUrl = ds.getJdbcUrl();
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:mysql")) {
				return new MySqlQueryer(ds, param);
			}
			
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:oracle")) {
				return new OracleQueryer(ds, param);
			}
			
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:sqlserver")) {
				return new SqlServerQueryer(ds, param);
			}
			
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:sqlite")) {
				return new SQLiteQueryer(ds, param);
			}
			
			if(StringUtils.containsIgnoreCase(ds.getJdbcUrl(), "jdbc:phoenix")) {
				return new HBaseQueryer(ds, param);
			}
		}
		return null;
	}
	
	
}
