<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源管理left</title>
<jsp:include page="../inc.jsp"></jsp:include>
	<style type="text/css">
      .m{color:#ff9900;}
      .n{color:#000000;}
    </style>
</head>
<body style="font-family:Microsoft YaHei;">
	<ul class="list-group"  style="text-align:center;">
	<c:forEach items="${list }" var="l">
		<c:if test="${l.FATHER_ID == '001000'}">
				<li class="list-group-item"><a href="<%=request.getContextPath()%>${l.URL }" onclick="${l.FUNCTION }(this)" id="${l.FUNCTION }Id" target="mainFrame2">${l.NAME }</a></li>
		</c:if>
	</c:forEach>
	</ul>
	

<%-- 
	<ul class="list-group"  style="text-align:center;">
		<li class="list-group-item"><a href="<%=request.getContextPath()%>/ziYuanGuanLi/fanWen/fanWen.jsp" onclick="fw(this)" id="fwId" target="mainFrame2">范文</a></li>
	  	<li class="list-group-item"><a href="<%=request.getContextPath()%>/ziYuanGuanLi/suCai/suCai.jsp" onclick="sc(this)" id="scId" target="mainFrame2">素材</a></li>
	  	<li class="list-group-item"><a href="<%=request.getContextPath()%>/ziYuanGuanLi/jiFa/jiFa.jsp" onclick="jf(this)" id="jfId" target="mainFrame2">技法</a></li>
	</ul>
--%>
</body>

<script>
$('a').each(function(){
	$(this).click(function(){
		$(this).css("color","#ff9900");
		$('a').not(this).css("color","#000000");
	})
})
$(function (){
	$("a").css("color","#000000");
	$("a:first").css("color","#ff9900");
})
/* 范文  fw */
function fw(obj){
	var fwId = document.getElementById("fwId");
	fwId.setAttribute("class", "m");
	var scId = document.getElementById("scId");
	scId.setAttribute("class", "n");
	var jfId = document.getElementById("jfId");
	jfId.setAttribute("class", "n");
}
/* 素材  sc */
function sc(obj){
	var scId = document.getElementById("scId");
	scId.setAttribute("class", "m");
	var fwId = document.getElementById("fwId");
	fwId.setAttribute("class", "n");
	var jfId = document.getElementById("jfId");
	jfId.setAttribute("class", "n");
}
/* 技法 jf */
function jf(obj){
	var jfId = document.getElementById("jfId");
	jfId.setAttribute("class", "m");
	var fwId = document.getElementById("fwId");
	fwId.setAttribute("class", "n");
	var scId = document.getElementById("scId");
	scId.setAttribute("class", "n");
}
</script>

</html>