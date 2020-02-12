//保存批改图片
function file_update(){
	var can = document.getElementById("img1");
	var dataurl = can.toDataURL('image/png'); //base64图片数据
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
	bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	while(n--){
		u8arr[n] = bstr.charCodeAt(n);
	}
	var obj = new Blob([u8arr], {type:mime});
	var fd = new FormData();
	fd.append("upfile", obj,"image.png");
	$.ajax({
	    url: "http://localhost:8080/pingdian/rest/uploadService/test",
	    type: "POST",
	    processData: false,
	    contentType: false,
	    data: fd,
	    success: function (data) {
	    	//console.log(data);
	    	alert("保存成功！");
	    	window.close();
	    },
	    error: function() {  
            alert("加载失败");  
        }
	});
}

var img_width;
var img_height;
//通过图片路径打开图片
function open_img(){
	//var route = location.search.substring(30);//图片路径
	/*var imgUrl = location.search.replace('?http://123.56.186.12:8101', '');//图片路径
	var route = "https://api.pingdianedu.com:443/download";
	var img = new Image();
	img.src = location.search.replace('?', '')
	img_width = img.width;//获取图片宽度
	img_height = img.height;//获取图片高度
	location.hash = "load:" + encodeURIComponent(route + imgUrl);
	*/
	var route = location.search.replace('?', '');//图片路径
	var can = document.getElementById("img2");
	can.src = route;
	var img = new Image();
	img.src = route;
	var w = img.width;//获取图片宽度
	var h = img.height;//获取图片高度
	if(w>700){
		img_width = 700;
		var g = (700/w)*h;
		img_height = Math.round(g);//四舍五入
	}else {
		img_width = w;
		img_height = h;
	}
	can.style.width = img_width + "px";
	can.style.height = img_height + "px";
	document.getElementById("section3").style.height = (img_height-50) + "px";
}
//打开图片
window.onload = function(){
	open_img();
}


//画笔
function huabi() {
    Tools.active.call(this);
    var $t = $(this),
        model = $t.parentsUntil(".canvas-container")
            .last().parent().find(".my-canvas")
            .data("model");
    model._currentTool = "pen";
    function onMouseDown(e) {
        Tools.showEvent.apply(this, arguments);
        var $t = $(this), model = $t.data("model");
        if (model._currentTool == "pen") {
            model._isMouseDownInCanvas = true;
            var cvs = model.getCanvas();
            model._pencilLoc = { x: cvs.canvas.offsetLeft, y: cvs.canvas.offsetTop };
            var pos = Tools.getLocation(model, e, model._pencilLoc);
            cvs.beginPath();
            cvs.moveTo(pos.x, pos.y);
            cvs.strokeStyle = model._currentColor;
            cvs.shadowColor = model._currentColor;
            cvs.lineWidth = model._currentPenWidth;

            e.preventDefault();
            return false;
        }
    }
    function onMouseMove(e) {
        Tools.showEvent.apply(this, arguments);
        var $t = $(this), model = $t.data("model"), canvas = model.$canvas[0];
        var cvs = model.getCanvas();
        if (model._currentTool == "pen" && model._isMouseDownInCanvas) {
            var loc = Tools.getLocation(model, e, model._pencilLoc);
            cvs.lineTo(loc.x, loc.y);
            cvs.stroke();
            //console.log("pencil line {0}".format(JSON.stringify(loc)));
        }
    }
    function onMouseUp() {
        Tools.showEvent.apply(this, arguments);
        var $t = $(this), model = $t.data("model");
        if (model._currentTool == "pen" && model._isMouseDownInCanvas) {
            var cvs = model.getCanvas();
            cvs.closePath();
            Tools.saveHistroy(model, 1);
            //console.log("pencil up");
        }
        model._isMouseDownInCanvas = false;
    }
    model.$canvas
        .unbind("mousedown")
        .unbind("mouseup")
        .unbind("mousemove")
        .unbind("touchstart")
        .unbind("touchend")
        .unbind("touchmove")
        .bind("mousedown", onMouseDown)
        .bind("mouseup", onMouseUp)
        .bind("mousemove", onMouseMove)
        .bind("touchstart", onMouseDown)
        .bind("touchend", onMouseUp)
        .bind("touchmove", onMouseMove)
    ;

    e.preventDefault();
    return false;
}



//撤销
function chexiao(e) {
    var $t = $(this);
    model = $t.parentsUntil(".canvas-container")
            .last().parent().find(".my-canvas")
            .data("model");
    var $canvas = $(".my-canvas");
    var canvas = model.$canvas[0];
    var cvs = model.getCanvas();
    if (model._canvasHistoryPoint > 0)
        model._canvasHistoryPoint--;
    var img = model._canvasHistory[model._canvasHistoryPoint];
    if (img)
        cvs.putImageData(img, 0, 0);
    e.preventDefault();
    return false;
}

//批注
function add_comment(){
	comment = prompt("请输入您的点评:","");
	if (comment != null){
		var le = comment.length;
		if(le==0){
			alert("您输入的点评内容为空!");
		}else{
			//alert("您输入的点评内容是：" + comment);
			globalArr[globalArr.length-1].commentConcent = comment;
			var str = JSON.stringify(globalArr);
			alert("坐标信息集合-globalArr："+str);
			
			var ctx = document.getElementById("mycanvas").getContext("2d");

			ctx.font = "10px PingFang-SC-Bold";
			var gradient=ctx.createLinearGradient(0,0,c.width,0);
			ctx.fillStyle=gradient;
			ctx.fillText("此处有批注",655,370);
			
		}
	}else{
		//alert("你按了[取消]按钮");
	}
}
