package com.paipianwang.SmartReport.data.jdbc;

import java.util.List;

import com.paipianwang.SmartReport.data.Pagination;
import com.paipianwang.SmartReport.data.SortType;

public interface IBaseDao<TEntity> {

	/**
	 * 添加一条数据
	 * @param entity 实体对象
	 * @return 影响记录的行数，-1表示操作失败，大于-1表示操作成功
	 */
	public int insert(final TEntity entity);
	
	/**
	 * 添加一条数据，并发挥插入记录的ID值
	 * @param entity 实体对象
	 * @return 插入记录的数据库自增标识
	 */
	public int insertWithId(final TEntity entity);
	
	/**
	 * 批量插入数据
	 * @param entities 实体对象集合
	 * @return 影响记录的行数
	 */
	public int batchInsert(final List<TEntity> entities);
	
	/**
	 * 根据指定条件，从数据库中删除记录
	 * @param condition 删除记录的条件语句，不需要带SQL语句的WHERE 关键字
	 * @param args SQL参数对应的集合，如果条件
	 * @return
	 */
	public int delete(final String condition, final Object... args);
	
	/**
	 * 清空表中的所有数据
	 * @return 影响记录的行数，-1表示操作失败，大于-1表示成功
	 */
	public int deleteAll();
	
	/**
	 * 清空表中的所有数据，且不记录数据库日志(调用truncate语句)
	 */
	public void truncate();
	
	/**
	 * 根据制定记录的ID，从数据库中的删除指定记录(用于整型主键)
	 * @param keyValue
	 */
	public int deleteByKey(final int keyValue);
	
	/**
	 * 根据指定记录的ID,删除指定记录(用于字符串型主键)
	 * @param keyValue 指定记录的ID值
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int deleteByKey(final String keyValue);
	
	/**
	 * 删除一个或多个指定标识的记录
	 * @param keyValues 记录标识数组
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int deleteByKey(final int[] keyValues);
	
	/**
	 * 删除一个或多个指定标识的记录
	 * @param keyValues 记录标识数组
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int deleteByKey(final String[] keyValues);
	
	/**
	 * 删除多条数据,条件为符合多个指定列的值,使用 IN 匹配
	 * @param keyValue 主键ID值集合以","号分割
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int deleteInByKey(final String keyValue);
	
	/**
	 * 更新数据库表中指定主键值的记录
	 * @param entity
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return
	 */
	public int update(final TEntity entity, final String... columnNames);
	
	/**
	 * 更新数据库表中指定主键值的记录
	 * @param entity 实体对象
	 * @param keyValue 表中主键的值
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int update(final TEntity entity, final int keyValue, final String... columnNames);
	
	/**
	 * 更新数据库表中的记录
	 * @param condition 不带WHERE的更新条件
	 * @param args SQL参数对应值的集合,该数组的值必须与columnNames按顺序对应
	 * @param coulmnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int update(final String condition, final Object[] args, final String[] coulmnNames);
	
	/**
	 * 更新数据库表中的记录
	 * @param entity 实体对象
	 * @param condition 不带WHERE的更新条件
	 * @param args SQL参数对应值得集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int update(final TEntity entity, final String condition, final Object[] args, final String... columnNames);
	
	/**
	 * 更新一条数据,条件为符合指定列的值,使用 IN 匹配提示: IN的条件仅用于主键列
	 * @param entity 实体对象
	 * @param keyValues 主键ID值以","号分割
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int updateInByKey(final TEntity entity, final String keyValues, final String... columnNames);
	
	/**
	 * 更新所有记录为制定entity中属性的值
	 * @param entity 实体对象
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int updateAll(final TEntity entity, final String... columnNames);
	
	/**
	 * 根据主键批量更新数据
	 * @param entities 更新的对象集合
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示成功
	 */
	public int batchUpdate(final List<TEntity> entities, final String... columnNames);
	
	/**
	 * 批量更新记录
	 * @param entities 更新的对象集合
	 * @param condition 指定的条件,SQL语句不要带WHERE关键字的条件
	 * @param cndColumnNames 条件中所命名用的列名
	 * @param columnNames 指定数据库表中需要更新的列名集合
	 * @return 影响记录的行数,-1表示操作失败,大于-1表示操作成功
	 */
	public int batchUpdate(final List<TEntity> entities, final String condition, final String[] cndColumnNames, final String... columnNames);
	
	/**
	 * 获取一条满足指定条件的实体对象(返回值需判断是否为null)
	 * @param condition 指定条件,要求SQL语句不带WHERE关键字的条件
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @param columnNames 查询的列名
	 * @return 实体对象或NULL
	 */
	public TEntity queryOne(final String condition, final Object[] args, final String... columnNames);
	
	/**
	 * 获取所有的实体对象集合(返回值不需要判断是否为NULL)
	 * @return 实体对象集合
	 */
	public List<TEntity> query();
	
	/**
	 * 获取所有的实体对象集合(返回值不需要判断是否为NULL)
	 * @param columnNames 查询的列名
	 * @return 实体对象集合
	 */
	public List<TEntity> query(final String[] columnNames);
	
	/**
	 * 获取所有的实体对象集合(返回值不需要判断是否为NULL)
	 * @param sortItem 排序字段名称，不要求带ORDER BY关键字,只要指定排序字段名称即可
	 * @param sortType 排序规则
	 * @param columnNames 查询的列名
	 * @return 实体对象集合
	 */
	public List<TEntity> query(final String sortItem, final SortType sortType, final String... columnNames);
	
	/**
	 * 获取满足指定条件的实体对象集合(返回值不需要判断是否为NULL)
	 * @param condition 指定的条件,不要求带SQL语句WHERE关键字的条件
	 * @param columnNames 查询的列名
	 * @return 指定条件的表中的实体对象集合
	 */
	public List<TEntity> query(final String condition, final String... columnNames);
	
	/**
	 * 获取满足指定条件的实体对象集合(返回值不需要判断NULL)
	 * @param condition 指定的条件,不要求带SQL语句WHERE关键字的条件
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @param columnNames 查询的列名
	 * @return 实体对象集合
	 */
	public List<TEntity> query(final String condition, final Object[] args, final String... columnNames);
	
	/**
	 * 获取满足指定条件的实体对象集合(返回值不需判断是否为NULL)
	 * @param condition 指定的条件,不要求带SQL语句Where关键字的条件
	 * @param sortItem 排序字段名称，不要求带ORDER BY关键字,只要指定排序字段名称即可
	 * @param sortType 排序规则
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @param columnNames 查询的列名
	 * @return 实体对象集合
	 */
	public List<TEntity> query(final String condition, final String sortItem, final SortType sortType, final Object[] args, final String... columnNames);
	
	/**
	 * 分页查询方法(返回不需要判断是否为NULL)
	 * @param condition 指定的条件,不要求带SQL语句Where关键字的条件
	 * @param page 分页参数对象
	 * @param columnNames 查询的列名
	 * @return 当前分页的所有取实体对象集合
	 */
	public List<TEntity> query(final String condition, final Pagination page, final String... columnNames);
	
	/**
	 * 分页查询(返回值不需要判断是否为NULL)
	 * @param condition 指定的条件,不要求带SQL语句Where关键字的条件
	 * @param page 分页参数对象
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @param columnNames 查询的列名
	 * @return 当前分页的所有取实体对象集合
	 */
	public List<TEntity> query(final String condition, final Pagination page, final Object[] args, final String... columnNames);
	
	/**
	 * 获取指定主键值的实体对象(返回值需要判断是否为NULL)
	 * @param keyValue 主键ID值集合以","号分割
	 * @param columnNames 查询的列名
	 * @return 实体对象
	 */
	public TEntity queryByKey(final String keyValue, final String... columnNames);
	
	/**
	 * 获取指定主键值的实体对象(返回值需要判断是否为NULL)
	 * @param keyValue 主键ID值
	 * @param columnNames 查询的列名
	 * @return 实体对象
	 */
	public TEntity queryByKey(final int keyValue, final String... columnNames);
	
	/**
	 * 获取所有实体对象集合(返回值不需要判断是否为NULL)
	 * @param keyValues 主键ID值
	 * @param columnNames 查询的列名
	 * @return 实体对象集合
	 */
	public List<TEntity> queryInByKey(final String keyValues, final String... columnNames);
	
	/**
	 * 获取最大ID值(没有记录的时候返回0)
	 * @return 最大ID值
	 */
	public int queryMaxId();
	
	/**
	 * 获取该对象指定属性的最大值(没有记录的时候返回0)
	 * @param condition 要求带SQL语句WHERE关键字的条件
	 * @param columName 表中的字段(列)名称,字段的值必须是整型数据
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @return 指定属性的最大值
	 */
	public int queryMaxValue(final String condition, final String columName, final Object... args);
	
	/**
	 * 获取数据库中表的总记录总数
	 * @return 表的记录总数
	 */
	public int count();
	
	/**
	 * 获取指定条件的记录总数
	 * @param condition 要求带SQL语句Where关键字的条件，如果不带Where关键字该方法将对表中所有记录执行操作
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @return 指定条件的记录总数
	 */
	public int count(final String condition, final Object... args);
	
	/**
	 * 判断指定条件的记录是否存在
	 * @param condition 指定的条件,不需要带SQL语句的Where关键字
	 * @param args SQL参数对应值的集合,如果条件字符串中含有参数标记,则必须设置该数组的值
	 * @return 存在则返回true,否则为false
	 */
	public boolean isExist(final String condition, final Object... args);
	
	/**
	 * 查询数据库,判断指定标识的记录是否存在。
	 *
	 * @param keyValue 主键ID值
	 * @return 存在则返回true,否则还false
	 */
	boolean isExistByKey(int keyValue);

	/**
	 * 查询数据库,判断指定标识的记录是否存在。
	 *
	 * @param keyValues 主键ID值集合
	 * @return 存在则返回true,否则还false
	 */
	boolean isExistByKey(int[] keyValues);
	
	/**
	 * 判断指定标识的记录是否存在
	 * @param keyValue 主键ID值
	 * @return 存在返回true,否则返回false
	 */
	public boolean isExistByKey(final String keyValue);
	
	/**
	 * 判断指定标识的记录是否存在
	 * @param keyValues 主键ID集合
	 * @return 存在则返回true,否则返回false
	 */
	public boolean isExistByKey(final String[] keyValues);
	
	/**
	 * 判断指定标识的记录是否存在,使用 IN 匹配
	 * @param keyValue 主键ID值以","号分割
	 * @return 存在则返回true,否则返回false
	 */
	public boolean isExistInByKey(final String keyValue);
}
