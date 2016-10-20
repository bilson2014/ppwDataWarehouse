var myChart;
$().ready(
	function() {
		$("#bar").on('click',function(){
			var json = eval('({"dataRow": {"10": {"rate": 0.2,"price": 10000},"20": {"rate": 0.2,"price": 49800},"40": {"rate": 0.6,"price": 29800}},"dimColumns": [{"name": "num","text": "项目数"},{"name": "price","text": "项目金额"},{"name": "rate","text": "比例"}]})');
			var myChart = echarts.init(document.getElementById('main'));
			var option = parser(json, 'bar');
			if(option == null)
				alert('图标生成错误');
			else
				myChart.setOption(option);
			console.log($.toJSON(option));
		});
		$("#line").on('click',function(){
			var json = eval('({"dataRow": {"10": {"rate": 0.2,"price": 10000},"20": {"rate": 0.2,"price": 49800},"40": {"rate": 0.6,"price": 29800}},"dimColumns": [{"name": "num","text": "项目数"},{"name": "price","text": "项目金额"},{"name": "rate","text": "比例"}]})');
			var myChart = echarts.init(document.getElementById('main'));
			var option = parser(json, 'line');
			if(option == null)
				alert('图标生成错误');
			else
				myChart.setOption(option);
			console.log($.toJSON(option));	
				});
		$("#pie").on('click',function(){
			var json = eval('({"dataRow": {"10": {"price": 10000},"20": {"price": 49800},"40": {"price": 29800}},"dimColumns": [{"name": "num","text": "项目数"},{"name": "price","text": "项目金额"}]})');
			var myChart = echarts.init(document.getElementById('main'));
			var option = parser(json, 'pie');
			if(option == null)
				alert('图标生成错误');
			else
				myChart.setOption(option);
			console.log($.toJSON(option));
		});
});