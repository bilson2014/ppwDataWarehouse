package com.paipianwang.SmartReport.engine.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.paipianwang.SmartReport.engine.data.ReportDataSource;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportParameter;

public class MySqlQueryer extends AbstractQueryer implements Queryer{

	public MySqlQueryer(ReportDataSource ds, ReportParameter param) {
		super(ds, param);
	}


	@Override
	protected Connection getJdbcConnection() {
		try {
			return DriverManager.getConnection(this.ds.getJdbcUrl(), this.ds.getUser(), this.ds.getPassword());
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected String preprocessSqlText(String sqlText) {
		sqlText = StringUtils.stripEnd(sqlText.trim(), ";");
        Pattern pattern = Pattern.compile("limit.*?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sqlText);
        if (matcher.find()) {
            sqlText = matcher.replaceFirst("");
        }
        return sqlText + " limit 1";
	}
	
	public List<ReportMetaDataColumn> parseMetaDataColumns(String sqlText) {
        List<ReportMetaDataColumn> result = super.parseMetaDataColumns(sqlText);
        if (result.size() == 0) {
            return result;
        }

        return result;
    }
}
