var copyData ='';
$(function() {
	// 注册 生成报表 按钮
	$('#btnGenarate').click(ReportTemplate.generate);
	// 注册 生成报表 按钮
	$('#btnShowChart').click(ReportTemplate.showChart);
	// 注册 统计列/计算列 全选按钮
	$('#checkAllStatColumn').click(ReportTemplate.checkedAllStatColumn);
	// 注册 导出excel 按钮
	$('#btnExportToExcel').click(ReportTemplate.exportToExcel);
	
	ReportTemplate.generate();
	getAllClick();
});

function getAllClick(){
	$('#generateCharts').on('click',ReportChart.generateNew);
}

function getFrom(){
	var fisrtFrom = $('#templateFrom').html();
	var chartFrom = $('#chartFrom').html();
	/*var pushFrom = $('#pushFrom');
	var $body=fisrtFrom;
	$body+=chartFrom;
	$body+='<button id="pushAll"></button>';
	pushFrom.append($body);
    $('#pushAll').click();
    pushFrom.html('');*/
	var $form = '<form id="dataForm" >';
	$form += fisrtFrom;
	$form += chartFrom;
	$form += '</form>';
	$('body').append($form);
}

var ReportTemplate = function() {
	
};

//　生成报表
ReportTemplate.generate = function(callback) {
	
	// 获取是否合并信息
	$('#isRowSpan').val($('#isMergeRow').prop('checked'));
	$.ajax({
		type : "POST",
		url : queryPageRootUrl + 'generate',
		data : $("#templateFrom").serialize(),
		dataType : "json",
		beforeSend : function() {
			$('#loadingText').html("报表正在生成中, 请稍等...");
			$('#loading').show();
		},
		success : function(result) {
			$('#reportDiv').html(result.htmlTable);
			// 渲染报表
			ReportTemplate.initTable();
			ReportTemplate.filterTable = ReportTemplate.renderFilterTable(result);
			if (callback instanceof Function) {
				callback();
			}
			ReportChart.generate();
		},
		complete : function() {
			$('#loading').hide();
		}
	});
}

// 全选/全部清除
ReportTemplate.checkedAllStatColumn = function(e) {
	var checked = $("input[name='checkAllStatColumn']").prop("checked");
	$("input[name='statColumns']").prop("checked", checked);
};

// 渲染报表
ReportTemplate.initTable = function() {
	var table = $("#smartreport");
	return ReportTemplate.renderClassicReport(table);
}

// 渲染,排序
ReportTemplate.renderClassicReport = function(table) {
	$("#smartreport>tbody>tr").click(function() {
		$('#smartreport .selected').removeClass('selected').removeAttr('title');
		$(this).addClass('selected');
	});
	$('#smartreport>tbody>tr').mouseover(function(e) {
		$(this).addClass('selected');
	});
	$('#smartreport>tbody>tr').mouseleave(function(e) {
		$('#smartreport .selected').removeClass('selected').removeAttr('title');
	});
	
	var noRowSpan = !ReportTemplate.hasRowSpan();
	table.data('isSort', noRowSpan).fixScroll();
	
	//如果表格中没有跨行rowspan(暂不支持跨行)
	if (noRowSpan) {
		table.tablesorter({
			sortInitialOrder : 'desc'
		});
		table.find('>thead>tr').attr({
			title : "点击可以排序"
		}).css({
			cursor : "pointer"
		});
	}
}

//表格中是否跨行
ReportTemplate.hasRowSpan = function() {
	var rowspans = $("#easyreport>tbody>tr>td[rowspan]");
	return (rowspans && rowspans.length > 0);
};

// 导出excel表格
ReportTemplate.exportToExcel = function(e) {
	var htmlText = '';
	htmlText += (ReportTemplate.filterTable || '');
	htmlText += '<table>' + $('#smartreport').html() + '</table>';
	var bytes = ReportTemplate.getBytes(htmlText);
	if (bytes > 2000000) {
		htmlText = "large";
	}
	var postUrl = queryPageRootUrl + 'exportToExcel';
	var postData = $('#templateFrom').serializeObject();
	postData["htmlText"] = htmlText;

	$('#loadingText').html("正在导出Excel中, 请稍等...");
	$('#loading').show();
	$.fileDownload(postUrl, {
		httpMethod : "POST",
		data : postData
	}).done(function() {
		$('#loading').hide();
	}).fail(function() {
		$('#loading').hide();
	});
	e.preventDefault();
};

//将报表上面的过滤信息拼成table，用于写入excel中
ReportTemplate.renderFilterTable = function(result) {
	var html = '<table>';
	html += '<tr><td align="center" colspan="'+result.metaDataColumnCount+'"><h3>' + $('#rpTitle').text() + '</h3></td></tr>';
	html += '<tr><td align="right" colspan="'+result.metaDataColumnCount+'"><h3>导出时间:' + currentTime()+ '</h3></td></tr>';
	$('#templateFrom .j-item').each(function() {
		var type = $(this).attr('data-type');
		if (type === 'date-range') {
			var input = $(this).find('.combo-text');
			html += '<tr><td align="right" colspan="'+result.metaDataColumnCount+'"><strong>时间范围:</strong>' + input.eq(0).val() + '~' + input.eq(1).val() + '</td></tr>';
		} else if (type === 'checkbox') {
			html += '<tr><td align="right" colspan="'+result.metaDataColumnCount+'"><strong>筛选统计列:</strong>';
			var rowChoose = [];
			$(this).find('input[type="checkbox"]:checked').each(function() {
				rowChoose.push($(this).attr('data-name'));
			})
			html += rowChoose.join('、');
			html += '</td></tr>';
		}
		else if (new RegExp('datebox').test($(this).find("input").attr("class"))) {
			var label = $(this).find('label').text().replace(':', '');
			var val = $(this).find("input").attr("value");
			if(!val){
				val = $(this).find('.combo-text').val();
			}
			html += '<tr><td><strong>' + label + '</strong></td><td>' + val + '</td></tr>';
		}
		else {
			var label = $(this).find('label').text().replace(':', '');
			var val = $(this).find('.combo-text').val();
			if(!val){
				val = $(this).find("input").attr("value");
			}
			html += '<tr><td><strong>' + label + '</strong></td><td>' + val + '</td></tr>';
		}
	})
	html += '<tr></tr><tr></tr><tr></tr></table>';
	return html;
};

function currentTime(){
	var d = new Date(),str = '';
	str += d.getFullYear()+'-';
	str  += d.getMonth() + 1+'-';
	str  += d.getDate()+' ';
	str += d.getHours()+':';
	str  += d.getMinutes()+':';
	str+= d.getSeconds()+'';
	return str;
};

ReportTemplate.getBytes = function(str) {
	var totalLength = 0;
	var i;
	var charCode;
	for (i = 0; i < str.length; i++) {
		charCode = str.charCodeAt(i);
		if (charCode < 0x007f) {
			totalLength = totalLength + 1;
		} else if ((0x0080 <= charCode) && (charCode <= 0x07ff)) {
			totalLength += 2;
		} else if ((0x0800 <= charCode) && (charCode <= 0xffff)) {
			totalLength += 3;
		}
	}
	return totalLength;
};

ReportTemplate.showChart = function() {
	var title = $('#rpName').val() + "(图表)";
	var id = $('#rpId').val();
	var uid = $('#rpUid').val();
	parent.showChart(title, id, uid);
}


var ReportChart = function() {
	
};

ReportChart.metaData = null;

ReportChart.generateNew = function(){
	if (ReportChart.checkStatColumn()) {
		ReportChart.show("chart1",'line');
	}
};

ReportChart.generate = function(e) {
	var formParams = $("#templateFrom").serializeObject();
	var dataParams = $('#chartFrom').serializeObject();
	
	$.ajax({
		type : "POST",
		url : chartPageRootUrl + 'getData',
		data : $.extend(true, formParams, dataParams),
		dataType : "json",
		beforeSend : function() {
			$('#loadingText').html("图表正在生成中, 请稍等...");
			$('#loading').show();
		},
		success : function(metaData) {
			copyData = metaData;
			ReportChart.metaData = metaData;
			ReportChart.initDimOptions();
			ReportChart.clear();
			ReportChart.setDimDefaultValue();
			if (ReportChart.checkStatColumn()) {
				ReportChart.show("chart1",'line');
				//ReportChart.show("chart2",'bar');
			}
		},
		complete : function() {
			$('#loading').hide();
		}
	});
};

ReportChart.initDimOptions = function() {
	for(var key in ReportChart.metaData.dimColumnMap){
		var id = "#dim_"+key;
		$(id).combobox({
			textField:'text',
		    valueField:'value',
		    data:ReportChart.metaData.dimColumnMap[key],
		    onSelect:function(option){
		    	if(option.value == "all"){
		    		ReportChart.setXAxisDimOptions();
		    	}
		    }
		});
	}
};

ReportChart.viewChart = function(e) {
	if (ReportChart.metaData && ReportChart.checkStatColumn()) {
		ReportChart.show('chart1');
		//ReportChart.show('chart2');
	}
};

ReportChart.addChart = function(e) {
	if (ReportChart.metaData && ReportChart.checkStatColumn()) {
		var count = $("div[id*='chart']").size() + 1;
		var id = 'chart' + count;
		$("#chart1").after("<div id=\"" + id + "\" style=\"height: 345px; border: 1px solid #ccc;\"></div>");
		//$("#chart2").after("<div id=\"" + id + "\" style=\"height: 345px; border: 1px solid #ccc;\"></div>");
		ReportChart.show(id);
	}
};

ReportChart.clear = function() {
	$("div[id*='chart']").each(function() {
//		if ($(this).attr("id") != "chart1" || $(this).attr("id") != "chart2") {
		if ($(this).attr("id") != "chart1") {
		//	$(this).remove();
		}
	});
};

ReportChart.resetChart = function(e) {
	if (ReportChart.metaData) {
		ReportChart.setDimDefaultValue();
		$("div[id*='chart']").each(function() {
			if ($(this).attr("id") != "chart1") {
				$(this).remove();
			}
		});
		ReportChart.show('chart1');
		//ReportChart.show('chart2');
	}
};

ReportChart.toggleChart = function(e) {
	if (ReportChart.metaData) {
		$("div[id*='chart']").each(function() {
			var dom = document.getElementById($(this).attr("id"));
			var currChart = require('echarts').init(dom);
			var xAxis = currChart.getOption().xAxis;
			var yAxis = currChart.getOption().yAxis;
			currChart.setOption({
				xAxis : yAxis,
				yAxis : xAxis
			});
		});
	}
};

ReportChart.checkStatColumn = function() {
	return ReportChart.checkStatColumnCount() && ReportChart.checkCanSelectAllDimCount();
};

ReportChart.checkStatColumnCount = function() {
	var checkedStatColumnCount = ReportChart.getCheckedStatColumnCount();
	if (checkedStatColumnCount < 1) {
		$.messager.alert('失败', "您没有选择统计列!", 'error');
		return false;
	}
	return true;
};

ReportChart.checkCanSelectAllDimCount = function() {
	var count = ReportChart.getCanSelectAllDimCount();
	if (ReportChart.isSelectMutiAll()) {
		$.messager.alert('失败', "只能选择" + count + "个维度的[全部]项!", 'error');
		return false;
	}
	return true;
};

ReportChart.setDimDefaultValue = function() {
	var dimColumns = ReportChart.metaData.dimColumns;
	var canSelectAllDimCount = ReportChart.getCanSelectAllDimCount();
	for (var i = 0; i < dimColumns.length; i++) {
		if (i < canSelectAllDimCount) {
			continue;
		}
		var id = "#dim_" + dimColumns[i].name;
		var values = ReportChart.getComoboxValues(id);
		if (values.length > 1) {
			$(id).combobox('select',values[1]);
		}
	}
	ReportChart.setXAxisDimOptions();
};

ReportChart.getComoboxValues = function(id){
	return $.map($(id).combobox('getData'),function(rec){
		return rec.value;
	});
};

ReportChart.getCheckedStatColumnCount = function() {
	var statColumns = ReportChart.getCheckedStatColumns();
	return statColumns ? statColumns.length : 0;
};

ReportChart.getCanSelectAllDimCount = function() {
	var checkedStatColumnCount = ReportChart.getCheckedStatColumnCount();
	return checkedStatColumnCount == 1 ? 2 : 1;
};

ReportChart.getCheckedStatColumns = function() {
	return $("input[name='statColumns']:checked").map(function() {
		return $(this).val();
	}).get();
};

ReportChart.getCheckedStatColumn = function() {
	if (ReportChart.hasSecondAllDim()) {
		var name = $("input[name='statColumns']:checked").map(function() {
			return $(this).val();
		}).get()[0];
		var checkedDims = $.grep(ReportChart.metaData.statColumns, function(statColumn) {
			return statColumn.name == name;
		});
		if (checkedDims && checkedDims.length > 0) {
			return checkedDims[0];
		}
	}
	return null;
};

ReportChart.getDisplayStatColumns = function() {
	var checkedStatColumns = ReportChart.getCheckedStatColumns();
	return $.grep(ReportChart.metaData.statColumns, function(statColumn) {
		return $.inArray(statColumn.name, checkedStatColumns) > -1;
	});
};

ReportChart.isExistAllOption = function() {
	var exists = false;
	var dimColumns = ReportChart.metaData.dimColumns;
	for (var i = dimColumns.length - 1; i >= 0; i--) {
		id = "#dim_" + dimColumns[i].name;
		var value = $(id).combobox('getValue');
		if (value == "all") {
			exists = true;
			break;
		}
	}
	return exists;
};

// 是否选择多个维度的all(全部)项，
// 因为图表只支持二维，所以选择多个all项无效
ReportChart.isSelectMutiAll = function() {
	var dimColumns = ReportChart.metaData.dimColumns;
	var canSelectAllDimCount = ReportChart.getCanSelectAllDimCount();
	var count = 0;
	for (var i = 0; i < dimColumns.length; i++) {
		id = "#dim_" + dimColumns[i].name;
		var value = $(id).combobox('getValue');
		if (value == "all") {
			count++;
		}
	}
	return count > canSelectAllDimCount;
};

ReportChart.setXAxisDimOptions = function() {
	var dimColumns = ReportChart.metaData.dimColumns;
	var exist = ReportChart.isExistAllOption();
	$('#xAxisDim').empty();
	for (var i = 0; i < dimColumns.length; i++) {
		var id = "#dim_" + dimColumns[i].name;
		var value = $(id).combobox('getValue');
		if (!exist || (exist && value == "all")) {
			$("#xAxisDim").append("<option value='" + dimColumns[i].name + "'>" + dimColumns[i].text + "</option>");
		}
		
	}
};

ReportChart.getSecondAllDimName = function() {
	var showDimName = $("#xAxisDim").val();
	if (ReportChart.hasSecondAllDim()) {
		return $("#xAxisDim option").map(function() {
			if ($(this).val() != showDimName) {
				return $(this).val();
			}
		}).get()[0];
	}
	return null;
};

ReportChart.hasSecondAllDim = function() {
	return (ReportChart.getCheckedStatColumnCount() == 1 && ReportChart.isExistAllOption() && $("#xAxisDim option").size() == 2);
};

ReportChart.checkedAllStatColumn = function() {
	var checked = $("input[name='checkAllStatColumn']").prop("checked");
	$("input[name='statColumns']").prop("checked", checked);
};

ReportChart.toChartData = function(chartType) {
	var chartData = {
		"title" : ReportChart.getTitle(),
		"legends" : ReportChart.getLegendData(),
		"categories" : ReportChart.getCategories(),
		"series" : ReportChart.getSeries(chartType)
	};
	return chartData;
};

ReportChart.getTitle = function() {
	var dimColumns = ReportChart.metaData.dimColumns;
	var names = [];
	names.push($('#rpName').val() + ' (');
	for (var i = 0; i < dimColumns.length; i++) {
		var id = "#dim_" + dimColumns[i].name;
		var value = $(id).combobox('getValue');
		var text = dimColumns[i].text + ":" + value;
		names.push(text);
	}
	var checkedStatColumn = ReportChart.getCheckedStatColumn();
	if (checkedStatColumn) {
		names.push(checkedStatColumn.text);
		
	}
	names.push(')');
	return names.join(' ');
};

ReportChart.getLegendData = function() {
	return $.map(ReportChart.getLegends(), function(e) {
		return e.text;
	});
};

ReportChart.getLegends = function() {
	var showDimName = ReportChart.getSecondAllDimName();
	if (showDimName) {
		var id = "#dim_" + showDimName;
		var values = ReportChart.getComoboxValues(id);
		return $.map(values,function(value){
			if (value != "all") {
				return {
					"name" : showDimName,
					"text" : value
				};
			}
		});
	}
	return ReportChart.getDisplayStatColumns();
};

ReportChart.getCategories = function() {
	return ReportChart.getCategory().data;
};

ReportChart.getCategory = function() {
	var dimName = $('#xAxisDim').val();
	var id = "#dim_" + dimName;
	var sortType = $("#sortType").val();
	var values = null;

	if (ReportChart.isExistAllOption()) {
		values = $.map(ReportChart.getComoboxValues(id),function(value){
			if (value != "all") {
				return value;
			}
		});
	} else {
		values = [];
		values.push($(id).combobox('getValue'));
	}

	values = values.sort();
	if (sortType == "desc") {
		values = values.reverse();
	}

	return {
		"name" : dimName,
		"data" : values
	};
};

ReportChart.getSeries = function(chartType) {
	var dimColumns = ReportChart.metaData.dimColumns;
	var keyValues = [];
	for (var i = 0; i < dimColumns.length; i++) {
		
		var id = "#dim_" + dimColumns[i].name;
		var value = $(id).combobox('getValue');
		if (value == "all") {
			value = "#{" + dimColumns[i].name + "}";
		}

		keyValues.push(value.replace('$', '*'));
	}
	var keyTemplate = keyValues.join('$') + '$';
	var category = ReportChart.getCategory();
	var legends = ReportChart.getLegends();
	var categoryName = "#{" + category.name + "}";
	var series = [];
	for (var j = 0; j < legends.length; j++) {
		
		series.push({
			name : legends[j].text,
			type : chartType,
			data : []
		});
	}

	if (ReportChart.hasSecondAllDim()) {
		var secondAllDimName = "#{" + ReportChart.getSecondAllDimName() + "}";
		var statColumnName = ReportChart.getCheckedStatColumn().name;
		for (var i = 0; i < category.data.length; i++) {
			var tempKey = keyTemplate.replace(categoryName, category.data[i].replace('$', '*'));
			for (var j = 0; j < legends.length; j++) {
				var key = tempKey.replace(secondAllDimName, legends[j].text.replace('$', '*'));
				var row = ReportChart.metaData.dataRows[key];
				series[j].data.push(row ? row[statColumnName] : 0);
			}
		}
		return series;
	}

	for (var i = 0; i < category.data.length; i++) {
		var key;
		if(category.data[i] == null){
			key = keyTemplate;
		} else {
			key = keyTemplate.replace(categoryName, category.data[i].replace('$', '*'));
		}
		var row = ReportChart.metaData.dataRows[key];
		for (var j = 0; j < legends.length; j++) {
			
			var columnName = legends[j].name;
			series[j].data.push(row ? row[columnName] : 0);
		}
	}
	return series;
};

ReportChart.show = function(id, chartType) {
	chartType = chartType || "line";
	var chartData = ReportChart.toChartData(chartType);
	
	require.config({
		paths : {
			'echarts' : getContextPath() + '/resources/lib/echarts/echarts',
			'echarts/chart/bar' : getContextPath() + '/resources/lib/echarts/chart/bar',
			'echarts/chart/line' : getContextPath() + '/resources/lib/echarts/chart/line'
		}
	});
	require([ 'echarts', 'echarts/chart/bar', 'echarts/chart/line' ], function(ec) {
		var myChart = ec.init(document.getElementById(id));
		myChart.clear();
		myChart.setOption({
			title : {
				text : chartData.title,
				x : 'center',
				y : 'top'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : chartData.legends,
				x : 'center',
				y : 'bottom'
			},
			toolbox : {
				show : true,
				orient : 'vertical',
				x : 'right',
				y : 'center',
				feature : {
			            magicType : {show: true, type: ['line', 'bar']},
			            restore : {show: true},
			            saveAsImage : {show: true}
			    }
			},
			calculable : true,

			xAxis : [ {
				type : 'category',
				data : chartData.categories,
				
			} ],
			yAxis : [ {
				type : 'value',
				splitArea : {
					show : true
				}
			} ],
			series : chartData.series
		});
	});
}
