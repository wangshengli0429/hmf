<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>作文管理left</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<style>
	body{
		background:#f1f1f1;
		height: 100%;
		overflow:hidden;
		font-family:"Microsoft YaHei";
	}
</style>
<body style="height:100%;">
<!-- /teacherPC/zuoWenGuanLi/zwgl_center.jsp -->
<a href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1" id="zwid" target="mainFrame4" style="text-decoration:none;">
	<img src="<%=request.getContextPath()%>/images/gl.png" style="margin-top:48px; margin-left:30px;align:centre;width:42px;">
	<div style="margin-top:12px;margin-left:12px;">
	<span style="font-size:20px;color:#54a7fd;">作文管理</span>
	</div>
</a>


<%-- 
<nav class="navbar navbar-default" role="navigation"> 
    <div class="container-fluid">
        <ul class="nav navbar-header"> 
            <li>
				<a href="<%=request.getContextPath()%>/teacherPC/zuoWenGuanLi/zwgl_center.jsp" onclick="zwgl()" id="zwid" target="mainFrame4" class="navbar-brand" style="color:#ff9900;font-size:15px;">作文管理</a>
            </li>
		</ul>
	</div>
</nav>
 --%>
</body>
</html>