package com.paipianwang.SmartReport.data.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.paipianwang.SmartReport.data.Pagination;
import com.paipianwang.SmartReport.data.mapping.ColumnProperty;

public class BaseJdbcDao extends JdbcDaoSupport implements IBaseJdbcDao{

	protected final Logger logger = Logger.getLogger(this.getClass());
	
	
	public BaseJdbcDao() {
		super();
	}
	
	@Resource
	public void setJDBCTemplate(final JdbcTemplate jdbcTemplate) {
		this.setJdbcTemplate(jdbcTemplate);
	}
	
	private void debugWrite(final String sql) {
		if(logger.isDebugEnabled()) {
			logger.debug("执行更新SQL语句: " + sql);
		}
	}
	
	private void debugWrite(String sql, int resultCount) {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，SQL=[" + sql + "]");
			logger.debug("返回结果列表:" + resultCount);
		}
	}
	
	@Override
	public int update(final String sql) throws DataAccessException {
		this.debugWrite(sql);
		return getJdbcTemplate().update(sql);
	}
	
	@Override
	public int update(final String sql, final Object[] args) throws DataAccessException {
		this.debugWrite(sql);
		return getJdbcTemplate().update(sql, args);
	}

	@Override
	public int update(final String sql, final Object[] args, final int[] argTypes) throws DataAccessException {
		this.debugWrite(sql);
		return getJdbcTemplate().update(sql, args, argTypes);
	}

	@Override
	public int updateWithId(final String sql, final Object[] args, final int[] argTypes) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update((PreparedStatementCreator) con -> {
			PreparedStatement pstat = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < args.length; i++) {
				if(args[i] == null){
					pstat.setNull(i + 1, Types.NULL);
				} else {
					pstat.setObject(i + 1, args[i], argTypes[i]);
				}
			}
			return pstat;
		}, keyHolder);
		
		this.debugWrite(sql);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int updateWithId(final String sql, final Object[] args) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update((PreparedStatementCreator) con -> {
			PreparedStatement pstat = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < args.length; i++) {
				if (args[i] == null) {
					pstat.setNull(i + 1, Types.NULL);
				} else {
					pstat.setObject(i + 1, args[i]);
				}
			}
			return pstat;
		},keyHolder);
		
		this.debugWrite(sql);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int batchUpdate(final String sql, final List<List<ColumnProperty>> list) throws DataAccessException {
		int[] rowsAffecteds = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement pstat, final int i) throws SQLException {
				List<ColumnProperty> columnProperties = list.get(i);
				for (int index = 0; index < columnProperties.size(); index++) {
					ColumnProperty columnProperty = columnProperties.get(index);
					pstat.setObject(index + 1, columnProperty.getValue());
				}
				
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		
		int rowsAffected = 0;
		for (int element : rowsAffecteds) {
			rowsAffected += element;
		}
		return rowsAffected;
	}

	@Override
	public String queryForString(final String sql) throws DataAccessException {
		this.debugWrite(sql);
		return this.getJdbcTemplate().queryForObject(sql, String.class);
	}

	@Override
	public int queryForInt(final String sql) throws DataAccessException {
		this.debugWrite(sql);
		return this.getJdbcTemplate().queryForObject(sql,Integer.class);
	}

	@Override
	public long queryForLong(final String sql) throws DataAccessException {
		this.debugWrite(sql);
		return this.getJdbcTemplate().queryForObject(sql, Long.class);
	}

	@Override
	public <TEntity> TEntity queryForObject(final String sql, final Class<TEntity> objectClass) throws DataAccessException {
		this.debugWrite(sql);
		return this.getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<TEntity> (objectClass));
	}

	@Override
	public boolean isExist(final String tableName, final String whereClause) throws DataAccessException {
		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("数据判存失败：表名为空!");
		}
		final StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT 1 FROM ").append(tableName).append(" WHERE ROWNUM = 1 ");
		if (StringUtils.isNotBlank(whereClause)) {
			sql.append(" AND (").append(whereClause).append(")");
		}
		
		final String sqlCmd = sql.toString();
		this.debugWrite(sqlCmd);
		return this.getJdbcTemplate().queryForList(sqlCmd).size() == 1;
	}

	@Override
	public <TEntity> List<TEntity> queryForList(final String sql, final Object[] args, final Class<TEntity> poClass)
			throws DataAccessException {
		final List<TEntity> list = this.getJdbcTemplate().query(sql, args,new BeanPropertyRowMapper<TEntity>(poClass));
		this.debugWrite(sql);
		return list;
	}

	@Override
	public <TEntity> List<TEntity> queryForList(final String sql, final Object[] args, final int[] argTypes, final Class<TEntity> poClass)
			throws DataAccessException {
		final List<TEntity> list = this.getJdbcTemplate().query(sql, args, argTypes, new BeanPropertyRowMapper<TEntity>(poClass));
		this.debugWrite(sql);
		return list;
	}

	@Override
	public <TEntity> List<TEntity> queryForList(final String sql, Class<TEntity> poClass) throws DataAccessException {
		final List<TEntity> list = this.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<TEntity>(poClass));
		this.debugWrite(sql);
		return list;
	}


	@Override
	public <TEntity> List<TEntity> queryByPage(String sql, final Object[] args, final Pagination pagination, final Class<TEntity> poClass)
			throws DataAccessException {
		if(pagination != null){
			pagination.setTotals(this.queryByCount(sql, args));
		}
		sql = this.preparePageSql(sql, pagination);
		final List<TEntity> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<TEntity>(poClass));
		this.debugWrite(sql, list.size());
		return list;
	}

	@Override
	public <TEntity> List<TEntity> queryByPage(String sql, Object[] args, int[] argTypes, Pagination pagination,
			Class<TEntity> poClass) throws DataAccessException {
		if (pagination != null) {
			pagination.setTotals(this.queryByCount(sql, args, argTypes));
		}
		sql = this.preparePageSql(sql, pagination);
		final List<TEntity> list = this.getJdbcTemplate().query(sql, args, new BeanPropertyRowMapper<TEntity>(poClass));
		this.debugWrite(sql, list.size());
		return list;
	}

	protected String preparePageSql(final String sql, final Pagination pagination) {
		if (pagination == null) {
			return sql;
		}
		
		final StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(sql);
		sqlBuf.append(String.format(" LIMIT %s,%s", pagination.getStartIndex(), pagination.getPageSize()));
		return sqlBuf.toString();
	}

	@Override
	public <TEntity> List<TEntity> queryByPage(final String sql, final Pagination pagination, final Class<TEntity> poClass)
			throws DataAccessException {
		return queryByPage(sql, pagination, poClass);
	}
	
	public <T> List<T> queryByPage(String sql, final Pagination page, final RowMapper<T> mapper) throws DataAccessException {
		if (page != null) {
			page.setTotals(this.queryByCount(sql));
		}
		sql = this.preparePageSql(sql, page);
		List<T> list = getJdbcTemplate().query(sql, mapper);
		this.debugWrite(sql, list.size());
		return list;
	}

	@Override
	public long queryByCount(final String sql) throws DataAccessException {
		final String sqlCmd = this.getQueryByCountSql(sql);
		return queryForLong(sqlCmd);
	}

	@Override
	public long queryByCount(final String sql, final Object[] args) throws DataAccessException {
		final String sqlCmd = this.getQueryByCountSql(sql);
		return this.getJdbcTemplate().queryForObject(sqlCmd, args, Long.class);
	}

	@Override
	public long queryByCount(final String sql, final Object[] args, final int[] argTypes) throws DataAccessException {
		final String sqlCmd = this.getQueryByCountSql(sql);
		return this.getJdbcTemplate().queryForObject(sqlCmd, args, argTypes, Long.class);
	}

	protected String getQueryByCountSql(final String sql) {
		final StringBuffer countSql = new StringBuffer(30 + sql.length());
		countSql.append(" SELECT COUNT(1) FROM (").append(sql).append(") Z");
		String sqlCmd = countSql.toString();
		this.debugWrite(sqlCmd);
		return sqlCmd;
	}

}
