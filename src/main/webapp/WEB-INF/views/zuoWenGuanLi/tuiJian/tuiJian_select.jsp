<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看推荐作文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<c:forEach items="${tuijian}" var="t">
<div style="display:inline;">
<span>作文管理>作文推荐>${t.name}</span>
</div>
<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<center>
	<h1>${t.name}</h1>
	<p style="color:#b2b2b2;align:center;">
	作者：${t.author} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 年级：${t.ageDetail}
	</p>
</center>
	${t.content}
</c:forEach>
</div>
</div>
</div>
</body>
</html>