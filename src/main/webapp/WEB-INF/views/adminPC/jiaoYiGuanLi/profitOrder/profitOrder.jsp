<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收益订单管理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<form id="dingdanId" action="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=18" method="post" class="form-inline" role="form">
	<input type="hidden" id="aa" value="${m}">
	<div class="form-group">
	    <label class="form-label" for="name">支付时间段：</label>
	    <input type="text" name="startDate" id="startDate" value="${startDate}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00'})" style="width:178px;height:19px;" placeholder="开始时间">
	    <input type="text" name="endDate" id="endDate" value="${endDate}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59'})" style="width:178px;height:19px;" placeholder="结束时间">
	</div>
	<br><br>
	
	<div class="form-group" style="display:inline;">
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="订单编号/下单用户">
		  <button class="btn btn-info" onclick="selectDingdan()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover">
<thead>
	<tr class="active">
		<td>订单编号</td>
		<td>下单用户名称</td>
		<td>下单用户账户</td>
		<td>订单金额</td>
		<td>支付方式</td>
		<td style="text-align:center">支付时间</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr>
		<td>${l.outtradeno}</td>
		<td>${l.name}</td>
		<td>${l.phone}</td>
		<td>${l.total}</td>
		<td>${l.payment}</td>
		<td style="text-align:center">${l.gmtpayment}</td>
	</tr>
	</c:forEach>
</tbody>
</table>
<div id="nodata" style="display:none; width:100%; padding-left:50%;">无数据</div>
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
<script type="text/javascript">
	$(function (){
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	
	/* 条件查询 */
	function selectDingdan(){
		var v = $("#seachName").val();
		
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		var b = re.test(v);
		if(b){
			alert("您输入的搜索条件无效，请重新输入");
			return false;
		}
		var regEx = new RegExp(/^(([^\^\.<>%&',;=?$"':#@!~\]\[{}\\/`\|])*)$/);
   		var result = v.match(regEx);
   		if(result == null){
	        alert("您输入的：" + v + "，含有特殊字符，请重新输入");  
	        return false;  
   		} 
		$("#dingdanId").submit();
	}
</script>
<!-- 分页 -->
<script>
	$("#startDate").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#endDate").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#dingdanId").submit();
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
			$("#dingdanId").submit();
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
			$("#dingdanId").submit();
			
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
			$("#dingdanId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#dingdanId").submit();
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
			$("#dingdanId").submit();
		}
	}
</script>
</html>