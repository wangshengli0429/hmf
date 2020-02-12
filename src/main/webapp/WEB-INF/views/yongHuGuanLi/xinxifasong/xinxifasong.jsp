<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息发送information</title>
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
<input type="hidden" id="xinResult" value="${xinResult}">
<form id="userId"  method="post" class="form-inline" role="form">
	<input type="text" name="ids" id="ids" style="display:none">
	<input type="text" name="inf" id="inf" style="display:none">
	<div class="form-group">
		<label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户类型：</label>
	    <select name="userType" id="userType" class="form-control" style="width:178px">
	      	<option value="student" ${userType == 'student'?'selected':''}>学生</option>
	      	<option value="teacher" ${userType == 'teacher'?'selected':''}>老师</option>
	    </select>
	    <br>
	    <label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;搜索条件：</label>
	    <input type="text" name="name" id="name" value="${name}" class="form-control" style="width:178px;height:19px;margin-top:10px;" placeholder="用户名/手机号" >
		<br>
		<button class="btn btn-info" onclick="selectUser()" style="background:#54a7fd;margin-left:30px;margin-top:5px;" type="button">查询</button>
	</div>
	<div class="form-group" style="display:inline;">
		<div style="display:inline;float: right;">
		  <label>请输入发送消息内容：</label>
		  <button class="btn btn-info" onclick="appTuiSong()" style="background:#54a7fd;" type="button">app推送发送</button>
		  <button class="btn btn-info" onclick="xiaoXiLieBiao()" style="background:#54a7fd;margin-left:10px;" type="button">消息列表发送</button>
		  <br>
		  <textarea rows="2" cols="100" id="information" style="margin-top:10px;"></textarea>
		</div>
	</div>
	<br><br>


<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
<thead>
	<tr class="active" align="center">
		<td style="width:150px;">全选/全不选<input type="checkbox" name="choose" onclick="_chkSelectAll(this.checked)" style="height:15px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px; margin-left:5px;"/></td>
		<td style="width:150px;">用户类型</td>
		<td style="text-align:left">用户名</td>
		<td style="text-align:left">电话</td>
		<td style="text-align:left">学校</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr align="center">
		<td><input name="choose" type="checkbox" value="${l.idAndType}"  /></td>
		<td>${l.userType == 'student'?'学生':'老师'}</td>
		<td style="text-align:left">${l.name}</td>
		<td style="text-align:left">${l.PHONE}</td>
		<td style="text-align:left">${l.school}</td>
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
		var xinResult = $("#xinResult").val();
		if(xinResult != null && xinResult != ""){
			alert(xinResult);
		}
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	/* 发送 */
	function appTuiSong(){
		var userType = $("#userType").find("option:selected").val(); 
		if(userType == 'student'){
			alert("学生客户端尚未开通此功能");
			return false;
		}
		var obj = document.getElementsByName('choose');
		var ids = "";
		var information = $("#information").val();
		if(information == ''){
			alert("请输入消息内容");
			return false;
		}
		if ($(":checkbox[name=choose]:checked").size() == 0) {
			alert("请至少选择一个用户！");
			return false;
		}
		for(var i=0; i<obj.length; i++){ 
			if(obj[i].checked){ 
				ids+=obj[i].value+','} //如果选中，将value添加到变量s中 
		}
		var url = "<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=18&userType=" + userType;
		$("#ids").val(ids);
		$("#inf").val(information);
		$("#userId").attr("action",url);
		$("#userId").submit(); 
	}
	function xiaoXiLieBiao(){
		var userType = $("#userType").find("option:selected").val(); 
		var obj = document.getElementsByName('choose');
		var ids = "";
		var information = $("#information").val();
		if(information == ''){
			alert("请输入消息内容");
			return false;
		}
		if ($(":checkbox[name=choose]:checked").size() == 0) {
			alert("请至少选择一个用户！");
			return false;
		}
		for(var i=0; i<obj.length; i++){ 
			if(obj[i].checked){ 
				ids+=obj[i].value+','} //如果选中，将value添加到变量s中 
		}
		var url = "<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=19&userType=" + userType;
		$("#ids").val(ids);
		$("#inf").val(information);
		$("#userId").attr("action",url);
		$("#userId").submit(); 
	}
</script>

<script>
	
	/* 条件查询 */
	function selectUser(){
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
	
		$("#userId").attr("action","<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=17");
		$("#userId").submit(); 
	}
	
	$("#userType").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#name").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
</script>

<script type="text/javascript">
	function _chkSelectAll(_ischecked){
			var hobbyarr = document.getElementsByName("choose");
			for(var i=0;i<hobbyarr.length;i++){
				hobbyarr[i].checked = _ischecked;
			}
		}

		function _reverseChecked(){
			var hobbyarr = document.getElementsByName("choose");
			for(var i=0;i<hobbyarr.length;i++){
				hobbyarr[i].checked = !hobbyarr[i].checked;
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
			selectUser();
			//$("#userId").submit();
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
			selectUser();
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
			selectUser();
			
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
			selectUser();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		selectUser();
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
			selectUser();
		}
	}
</script>

</html>