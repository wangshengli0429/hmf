<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退款/售后-退款处理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${tklist}" var="t">
<div style="display:inline;">
<span>退款管理>退款已处理，退款编号：${t.ddbh}</span>
</div>
<!-- javascript:fanhui() -->
<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=6" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="tjform" class="form-inline cmxform" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td valign="top">买家申请：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td>申请时间：</td>
					<td>${t.ctime}</td>
				</tr>
				<tr>
					<td>申请金额：</td>
					<td>${t.money}</td>
				</tr>
				<tr>
					<td>所属老师：</td>
					<td>
						<c:if test="${ empty t.tname}">-</c:if>
						<c:if test="${not empty t.tname}">${t.tname}</c:if>
					</td>
				</tr>
				<tr>
					<td>所属作文：</td>
					<td>${t.title}</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top" width="150px">平台退款审核：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td>平台确认：</td>
					<td>${t.state==2?"同意":""}${t.state==3?"不同意":""}</td>
				</tr>
				<tr>
					<td>处理时间：</td>
					<td>${t.dtime}</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top">退款详情：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td>支付方式：</td>
					<td>${t.payment}</td>
				</tr>
				<tr>
					<td>退款金额：</td>
					<td>${t.buyer}</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>

</c:forEach>
</div>
</div>
</div>
</body>
</html>