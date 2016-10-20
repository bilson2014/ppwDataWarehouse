package com.paipianwang.SmartReport.data;

public class Pagination extends BaseObject{

	private static final long serialVersionUID = 250812177236200312L;
	
	private long totals = 0l;
	private int startIndex = 0;
	private int pageSize = 0;
	private String sortItem;
	private String sortType;
	
	public Pagination() {
		
	}
	
	public Pagination(int startIndex, int pageSize) {
		this(startIndex,pageSize,"","asc");
	}

	public Pagination(int startIndex, int pageSize, String sortItem) {
		this(startIndex, pageSize, sortItem, "asc");
	}

	public Pagination(int startIndex, int pageSize, String sortItem, String sortType) {
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.sortItem = sortItem;
		this.sortType = sortType;
	}

	public long getTotals() {
		return totals;
	}

	public void setTotals(long totals) {
		this.totals = totals;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortItem() {
		return sortItem;
	}

	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
}
