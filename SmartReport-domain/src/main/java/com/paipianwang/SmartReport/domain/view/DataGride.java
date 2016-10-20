package com.paipianwang.SmartReport.domain.view;

import java.util.List;

import com.paipianwang.SmartReport.data.BaseObject;

/**
 * DataGride 封装类
 * @author Administrator
 *
 */
public class DataGride<T> extends BaseObject {

	private static final long serialVersionUID = 3748473706441967785L;

	private long total = 0l; // 总数
	
	private List<T> rows = null;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
