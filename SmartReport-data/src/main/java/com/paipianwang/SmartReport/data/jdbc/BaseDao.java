package com.paipianwang.SmartReport.data.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.paipianwang.SmartReport.data.Pagination;
import com.paipianwang.SmartReport.data.SortType;
import com.paipianwang.SmartReport.data.annotations.Column;
import com.paipianwang.SmartReport.data.exceptions.SQLConditionException;
import com.paipianwang.SmartReport.data.mapping.ColumnMap;
import com.paipianwang.SmartReport.data.mapping.ColumnProperty;
import com.paipianwang.SmartReport.data.mapping.EntityMapper;

public abstract class BaseDao<TEntity> extends BaseJdbcDao implements IBaseDao<TEntity>{

	protected String tableName = "";
	protected String primaryKey;
	
	public BaseDao(String tableName, String primaryKey) {
		this.tableName = tableName;
		this.primaryKey = primaryKey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public int insert(final TEntity entity) {
		final SqlExpression sqlExpr = this.generateInsertSqlExpression(this.getColumnMap(entity), this.tableName);
		return this.update(sqlExpr.getCommandText(), sqlExpr.getParameters());
	}

	@Override
	public int insertWithId(final TEntity entity) {
		final SqlExpression sqlExpr = this.generateInsertSqlExpression(this.getColumnMap(entity), this.tableName);
		return this.updateWithId(sqlExpr.getCommandText(), sqlExpr.getParameters());
	}

	@Override
	public int batchInsert(final List<TEntity> entities) {
		final SqlExpression sqlExpr = this.generateBatchInsertSqlExpression(entities, this.tableName);
		return this.batchUpdate(sqlExpr.getCommandText(), sqlExpr.getBatchParameters());
	}

	@Override
	public int delete(String condition, final Object... args) {
		if (StringUtils.isBlank(condition)) {
			throw new SQLConditionException("condition:条件表达式不能为NULL!");
		}
		condition = this.checkCondition(condition);
		final String sqlCmd = String.format("DELETE FROM %1$s %2$s", this.tableName, condition);
		return this.update(sqlCmd, args);
	}

	@Override
	public int deleteAll() {
		final String sqlCmd = String.format("DELETE FROM %s", this.tableName);
		return this.update(sqlCmd);
	}

	@Override
	public void truncate() {
		final String sqlCmd = String.format("TRUNCATE TABLE %s", this.tableName);
		this.update(sqlCmd);
	}

	@Override
	public int deleteByKey(final int keyValue) {
		final String condition = String.format("%1$s = %2$s", this.primaryKey, keyValue);
		return this.delete(condition);
	}

	@Override
	public int deleteByKey(final String keyValue) {
		final String condition = String.format("%s = ?", this.primaryKey);
		return this.delete(condition, keyValue);
	}

	@Override
	public int deleteByKey(final int[] keyValues) {
		final String[] keys = new String[keyValues.length];
		for(int i = 0;i < keyValues.length; i++){
			keys[i] = Integer.toString(keyValues[i]);
		}
		return this.deleteByKey(keys);
	}

	@Override
	public int deleteByKey(final String[] keyValues) {
		return this.deleteByKey(StringUtils.join(keyValues, ','));
	}

	@Override
	public int deleteInByKey(final String keyValues) {
		final String condition = String.format("%1$s IN(%2%s)", this.primaryKey, keyValues);
		return this.delete(condition);
	}

	@Override
	public int update(final TEntity entity, final String... columnNames) {
		final String condition = String.format("%s = ?", this.primaryKey);
		final Object keyValue = this.getPrimaryKeyValue(entity.getClass(), entity);
		final Object[] args = new Object[] {keyValue};
		return this.update(entity, condition, args, columnNames);
	}

	@Override
	public int update(final TEntity entity, final int keyValue, final String... columnNames) {
		final String condition = String.format("%1$s = %2$s", this.primaryKey, keyValue);
		return this.update(entity, condition, null, columnNames);
	}

	@Override
	public int update(String condition, final Object[] args, final String[] columnNames) {
		if (args == null || args.length == 0) {
			throw new NullPointerException("args:不能为NULL!");
		}
		if (columnNames == null || columnNames.length == 0) {
			throw new NullPointerException("columnNames:不能为NULL!");
		}
		if (args.length < columnNames.length) {
			throw new ArrayIndexOutOfBoundsException("参数:'args'长度不能小于'columnNames'的长度");
		}
		
		condition = this.checkCondition(condition);
		final ArrayList<String> columns = new ArrayList<String>(columnNames.length);
		for (final String columnName : columnNames) {
			columns.add(String.format("%s = ?", columnName));
		}
		
		final String sqlCmd = String.format("UPDATE %1$s SET %2$s %3$s ", tableName, StringUtils.join(columns, ','), condition);
		return this.update(sqlCmd, args);
	}

	@Override
	public int update(final TEntity entity, String condition, final Object[] args, final String... columnNames) {
		if (entity == null) {
			throw new NullPointerException("entity:未将对象引用到实例!");
		}
		if (StringUtils.isBlank(condition)) {
			throw new NullPointerException("condition:条件表达式不能为NULL!");
		}
		
		condition = this.checkCondition(condition);
		final SqlExpression sqlExpr = this.generateUpdateSqlExpression(condition, 
				this.getColumnMap(entity, columnNames), this.tableName, args);
		return this.update(sqlExpr.getCommandText(),sqlExpr.getParameters());
	}

	@Override
	public int updateInByKey(final TEntity entity, final String keyValues, final String... columnNames) {
		final String condition = String.format("%1$s IN(%2$s)", this.primaryKey, keyValues);
		return this.update(entity, condition, null, columnNames);
	}

	@Override
	public int updateAll(final TEntity entity, final String... columnNames) {
		if(entity == null) {
			throw new NullPointerException("entity:未将对象引用到实例!");
		}
		final SqlExpression sqlExpr = this.generateUpdateSqlExpression("", this.getColumnMap(entity, columnNames), this.tableName);
		return this.update(sqlExpr.getCommandText(), sqlExpr.getParameters());
	}

	@Override
	public int batchUpdate(final List<TEntity> entities, final String... columnNames) {
		final String condition = String.format(" WHERE %1$s = ?", this.primaryKey);
		final String[] cndColumnNames = new String[] {this.primaryKey};
		return this.batchUpdate(entities, condition, cndColumnNames, columnNames);
	}

	@Override
	public int batchUpdate(final List<TEntity> entities, final String condition, final String[] cndColumnNames, final String... columnNames) {
		final SqlExpression sqlExpr = this.generateBatchUpdateSqlExpression(entities, condition, cndColumnNames, this.tableName, columnNames);
		return this.batchUpdate(sqlExpr.getCommandText(), sqlExpr.getBatchParameters());
	}

	@Override
	public TEntity queryOne(final String condition, final Object[] args, String... columnNames) {
		final List<TEntity> list = this.query(condition, args, columnNames);
		return (list == null || list.isEmpty()) ? null : list.get(0);
	}

	@Override
	public List<TEntity> query() {
		return this.query("");
	}

	@Override
	public List<TEntity> query(final String[] columnNames) {
		return this.query("", "", SortType.ASC, null, columnNames);
	}

	@Override
	public List<TEntity> query(final String sortItem, final SortType sortType, final String... columnNames) {
		return this.query("", sortItem, sortType, null, columnNames);
	}

	@Override
	public List<TEntity> query(final String condition, final String... columnNames) {
		return this.query(condition, "", SortType.ASC, null, columnNames);
	}

	@Override
	public List<TEntity> query(final String condition, final Object[] args, final String... columnNames) {
		return this.query(condition, "", SortType.ASC, args, columnNames);
	}

	@Override
	public List<TEntity> query(final String condition, final String sortItem, final SortType sortType, final Object[] args,
			final String... columnNames) {
		final Pagination pagination = new Pagination();
		pagination.setSortItem(sortItem);
		pagination.setSortType(sortType.toString());
		pagination.setPageSize(-1);
		return this.query(condition, pagination, args, columnNames);
	}

	@Override
	public List<TEntity> query(final String condition, final Pagination pagination, final String... columnNames) {
		return this.query(condition, pagination, null, columnNames);
	}

	@Override
	public List<TEntity> query(String condition, final Pagination pagination, final Object[] args, final String... columnNames) {
		condition = this.checkCondition(condition);
		final String columns = this.getColumns(columnNames);
		final String orderByClause = StringUtils.isBlank(pagination.getSortItem()) ? "" : String.format("ORDER BY %1$s %2$s", pagination.getSortItem(), pagination.getSortType());
		final String sqlCmd = String.format("SELECT %1$s FROM %2$s %3$s %4$s", columns, this.tableName, condition, orderByClause);
		
		final Class<TEntity> clazz = this.getParameterizedType(this.getClass());
		// 不分页查询
		if(pagination.getPageSize() == -1) {
			return this.queryForList(sqlCmd, args, clazz);
		}
		return this.queryByPage(sqlCmd, args, pagination, clazz);
	}

	@Override
	public TEntity queryByKey(final String keyValue, final String... columnNames) {
		final String condition = String.format("%1$s = %2$s", this.primaryKey, keyValue);
		return this.queryOne(condition, null, columnNames);
	}

	@Override
	public TEntity queryByKey(final int keyValue, final String... columnNames) {
		final String condition = String.format("%1$s = ?", this.primaryKey);
		return this.queryOne(condition, new Object[]{ keyValue }, columnNames);
	}

	@Override
	public List<TEntity> queryInByKey(final String keyValues, final String... columnNames) {
		String condition = String.format("%1$s IN (%2$S)", this.primaryKey, keyValues);
		return this.query(condition, columnNames);
	}

	@Override
	public int queryMaxId() {
		return this.queryMaxValue("", this.primaryKey);
	}

	@Override
	public int queryMaxValue(String condition, final String columnName, final Object... args) {
		condition = this.checkCondition(condition);
		final StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT MAX(%1$s) AS MaxValue FROM %2$s %3$s");
		final String sqlCmd = String.format(strSql.toString(), columnName, this.tableName, condition);
		return this.getJdbcTemplate().queryForObject(sqlCmd, args, Integer.class);
	}

	@Override
	public int count() {
		return this.count("");
	}

	@Override
	public int count(String condition, final Object... args) {
		condition = this.checkCondition(condition);
		final StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(1) AS total FROM %1$s %2$S");
		final String sqlCmd = String.format(strSql.toString(), this.tableName, condition);
		return this.getJdbcTemplate().queryForObject(sqlCmd, args, Integer.class);
	}

	@Override
	public boolean isExist(String condition, Object... args) {
		condition = this.checkCondition(condition);
		final StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(1) FROM %1$s %2$s");
		final String sqlCmd = String.format(strSql.toString(), this.tableName, condition);
		return this.getJdbcTemplate().queryForObject(sqlCmd, args, Integer.class) > 0;
	}

	@Override
	public boolean isExistByKey(final int keyValue) {
		final String condition = String.format("%1$s = %2$s", this.primaryKey, keyValue);
		return this.isExist(condition);
	}

	@Override
	public boolean isExistByKey(final int[] keyValues) {
		String[] keys = new String[keyValues.length];
		for (int i = 0; i < keyValues.length; i++) {
			keys[i] = Integer.toString(keyValues[i]);
		}
		return this.isExistByKey(keys);
	}
	
	@Override
	public boolean isExistByKey(final String keyValue) {
		String condition = String.format("$s = ?", this.primaryKey);
		final Object[] args = new Object[] {keyValue};
		return this.isExist(condition, args);
	}

	@Override
	public boolean isExistByKey(final String[] keyValues) {
		return this.isExistByKey(StringUtils.join(keyValues, ','));
	}

	@Override
	public boolean isExistInByKey(final String keyValue) {
		final String condition = String.format("%1$s IN (%2$S)", keyValue);
		return this.isExist(condition);
	}
	
	protected SqlExpression generateInsertSqlExpression(final ColumnMap columnMap, final String tableName) {
		if (columnMap == null || columnMap.size() == 0) {
			throw new NullPointerException("columnMap:列映射对象不能为NULL");
		}
		
		final String[] fields = new String[columnMap.size()];
		final String[] argSymbols = new String[columnMap.size()];
		final Object[] args = new Object[columnMap.size()];
		final int[] argTypes = new int[columnMap.size()];
		
		int i = 0;
		for (final String key : columnMap.keySet()) {
			fields[i] = key;
			argSymbols[i] = " ? ";
			args[i] = columnMap.get(key).getValue();
			argTypes[i] = columnMap.get(key).getSqlType();
			i ++;
		}
		
		final String commandText = String.format("INSERT INTO %1$s (%2$s) VALUES (%3$s)", tableName, StringUtils.join(fields,
				','), StringUtils.join(argSymbols, ','));
		return new SqlExpression(commandText, args, argTypes);
	}
	
	protected SqlExpression generateBatchInsertSqlExpression(final List<TEntity> entities, final String tableName2) {
		if (entities == null || entities.size() == 0) {
			throw new NullPointerException("entities:批量增加的数据集合不能为NULL!");
		}
		
		ColumnMap columnMap = this.getColumnMap(entities.get(0));
		final String[] fields = new String[columnMap.size()];
		final String[] argSymbols = new String[columnMap.size()];
		int i = 0;
		for (final String key : columnMap.keySet()) {
			fields[i] = key;
			argSymbols[i] = " ? ";
			i ++;
		}
		
		final List<List<ColumnProperty>> batchParameters = new ArrayList<List<ColumnProperty>>(entities.size());
		for (TEntity entity : entities) {
			columnMap = this.getColumnMap(entity);
			final List<ColumnProperty> columnProperties = new ArrayList<ColumnProperty>(columnMap.size());
			for (final String key : columnMap.keySet()) {
				columnProperties.add(columnMap.get(key));
			}
			batchParameters.add(columnProperties);
		}
		
		final String commandText = String.format("INSERT INTO %1$s (%2$s) VALUES (%3$s)", tableName, StringUtils.join(fields, ','), StringUtils.join(argSymbols, ','));
		return new SqlExpression(commandText, batchParameters);
	}
	
	protected ColumnMap getColumnMap(TEntity entity, String... columnNames) {
		return this.getColumnMap(entity, true, columnNames);
	}

	protected ColumnMap getColumnMap(TEntity entity, boolean isFilter, String... columnNames) {
		if (entity == null) {
			throw new NullPointerException("entity:对象不能为空");
		}
		return EntityMapper.getColumnMap(entity, true, columnNames);
	}
	
	protected String checkCondition(final String condition) {
		if (this.containWhere(condition)) {
			return condition;
		}
		
		if (StringUtils.isNotBlank(condition)) {
			return " WHERE " + condition;
		}
		return StringUtils.EMPTY;
	}

	protected boolean containWhere(final String condition) {
		return (StringUtils.isNotBlank(condition) && condition.trim().toUpperCase().startsWith("WHERE"));
	}
	
	protected Object getPrimaryKeyValue(final Class<?> clazz, final TEntity entity) {
		Object value = null;
		final Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return value;
		}
		
		try {
			for (final Field field : fields) {
				final Column column = field.getAnnotation(Column.class);
				if (column != null && column.isPrimaryKey()) {
					field.setAccessible(true);
					return field.get(entity);
				}
			}
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		
		return value;
	}
	
	protected SqlExpression generateUpdateSqlExpression(final String condition, final ColumnMap columnMap, final String tableName,
			final Object... cndArgs) {
		if (columnMap == null || columnMap.size() == 0) {
			throw new NullPointerException("columnMap:列映射对象为NULL!");
		}
		
		final int initCapacity = columnMap.size() + (cndArgs == null ? 0 : cndArgs.length);
		final ArrayList<Object> args = new ArrayList<Object>(initCapacity);
		final ArrayList<String> columns = new ArrayList<String>(columnMap.size());
		
		for (final String key : columnMap.keySet()) {
			columns.add(String.format("%s = ?", key));
			args.add(columnMap.get(key).getValue());
		}
		
		if (cndArgs != null && cndArgs.length > 0) {
			args.addAll(Arrays.asList(cndArgs));
		}
		
		final String commandText = String.format("UPDATE %1$s SET %2$s %3$s", tableName, StringUtils.join(columns, ','), condition);
		return new SqlExpression(commandText, args.toArray(), null);
	}
	
	protected SqlExpression generateBatchUpdateSqlExpression(final List<TEntity> entities, final String condition,
			final String[] cndColumnNames, final String tableName, final String[] columnNames) {
		if (entities == null || entities.size() == 0) {
			throw new NullPointerException("entities:批量修改的数据集合不能为NULL!");
		}
		
		ColumnMap columnMap = this.getColumnMap(entities.get(0), columnNames);
		final ArrayList<String> columns = new ArrayList<String>(columnMap.size());
		for (final String key : columnMap.keySet()) {
			columns.add(String.format("%s = ?", key));
		}
		
		final List<List<ColumnProperty>> batchParameters = new ArrayList<>(entities.size());
		for (final TEntity entity : entities) {
			columnMap = this.getColumnMap(entity, columnNames);
			final int initCapacity = columnMap.size() + (cndColumnNames == null ? 0 : cndColumnNames.length);
			final List<ColumnProperty> columnProperties = new ArrayList<>(initCapacity);
			for (final String key : columnMap.keySet()) {
				columnProperties.add(columnMap.get(key));
			}
			if (cndColumnNames != null && cndColumnNames.length > 0) {
				columnMap = this.getColumnMap(entity, false, cndColumnNames);
				for (final String cndColumnName : cndColumnNames) {
					columnProperties.add(columnMap.get(cndColumnName));
				}
			}
			batchParameters.add(columnProperties);
		}
		
		final String commandText = String.format("UPDATE %1$s SET %2$s %3$s ",
				tableName, StringUtils.join(columns, ','), condition);
		return new SqlExpression(commandText, batchParameters);
	}
	
	private String getColumns(final String ... columnNames) {
		return (columnNames == null || columnNames.length == 0) ? "*" : StringUtils.join(columnNames, ',');
	}
	
	/**
	 * 获取父类泛型
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<TEntity> getParameterizedType(final Class<?> type) {
		return (Class<TEntity>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
