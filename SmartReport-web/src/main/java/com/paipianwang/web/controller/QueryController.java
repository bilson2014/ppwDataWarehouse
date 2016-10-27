package com.paipianwang.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.paipianwang.SmartReport.domain.view.EasyUIQueryFormView;
import com.paipianwang.SmartReport.engine.exceptions.NotFoundLayoutColumnException;
import com.paipianwang.SmartReport.engine.exceptions.QueryParamsException;
import com.paipianwang.SmartReport.engine.exceptions.SQLQueryException;
import com.paipianwang.SmartReport.engine.exceptions.TemplatePraseException;
import com.paipianwang.web.utils.ReportingUtils;

@RestController
@RequestMapping("/report/query")
public class QueryController extends AbstractController{

	@RequestMapping(value = {"","/","/index"})
	public ModelAndView queryView(final HttpServletRequest request) {
		
		return new ModelAndView("/report/query");
	}
	
	@RequestMapping(value = "/view")
	public ModelAndView queryFontView(final HttpServletRequest request) {
		
		return new ModelAndView("/table/query");
	}
	
	// 加载模板
	@RequestMapping(value = {"/uid/{uid}"})
	public ModelAndView query(@PathVariable("uid") final String uid,final HttpServletRequest request) {
		
		final ModelAndView mv = new ModelAndView("/report/template");
		ReportingUtils.previewByTemplate(uid, mv, new EasyUIQueryFormView(), request);
		return mv;
	}
	
	// 加载模板
	@RequestMapping(value = {"/view/uid/{uid}"})
	public ModelAndView queryView(@PathVariable("uid") final String uid,final HttpServletRequest request) {
		
		final ModelAndView mv = new ModelAndView("/table/template");
		ReportingUtils.previewByTemplate(uid, mv, new EasyUIQueryFormView(), request);
		return mv;
	}
	
	// 生成二维表
	@RequestMapping(value = "/generate")
	public JSONObject generate(final String uid, final HttpServletRequest request) {
		final JSONObject json = new JSONObject();
		
		try {
			ReportingUtils.generate(uid ,json, request);
		} catch (QueryParamsException | NotFoundLayoutColumnException | SQLQueryException | TemplatePraseException ex) {
			json.put("htmlTable", ex.getMessage());
            this.logException("报表生成失败", ex);
		} catch (Exception ex) {
			json.put("htmlTable", "报表生成失败！请联系管理员.");
            this.logException("报表生成失败", ex);
		}
		
		return json;
	}
	
	@RequestMapping("/exportToExcel")
	public void exportToExcel(final String uid, final String name, final String htmlText, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
            ReportingUtils.exportToExcel(uid, name, htmlText, request, response);
        } catch (Exception ex) {
            this.logException("导出Excel失败", ex);
        }
	}
}
