package com.paipianwang.SmartReport.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.SmartReport.common.utils.StringUtil;
import com.paipianwang.SmartReport.dao.ReportingDao;
import com.paipianwang.SmartReport.data.jdbc.BaseService;
import com.paipianwang.SmartReport.domain.po.DataSourcePo;
import com.paipianwang.SmartReport.domain.po.ReportingPo;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;

/**
 * 报表信息服务类
 * @author Jack
 *
 */
@Service
public class ReportingService extends BaseService<ReportingDao, ReportingPo> {

	@Resource
	private DataSourceService dsService; // 注册数据源服务
	
	@Autowired
	public ReportingService(ReportingDao dao) {
		super(dao);
	}

	/**
	 * 根据SQL语句获取数据库元数据
	 * @param dsId 数据源ID
	 * @param sqlText SQL语句
	 * @return
	 */
	public List<ReportMetaDataColumn> getReportMetaDataColumns(final Integer dsId, String sqlText) {
		final DataSourcePo ds = dsService.getEntityById(dsId);
		return getReportMetaDataColumns(ds, sqlText);
	}
	
	public List<ReportMetaDataColumn> getReportMetaDataColumns(final DataSourcePo ds, final String sqlText) {
		// 获取元数据集
		return this.dao.executeSqlText(ds.getJdbcUrl(), ds.getUser(), ds.getPassword(), sqlText);
	}

	public Integer addReport(final ReportingPo po) {
		po.setUid(StringUtil.getUUID());
		final int newId = this.dao.insertWithId(po);
		this.setHasChild(po.getPid());
		this.dao.updatePath(newId, this.getPath(po.getPid(), newId));
		return newId;
	}

	public String getPath(int pid, int id) {
        if (pid <= 0) {
            return "" + id;
        }
        return this.dao.queryPath(pid) + "," + id;
    }

	public void setHasChild(int pid) {
	        if (pid > 0) {
	            this.dao.updateHasChild(pid, true);
	        }
	    }

	public boolean editReport(final ReportingPo po) {
		final String[] columnNames = new String[] {
				ReportingPo.DsId, ReportingPo.Name, ReportingPo.Status,
				ReportingPo.Sequence, ReportingPo.DataRange, ReportingPo.Layout,
				ReportingPo.StatColumnLayout, ReportingPo.SqlText, ReportingPo.MetaColumns
		};
		
		return this.edit(po, po.getId(), columnNames);
	}

	public List<ReportingPo> getEntityByPid(final int pid) {
		return dao.queryByPid(pid);
	}

	public boolean setQueryParam(final Integer id, final String jsonQueryParams) {
		return this.dao.updateQueryParams(id, jsonQueryParams) > 0;
	}

	/**
	 * 查询子节点数量
	 * @param id
	 * @return
	 */
	public boolean hasChild(final Integer id) {
		return this.dao.countChildBy(id) > 0;
	}

	
	public boolean remove(final Integer id, final Integer pid) {
		this.dao.deleteByKey(id);
		
		if(this.hasChild(pid)) {
			return true;
		}
		
		return this.dao.updateHasChild(pid, false) > 0;
		
	}

	public ReportingPo getEntityByUid(final String uid) {
		return this.dao.queryByUid(uid);
	}

}
