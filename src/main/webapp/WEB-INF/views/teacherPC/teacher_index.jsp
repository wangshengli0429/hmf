<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>教师端</title>
</head>
<frameset rows="100px,*" frameborder="no" border="1" framespacing="1" bordercolor="#f1f1f1" id="mainFrame3">
	<frame src="<%=request.getContextPath()%>/teacherPC/teacher_top.jsp" bordercolor="#f1f1f1" noresize/>
	<frameset rows="*,7%" frameborder="no" border="1" framespacing="1" bordercolor="#f1f1f1">
			<frame src="<%=request.getContextPath()%>/teacherPC/yuPiGai/yuPiGai.jsp" bordercolor="#f1f1f1" noresize/>
			<frame src="<%=request.getContextPath()%>/down.jsp" bordercolor="#f1f1f1"/>
	</frameset>
</frameset>
</html>