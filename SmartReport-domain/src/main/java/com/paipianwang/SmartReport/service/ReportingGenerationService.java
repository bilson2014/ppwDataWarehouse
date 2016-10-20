package com.paipianwang.SmartReport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.paipianwang.SmartReport.common.utils.DateUtils;
import com.paipianwang.SmartReport.dao.ReportingDao;
import com.paipianwang.SmartReport.data.jdbc.BaseService;
import com.paipianwang.SmartReport.domain.po.DataSourcePo;
import com.paipianwang.SmartReport.domain.po.QueryParameterPo;
import com.paipianwang.SmartReport.domain.po.ReportingPo;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlCheckBox;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlCheckBoxList;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlComboBox;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlDateBox;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlFormElement;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlSelectOption;
import com.paipianwang.SmartReport.domain.viewmodel.HtmlTextBox;
import com.paipianwang.SmartReport.engine.ReportGenerator;
import com.paipianwang.SmartReport.engine.data.ColumnType;
import com.paipianwang.SmartReport.engine.data.ReportDataSet;
import com.paipianwang.SmartReport.engine.data.ReportDataSource;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportParameter;
import com.paipianwang.SmartReport.engine.data.ReportQueryParamItem;
import com.paipianwang.SmartReport.engine.data.ReportSqlTemplate;
import com.paipianwang.SmartReport.engine.data.ReportTable;
import com.paipianwang.SmartReport.engine.utils.VelocityUtils;

@Service
public class ReportingGenerationService extends BaseService<ReportingDao, ReportingPo> {

	@Resource
	private DataSourceService dsService;
	
	@Resource
	private ReportingService reportingService;
	
	@Autowired
	public ReportingGenerationService(ReportingDao dao) {
		super(dao);
	}

	public Map<String, Object> getBuildInParameters(Map<?, ?> parameterMap) {
		return getBuildInParameters(parameterMap, 7);
	}
	
	public Map<String, Object> getBuildInParameters(Map<?, ?> parameterMap, Integer dataRange) {
		HashMap<String, Object> formParams = new HashMap<String, Object>();
		this.setBuildInParams(formParams, parameterMap, dataRange);
		return formParams;
	}

	/**
	 * 替换报表开始时间与结束时间
	 * @param formParams
	 * @param parameterMap
	 * @param dataRange
	 */
	private void setBuildInParams(HashMap<String, Object> formParams, Map<?, ?> parameterMap, Integer dataRange) {
		dataRange = dataRange - 1 < 0 ? 0 : dataRange -1 ;
		if (parameterMap.containsKey("startTime")) {
            String[] values = (String[]) parameterMap.get("startTime");
            formParams.put("startTime", values[0]);
        } else {
            formParams.put("startTime", DateUtils.add(-dataRange, "yyyy-MM-dd"));
        }
        if (parameterMap.containsKey("endTime")) {
            String[] values = (String[]) parameterMap.get("endTime");
            formParams.put("endTime", values[0]);
        } else {
            formParams.put("endTime", DateUtils.getNow("yyyy-MM-dd"));
        }
        // 判断是否设置报表统计列
        if (parameterMap.containsKey("statColumns")) {
            String[] values = (String[]) parameterMap.get("statColumns");
            formParams.put("statColumns", StringUtils.join(values, ','));
        } else {
            formParams.put("statColumns", "");
        }
        // 判断是否设置报表表格行跨行显示
        if (parameterMap.containsKey("isRowSpan")) {
            String[] values = (String[]) parameterMap.get("isRowSpan");
            formParams.put("isRowSpan", values[0]);
        } else {
            formParams.put("isRowSpan", "true");
        }
		
		String startTime = formParams.get("startTime").toString();
        String endTime = formParams.get("endTime").toString();
        formParams.put("intStartTime", Integer.valueOf(DateUtils.getDate(startTime, "yyyyMMdd")));
        formParams.put("utcStartTime", DateUtils.getUtcDate(startTime, "yyyy-MM-dd"));
        formParams.put("utcIntStartTime", Integer.valueOf(DateUtils.getUtcDate(startTime, "yyyyMMdd")));
        formParams.put("intEndTime", Integer.valueOf(DateUtils.getDate(endTime, "yyyyMMdd")));
        formParams.put("utcEndTime", DateUtils.getUtcDate(endTime, "yyyy-MM-dd"));
        formParams.put("utcIntEndTime", Integer.valueOf(DateUtils.getUtcDate(endTime, "yyyyMMdd")));
	}

	public Map<String, HtmlFormElement> getFormElementMap(ReportingPo report, Map<String, Object> buildInParams,
			int minDisplayedStatColumn) {
		final List<HtmlFormElement> formElements = this.getFormElements(report, buildInParams, minDisplayedStatColumn);
		Map<String, HtmlFormElement> formElementMap = new HashMap<>(formElements.size());
        for (HtmlFormElement element : formElements) {
            formElementMap.put(element.getName(), element);
        }
        return formElementMap;
	}

	public List<HtmlFormElement> getFormElements(ReportingPo report, Map<String, Object> buildInParams,
			int minDisplayedStatColumn) {
		final List<HtmlFormElement> formElements = new ArrayList<HtmlFormElement>();
		formElements.addAll(this.getDateFormElements(report, buildInParams));
        formElements.addAll(this.getQueryParamFormElements(report, buildInParams));
        formElements.add(this.getStatColumnFormElements(report.getMetaColumnList(), minDisplayedStatColumn));
		return formElements;
	}

	public HtmlCheckBoxList getStatColumnFormElements(List<ReportMetaDataColumn> columns, int minDisplayedStatColumn) {
        List<ReportMetaDataColumn> statColumns = new ArrayList<ReportMetaDataColumn>();
        for (ReportMetaDataColumn column : columns) {
            if (column.getType() == ColumnType.STATISTICAL ||
                    column.getType() == ColumnType.COMPUTED) {
                statColumns.add(column);
            }
        }

        if (statColumns.size() <= minDisplayedStatColumn) {
            return null;
        }

        List<HtmlCheckBox> checkBoxes = new ArrayList<HtmlCheckBox>(statColumns.size());
        for (ReportMetaDataColumn column : statColumns) {
            HtmlCheckBox checkbox = new HtmlCheckBox(column.getName(), column.getText(), column.getName());
            checkbox.setChecked(!column.isOptional());
            checkBoxes.add(checkbox);
        }
        return new HtmlCheckBoxList("statColumns", "统计列", checkBoxes);
    }

	public List<HtmlFormElement> getQueryParamFormElements(ReportingPo report,
			Map<String, Object> buildInParams) {
		final DataSourcePo ds = this.dsService.getEntityById(report.getDsId());
        final List<QueryParameterPo> queryParams = report.getQueryParamList();
        final List<HtmlFormElement> formElements = new ArrayList<HtmlFormElement>(3);

        for (QueryParameterPo queryParam : queryParams) {
            HtmlFormElement htmlFormElement = null;
            queryParam.setDefaultText(VelocityUtils.parse(queryParam.getDefaultText(), buildInParams));
            queryParam.setDefaultValue(VelocityUtils.parse(queryParam.getDefaultValue(), buildInParams));
            queryParam.setContent(VelocityUtils.parse(queryParam.getContent(), buildInParams));
            String formElement = queryParam.getFormElement().toLowerCase();
            if (formElement.equals("select") || formElement.equalsIgnoreCase("selectMul")) {
                htmlFormElement = this.getComboBoxFormElements(queryParam, ds, buildInParams);
            } else if (formElement.equals("checkbox")) {
                htmlFormElement = new HtmlCheckBox(queryParam.getName(), queryParam.getText(), queryParam.getRealDefaultValue());
            } else if (formElement.equals("text")) {
                htmlFormElement = new HtmlTextBox(queryParam.getName(), queryParam.getText(), queryParam.getRealDefaultValue());
            } else if (formElement.equals("date")) {
                htmlFormElement = new HtmlDateBox(queryParam.getName(), queryParam.getText(), queryParam.getRealDefaultValue());
            }
            if (htmlFormElement != null) {
                this.setElementCommonProperities(queryParam, htmlFormElement);
                formElements.add(htmlFormElement);
            }
        }
        return formElements;
	}

	public void setElementCommonProperities(QueryParameterPo queryParam, HtmlFormElement htmlFormElement) {
		htmlFormElement.setDataType(queryParam.getDataType());
        htmlFormElement.setHeight(queryParam.getHeight());
        htmlFormElement.setWidth(queryParam.getWidth());
        htmlFormElement.setRequired(queryParam.isRequired());
        htmlFormElement.setDefaultText(queryParam.getRealDefaultText());
        htmlFormElement.setDefaultValue(queryParam.getRealDefaultValue());
        htmlFormElement.setComment(queryParam.getComment());
	}

	public HtmlFormElement getComboBoxFormElements(QueryParameterPo queryParam, DataSourcePo ds,
			Map<String, Object> buildInParams) {
		List<ReportQueryParamItem> options = this.getOptions(queryParam, ds, buildInParams);
        List<HtmlSelectOption> htmlSelectOptions = new ArrayList<HtmlSelectOption>(options.size());

        if (queryParam.hasDefaultValue()) {
            htmlSelectOptions.add(new HtmlSelectOption(queryParam.getDefaultText(), queryParam.getDefaultValue(), true));
        }
        for (int i = 0; i < options.size(); i++) {
            ReportQueryParamItem option = options.get(i);
            if (!option.getName().equals(queryParam.getDefaultValue())) {
                htmlSelectOptions.add(new HtmlSelectOption(option.getText(), option.getName(), (!queryParam.hasDefaultValue() && i == 0)));
            }
        }

        HtmlComboBox htmlComboBox = new HtmlComboBox(queryParam.getName(), queryParam.getText(), htmlSelectOptions);
        htmlComboBox.setMultipled(queryParam.getFormElement().equals("selectMul"));
        htmlComboBox.setAutoComplete(queryParam.isAutoComplete());
        return htmlComboBox;
	}

	public List<ReportQueryParamItem> getOptions(QueryParameterPo queryParam, DataSourcePo ds,
			Map<String, Object> buildInParams) {
		if (queryParam.getDataSource().equals("sql")) {
            return this.reportingService.getDao().executeQueryParamSqlText(ds.getJdbcUrl(), ds.getUser(), ds.getPassword(), queryParam.getContent());
        }

        List<ReportQueryParamItem> options = new ArrayList<ReportQueryParamItem>();
        if (queryParam.getDataSource().equals("text") && StringUtils.isNoneBlank(queryParam.getContent())) {
            HashSet<String> set = new HashSet<String>();
            String[] optionSplits = StringUtils.split(queryParam.getContent(), '|');
            for (String option : optionSplits) {
                String[] namevalueSplits = StringUtils.split(option, ',');
                String name = namevalueSplits[0];
                String text = namevalueSplits.length > 1 ? namevalueSplits[1] : name;
                if (!set.contains(name)) {
                    set.add(name);
                    options.add(new ReportQueryParamItem(name, text));
                }
            }
        }
        return options;
	}

	public List<HtmlDateBox> getDateFormElements(ReportingPo report,
			Map<String, Object> buildInParams) {
		StringBuilder text = new StringBuilder(report.getSqlText());
        text.append(" ");
        text.append(report.getQueryParams());

        String regex = "\\$\\{.*?\\}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text.toString());
        Set<String> set = new HashSet<String>(2);
        while (matcher.find()) {
            String group = matcher.group(0);
            String name = group.replaceAll("utc|int|Int|[\\$\\{\\}]", "");
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            if (!set.contains(name) && StringUtils.indexOfIgnoreCase(group, name) != -1) {
                set.add(name);
            }
        }

        List<HtmlDateBox> dateboxes = new ArrayList<>(2);
        String name = "startTime";
        if (set.contains(name)) {
            dateboxes.add(new HtmlDateBox(name, "开始日期", buildInParams.get(name).toString()));
        }
        name = "endTime";
        if (set.contains(name)) {
            dateboxes.add(new HtmlDateBox(name, "结束日期", buildInParams.get(name).toString()));
        }
        return dateboxes;
	}

	public List<HtmlFormElement> getDateAndQueryParamFormElements(ReportingPo report,
			Map<String, Object> buildInParams) {
		List<HtmlFormElement> formElements = new ArrayList<>(15);
        formElements.addAll(this.getDateFormElements(report, buildInParams));
        formElements.addAll(this.getQueryParamFormElements(report, buildInParams));
        return formElements;
	}

	public List<HtmlFormElement> getNonStatColumnFormElements(List<ReportMetaDataColumn> columns) {
        List<HtmlFormElement> formElements = new ArrayList<HtmlFormElement>(10);
        for (ReportMetaDataColumn column : columns) {
            if (column.getType() == ColumnType.LAYOUT || column.getType() == ColumnType.DIMENSION) {
                HtmlComboBox htmlComboBox =
                        new HtmlComboBox("dim_" + column.getName(), column.getText(), new ArrayList<HtmlSelectOption>(0));
                htmlComboBox.setAutoComplete(true);
                formElements.add(htmlComboBox);
            }
        }
        return formElements;
    }

	public Map<String, Object> getFormParameters(final Map<?, ?> parameterMap, final int dataRange) {
		HashMap<String, Object> formParams = new HashMap<String, Object>();
		// 替换报表开始时间与结束时间
		this.setBuildInParams(formParams, parameterMap, 7);
		// 设置报表的查询参数
		this.setQueryParams(formParams, parameterMap);
		return formParams;
	}

	public void setQueryParams(final HashMap<String, Object> formParams, final Map<?, ?> parameterMap) {
		String[] values = (String[]) parameterMap.get("uid");
        if (values == null || values.length == 0 || "".equals(values[0].trim())) {
            return;
        }

        String uid = values[0].trim();
        ReportingPo report = this.reportingService.getEntityByUid(uid);
        List<QueryParameterPo> queryParams = report.getQueryParamList();
        for (QueryParameterPo queryParam : queryParams) {
            String value = "";
            values = (String[]) parameterMap.get(queryParam.getName());
            if (values != null && values.length > 0) {
                value = this.getQueryParamValue(queryParam.getDataType(), values);
            }
            formParams.put(queryParam.getName(), value);
        }
	}

	private String getQueryParamValue(String dataType, String[] values) {
		if (values.length == 1) {
            return values[0];
        }
        if (dataType.equals("float") || dataType.equals("integer")) {
            return StringUtils.join(values, ",");
        }
        return StringUtils.join(values, "','");
	}

	public ReportTable getReportTable(final ReportingPo report, final Map<String, Object> formParams) {
		// 查询数据源
		final DataSourcePo ds = dsService.getEntityById(report.getDsId());
		final ReportDataSource reportDs = new ReportDataSource(ds.getJdbcUrl(), ds.getUser(), ds.getPassword());
		return ReportGenerator.generate(reportDs, this.createReportParameter(report, formParams));
	}

	private ReportParameter createReportParameter(final ReportingPo report, final Map<String, Object> formParams) {
		// 拼接SQL
		final String sqlText = new ReportSqlTemplate(report.getSqlText(), formParams).execute();
		// 获取统计列
		Set<String> enabledStatColumn = this.getEnabledStatColumns(formParams);
		List<ReportMetaDataColumn> metaColumns = JSON.parseArray(report.getMetaColumns(), ReportMetaDataColumn.class);
		
		return new ReportParameter(report.getId().toString(), report.getName(),
                report.getLayout(), report.getStatColumnLayout(), metaColumns,
                enabledStatColumn, Boolean.valueOf(formParams.get("isRowSpan").toString()), sqlText);
	}

	/**
	 * 获取要显示的列
	 * @param formParams
	 * @return
	 */
	private Set<String> getEnabledStatColumns(final Map<String, Object> formParams) {
		final Set<String> checkedSet = new HashSet<String>();
		final String checkedColumnNames = formParams.get("statColumns").toString();
		
		if(StringUtils.isBlank(checkedColumnNames)) {
			return checkedSet;
		}
		final String[] columnNames = StringUtils.split(checkedColumnNames, ",");
		for (final String columnName : columnNames) {
			if(!checkedSet.contains(columnName)) {
				checkedSet.add(columnName);
			}
		}
		return checkedSet;
	}

	public ReportDataSet getReportDataSet(final ReportingPo report, final Map<String, Object> formParameters) {
		DataSourcePo ds = this.dsService.getEntityById(report.getDsId());
        ReportDataSource reportDs = new ReportDataSource(ds.getJdbcUrl(), ds.getUser(), ds.getPassword());
        return ReportGenerator.getDataSet(reportDs, this.createReportParameter(report, formParameters));
	}

	
}
