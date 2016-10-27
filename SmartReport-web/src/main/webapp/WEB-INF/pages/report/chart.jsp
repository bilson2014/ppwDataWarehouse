<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>${name}-报表展示图</title>
	<%@ include file="/WEB-INF/pages/includes/header.jsp" %>
	<%@ include file="/WEB-INF/pages/includes/form_scripts.jsp" %>
	<%-- <script src="<%=request.getContextPath()%>/resources/lib/echars/EchartsConfig.js"></script>
    <script src="<%=request.getContextPath()%>/resources/lib/echars/echarts.js"></script> --%>
    <script src="<%=request.getContextPath()%>/resources/lib/echarts/esl.js"></script>
    <script src="<%=request.getContextPath()%>/resources/lib/echarts/echarts.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/report/chart.js?v=<%=Math.random()%>"></script>
</head>
<body>
	<!-- loading information start -->
	<div id="loading" style="position: absolute; z-index: 300; height: 120px; width: 284px; left: 50%; top: 50%; margin-left: -150px; margin-top: -80px; display: none;">
	    <div style="position: absolute; z-index: 300; width: 270px; height: 90px; background-color: #FFFFFF; border: solid #000000 1px; font-size: 14px;">
	        <div style="height: 26px; background: #f1f1f1; line-height: 26px; padding: 0px 3px 0px 3px; font-weight: bolder;">
	            操作提示 <span id="Per"></span><span id="SCount" style="font-weight: normal"></span>
	        </div>
	        <div style="height: 64px; line-height: 150%; padding: 0px 3px 0px 3px;" align="center">
	            <br/>
	            <table>
	                <tr>
	                    <td valign="top">
	                    	<img alt="loading" border="0" src="<%=request.getContextPath()%>/resources/images/icons/loading.gif"/>
	                    </td>
	                    <td valign="middle" style="font-size: 14px;" id="loadingText">图表正在生成中, 请稍等...</td>
	                </tr>
	            </table>
	            <br/>
	        </div>
	    </div>
	    <div style="position: absolute; width: 270px; z-index: 299; left: 4px; top: 5px; background-color: #E8E8E8;"></div>
	</div>
	<!-- loading information end -->
	
	<!-- MetaDataColumn start -->
	<div style="margin: 5px;">
	    <form id="templateFrom" method="post">
	        <table cellpadding="0" class="form-table" cellspacing="0" style="width: 99%;">
	            <tr class="text_center blueside" style="height: 45px;">
	                <td id="rpName" colspan="2" style="font-size: 24px;">${name}</td>
	            </tr>
	            <tr style="height: 40px;">
	                <td style="text-align: left; vertical-align: middle; white-space: normal; word-wrap: break-word;">${formHtmlText}&nbsp;&nbsp;
	                    <a id="btnGenarate" href="#" class="easyui-linkbutton" style="vertical-align: bottom"
	                       data-options="iconCls:'icon-ok'">生成</a>
	                    <input id="rpId" type="hidden" name="id" value="${id}"/>
	                    <input id="rpName" type="hidden" name="name" value="${name}"/>
	                    <input id="rpUid" type="hidden" name="uid" value="${uid}"/>
	                </td>
	            </tr>
	            <c:if test="${not empty statColumHtmlText}">
	                <tr style="height: 40px;">
	                    <td colspan="2"
	                        style="text-align: left; vertical-align: middle; white-space: normal; word-wrap: break-word;">${statColumHtmlText}</td>
	                </tr>
	            </c:if>
	            <tr style="height: 40px;">
	                <td colspan="2" style="text-align: left; vertical-align: middle;">
	                    <label style="width: 120px;">选择维度:</label>${nonStatColumHtmlText}&nbsp;
	                    横轴维度：<select id="xAxisDim"></select>&nbsp;
	                    顺序：<select id="sortType">
	                    <option value="asc">升序</option>
	                    <option value="desc">降序</option>
	                </select>&nbsp;
	                    <a id="btnViewChart" href="#" class="easyui-linkbutton" style="vertical-align: bottom"
	                       data-options="iconCls:'icon-preview'">查看</a>&nbsp;
	                    <a id="btnResetChart" href="#" class="easyui-linkbutton" style="vertical-align: bottom"
	                       data-options="iconCls:'icon-reload'">重置</a>
	                </td>
	            </tr>
	        </table>
	    </form>
	</div>
	<!-- MetaDataColumn end -->
	
	<!-- chart start -->
	<div id="chart1" style="height: 345px; border: 1px solid #ccc;"></div>
	<!-- chart end -->
</body>
</html>