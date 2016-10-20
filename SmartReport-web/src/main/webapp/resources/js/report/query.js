$(function () {

    ReportQuery.init();

    $('#west').panel({
        tools: [ {
            iconCls: 'icon-reload',
            handler: ReportQuery.reloadTree
        }]
    });

 // 初始化树
	$('#reportTree').tree({
		checkbox : false,
		method : 'get',
		animate : true,
		dnd : true,
		url : designerPageRootUrl + 'listChildNodes',
		onLoadSuccess : function(node, data) {
			if (ReportQuery.treeNodePathIds && ReportQuery.treeNodePathIds.length > 0) {
				var id = ReportDesigner.treeNodePathIds.shift();
				ReportQuery.selectAndExpandTreeNode(id);
			}
		},
		onClick: function (node) {
            if (node.attributes.flag == 1) {
                ReportQuery.previewInNewTab();
            }
        }
	});

    $('#tabs').tabs({
        onSelect: ReportQuery.tabSelectedHandlder
    });

});

var ReportQuery = function () {
};


ReportQuery.treeNodePathIds = [];

ReportQuery.init = function () {
};

//
// tabs相关操作
//
ReportQuery.getSelectedTabIndex = function () {
    var tab = $('#tabs').tabs('getSelected');
    return $('#tabs').tabs('getTabIndex', tab);
};

ReportQuery.selectTab = function (index) {
    $('#tabs').tabs('select', index);
};

//
// tree相关操作
//

ReportQuery.clickTreeNodeHandler = function (currNode) {
    if (node.attributes.flag == 1) {
        ReportQuery.previewInNewTab();
    }
};

ReportQuery.selectTreeNodeHandler = function (node) {
    $('#reportTree').tree('options').url = designerPageRootUrl + 'listChildNodes';
    $('#reportTree').tree('expand', node.target);

    ReportQuery.clearAllTab();
    if (node.attributes.flag != 1) {
        $('#reportAction').val('add');
        $('#reportPid').val(node.id);
        ReportQuery.initButtonStatus('add');
    } else {
        ReportQuery.loadAllTabData();
    }
};


ReportQuery.selectTreeNode = function (id) {
    var node = $('#reportTree').tree('find', id);
    if (node) {
        $('#reportTree').tree('select', node.target);
    }
};

ReportQuery.getTreeNodeById = function (id) {
    return $('#reportTree').tree('find', id);
};

ReportQuery.selectAndExpandTreeNode = function (id) {
    var node = $('#reportTree').tree('find', id);
    if (node) {
        $('#reportTree').tree('select', node.target);
        ReportQuery.selectTreeNodeHandler(node);
        var parentNode = $('#reportTree').tree('getParent', node.target);
        if (parentNode) {
            $('#reportTree').tree('expand', parentNode.target);
        }
    }
};

ReportQuery.getSelectedTreeNode = function () {
    return $('#reportTree').tree('getSelected');
};

ReportQuery.reloadTree = function () {
    $('#reportTree').tree('reload');
};


ReportQuery.previewInNewTab = function () {
    var node = ReportQuery.getSelectedTreeNode();
    if (node.attributes.flag == 1) {
        var title = node.attributes.name;
        if ($('#tabs').tabs('exists', title)) {
            $('#tabs').tabs('select', title);
            var tab = $('#tabs').tabs('getSelected');
            return tab.panel('refresh');
        }
        var previewUrl = queryPageRootUrl + "uid/" + node.attributes.uid;
        $('#tabs').tabs('add', {
            id: node.id,
            title: title,
            content: '<iframe scrolling="yes" frameborder="0" src="' + previewUrl + '" style="width:100%;height:100%;"></iframe>',
            closable: true
        });
    }
};

// 在tab里显示报表图表页
function showChart(title, id, uid) {
    if ($('#tabs').tabs('exists', title)) {
        $('#tabs').tabs('select', title);
        var tab = $('#tabs').tabs('getSelected');
        return tab.panel('refresh');
    }
    var url = chartPageRootUrl + uid;
    $('#tabs').tabs('add', {
        id: id,
        title: title,
        content: '<iframe scrolling="yes" frameborder="0" src="' + url + '" style="width:100%;height:100%;"></iframe>',
        closable: true
    });
}
