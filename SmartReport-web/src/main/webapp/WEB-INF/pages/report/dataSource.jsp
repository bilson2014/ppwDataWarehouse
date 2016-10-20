<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- import JavaScript -->
<spring:url value="/resources/js/report/dataResource.js" var="dataResourceJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>数据源管理</title>
	<%@ include file="/WEB-INF/pages/includes/header.jsp" %>
	<%@ include file="/WEB-INF/pages/includes/form_scripts.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

	<!-- DataGride -->
	<div data-options="region:'center',border:false" >
		<div id="datasourceGrid"></div>
	</div>
	
	<!-- form -->
	<div id="datasourceDlg" class="easyui-dialog" style="width: 600px; height: 300px; padding: 10px 20px;">
	    <form id="datasourceForm" method="post">
	        <table cellpadding="0" class="form-table" cellpadding="5" cellspacing="5" style="width: 99%;">
	            <tr>
	                <td class="text_r blueside">数据源名称:</td>
	                <td><input name="name" class="easyui-validatebox" required="true" style="width: 300px;"/></td>
	            </tr>
	            <tr>
	                <td class="text_r blueside">用户名:</td>
	                <td><input id="configUser" name="user" class="easyui-validatebox" required="true"
	                           style="width: 300px;"/></td>
	            </tr>
	            <tr>
	                <td class="text_r blueside">密码:</td>
	                <td><input id="configPassword" name="password" class="easyui-validatebox" required="true"
	                           style="width: 300px;"/> <input id="datasourceId"
	                                                          type="hidden" name="id" value="0"/> <input
	                        id="datasourceAction" type="hidden" name="action"/></td>
	            </tr>
	
	            <tr>
	                <td class="text_r blueside top" width="22%">JDBC连接字符串:</td>
	                <td><textarea id="configJdbcUrl" name="jdbcUrl" style="width: 99%; height: 100px;"
	                              class="easyui-validatebox" required="true"></textarea></td>
	            </tr>
	        </table>
	    </form>
	</div>
	
	<script type="text/javascript" src="${dataResourceJs }"></script>
</body>
</html>