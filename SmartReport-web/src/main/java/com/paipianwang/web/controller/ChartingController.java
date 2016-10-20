package com.paipianwang.web.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.paipianwang.SmartReport.domain.po.ReportingPo;
import com.paipianwang.SmartReport.domain.view.EasyUIQueryFormView;
import com.paipianwang.SmartReport.engine.data.ReportDataSet;
import com.paipianwang.SmartReport.engine.exceptions.NotFoundLayoutColumnException;
import com.paipianwang.SmartReport.engine.exceptions.QueryParamsException;
import com.paipianwang.SmartReport.engine.exceptions.SQLQueryException;
import com.paipianwang.SmartReport.engine.exceptions.TemplatePraseException;
import com.paipianwang.SmartReport.service.ReportingChartService;
import com.paipianwang.SmartReport.service.ReportingGenerationService;
import com.paipianwang.SmartReport.service.ReportingService;
import com.paipianwang.web.utils.ReportingUtils;

@RestController
@RequestMapping(value = "/report/chart")
public class ChartingController extends AbstractController {

	@Resource
    private ReportingService reportingService;
    @Resource
    private ReportingGenerationService generationService;
    @Resource
    private ReportingChartService reportChartService;
    
	@RequestMapping(value = {"/{uid}"})
    public ModelAndView preview(@PathVariable final String uid, final HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("report/chart");

        try {
            ReportingUtils.previewByTemplate(uid, modelAndView, new EasyUIQueryFormView(), request);
        } catch (QueryParamsException | TemplatePraseException ex) {
            modelAndView.addObject("formHtmlText", ex.getMessage());
            this.logException("查询参数生成失败", ex);
        } catch (Exception ex) {
            modelAndView.addObject("formHtmlText", "查询参数生成失败！请联系管理员.");
            this.logException("查询参数生成失败", ex);
        }
        return modelAndView;
    }
	
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	public JSONObject getData(final String uid, final HttpServletRequest request) {
		final JSONObject jsonObject = new JSONObject();
        jsonObject.put("dimColumnMap", null);
        jsonObject.put("dimColumns", null);
        jsonObject.put("statColumns", null);
        jsonObject.put("dataRows", null);
        jsonObject.put("msg", "");
        
        if (uid == null) {
            return jsonObject;
        }
        
        try {
            ReportingPo po = reportingService.getEntityByUid(uid);
            Map<String, Object> formParameters = generationService.getFormParameters(request.getParameterMap(), po.getDataRange());
            ReportDataSet reportData = generationService.getReportDataSet(po, formParameters);
            jsonObject.put("dimColumnMap", reportChartService.getDimColumnMap(reportData));
            jsonObject.put("dimColumns", reportChartService.getDimColumns(reportData));
            jsonObject.put("statColumns", reportChartService.getStatColumns(reportData));
            jsonObject.put("dataRows", reportChartService.getDataRows(reportData));
        } catch (QueryParamsException | NotFoundLayoutColumnException | SQLQueryException | TemplatePraseException ex) {
            jsonObject.put("msg", ex.getMessage());
            this.logException("报表生成失败", ex);
        } catch (Exception ex) {
            jsonObject.put("msg", "报表生成失败！请联系管理员。");
            this.logException("报表生成失败", ex);
        }
		
		// TODO 数据封装
		return jsonObject;
	}
}
