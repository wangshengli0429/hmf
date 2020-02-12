<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>师资认证-待审核</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/console/js/jquery/jquery.min.js"></script>
<link  href="<%=request.getContextPath()%>/console/css/base.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/console/css/index.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/jslib/bootstrap/css/bootstrap.min.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/bootstrap/js/bootstrap.min.js"></script>

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
<input type="hidden" id="aa" value="${m}">
<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#a" id="aid" onclick="daishenhe()" style="font-size:17px;color:#54a7fd;text-decoration:none;">待审核</a>
	</div>
	<div style="background:#489ef7;margin-top:7px;margin-left:-3px;float:left;height:2px;width:60px;">
	</div>
</div>
<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#b" id="bid" onclick="yishenhe()" style="font-size:17px;color:#b0b0b0;text-decoration:none;">已审核</a>
	</div>
</div><br><br>

<!-- 待审核 -->
<form id="daishId" action="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=5" method="post" class="form-inline" role="form">			<br><br>
	<div class="form-group" style="display:inline;">
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="用户名/姓名">
		  <button class="btn btn-info" onclick="selectDaish()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>
	
	<!-- 数据表格 展示 -->
	<table class="table table-hover" style="width: 100%;" id="tb1">
	<thead>
		<tr class="active" align="center">
			<td style="text-align: left;">用户名</td>
			<td style="text-align: left;">姓名</td>
			<td>注册时间</td>
			<td>上传资料时间</td>
			<td>完善个人信息时间</td>
			<td>提交预批改作文时间</td>
			<td style="width:130px;">操作</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="l">
		<tr>
			<td name="one" style="text-align: left;">${l.userName}</td>
			<td name="two" style="text-align: left;">${l.name}</td>
			<td name="three">${l.createTime}</td>
			<td name="four">${l.uploadTime}</td>
			<td name="six">${l.perfectTime}</td>
			<td name="five">${l.comTime}</td>
			<td style="text-align: left;width: 110px;">
				<button type="button" onclick="findWsh(${l.id})" class="btn btn-info" style="background:#54a7fd;width:52px;">查看</button>
				<button type="button" onclick="shenHe(${l.id})" class="btn btn-info" style="background:#54a7fd;width:52px;">审核</button>
			<c:if test="${l.uploadTime!=null&&l.perfectTime!=null}">
			</c:if>
			</td>
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
<script>
	$(function(){
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	/* 未审核 */
	function daishenhe(){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=5";
	}
	/* 条件查询 */
	function selectDaish(){
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
		$("#daishId").submit();
	}
	
	/* 审核按钮 */
	function shenHe(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=6&id="+id;
	}
	
	/* 已审核 */
	function yishenhe(){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=8";
	}
	
	/* 查看 */
	function findWsh(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=14&id="+id;
	}
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
</script>
<!-- 分页 -->
<script>
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#daishId").submit();
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
			$("#daishId").submit();
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
			$("#daishId").submit();
			
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
			$("#daishId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#daishId").submit();
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
			$("#daishId").submit();
		}
	}
</script>
</html>