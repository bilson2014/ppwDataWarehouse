/**
 * 标题
 */
function title(text) {
	this.text = text;
}
/**
 * 提示框
 */
function tooltip(trigger) {
	this.trigger = trigger;
}
/**
 * 图例
 */
function legend(data) {
	this.data = data;
}
/**
 * X 轴<br>
 * 'value' 数值轴，适用于连续数据。<br>
 * 'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。<br>
 * 'time'
 * 时间轴，适用于连续的时序数据，与数值轴相比时间轴带有时间的格式化，在刻度计算上也有所不同，例如会根据跨度的范围来决定使用月，星期，日还是小时范围的刻度。<br>
 * 'log' 对数轴。适用于对数数据。<br>
 */
function xAxis(name, type, data) {
	this.name = name;
	this.type = type;
	this.data = data;
}
/**
 * Y 轴
 */
function yAxis(type, name) {
	this.type = type;
	this.name = name;
}

function series() {
	this.name = '';
	this.type = '';
	this.data = {};
	this.yAxisIndex = 0;

	this.setName = function(name) {
		this.name = name;
	}
	this.setType = function(type) {
		this.type = type;
	}
	this.setData = function(data) {
		this.data = data;
	}
	this.setYAxisIndex = function(yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
	}
	this.getData = function() {
		return this.data;
	}
	this.getName = function (){
		return this.name;
	}
}
function pieItem(value, name) {
	this.value = value;
	this.name = name;
}
function option(title, tooltip, legend, xAxis, yAxis, series) {
	this.title = title;
	this.tooltip = tooltip;
	this.legend = legend;
	this.xAxis = xAxis;
	this.yAxis = yAxis;
	this.series = series;
}

function parser(json, type) {
	// 源对象
	var optTitle;
	var optTooltip;
	var optLegend;
	var optXAxis;
	var optYAxis;
	var optSeries;

	// 构造源对象（不求效率，但求清晰）
	switch (type) {
	case 'line':
	case 'bar':
		// 解析坐标系类型统计图
		optTooltip = new tooltip('axis');
		optLegend = parserLegend(json.dimColumns);
		optXAxis = parserXAxis('category', json.dataRow);
		optYAxis = parserYAxis(json.dimColumns);
		optSeries = parserSeries(json.dimColumns, json.dataRow, type);
		return new option(new title(''), optTooltip, optLegend, optXAxis,
				optYAxis, optSeries);
	case 'pie':
		// 解析数据项类型统计图
		if(json.dimColumns.length != 2)
			return null;
		optTooltip = new tooltip('item');
		optLegend = parserLegend(json.dimColumns);
		optSeries = parserSeries2(json.dimColumns, json.dataRow, type);
		return new option(new title(''), optTooltip, optLegend, null, null,
				optSeries);
		break;
	}
}
function parserTitle(titleText) {
	return new title(titleText);
}
function parserTooltip(type) {
	return new tooltip(type);
}
function parserLegend(columns) {
	var xData = new Array();
	for (var int = 0; int < columns.length; int++) {
		if (int == 0)
			continue;
		xData.push(columns[int].text);
	}
	return new legend(xData);
}
function parserXAxis(columns, data) {
	var xField = columns.name;
	var xName = columns.text;
	var xData = new Array();
	for ( var key in data) {
		xData.push(key);
	}
	return new xAxis(xName, 'category', xData);
}
function parserYAxis(field) {
	var yDatas = new Array();
	for (var int = 0; int < field.length; int++) {
		if (int == 0)
			continue;
		var obj = field[int];
		yDatas.push(new yAxis('value', obj.text));
	}
	return yDatas;
}
function parserSeries(columns, data, type) {
	var seriesArray = new Array();
	var seriesArray1 = new Array();
	for (var int = 0; int < columns.length; int++) {
		if (int == 0)
			continue;
		var obj = columns[int];
		var s = new series();
		s.setName(obj.text);
		s.setYAxisIndex(int - 1);
		s.setType(type);
		s.setData(new Array());
		seriesArray[obj.name] = s;
	}
	for ( var da in data) {
		var obj = data[da];
		for ( var field in obj) {
			var array = seriesArray[field].getData();
			array.push(obj[field]);
		}
	}
	for (var int = 0; int < columns.length; int++) {
		if (int == 0)
			continue;
		var obj = columns[int];
		seriesArray1.push(seriesArray[obj.name]);
	}
	return seriesArray1;
}
function parserSeries2(columns, data, type) {
	var seriesArray = new Array();
	var seriesArray1 = new Array();
	for (var int = 0; int < columns.length; int++) {
		if (int == 0)
			continue;
		var obj = columns[int];
		var s = new series();
		s.setName(obj.text);
		s.setYAxisIndex(int - 1);
		s.setType(type);
		s.setData(new Array());
		seriesArray[obj.name] = s;
	}
	for (var da in data) {
		var obj = data[da];
		for (var field in obj) {
			var array = seriesArray[field].getData();
			array.push(new pieItem(obj[field],da));
		}
	}
	for (var int = 0; int < columns.length; int++) {
		if (int == 0)
			continue;
		var obj = columns[int];
		seriesArray1.push(seriesArray[obj.name]);
	}
	return seriesArray1;
}