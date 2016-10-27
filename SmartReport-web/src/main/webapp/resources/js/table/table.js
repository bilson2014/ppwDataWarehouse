var StepTool;
var stepListJson;
var isShow = false;
var checkHidden = false;
var currentIndex;
var hasClick = false;
var nowImg = 2;
var countCheck = 0;
var isHistory = false;
var changeColor = "#081d33";

$().ready(function() {



    


//    test();
//    testOpen();
//    tree();
//    noCheck();
//    tap();
//    testTreeLi();
//    doubleFormOptionColor();
//    doubleOptionCardInfoColor();
//    doubleManagerFromOptionColor();
//    openList();
//    openTitleList();
//    //checkCheckEd();
//    checkBoxChangeWord();
//    scroll();
	
	controlTree();
	openTitleList();
});

//一级菜单
function openTitleList(){

   var optinList = $('.optionList');
   $('#optionFind').on('click',function(){
      if($('#hideTitleList').hasClass('close')){
      	   $('#hideTitleList').slideDown();
      	   $('#hideTitleList').removeClass('close');
      	   $('#optionFind').removeClass('optionG');
      	   $('#optionFind').addClass('option');
      	   $('#listTree').slideUp();
      }else{
            $('#hideTitleList').slideUp();
      	    $('#hideTitleList').addClass('close');
      	    $('#optionFind').removeClass('option');
      	    $('#optionFind').addClass('optionG');
      	    optinList.removeClass('optionG');
            optinList.addClass('option');
            $('#listTree').slideDown();
      }
   });


   optinList.on('click',function(){
	        $('#hideTitleList').slideUp();
      	    $('#hideTitleList').addClass('close');
      	    $('#optionFind').removeClass('option');
      	    $('#optionFind').addClass('optionG');
      	    optinList.removeClass('optionG');
            optinList.addClass('option');
            $('#listTree').slideDown();
	    	var thisText = $(this).text().trim();
	    	$('#setTitle').text(thisText);

         switch(thisText){
              case '报表查询':
                  $('#optionMake').text('报表设计');
                  $('#optionManager').text('数据源管理');
              break;
              case '报表设计':
                   $('#optionMake').text('报表查询');
                  $('#optionManager').text('数据源管理');
               break;
             case '数据源管理':
                  $('#optionMake').text('报表查询');
                  $('#optionManager').text('报表设计');
              break;
         }

    });

}


function controlTree(){
	$('#openList').on('click', function() {

        if ($('#showList').hasClass('open')) {
            $('#showList').slideDown();
            $('#showList').removeClass('open');
        } else {
            $('#showList').slideUp();
            $('#showList').addClass('open');
        }

    });

    $('#openSet').on('click', function() {

        if ($('#setOpen').hasClass('open')) {
            $('#setOpen').show();
            $('#setOpen').removeClass('open');
        } else {
            $('#setOpen').hide();
            $('#setOpen').addClass('open');
        }
    });
}



function scroll(){

	 var scrollValue = 0;
     var scrollLeftValue = 0;
	$('#fisrtScrollFlow').scroll(function(){
       scrollValue = $('#fisrtScrollFlow').scrollTop();
       $('#firstFlow').scrollTop(scrollValue);
    });

     $('#fisrtScrollFlow').scroll(function(){
       scrollValue = $('#fisrtScrollFlow').scrollLeft();
       $('#fisrtLeftScrollFlow').scrollLeft(scrollValue);
    });

     var scrollSecondValue = 0;
     var scrollSecondLeftValue = 0;
     	$('#secondScrollFlow').scroll(function(){
       scrollValue = $('#secondScrollFlow').scrollTop();
       $('#secondFlow').scrollTop(scrollValue);
    });

}


function checkCheckEd() {

    $('#checkboxFourInput').on('click', function() {
        if ($('#checkboxFourInput').is(':checked')) {
            alert('yes');
        } else {
            alert('no');
        }
    });

}


function test() {

    $('#111').on('click', function() {
        $('#first').removeClass('hide');
        $('#second').addClass('hide');
        $('#third').addClass('hide');
    });

    $('#222').on('click', function() {
        $('#first').addClass('hide');
        $('#second').removeClass('hide');
        $('#third').addClass('hide');
    });

    $('#333').on('click', function() {
        $('#first').addClass('hide');
        $('#second').addClass('hide');
        $('#third').removeClass('hide');
    });
}

function testOpen() {

    $('#control1').on('click', function() {
        $('#open1').show();
    });
    $('#control2').on('click', function() {
        $('#open2').show();
    });
    $('#control3').on('click', function() {
        $('#open3').show();
    });

    $('#close1').on('click', function() {
        $('#open1').hide();
    });
    $('#close2').on('click', function() {
        $('#open2').hide();
    });
    $('#close3').on('click', function() {
        $('#open3').hide();
    });


}

function tree() {

    var openTree = $("[name^=title]");
    openTree.on('click', function() {

        if ($(this).parent().hasClass('activity')) {
            $(this).parent().removeClass('activity');
            $(this).parent().find('.changeP').addClass('checkImg');
            $(this).parent().find('ul').slideUp();
        } else {
            $(this).parent().addClass('activity');
            $(this).parent().find('.changeP').removeClass('checkImg');
            $(this).parent().find('ul').slideDown();
        }

    });

}

function checkBoxChangeWord(){

    $('#firstWord').on('click',function(){
      if($('#firstWord').text()=="横向展示"){
      	$('#firstWord').text('纵向展示');
      }else{
      	$('#firstWord').text('横向展示');
      }
  });

    $('#secondWord').on('click',function(){
      if($('#secondWord').text()=="横向展示"){
      	$('#secondWord').text('纵向展示');
      }else{
      	$('#secondWord').text('横向展示');
      }
  });


}

function noCheck() {
    var openCheck = $('.noCheck');

    openCheck.on('click', function() {

        if ($(this).hasClass('check')) {
            $(this).removeClass('check');
        } else {
        	if($(this).text()!='统计列'){
            $(this).addClass('check');
          }
        }

    });

    $('#checkAll').on('click', function() {

    	if( $('.noCheck').hasClass('check')){
             $('.noCheck').removeClass('check');
    	}
        else{
        	 $('.noCheck').addClass('check');
        }
       
    });


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


function testTreeLi() {

    var tapLi = $('.parentLi').find('ul').find('li');

    tapLi.on('click', function() {
        tapLi.removeClass('check');
        $(this).addClass('check');
        addStep($(this));
    });

}

function addStep(item) {
    
    var checkWord = item.text();
    var checkId = item.text().trim();
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
        var $body = '<div class="check" id="'+checkId+'">' + item.text() + '<img src="../images/biao/transX.png"></img>';
        $body += '</div>'
        $('#step').append($body);
    }
    tap();
}

function doubleFormOptionColor(){

  var col = $('.FormOption');

    for (var i = 0; i < col.length; i++) {
         
         if(i%2){
             $(col[i]).css('background',changeColor);
         }

    }

}

function doubleOptionCardInfoColor(){

  var col = $('.optionCardInfo');
    for (var i = 0; i < col.length; i++) { 
         if(i%2){
         	 var colDiv = $(col[i]).find('div');
              for (var j = 0; j < colDiv.length;j++){
              	 $(colDiv[j]).css('background',changeColor);
              	 // if(j!=0){
              	 // 	 $(colDiv[j]).css('background',changeColor);
              	 // }
              }
         }
    }
}

function doubleManagerFromOptionColor(){
var col = $('.managerFromOption');

    for (var i = 0; i < col.length; i++) { 
         if(i%2){
             $(col[i]).css('background',changeColor);
         }
    }
}




function openList(){

    $('#openList').on('click',function(){

           if($('.subList').hasClass('show')){
           	 $('.subList').hide();
           	 $('.subList').removeClass('show');
           	 $('.contentList').removeClass('width');
           	 $('.contentList').addClass('changeWidth');
           }
           else{
           	 $('.subList').show();
           	 $('.subList').addClass('show');
           	 $('.contentList').addClass('width');
           	 $('.contentList').removeClass('changeWidth');
           }
    });

     $('#flowDown').on('click',function(){

           if($('.contentTable').hasClass('contentTableHeight')){
           	 $('.contentTable').removeClass('contentTableHeight');
             $('.contentTable').removeClass('changeHeight');
             $('.contentTable').addClass('changeHeightClose');
             $('#flowDown').removeClass('checkImgUpDown');
           }
           else{
              $('.contentTable').addClass('contentTableHeight');
              $('.contentTable').addClass('changeHeight');
              $('.contentTable').removeClass('changeHeightClose');
              $('#flowDown').addClass('checkImgUpDown');
           }
    });

}



