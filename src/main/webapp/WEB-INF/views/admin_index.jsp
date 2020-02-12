<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理端</title>
<jsp:include page="inc.jsp"></jsp:include>
</head>
<%-- <input type="hidden" value="${list }" id="list" name="list"> --%>
<frameset rows="100px,*" frameborder="yes" border="1" framespacing="1" name="index1" id="index1">
	<frame src="<%=request.getContextPath()%>/top.jsp" bordercolor="#f1f1f1" noresize/>
	<frameset cols="7%,*" frameborder="yes" border="1" framespacing="1">
		<frame src="<%=request.getContextPath()%>/left.jsp" id="leftid" bordercolor="#f1f1f1" noresize>
		<%-- <frame src="<%=request.getContextPath()%>/servlet/ManageLoginServlet?do=3" id="leftid" bordercolor="#f1f1f1" noresize> --%>
		
		<frameset rows="*,7%" frameborder="yes" border="1" framespacing="1" bordercolor="#d4d4d4">
			<frame src="<%=request.getContextPath()%>/center.jsp" style= "background-color:#d4d4d4" bordercolor="#d4d4d4" name="mainFrame" id="mainFrame" title="mainFrame" noresize>
			<frame src="<%=request.getContextPath()%>/down.jsp" bordercolor="#d4d4d4" noresize/>
		</frameset>
	</frameset>
</frameset>
</html>