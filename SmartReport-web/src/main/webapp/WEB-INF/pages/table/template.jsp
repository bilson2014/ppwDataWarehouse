<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>${name}</title>
	<%@ include file="/WEB-INF/pages/table/header.jsp"%>
	<%@ include file="/WEB-INF/pages/table/form_scripts.jsp"%>
	<%@ include file="/WEB-INF/pages/table/report_scripts.jsp"%>
	<script src="<%=request.getContextPath()%>/resources/lib/echarts/esl.js"></script>
    <script src="<%=request.getContextPath()%>/resources/lib/echarts/echarts.js"></script>

</head>
<body>
	<!-- loading information start -->
	<div id="loading" style="">
	    <div class="loadContent">
	        <div  class="loadTitle" style="">
	                                             操作提示 <span id="Per"></span><span id="SCount" style="font-weight: normal"></span>
	        </div>
	        <div class="loadMid" style="">
	            <br/>
	            <table style="position:relative;left:44px;top:37px">
	                <tr>
	                    <td valign="top">
	                    	<img alt="loading" border="0" src="<%=request.getContextPath()%>/resources/images/icons/loading.gif"/>
	                    </td>
	                    <td valign="middle" style="font-size: 14px;" id="loadingText">报表正在生成中, 请稍等...</td>
	                </tr>
	            </table>
	            <br/>
	        </div>
	    </div>
	    <div style="position: absolute; width: 270px; z-index: 299; left: 4px; top: 5px; background-color: #E8E8E8;"></div>
	</div>
	<!-- loading information end -->
	
	<!-- comment info start -->
	<div id="rpCommentTip" tabindex="-1" class="tooltip tooltip-bottom" style="display: none; width: 400px; right: 2px; top: 95px; z-index: 9001; display: none; background-color: rgb(102, 102, 102); border-color: rgb(102, 102, 102);">
	    <div class="tooltip-content">
	        <span style="color: #fff;" id="rpComment"><c:if test="${empty comment}">该报表没有说明信息！</c:if>${comment}</span>
	    </div>
	    <div class="tooltip-arrow-outer" style="border-bottom-color: rgb(102, 102, 102); left: 90%;"></div>
	    <div class="tooltip-arrow" style="border-bottom-color: rgb(102, 102, 102); left: 90%;"></div>
	</div>
	<!-- comment info end -->
	
	<!-- MetaDataColumn start -->
	<div style="margin: 5px;">
	    <form id="templateFrom" method="post">
	        <table cellpadding="0" class="form-table" cellspacing="0" style="width: 99%;">
	            <tr class="text_center blueside" style="height: 45px;">
	                <td colspan="2" id="rpTitle" style="font-size: 24px;">${name}</td>
	            </tr>
	            <tr style="height: 40px;">
	                <td style="text-align: left; vertical-align: middle; white-space: normal;">${formHtmlText}
	                   <div class="mergrRow">
	                       <!-- <input id="isMergeRow" type="checkbox" name="isMergeRow" checked="checked" class="myCheckBox"/> -->
	                        <div class="checkboxMyselfFirstPage">
                                    <input type="checkbox" type="checkbox" name="isMergeRow" checked="checked" class="myCheckBox" id="isMergeRow" />
                                    <label for="isMergeRow"></label>
                            </div>
	                       <span>合并左边相同维度行</span>
	                   </div> 
	                    <a id="btnGenarate" href="#" class="easyui-linkbutton" style="vertical-align: bottom"
	                       data-options="iconCls:'icon-ok'">生成报表</a></td>
	                <td style="text-align: right; vertical-align: middle; white-space: normal;">
	                   <div id="btnExportToExcel" class="exportExcel">
	                        <img style="cursor: pointer;"  title="导出报表"
	                         src="<%=request.getContextPath()%>/resources/images/biao/export.png"/>&nbsp;
	                                                                                   <span>导出报表</span>
	                   </div>      
	                    <img style="cursor: pointer;" id="btnShowChart" title="图表展示"
	                         src="<%=request.getContextPath()%>/resources/images/icons/chart.png"/>&nbsp;
	                    <input id="rpId" type="hidden" name="id" value="${id}"/>
	                    <input id="rpName" type="hidden" name="name" value="${name}"/>
	                    <input id="rpUid" type="hidden" name="uid" value="${uid}"/>
	                    <input id="isRowSpan" type="hidden" name="isRowSpan" value="true"/></td>
	            </tr>
	            <c:if test="${not empty statColumHtmlText}">
	                <tr style="height: 40px;">
	                    <td colspan="2"
	                        style="text-align: left; vertical-align: middle; white-space: normal; word-wrap: break-word;">${statColumHtmlText}</td>
	                </tr>
	            </c:if>
	        </table>
	    </form>
	</div>
	<!-- MetaDataColumn end -->
	
	<!-- reporting body start -->
	<div class="controlTable">
			<div style="margin: 5px;" id="reportDiv">${htmlTable}</div>
			
	</div>

	 <form class="chartFrom" id="chartFrom" method="post">               		
	   	  <label class="choose" style="width: 10px;"></label>${nonStatColumHtmlText}&nbsp;
	      <label class="chooseLeft"> 横轴维度：</label><select class="formSelect" id="xAxisDim"></select>&nbsp;
	      <label class="chooseLeft">  顺序：</label>
	                <select class="formSelect" id="sortType">
	                    <option value="asc">升序</option>
	                    <option value="desc">降序</option>
	                </select>
	          <div class="getAll" id="generateCharts">生成图表</div>      
	 </form> 
	<div id="chart1" class="chart">${message}</div>
	<!-- <div id="chart2" class="chart"></div> -->
	</div>
	<!-- reporting body end -->
</body>
</html>