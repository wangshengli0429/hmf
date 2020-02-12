<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>老师管理teacher</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<style>
td {
 text-align: center;
} 
</style>
</head>
<body style="font-family:Microsoft YaHei;">

<div style="width:100%;height:5px;background:#ffffff;"></div>
<input type="hidden" id="aa" value="${m}">
<form id="userId" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=34" method="post" class="form-inline" role="form">
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="name" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="用户名/姓名" >
		  <button class="btn btn-info" onclick="selectTeacher()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	<br><br>


<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
<thead>
	<tr class="active" align="center">
		<td>请选择</td>
		<td>用户名</td>
		<td style="text-align:left">姓名</td>
		<td>性别</td>
		<td style="text-align:left">学校</td>
		<td>年级</td>
		<td style="text-align:left">省市</td>
		<td>教龄时间（年）</td>
		<td>中高考阅卷经验</td>
		<td>状态</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr align="center">
		<td><input type="radio" name="teaid" value="${l.id}"></td>
		<td name="one">${l.userName}</td>
		<td name="two" style="text-align:left">${l.name}</td>
		<td name="three">${l.sex}</td>
		<td name="four" style="text-align:left">${l.school}</td>
		<td name="five">${l.grade}</td>
		<td name="six" style="text-align:left">${l.city}</td>
		<td name="seven">${l.eduTime}</td>
		<td name="eight">${l.exper=='0'?"无":""}${l.exper=='1'?"有":""}</td>
		<td name="nine">${l.state=='1'?"正常":""}${l.state=='2'?"休息":""}</td>
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
<!-- 
<button class="btn btn-info" onclick="updateTear()" style="background:#54a7fd;float:right;margin-bottom:0px;" type="button">确定</button>
 -->

</body>

<script>

	/* 条件查询 */
	function selectTeacher(){
		var v = $("#name").val();
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
		$("#userId").submit(); 
	}
	
	$("#name").change(function name() {
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
			$("#userId").submit();
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
			$("#userId").submit();
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
			$("#userId").submit();
			
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
			$("#userId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#userId").submit();
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
			$("#userId").submit();
		}
	}
</script>

</html>