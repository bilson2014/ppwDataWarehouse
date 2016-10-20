package com.paipianwang.SmartReport.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.paipianwang.SmartReport.data.SortType;
import com.paipianwang.SmartReport.data.criterion.Restrictions;
import com.paipianwang.SmartReport.data.jdbc.BaseDao;
import com.paipianwang.SmartReport.domain.po.ReportingPo;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportQueryParamItem;
import com.paipianwang.SmartReport.engine.query.QueryFactory;

@Repository
public class ReportingDao extends BaseDao<ReportingPo> {

	public ReportingDao() {
		super(ReportingPo.EntityName, ReportingPo.Id);
	}

	public List<ReportMetaDataColumn> executeSqlText(final String jdbcUrl, final String user, final String password, final String sqlText) {
		
		return QueryFactory.create(jdbcUrl, user, password).parseMetaDataColumns(sqlText);
	}

	public String queryPath(final int pid) {
		ReportingPo po = this.queryByKey(pid, ReportingPo.Path);
		return po == null ? "" : po.getPath();
	}

	public int updatePath(final int id, final String path) {
		final String condition = Restrictions.equal(ReportingPo.Pid, "?").toString();
		String[] columnNames = new String[]{ReportingPo.Path};
		Object[] args = new Object[]{path, id};
		return this.update(condition, args, columnNames);
	}

	public int updateHasChild(int pid, boolean hasChild) {
		String condition = Restrictions.equal(ReportingPo.Id, "?").toString();
        String[] columnNames = new String[]{ReportingPo.HasChild};
        Object[] args = new Object[]{hasChild, pid};
        return this.update(condition, args, columnNames);
		
	}

	public List<ReportingPo> queryByPid(final int pid) {
		final String condition = Restrictions.equal(ReportingPo.Pid, "?")
				.append(Restrictions.orderBy(SortType.ASC, ReportingPo.Sequence))
				.toString();
		final Object[] args = new Object[]{pid};
		return this.query(condition, args);
	}

	/**
	 * 更新报表查询参数
	 * @param id 报表ID
	 * @param jsonQueryParams 查询参数json字符串
	 */
	public int updateQueryParams(final Integer id, final String jsonQueryParams) {
		final String condition = Restrictions.equal(ReportingPo.Id, "?").toString();
		final String[] columnNames = new String[] {
			ReportingPo.QueryParams	
		};
		final Object[] args = new Object[] {
				jsonQueryParams, id
		};
		return this.update(condition, args, columnNames);
	}

	/**
	 * 查询该节点是否存在子节点
	 * @param id 该节点ID
	 * @return
	 */
	public int countChildBy(final Integer id) {
		final String condition = Restrictions.equal(ReportingPo.Pid, "?").toString();
		final Object[] args = new Object[] {
				id
		};
		return this.count(condition, args);
	}

	/**
	 * 根据UID获取模板
	 * @param uid
	 * @return
	 */
	public ReportingPo queryByUid(final String uid) {
		final String condition = Restrictions.equal(ReportingPo.Uid, "?").toString();
		final Object[] args = new Object[] {
				uid
		};
		return this.queryOne(condition, args);
	}

	public List<ReportQueryParamItem> executeQueryParamSqlText(String jdbcUrl, String user, String password,
			String sqlText) {
		return QueryFactory.create(jdbcUrl, user, password).parseQueryParamItems(sqlText);
	}
	

}
