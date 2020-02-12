<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审核不通过</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="text-align:center;font-family:Microsoft YaHei;">
<div class="viewFramework-content">
	
	<div style="padding-top:120px;">
		<img src="<%=request.getContextPath()%>/images/no.png" alt="图片" title="审核不通过">
	</div>
	<div class="inline-block" style="text-align:center;padding-top:30px;">
		<b>
			<span style="color:#fc0403;font-family:Microsoft YaHei;font-size:40px;">未通过</span>
		</b>
	</div>
	
	<div style="text-align:center;padding-top:60px;">
		<b>
			<span id="cause2" style="color:#333333;font-family:Microsoft YaHei;font-size:20px;">试点评已通过，师资认证未通过，请重新上传！</span>
		</b>
	</div>
	<div style="text-align:center;padding-top:30px;">
		<span style="color:#999999;font-family:Microsoft YaHei;font-size:14px;">注：资质认证，预点评作文，基本信息都设置完成才可成为平台点评老师，若有未完成的，去完善吧。</span>
	</div>
	
</div>
</body>
</html>