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

<c:forEach items="${tkcl}" var="t">
<div style="display:inline;">
<span>退款管理>退款待处理，退款编号：${t.ddbh}</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=2" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="tkform" action="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=5" method="post" class="form-inline cmxform" role="form">
<input type="hidden" name="id" value="${t.id}"/>
<input type="hidden" name="sid" value="${t.sid}"/>
<input type="hidden" name="ddbh" value="${t.ddbh}"/>
<input type="hidden" name="title" value="${t.title}"/>
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td valign="top">买家申请：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td>申请时间：</td>
					<td>${t.time}</td>
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
		<td valign="top" width="150px">订单支付信息：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td>支付方式：</td>
					<td>${t.payment}</td>
				</tr>
				<tr>
					<td>订单总额：</td>
					<td>${t.orderPrice}</td>
				</tr>
				<tr>
					<td>支付金额：</td>
					<td>${t.buyer}</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top">平台审核：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td width="70px">
						<input type="radio" name="state" value="2" style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;">同意
					</td>
					<td width="70px">
						<input type="radio" name="state" value="3" style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;">不同意
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:70px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="tuiKuan()" class="btn btn-default" data-toggle="button" style="width:70px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</form>

</c:forEach>
</div>
</div>
</div>
</body>

<script type="text/javascript">
	/* 确定    tuiKuan */
	function tuiKuan(){
		var state = $("input[name='state']:checked").val();
		if(state==2||state==3){
			$("#tkform").submit();
		}
	}
	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=2";
	}

</script>

</html>