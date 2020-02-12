<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>top</title>
<jsp:include page="inc.jsp"></jsp:include>
</head>
<style>
	body{
		background:#54a7fd;
		height: 100%;
		overflow:hidden;
		font-family:Microsoft YaHei;
	}
</style>
<body style="height:100%;">

<img src="<%=request.getContextPath()%>/images/logo.png" title="评点教育" style="margin-left:8px;margin-top:8px;float:left; height:74px;">
<span style="margin-left:18px;margin-top:24px;float:left; font-size:30px;color:#ffffff;font-family:Microsoft YaHei;">评点教育</span>

<div style="background:#489ef7;margin-top:0px;margin-right:0px;float:right;height:100%;width:130px">
	<a href="javascript:void(0);" onclick="signout()">
		<span style="color:#ffffff;margin-top:40px;margin-right:25px;float:right;font-size:18px;">退出</span>
		<img src="<%=request.getContextPath()%>/images/tc.png" title="退出" style="margin-right:20px;margin-top:40px;float:right;">
	</a>
</div>

<a href="javascript:void(0);" onclick="update()" >
<span style="color:#ffffff;margin-top:42px;margin-right:15px;float:right;font-size:15px;">修改密码</span>
</a>

<span style="color:#ffffff;margin-top:40px;margin-right:15px;float:right;font-size:18px;">${userName}</span>
<img src="<%=request.getContextPath()%>/images/user.png" title="用户" style="margin-top:40px;margin-right:15px;float:right;">

<%-- 
<img src="<%=request.getContextPath()%>/images/u60.png" alt="图片" title="评点教育" style="margin-top:10px; margin-left:20px; float:left; height:76px;">
<h3 style="margin-top:35px; margin-left:30px; float:left;"><b>评点教育</b></h3>
<span style="margin-top:70px; margin-right:30px; float:right;">管理员姓名    &nbsp;&nbsp;&nbsp;&nbsp;<a href="#">退出</a></span>
 --%>
</body>
<script>
	function signout(){
		top.location.href="<%=request.getContextPath()%>/servlet/ManageLoginServlet?do=2";
	}
	function update(){
		top.location.href="<%=request.getContextPath()%>/servlet/ManageUpdateServlet?do=1";
	}
</script>
</html>