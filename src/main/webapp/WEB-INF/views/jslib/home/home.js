var urlload = null;
window.onload=function(){
	listFilter();
	// 查看详情
	$("ul").delegate("li", "tap", function() {
		var id = $(this).attr("id");
		var condition = $("#condition").val();
		var url = null;
		if (condition != null && condition != '') {
			url = 'share.html?id=' + id + '&condition=' + condition;
		}else {
			url = 'share.html?id=' + id;
		}
		$(location).attr('href', encodeURI(url));
	});
	
	//下载链接
	$("#appDownLoad").on("tap", function() {
		$(location).attr('href', urlload);
	});
	
	$("#condition").bind("keyup", listFilter);
}

function listFilter() {
	var condition = $("#condition").val();
	$.ajax({  
        type: "post",  
        url: "/pingdian/rest/otherService/compositionHome",
        data: '{"BM":{"condition":"'+condition+'"}}',
        success: function(str) {
        	if (str.EC != 0) {
				alert('请求错误！');
				return;
			}
        	$("ul").empty();
        	urlload = str.BM.map.url;
        	var xiaoxue = str.BM.map.xiaoxue;
        	var chuzhong = str.BM.map.chuzhong;
        	var gaozhong = str.BM.map.gaozhong;
        	for (i = 0; i < xiaoxue.length; i++) {
				var v = '<li id="'+xiaoxue[i].id+'" class="myli">'+xiaoxue[i].title+'</li>';
				$("#primary").append(v);
			}
        	for (i = 0; i < chuzhong.length; i++) {
        		var v = '<li id="'+chuzhong[i].id+'" class="myli">'+chuzhong[i].title+'</li>';
        		$("#middle").append(v);
        	}
        	for (i = 0; i < gaozhong.length; i++) {
        		var v = '<li id="'+gaozhong[i].id+'" class="myli">'+gaozhong[i].title+'</li>';
        		$("#high").append(v);
        	}
        	//在使用'ul'标签时才使用，作用:刷新列表,重新加载所在标签的样式
        	$("ul").listview("refresh");  
        	$("ul").trigger("create");  
        	
        },
        error: function (){
		}
    });
	
}
