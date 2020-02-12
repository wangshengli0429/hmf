<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>error</title>
<jsp:include page="/inc.jsp"></jsp:include>
</head>
<body style="text-align:center;font-family:Microsoft YaHei;">
<div class="viewFramework-content">
	
	<div class="inline-block" style="text-align:center;padding-top:132px;">
		<b>
			<span style="font-family:Microsoft YaHei;color:#ff3652;font-size:40px;">404！</span>
			<span style="font-family:Microsoft YaHai;color:#333333;font-size:24px">您访问的页面找不到了！</span>
		</b>
	</div>
	<div style="padding-top:42px;">
		<img src="<%=request.getContextPath()%>/images/404.png" style="width:284px;height:174px;">
	</div>
	<div style="text-align:center;padding-top:34px;">
		<button type="button" class="btn btn-info" onclick="homePage()" style="background:#54a7fd;font-family:Microsoft YaHei;text-align:center;font-size:26px;width:160px;height:48px;" data-toggle="button">返回首页</button>
	</div>
</div>
</body>
<script>//history.go(0)
	function homePage(){
		top.location.href="<%=request.getContextPath()%>/teacherPC/zuoWenGuanLi/zwGuanLi_index.jsp";
	}
</script>
</html>