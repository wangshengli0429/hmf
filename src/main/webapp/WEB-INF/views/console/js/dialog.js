$(function(){	
	$("div.dialog_close").click(function(){
		$("#info").html("");
		$("div.popup_bg").hide();
		$("div.dialog").hide();
	});
	
	$("#cancel").click(function(){
		$("div.dialog").hide();
		$("div.popup_bg").hide();
	});
});
// 封装信息消息文本.
	function showDialogInfo(msg,type){
		return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
				'<h3 class="pl20 f18">提示</h3>'+
			    '<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
			     ' <div class="popup-content-container pb20 clearfix">'+
			      '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
			     ' <div class="tc f16"> <a href="javascript:void(0);" onclick="closeInfo()" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a></div> '+
			'</div></div>';
	}

	function showDialogInfoForMethod(msg,type,clickMethod){
		return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
			'<h3 class="pl20 f18">提示</h3>'+
			'<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
			' <div class="popup-content-container pb20 clearfix">'+
			'<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
			' <div class="tc f16"> <a href="javascript:void(0);" onclick="'+clickMethod+'" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a></div> '+
			'</div></div>';
	}

	function showForwardInfo(msg,type,url){
		return '<div class="popup-box" style="width:470px;min-height:200px;"><div class="popup-title-container">'+
		'<h3 class="pl20 f18">提示</h3>'+
		'<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
	    '<div class="popup-content-container pb20 clearfix">'+
	     ' <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
	     ' <div class="tc f16"><a href="'+url+'" class="btn-blue2 btn white radius-6 pl20 pr20">确 定</a><a onclick="closeInfo()" class="btn btn-blue2 radius-6 pl20 pr20 ml40">取 消</a></div> '+
	   ' </div>'+
	'</div>';
	}
	
	function showSuccInfo(msg,type,url){
		var isShowClose = true;
		if(arguments.length==4){
			isShowClose = arguments[3];
		}
		var divHtml = "";
		divHtml +='<div class="popup-box" style="width:470px;min-height:200px;"><div class="popup-title-container">';
		divHtml +='<h3 class="pl20 f18">提示</h3>';
		if(isShowClose == false){
			divHtml +='</div>';
		}else{
			divHtml +='<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>';
		}
		divHtml +='<div class="popup-content-container pb20 clearfix">';
		divHtml +=' <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>';
		divHtml +=' <div class="tc f16"><a href="'+url+'" class="btn-blue2 btn white radius-6 pl20 pr20">确 定</a></div> ';
		divHtml +=' </div>';
		divHtml +='</div>';
	    return divHtml;
	}
	
	function showInfoToMenu(msg,type,url,showObj,dataTitle){
		return '<div class="popup-box" style="width:470px;min-height:200px;"><div class="popup-title-container">'+
		'<h3 class="pl20 f18">提示</h3>'+
		'<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
	    '<div class="popup-content-container pb20 clearfix">'+
	     ' <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
	     ' <div class="tc f16"><a href="'+url+'" showObj="'+showObj+'" data-title="'+dataTitle+'" class="btn-blue2 btn white radius-6 pl20 pr20 link_url">确 定</a></div> '+
	   ' </div>'+
	'</div>';
	}

	function showInfo(msg,type){
		return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
		'<h3 class="pl20 f18">提示</h3>'+
	    '<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
	     ' <div class="popup-content-container pb20 clearfix">'+
	      '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
	     ' <div class="tc f16"> <a href="javascript:void(0);" onclick="closeInfo()" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a></div> '+
	'</div></div>';
	}

	function showConfirmDiv(msg,param,type){
		return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
		'<h3 class="pl20 f18">提示</h3>'+
	    '<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
	     ' <div class="popup-content-container pb20 clearfix">'+
	      '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
	     ' <div class="tc f16"> <a href="javascript:void(0);" onclick="toConfirm(\''+param+'\',\''+type+'\');" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a><a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> </div> '+
	'</div></div>';
	}
	
	function closeDiv(){
		$("#info").html("");
		$("div.popup_bg").hide();
		var list=parent.art.dialog.list;for(var i in list)list[i].close();
	}
	
	
	//对账管理使用 lingyuanjie 20160514
	// 封装信息消息文本
	function showDialogInfoReload(msg,type){
		return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
				'<h3 class="pl20 f18">提示</h3>'+
			    '<a class="icon-i popup-close2" onclick="closeInfoReload()"></a></div>'+
			     ' <div class="popup-content-container pb20 clearfix">'+
			      '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
			     ' <div class="tc f16"> <a href="javascript:void(0);" onclick="closeInfoReload()" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a></div> '+
			'</div></div>';
	}
	//关闭弹层，刷新当前页
	function closeInfoReload(){
		$("#info").html("");
		$("div.popup_bg").hide();
		window.location.reload(); //不同处 -> 关闭弹窗后刷新当前页
	}