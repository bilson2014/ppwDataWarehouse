package com.paipianwang.SmartReport.common.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

	private String id;
	private String pid;
	private String text;
	private String state;
	private String iconCls;
	private boolean checked;
	private T attributes;
	private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();

	public TreeNode(String id, String pid, String text) {
		super();
		this.id = id;
		this.pid = pid;
		this.text = text;
	}

	public TreeNode(String id, String text, String state, T attributes) {
		super();
		this.id = id;
		this.text = text;
		this.state = state;
		this.attributes = attributes;
	}

	public TreeNode(String id, String pid, String text, String state, T attributes) {
		super();
		this.id = id;
		this.pid = pid;
		this.text = text;
		this.state = state;
		this.attributes = attributes;
	}

	public TreeNode(String id, String pid, String text, String state, String iconCls, boolean checked, T attributes) {
		super();
		this.id = id;
		this.pid = pid;
		this.text = text;
		this.state = state;
		this.iconCls = iconCls;
		this.checked = checked;
		this.attributes = attributes;
	}

	public String getId() {
		return id;
	}

	public String getPid() {
		return pid;
	}

	public String getText() {
		return text;
	}

	public String getState() {
		return state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public boolean isChecked() {
		return checked;
	}

	public T getAttributes() {
		return attributes;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

}
