<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看素材e</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${sucai}" var="s">
<div style="display:inline;">
<span>资源管理>素材>${s.name}</span>
</div>
<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=8" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>
	<center>
		<h3><b>${s.name}</b></h3>
		<p>
			${s.ageDetail1}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${s.type1}
		</p>
	</center>
	${s.content}<br><br>
	<p align="right">${s.times}</p>
	<p>阅读人数	${s.yuedu}&nbsp;&nbsp;&nbsp;&nbsp;	收藏人数${s.shoucang}</p>
</c:forEach>
</div>
</div>
</div>
</body>
</html>