package com.paipianwang.SmartReport.engine;

import java.util.List;

import com.paipianwang.SmartReport.engine.data.HorizontalStatColumnDataSet;
import com.paipianwang.SmartReport.engine.data.LayoutType;
import com.paipianwang.SmartReport.engine.data.ReportDataSet;
import com.paipianwang.SmartReport.engine.data.ReportDataSource;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataRow;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataSet;
import com.paipianwang.SmartReport.engine.data.ReportParameter;
import com.paipianwang.SmartReport.engine.data.VerticalStatColumnDataSet;
import com.paipianwang.SmartReport.engine.query.QueryFactory;
import com.paipianwang.SmartReport.engine.query.Queryer;

/**
 * 数据执行器类，负责选择正确的报表查询器并获取数据，最终转化为成报表的数据集
 */
public class DataExecutor {
    private final ReportParameter parameter;
    private final ReportDataSource dataSource;
    private final Queryer queryer;

    /**
     * 数据执行器
     *
     * @param parameter 报表参数对象
     */
    public DataExecutor(ReportParameter parameter) {
        this.parameter = parameter;
        this.dataSource = null;
        this.queryer = null;
    }

    /**
     * 数据执行器
     *
     * @param dataSource 报表数据源配置对象
     * @param parameter  报表参数对象
     */
    public DataExecutor(ReportDataSource dataSource, ReportParameter parameter) {
        this.dataSource = dataSource;
        this.parameter = parameter;
        this.queryer = null;
    }

    /**
     * 数据执行器
     *
     * @param queryer   报表查询器对象
     * @param parameter 报表参数对象
     */
    public DataExecutor(Queryer queryer, ReportParameter parameter) {
        this.dataSource = null;
        this.parameter = parameter;
        this.queryer = queryer;
    }

    /**
     * 选择正确的报表查询器并获取数据，最终转化为成报表的数据集
     *
     * @return ReportDataSet报表数据集对象
     */
    public ReportDataSet execute() {
        Queryer queryer = this.getQueryer();
        if (queryer == null) {
            throw new RuntimeException("未指定报表查询器对象!");
        }
        // 获取元数据列集合
        List<ReportMetaDataColumn> metaDataColumns = queryer.getMetaDataColumns();
        // 获取元数据行集合
        List<ReportMetaDataRow> metaDataRows = queryer.getMetaDataRows();
        ReportMetaDataSet metaDataSet = new ReportMetaDataSet(metaDataRows, metaDataColumns, this.parameter.getEnabledStatColumns());
        // 确定布局
        return this.parameter.getStatColumnLayout() == LayoutType.VERTICAL ?
                new VerticalStatColumnDataSet(metaDataSet, this.parameter.getLayout(), this.parameter.getStatColumnLayout()) :
                new HorizontalStatColumnDataSet(metaDataSet, this.parameter.getLayout(), this.parameter.getStatColumnLayout());
    }

    /**
     * 选择正确的报表查询器并获取数据，最终转化为成报表的数据集
     *
     * @param metaDataSet
     * @return ReportDataSet报表数据集对象
     */
    public ReportDataSet execute(ReportMetaDataSet metaDataSet) {
        if (metaDataSet == null) {
            throw new RuntimeException("报表元数据集不能为null!");
        }
        return this.parameter.getStatColumnLayout() == LayoutType.VERTICAL ?
                new VerticalStatColumnDataSet(metaDataSet, this.parameter.getLayout(), this.parameter.getStatColumnLayout()) :
                new HorizontalStatColumnDataSet(metaDataSet, this.parameter.getLayout(), this.parameter.getStatColumnLayout());
    }

    private Queryer getQueryer() {
        if (this.queryer != null) {
            return this.queryer;
        }
        return QueryFactory.create(this.dataSource, this.parameter);
    }
}
