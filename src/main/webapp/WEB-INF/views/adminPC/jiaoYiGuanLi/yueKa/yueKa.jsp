<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>月卡管理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<form id="yueKaId" action="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=17" method="post" class="form-inline" role="form">
	<input type="hidden" id="aa" value="${m}">
	<div class="form-group">
		<label class="form-label" for="name">会员卡类型：</label>
	    <select name="type" id="type" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<option value="1" ${type=="1"?"selected":""}>月卡</option>
	      	<option value="2" ${type=="2"?"selected":""}>年卡</option>
	    </select>
		<label class="form-label" for="name">状态：</label>
	    <select name="failure" id="failure" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<option value="0" ${failure=="0"?"selected":""}>有效</option>
	      	<option value="1" ${failure=="1"?"selected":""}>失效</option>
	    </select>
	</div>
	<br><br>
	
	<div class="form-group" style="display:inline;">
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="订单编号/下单用户">
		  <button class="btn btn-info" onclick="selectYueKa()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover">
<thead>
	<tr class="active">
		<td>订单编号</td>
		<td>下单用户</td>
		<td>会员卡名称</td>
		<td>支付金额</td>
		<td>剩余使用次数</td>
		<td>状态</td>
		<td>支付时间</td>
		<td>过期时间</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr>
		<td>${l.OUT_TRADE_NO}</td>
		<td>${l.NICKNAME}</td>
		<td>${l.card_name}</td>
		<td>${l.card_price}</td>
		<td>${l.usage_count}次</td>
		<td>
			<c:if test="${l.failure==0}">
				有效
			</c:if>
			<c:if test="${l.failure==1}">
				过期
			</c:if>
		</td>
		<td>${l.GMT_PAYMENT}</td>
		<td>${l.END_TIME}</td>
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
	function selectYueKa(){
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
		$("#yueKaId").submit();
	}
</script>
<!-- 分页 -->
<script>
	$("#state").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
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
			$("#yueKaId").submit();
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
			$("#yueKaId").submit();
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
			$("#yueKaId").submit();
			
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
			$("#yueKaId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#yueKaId").submit();
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
			$("#yueKaId").submit();
		}
	}
</script>
</html>