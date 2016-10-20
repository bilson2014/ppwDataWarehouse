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
});

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