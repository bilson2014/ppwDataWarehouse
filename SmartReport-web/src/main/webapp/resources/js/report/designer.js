var sqlEditor;
var tree;
$().ready(function(){
	var editing = undefined;
	tree = $('#reportTree');
	
	// 初始化树
	$('#reportTree').tree({
		checkbox : false,
		method : 'get',
		animate : true,
		dnd : true,
		url : designerPageRootUrl + 'listChildNodes',
		onLoadSuccess : function(node, data) {
			if (ReportDesigner.treeNodePathIds && ReportDesigner.treeNodePathIds.length > 0) {
				var id = ReportDesigner.treeNodePathIds.shift();
				ReportDesigner.selectAndExpandTreeNode(id);
			}
		},
		onClick : ReportDesigner.clickTreeNodeHandler,
		onContextMenu : function(e, node) {
			e.preventDefault();
			$('#reportTree').tree('select', node.target);
			var copyNodeId = $('#copyNodeId').val();
			$('#reportTreeCtxMenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});
	
	// 初始化sqlContext
	sqlEditor = ace.edit("reportSqlText");
	sqlEditor.getSession().setMode("ace/mode/sql");
	sqlEditor.setTheme("ace/theme/twilight");
    sqlEditor.setOptions({
        enableBasicAutocompletion: !0,
        enableSnippets: !0,
        enableLiveAutocompletion: !0
    });
	// 设置变动标识位
	sqlEditor.getSession().on('change', function(obj) {
		if (obj.origin == "setValue") {
			$('#reportIsChange').val(0);
		} else {
			$('#reportIsChange').val(1);
		}
	});
	
	// 初始化状态
	ReportDesigner.setSqlEditorStatus();
	
	// 操作项
	sqlColumnOptions = [ {
		name : 'optional',
		text : '可选',
		type : 1
	}, {
		name : 'percent',
		text : '百分比',
		type : 1
	}, {
		name : 'displayInMail',
		text : '邮件显示',
		type : 1
	}, {
		name : 'expression',
		text : '表达式',
		type : 4
	}, {
		name : 'comment',
		text : '备注',
		type : 2
	}, {
		name : 'format',
		text : '格式',
		type : 2
	} ];
	
	// 布局类型选项
	sqlColumnTypeOptions = [ {
		text : '布局列',
		value : 1
	}, {
		text : '维度列',
		value : 2
	}, {
		text : '统计列',
		value : 3
	}, {
		text : '计算列',
		value : 4
	} ];
	
	// 字段排序选项
	var sqlColumnSortTypeOptions = [ {
		text : '默认',
		value : 0
	}, {
		text : '数字优先升序',
		value : 1
	}, {
		text : '数字优先降序',
		value : 2
	}, {
		text : '字符优先升序',
		value : 3
	}, {
		text : '字符优先降序',
		value : 4
	} ];
	
	// 模板初始化
	ReportDesigner.init();
	
	// 加载元数据列表
	$('#sqlColumnGrid').datagrid({
		method : 'post',
		fit : true,
		fitColumns : true,
		singleSelect : true,
		rownumbers : true,
		tools : [ {
			iconCls : 'icon-up',
			handler : function() {
				var grid = $('#sqlColumnGrid');
				var row = grid.datagrid('getSelected');
				var index = grid.datagrid('getRowIndex', row);
				// 升序降序排列
				ReportDesigner.resortDatagrid(index, 'up', grid);
			}
		}, '-', {
			iconCls : 'icon-down',
			handler : function() {
				var grid = $('#sqlColumnGrid');
				var row = grid.datagrid('getSelected');
				var index = grid.datagrid('getRowIndex', row);
				// 升序降序排列
				ReportDesigner.resortDatagrid(index, 'down', grid);
			}
		}, '-', {
			iconCls : 'icon-add',
			handler : function() {
				var index = 1;
				var rows = $('#sqlColumnGrid').datagrid('getRows');
				if(rows && rows.length > 0) {
					for(var i = 0;i < rows.length; i++) {
						var type = $('#type' + i).val();
						if(type == 4) {
							index ++;
						}
					}
				}
				
				$.post(designerPageRootUrl + 'addSqlColumn', function(row){
					row.name = row.name + index;
					row.text = row.name;
					row.type = 4;
					row.sortType = 0;
					$('#sqlColumnGrid').datagrid('appendRow', row);
				}, 'json');
			}
		}, '-', {
			iconCls : 'icon-cancel',
			handler : function() {
				var row = $('#sqlColumnGrid').datagrid('getSelected');
				
				if(row != undefined) {
					var index = $('#sqlColumnGrid').datagrid('getRowIndex', row);
					$('#sqlColumnGrid').datagrid('deleteRow', index);
					$('#sqlColumnGrid').datagrid('loadData', $('#sqlColumnGrid').datagrid('getRows'));
				}
			}
		} ],
		columns : [ [
						{
							field : 'name',
							title : '列名',
							width : 100,
							formatter : function(value, row, index) {
								var id = "name" + index;
								var tmpl = '<input style="width:98%;" type="text" id="{{id}}" name="name" value="{{value}}" />';
								return template.compile(tmpl)({
									id : id,
									value : row.name
								});
							}
						},
						{
							field : 'text',
							title : '标题',
							width : 100,
							formatter : function(value, row, index) {
								var id = "text" + index;
								var tmpl = '<input style="width:98%;" type="text" id="{{id}}" name="text" value="{{value}}" />';
								return template.compile(tmpl)({
									id : id,
									value : row.text
								});
							}
						},
						{
							field : 'type',
							title : '类型',
							width : 75,
							formatter : function(value, row, index) {
								var id = "type" + index;
								var tmpl = '\
										<select id="{{id}}" name=\"type\">\
										{{each list}}\
											<option value="{{$value.value}}" {{if $value.value == currValue}}selected{{/if}}>{{$value.text}}</option>\
										{{/each}}\
										</select>';
								return template.compile(tmpl)({
									id : id,
									currValue : value,
									list : sqlColumnTypeOptions
								});
							}
						},
						{
							field : 'dataType',
							title : '数据类型',
							width : 75
						},
						{
							field : 'width',
							title : '宽度',
							width : 40
						},
						{
							field : 'decimals',
							title : '精度',
							width : 50,
							formatter : function(value, row, index) {
								var id = "decimals" + index;
								if (!row.decimals) {
									row.decimals = 0;
								}
								var tmpl = '<input style="width:42px;" type="text" id="{{id}}" name="decimals" value="{{value}}" />';
								return template.compile(tmpl)({
									id : id,
									value : row.decimals
								});
							}
						},
						{
							field : 'sortType',
							title : '排序类型',
							width : 100,
							formatter : function(value, row, index) {
								var id = "sortType" + index;
								var tmpl = '\
										<select id="{{id}}" name=\"sortType\">\
										{{each list}}\
											<option value="{{$value.value}}" {{if $value.value == currValue}}selected{{/if}}>{{$value.text}}</option>\
										{{/each}}\
										</select>';
								return template.compile(tmpl)({
									id : id,
									currValue : value,
									list : sqlColumnSortTypeOptions 
								});
							}
						} ] ]
	});
	
	$('#queryParamGrid').datagrid({
		method : 'get',
		fit : true,
		singleSelect : true,
		rownumbers : true,
		tools : [ {
			iconCls : 'icon-up',
			handler : function() {
				var grid = $("#queryParamGrid");
				var row = grid.datagrid('getSelected');
				var index = grid.datagrid('getRowIndex', row);
				ReportDesigner.resortDatagrid(index, 'up', grid);
			}
		}, '-', {
			iconCls : 'icon-down',
			handler : function() {
				var grid = $("#queryParamGrid");
				var row = grid.datagrid('getSelected');
				var index = grid.datagrid('getRowIndex', row);
				ReportDesigner.resortDatagrid(index, 'down', grid);
			}
		} ],
		columns : [ [ {
			field : 'name',
			title : '参数名',
			width : 100
		}, {
			field : 'text',
			title : '标题',
			width : 100
		}, {
			field : 'defaultValue',
			title : '默认值',
			width : 100
		}, {
			field : 'defaultText',
			title : '默认标题',
			width : 100
		}, {
			field : 'formElement',
			title : '表单控件',
			width : 100
		}, {
			field : 'dataSource',
			title : '来源类型',
			width : 100,
			formatter : function(value, row, index) {
				if (value == "sql") {
					return "SQL语句";
				}
				if (value == "text") {
					return "文本字符串";
				}
				return "无内容";
			}
		}, {
			field : 'dataType',
			title : '数据类型',
			width : 100
		}, {
			field : 'width',
			title : '数据长度',
			width : 100
		}, {
			field : 'required',
			title : '是否必选',
			width : 80
		}, {
			field : 'autoComplete',
			title : '是否自动提示',
			width : 80
		}, {
			field : 'options',
			title : '操作',
			width : 50,
			formatter : function(value, row, index) {
				var imgPath = getContextPath() + '/resources/images/icons/remove.png';
				var tmpl = '<a href="#" title ="删除" onclick="ReportDesigner.deleteQueryParam(\'{{index}}\')"><img src="{{imgPath}}" alt="删除"/"></a>';
				return template.compile(tmpl)({
					index : index,
					imgPath : imgPath
				});
			}
		} ] ],
		onDblClickRow : function(index, row) {
			$('#queryParamForm').autofill(row);
			$("#queryParamIsRequired").prop("checked",row.required);
			$("#queryParamIsAutoComplete").prop("checked",row.autoComplete);
			$("#queryParamGridIndex").val(index);
		}
	});
	
	$('#tabs').tabs({
		onContextMenu : function(e, title, index) {
			e.preventDefault();
			$('#tabsCtxMenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		},
		onSelect : ReportDesigner.tabSelectedHandlder
	});
	
	$('#setTreeNodeDlg').dialog({
		closed : true,
		modal : true,
		top:(screen.height-500)/2,
		left:(screen.width-360)/2,
		width : 500,
		height : 360,
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-no',
			handler : function() {
				$("#setTreeNodeDlg").dialog('close');
			}
		}, {
			text : '保存',
			iconCls : 'icon-save',
			handler : ReportDesigner.saveTreeNode
		} ]
	});
	
	$('#columnExpressionDlg').dialog({
		closed : true,
		modal : true,
		top:(screen.height-500)/2,
		left:(screen.width-300)/2,
		width : 500,
		height : 300,
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-no',
			handler : function() {
				$("#columnExpressionDlg").dialog('close');
			}
		}, {
			text : '保存',
			iconCls : 'icon-save',
			handler : ReportDesigner.saveSqlColumnGridOptionDialog
		} ]
	});
	
	$('#columnCommentDlg').dialog({
		closed : true,
		modal : true,
		top:(screen.height-600)/2,
		left:(screen.width-400)/2,
		width : 600,
		height : 400,
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-no',
			handler : function() {
				$("#columnCommentDlg").dialog('close');
			}
		}, {
			text : '保存',
			iconCls : 'icon-save',
			handler : ReportDesigner.saveSqlColumnGridOptionDialog
		} ]
	});
	
	$('#columnFormatDlg').dialog({
		closed : true,
		modal : true,
		top:(screen.height-600)/2,
		left:(screen.width-400)/2,
		width : 600,
		height : 400,
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-no',
			handler : function() {
				$("#columnFormatDlg").dialog('close');
			}
		}, {
			text : '保存',
			iconCls : 'icon-save',
			handler : ReportDesigner.saveSqlColumnGridOptionDialog
		} ]
	});
});

var ReportDesigner = function() {
	
};

ReportDesigner.add = function() {
	var node = ReportDesigner.getSelectedTreeNode();
	if (node) {
		$('#reportAction').val('add');
		ReportDesigner.clearAllTab();
		ReportDesigner.selectTab(0);
		$('#reportId').val(0);
		$('#reportPid').val(node.id);
		ReportDesigner.initButtonStatus('add');
		ReportDesigner.clearAllEditor();
	} else {
		$.messager.alert('提示', "请选择一个节点", 'info');
	}
};

ReportDesigner.treeNodePathIds = [];

// 模板初始化
ReportDesigner.init = function() {
	// 冻结报表预览功能
	$('#btnViewReport').linkbutton('disable');
	// 初始化报表状态
	$('#reportStatus').change(function() {
		return ReportDesigner.setSqlEditorStatus();
	});
	// 加载数据源
	ReportDesigner.loadDataSources();
}

// 根据状态控制SQL部分是锁定还是未锁定
ReportDesigner.setSqlEditorStatus = function(){
	var status = $('#reportStatus').val();
	if(status > 0) {
		return sqlEditor.setReadOnly(true);
	}
	return sqlEditor.setOption(false);
}

// 保存修改
ReportDesigner.saveChanged = function(data, handler) {
	var isChanged = $("#reportIsChange").val() == 0 ? false : true;
	if (!isChanged) {
		return handler(data);
	}
	$.messager.confirm('确认', '是否保存修改的数据?', function(r) {
		if (r) {
			ReportDesigner.save();
		}
		handler(data);
	});
};

// 保存
ReportDesigner.save = function() {
	if (!$("#settingsForm").valid()) {
		return;
	}
	
	var sqlColumnRows = $('#sqlColumnGrid').datagrid('getRows');
	if(sqlColumnRows == null && sqlColumnRows.length == 0) {
		return $.messager.alert('失败', "没有任何报表SQL配置列选项！", 'error');
	}
	
	ReportDesigner.setSqlColumnRows(sqlColumnRows);
	var columnTypeMap = ReportDesigner.getColumnTypeMap(sqlColumnRows);
	if(columnTypeMap.layout == 0 || columnTypeMap.stat == 0) {
		return $.messager.alert('失败', "您没有设置布局列或者统计列", 'error');
	}
	
	var emptyExprColumns = ReportDesigner.getEmptyExprColumns(sqlColumnRows);
	if(emptyExprColumns && emptyExprColumns.length > 0) {
		return $.messager.alert('失败', "计算列：[" + emptyExprColumns.join() + "]没有设置表达式！", 'error');
	}
	
	$.messager.progress({
		title : '请稍候',
		text : '正在处理中...'
	});
	
	var act = $('#reportAction').val();
	$.post(designerPageRootUrl + act, {
		id : $('#reportId').val(),
		pid : $('#reportPid').val(),
		dsId : $('#reportDsId').combobox('getValue'),
		dataRange : $('#reportDataRange').val(),
		name : $("#reportName").val(),
		uid : $("#reportUid").val(),
		layout : $("#reportLayout").val(),
		statColumnLayout : $("#reportStatColumnLayout").val(),
		sqlText : sqlEditor.getValue(),
		metaColumns : JSON.stringify(sqlColumnRows),
		status : $("#reportStatus").val(),
		sequence : $("#reportSequence").val(),
		isChange : $('#reportIsChange').val() == 0 ? false : true
	},function(result) {
		$.messager.progress("close");
		if(result.success) {
			$('#reportIsChange').val(0);
		}
		$.messager.alert('操作提示', result.msg, result.success ? 'info' : 'error');
	}, 'json');
}

// 加载数据源列表
ReportDesigner.loadDataSources = function() {
	$('#reportDsId').combobox({
		url : dsPageRootUrl + '/list',
		valueField : 'id',
		textField : 'name',
		editable : true,
		filter: function(info, row){
			if(row.name == null)
				return false;
			return row.name.indexOf(info) >= 0;
		}
	});
}

ReportDesigner.clearAllEditor = function() {
	sqlEditor.setValue('');
};

// 重置
ReportDesigner.resetSettingsForm = function() {
	$("#reportDsId").val('');
	$("#reportLayout").val('1');
	$("#reportDataRange").val('7');
	$("#reportName").val('');
	$("#reportStatColumnLayout").val('1')
	$("#reportStatus").val('0');
	$("#reportSequence").val('100');
	$("#reportId").val('0');
	$("#reportAction").val('add');
	$("#reportUid").val('');
	$("#reportPid").val('0');
	$("#reportIsChange").val('0');
};

ReportDesigner.getSelectedTreeNode = function() {
	return $('#reportTree').tree('getSelected');
};

ReportDesigner.initButtonStatus = function(action) {
	//$('#btnNewReport').linkbutton(action == 'add' ? 'enable' : 'disable');
	$('#btnViewReport').linkbutton(action == 'add' ? 'disable' : 'enable');
};

ReportDesigner.edit = function(data) {
	$('#reportAction').val('edit');
	$('#settingsForm').form('load', data);
	$("#sqlColumnGrid").datagrid('loadData', eval(data.metaColumns));
	$('#reportIsChange').val(0);
	sqlEditor.setValue(data.sqlText);
	ReportDesigner.initButtonStatus('edit');
};

ReportDesigner.loadDataToQueryParamTab = function(data) {
	$("#jsonQueryParams").val(data.queryParams);
	$("#queryParamReportId").val(data.id);
	var jsonQueryParams = $('#jsonQueryParams').val();
	if (jsonQueryParams == null || jsonQueryParams == "") {
		jsonQueryParams = {
			total : 0,
			rows : []
		};
	}
	$("#queryParamGrid").datagrid('loadData', eval(jsonQueryParams));
};

//
// 报表配置相关操作
//

// 计算元数据类型
ReportDesigner.getColumnTypeMap = function(rows) {
	var typeMap = {
		'layout' : 0,
		'dim' : 0,
		'state' : 0,
		'computed' : 0
	}
	
	for(var i = 0;i < rows.length;i ++) {
		if(rows[i].type == 1) {
			typeMap.layout += 1;
		} else if(rows[i].type == 2){
			typeMap.dim += 1;
		} else if(rows[i].type == 3) {
			typeMap.state += 1;
		} else if(rows[i].type == 4) {
			typeMap.computed += 1;
		}
	}
	
	return typeMap;
}

ReportDesigner.getEmptyExprColumns = function(sqlColumnRows) {
	var emptyColumns = [];
	for(var i = 0;i < sqlColumnRows.length;i ++) {
		var row = sqlColumnRows[i];
		if(row.type == 4 && $.trim(row.expression) == '') {
			emptyColumns.push(row.name);
		}
	}
	
	return emptyColumns;
}

// 执行SQL语句
ReportDesigner.executeSql = function() {
	
	$.messager.progress({
		title : '请稍后...',
		text : '数据正在加载中...',
	});

	var queryParamRows = $("#queryParamGrid").datagrid('getRows');
	var jsonQueryParams = queryParamRows ? JSON.stringify(queryParamRows) : "";
	
	$.post(designerPageRootUrl + 'loadSqlColumns', {
		sqlText : sqlEditor.getValue(),
		dsId : $('#reportDsId').combobox('getValue'),
		dataRange : $('#reportDataRange').val(),
		jsonQueryParams : jsonQueryParams
	}, function(result) {
		$.messager.progress("close");
		if (result.success) {
			$("#sqlColumnGrid").datagrid('clearChecked');
			// format columnType and sortType
			var rows = ReportDesigner.eachSqlColumnRows(result.data);
			return ReportDesigner.loadSqlColumns(rows);
		}
		return $.messager.alert('错误', result.msg);
	}, 'json');
};

ReportDesigner.eachSqlColumnRows = function(rows){
	if(rows && rows.length > 0){
		for(var i=0;i<rows.length;i++){
			var row = rows[i];
			row.type = ReportDesigner.getColumnTypeValue(row.type);
			row.sortType = ReportDesigner.getColumnSortTypeValue(row.sortType);
		}
	}
	return rows;
}

ReportDesigner.loadSqlColumns = function(newRows) {
	var sqlColumnRows = $("#sqlColumnGrid").datagrid('getRows');
	if (sqlColumnRows == null || sqlColumnRows.length == 0) {
		return $("#sqlColumnGrid").datagrid('loadData', newRows);
	}
	ReportDesigner.setSqlColumnRows(sqlColumnRows);
	var oldRowMap = {};
	for (var i = 0; i < sqlColumnRows.length; i++) {
		var name = sqlColumnRows[i].name;
		oldRowMap[name] = sqlColumnRows[i];
	}

	for (var i = 0; i < newRows.length; i++) {
		var name = newRows[i].name;
		if (oldRowMap[name]) {
			oldRowMap[name].dataType = newRows[i].dataType;
			oldRowMap[name].width = newRows[i].width;
			newRows[i] = oldRowMap[name];
		}
	}

	$.each(sqlColumnRows, function(i, row) {
		if (row.type == 4) {
			newRows.push(row);
		}
	});
	return $("#sqlColumnGrid").datagrid('loadData', newRows);
};

ReportDesigner.getColumnTypeValue = function(name){
	if (name == "LAYOUT") {
		return 1;
	}
	if (name == "DIMENSION") {
		return 2;
	}
	if (name == "STATISTICAL") {
		return 3;
	}
	if (name == "COMPUTED") {
		return 4;
	}
	return 2;
};

ReportDesigner.getColumnSortTypeValue = function(name){
	if (name =="DEFAULT") {
		return  0;
	}
	if (name == "DIGIT_ASCENDING") {
		return 1;
	}
	if (name == "DIGIT_DESCENDING") {
		return 2;
	}
	if (name == "CHAR_ASCENDING") {
		return 3;
	}
	if (name == "CHAR_DESCENDING") {
		return 4;
	}
	return 0;
};

ReportDesigner.setSqlColumnRows = function(rows) {
	for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
		var row = rows[rowIndex];
		var subOptions = ReportDesigner.getCheckboxOptions(row.type);
		for (var optIndex = 0; optIndex < subOptions.length; optIndex++) {
			var option = subOptions[optIndex];
			var optionId = "#" + option.name + rowIndex;
			row[option.name] = $(optionId).prop("checked");
		}
		row["name"] = $("#name" + rowIndex).val();
		row["text"] = $("#text" + rowIndex).val();
		row["type"] = $("#type" + rowIndex).val();
		row["sortType"] = $("#sortType" + rowIndex).val();
		row["decimals"] = $("#decimals" + rowIndex).val();
	}
	return rows;
};

ReportDesigner.getCheckboxOptions = function(type) {
	var subOptions = [];
	if (type == 4) {
		subOptions = $.grep(sqlColumnOptions, function(option, i) {
			return option.type == 1;
		});
	} else if (type == 3) {
		subOptions = $.grep(sqlColumnOptions, function(option, i) {
			return option.type == 1 || option.type == 2;
		});
	} else {
		subOptions = $.grep(sqlColumnOptions, function(option, i) {
			return option.type == 3;
		});
	}
	return subOptions;
};

// 排序
ReportDesigner.resortDatagrid = function(index, type, grid) {
	var maxIndex = grid.datagrid('getRows').length - 1;
	var moveIndex = ('up' == type) ? index - 1 : index + 1;
	if(moveIndex >= 0 && moveIndex <= maxIndex) {
		var currRow = grid.datagrid('getData').rows[index];
		var moveRow = grid.datagrid('getData').rows[moveIndex];
		grid.datagrid('getData').rows[moveIndex] = currRow;
		grid.datagrid('getData').rows[index] = moveRow;
		grid.datagrid('refreshRow', index);
		grid.datagrid('refreshRow', moveIndex);
		grid.datagrid('selectRow', moveIndex);
	}
};

// 
// param 相关
// 

// 增加/修改参数
ReportDesigner.setQueryParam = function(method) {
	if($('#queryParamForm').valid()) {
		var row = $('#queryParamForm').serializeObject();
		row.required = $('#queryParamIsRequired').prop('checked');
		row.autoComplete = $('#queryParamIsAutoComplete').prop('checked');
		
		// 添加
		if(method == 'add') {
			$('#queryParamGrid').datagrid('appendRow', row);
		} else if(method == 'edit') {
			var index = $('#queryParamGridIndex').val();
			$('#queryParamGrid').datagrid('updateRow', {
				index : index,
				row : row
			});
		}
		
		$('#queryParamForm').form('reset');
	}
}

// 保存参数
ReportDesigner.saveQueryParam = function() {
	var rid = $('#queryParamReportId').val();
	var params = $('#queryParamGrid').datagrid('getRows');
	
	if(params == null && params.length == 0) {
		$('#jsonQueryParams').val('');
	} else {
		$('#jsonQueryParams').val(JSON.stringify(params));
	}
	
	$.messager.progress({
		title : '请稍后...',
		text : '正在处理中...',
	});
	
	// 保存
	$.post(designerPageRootUrl + 'setQueryParam', {
		id : rid,
		jsonQueryParams : $('#jsonQueryParams').val()
	}, function(result) {
		$.messager.progress("close");
		if (result.success) {
			ReportDesigner.updateTreeNode('edit', result.data);
		}
		$.messager.alert('操作提示', result.msg, result.success ? 'info' : 'error');
	}, 'json');
}



//
// tree 相关操作
//

// tree 右键操作
ReportDesigner.treeContextMenu = function(item) {
	if (item.name == "addRp") {
		return ReportDesigner.add();
	}
	if (item.name == "remove") {
		return ReportDesigner.removeTreeNode();
	}
	if (item.name == "refresh") {
		return ReportDesigner.reloadTree();
	}
	return;
};

// 刷新树
ReportDesigner.reloadTree = function() {
	$('#reportTree').tree('reload');
};

// 删除节点
ReportDesigner.removeTreeNode = function() {
	var node = ReportDesigner.getSelectedTreeNode();
	if (node) {
		var data = node.attributes;
		$.messager.confirm('删除', '您确定要删除该节点吗?', function(isConfirm) {
			if (!isConfirm) {
				return;
			}
			$.post(designerPageRootUrl + 'remove', {
				id : data.id,
				pid : data.pid
			}, function(data) {
				if (data.success) {
					$('#reportTree').tree('remove', node.target);
					ReportDesigner.clearAllTab();
				}
				ReportCommon.showMsg(data.msg);
			}, 'json');
		});
	}
};

ReportDesigner.clickTreeNodeHandler = function(currNode) {
	ReportDesigner.saveChanged(currNode, function(node) {
		var index = ReportDesigner.getSelectedTabIndex();
		if (index > 1) {
			ReportDesigner.selectTab(0);
		}
		ReportDesigner.selectTreeNodeHandler(node);
	});
};

ReportDesigner.selectTreeNodeHandler = function(node) {
	$('#reportTree').tree('options').url = designerPageRootUrl + 'listChildNodes';
	$('#reportTree').tree('expand', node.target);

	ReportDesigner.clearAllTab();
	if (node.attributes.flag != 1) {
		$('#reportAction').val('add');
		$('#reportPid').val(node.id);
		ReportDesigner.initButtonStatus('add');
	} else {
		ReportDesigner.loadAllTabData();
	}
};

ReportDesigner.clearAllTab = function() {
	ReportDesigner.clearTab(0);
	ReportDesigner.clearTab(1);
};

ReportDesigner.loadAllTabData = function() {
	ReportDesigner.loadDataToTab(0);
	ReportDesigner.loadDataToTab(1);
};

// 更新树节点
ReportDesigner.updateTreeNode = function(act, nodeData) {
	if (nodeData == null) {
		return;
	}
	if (act == "add") {
		var newNode = nodeData[0];
		var pid = newNode.attributes.pid;
		// 如果是增加根节点
		if (pid == "0") {
			var roots = $('#reportTree').tree('getRoots');
			if (roots && roots.length > 0) {
				$('#reportTree').tree('insert', {
					after : roots[roots.length - 1].target,
					data : newNode
				});
			} else {
				$('#reportTree').tree('reload');
			}
		} else {
			var parentNode = ReportDesigner.getTreeNodeById(pid);
			$('#reportTree').tree('append', {
				parent : parentNode.target,
				data : nodeData
			});
		}
		ReportDesigner.selectTreeNode(newNode.id);
		var currNode = ReportDesigner.getSelectedTreeNode();
		return currNode ? ReportDesigner.selectTreeNodeHandler(currNode) : null;
	}
	if (act == "edit") {
		var node = ReportDesigner.getTreeNodeById(nodeData.id);
		nodeData["target"] = node.target;
		$('#reportTree').tree('update', nodeData);
		// 加载报表节点基础信息
		return ReportDesigner.loadDataToPropertiesTab(nodeData.attributes);
	}
};

// 获取树节点
ReportDesigner.getTreeNodeById = function(id) {
	return $('#reportTree').tree('find', id);
};

// 树节点选中方法
ReportDesigner.selectTreeNode = function(id) {
	var node = $('#reportTree').tree('find', id);
	if (node) {
		$('#reportTree').tree('select', node.target);
	}
};

// 加载树节点基础信息
ReportDesigner.loadDataToPropertiesTab = function(data) {
	for ( var propName in data) {
		var id = "#reportProp_" + propName;
		var value = ReportDesigner.getPropertyValue(propName, data);
		$(id).html(value);
	}
};

ReportDesigner.getPropertyValue = function(name, object) {
	var value = object[name];
	if (name == "flag") {
		return ReportDesigner.getFlagName(value);
	}
	if (name == "layout") {
		return ReportDesigner.getLayoutName(value);
	}
	if (name == "hasChild") {
		return value ? "有" : "无";
	}
	if (name == "status") {
		return value == 1 ? "锁定" : "编辑";
	}
	return value;
};

ReportDesigner.getFlagName = function(flag) {
	if (flag == 0) {
		return "树节点";
	}
	if (flag == 1) {
		return "报表节点";
	}
	return "其他";
};

ReportDesigner.getLayoutName = function(layout) {
	if (layout == 1) {
		return "横向布局";
	}
	if (layout == 2) {
		return "纵向布局";
	}
	return "无";
};

// 
// tabs 相关操作
// 

ReportDesigner.getSelectedTabIndex = function() {
	var tab = $('#tabs').tabs('getSelected');
	return $('#tabs').tabs('getTabIndex', tab);
};

ReportDesigner.selectTab = function(index) {
	$('#tabs').tabs('select', index);
};

ReportDesigner.tabSelectedHandlder = function(title, currIndex) {
	ReportDesigner.saveChanged(currIndex, function(index) {
		if (index == 0) {
			// 刷新
			return sqlEditor.setValue('');
		}
		if (index > 1) {
			var tab = $('#tabs').tabs('getTab', index);
			var id = tab.panel('options').id;
			ReportDesigner.selectAndExpandTreeNode(id);
		}
	});
};

ReportDesigner.loadDataToTab = function(index) {
	// 非固定tab
	if (index > 1) {
		return;
	}
	var node = ReportDesigner.getSelectedTreeNode();
	if (node) {
		// 报表配置
		if (index == 0) {
			return ReportDesigner.edit(node.attributes);
		}
		// 参数设置
		if (index == 1) {
			return ReportDesigner.loadDataToQueryParamTab(node.attributes);
		}
	}
};

ReportDesigner.clearTab = function(index) {
	if (index == 0) {
		ReportDesigner.clearAllEditor();
		ReportDesigner.resetSettingsForm();
		return ReportCommon.clearDataGrid('#sqlColumnGrid');
	}
	if (index == 1) {
		$('#queryParamForm').form('reset');
		return ReportCommon.clearDataGrid('#queryParamGrid');
	}
};

ReportDesigner.clearAllCheckedNode = function(target) {
	var nodes = $(target).tree('getChecked');
	if (nodes) {
		for (var i = 0; i < nodes.length; i++) {
			$(target).tree('uncheck', nodes[i].target);
		}
	}
};

ReportDesigner.clearAllTab = function() {
	ReportDesigner.clearTab(0);
	ReportDesigner.clearTab(1);
};

ReportDesigner.loadAllTabData = function() {
	ReportDesigner.loadDataToTab(0);
	ReportDesigner.loadDataToTab(1);
};

ReportDesigner.reloadSelectedTab = function() {
	var index = ReportDesigner.getSelectedTabIndex();
	ReportDesigner.clearTab(index);
	ReportDesigner.loadDataToTab(index);
};

ReportDesigner.tabContextMenu = function(item) {
	if (item.name == "current") {
		return ReportDesigner.closeCurrentTab();
	}
	if (item.name == "others") {
		return ReportDesigner.closeOthersTab();
	}
	if (item.name == "all") {
		return ReportDesigner.closeAllTab();
	}
	return;
};

ReportDesigner.closeCurrentTab = function() {
	var tab = $('#tabs').tabs('getSelected');
	var options = tab.panel('options');
	if (options.closable) {
		$('#tabs').tabs('close', tab.panel('options').title);
	}
};

ReportDesigner.closeOthersTab = function() {
	var currentTab = $('#tabs').tabs('getSelected');
	var currTitle = currentTab.panel('options').title;
	$('.tabs-inner span').each(function(i, n) {
		var title = $(n).text();
		var tab = $('#tabs').tabs('getTab', title);
		if (tab) {
			var options = tab.panel('options');
			if (title != currTitle && options.closable) {
				$('#tabs').tabs('close', title);
			}
		}
	});
};

ReportDesigner.closeAllTab = function() {
	$('.tabs-inner span').each(function(i, n) {
		var title = $(n).text();
		var tab = $('#tabs').tabs('getTab', title);
		if (tab) {
			var options = tab.panel('options');
			if (options.closable) {
				$('#tabs').tabs('close', title);
			}
		}
	});
};

