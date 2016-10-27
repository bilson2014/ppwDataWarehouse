var tree;

$().ready(function() {
	
	loadTreeNode();
	
});

function loadTreeNode(){
	
	var editing = undefined;
	tree = $('#listTree');
	
	// 初始化树
	$('#listTree').tree({
		checkbox : false,
		method : 'get',
		animate : true,
		dnd : true,
		url : designerPageRootUrl + 'listChildNodes',
		onLoadSuccess : function(node, data) {
			if (ReportDesigner.treeNodePathIds && ReportDesigner.treeNodePathIds.length > 0) {
				var id = ReportDesigner.treeNodePathIds.shift();
				ReportDesigner.selectAndExpandTreeNode(id);
			}
			TreeLi();
		},
		onClick : ReportDesigner.clickTreeNodeHandler,
		onContextMenu : function(e, node) {
			e.preventDefault();
			$('#reportTree').tree('select', node.target);
			var copyNodeId = $('#copyNodeId').val();
			$('#reportTreeCtxMenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
		
	});
	

}

var ReportDesigner = function() {
	
};

ReportDesigner.add = function() {
	var node = ReportDesigner.getSelectedTreeNode();
	if (node) {
		$('#reportAction').val('add');
		ReportDesigner.clearAllTab();
		ReportDesigner.selectTab(0);
		$('#reportId').val(0);
		$('#reportPid').val(node.id);
		ReportDesigner.initButtonStatus('add');
		ReportDesigner.clearAllEditor();
	} else {
		$.messager.alert('提示', "请选择一个节点", 'info');
	}
};

function TreeLi() {
	
//  var tapLi = $('.parentLi').find('ul').find('li');
//  tapLi.on('click', function() {
//  	$('.parentLi').off('click');
//      tapLi.removeClass('check');
//      $(this).addClass('check');
//      addStep($(this));
//  });
	
		 var tapLi = $('.childenNode');
		 tapLi.on('click', function() {
			 addStep($(this));
		});
	
}

function addStep(item) {
  
  var checkWord = item.text();
  var checkId = item.attr('id');
  var hasSame = true;
  var man = $('#step div');
   $('#step').find('div').removeClass('check');
  for (var i = 0; i < man.length; i++) {
      var checkSame = $(man[i]).text();
      if (checkWord == checkSame) {
          hasSame = true;
          $('#'+checkId+'').addClass('check');
          break;
      } else {
          hasSame = false;
      }

  }

  if (!hasSame) {
      var $body = '<div class="check" name="'+checkId+'">' + item.text() + '<img src="/resources/images/biao/transX.png"></img>';
      $body += '</div>'
      $('#step').append($body);
  }
  tap();
}
function tap() {

    var tap = $('#step').find('div');
    var tapClose = $('#step').find('div').find('img');

    tap.on('click', function() {
        $('#step').find('div').removeClass('check');
        $(this).addClass('check');
    });

    tapClose.on('click', function() {
        $(this).parent().remove();
    });
}


//加载报表

//$(function() {
//	// 注册 生成报表 按钮
//	$('#btnGenarate').click(ReportTemplate.generate);
//	// 注册 生成报表 按钮
//	$('#btnShowChart').click(ReportTemplate.showChart);
//	// 注册 统计列/计算列 全选按钮
//	$('#checkAllStatColumn').click(ReportTemplate.checkedAllStatColumn);
//	// 注册 导出excel 按钮
//	$('#btnExportToExcel').click(ReportTemplate.exportToExcel);
//	
//	ReportTemplate.generate();
//});

//function loadTreeFirstNode(){
//
//	loadData(function(msg){
//		if(msg.length > 0){
//			for(i=0;i<msg.length;i++){
//			createFirstNode(msg[i],i);
//			}
//			tap();
//			tree();
//			loadTreeSecondNode(msg[0].id);
//		}
//	},designerPageRootUrl + 'listChildNodes', $.toJSON({
//		      id:'1'
//	}))
//}
//
//function createFirstNode(item,num){
//	content = $('#addListNode');
//	var $body = "";
//	var changImg = "checkImg";
//	if(num==0){
//		$body+='<li class="parentLi activity" id="'+item.id+'">';
//		changImg="";
//	}else{
//		$body+='<li class="parentLi" id="'+item.id+'">';
//		
//	}
//	$body+='<div class="title" name="title">'+
//	'<img class="icon" src="/resources/images/biao/book.png">'+item.text+''+
//	'<img class="position changeP '+changImg+'" src="/resources/images//biao/tree.png">'+
//	'</div>'+
//	'<ul class="'+item.id+'">'+
//	'</ul>'+
//	'</li>';
//     content.append($body);
//}
//
//var time =0;
//function loadTreeSecondNode(id){
//	if(time ==0){
//	loadNode(id);
//	time++;
//	}
//   $('.parentLi').on('click',function(){
//	   var nodeId = $(this).attr('id');
//	   loadNode(nodeId);
//   });	
//
//}
//
//function loadNode(nodeId){
//	   
//	   
//	    $('.'+nodeId+'').find('li').remove();
//		loadData(function(msg){
//			if(msg.length > 0){
//				for(i=0;i<msg.length;i++){
//				createSecondNode(msg[i],nodeId,i);
//				}
//				$('#'+nodeId+'').slideDown();
//				TreeLi();
//			}
//		},designerPageRootUrl + 'listChildNodes', $.toJSON({
//			      id:nodeId
//		}))
//}
//
//function createSecondNode(item){
//	content = $('#addListNode');
//    var $body ='<li class="parentLi activity" id="'+item.id+'">'+
//	'<div class="title" name="title">'+
//	'<img class="icon" src="/resources/images/biao/book.png">'+item.text+''+
//	'<img class="position changeP" src="/resources/images//biao/tree.png">'+
//	'</div>'+
//	'<ul>'+
//	'</ul>'+
//	'</li>';
//     content.append($body);
//}
//
//function createSecondNode(item,id,num){
//	var content = $('.'+id+'');
//	var $body="";
//	if(num == 0){
//	$body ='<li class="check" id="'+num+'"> <img class="positions" src="/resources/images/biao/greenT.png">'+item.text+'</li>';
//	}else{
//	$body ='<li id="'+num+'"> <img class="positions" src="/resources/images/biao/greenT.png">'+item.text+'</li>';
//	}
//    content.append($body);
//}
////顶栏控制
//
//
//

//
////侧栏控制
//function tree() {
//
//    var openTree = $("[name^=title]");
//    openTree.on('click', function() {
//        
//        if ($(this).parent().hasClass('activity')) {
//            $(this).parent().removeClass('activity');
//            $(this).parent().find('.changeP').addClass('checkImg');
//            $(this).parent().find('ul').slideUp();
//        } else {
//            $(this).parent().addClass('activity');
//            $(this).parent().find('.changeP').removeClass('checkImg');
//            $(this).parent().find('ul').slideDown();
//        }
//
//    });
//
//}
//
































