<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://wwl.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑图片</title>
<jsp:include page="/inc.jsp"></jsp:include>
<link href="<%=request.getContextPath()%>/editPicture/jslib/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/editPicture/jslib/jQuery.canvas.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/editPicture/jslib/font-awesome.min.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/editPicture/jslib/jquery-1.10.2.min.js"></script>
<script src="<%=request.getContextPath()%>/editPicture/jslib/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/editPicture/jslib/jQuery.canvas.js"></script>
</head>
<body style="font-family:Microsoft YaHei;">
<div id="fade" style="display: none; position: absolute; top: 0%; left: 0%; width: 100%; height: 100%; background-color: black; z-index:1001; -moz-opacity: 0.6; opacity:.60; filter: alpha(opacity=60);"> 
	<div style="position: absolute; top: 20%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1003; overflow: auto;">
        <div style="display:inline;" onclick="picClose()">
        	<img src="<%=request.getContextPath()%>/images/guanbi-.png" style="width:50%;height:50%;"/>
        </div>
        
	</div>
</div>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

	<input type="hidden" value="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg">
	<input type="hidden" id="img2" value="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg">
	<!-- // 查看图片 // -->
   	<a href='javascript:void(0);' onclick='picBig1()'><img name="imga" src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg" style="width:100px;height:100px;"></a>
   	<a href='javascript:void(0);' onclick='picBig2()'><img name="imga" src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg" style="width:100px;height:100px;"></a>
	<!-- // 查看图片 // left:200px;-->
	<div id="divCenter1" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <img id="img1" class="myCanvas" src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg">
    	<div style="position: absolute; top:2%; left: 94%; padding: 0px; border: 0px solid orange; z-index:1003; overflow: auto;" onclick="preservation()">
        	<button type="button" class="btn btn-danger" style="background:#ff0000;" data-toggle="button">保存</button>
        </div>
    </div>
    <div id="divCenter2" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <img class="myCanvas" src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg">
    </div>
	<!-- // 查看图片 // -->
	
</div>
</div>
</div>
</body>

	<!-- // 查看图片 // -->
	<script>
    	/* $("[name='imga']").each(function(){
    		$(this).click(function(){
    			var a = $(this).attr("src");
    			alert(a);
    			
    		})
    	}) */
		function picBig1() {
	    	picClose();
	    	document.getElementById('fade').style.display='block';
	        document.getElementById("divCenter1").style.display="block";
	    }
		function picBig2() {
	    	picClose();
	    	document.getElementById('fade').style.display='block';
	        document.getElementById("divCenter2").style.display="block";
	    }
	    /* 点击大图关闭 */
	    function picClose() {
	        document.getElementById("divCenter1").style.display="none";
	        document.getElementById("divCenter2").style.display="none";
	        document.getElementById('fade').style.display='none';
	    }
	    /* 保存 */
	    function preservation (){
	    	alert("保存");
	    	//var canvas = document.getElementById("divCenter1").getElementsByTagName("img");
	    	var canvas = document.getElementById("img1");
	    	var ctx = canvas.getContext('2d');
	    	canvas.toDataURL("image/png");
	    	alert(canvas);
	    }
	</script>

</html>