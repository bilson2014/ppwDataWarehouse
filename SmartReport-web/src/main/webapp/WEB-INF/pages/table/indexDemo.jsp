<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<%@ include file="/WEB-INF/pages/table/header.jsp" %>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/images/favicon.ico" >
</head>
<body>
	<div class="easyui-layout" fit="true" style="height: 250px;">
	
    <div region="center">
        <div id="content" class="easyui-tabs" border="false" fit="true">
            <div title="报表查询" data-options="iconCls:'icon-table'" style="overflow:auto;">
                <iframe scrolling="auto" frameborder="0" src="<%=request.getContextPath()%>/report/query/view"
                        style="width: 100%; height: 100%;"></iframe>
            </div>
        </div>
    </div>
    
</div>
</body>
</html>