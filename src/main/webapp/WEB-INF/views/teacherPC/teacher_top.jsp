<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>教师PC端top</title>
<jsp:include page="../inc.jsp"></jsp:include>
</head>
<style>
	body{
		background:#54a7fd;
		height: 100%;
		overflow:hidden;
		font-family:"Microsoft YaHei";
	}
</style>
<body style="height:100%;">

<img src="<%=request.getContextPath()%>/images/logo.png" title="评点教育" style="margin-left:8px;margin-top:8px;float:left; height:74px;width:74px;">
<span style="margin-left:18px;margin-top:24px;float:left; font-size:30px;color:#ffffff;font-family:Microsoft YaHei;">评点教育</span>

<div style="background:#489ef7;margin-top:0px;margin-right:0px;float:right;height:100%;width:130px">
	<a href="javascript:void(0);" onclick="signout()">
		<span style="color:#ffffff;margin-top:40px;margin-right:25px;float:right;font-family:Microsoft YaHei;font-size:18px;">退出</span>
		<img src="<%=request.getContextPath()%>/images/tc.png" title="退出" style="margin-right:20px;margin-top:40px;float:right;">
	</a>
</div>

<span style="color:#ffffff;margin-top:40px;margin-right:15px;float:right;font-family:Microsoft YaHei;font-size:18px;">${tuserName}</span>
<img src="<%=request.getContextPath()%>/images/user.png" title="用户" style="margin-top:40px;margin-right:15px;float:right;">

</body>
<script>
	function signout(){
		window.parent.parent.location.replace("<%=request.getContextPath()%>/servlet/LogoutServlet");
		<%-- top.location.href="<%=request.getContextPath()%>/servlet/LogoutServlet"; --%>
		<%-- top.location.href="<%=request.getContextPath()%>/teacherPC/teacher_login.jsp"; --%>
	}
</script>
</html>