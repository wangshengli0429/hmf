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
			<span style="color:#fc0403;font-family:Microsoft YaHei;font-size:40px;">审核不通过</span>
		</b>
	</div>
	<div style="text-align:center;padding-top:60px;">
		<b>
			<span id="cause1" style="color:#333333;font-family:Microsoft YaHei;font-size:20px;">您的预批改审核未通过，请重新点评作文，提交审核！</span><br>
			<span id="cause2" style="color:#333333;font-family:Microsoft YaHei;font-size:20px;">${m}</span>
		</b>
	</div>
	<div id="toupdate" style="text-align:center;padding-top:30px;">
		<input type="button" value="去批改" onclick="toupdate()" style="font-size:24px;background:#ff6c00;color:#ffffff;width:128px;height:50px; " >
	</div>
	
</div>
</body>
<script type="text/javascript">
	var cause = $("#cause2").html();
	if(cause.length>0){
		$("#cause2").html(cause+"，请重新上传！");
	}
	function toupdate(){
		location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=8";
	}
	
</script>
</html>