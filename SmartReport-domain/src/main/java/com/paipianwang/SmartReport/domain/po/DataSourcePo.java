package com.paipianwang.SmartReport.domain.po;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.paipianwang.SmartReport.common.json.CustomDateTimeSerializer;
import com.paipianwang.SmartReport.data.annotations.Column;

public class DataSourcePo implements Serializable{

	private static final long serialVersionUID = 6712591308841997054L;
	
	/**
	 * 列名ID,数据源ID
	 */
	public final static String Id = "id";
	
	/**
	 * 列名uid,数据源唯一ID
	 */
	public final static String Uid = "uid";
	
	/**
	 * 列名name,数据源名称
	 */
	public final static String Name = "name";
	
	/**
	 * 列名jdbc_url,数据源连接字符串
	 */
	public final static String JdbcUrl = "jdbc_url";
	
	/**
	 * 列名user,数据源登录用户名
	 */
	public final static String User = "user";
	
	/**
	 * 列名password,数据源登录密码
	 */
	public final static String Password = "password";
	
	/**
	 * 列名comment,说明备注
	 */
	public final static String Comment = "comment";
	
	/**
	 * 列名create_user,创建者
	 */
	public final static String CreateUser = "create_user";
	
	/**
	 * 列名create_time,记录创建时间
	 */
	public final static String CreateTime = "create_time";
	
	/**
	 * 列名update_time,记录修改时间
	 */
	public final static String UpdateTime = "update_time";
	
	/**
	 * 数据库表名 datasource
	 */
	public static String EntityName = "datasource";
	@Column(name = "id", isIgnored = true, isPrimaryKey = true)
	private Integer id;
	
	@Column(name = "uid")
	private String uid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "jdbc_url")
	private String jdbcUrl;
	
	@Column(name = "user")
	private String user;
	
	@Column(name = "password")
	private String password;
	
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

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@JsonSerialize(using = CustomDateTimeSerializer.class )
	public Date getUpdateTime() {
		return updateTime == null ? Calendar.getInstance().getTime() : updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
