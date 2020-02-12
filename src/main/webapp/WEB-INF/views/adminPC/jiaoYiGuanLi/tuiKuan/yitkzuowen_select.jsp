<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退款-已处理-查看作文详情</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div id="fade" style="display: none;"> 
	<div style="position: absolute; top: 0%; left: 0%; width: 100%; height: 100%; background-color: black; z-index:1001; -moz-opacity: 0.6; opacity:.60; filter: alpha(opacity=60);"></div>
	<div style="position: absolute; top: 20%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1004; overflow: auto;">
        <div style="display:inline;" onclick="picClose1()">
        	<img title="关闭" src="<%=request.getContextPath()%>/images/guanbi-.png" style="width:100%;height:100%;"/>
        </div>
	</div>
	<div style="position: absolute; top: 28%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1004; overflow: auto;">
        <img id="xuanzhuan" title="旋转" src="<%=request.getContextPath()%>/images/xuanzhuan.png" style="width:100%;height:100%;"/>
	</div>
</div>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${tkzw}" var="t">
<div style="display:inline;">
<span>退款/售后>已处理>${t.title}（${t.draft}）</span>
</div>
<div style="display:inline;float:right;">
<%-- 
<a href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=6" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
 --%>
<a href="javascript:history.go(-1)" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>
<c:if test="${not empty t.propo}">
要求：${t.propo}
</c:if>
<center>
	<h3><b>${t.title}</b></h3>
	<p style="color:#b2b2b2">
		作者昵称：${t.author}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		${t.draft}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		${t.geade}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		${t.time}
	</p>
</center>
	${t.content}
	<br><br>
	
	<input type="hidden" id="img1" value="${t.img1}">
	<input type="hidden" id="img2" value="${t.img2}">
	<input type="hidden" id="img3" value="${t.img3}">
	<!-- // 查看图片 // -->
	<div style="display:inline">
		<img class="pimg" id="imga" src="${t.img1}" style="width:100px;height:100px;">
		<img class="pimg" id="imgb" src="${t.img2}" style="width:100px;height:100px;">
		<img class="pimg" id="imgc" src="${t.img3}" style="width:100px;height:100px;">
	</div>
	<!-- // 查看图片 // left:200px;-->
	<div id="divCenter" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
		<div style="display:inline;">
			<img id="img" src="" style="width:100%;height:100%;"/>
		</div>
	</div>
	<!-- // 查看图片 // -->
	
</c:forEach>
</div>
</div>
</div>
</body>
<!-- // 查看图片 // -->
<script type="text/javascript">
var num = 0;
$("#xuanzhuan").click(function(){
	num+=90;
	$("#divCenter").css('transform','rotate('+num+'deg)');
	
})
$(function(){  
    $(".pimg").click(function(){  
        var _this = $(this);//将当前的pimg元素作为_this传入函数  
        imgShow( _this);  
    });  
});
//判断作文内容、图片是否为空
$(function (){
	var a = $("#img1").val();
	var b = $("#img2").val();
	var c = $("#img3").val();
	if(a.length==0){
		$("#imga").hide();
	}
	if(b.length==0){
		$("#imgb").hide();
	}
	if(c.length==0){
		$("#imgc").hide();
	}
})

function imgShow( _this){  
    var src = _this.attr("src");//获取当前点击的pimg元素中的src属性 
    $("#img").attr("src", src);//设置#bigimg元素的src属性  
    document.getElementById('fade').style.display='block';
    document.getElementById("divCenter").style.display="block";
}
function picClose1() {
    document.getElementById("divCenter").style.display="none";
    document.getElementById('fade').style.display='none';
}
</script>
<!-- // 查看图片 // -->
</html>