package com.paipianwang.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.paipianwang.SmartReport.common.viewmodel.JsonResult;
import com.paipianwang.SmartReport.common.viewmodel.ParamJsonResult;
import com.paipianwang.SmartReport.common.viewmodel.TreeNode;
import com.paipianwang.SmartReport.domain.po.QueryParameterPo;
import com.paipianwang.SmartReport.domain.po.ReportingPo;
import com.paipianwang.SmartReport.engine.data.ColumnType;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.utils.VelocityUtils;
import com.paipianwang.SmartReport.service.ReportingGenerationService;
import com.paipianwang.SmartReport.service.ReportingService;

@RestController
@RequestMapping("report/designer")
public class DesignerController extends AbstractController {

	@Resource
	private ReportingService reportingService; // 模板订制服务
	
	@Resource
	private ReportingGenerationService generationService; // 报表生成服务
	
	@RequestMapping(value = {"","/","/index"})
	public ModelAndView design() {
		return new ModelAndView("report/designer");
	}
	
	@RequestMapping(value = "/listChildNodes")
	public List<TreeNode<ReportingPo>> listNodes(Integer id, final HttpServletRequest request) {
		if(id == null) {
			id = 0;
		}
		
		final List<TreeNode<ReportingPo>> treeNodes = new ArrayList<TreeNode<ReportingPo>>();
		List<ReportingPo> list = reportingService.getEntityByPid(id);
		for (final ReportingPo reportingPo : list) {
			treeNodes.add(this.createTreeNode(reportingPo));
		}
		
		return treeNodes;
	}
	
	/**
	 * 获取元数据列
	 * @param dsId 数据源ID
	 * @param sqlText 原始SQL语句
	 * @param dataRange 天数
	 * @param jsonQueryParam 参数json字符串
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadSqlColumns")
	public ParamJsonResult<List<ReportMetaDataColumn>> loadSqlMetaData(final Integer dsId,String sqlText,
			final Integer dataRange, final String jsonQueryParams, final HttpServletRequest request) {
		final ParamJsonResult<List<ReportMetaDataColumn>> result = new ParamJsonResult<List<ReportMetaDataColumn>>(false, "");
		
		if(dsId == null) {
			return result;
		}
		
		try {
			// SQL参数拼装
			sqlText = getSqlText(sqlText, jsonQueryParams, dataRange, request);
			// 根据SQL语句读取数据库元数据
			result.setData(reportingService.getReportMetaDataColumns(dsId, sqlText));
			result.setSuccess(true);
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		return result;
	}
	
	/**
	 * 添加一行元数据
	 * @return
	 */
	@RequestMapping("/addSqlColumn")
	public ReportMetaDataColumn addSqlColumn() {
		final ReportMetaDataColumn column = new ReportMetaDataColumn();
		column.setName("expr");
		column.setType(ColumnType.STATISTICAL.getValue());
		column.setDataType("DECIMAL");
		column.setWidth(42);
		return column;
	}

	// 查询参数与SQL语句整合
	private String getSqlText(String sqlText, String jsonQueryParam, Integer dataRange, HttpServletRequest request) {
		Map<String, Object> formParameters = generationService.getBuildInParameters(request.getParameterMap(), dataRange);
		
		if(StringUtils.isNotBlank(jsonQueryParam)) {
			List<QueryParameterPo> queryParams = JSON.parseArray(jsonQueryParam, QueryParameterPo.class);
			for (QueryParameterPo param : queryParams) {
				if(formParameters.containsKey(param.getName()))
					continue;
				formParameters.put(param.getName(), param.getRealDefaultValue());
			}
		}
		return VelocityUtils.parse(sqlText, formParameters);
	}

	/**
	 * 新建模板配置
	 */
	@RequestMapping(value = "/add")
	public ParamJsonResult<List<TreeNode<ReportingPo>>> add(ReportingPo po, final HttpServletRequest request) {
		final ParamJsonResult<List<TreeNode<ReportingPo>>> result = new ParamJsonResult<List<TreeNode<ReportingPo>>>(false, "");
		
		try {
			po.setCreateUser("");
			po.setComment("");
			po.setFlag(1);
			po.setId(this.reportingService.addReport(po));
			po = reportingService.getEntityById(po.getId());
			List<TreeNode<ReportingPo>> nodes = new ArrayList<TreeNode<ReportingPo>>();
			TreeNode<ReportingPo> treeNode = this.createTreeNode(po);
			nodes.add(treeNode);
			result.setData(nodes);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/edit")
	public ParamJsonResult<TreeNode<ReportingPo>> edit(final ReportingPo po, final HttpServletRequest request) {
		final ParamJsonResult<TreeNode<ReportingPo>> result = new ParamJsonResult<TreeNode<ReportingPo>>(false, "");
		
		try {
			reportingService.editReport(po);
			TreeNode<ReportingPo> node = this.createTreeNode(po);
			result.setData(node);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/remove")
	public JsonResult remove(final Integer id, final Integer pid, final HttpServletRequest request) {
		final JsonResult result = new JsonResult(false, "");
		
		try {
			if(!reportingService.hasChild(id)) {
				reportingService.remove(id, pid);
				this.setSuccessResult(result, "");
			}else {
				this.setFailureResult(result, "操作失败！当前节点还有子节点，请先删除子节点！");
			}
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
	/**
	 * 创建树节点
	 */
	private TreeNode<ReportingPo> createTreeNode(final ReportingPo po) {
		final String id = Integer.toString(po.getId());
		String text = po.getName();
		String state = po.getHasChild() ? "closed" : "open";
		return new TreeNode<ReportingPo>(id, text, state, po);
	}
	
	/**
	 * 更新查询参数
	 * @param id 报表ID
	 * @param jsonQueryParams 报表查询参数json字符串
	 */
	@RequestMapping(value = "/setQueryParam")
	public ParamJsonResult<TreeNode<ReportingPo>> setQueryParam(final Integer id, final String jsonQueryParams,
			final HttpServletRequest request) {
		final ParamJsonResult<TreeNode<ReportingPo>> result = new ParamJsonResult<TreeNode<ReportingPo>>(false, "");
		
		if(id == null) {
			this.setFailureResult(result, "请选择报表节点!");
		}
		
		try {
			// 更新报表查询参数
			reportingService.setQueryParam(id, jsonQueryParams);
			final ReportingPo po = reportingService.getEntityById(id);
			TreeNode<ReportingPo> treeNode = this.createTreeNode(po);
			result.setData(treeNode);
			this.setSuccessResult(result, "");
		} catch (Exception ex) {
			this.setExceptionResult(result, ex);
		}
		
		return result;
	}
	
}
