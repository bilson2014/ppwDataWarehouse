package com.paipianwang.SmartReport.engine.data;

import java.io.Serializable;

/**
 * 报表数据源类
 * 
 * @author Jack
 *
 */
public class ReportDataSource implements Serializable {

	private static final long serialVersionUID = 2594490176381613090L;
	private String jdbcUrl; // 数据源连接字符串
	private String user; // 用户名
	private String password; // 密码

	public ReportDataSource() {

	}

	public ReportDataSource(final String jdbcUrl, final String user, final String password) {
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
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

}
