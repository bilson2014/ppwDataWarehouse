package com.paipianwang.web.utils;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.paipianwang.SmartReport.common.utils.DateUtils;
import com.paipianwang.SmartReport.domain.po.ReportingPo;
import com.paipianwang.SmartReport.domain.view.EasyUIQueryFormView;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlFormElement;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportTable;
import com.paipianwang.SmartReport.service.ReportingGenerationService;
import com.paipianwang.SmartReport.service.ReportingService;

@Service
public class ReportingUtils {

	private static ReportingService reportingService;
    private static ReportingGenerationService generationService;
    
    @Autowired
    public ReportingUtils(ReportingService reportingService, ReportingGenerationService generationService) {
        ReportingUtils.reportingService = reportingService;
        ReportingUtils.generationService = generationService;
    }

	public static void previewByTemplate(String uid, ModelAndView modelAndView, EasyUIQueryFormView formView,
			HttpServletRequest request) {
		final ReportingPo report = reportingService.getEntityByUid(uid);
		final List<ReportMetaDataColumn> metaDataColumns = report.getMetaColumnList();
		final Map<String, Object> buildInParams = generationService.getBuildInParameters(request.getParameterMap(), report.getDataRange());
		List<HtmlFormElement> dateAndQueryElements = generationService.getDateAndQueryParamFormElements(report, buildInParams);
        HtmlFormElement statColumnFormElements = generationService.getStatColumnFormElements(metaDataColumns, 0);
        List<HtmlFormElement> nonStatColumnFormElements = generationService.getNonStatColumnFormElements(metaDataColumns);
        modelAndView.addObject("uid", uid);
        modelAndView.addObject("id", report.getId());
        modelAndView.addObject("name", report.getName());
        modelAndView.addObject("comment", report.getComment().trim());
        modelAndView.addObject("formHtmlText", formView.getFormHtmlText(dateAndQueryElements));
        modelAndView.addObject("statColumHtmlText", formView.getFormHtmlText(statColumnFormElements));
        modelAndView.addObject("nonStatColumHtmlText", formView.getFormHtmlText(nonStatColumnFormElements));
	}

	// 生成二维表
	public static void generate(final String uid, final JSONObject json, final HttpServletRequest request) {
		generate(uid, json, request.getParameterMap());
		
	}

	public static void generate(final String uid, final JSONObject json, final Map<String, String[]> parameterMap) {
		generate(uid, json, new HashMap<String, Object>(0), parameterMap);
	}

	private static void generate(final String uid, final JSONObject json, final HashMap<String, Object> attachParams,
			final Map<?, ?> parameterMap) {
		if(StringUtils.isBlank(uid)) {
			json.put("htmlTable", "模板参数为Null,数据不能加载!");
			return ;
		}
		
		final ReportTable reportTable = generate(uid, attachParams, parameterMap);
		json.put("htmlTable", reportTable.getHtmlText());
		json.put("metaDataRowCount", reportTable.getMetaDataRowCount());
		json.put("metaDataColumnCount", reportTable.getMetaDataColumnCount());
	}

	public static ReportTable generate(final String uid, final HashMap<String, Object> attachParams, final Map<?, ?> parameterMap) {
		// 获取 报表配置信息
		final ReportingPo report = reportingService.getEntityByUid(uid);
		// 报表查询参数信息
		Map<String, Object> formParams = generationService.getFormParameters(parameterMap, report.getDataRange());
		if (attachParams != null && attachParams.size() > 0) {
            for (Entry<String, Object> es : attachParams.entrySet()) {
                formParams.put(es.getKey(), es.getValue());
            }
        }
		return generationService.getReportTable(report, formParams);
	}

	public static void exportToExcel(final String uid, final String name, String htmlText, final HttpServletRequest request,
			final HttpServletResponse response) {
		htmlText = htmlText.replaceFirst("<table>", "<tableFirst>");
        htmlText = htmlText.replaceAll("<table>", "<table cellpadding=\"3\" cellspacing=\"0\"  border=\"1\"  rull=\"all\" style=\"border-collapse: collapse\">");
        htmlText = htmlText.replaceFirst("<tableFirst>", "<table>");
        try (OutputStream out = response.getOutputStream()) {
            String fileName = name + "_" + DateUtils.getNow("yyyyMMddHHmmss");
            fileName = new String(fileName.getBytes(), "ISO8859-1") + ".xls";
            if ("large".equals(htmlText)) {
                ReportingPo report = reportingService.getEntityByUid(uid);
                Map<String, Object> formParameters = generationService.getFormParameters(request.getParameterMap(), report.getDataRange());
                ReportTable reportTable = generationService.getReportTable(report, formParameters);
                htmlText = reportTable.getHtmlText();
            }
            response.reset();
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s", fileName));
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
            response.addCookie(new Cookie("fileDownload", "true"));
            //out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); // 生成带bom的utf8文件
            out.write(htmlText.getBytes());
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
		
	}
    
}
