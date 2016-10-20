package com.paipianwang.SmartReport.data.jdbc;

import java.util.List;

import com.paipianwang.SmartReport.data.Pagination;

public abstract class BaseService<TDao extends BaseDao<TEntity>, TEntity> {

	protected TDao dao;
	
	public BaseService(TDao dao) {
		this.dao = dao;
	}
	
	public TDao getDao() {
		return this.dao;
	}
	
	/**
	 * 根据数据库自增ID判断该数据是否存在
	 * @param id 
	 * @return
	 */
	public boolean isExist(int id) {
		return this.dao.isExistByKey(id);
	}
	
	/**
	 * 根据业务ID判断该数据是否存在
	 * @param id
	 * @return
	 */
	public boolean isExist(final String id) {
		return this.dao.isExistByKey(id);
	}
	
	/**
	 * 添加一条数据，并发挥插入记录的ID值
	 * @param entity
	 * @return
	 */
	public int addWithId(final TEntity entity) {
		return this.dao.insertWithId(entity);
	}
	
	/**
	 * 添加一条数据
	 * @param entity 实体
	 * @return 是否成功
	 */
	public boolean add(final TEntity entity) {
		return this.dao.insert(entity) > 0;
	}
	
	/**
	 * 根据制定记录的ID，从数据库中的删除指定记录(用于整型主键)
	 * @param id ID
	 * @return 是否成功
	 */
	public boolean remove(final int id) {
		return this.dao.deleteByKey(id) > 0;
	}
	
	/**
	 * 根据制定记录的ID，从数据库中的删除指定记录(用于整型主键)
	 * @param id ID
	 * @return 是否成功
	 */
	public boolean remove(final String id) {
		return this.dao.delete(id) > 0;
	}
	
	/**
	 * 删除多条数据,条件为符合多个指定列的值,使用 IN 匹配
	 * @param ids
	 * @return 是否成功
	 */
	public boolean removeByIds(final String ids) {
		return this.dao.deleteInByKey(ids) > 0;
	}
	
	/**
	 * 清空表中的所有数据，且不记录数据库日志(调用truncate语句)
	 */
	public void truncate() {
		this.dao.truncate();
	}
	
	/**
	 * 更新
	 * @param entity 实体
	 * @param columnNames 更新的字段名
	 * @return 是否成功
	 */
	public boolean edit(final TEntity entity, final String... columnNames) {
		return this.dao.update(entity,columnNames) > 0;
	}
	
	/**
	 * 更新
	 * @param entity 实体
	 * @param id ID
	 * @param columnNames 更新的字段名
	 * @return 是否成功
	 */
	public boolean edit(final TEntity entity, final int id, final String... columnNames) {
		return this.dao.update(entity, id, columnNames) > 0;
	}
	
	/**
	 * 更新一条数据,条件为符合指定列的值,使用 IN 匹配提示: IN的条件仅用于主键列
	 * @param entity 实体
	 * @param ids ID数组
	 * @param columnNames 更新的字段名
	 * @return 是否成功
	 */
	public boolean editByIds(final TEntity entity, final String ids, final String... columnNames) {
		return this.dao.updateInByKey(entity, ids, columnNames) > 0;
	}
	
	/**
	 * 根据ID获取实体
	 * @param id 业务ID
	 * @param columnNames 显示的字段
	 * @return 实体
	 */
	public TEntity getEntityById(final String id, final String... columnNames) {
		return this.dao.queryByKey(id, columnNames);
	}
	
	/**
	 * 根据ID获取实体
	 * @param id 数据库ID
	 * @param columnNames 显示的字段
	 * @return 实体
	 */
	public TEntity getEntityById(final int id, final String... columnNames) {
		return this.dao.queryByKey(id, columnNames);
	}
	
	/**
	 * 从数据库中获取所有的实体对象集合(返回值不需判断是否为null)
	 * @param columnNames 显示的字段名
	 * @return 集合
	 */
	public List<TEntity> getAll(final String... columnNames) {
		return this.dao.query(columnNames);
	}
	
	/**
	 * 分页查询(返回值不需要判断是否为NULL)
	 * @param pagination 分页
	 * @param columnNames 实现的字段名
	 * @return 集合
	 */
	public List<TEntity> getEntitiesByPage(final Pagination pagination, final String... columnNames) {
		return this.dao.query("", pagination, null, columnNames);
	}
	
	/**
	 * 获取数据库中表的记录总数
	 * @return 总数 
	 */
	public int getCount() {
		return this.dao.count();
	}
}
