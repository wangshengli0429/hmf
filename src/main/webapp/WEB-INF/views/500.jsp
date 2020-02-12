<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>error</title>
<jsp:include page="/inc.jsp"></jsp:include>
</head>
<body style="text-align:center">
<div class="viewFramework-content">
	
	<div class="inline-block" style="text-align:center;padding-top:132px;">
		<b>
			<span style="font-family:MicrosoftYaHai;color:#333333;font-size:24px">服务器忙晕了，休息一下马上回来！</span>
		</b>
	</div>
	<div style="padding-top:22px;">
		<img src="<%=request.getContextPath()%>/images/500.png" style="width:217px;height:214px;">
	</div>
</div>
</body>
</html>