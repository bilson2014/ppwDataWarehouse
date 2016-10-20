package com.paipianwang.SmartReport.dao;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.paipianwang.SmartReport.data.Pagination;
import com.paipianwang.SmartReport.data.criterion.Restrictions;
import com.paipianwang.SmartReport.data.jdbc.BaseDao;
import com.paipianwang.SmartReport.domain.po.DataSourcePo;

@Repository
public class DataSourceDao extends BaseDao<DataSourcePo> {

	public DataSourceDao() {
		super(DataSourcePo.EntityName, DataSourcePo.Id);
	}
	
	/**
	 * 测试数据源是否能够连接
	 * @param url jdbcurl
	 * @param user username
	 * @param password password
	 * @return 是否连接成功
	 */
	public boolean testConnection(final String url, final String user, final String password) {
		try {
			DriverManager.getConnection(url, user, password);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * 分页查询
	 * @param createUser
	 * @param pagination
	 * @return
	 */
	public List<DataSourcePo> queryByPage(final String createUser, final Pagination pagination) {
		final String condition = Restrictions.equal(DataSourcePo.CreateUser, "?").toString();
		Object[] args = new Object[]{ createUser };
		return this.query(condition, pagination, args);
	}
}
