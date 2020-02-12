<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<%-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/console/js/jquery/jquery.min.js"></script>
<link  href="<%=request.getContextPath()%>/console/css/base.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/console/css/index.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/jslib/bootstrap/css/bootstrap.min.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/bootstrap/js/bootstrap.min.js"></script>
 --%>
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

<!-- 老师	 -->
<form id="daishId" method="post" class="form-inline" role="form">
	<input type="hidden" name="ids" id="ids">
	<input type="hidden" name="inf" id="inf">
	<br>
	<div class="form-group">
		<label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;类型：</label>
	    <select name="userType" id="userType" onchange="getUserType(this.value)" class="form-control" style="width:178px">
	      	<option value="teacher" ${userType == 'teacher'?'selected':''}>老师</option>
	      	<option value="student" ${userType == 'student'?'selected':''}>学生</option>
	    </select>
	    <div id="tea" class="form-group" style="display: inline;">
		    <label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;完成状态：</label>
		    <select name="userState" id="userState" class="form-control" style="width:178px">
		      	<option value="">全部</option>
		      	<option value="1" ${userState == '1'?'selected':''}>未提交信息</option>
		      	<option value="2" ${userState == '2'?'selected':''}>未预点评</option>
		    </select>
	    </div>
	    <div id="stu" class="form-group" style="display: none;">
		    <label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年级：</label>
		    <select name="ageDetail" id="ageDetail" class="form-control" style="width:178px">
		      	<option value="">全部</option>
		   		<c:forEach items="${ageDetail}" var="a">
		      	<option value="${a.CODE}" ${a.CODE==grade?'selected':''}>${a.GRADE}</option>
		      	</c:forEach>
		    </select>
	    </div>
	    
		<button class="btn btn-info" onclick="selectUser()" style="background:#54a7fd;margin-left:30px;margin-top:5px;" type="button">查询</button>
	</div>
	
	<div style="margin-top: 10px;">
		<label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;模板：</label>
	    <select name="template" id="template" class="form-control" style="width:178px">
	    	<option value="">请选择模板</option>
	      	<option value="message1" ${template == 'message1'?'selected':''} ${userType == 'student'?'hidden':''}>您已注册一堂作文课，请打开一堂作文课教师端APP1、点击我的--头像--基本信息，将所有信息填写完整，包括头像2、点击我的--师资认证，上传相关证件信息</option>
	      	<option value="message2" ${template == 'message2'?'selected':''} ${userType == 'student'?'hidden':''}>您已提交所有信息，请使用电脑登录https://mobile.pingdianedu.com:8443/pingdian进行预点评作文后提交</option>
	      	<%-- 
	      	<option value="message3" ${template == 'message3'?'selected':''} ${userType == 'teacher'?'hidden':''}>您已注册一堂作文课，请打开一堂作文课学生端APP1、点击我的--头像--基本信息，将所有信息填写完整，包括年级</option>
	      	 --%>
	    </select>
	    
		<button class="btn btn-info" onclick="sendOut()" style="background:#54a7fd;margin-left:30px;margin-top:5px;" type="button">发送</button>
	</div>
	
	<br>
	
	<!-- 老师数据表格 展示 -->
	<c:if test="${userType==null||userType=='teacher'}">
	<table class="table table-hover" style="width: 100%;" id="tb1">
	<thead>
		<tr class="active" align="center">
			<td style="width:130px;">全选/全不选<input type="checkbox" name="choose" onclick="_chkSelectAll(this.checked)" style="height:15px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px; margin-left:5px;"/></td>
			<td style="width:100px;">用户类型</td>
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
			<td><input name="choose" type="checkbox" value="${l.userName}"/></td>
			<td >老师</td>
			<td name="one" style="text-align: left;">${l.userName}</td>
			<td name="two" style="text-align: left;">${l.name}</td>
			<td name="three">${l.createTime}</td>
			<td name="four">${l.uploadTime}</td>
			<td name="five">${l.perfectTime}</td>
			<td name="six">${l.comTime}</td>
			<td style="text-align: content;">
				<button type="button" onclick="findWsh(${l.id})" class="btn btn-info" style="background:#54a7fd;width:55px;">查看</button>
			</td>
		</tr>
		</c:forEach>
	</tbody>
	</table>
	</c:if>
	<!-- 学生数据表格 展示 -->
	<c:if test="${userType!=null&&userType=='student'}">
	<table class="table table-hover" style="width: 100%;">
	<thead>
		<tr class="active" align="center">
			<td style="width:130px;">全选/全不选<input type="checkbox" name="choose" onclick="_chkSelectAll(this.checked)" style="height:15px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px; margin-left:5px;"/></td>
			<td style="width:100px;">用户类型</td>
			<td>用户名</td>
			<td style="text-align:left">昵称</td>
			<td style="text-align:left">姓名</td>
			<td>性别</td>
			<td style="text-align:left">学校</td>
			<td>年级</td>
			<td style="text-align:left">省市</td>
			<td>注册时间</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="l">
		<tr align="center">
			<td><input name="choose" type="checkbox" value="${l.phone}"/></td>
			<td >学生</td>
			<td>${l.phone}</td>
			<td style="text-align:left">${l.userName}</td>
			<td style="text-align:left">${l.name}</td>
			<td >${l.sex}</td>
			<td style="text-align:left">${l.school}</td>
			<td>${l.grade}</td>
			<td style="text-align:left">${l.city}</td>
			<td>${l.createTime}</td>
		</tr>
		</c:forEach>
	</tbody>
	</table>
	</c:if>
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
function getUserType(type){
	if(type=="teacher"){
		document.getElementById("stu").style.display="none";
		document.getElementById("tea").style.display="inline";
	}else if(type=="student"){
		document.getElementById("tea").style.display="none";
		document.getElementById("stu").style.display="inline";
	}
}
	
	$(function(){
		var userType = $('#userType option:selected').val();
		getUserType(userType);
		var template = $('#template option:selected').val();
		if(userType=="student"){
			if(template=="message1"||template=="message2"){
				document.getElementById('template').value="";
			}
		}else if(userType=="teacher"){
			if(template!="message1"&&template!="message2"){
				document.getElementById('template').value="";
			}
		}
		
		var xinResult = $("#xinResult").val();
		if(xinResult != null && xinResult != ""){
			alert(xinResult);
		}
		
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	/* 条件查询 */
	function selectUser(){
   		var url = "<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=20";
		$("#daishId").attr("action",url);
		$("#daishId").submit();
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
<script type="text/javascript">
	/* 全选 */
	function _chkSelectAll(_ischecked){
		var hobbyarr = document.getElementsByName("choose");
		for(var i=0;i<hobbyarr.length;i++){
			hobbyarr[i].checked = _ischecked;
		}
	}
	/* 发送短信 */
	function sendOut(){
		var obj = document.getElementsByName('choose');
		var ids = "";
		var information = $('#template option:selected').val();
		if(information == ''){
			alert("请选择模板！");
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
		//alert("ids："+ids+"；information："+information);
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否向已选择的用户发送短信？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				var url = "<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=21";
				$("#ids").val(ids);
				$("#inf").val(information);
				$("#daishId").attr("action",url);
				$("#daishId").submit();
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
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
			//$("#daishId").submit();
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
			//$("#daishId").submit();
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
			//$("#daishId").submit();
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
			//$("#daishId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		selectUser();
		//$("#daishId").submit();
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
			selectUser();//
			$("#daishId").submit();
		}
	}
</script>
</html>