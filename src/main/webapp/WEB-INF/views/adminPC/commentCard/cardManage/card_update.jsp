<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改评点卡</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body>
<div style="width:100%;height:5px;background:#ffffff;"></div>
<center>
	<form id="updateCardId" action="<%=request.getContextPath()%>/servlet/CardManageServlet?do=6" method="post" class="form-inline" role="form">
	<c:forEach items="${list}" var="l">
	<input type="hidden" name="id" value="${l.ID}">
	<table style="border-collapse:separate; border-spacing:0px 10px;">
		<tr>
			<td style="float:right;text-align:center;">评点卡名称：</td>
			<td><input type="text" name="name" value="${l.NAME}" class="form-control" style="width:178px;height:19px;"></td>
		</tr>
		<tr>
			<td style="float:right;">评点卡可使用次数：</td>
			<td>
				<input type="number" name="count" value="${l.COUNT}" class="form-control" style="width:205px;height:30px;display:inline;">
				<%-- <select name="count" class="form-control" style="width:205px;height:30px;">
					<option value="" >请选择</option>
			      	<option value="1" ${l.COUNT==1?"selected":""}>1</option>
			      	<option value="5" ${l.COUNT==5?"selected":""}>5</option>
			      	<option value="10" ${l.COUNT==10?"selected":""}>10</option>
			    </select> --%>次
	    	</td>
		</tr>
		<tr>
			<td style="float:right;">有效期：</td>
			<td>
				<select name="validitytime" class="form-control" style="width:205px;height:30px;">
			      	<option value="" >请选择</option>
			      	<option value="90" ${l.TIME==90?"selected":""}>90天</option>
			      	<option value="180" ${l.TIME==180?"selected":""}>180天</option>
			      	<option value="365" ${l.TIME==365?"selected":""}>365天</option>
			    </select>
			</td>
		</tr>
		<tr>
			<td style="float:right;">现价格：</td>
			<td><input type="number" name="price" value="${l.PRICE}" step="0.01" class="form-control" style="width:205px;height:30px;display:inline;">元</td>
		</tr>
		<tr>
			<td style="float:right;">原价：</td>
			<td><input type="number" name="old_price" value="${l.OLD_PRICE}" step="0.01" class="form-control" style="width:205px;height:30px;display:inline;">元</td>
		</tr>
	</table>
	</c:forEach>
	</form>
</center>
<div style="text-align:right;display:none;">
	<button type="button" class="btn btn-default" data-toggle="button" onclick="cancel()">取消</button>
	<button type="button" class="btn btn-info" style="background:#428bca;" data-toggle="button" onclick="updateCard()">确定</button>
</div>
</body>
<script type="text/javascript">
	/* 确定修改   updateCard */
	function updateCard(){
		$.ajax({
			url:"${pageContext.request.contextPath}/servlet/CardManageServlet?do=6",
			type:"post",
			data:$("#updateCardId").serialize(),
			success:function (data){
				var d = dialog({
					title: '提示',
					width: '300',
					//height: '35',
					content: '<center><h4><b>'+data+'</b></h4></center>',
					okValue: '确 定',
					ok: function () {
						location.href="<%=request.getContextPath()%>/servlet/CardManageServlet?do=2";
					}
				});
				d.showModal();
			},
			error: function (){//reload
				alert("请求失败！");
			}
		});
	}
	/* 取消 cancel  */
	function cancel(){
		location.href="<%=request.getContextPath()%>servlet/CardManageServlet?do=2";
	}
</script>
</html>