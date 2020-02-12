<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易管理center</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>

<frameset cols="8%,*" frameborder="yes" border="1" framespacing="1" bordercolor="#d4d4d4" FRAMESPACING="1">
	<frame src="<%=request.getContextPath()%>/adminPC/thirdParty/thirdPartyLeft.jsp" bordercolor="#d4d4d4">
	<c:set var="flag" value="0" />
	<c:forEach items="${list }" var="l" >
		<c:if test="${l.FATHER_ID == '009000' && flag == '0'}">
			<frame src="<%=request.getContextPath()%>${l.URL }" bordercolor="#d4d4d4" name="mainFrame2" id="mainFrame2" title="mainFrame2">
			<c:set var="flag" value="1" />
		</c:if>
	</c:forEach>
</frameset>

</html>