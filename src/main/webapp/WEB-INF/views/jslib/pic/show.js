var req = getRequest();
window.onload = function() {
	var arr = req.split('-');
	var id = arr[0];
	var current = arr[1];// 当前图片
	var count = 0;// 图片个数
	var resData = null;
	$.ajax({
		type : "post",
		url : "/pingdian/rest/otherService/showComposition",
		data : '{"BM":{"id":"'+req+'"}}',
		success : function(str) {
			if (str.EC != 0) {
				alert('请求错误！');
				return;
			}
			count = str.BM.list.count;
			resData = str.BM.list.detailList;
			if (str.BM.list.image != null && str.BM.list.image != '') {
				$("#image").attr('src', str.BM.list.image);// 图片
			}
			if (str.BM.list.detailList != null && str.BM.list.detailList.length > 0) {
				for (i = 0; i < str.BM.list.detailList.length; i++) {
					var map = str.BM.list.detailList[i];
					var t1 = '<area id="myarea' + i
							+ '" shape="rect" coords="';
					var t2 = map.x1 + ',' + map.y1 + ',' + map.x2 + ','	+ map.y2;
					var t3 = '" href="javascript:void(0)">';
					$("#mymap").append(t1 + t2 + t3);
				}
			}

		},
		error : function() {
			alert("请求失败！");
		}
	});

	/*
	 * //图片热区点击事件 $("area").on("tap",function(){ alert(1); var v =
	 * $(this).attr("id").substr(6); var id = '#myshow' + v;
	 * $('#myshow').click(); });
	 */
	// 图片热区点击事件
	$("map").delegate("area", "tap", function() {
		var v = $(this).attr("id").substr(6);
		$("#message").text(resData[v].content);
		$('#myshow').click();
	});

	// 链接作文原文页面
	$("#composition").on("tap", function() {
		$(location).attr('href', 'share.html?id=' + id);
	});
	$("#nextImg").on("tap", function() {
		if (current < count) {
			var next = parseInt(current) + 1;
			$(location).attr('href', 'pic.html?id=' + id + '-' + next);
		} else {
			$(location).attr('href', 'pic.html?id=' + id + '-' + 1);
		}
	});
}
// 获取传入id
function getRequest() {
	var url = location.search; // 获取url中"?"符后的字串
	var arr = url.split("=");
	if (url.indexOf("?") != -1) {
		return url.substring(4, url.length);
	}
}
