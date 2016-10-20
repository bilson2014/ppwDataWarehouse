package com.paipianwang.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.SmartReport.common.utils.StringUtil;
import com.paipianwang.SmartReport.common.viewmodel.JsonResult;
import com.paipianwang.SmartReport.data.Pagination;
import com.paipianwang.SmartReport.domain.po.DataSourcePo;
import com.paipianwang.SmartReport.domain.view.DataGride;
import com.paipianwang.SmartReport.service.DataSourceService;

/**
 * 数据源控制器
 * @author Jack
 *
 */
@RestController
@RequestMapping(value = "report/ds")
public class DataSourcesController extends AbstractController {

	@Resource
	private DataSourceService dataSourceService;
	
	@RequestMapping(value = {"","/","/index"})
	public ModelAndView dsView() {
		return new ModelAndView("report/dataSource");
	}
	
	/**
	 * 查询数据源列表
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/query")
	public DataGride<DataSourcePo> query(Integer page, Integer rows, final HttpServletRequest request) {
		if (page == null)
			page = 1;
		if (rows == null)
			rows = 30;
		final Pagination pagination = new Pagination((page - 1) * rows , rows);
		final String[] columnNames = new String[] {
				DataSourcePo.Id, DataSourcePo.Uid, DataSourcePo.Name, 
				DataSourcePo.JdbcUrl, DataSourcePo.CreateUser, DataSourcePo.CreateTime
		};
		final List<DataSourcePo> list = this.dataSourceService.getEntitiesByPage(pagination, columnNames);
		final long total = dataSourceService.getCount();
		
		DataGride<DataSourcePo> dataGride = new DataGride<DataSourcePo>();
		dataGride.setTotal(total);
		dataGride.setRows(list);
		return dataGride;
	}
	
	/**
	 * 新增数据源
	 * @param dataSource
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	public JsonResult add(final DataSourcePo dataSource,final HttpServletRequest request) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			dataSource.setUid(StringUtil.getUUID());
			dataSourceService.add(dataSource);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 修改数据源
	 * @param dataSource
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public JsonResult edit(final DataSourcePo dataSource, final HttpServletRequest request) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			String[] columnNames = new String[] { DataSourcePo.Name, DataSourcePo.User, DataSourcePo.Password,
					DataSourcePo.JdbcUrl, DataSourcePo.UpdateTime };
			// 设置更新时间
			dataSource.setUpdateTime(dataSource.getUpdateTime());
			this.dataSourceService.edit(dataSource, columnNames);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 测试数据源连接
	 * @param url jdbcurl
	 * @param user username
	 * @param password password
	 * @return
	 */
	@RequestMapping(value = "/testConnection")
	public JsonResult testConnection(final String url, final String user, final String pass) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			result.setSuccess(this.dataSourceService.getDao().testConnection(url, user, pass));
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 根据ID测试数据源连接
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/testConnectionById")
	public JsonResult testConnection(final Integer id) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			final DataSourcePo dataSource = this.dataSourceService.getEntityById(id);
			result.setSuccess(this.dataSourceService.getDao().testConnection(dataSource.getJdbcUrl(),
					dataSource.getUser(), dataSource.getPassword()));
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 删除一条数据源信息
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove")
	public JsonResult remove(final int id, final HttpServletRequest request) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			this.dataSourceService.remove(id);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 批量删除数据源信息
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/batchRemove")
	public JsonResult remove(final String ids, final HttpServletRequest request) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			this.dataSourceService.removeByIds(ids);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 获取数据源信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public List<DataSourcePo> list(final HttpServletRequest request) {
		final String[] columnNames = new String[] {
				DataSourcePo.Id,
				DataSourcePo.Uid,
				DataSourcePo.Name
		};
		final List<DataSourcePo> list = this.dataSourceService.getAll(columnNames);
		return list;
	}
}
