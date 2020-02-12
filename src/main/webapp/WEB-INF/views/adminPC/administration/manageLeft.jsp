<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理left</title>
<jsp:include page="../../inc.jsp"></jsp:include>
	<style type="text/css">
      .m{color:#ff9900;}
      .n{color:#000000;}
    </style>
</head>
<body style="font-family:Microsoft YaHei;"> 
	<ul class="list-group"  style="text-align:center;">
	<c:forEach items="${list }" var="l">
		<c:if test="${l.FATHER_ID == '007000'}">
				<li class="list-group-item"><a href="<%=request.getContextPath()%>${l.URL }" onclick="${l.FUNCTION }(this)" id="${l.FUNCTION }Id" target="mainFrame2">${l.NAME }</a></li>
		</c:if>
	</c:forEach>
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
/* 权限管理   manage */
function manage(obj){
	var manageId = document.getElementById("manageId");
	manageId.setAttribute("class", "m");
}
</script>

</html>