<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>作文管理</title>
</head>
<frameset rows="100px,*" frameborder="1" border="1" framespacing="1" bordercolor="#f1f1f1" id="mainFrame3">
	<frame src="<%=request.getContextPath()%>/teacherPC/teacher_top.jsp" bordercolor="#f1f1f1" noresize/>
	<frameset cols="7%,*" frameborder="1" border="1" framespacing="1" bordercolor="#f1f1f1">
		<frame src="<%=request.getContextPath()%>/teacherPC/zuoWenGuanLi/zwgl_left.jsp" bordercolor="#f1f1f1" noresize/>
		<frameset rows="*,7%" frameborder="1" border="1" framespacing="1" bordercolor="#f1f1f1">
			<frame src="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1" style= "background-color:#f1f1f1" bordercolor="#f1f1f1" name="mainFrame4" id="mainFrame4" title="mainFrame4" noresize>
			<frame src="<%=request.getContextPath()%>/down.jsp" bordercolor="#f1f1f1" noresize/>
		</frameset>
	</frameset>
</frameset>
</html>