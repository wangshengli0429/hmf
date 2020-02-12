<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑图片</title>
<script src="<%=request.getContextPath()%>/editPicture/jslib/jquery-1.10.2.min.js"></script>
<link href="<%=request.getContextPath()%>/editPicture/jslib/font-awesome.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/editPicture/jslib/jQuery.canvas.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/editPicture/jslib/jQuery.canvas.js"></script>
<link href="<%=request.getContextPath()%>/editPicture/jslib/bootstrap.min.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/editPicture/jslib/bootstrap.min.js"></script>
</head>
<body>
<!--  center   案例展示网址：http://xdq-test.vip/jcanvas/            http://xdq-test.vip/jcanvas/Demos/simple.html-->
<div class="canvas-container" style="left:20%;width:60%;">
	<button class="btn btn-info" onclick="preservation()" style="background:#54a7fd;" type="button">保存</button>
	<img class="myCanvas" id="img1" src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg">
	<!-- <canvas class="my-canvas" style="width: 512px; height: 384px;" width="512" height="384"></canvas> -->
	
	<!-- 
	<div class="canvas-tools">
	<span title="画笔工具" class="fa fa-pencil pencil actived"></span>
	<span title="文本工具" class="fa fa-font text"></span>
	<span title="颜色" class="fa fa-spinner colors" style="color: rgb(255, 0, 0);">
	</span><span title="画笔粗细" class="fa fa-circle width"></span>
	<span title="橡皮檫" class="fa fa-eraser eraser"></span>
	<span title="撤销" class="fa fa-undo undo"></span>
	<span title="重做" class="fa fa-repeat redo"></span>
	<span title="清除" class="fa fa-remove clear"></span>
	</div>
	-->
</div>
</body>
<script>
	function preservation(){
		alert("保存");
	}
	
</script>
</html>