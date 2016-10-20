package com.paipianwang.SmartReport.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.paipianwang.SmartReport.data.Pagination;
import com.paipianwang.SmartReport.data.mapping.ColumnProperty;

/**
 * 基于JDBC的DAO的公共操作接口，对数据库提供标准的增删改查功能
 * @author Jack
 */
public interface IBaseJdbcDao {

	/**
	 * 执行更新SQL语句
	 * 
	 * @param sql 语句
	 * @return 返回成功更新的记录数
	 * @throws DataAccessException
	 */
	public int update(final String sql) throws DataAccessException;
	
	/**
	 * 执行更新SQL语句
	 * 
	 * @param sql sql语句
	 * @param args 参数值
	 * @return 返回成功更新的记录数
	 * @throws DataAccessException
	 */
	public int update(final String sql, final Object[] args) throws DataAccessException;
	
	/**
	 * 执行更新SQL语句
	 * 
	 * @param sql 语句
	 * @param args sql参数
	 * @param argTypes 参数类型
	 * @return 返回成功更新的记录数
	 * @throws DataAccessException
	 */
	public int update(final String sql,final Object[] args, final int[] argTypes) throws DataAccessException;
	
	/**
	 * 向表中插入一条记录并返回其自增ID
	 * 
	 * @param sql 语句
	 * @param args sql参数
	 * @param argTypes sql参数对应的数据类型
	 * @return 自增id
	 * @throws DataAccessException
	 */
	public int updateWithId(final String sql, final Object[] args, final int[] argTypes) throws DataAccessException;
	
	/**
	 * 向表中插入一条记录并返回其自增ID
	 * 
	 * @param sql 语句
	 * @param args sql参数
	 * @return 自增id
	 * @throws DataAccessException
	 */
	public int updateWithId(final String sql, final Object[] args) throws DataAccessException;
	
	/**
	 * 批量向表中增加或更新记录
	 * 
	 * @param sql sql语句
	 * @param list 等处理记录的列属性集合
	 * @return
	 * @throws DataAccessException
	 */
	public int batchUpdate(final String sql, final List<List<ColumnProperty>> list) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句，返回Stirng值
	 * 
	 * @param sql sql 语句
	 * @return Stirng值
	 * @throws DataAccessException
	 */
	public String queryForString(final String sql) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句，返回int值
	 * 
	 * @param sql sql 语句
	 * @return int值
	 * @throws DataAccessException
	 */
	public int queryForInt(final String sql) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句，返回long值
	 * 
	 * @param sql sql 语句
	 * @return long值
	 * @throws DataAccessException
	 */
	public long queryForLong(final String sql) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句，验证是否存在满足whereClause条件的数据且是唯一
	 * @param tableName
	 * @param whereClause
	 * @return 返回boolean
	 * @throws DataAccessException
	 */
	public boolean isExist(final String tableName, final String whereClause) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @param args
	 * @param poClass
	 * @return 结果列表
	 * @throws DataAccessException
	 */
	public <TEntity> List<TEntity> queryForList(final String sql, final Object[] args, final Class<TEntity> poClass) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param poClass
	 * @return 结果列表
	 * @throws DataAccessException
	 */
	public <TEntity> List<TEntity> queryForList(final String sql, final Object[] args, final int[] argTypes, final Class<TEntity> poClass) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @param poClass
	 * @return 结果列表
	 * @throws DataAccessException
	 */
	public <TEntity> List<TEntity> queryForList(final String sql, final Class<TEntity> poClass) throws DataAccessException;
	
	/**
	 * 执行查询SQL语句
	 *
	 * @param sql
	 * @param objectClass
	 * @return 返回Class的对象
	 * @throws DataAccessException
	 */
	public <T> Object queryForObject(final String sql, final Class<T> objectClass) throws DataAccessException;
	
	/**
	 * 执行分页查询SQL语句
	 * @param sql
	 * @param args
	 * @param pagination
	 * @param poClass
	 * @return 结果列表
	 * @throws DataAccessException
	 */
	public <TEntity> List<TEntity> queryByPage(final String sql, final Object[] args, final Pagination pagination, final Class<TEntity> poClass) throws DataAccessException;
	
	/**
	 * 执行分页查询SQL语句
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param pagination
	 * @param poClass
	 * @return
	 * @throws DataAccessException
	 */
	public <TEntity> List<TEntity> queryByPage(final String sql, final Object[] args, final int[] argTypes, final Pagination pagination, final Class<TEntity> poClass) throws DataAccessException;
	
	/**
	 * 执行分页查询SQL语句
	 * @param sql
	 * @param pagination
	 * @param poClass
	 * @return
	 * @throws DataAccessException
	 */
	public <TEntity> List<TEntity> queryByPage(final String sql, final Pagination pagination, final Class<TEntity> poClass) throws DataAccessException;
	
	/**
	 * 执行SQL语句得到记录总数
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	public long queryByCount(final String sql) throws DataAccessException;
	
	/**
	 * 执行SQL语句得到记录总数
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public long queryByCount(final String sql, final Object[] args) throws DataAccessException;
	
	/**
	 * 执行SQL语句得到记录总数
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 */
	public long queryByCount(final String sql, final Object[] args, final int[] argTypes) throws DataAccessException;
}
