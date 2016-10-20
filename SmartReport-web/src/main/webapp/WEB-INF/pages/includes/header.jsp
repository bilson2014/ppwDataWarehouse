<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- import CSS --%>
<spring:url value="/resources/images/favicon.ico" var="faviconIco" />
<spring:url value="/resources/lib/easyui/themes/metro/easyui.css" var="easyuiCss" />
<spring:url value="/resources/lib/easyui/themes/icon.css" var="iconCss" />
<spring:url value="/resources/css/main.css" var="mainCss" />

<%-- import JS --%>
<spring:url value="/resources/lib/jquery.min.js" var="jqueryJs" />
<spring:url value="/resources/lib/easyui/jquery.easyui.min.js" var="jqueryEasyuiJs" />
<spring:url value="/resources/lib/easyui/locale/easyui-lang-zh_CN.js" var="easyuiZHJs" />
<spring:url value="/resources/lib/jquery.extension.js" var="jqueryExtensionJs" />
<spring:url value="/resources/lib/artTemplate.js" var="artTemplateJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/js/basePathConfig.js" var="basePathConfigJs" />

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Pdate</title>
<link rel="shortcut icon" href="${faviconIco }"/>
<link rel="stylesheet" href="${easyuiCss }"/>
<link rel="stylesheet" href="${iconCss }"/>
<link rel="stylesheet" href="${mainCss }"/>
<!--[if lt IE 9]>
		请使用IE9以上浏览器操作本系统
	<![endif]-->
<script src="${jqueryJs }" ></script>
<script src="${jqueryEasyuiJs }"></script>
<script src="${easyuiZHJs }"></script>
<script src="${jqueryExtensionJs }"></script>
<script src="${artTemplateJs }"></script>
<script src="${jsonJs }"></script>
<script src="${commonJs }"></script>
<script src="${basePathConfigJs }"></script>

