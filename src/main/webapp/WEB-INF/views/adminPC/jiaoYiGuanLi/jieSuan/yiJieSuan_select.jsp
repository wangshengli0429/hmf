<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看(已结算)</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${yjsInfor}" var="f">
<div style="display:inline;">
<span>结算管理>查看${f.tname}</span>
</div>
<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=10" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<table style="border-collapse:separate; border-spacing:0px 0px;">
	<tr>
		<td valign="top">基本信息：</td>
		<td>
			<table style="border-collapse:separate; border-spacing:0px 12px;">
				<tr>
					<td>老师姓名：</td>
					<td>${f.tname}</td>
				</tr>
				<tr>
					<td>老师账号：</td>
					<td>${f.phone}</td>
				</tr>
				<tr>
					<td>结算状态：</td>
					<td>已结算</td>
				</tr>
				<tr>
					<td>所属年月：</td>
					<td>${f.time}</td>
				</tr>
				<tr>
					<td>操作人员账号：</td>
					<td>${f.userName}</td>
				</tr>
				<tr>
					<td>结算时间：</td>
					<td>${f.clearTime}</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</c:forEach>

<button type="button" onclick="exportJieSuan()" class="btn btn-default glyphicon glyphicon-share-alt" data-toggle="button">&nbsp;导出</button>
<br><br>
<form id="zdform" action="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=12" method="post" class="form-inline" role="form">
	<input type="hidden" id="zdid" value="${id}"/>
	<!-- 数据表格 展示 -->
	<table class="table table-hover">
	<thead>
		<tr class="active">
			<td><input type="checkbox" id="checkAll" onclick="quanxuan()"/></td>
			<td>账单编号</td>
			<td>所属作文</td>
			<td>作者昵称</td>
			<td>收取佣金（元）</td>
			<td>支付金额（元）</td>
			<td>点评时间</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${yjslist}" var="y">
		<tr>
			<td><input type="checkbox" name="checkAll" value="${y.id}"/></td>
			<td>${y.outtradeNo}</td>
			<td>${y.title}</td>
			<td>${y.author}</td>
			<td>${y.total}</td>
			<td>${y.buyerpay}</td>
			<td>${y.comTime}</td>
		</tr>
		</c:forEach>
	</tbody>
	</table>
	<!-- /数据表格 展示 -->
	
	<!-- 分页 -->
	<div class="text-center">
	<ul id="visible-pages-example" class="pagination">
	<li class="first" data-page="1">
	<a href="javascript:homePage()" >首页</a>
	</li>
	<li class="prev" data-page="1">
	<a href="javascript:previous()">上一页</a>
	</li>
	<li class="prev" data-page="10">
	<a>
	当前页<input value="${page.currentPage }" name="currentPage" id="currentPage" size="3" style="height:20px;text-align:center;vertical-align:middle;" onkeydown="javascript:if(event.keyCode==13) toPage();">页
	每页<input value="${page.numPerPage }" name="numPerPage" id="numPerPage" size="3" style="height:20px;text-align:center;vertical-align:middle;" onkeydown="javascript:if(event.keyCode==13) toCount();">条
	总<span id="totalPages">${page.totalPages }</span>页 总${page.totalRows }条
	</a>
	</li>
	<li class="next" data-page="2">
	<a href="javascript:next()">下一页</a>
	</li>
	<li class="last" data-page="35">
	<a href="javascript:last()">末页</a>
	</li>
	</ul>
	</div>
	<!-- /分页 -->
</form>
</div>
</div>
</div>
</body>
<script>
	/* 导出   export */
	function exportJieSuan(){
		var ids ="";
		var array = $('input[name="checkAll"]:checked').each(
			function(){ 
				ids += ","+$(this).val(); 
			}
		); 
		ids=ids.substr(1);
		var zdid = $("#zdid").val();
		if (ids.length>0) {
			location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=16&zdid="+zdid+"&ids="+ids;
		}else {
			alert("请选则需要导出的数据！")
		}
	}
</script>

<!-- 全选  反选 -->
<script>
	function quanxuan(){
		if($("#checkAll").prop("checked")){
			$("input[name='checkAll']").prop("checked", true);
		}else{
			$("input[name='checkAll']").prop("checked", false);
		}
		
	}
</script>
<!-- 分页 -->
<script>
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#zdform").submit();
		}else{
			alert("已是第一页")
		}
	}
	/* 上一页 */
	function previous (){
		var current = $("#currentPage").val();//当前页
		if(current>1){
			var a  = Number(current)-1;
			$("#currentPage").val(a);
			$("#zdform").submit();
		}else{
			alert("已是第一页")
		}
	}
	/* 下一页 */
	function next(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current<total){
			var a  = Number(current)+1;
			$("#currentPage").val(a);
			$("#zdform").submit();
			
		}else{
			alert("已是最后一页");
		}
	}
	/* 末页 */
	function last(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current<total){
			$("#currentPage").val(total);
			$("#zdform").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#zdform").submit();
	}
	/*跳转到指定页码*/
	function toPage(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current>total){
			alert("输入页码超过总页数！")
		}else if(current < 1){
			alert("请您输入正确页码！")
		}else{
			$("#currentPage").val(current);
			$("#zdform").submit();
		}
	}
</script>
</html>