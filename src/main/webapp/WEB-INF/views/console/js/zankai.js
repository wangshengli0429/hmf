function show(id) {
	var obj = document.getElementById(id);
	if (obj.style.display == "block") {
		obj.style.display = "none";
	} else {
		obj.style.display = "block";
	}
}

function setTab(name, cursel, n) {
	for (var i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

var myfn = new Object({
	AjaxFn : function(url, div) {
		$.ajax({
			url : url,
			success : function(data) {
				div.html(data);
			}
		});
	}
});

// 窗口关闭
function closeInfo() {
	$(".popup-box").hide();
	$(".popup_bg").hide();

}

$(function() {
	$(".search-icon").bind("mouseover",function(){
		$("input[name='paging.current']").val("1");
	});
	// 头部点击样式
	$(".main-nav-a").click(
			function() {
				try {
					if (!parent.frames["leftFrame"].menuFinishLoad) {
						return false;
					}
				}catch(e){

				}
				$(this).addClass("select-a").parents("li").siblings("li").find(
						".main-nav-a").removeClass("select-a");
				var title = $(this).attr("data-title");

				if ($(this).attr("data-title") == "index") {
					parent.frames["ContentFrame1"].cols = "0,*";
				} else {
					parent.frames["ContentFrame1"].cols = "230,*";
				}
				$(parent.frames["leftFrame"].document).find(
						".left-subnav-containe .click-link").removeClass(
						"select-a");
				if($(this).attr("data-title")!="user" && $(this).attr("data-title")!="app"){
					$(parent.frames["leftFrame"].document).find(".left-subnav-containe").find("ul").hide();
					$(parent.frames["leftFrame"].document).find(".arrow-down-icon").removeClass(
					"arrow-up-icon");
				}else{
					$(parent.frames["leftFrame"].document).find(".left-subnav-containe").find("ul").show();
				}
				$(parent.frames["leftFrame"].document).find(
						".left-subnav-containe .item-subnav-box[data-title='"
								+ title + "']").show().siblings().hide();

			});
	$(".update_pwd").click(function(){
		var title = $(this).attr("data-title");
		$(parent.frames["topFrame"].document).find("a").removeClass("select-a");
		$(parent.frames["topFrame"].document).find("a[data-title='"+title+"']").addClass("select-a");
	});

	$(".link_url").click(function(){
		var title = $(this).attr("data-title");
		var showObj = $(this).attr("showObj");
		parent.frames["ContentFrame1"].cols = "230,*";
		var obj = $(parent.frames["leftFrame"].document);
		obj.find(".left-subnav-containe .item-subnav-box[data-title='"+ title + "']").show().siblings().hide();
		obj.find(".left-subnav-containe").find("ul").hide();
		obj.find(".left-subnav-containe").find(".arrow-down-icon").removeClass("arrow-up-icon");
		obj.find(".left-subnav-containe a").removeClass("select-a");
		obj.find("a[id='"+showObj+"']").addClass("select-a");
		obj.find("a[id='"+showObj+"']").parent().parent().show();
		$(parent.frames["topFrame"].document).find("a").removeClass("select-a");
		$(parent.frames["topFrame"].document).find("a[data-title='"+title+"']").addClass("select-a");
	});
	// 左边菜单点击样式

	$(".left-container")
			.on(
					"click",
					".click-link",
					function() {
						$(this).parents("dl").find(".click-link").removeClass(
								"select-a");
						$(this).addClass("select-a");
						$(this).parents("dd").siblings("dd").find(
								".arrow-down-icon")
								.removeClass("arrow-up-icon");
						var ulbox = $(this).parents("dd").find("ul");
						if ($(this).find(".arrow-down-icon").hasClass(
								"arrow-up-icon")) {
							$(this).find(".arrow-down-icon").removeClass(
									"arrow-up-icon");
							ulbox.slideUp(200);
						} else {
							$(this).find(".arrow-down-icon").addClass(
									"arrow-up-icon");
							ulbox.slideDown(200);
							if ($(this).hasClass("item-a")) {
								var url = ulbox.find("li").eq(0).find("a")
										.attr("href");
								ulbox.find("a").each(function(){
									if(!$(this).hasClass("disabled")){
										if(!$(this).hasClass("select-a")){
											$(this).addClass("select-a");
											url = $(this).attr("href");
											return false;
										}
									}
								});
								parent.document.getElementById("mainFrame").src = url
							}

						}
						$(this).parents("dd").siblings("dd").find("ul")
								.slideUp(200);

					});

	// 左边隐藏，展开
	$(".left-hide-arrow").click(function() {
		var btn = $(this);
		var ff = parent.document.getElementById("ContentFrame1");
		if (btn.hasClass("left-container-hide")) {
			btn.removeClass("left-container-hide");

			ff.cols = "230,*";
		} else {
			btn.addClass("left-container-hide");
			ff.cols = "13,*";
		}

	});
	
	$(".main-nav-a").click(function() {
		 $(parent.frames["leftFrame"].document).find('.left-hide-arrow').removeClass("left-container-hide");
	});

	$("body").on(
			"click",
			".link-btn-click",
			function() {
				if ($(this).hasClass("select-a")) {
					return false;
				}
				$(this).addClass("select-a").append(
						"<i class='icon-i tab-arrowtop-icon'></i>");
				$(this).parents("li").siblings("li").find("a").removeClass(
						"select-a").find("i").remove();
			});


	// 页面加载
	$("body").on("click", ".click-link", function() {
		var boxstr = null, box;
		var urlstr = $(this).attr("data-url");
		boxstr = $(this).attr("data-container");
		if (boxstr !== "" && typeof (boxstr) !== "undefined") {
			box = $("." + boxstr);
		} else {
			box = $(".viewFramework-content");
		}
		if (typeof (urlstr) != "undefined") {
			if (urlstr == "null") {
				alert("此链接暂无页面，请查看其它类似页面!");
			} else {
				myfn.AjaxFn(urlstr, box);
			}
		}
	});

	// 弹出框按钮
	$("body").on("click", ".popup-link", function() {

		var urlstr = $(this).attr("data-url");
		if (typeof (urlstr) != "undefined") {
			if (urlstr != "null") {
				myfn.AjaxFn(urlstr, $(".popup-box-content"));
				$(".popup-box").show().css({
					"z-index" : 99999
				});
				$(".popup_bg").show();
			}
		}
		//设置弹框的宽度高度样式
		if (typeof ($(this).attr("data-wh")) != "undefined") {
			var wh = $(this).attr("data-wh").split("*");
			var w = wh[0];
			var h = wh[1];
			var windos_h=$(window).height();
			if(h>windos_h)
			{
				h=windos_h-40;
			}
			$(".popup-content-container-2").css({"max-height":h-50+"px"});
			$(".popup-box").css({
				"left" : 50 + "%",
				"top" : 50 + "%",
				"width" : w + "px",
				"margin-left" : -w / 2 + "px",
				"height" : h + "px",
				"margin-top" : -h / 2 + "px"
			});
		} else {
			$(".popup-content-container-2").css({
				"max-height" : $(window).height() - 300
			});

		}
	});

	// input[type=text]点击
	$("body").on("focus", "input[type='text']", function() {
		$(this).addClass("text-onchange");

	});

	$("body").on("blur", "input[type='text']", function() {
		$(this).removeClass("text-onchange");

	});

});

function mykeyDown(e) {
	var ev = e ? e : event;
	if (window.addEventListener) {
		if (ev.keyCode == 116) {
			// F5的键盘常用ASCII码为116
			parent.frames['mainFrame'].location.reload();
			ev.preventDefault();
			return false;
		}
	} else {
		if (ev.keyCode == 116) {
			ev.keyCode = 0;
			ev.returnValue = false;
			parent.frames('mainFrame').location.reload();
			return false;
		}
	}
} // 给每个frame都绑定onkeydown事件

window.onload = function() {
	var win = window;
	var i = 0;
	while (win.parent != win) {
		i++;
		win = win.parent;
	}
	if (i == 1) {
		document.onkeydown = mykeyDown;
		for (var i = 0; i < frames.length; i++) {
			if (typeof document.addEventListener != "undefined") {
				frames[i].document.addEventListener("keydown", mykeyDown, true);
			} else {
				frames[i].document.attachEvent("onkeydown", mykeyDown);
			}
		}
	}
};
