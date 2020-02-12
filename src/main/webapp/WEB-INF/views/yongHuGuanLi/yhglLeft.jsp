<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理left</title>
<jsp:include page="../inc.jsp"></jsp:include>
	<style type="text/css">
      .m{color:#ff9900;}
      .n{color:#000000;}
    </style>
</head>
<body style="font-family:Microsoft YaHei;"> 

	<ul class="list-group"  style="text-align:center;">
	<c:forEach items="${list }" var="l">
		<c:if test="${l.FATHER_ID == '002000'}">
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
/* 老师用户管理  teacher */
function teacher(obj){
	var teacherId = document.getElementById("teacherId");
	teacherId.setAttribute("class", "m");
	var studentId = document.getElementById("studentId");
	studentId.setAttribute("class", "n");
	var renZhengId = document.getElementById("renZhengId");
	renZhengId.setAttribute("class", "n");
	var informationId = document.getElementById("informationId");
	informationId.setAttribute("class", "n");
}

/* 学生用户管理  student */
function student(obj){
	var teacherId = document.getElementById("teacherId");
	teacherId.setAttribute("class", "n");
	var studentId = document.getElementById("studentId");
	studentId.setAttribute("class", "m");
	var renZhengId = document.getElementById("renZhengId");
	renZhengId.setAttribute("class", "n");
	var informationId = document.getElementById("informationId");
	informationId.setAttribute("class", "n");
}

/* 师资认证   renZheng*/
function renZheng(obj){
	var teacherId = document.getElementById("teacherId");
	teacherId.setAttribute("class", "n");
	var studentId = document.getElementById("studentId");
	studentId.setAttribute("class", "n");
	var renZhengId = document.getElementById("renZhengId");
	renZhengId.setAttribute("class", "m");
	var informationId = document.getElementById("informationId");
	informationId.setAttribute("class", "n");
}

/* 信息发送  information*/
function information(obj){
	var teacherId = document.getElementById("teacherId");
	teacherId.setAttribute("class", "n");
	var studentId = document.getElementById("studentId");
	studentId.setAttribute("class", "n");
	var renZhengId = document.getElementById("renZhengId");
	renZhengId.setAttribute("class", "n");
	var informationId = document.getElementById("informationId");
	informationId.setAttribute("class", "m");
}
</script>

</html>