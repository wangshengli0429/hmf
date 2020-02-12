<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退款</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<style>
td {
 text-align: center;
} 
</style>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<form id="yiclId" action="<%=request.getContextPath()%>/servlet/ThirdPartyServlet?do=3" method="post" class="form-inline" role="form">			<br><br>
	<input type="hidden" id="aa" value="${m}">
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">状态：</label>
	    <select name="state" id="state" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<option value="1" ${state==1?"selected":""}>退款中</option>
	      	<option value="2" ${state==2?"selected":""}>已退款</option>
	      	<option value="3" ${state==3?"selected":""}>拒绝退款</option>
	      	<option value="4" ${state==4?"selected":""}>取消退款</option>
	      	<option value="5" ${state==5?"selected":""}>退款失败</option>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <label class="form-label" for="name">申请时间：</label>
	    <input type="text" name="startDate" id="startDate" value="${startDate}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00'})" style="width:178px;height:19px;" placeholder="开始时间">
	    <input type="text" name="endDate" id="endDate" value="${endDate}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59'})" style="width:178px;height:19px;" placeholder="结束时间">
		<br><br>
		<div style="display:inline;float: right;">
			<input type="text" name="name" id="seachName"  value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="退款编号/退款用户/作文标题">
			<button class="btn btn-info" onclick="selectYicl()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

	<!-- 数据表格 展示 -->
	<table class="table table-hover" style="width: 100%;" id="tb2">
	<thead>
		<tr class="active" align="center">
			<td style="text-align: left;">退款编号</td>
			<td style="text-align: left;">退款用户</td>
			<td>退款金额</td>
			<td style="text-align: left;">所属作文</td>
			<td style="text-align: left;">所属老师</td>
			<td>状态</td>
			<td>审核人账号</td>
			<td>申请时间</td>
			<td>处理时间</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${tklist}" var="t">
		<tr>
			<td name="one" style="text-align: left;">${t.outtradeno}</td>
			<td name="two" style="text-align: left;">${t.sname}</td>
			<td name="three">${t.money}</td>
			<td name="four" style="text-align: left;"><a href="##" onclick="selectTkzw(${t.comid})">${t.title}（${t.draft}）</a></td>
			<td name="five" style="text-align: left;">
				<c:if test="${ empty t.tname}">-</c:if>
				<c:if test="${not empty t.tname}">${t.tname}</c:if>
			</td>
			<td name="six">${t.state==1?"退款中":""}${t.state==2?"已退款":""}${t.state==3?"拒绝退款":""}${t.state==4?"退款失败":""}${t.state==5?"拒绝退款":""}</td>
			<td name="seven">${t.userName}</td>
			<td name="eight">${t.ctime}</td>
			<td name="nine">${t.dtime}</td>
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
	function selectYicl(){
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
		$("#yiclId").submit();
	}

	/* 查看退款作文 */
	function selectTkzw(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=7&id="+id;
	}
	
</script>
<!-- 分页 -->
<script>
	$("#state").change(function name() {
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
			$("#yiclId").submit();
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
			$("#yiclId").submit();
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
			$("#yiclId").submit();
			
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
			$("#yiclId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#yiclId").submit();
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
			$("#yiclId").submit();
		}
	}
</script>
</html>