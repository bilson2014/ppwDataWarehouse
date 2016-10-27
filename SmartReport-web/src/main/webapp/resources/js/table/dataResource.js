//var dsPageRootUrl = getContextPath() + '/report/ds/';

$(function(){
	$('#datasourceGrid').datagrid({
		method : 'get',
		url : dsPageRootUrl + 'query',
		idField : 'id',
		loadMsg : '数据正在加载,请耐心的等待...' ,
		fit : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
  		columns : [ [ {
			field : 'id',
			title : '标识',
			width : 50
		}, {
			field : 'name',
			title : '数据源名称',
			width : 100
		}, {
			field : 'uid',
			title : '数据源唯一ID',
			width : 100
		}, {
			field : 'jdbcUrl',
			title : '数据源连接字符串',
			width : 200
		}, {
			field : 'createTime',
			title : '创建日期',
			width : 100
		}, {
			field : 'options',
			title : '操作',
			width : 300,
			formatter : function(value, row, index) {
				return DataSource.optionsFormatter(value, row, index);
			}
		} ] ],
		pageSize : 30,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		pagination : true,
		showFooter : false,
		rownumbers : true,
		singleSelect : true,
		toolbar : [ {
			text : '增加',
			iconCls : 'icon-add',
			handler : DataSource.add
		}, '-',{
			text : '修改',
			iconCls : 'icon-edit',
			handler : DataSource.edit
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : DataSource.remove
		} ],
		onDblClickRow : function(index, row) {
			DataSource.edit();
		}
	});
	
	// 初始化Form表单
	$('#datasourceDlg').dialog({
		closed : true,
		modal : true,
		buttons : [ {
			text : '测试连接',
			iconCls : 'icon-search',
			handler : DataSource.testConnection
		}, {
			text : '关闭',
			iconCls : 'icon-no',
			handler : function() {
				$('#datasourceDlg').dialog('close');
			}
		}, {
			text : '保存',
			iconCls : 'icon-save',
			handler : DataSource.save
		} ]
	});
});

var DataSource = function() {
	
};

// add
DataSource.add = function() {
	ReportCommon.add('#datasourceDlg', '#datasourceForm', '#datasourceAction', '#datasourceId', '新增数据源配置');
};

// edit
DataSource.edit = function() {
	ReportCommon.edit('#datasourceDlg', '#datasourceForm', '#datasourceAction', '#datasourceGrid', '#datasourceId', '修改数据源配置');
};

// remove
DataSource.remove = function() {
	ReportCommon.removeWithActUrl('#datasourceGrid', dsPageRootUrl + 'query', dsPageRootUrl + 'remove');
};

// save
DataSource.save = function() {
	ReportCommon.saveWithActUrl('#datasourceDlg', '#datasourceForm', '#datasourceAction', '#datasourceGrid', dsPageRootUrl + '/query', dsPageRootUrl + '');
};

// Fucntion testConnection icon
DataSource.optionsFormatter = function(value, row, index) {
	var path = getContextPath() + '/resources/images/icons/connect.png';
	return '<a href="javascript:void(0);" title="测试连接" onclick="javascript:DataSource.applyConnection('+ index +')"><img src="'+ path +'" alt="测试连接" /></a>';
};

// test connection
DataSource.applyConnection = function(index) {
	$('#datasourceGrid').datagrid('selectRow', index);
	var row = $('#datasourceGrid').datagrid('getSelected');
	
	$.post(dsPageRootUrl + 'testConnectionById', {
		id : row.id
	}, function callback(data) {
		if (data.success) {
			$.messager.alert('成功', "测试成功", 'success');
		} else {
			$.messager.alert('失败', "测试失败", 'error');
		}
	}, 'json');
};

DataSource.testConnection = function() {
	$.post(dsPageRootUrl + 'testConnection', {
		url : $("#configJdbcUrl").val().trim(),
		pass : $("#configPassword").val().trim(),
		user : $("#configUser").val().trim()
	}, function callback(data) {
		if (data.success) {
			$.messager.alert('成功', "测试成功", 'success');
		} else {
			$.messager.alert('失败', "测试失败", 'error');
		}
	}, 'json');
	
}