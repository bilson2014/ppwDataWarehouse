package com.paipianwang.SmartReport.engine.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paipianwang.SmartReport.engine.data.ColumnType;
import com.paipianwang.SmartReport.engine.data.ReportDataSource;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataCell;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataRow;
import com.paipianwang.SmartReport.engine.data.ReportParameter;
import com.paipianwang.SmartReport.engine.data.ReportQueryParamItem;
import com.paipianwang.SmartReport.engine.exceptions.SQLQueryException;

/**
 * Queryer 基础类
 * @author Jack
 *
 */
public abstract class AbstractQueryer {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	protected ReportDataSource ds;
	protected ReportParameter param;
	protected List<ReportMetaDataColumn> metaColumns;
	
	public AbstractQueryer(ReportDataSource ds, ReportParameter param) {
		this.ds = ds;
		this.param = param;
		this.metaColumns = param == null ? 
				new ArrayList<ReportMetaDataColumn>(0) : 
				new ArrayList<ReportMetaDataColumn>(param.getMetaColumns());
		
	}
	
	public List<ReportMetaDataColumn> parseMetaDataColumns(final String sqlText) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ReportMetaDataColumn> columns = null;
		
		try {
			logger.debug(sqlText);
			conn = this.getJdbcConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(this.preprocessSqlText(sqlText));
			// 获取元数据
			final ResultSetMetaData rsMetaData = rs.getMetaData();
			// 获取元数据列数
			final int count = rsMetaData.getColumnCount();
			columns = new ArrayList<ReportMetaDataColumn>(count);

			for(int i = 1;i <= count; i ++) {
				final ReportMetaDataColumn column = new ReportMetaDataColumn();
				column.setName(rsMetaData.getColumnLabel(i));
				column.setDataType(rsMetaData.getColumnTypeName(i));
				column.setWidth(rsMetaData.getColumnDisplaySize(i));
				
				// 设置默认列类型
				if(i == 2) {
					column.setType(ColumnType.LAYOUT.getValue());
				} else if(i == 3) {
					column.setType(ColumnType.DIMENSION.getValue());
				} else {
					column.setType(ColumnType.STATISTICAL.getValue());
				}
					
				
				columns.add(column);
			}
		} catch (SQLException ex) {
			throw new RuntimeException("获取元数据出错", ex);
		} finally {
			releaseJdbcResource(conn, stmt, rs);
		}
		
		return columns == null ? new ArrayList<ReportMetaDataColumn>(0) : columns;
	}
	
	public List<ReportQueryParamItem> parseQueryParamItems(final String sqlText) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ReportQueryParamItem> rows = new ArrayList<ReportQueryParamItem>();
		
		try {
			logger.debug(sqlText);
			conn = this.getJdbcConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlText);
			while (rs.next()) {
				String name = rs.getString("name");
				String text = rs.getString("text");
				name = name == null ? "" : name.trim();
				text = text == null ? "" : text.trim();
				rows.add(new ReportQueryParamItem(name, text));
			}
		} catch (SQLException ex) {
			throw new RuntimeException("获取查询参数元数据错误", ex);
		} finally {
			this.releaseJdbcResource(conn, stmt, rs);
		}
		
		return rows;
	}
	
	public List<ReportMetaDataRow> getMetaDataRows() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			final String sqlText = param.getSqlText();
			logger.debug(sqlText);
			conn = this.getJdbcConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlText);
			return getMetaDataRows(rs, getSqlColumns(param.getMetaColumns()));
		} catch (SQLException ex) {
			logger.error(String.format("SqlText:%s，Msg:%s", this.param.getSqlText(), ex));
            throw new SQLQueryException(ex);
		} finally {
			releaseJdbcResource(conn, stmt, rs);
		}
		
	}
	
	 public List<ReportMetaDataColumn> getMetaDataColumns() {
	        return this.metaColumns;
	    }
	
	protected List<ReportMetaDataRow> getMetaDataRows(final ResultSet rs, final List<ReportMetaDataColumn> sqlColumns) 
			throws SQLException {
		final List<ReportMetaDataRow> rows = new ArrayList<ReportMetaDataRow>();
		while (rs.next()) {
			ReportMetaDataRow row = new ReportMetaDataRow();
			for (ReportMetaDataColumn column : sqlColumns) {
				Object value = rs.getObject(column.getName());
				if(column.getDataType().contains("BINARY")) {
					value = new String((byte[]) value);
				}
				row.add(new ReportMetaDataCell(column, column.getName(), value));
			}
			rows.add(row);
		}
		return rows;
	}

	/**
	 * 获取除计算列之外的所有元数据类型
	 * @param metaDataColumns
	 * @return
	 */
	protected List<ReportMetaDataColumn> getSqlColumns(List<ReportMetaDataColumn> metaDataColumns) {
		return metaDataColumns.parallelStream().
				filter(data -> data.getType() != ColumnType.COMPUTED)
				.collect(Collectors.toList());
	}

	/**
	 * 预处理获取列表集合的sql语句
	 * 可以处理全表查询的SQL，从而避免过多的内存消耗,避免性能问题的出现
	 * @param sqlText SQL语句
	 * @return
	 */
	protected String preprocessSqlText(final String sqlText) {
		return sqlText;
	}

	// 获取jdbc连接
	protected abstract Connection getJdbcConnection();
	
	// 释放数据库资源
	protected void releaseJdbcResource(final Connection conn, final Statement stmt, final ResultSet rs) {
		try {
			if(conn != null) {
				conn.close();
			}
			
			if(stmt != null) {
				stmt.close();
			}
			
			if(rs != null) {
				rs.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException("数据库资源释放异常", ex);
		} 
		
	}
	
}
