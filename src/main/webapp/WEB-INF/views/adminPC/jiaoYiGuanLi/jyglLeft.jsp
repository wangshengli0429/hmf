<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易管理left</title>
<jsp:include page="../../inc.jsp"></jsp:include>
	<style type="text/css">
      .m{color:#ff9900;}
      .n{color:#000000;}
    </style>
</head>
<body style="font-family:Microsoft YaHei;"> 

	<ul class="list-group"  style="text-align:center;">
	<c:forEach items="${list }" var="l">
		<c:if test="${l.FATHER_ID == '004000'}">
				<li class="list-group-item"><a href="<%=request.getContextPath()%>${l.URL }" onclick="${l.FUNCTION }(this)" id="${l.FUNCTION }Id" target="mainFrame2">${l.NAME }</a></li>
		</c:if>
	</c:forEach>
	  	<%-- <li class="list-group-item"><a href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=17" onclick="yueKa(this)" id="yueKaId" target="mainFrame2">会员卡管理</a></li> --%>
	</ul>
    
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
/* 订单管理   dingDan */
function dingDan(obj){
	var dingDanId = document.getElementById("dingDanId");
	dingDanId.setAttribute("class", "m");
	var tuiKuanId = document.getElementById("tuiKuanId");
	tuiKuanId.setAttribute("class", "n");
	var jieSuanId = document.getElementById("jieSuanId");
	jieSuanId.setAttribute("class", "n");
	var jieSuanId = document.getElementById("yueKaId");
	jieSuanId.setAttribute("class", "n");
}
/* 退款/售后  tuiKuan */
function tuiKuan(obj){
	var dingDanId = document.getElementById("dingDanId");
	dingDanId.setAttribute("class", "n");
	var tuiKuanId = document.getElementById("tuiKuanId");
	tuiKuanId.setAttribute("class", "m");
	var jieSuanId = document.getElementById("jieSuanId");
	jieSuanId.setAttribute("class", "n");
	var jieSuanId = document.getElementById("yueKaId");
	jieSuanId.setAttribute("class", "n");
}
/* 结算管理  jieSuan */
function jieSuan(obj){
	var dingDanId = document.getElementById("dingDanId");
	dingDanId.setAttribute("class", "n");
	var tuiKuanId = document.getElementById("tuiKuanId");
	tuiKuanId.setAttribute("class", "n");
	var jieSuanId = document.getElementById("jieSuanId");
	jieSuanId.setAttribute("class", "m");
	var jieSuanId = document.getElementById("yueKaId");
	jieSuanId.setAttribute("class", "n");
}
/* 月卡管理  yueKa */
function yueKa(obj){
	var dingDanId = document.getElementById("dingDanId");
	dingDanId.setAttribute("class", "n");
	var tuiKuanId = document.getElementById("tuiKuanId");
	tuiKuanId.setAttribute("class", "n");
	var jieSuanId = document.getElementById("jieSuanId");
	jieSuanId.setAttribute("class", "n");
	var jieSuanId = document.getElementById("yueKaId");
	jieSuanId.setAttribute("class", "m");
}
</script>

</html>