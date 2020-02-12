<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看范文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${fanwen}" var="f">
<div style="display:inline;">
<span>资源管理>范文>${f.name}</span>
</div>
<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>
	<center>
		<h3><b>${f.name}</b></h3>
		<p>
			作者：${f.author}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${f.ageDetail}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${f.type1}
		</p>
	</center>
	${f.content}<br><br>
	<p align="right">${f.times}</p>
	<p>阅读人数	${f.yuedu}&nbsp;&nbsp;&nbsp;&nbsp;	收藏人数${f.shoucang}</p>
</c:forEach>
</div>
</div>
</div>
</body>
</html>