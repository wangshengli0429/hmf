<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提交完成</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="text-align:center;font-family:Microsoft YaHei;">
<div class="viewFramework-content">
	
	<div style="padding-top:120px;">
		<img src="<%=request.getContextPath()%>/images/tijiao.png" alt="图片" title="作文提交" style="height:156px;width:156px">
	</div>
	<div class="inline-block" style="text-align:center;padding-top:28px;">
		<b>
			<span style="color:#54ae7b;font-family:Microsoft YaHei;font-size:40px;">审核中</span>
		</b>
	</div>
	<div style="text-align:center;padding-top:60px;">
		<b>
			<span id="cause1" style="color:#333333;font-family:Microsoft YaHei;font-size:20px;">试点评已通过，师资认证审核中，请等候！</span><br>
		</b>
	</div>
	<div style="text-align:center;padding-top:30px;">
		<span style="color:#999999;font-family:Microsoft YaHei;font-size:14px;">注：资质认证，预点评作文，基本信息都设置完成才可成为平台点评老师，若有未完成的，去完善吧。</span>
	</div>
	
</div>
</body>
<script type="text/javascript"> 
/* 禁用浏览器后退按钮 */
$(function() {
	if (window.history && window.history.pushState) {
		$(window).on('popstate', function () {
			window.history.pushState('forward', null, '#');
			window.history.forward(1);
		});
	}
	window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
	window.history.forward(1);
})
</script>
</html>