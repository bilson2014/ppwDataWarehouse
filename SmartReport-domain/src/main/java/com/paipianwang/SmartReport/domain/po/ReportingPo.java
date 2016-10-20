package com.paipianwang.SmartReport.domain.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.alibaba.fastjson.JSON;
import com.paipianwang.SmartReport.common.json.CustomDateTimeSerializer;
import com.paipianwang.SmartReport.data.annotations.Column;
import com.paipianwang.SmartReport.engine.data.ReportMetaDataColumn;

/**
 * 报表信息持久化类
 * 
 * @author Jack
 *
 */
public class ReportingPo implements Serializable {

	private static final long serialVersionUID = 4763990478179803520L;

	/**
	 * 列名ID,报表ID
	 */
	public final static String Id = "id";

	/**
	 * 列名pid,父节点ID
	 */
	public final static String Pid = "pid";
	/**
	 * 列名ds_id,数据源ID
	 */
	public final static String DsId = "ds_id";
	/**
	 * 列名uid,报表唯一ID,由接口调用方引用
	 */
	public final static String Uid = "uid";
	/**
	 * 列名name,报表名称
	 */
	public final static String Name = "name";
	/**
	 * 列名path,报表树型结构路径
	 */
	public final static String Path = "path";
	/**
	 * 列名flag,报表树型节点标志(0:分类节点,1:报表节点)
	 */
	public final static String Flag = "flag";
	/**
	 * 列名has_child
	 */
	public final static String HasChild = "has_child";
	/**
	 * 列名status,报表状态(0:编辑,1:锁定)
	 */
	public final static String Status = "status";
	/**
	 * 列名sequence,报表节点在其父节点中的顺序
	 */
	public final static String Sequence = "sequence";
	/**
	 * 列名data_range, 报表默认展示多少天数据
	 */
	public final static String DataRange = "data_range";
	/**
	 * 列名layout, 布局形式(1:横向,2:纵向)
	 */
	public final static String Layout = "layout";
	/**
	 * 列名stat_column_layout, 统计列布局形式(1:横向,2:纵向)
	 */
	public final static String StatColumnLayout = "stat_column_layout";
	/**
	 * 列名sql_text, 报表SQL语句
	 */
	public final static String SqlText = "sql_text";
	/**
	 * 列名meta_columns, 报表元数据列集合的JSON序列化字符串
	 */
	public final static String MetaColumns = "meta_columns";
	/**
	 * 列名query_params, 查询条件列属性集合json字符串
	 */
	public final static String QueryParams = "query_params";
	/**
	 * 列名comment, 说明备注
	 */
	public final static String Comment = "comment";
	/**
	 * 列名create_user, 创建用户
	 */
	public static final String CreateUser = "create_user";
	/**
	 * 列名create_time, 记录创建时间
	 */
	public static final String CreateTime = "create_time";
	/**
	 * 列名update_time, 记录修改时间
	 */
	public static final String UpdateTime = "update_time";

	/**
	 * 实体Reporting属性
	 */
	public static String EntityName = "reporting";
	@Column(name = "id", isIgnored = true, isPrimaryKey = true)
	private Integer id;
	@Column(name = "pid")
	private Integer pid = 0;
	@Column(name = "ds_id")
	private Integer dsId = 0;
	@Column(name = "uid")
	private String uid;
	@Column(name = "name")
	private String name;
	@Column(name = "path")
	private String path = "";
	@Column(name = "flag")
	private Integer flag = 0;
	@Column(name = "has_child")
	private boolean hasChild;
	@Column(name = "status")
	private Integer status = 0;
	@Column(name = "sequence")
	private Integer sequence = 0;
	@Column(name = "data_range")
	private Integer dataRange = 7;
	@Column(name = "layout")
	private Integer layout = 1;
	@Column(name = "stat_column_layout")
	private Integer statColumnLayout = 1;
	@Column(name = "sql_text")
	private String sqlText = "";
	@Column(name = "meta_columns")
	private String metaColumns = "";
	@Column(name = "query_params")
	private String queryParams = "";
	@Column(name = "comment")
	private String comment = "";
	@Column(name = "create_user")
	private String createUser = "";
	@Column(name = "create_time")
	private Date createTime = Calendar.getInstance().getTime();
	@Column(name = "update_time")
	private Date updateTime = Calendar.getInstance().getTime();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getDsId() {
		return dsId;
	}

	public void setDsId(Integer dsId) {
		this.dsId = dsId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public boolean getHasChild() {
        return this.hasChild;
    }

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getDataRange() {
		return dataRange;
	}

	public void setDataRange(Integer dataRange) {
		this.dataRange = dataRange;
	}

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public Integer getStatColumnLayout() {
		return statColumnLayout;
	}

	public void setStatColumnLayout(Integer statColumnLayout) {
		this.statColumnLayout = statColumnLayout;
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public String getMetaColumns() {
		return metaColumns;
	}

	public void setMetaColumns(String metaColumns) {
		this.metaColumns = metaColumns;
	}

	public String getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime == null ? Calendar.getInstance().getTime() : createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getUpdateTime() {
		return updateTime == null ? Calendar.getInstance().getTime() : updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 解析json格式的报表元数据列为ReportMetaDataColumn对象集合
	 */
	@JsonIgnore
	public List<ReportMetaDataColumn> getMetaColumnList() {
		if (StringUtils.isBlank(this.metaColumns)) {
			return new ArrayList<ReportMetaDataColumn>(0);
		}
		return JSON.parseArray(metaColumns, ReportMetaDataColumn.class);
	}

	/**
	 * 解析json格式的报表查询参数为QueryParameterPo对象集合
	 */
	@JsonIgnore
	public List<QueryParameterPo> getQueryParamList() {
		if(StringUtils.isBlank(this.queryParams)) {
			return new ArrayList<QueryParameterPo>();
		}
		return JSON.parseArray(queryParams, QueryParameterPo.class);
	}
}
