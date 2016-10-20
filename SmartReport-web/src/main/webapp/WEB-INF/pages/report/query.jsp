<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- import javascript -->
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>报表查询</title>
	<%@ include file="/WEB-INF/pages/includes/header.jsp" %>
	<%@ include file="/WEB-INF/pages/includes/form_scripts.jsp" %>
    <script src="<%=request.getContextPath()%>/resources/js/report/query.js?v=<%=Math.random()%>"></script>
</head>
<body id="layout" class="easyui-layout" style="text-align: left">
<!-- left tree start -->
<div id="west" region="west" border="false" split="true" title=" " style="width: 250px; padding: 5px;">
    <ul id="reportTree"></ul>
</div>
<!-- left tree end -->

<!-- right tab start -->
<div region="center" border="false">
    <div id="tabs" class="easyui-tabs" fit="true" border="false" plain="true"></div>
</div>
<!-- right tab end -->

</body>
</html>