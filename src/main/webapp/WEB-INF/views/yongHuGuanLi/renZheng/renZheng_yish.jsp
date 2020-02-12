<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>师资认证-已审核</title>
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
<input type="hidden" id="aa" value="${m}">
<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#a" id="aid" onclick="daishenhe()" style="font-size:17px;color:#b0b0b0;text-decoration:none;">待审核</a>
	</div>
</div>
<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#b" id="bid" onclick="yishenhe()" style="font-size:17px;color:#54a7fd;text-decoration:none;">已审核</a>
	</div>
	<div style="background:#489ef7;margin-top:7px;margin-left:-3px;float:left;height:2px;width:60px;">
	</div>
</div><br><br>


<!-- 已审核-->
<form id="yishId" action="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=8" method="post" class="form-inline" role="form">			<br><br>
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="type">状态：</label>
	    <select name="austate" id="austate" class="form-control" style="width:178px;">
	   		<option value="">全部</option>
	      	<option value="1" ${austate=='1'?'selected':''}>已通过</option>
	      	<option value="2" ${austate=='2'?'selected':''}>未通过</option>
	      	<option value="3" ${austate=='3'?'selected':''}>已禁用</option>
	    </select>
	
		<div style="display:inline;float: right;">
			<input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="用户名/姓名">
			<button class="btn btn-info" onclick="selectYish()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

	<!-- 数据表格 展示 -->
	<table class="table table-hover" style="width: 100%;" id="tb2">
	<thead>
		<tr class="active" align="center">
			<td style="text-align: left;">用户名</td>
			<td style="text-align: left;">姓名</td>
			<td style="text-align: left;">状态</td>
			<td>注册时间</td>
			<td>上传资料时间</td>
			<td>提交预批改作文时间</td>
			<td>完善个人信息时间</td>
			<td>操作</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="l">
		<tr>
			<td style="text-align: left;">${l.userName}</td>
			<td style="text-align: left;">${l.name}</td>
			<td style="text-align: left;">
				<c:if test="${l.state==2||l.austate=='2'}">
					未通过
				</c:if>
				<c:if test="${l.state==1&&l.austate=='1'}">
					通过
				</c:if>
				<c:if test="${l.austate=='3'}">
					已禁用
				</c:if>
			</td>
			<td>${l.createTime}</td>
			<td>${l.uploadTime}</td>
			<td>${l.comTime}</td>
			<td>${l.perfectTime}</td>
			<td>
				<button type="button" onclick="findYsh(${l.id})" class="btn btn-info" style="background:#54a7fd;">查看</button>
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
<script type="text/javascript">
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
	
	/* 已审核 */
	function yishenhe(){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=8";
	}
	
	/* 条件查询 */
	function selectYish(){
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
		$("#yishId").submit();
	}
	
	/* 查看 */
	function findYsh(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=9&id="+id;
	}
	$("#austate").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
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
			$("#yishId").submit();
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
			$("#yishId").submit();
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
			$("#yishId").submit();
			
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
			$("#yishId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#yishId").submit();
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
			$("#yishId").submit();
		}
	}
</script>
</html>